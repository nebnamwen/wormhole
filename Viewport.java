import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Viewport extends JPanel implements KeyListener, ActionListener {

    final World world;

    public final Timer timer;
    public Camera camera;
    public long nanotime;

    final int width, height;
    final double x0, y0, radius;
    final Rectangle2D.Double fov;

    private ArrayList<TreeMap<Thing,HashSet<Mo>>> drawqueue;
    Graphics2D graphics;

    private HashSet<Integer> keyspressed;

    public Viewport(int width, int height, World world) {
	this.world = world;
	this.timer = new Timer(world.timing, this);
	this.keyspressed = new HashSet<Integer>();

	this.width = width;
	this.height = height;
	this.x0 = width * 0.5;
	this.y0 = height * 0.5;
	this.radius = Math.sqrt(x0*x0 + y0*y0);
	this.fov = new Rectangle2D.Double(0,0,width,height);

	setPreferredSize(new Dimension(width, height));
	setFocusable(true);
	setFocusTraversalKeysEnabled(false);
	addKeyListener(this);
    }

    public void start() {
	nanotime = System.nanoTime();
	timer.start();
    }

    // implements ActionListener

    public void actionPerformed(ActionEvent e) {
	long newnanotime = System.nanoTime();
        long delta = newnanotime - nanotime;
        double dt = delta * 0.000000001;
        nanotime = newnanotime;

	world.update(dt);

	repaint();
    }

    // implements KeyListener
    public void keyTyped (KeyEvent e) { }

    public void keyPressed (KeyEvent e) {
	markKey(e.getKeyCode());
    }

    public void keyReleased (KeyEvent e) {
	clearKey(e.getKeyCode());
    }

    public void markKey (int keycode) {
	keyspressed.add(new Integer(keycode));
    }

    public void clearKey (int keycode) {
	keyspressed.remove(new Integer(keycode));
    }

    public boolean checkKey (int keycode) {
	return keyspressed.contains(new Integer(keycode));
    }

    // painting methods

    public void fill(Paint color) {
	graphics.setPaint(color);
	graphics.fill(fov);
    }

    public Area getCircleShape(Mo T) {
	Circle circle = new Circle(T);

	// circle is too big to accurately draw as an ellipse,
	// approximate it as a line or parabola
	if (circle.b.isInfinite() || circle.b.sub(circle.a).norm() > radius * 8 ) {
	    Cx normal = circle.c.sub(circle.a).unit();
	    double length = circle.a.norm()+radius;
	    Cx edge = normal.mult(length);

	    double depth;
	    int count;
	    if (circle.b.isInfinite()) {
		depth = 0;
		count = 1;
	    }
	    else {
		depth = length*length/circle.b.sub(circle.a).norm();
		count = 2*(int)Math.sqrt(depth)+1;
		if (circle.a.sub(circle.c).mult(circle.b.sub(circle.c).conj()).r > 0) {
		    depth = -depth;
		}
	    }

	    ArrayList<Cx> points = new ArrayList<Cx>();
	    points.add(circle.a.add(new Cx(1,-1).mult(edge)));
	    for (int i = -count; i <= count; i++) {
		points.add(circle.a.add(normal.mult(new Cx(depth*i*i/(count*count), length*i/count))));
	    }
	    points.add(circle.a.add(new Cx(1,1).mult(edge)));

	    Path2D.Double path = new Path2D.Double();

	    boolean started = false;
	    for (Cx point : points) {
		double x = x0+point.i;
		double y = y0-point.r;
		if (!started) {
		    started = true;
		    path.moveTo(x, y);
		}
		else {
		    path.lineTo(x, y);
		}
	    }

	    path.closePath();
	    return new Area(path);
	}
	else {
	    Cx center = circle.a.add(circle.b).mult(0.5);
	    double r = center.sub(circle.a).norm();
	    Ellipse2D.Double ellipse =
		new Ellipse2D.Double(x0+center.i-r, y0-center.r-r, r*2, r*2);
	    // inside-out circle
	    if (circle.c.isInfinite() ||
		circle.a.sub(circle.c).mult(circle.b.sub(circle.c).conj()).r > 0) {
		Area area = new Area(fov);
		Area hole = new Area(ellipse);
		area.subtract(hole);
		return area;
	    }
	    // right-side-out circle
	    else {
		return new Area(ellipse);
	    }
	}	
    }

    public Area getPatchShape(Mo T, Mo[] patch) {
	Area area = null;

	for (Mo curve : patch) {
	    Area curvearea = getCircleShape(T.comp(curve));
	    if (area ==  null) {
		area = curvearea;
	    }
	    else {
		area.intersect(curvearea);
	    }
	}

	if (area == null) {
	    area = new Area();
	}

	return area;
    }

    public boolean isDotOnScreen(Mo T, double ds) {
	Area area = new Area(fov);
	Area dot = getCircleShape(T);
	area.intersect(dot);
	Rectangle2D rec = area.getBounds2D();
	if (area.isEmpty()) { return false; }
	if (rec.getWidth() < ds && rec.getHeight() < ds) { return false; }
	return true;
    }

    public void drawDot(Mo T, Paint color) {
	Area shape = getCircleShape(T);
	graphics.setPaint(color);
	graphics.fill(shape);
    }

    public void drawPatch(Mo T, Patch patch) {
	Area shape = getPatchShape(T, patch.shape);
	graphics.setPaint(patch.color);
	graphics.fill(shape);
    }

    public void drawSprite(Mo T, Sprite sprite) {
	for (Patch patch : sprite.patches) {
	    drawPatch(T, patch);
	}
    }

    public void queueThingForPainting(Thing thing, Mo T) {
	Sprite sprite = thing.getSprite();
	TreeMap<Thing, HashSet<Mo>> layer = drawqueue.get(sprite.layer);
	if (layer.get(thing) == null) {
	    layer.put(thing, new HashSet<Mo>());
	}
	layer.get(thing).add(T);
    }

    public void paintComponent(Graphics g) {
        graphics = (Graphics2D)g;
	graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	Space space = camera.getSpace();
	Mo T = camera.getT();

	space.paintBG(this, T);

	// initialize the draw queue
	drawqueue = new ArrayList<TreeMap<Thing,HashSet<Mo>>>();
	for (int i = 0; i < 256; i++) {
	    drawqueue.add(new TreeMap<Thing,HashSet<Mo>>());
	}

	space.queueThingsForPainting(this, T, null, true);

	for (int i = 0; i < 256; i++) {
	    TreeMap<Thing,HashSet<Mo>> layer = drawqueue.get(i);
	    for (Thing thing : layer.keySet()) {
		for (Mo transform : layer.get(thing)) {
		    drawSprite(transform, thing.getSprite());
		}
	    }
	}
    }

}

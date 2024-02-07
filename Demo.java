import javax.swing.*;
import java.awt.*;

public class Demo extends World {

    public Cursor cursor;

    public Demo() {
	super();

	// a blue plane
	Space s1 = new Space(this, Color.blue.darker());

	// the player's cursor, at the origin
	cursor = new Cursor(s1, Mo.IDENTITY, null);

	// a spot of color at a great distance to reproduce a bug
	s1.addChild(Mo.Translate(50000,0), Color.orange);
	
	// a colorful mobius strip
	Mo w1t = Mo.Translate(3,0);
	BGDomain ring = s1.addChild(w1t.comp(Mo.Scale(1.08)), Color.yellow);
	ring = ring.addChild(w1t.comp(Mo.Scale(1.05)), Color.red);
	ring = ring.addChild(w1t.comp(Mo.Scale(1.02)), Color.green);
	new Wormhole(s1, ring, w1t, 1).join(null);

	// a plain wormhole to another plane
	Space s2 = new Space(this, Color.green.darker().darker());
	new Wormhole(s1, null, Mo.Translate(3,3), 1).join(new Wormhole(s2, null, Mo.IDENTITY, 1));

	// an ordinary sphere
	Space s3 = new Sphere(this, Color.magenta.darker().darker(), 1.5);
	new Wormhole(s1, null, Mo.Translate(-3,-3), 1).join(new Wormhole(s3, null, Mo.IDENTITY, 1));

	// a projective plane (sphere with cross-cap)
	double s4r = 1.2;
	Space s4 = new Sphere(this, Color.cyan.darker(), s4r);
	new Wormhole(s4, null, Mo.INSIDEOUT, s4r*2).join(null);
	new Wormhole(s1, null, Mo.Translate(0,3), 1).join(new Wormhole(s4, null, Mo.IDENTITY, 0.7));

	// a pseudosphere
	Space s5 = new Pseudosphere(this, Color.red.darker(), Color.black, 3);
	new Wormhole(s1, null, Mo.Translate(-3,-0), 1).join(new Wormhole(s5, null, Mo.IDENTITY, 1));

	// a gasket with lots of wormholes
	Space s6 = new Sphere(this, Color.pink, 1.5);
	new Wormhole(s6, null, Mo.Translate(4,0), 1).join(new Wormhole(s6, null, Mo.Translate(-4,0), 1));
	new Wormhole(s6, null, Mo.Translate(0,4), 1).join(new Wormhole(s6, null, Mo.Translate(0,-4), 1));
	new Wormhole(s1, null, Mo.Translate(0,-3), 1).join(new Wormhole(s6, null, Mo.IDENTITY, 1));

	// a tube
	double s7r = 0.4;
	double w7rd = 0.77;
	Wormhole w7 = new Wormhole(s1, null, Mo.Translate(3,-3), 1);
	for (int i = 0; i < 5; i++) {
	    for (Color color: new Color[] { Color.magenta.darker().darker(), Color.green.darker().darker() }) {
		Space s7 = new Sphere(this, color, s7r);
		w7.join(new Wormhole(s7, null, Mo.INSIDEOUT, s7r*w7rd*2));
		w7 = new Wormhole(s7, null, Mo.IDENTITY, s7r*w7rd*2);
	    }
	}
	w7.join(new Wormhole(s1, null, Mo.Translate(-3,3), 1));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Demo world = new Demo();

		    Viewport viewport = new Viewport(800, 640, world);
		    world.cursor.attach(viewport);
                    viewport.camera = new Camera(world.cursor, Mo.Scale(100).comp(Mo.Translate(-1.5,0)));

                    JFrame f = new JFrame("Wormhole");
                    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    f.add(viewport);
                    f.pack();
                    f.setResizable(false);
                    f.setVisible(true);

                    viewport.start();
                }
            });
    }

}

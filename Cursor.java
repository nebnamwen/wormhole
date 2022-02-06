import java.util.EnumSet;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class Cursor extends Thing {

    public final static Sprite sprite;
    public Viewport viewport;

    static {
	sprite = new Sprite(5, new Patch[] {
		// ring
		new Patch(Color.black, new Mo[] {
			Mo.Scale(0.6),
			Mo.Scale(0.5).comp(Mo.INSIDEOUT)
		    }),
		// arrow
		new Patch(Color.black, new Mo[] {
			Mo.Scale(0.65).comp(Mo.INSIDEOUT),
			Mo.Translate(0.25,0.35).comp(Mo.Scale(0.75)),
			Mo.Translate(0.25,-0.35).comp(Mo.Scale(0.75))
		    }),
		// green light to starboard
		new Patch(Color.black, new Mo[] {
			Mo.Translate(0.5,0.3).comp(Mo.Scale(0.15))
		    }),
		new Patch(Color.green, new Mo[] {
			Mo.Translate(0.5,0.3).comp(Mo.Scale(0.11))
		    }),
		// red light to port
		new Patch(Color.black, new Mo[] {
			Mo.Translate(0.5,-0.3).comp(Mo.Scale(0.15))
		    }),
		new Patch(Color.red, new Mo[] {
			Mo.Translate(0.5,-0.3).comp(Mo.Scale(0.11))
		    }),
	    });
    }

    public Cursor(Space space, Mo position, Viewport viewport) {
	super(space, position, 1);
	this.viewport = viewport;
    }

    public void attach(Viewport viewport) {
	this.viewport = viewport;
    }

    public EnumSet<Thing.Attribute> attributes() {
	return EnumSet.of(Thing.Attribute.VISIBLE, Thing.Attribute.DYNAMIC);
    }

    public Sprite getSprite() {
	return sprite;
    }

    public void update(double dt) {
	// move
	double delta = dt;
	if (viewport.checkKey(KeyEvent.VK_SHIFT)) {
	    delta *= 2;
	}

        if (viewport.checkKey(KeyEvent.VK_UP)) {
            position = position.comp(Mo.Translate(delta,0));
        }
        if (viewport.checkKey(KeyEvent.VK_DOWN)) {
            position = position.comp(Mo.Translate(-delta,0));
        }
        if (viewport.checkKey(KeyEvent.VK_RIGHT)) {
            position = position.comp(Mo.Rotate(delta));
        }
        if (viewport.checkKey(KeyEvent.VK_LEFT)) {
            position = position.comp(Mo.Rotate(-delta));
        }

	// spawn things
        if (viewport.checkKey(KeyEvent.VK_F)) {
	    new Flower(space, position);
	    viewport.clearKey(KeyEvent.VK_F);
        }
        if (viewport.checkKey(KeyEvent.VK_D)) {
	    new Dart(space, position);
	    viewport.clearKey(KeyEvent.VK_D);
        }
        if (viewport.checkKey(KeyEvent.VK_G)) {
	    new Ghost(space, position);
	    viewport.clearKey(KeyEvent.VK_G);
        }

	// quit
        if (viewport.checkKey(KeyEvent.VK_Q)) {
	    System.exit(0);
	}

	super.update(dt);
    }
}
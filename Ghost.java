import java.util.EnumSet;
import java.awt.event.KeyEvent;
import java.awt.Color;

public class Ghost extends Thing {

    public final static Sprite sprite;

    static {
	sprite = new Sprite(2, new Patch[] {
		// ring
		new Patch(Color.gray, new Mo[] {
			Mo.Scale(0.6),
			Mo.Scale(0.5).comp(Mo.INSIDEOUT)
		    }),
		// arrow
		new Patch(Color.gray, new Mo[] {
			Mo.Scale(0.65).comp(Mo.INSIDEOUT),
			Mo.Translate(0.25,0.35).comp(Mo.Scale(0.75)),
			Mo.Translate(0.25,-0.35).comp(Mo.Scale(0.75))
		    }),
		// green light to starboard
		new Patch(Color.gray, new Mo[] {
			Mo.Translate(0.5,0.3).comp(Mo.Scale(0.15))
		    }),
		new Patch(Color.green, new Mo[] {
			Mo.Translate(0.5,0.3).comp(Mo.Scale(0.11))
		    }),
		// red light to port
		new Patch(Color.gray, new Mo[] {
			Mo.Translate(0.5,-0.3).comp(Mo.Scale(0.15))
		    }),
		new Patch(Color.red, new Mo[] {
			Mo.Translate(0.5,-0.3).comp(Mo.Scale(0.11))
		    }),
	    });
    }

    public Ghost(Space space, Mo position) {
	super(space, position, 1);
    }

    public EnumSet<Thing.Attribute> attributes() {
	return EnumSet.of(Thing.Attribute.VISIBLE);
    }

    public Sprite getSprite() {
	return sprite;
    }

}
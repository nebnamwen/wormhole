import java.awt.Color;
import java.util.EnumSet;

public class Flower extends Thing {

    public final static Sprite sprite;

    static {
	Patch[] patches = new Patch[6];
	for (int i = 0; i < 5; i++) {
	    patches[i] = new Patch(Color.orange, new Mo[] {
		    Mo.Rotate(Math.PI * 2 * i / 5).comp(Mo.Translate(0.65,0)).comp(Mo.Scale(0.35)),
		    Mo.Rotate(Math.PI * 2 * i / 5).comp(Mo.Translate(1,0)).comp(Mo.Scale(0.2)).comp(Mo.INSIDEOUT)
		});
	}
	patches[5] = new Patch(Color.yellow, new Mo[] { Mo.Scale(0.4) });
	sprite = new Sprite(1, patches);
    }

    public Flower(Space space, Mo position) {
	super(space, position, 0.5);
    }

    public EnumSet<Thing.Attribute> attributes() {
	return EnumSet.of(Thing.Attribute.VISIBLE);
    }

    public Sprite getSprite() {
	return sprite;
    }
}
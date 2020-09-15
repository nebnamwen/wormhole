import java.util.EnumSet;
import java.awt.Color;

public class Dart extends Thing {

    public final static Sprite sprite;
    double time = 0;

    static {
	sprite = new Sprite(3, new Patch[] {
		new Patch(Color.darkGray, new Mo[] {
			Mo.Translate(-1,2).comp(Mo.Scale(Math.sqrt(8))),
			Mo.Translate(-1,-2).comp(Mo.Scale(Math.sqrt(8))),
			Mo.Translate(-3,0).comp(Mo.Scale(2.5)).comp(Mo.INSIDEOUT)
		    }),
		new Patch(Color.gray, new Mo[] {
			Mo.Translate(-1,2).comp(Mo.Scale(Math.sqrt(8)-0.05)),
			Mo.Translate(-1,-2).comp(Mo.Scale(Math.sqrt(8)-0.05)),
			Mo.Translate(-3,0).comp(Mo.Scale(2.5+0.05)).comp(Mo.INSIDEOUT)
		    })
	    });
    }

    public Dart(Space space, Mo position) {
	super(space, position, 0.5);
    }

    public EnumSet<Thing.Attribute> attributes() {
	return EnumSet.of(Thing.Attribute.VISIBLE, Thing.Attribute.DYNAMIC);
    }

    public Sprite getSprite() {
	return sprite;
    }

    public void update(double dt) {
	position = position.comp(Mo.Translate(dt*10,0));
	super.update(dt);
	time += dt;
	if (time > 3) { space.removeThing(this); }
    }
}
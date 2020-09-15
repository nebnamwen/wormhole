import java.util.EnumSet;

public class Thing implements Comparable<Thing> {
    public enum Attribute { VISIBLE, DYNAMIC, SOLID }

    protected static long counter = 0;

    public Space space;
    public Mo position;
    public double radius;
    public long id = counter++;

    public EnumSet<Attribute> attributes() {
	return EnumSet.noneOf(Attribute.class);
    }

    public Thing(Space space, Mo position, double radius) {
	this.space = space;
	this.position = position;
	this.radius = radius;
	space.addThing(this);
	fallThrough();
	align();
    }

    public int compareTo(Thing other) {
	return (int)Long.signum(id - other.id);
    }

    public void fallThrough() {
	fallThrough(null);
    }

    public void fallThrough(Wormhole from) {
	while (true) {
	    Wormhole to = null;
	    for (Wormhole hole : space.wormholes) {
		if (hole != from &&
		    new Circle(hole.rim.inv().comp(position)).c.norm2() < 1) {
		    to = hole;
		}
	    }
	    if (to == null) { break; }

	    space.removeThing(this);
	    space = to.otherside.space;
	    position = to.transform.comp(position);
	    space.addThing(this);
	}
    }

    public void align() {
	position = position.comp(space.collectedMetric(position).correction());
    }

    public void update(double dt) {
	fallThrough();
	align();
    }

    public Mo rim() {
	return position.comp(Mo.Scale(radius));
    }

    public Sprite getSprite() {
	return null;
    }

}

import java.awt.Color;

public class Wormhole extends AbstractDomain {

    public Space space;
    public Mo position, outerrim, transform;
    public double radius;
    public Wormhole otherside;

    public Wormhole(Space space, BGDomain parent, Mo position, double radius) {
	this.space = space;
	if (parent == null) {
	    parent = space;
	}
	this.parent = parent;
	this.position = position.comp(space.metric(position).correction());
	this.rim = this.position.comp(Mo.Scale(radius));
	this.outerrim = this.position.comp(Mo.Scale(space.outerRim(radius)));
	parent.children.add(this);
	space.wormholes.add(this);
    }

    public void join(Wormhole other) {
	if (other == null) {
	    other = this;
	}
	if (otherside == null && (other.otherside == null || other.otherside == this)) {
	    otherside = other;
	    boolean flip = (other == this);
	    double orient = (other == this) ? -1.0 : 1.0;
	    transform = other.rim.comp(new Mo(Cx.ZERO, new Cx(orient), Cx.ONE, Cx.ZERO, flip)).comp(rim.inv());

	    if (other.otherside == null) {
		other.join(this);
	    }
	}
    }

    public void paintBG(Viewport V, Mo T) {
	Mo visiblerim = T.comp(this.rim);
	if (V.isDotOnScreen(visiblerim, 0.5)) {
	    if (otherside != null) {
		otherside.parent.paintBG(V, T.comp(otherside.transform), otherside);
	    }
	    else {
		V.drawDot(visiblerim, Color.black);
	    }
	}
    }
}

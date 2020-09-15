import java.awt.Paint;
import java.util.Collection;
import java.util.HashSet;

public class BGDomain extends AbstractDomain {
    Paint color;
    HashSet<AbstractDomain> children;

    public BGDomain(BGDomain parent, Mo rim, Paint color) {
	this.parent = parent;
	this.rim = rim;
	this.color = color;
	this.children = new HashSet<AbstractDomain>();
    }

    public BGDomain addChild(Mo rim, Paint color, Collection<AbstractDomain> children) {
	BGDomain child = new BGDomain(this, rim, color);
	this.children.removeAll(children);
	child.children.addAll(children);
	this.children.add(child);
	return child;
    }

    public BGDomain addChild(Mo rim, Paint color) {
	return addChild(rim, color, new HashSet<AbstractDomain>());
    }

    public void paintBG(Viewport V, Mo T) {
	paintBG(V, T, null);
    }

    public void paintBG(Viewport V, Mo T, AbstractDomain fromchild) {
	Mo rim = null;
	if (fromchild != null) {
	    rim = T.comp(fromchild.rim).comp(Mo.INSIDEOUT);
	}
	else if (this.rim != null) {
	    rim = T.comp(this.rim);
	}

	boolean visible = true;

	if (rim != null) {
	    if (V.isDotOnScreen(rim, 0.5)) {
		V.drawDot(rim, color);
	    }
	    else {
		visible = false;
	    }
	}
	else {
	    V.fill(color);
	}

	if (visible) {
	    for (AbstractDomain child : children) {
		if (child != fromchild) {
		    child.paintBG(V, T);
		}
		else if (parent != null) {
		    parent.paintBG(V, T, this);
		}
	    }
	}
    }
}

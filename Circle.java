public class Circle {
    public final Cx a, b, c;

    public Circle(Mo mo) {
	Cx x = Cx.ONE;
	if (mo.c.norm2() != 0 && mo.d.norm2() != 0) {
	    x = mo.d.div(mo.c).unit();
	}
	a = mo.apply(x);
	b = mo.apply(x.mult(-1));
	c = mo.apply(Cx.ZERO);
    }

    // will have to actually implement this for collision detection
    public Circle align() {
	return this;
    }
}
public class Mo {

    public static final Mo IDENTITY = new Mo(Cx.ONE, Cx.ZERO, Cx.ZERO, Cx.ONE);
    public static final Mo INSIDEOUT = new Mo(Cx.ZERO, Cx.ONE, Cx.ONE, Cx.ZERO);

    public final Cx a, b, c, d;
    public final boolean flip;

    public static Mo Scale(double s) {
	return new Mo(new Cx(s), Cx.ZERO, Cx.ZERO, Cx.ONE);
    }

    public static Mo Rotate(double s) {
	return new Mo(new Cx(Math.cos(s), Math.sin(s)), Cx.ZERO, Cx.ZERO, Cx.ONE);
    }

    public static Mo Translate(double y, double x) {
	return new Mo(Cx.ONE, new Cx(y, x), Cx.ZERO, Cx.ONE);
    }

    public Mo(Cx a, Cx b, Cx c, Cx d) {
	this(a, b, c, d, false);
    }

    public String toString() {
	String format = "(%s %s / %s %s)";
	if (flip) { format = "~" + format; }
	return String.format(format, a, b, c, d);
    }

    public Mo(Cx a, Cx b, Cx c, Cx d, boolean flip) {
	double det = a.mult(d).sub(b.mult(c)).norm();
	if (det < 0.5 || det > 2) {
	    double sqrtdet = Math.sqrt(det);
	    a = a.div(sqrtdet);
	    b = b.div(sqrtdet);
	    c = c.div(sqrtdet);
	    d = d.div(sqrtdet);
	}
	this.a = a;
	this.b = b;
	this.c = c;
	this.d = d;
	this.flip = flip;
    }

    public Mo conj() {
	return new Mo(a.conj(), b.conj(), c.conj(), d.conj(), !flip);
    }

    public Mo conj(boolean flip) {
	if (flip) {
            return this.conj();
	}
        else {
            return this;
	}
    }

    public Mo comp(Mo other) {
	other = other.conj(flip);
	return new Mo (a.mult(other.a).add(b.mult(other.c)),
		       a.mult(other.b).add(b.mult(other.d)),
		       c.mult(other.a).add(d.mult(other.c)),
		       c.mult(other.b).add(d.mult(other.d)),
		       other.flip);
    }

    public Mo inv() {
	Mo it = this.conj(flip);
	return new Mo(it.d, it.b.mult(-1), it.c.mult(-1), it.a, flip);
    }

    public Cx apply(Cx x) {
	return a.mult(x).add(b).div(c.mult(x).add(d));
    }

}

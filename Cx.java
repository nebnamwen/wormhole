public class Cx {

    public static final Cx ZERO = new Cx(0);
    public static final Cx ONE = new Cx(1);

    public final double r, i;

    public Cx(double r) {
	this(r, 0);
    }

    public Cx(double r, double i) {
	double sum = r+i;
	if (Double.isNaN(sum) || Double.isInfinite(sum)) {
	    r = Double.POSITIVE_INFINITY;
	    i = Double.POSITIVE_INFINITY;
	}
	this.r = r;
	this.i = i;
    }

    public String toString() {
	return String.format("%.2f+%.2fi", r, i);
    }

    public boolean isInfinite() {
	return Double.isInfinite(r);
    }

    public Cx add(Cx other) {
	return new Cx(r + other.r, i + other.i);
    }

    public Cx sub(Cx other) {
	return new Cx(r - other.r, i - other.i);
    }

    public Cx mult(double scale) {
	return new Cx(r * scale, i * scale);
    }

    public Cx mult(Cx other) {
	return new Cx(r * other.r - i * other.i,
		      i * other.r + r * other.i);
    }

    public Cx conj() {
	return new Cx(r, -i);
    }

    public Cx conj(boolean flip) {
	if (flip) {
	    return this.conj();
	}
	else {
	    return this;
	}
    }

    public double norm2() {
	return r * r + i * i;
    }

    public double norm() {
	return Math.sqrt(norm2());
    }

    public Cx unit() {
	double n = norm();
	return new Cx(r / n, i / n);
    }

    public Cx div(double scale) {
	if (Double.isInfinite(scale)) { return ZERO; }
	return new Cx(r / scale, i / scale);
    }

    public Cx div(Cx other) {
	if (Double.isInfinite(other.r)) { return ZERO; }
	return this.mult(other.conj()).div(other.norm2());
    }
}

public class Metric {
    public final double ds;
    public final Cx dds;

    public Metric(double ds, Cx dds) {
	this.ds = ds;
	this.dds = dds;
    }

    public Metric(Mo mo) {
	double ds = mo.a.div(mo.d).sub(mo.b.mult(mo.c).div(mo.d.mult(mo.d))).norm();
	Cx dds = mo.c.div(mo.d).mult(-2*ds);
	if (mo.flip) { dds = dds.conj(); } 
	this.ds = ds;
	this.dds = dds;
    }

    public Mo correction() {
	return new Mo(new Cx(1/ds), Cx.ZERO, dds.div(ds*ds*2), Cx.ONE);
    }

    public static Metric pythagoreanSum(Iterable<Metric> metrics) {
	double ds = 0;
	Cx dds = Cx.ZERO;
	for (Metric m : metrics) {
	    ds += m.ds*m.ds;
	    dds = dds.add(m.dds.mult(m.ds));
	}
	if (!Double.isInfinite(ds)) {
	    ds = Math.sqrt(ds);
	}
	dds = dds.div(ds);
	return new Metric(ds, dds);
    }

    public static Metric harmonicSum(Iterable<Metric> metrics) {
	double ds = 0;
	Cx dds = Cx.ZERO;
	for (Metric m : metrics) {
	    ds += 1/m.ds;
	    dds = dds.add(m.dds.div(m.ds*m.ds));
	}
	ds = 1/ds;
	dds = dds.mult(ds*ds);
	return new Metric(ds, dds);
    }

    public static Metric harmonicDifference(Metric a, Metric b) {
	double ds = 1/(1/a.ds - 1/b.ds);
	Cx dds = (a.dds.div(a.ds*a.ds)).sub(b.dds.div(b.ds*b.ds)).mult(ds*ds);
	if (ds < 0) {
	    ds *= -1;
	    dds = dds.mult(-1);
	}
	return new Metric(ds, dds);
    }

}

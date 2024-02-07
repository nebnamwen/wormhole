import java.awt.Paint;

public class Pseudosphere extends Space {
    public double radius;
    private Mo inversion;

    public Pseudosphere(World world, Paint color, double radius) {
	super(world, color);
	this.radius = radius;
	inversion = Mo.Scale(radius*radius).comp(Mo.INSIDEOUT);
    }

    public Pseudosphere(World world, Paint color, Paint outside, double radius) {
	this(world, color, radius);
	this.addChild(Mo.Scale(radius).comp(Mo.INSIDEOUT), outside);
    }

    public Metric metric(Mo T) {
	Metric f = super.metric(T);
	Metric g = super.metric(inversion.comp(T));
	Metric diff = Metric.harmonicDifference(f, g);
	return diff;
    }

    public static double atanh(double x) {
	return 0.5*Math.log( (1.0 + x) / (1.0 - x) ); 
    }

    public double outerRim(double size) {
	return radius*Math.tanh(atanh(size/radius)+atanh(world.scale/radius));
    }
}

import java.util.Arrays;
import java.awt.Paint;

public class Sphere extends Space {
    public double radius;
    private Mo inversion;

    public Sphere(World world, Paint color, double radius) {
	super(world, color);
	this.radius = radius;
	inversion = Mo.Scale(4*radius*radius).comp(Mo.INSIDEOUT);
    }

    public Metric metric(Mo T) {
	Metric f = super.metric(T);
	Metric g = super.metric(inversion.comp(T));
	Metric sum = Metric.harmonicSum(Arrays.asList(new Metric[] {f, g}));
	return sum;
    }

    public double outerRim(double size) {
	return 2*radius*Math.tan(Math.atan(size/(2*radius))+Math.atan(world.scale/(2*radius)));
    }
}
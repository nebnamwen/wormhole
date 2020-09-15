import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.awt.Paint;

public class Space extends BGDomain {

    public World world;
    public EnumMap<Thing.Attribute, HashSet<Thing>> things;
    public HashSet<Wormhole> wormholes;

    public Space(World world, Paint color) {
	super(null, null, color);
	this.world = world;
	world.spaces.add(this);
	wormholes = new HashSet<Wormhole>();
	things = new EnumMap<Thing.Attribute, HashSet<Thing>>(Thing.Attribute.class);
	for (Thing.Attribute attribute : Thing.Attribute.values()) {
	    things.put(attribute, new HashSet<Thing>());
	}
    }

    public void addThing(Thing thing) {
	for (Thing.Attribute attribute : thing.attributes()) {
	    things.get(attribute).add(thing);
	}
    }

    public void removeThing(Thing thing) {
	for (Thing.Attribute attribute : thing.attributes()) {
	    things.get(attribute).remove(thing);
	}
    }

    public double outerRim(double size) {
	return size + world.scale;
    }

    public Metric metric(Mo T) {
	return new Metric(T);
    }

    public Metric collectedMetric(Mo T) {
	return Metric.pythagoreanSum(collectMetrics(T));
    }

    protected Collection<Metric> collectMetrics(Mo T) {
	return collectMetrics(T, 0.05);
    }

    protected Collection<Metric> collectMetrics(Mo T, double threshold) {
	Collection<Metric> metrics = new HashSet<Metric>();
	collectMetrics(metrics, T, threshold, null);
	return metrics;
    }

    protected void collectMetrics(Collection<Metric> metrics, Mo T, double threshold, Wormhole from) {
	Metric m = metric(T);
	metrics.add(m);
	if (m.ds >= threshold) {
	    for (Wormhole hole : wormholes) {
		if (hole != from && hole.otherside != null) {
		    hole.otherside.space.collectMetrics(metrics, hole.transform.comp(T), threshold, hole.otherside);
		}
	    }
	}
    }

    public void queueThingsForPainting(Viewport V, Mo T, Wormhole from, boolean cont) {
	for (Thing thing : things.get(Thing.Attribute.VISIBLE)) {
	    Mo position = T.comp(thing.rim());
	    if (V.isDotOnScreen(position, 0.5)) {
		V.queueThingForPainting(thing, T.comp(thing.rim()));
	    }
	}
	if (cont) {
	    for (Wormhole hole : wormholes) {
		if (hole != from && hole.otherside != null) {
		    if (V.isDotOnScreen(T.comp(hole.outerrim), 2)) {
			hole.otherside.space.queueThingsForPainting(V, T.comp(hole.otherside.transform), hole.otherside,V.isDotOnScreen(T.comp(hole.rim), 2));
		    }
		}
	    }
	}
    }

}

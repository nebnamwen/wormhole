import java.util.ArrayList;
import java.util.HashSet;
import java.awt.Color;

public class World {

    public final double scale = 1.0;
    public final int timing = 20; // milliseconds

    public HashSet<Space> spaces;

    public World() {
	spaces = new HashSet<Space>();
    }

    public void update(double dt) {
	ArrayList<Thing> things = new ArrayList<Thing>();
	for (Space space : spaces) {
	    for (Thing thing : space.things.get(Thing.Attribute.DYNAMIC)) {
		things.add(thing);
	    }
	}
	for (Thing thing : things) {
	    thing.update(dt);
	}
    }

}

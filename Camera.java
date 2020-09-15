public class Camera {
    Thing thing;
    Mo T;

    public Camera(Thing thing, Mo T) {
	this.thing = thing;
	this.T = T;
    }

    public Space getSpace() {
	return thing.space;
    }

    public Mo getT() {
	return T.comp(thing.position.inv());
    }
}

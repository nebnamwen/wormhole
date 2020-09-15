Wormhole demonstration
by Benjamin Newman, bnewman@swarpa.net

* "Wormhole" is a framework for visualizing non-Euclidean 2D spaces in the form of a rudimentary game engine.
* The spaces are represented intrinsically, without embedding them in a third dimension.
* The precise mapping between the space and the screen depends on how the space is defined and may not reflect what an ant embedded in the space would see.
* A complete explanation of the math involved is beyond the scope of this README file, but suffice it to say that it involves a lot of 2x2 matrices of complex numbers.

* Wormhole is packaged as an executable Java archive, <Wormhole.jar>, which can be run on most systems with Java installed simply by double-clicking it.
* The demo contains a single hard-coded map which shows off a variety of structures and features.

CONTROLS:
* Use the arrow keys to move your cursor.
   - The cursor has a red headlight on the left and a green one on the right, so if you see another image of your cursor, you can see how it's oriented.
* Use the D, F, G keys to spawn objects:
   - Dart
   - Flower
   - Ghost (a phantom image of your cursor, likewise with orientation lights)
* Use the Q key, or close the window, to quit.

* Because of the lack of background textures, it can be hard to see the shape of the space you're in.  Planting flowers can help reveal the shape of the surrounding space.

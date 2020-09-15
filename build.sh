#!/bin/sh

rm *.class
javac *.java
jar cmf manifest.mf Wormhole.jar *.class *.java

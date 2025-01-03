# Worldsim

## A map editor for 2d games

This is a map editor for 2d games, primarily with top-down view.

## Controls

Move the camera/window with Arrow UP,DOWN,LEFT,RIGHT.
Move the cursor, for placing terrain and Objects with W,A,S,D.

Cycle active terrain type C-key.

Cycle the current Toolbox tool with the F-key.

Tools in the toolbox:
* Floodfill the map (first place several blocks there).
* Place terrain.
* Remove terrain.
* Place objects such as trees
* Remove objects, in proximity of the cursor


Export/save map E-key (saves the map as a json string).

For more keyboard bindings, see the code.

## Other features

Uses shadow jar to make an executable jar that can be exported to other computers and run with jre.

## Building and running 

Build and run with Gradle

./gradlew build

./gradlew run

./gradlew shadowJar


![screenshot](/worldsim1.png "A screenshot from the editor")


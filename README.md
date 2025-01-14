# Worldsim

## A map editor for 2d games

This is a map editor for 2d games, primarily with top-down view.

## Controls

Move the camera/window with Arrow UP,DOWN,LEFT,RIGHT.
Move the cursor, for placing terrain and Objects with W,A,S,D.

Cycle the currently selected terrain type with the C-key.
Cycle the currently selected object type with the X-key.

Cycle the current Toolbox tool with the F-key.

Tools in the toolbox:
* Place terrain.
* Remove terrain.
* Floodfill the map (first place several tiles close to each other at the spot).
* Place objects such as trees
* Remove objects, in proximity of the cursor


Export/save map E-key (saves the map as a json string).

For more keyboard bindings, see the code (registered keys in file App.java).

## Other features

Uses shadow jar to make an executable jar that can be exported to other computers and run with jre.

## Building and running 

Build and run with Gradle

./gradlew build

./gradlew run

./gradlew shadowJar


![screenshot](/worldsim1.png "A screenshot from the editor")
![screenshot](/worldsim2.png "Another screenshot from the editor")

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/kribo852/worldsim/gradle.yml)



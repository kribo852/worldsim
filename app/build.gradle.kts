/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("worldsim.java-application-conventions")
    id("io.freefair.lombok") version "8.11"
    id("com.gradleup.shadow") version "9.0.0-beta4"
}

dependencies {
    implementation("org.apache.commons:commons-text")
    implementation("com.google.code.gson:gson:2.11.0")
}

application {
    // Define the main class for the application.
    mainClass.set("worldsim.app.App")
}


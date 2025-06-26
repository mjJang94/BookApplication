// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.kotlin)
        classpath(libs.hilt.gradle)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
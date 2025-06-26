pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

rootProject.name = "BookApplication"
include(":app")
include(":remote")
include(":data")
include(":ui")

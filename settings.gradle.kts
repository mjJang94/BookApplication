rootProject.name = "BookApplication"

include(":app")

include(":remote")
include(":local")
include(":ui")
include(":data")
include(":common")
include(":domain")
include(":data-resource")
include(":presentation")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

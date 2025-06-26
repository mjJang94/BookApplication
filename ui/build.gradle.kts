
plugins {
    id("module.android")
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.mj.ui"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":presentation"))
    implementation(project(":data-resource"))
    implementation(project(":common"))

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.hilt.navigation)
}
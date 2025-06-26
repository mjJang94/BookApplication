plugins {
    id("module.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.mj.presentation"
}

dependencies {
    implementation(libs.androidx.viewmodel)
    implementation(project(":domain"))
}
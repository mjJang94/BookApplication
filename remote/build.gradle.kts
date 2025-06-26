plugins {
    id("module.android")
}

android {
    namespace = "com.mj.remote"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
}
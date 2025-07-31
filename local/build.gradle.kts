plugins {
    id("module.android")
}
android {
    namespace = "com.mj.local"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":common"))

    //data store
    implementation(libs.datastore.pref)
}
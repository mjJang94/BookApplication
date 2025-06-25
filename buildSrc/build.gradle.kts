import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.gradle)
    implementation(libs.kotlin)
    implementation(libs.hilt.gradle)
}
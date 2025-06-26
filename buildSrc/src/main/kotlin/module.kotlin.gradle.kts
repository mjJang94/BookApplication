import gradle.kotlin.dsl.accessors._df1a3a942e90e698d12fe13b136cffec.implementation
import gradle.kotlin.dsl.accessors._df1a3a942e90e698d12fe13b136cffec.kapt

plugins {
    id("kotlin")
    id("kotlin-kapt")
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")


dependencies {
    implementation(libs.getLibrary("hilt.core"))
    kapt(libs.getLibrary("hilt.compiler"))
}

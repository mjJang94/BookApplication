plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        compileSdk = 35
        minSdk = 24
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            consumerProguardFiles("proguard-rules.pro")
        }
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.getLibrary("androidx.core.ktx"))
    implementation(libs.getLibrary("androidx.lifecycle.runtime.ktx"))

    implementation(libs.getLibrary("hilt.android"))
    kapt(libs.getLibrary("hilt.compiler"))
}

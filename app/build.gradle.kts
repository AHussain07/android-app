// app/build.gradle.kts

plugins {
    // only your Android‑app plugin
    alias(libs.plugins.android.application)
}

android {
    namespace = "uk.edu.le.part2"
    compileSdk = 35

    defaultConfig {
        applicationId = "uk.edu.le.part2"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX & Material
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    // ROOM (Java annotation‑processor)
    implementation("androidx.room:room-runtime:2.5.1")
    implementation("androidx.room:room-ktx:2.5.1")      // optional
    annotationProcessor("androidx.room:room-compiler:2.5.1")

    // CoordinatorLayout (so ViewBinding finds it)
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

plugins {
    id("com.android.application")
    kotlin("android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "com.genlz.xposeddemo"
        minSdk = 27
        targetSdk = 31
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"

        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }

    packagingOptions {
        resources.excludes += "DebugProbesKt.bin"
    }
}

dependencies {

    val HILT = "2.40.5"
    implementation("com.google.dagger:hilt-android:$HILT")
    kapt("com.google.dagger:hilt-android-compiler:$HILT")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")

    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}
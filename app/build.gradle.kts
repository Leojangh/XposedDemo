plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    compileSdk = 34
    namespace = "com.genlz.xposeddemo"
    defaultConfig {
        applicationId = "com.genlz.xposeddemo"
        minSdk = 33
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }

    packaging {
        resources.excludes += "DebugProbesKt.bin"
    }
}

dependencies {

    val dagger: String by project
//    implementation("com.google.dagger:hilt-android:$dagger")
//    kapt("com.google.dagger:hilt-android-compiler:$dagger")
    implementation("com.google.dagger:dagger:$dagger")
    ksp("com.google.dagger:dagger-compiler:$dagger")

    implementation("org.smali:dexlib2:2.5.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    compileOnly("de.robv.android.xposed:api:82")
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
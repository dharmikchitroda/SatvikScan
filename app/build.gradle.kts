plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.satvikscan"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.satvikscan"
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
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.foundation.layout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //for navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")

    //camerax
    val camerax_version = "1.4.0"
    implementation("androidx.camera:camera-camera2:${camerax_version}") // camera logic actual
    implementation("androidx.camera:camera-lifecycle:${camerax_version}") // start or stop
    implementation("androidx.camera:camera-view:${camerax_version}") // PreviewView

    // viewmodel library1
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.10.0") // ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")  // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0") // use compose

    // ML kit library
    implementation("com.google.mlkit:barcode-scanning:17.3.0") // this dependency to bundle the model with your app not a playstore

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")   // Gson converter (JSON parse karne ke liye)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // coil - load image in our app from api
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("io.coil-kt:coil-gif:2.6.0")

    // text recognizaion Ml kit
    implementation("com.google.mlkit:text-recognition:16.0.1")  
}

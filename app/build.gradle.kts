plugins {
    id("com.android.application")
}

android {
    namespace = "com.prashant.q2pay"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.prashant.q2pay"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Volley
    implementation("com.android.volley:volley:1.2.1")

    //SDP
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    //Gif viewer
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.8")

    //Bottom NavigationBar
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")


    // Gson
    implementation("com.google.code.gson:gson:2.8.9")

    //Image Slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")



}
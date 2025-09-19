plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.muhammedturgut.caremate"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.muhammedturgut.caremate"
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
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ConstraintLayout kullanamk içine eklediğim kütüphane.
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    //Navigasyon kütüphanesi
    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    //AppWrite
    implementation ("io.appwrite:sdk-for-android:5.1.0")

    // Coroutine desteklemek istersen (ViewModel içinde daha rahat çalışır)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //lottie files animasyon kullanama kütüphanesi
    implementation ("com.airbnb.android:lottie-compose:6.1.0")

    //Retrofit ve okhttp3 converter gson kütüphanelri.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("androidx.compose.runtime:runtime-livedata:1.6.0")

    // MediaPipe - Yeni Tasks API (Eski solution API yerine)
    // Pose detection için güncel versiyonlar
    implementation("com.google.mediapipe:tasks-vision:0.10.14")
    implementation("com.google.mediapipe:tasks-core:0.10.14")


    // Alternatif: Eğer eski API ile çalışmak istiyorsanız, mevcut olan son versiyon:
    // implementation("com.google.mediapipe:solution-core:0.8.10")
    // implementation("com.google.mediapipe:solution-pose:0.8.10")

    // CameraX - Güncellenmiş versiyonlar
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("androidx.concurrent:concurrent-futures:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:1.7.3")


    // Room Data Base için kullanılan kütüphaneler.
    val roomVersion = "2.6.1"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("com.google.code.gson:gson:2.12.1")

}
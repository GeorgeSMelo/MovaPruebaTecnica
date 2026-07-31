plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms)
}

android {
    namespace = "com.example.movapruebatecnica"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.movapruebatecnica"
        minSdk = 24
        targetSdk = 36
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
        viewBinding = true
        buildConfig = true
        compose = true
    }
    configurations.all {
        resolutionStrategy {
            force("androidx.concurrent:concurrent-futures:1.2.0")
            force("androidx.concurrent:concurrent-futures-ktx:1.2.0")
        }
    }
}

dependencies {

    //Core Implementation
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.compose.material.icons.extended)

    // appCompat
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3)
    // recyclerView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.ui)

    // Navegación
    implementation(libs.navigation.compose)

    //VIEWMODEL & LIFECYCLE
    implementation(libs.liferecycle.viewmodel)
    implementation(libs.liferecycle.runtime)

    //RETROFIT & NETWORKING
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.retrofit)

    //HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    //Livedata
    implementation(libs.livedata.runtime)

    //ROOM
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.0")

    //Core testImplementation
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    //Coroutines Test
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.arch.core)

    //Mockk
    testImplementation(libs.mockk)

    // Scanner
    implementation(libs.scanner.google)
}

import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.homedesign.interiordesign.aihome"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.homedesign.interiordesign.aihome"
        minSdk = 29
        targetSdk = 36

        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val formattedDate = SimpleDateFormat("MM.dd.yyyy").format(Date())
        base.archivesName = "V340_AI_Video_Maker_No_ads_v${versionName}_$formattedDate"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {
            isMinifyEnabled = false
            isShrinkResources = false

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

    //noinspection WrongGradleMethod
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    bundle {
        language.enableSplit = false
        density.enableSplit = true
        abi.enableSplit = true
    }

    flavorDimensions += "default"

    productFlavors {
        create("for_dev_") {
            dimension = "default"
        }
        // create("for_product_") { dimension = "default" }
    }

    sourceSets {
        getByName("for_dev_") {
            res.srcDirs("src/for_dev_/res")
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Room + KSP
    implementation(libs.androidx.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit + OkHttp (ổn định hơn 3.0)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Gson
    implementation("com.google.code.gson:gson:2.13.2")

    // Koin
    implementation("io.insert-koin:koin-android:4.1.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Glide
    implementation("com.github.bumptech.glide:glide:5.0.5")

    // UI
    implementation("com.makeramen:roundedimageview:2.3.0")

    // Play Review
    implementation("com.google.android.play:review-ktx:2.0.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.0.0"))
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-config")

    // Blur
    implementation("jp.wasabeef:glide-transformations:4.3.0")

    // WorkManager (chỉ giữ cái cần thiết)
    implementation("androidx.work:work-runtime-ktx:2.11.2")

    // Test WorkManager
    androidTestImplementation("androidx.work:work-testing:2.11.2")
}
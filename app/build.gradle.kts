// app/build.gradle.kts
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}


android {
    namespace = "com.stealthvault.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.stealthvault.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file("release-keystore.jks")
            storePassword = "android"
            keyAlias = "upload"
            keyPassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
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
        freeCompilerArgs += listOf(
            "-Xskip-metadata-version-check"
        )
    }
    buildFeatures {
        viewBinding = true
        dataBinding = false
    }
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.fragment:fragment-ktx:1.8.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Room — KSP replaces kapt for annotation processing
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt — KSP replaces kapt for annotation processing
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Biometric & CameraX
    implementation(libs.androidx.biometric)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // SQLCipher for encrypted DB
    implementation(libs.sqlcipher)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Glide for image/video thumbnails (kapt still required for Glide 4.x)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)

    // Additional dependencies
    implementation("net.objecthunter:exp4j:0.4.8")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("com.google.zxing:core:3.5.1")
    implementation("com.google.guava:guava:31.1-android")

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    // Crypto
    implementation("org.bouncycastle:bcprov-jdk15to18:1.70")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// KSP configuration for Room
ksp {
    arg("room.incremental", "true")
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.expandProjection", "true")
}

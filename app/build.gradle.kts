plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.21"
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lelestargazer.qurban_ticketing_system"
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
        freeCompilerArgs += "-Xskip-prerelease-check"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // Feature
    implementation(projects.core.common)
    implementation(projects.core.theme)
    implementation(projects.feature.participantManagementSystem.data)
    implementation(projects.feature.participantManagementSystem.ui)



    implementation(projects.feature.member.shared.common)
    implementation(projects.feature.member.shared.data)
    implementation(projects.feature.member.management.ui)
    implementation(projects.feature.member.ticketing.ui)



    // Participant Management
//    implementation(projects.feature.participantManagement.ui)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)


    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    val nav_version = "2.9.0"

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")

    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")

    implementation ("com.google.accompanist:accompanist-permissions:0.37.3")
}
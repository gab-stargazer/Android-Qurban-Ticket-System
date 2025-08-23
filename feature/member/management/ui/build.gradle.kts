plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    kotlin("plugin.serialization") version "2.1.21"

}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system.member_management.ui"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    implementation(projects.core.common)
    implementation(projects.core.theme)
    implementation(projects.feature.member.shared.common)
    implementation(projects.feature.member.shared.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
    implementation("io.arrow-kt:arrow-optics:2.1.0")
    ksp("io.arrow-kt:arrow-optics-ksp-plugin:2.1.0")

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    //  Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.navigation)

    val nav_version = "2.9.0"

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation(libs.serialization)

    //  Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)
}
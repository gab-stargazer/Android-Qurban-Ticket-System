plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.ksp)
    kotlin("plugin.serialization") version "2.1.21"

}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system.member_management.ui"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")

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

    //  Accompanist
    implementation(libs.accompanist.permission)

    //  Arrow
    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
    implementation(libs.arrow.optic)
    ksp(libs.arrow.optic.ksp)

    //  Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    //  Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(libs.bundles.koin.compose)
    ksp(libs.koin.annotation.ksp)

    //  Paging
    implementation(libs.paging)
    implementation(libs.paging.compose)

    //  Serialization
    implementation(libs.serialization)

    implementation("io.github.crispindeity:kotlin-snowflake:1.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")

}
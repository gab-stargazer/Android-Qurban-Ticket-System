plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system.member_shared.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(projects.feature.member.shared.common)
    implementation(projects.feature.member.shared.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Arrow
    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)

    //  Itext
    implementation(libs.itext)

    //  Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.bundles.koin)
    implementation(libs.koin.workmanager)
    ksp(libs.koin.annotation.ksp)

    //  Paging
    implementation(libs.paging)

    //  QR
    implementation(libs.qr.generator)

    //  Retable
    implementation(libs.retable)

    //  Room
    implementation(libs.room)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    //  Workmanager
    implementation(libs.workmanager)



    implementation(platform("org.kotlincrypto.hash:bom:0.8.0"))
    implementation("org.kotlincrypto.hash:sha3")
    implementation("com.google.zxing:core:3.5.4")

    implementation("io.github.crispindeity:kotlin-snowflake:1.0.1")
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system.participant_management_system.data"
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
}

dependencies {

    implementation(projects.feature.participantManagementSystem.domain)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //  Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    //  Room
    implementation(libs.room)
    ksp(libs.room.compiler)

    implementation(platform("org.kotlincrypto.hash:bom:0.7.0"))
    implementation("org.kotlincrypto.hash:sha3")
    implementation("com.google.zxing:core:3.4.0")
    implementation("io.github.g0dkar:qrcode-kotlin:4.4.1")
    implementation("com.itextpdf:itext7-core:7.2.5")

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
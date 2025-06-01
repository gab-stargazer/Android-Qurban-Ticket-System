plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.room)
}

android {
    namespace = "com.lelestargazer.qurban_ticketing_system.member_shared.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

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

    implementation("com.github.4sh:retable:0.2.8")

    //  Room
    implementation(libs.room)
    ksp(libs.room.compiler)

    //  Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(platform("org.kotlincrypto.hash:bom:0.7.0"))
    implementation("org.kotlincrypto.hash:sha3")
    implementation("com.google.zxing:core:3.4.0")
    implementation("io.github.g0dkar:qrcode-kotlin:4.4.1")
    implementation("com.itextpdf:itext7-core:7.2.5")

    implementation(platform(libs.arrow.bom))
    implementation(libs.arrow.core)
}
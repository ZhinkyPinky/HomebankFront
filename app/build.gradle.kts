plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.homebankfront"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.homebankfront"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }

        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    flavorDimensions += listOf("env")
    productFlavors {
        create("production") {
            dimension = "env"
            manifestPlaceholders["appName"] = "Homebank"
            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://homebank-api.livelyhill-daa2c63f.northeurope.azurecontainerapps.io\""
            )
        }

        create("development") {
            dimension = "env"
            manifestPlaceholders["appName"] = "DevHomebank"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"http://192.168.68.87:8080\""
            )
        }

        create("developmentRemote") {
            dimension = "env"
            manifestPlaceholders["appName"] = "DevHomebankRemote"
            applicationIdSuffix = ".devRemote"
            versionNameSuffix = "-devRemote"

            buildConfigField(
                "String",
                "API_BASE_URL",
                "\"https://homebank-api-dev.livelyhill-daa2c63f.northeurope.azurecontainerapps.io\""
            )
        }
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

    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.credentials)
    implementation(libs.com.google.android.libraries.identity.googleid)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.security.crypto)
}
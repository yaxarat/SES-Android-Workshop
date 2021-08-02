import dependencies.Dependencies

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("name.remal.check-dependency-updates") version "1.4.1"
}

android {
    compileSdk = Dependencies.compileSdk
    buildToolsVersion = Dependencies.buildTools

    defaultConfig {
        applicationId = Dependencies.applicationId
        minSdk = Dependencies.minSdk
        targetSdk = Dependencies.targetSdk
        versionCode = Dependencies.versionCode
        versionName = Dependencies.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = Dependencies.jvmTarget
        useIR = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dependencies.kotlinCompilerExtension
        kotlinCompilerVersion = Dependencies.kotlin
    }
}

kotlin.sourceSets.all {
    languageSettings.apply {
        useExperimentalAnnotation("kotlinx.coroutines.ExperimentalCoroutinesApi")
        useExperimentalAnnotation("androidx.compose.ui.ExperimentalComposeUiApi")
        useExperimentalAnnotation("androidx.compose.foundation.ExperimentalFoundationApi")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Dependencies.core_ktx}")
    implementation("androidx.appcompat:appcompat:${Dependencies.appcompat}")
    implementation("androidx.compose.ui:ui:${Dependencies.compose}")
    implementation("androidx.compose.material:material:${Dependencies.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Dependencies.compose}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${Dependencies.lifecycle_runtime_ktx}")
    implementation("androidx.activity:activity-compose:${Dependencies.activity_compose}")
    implementation("androidx.fragment:fragment-ktx:${Dependencies.fragment_ktx}")
    implementation("com.squareup.retrofit2:retrofit:${Dependencies.retrofit}")
    implementation("com.squareup.retrofit2:converter-gson:${Dependencies.retrofit}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Dependencies.logging_interceptor}")
    implementation("com.google.android.material:material:${Dependencies.material}")
}
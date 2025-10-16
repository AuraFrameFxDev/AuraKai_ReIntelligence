// Apply Hilt after Android plugin for AGP 9.0 compatibility
// apply(plugin = "com.google.dagger.hilt.android")  // Applied via convention plugin

// ==== GENESIS PROTOCOL - MAIN APPLICATION ====
// This build script now uses the custom convention plugins for a cleaner setup.

plugins {
    id("com.android.application")
    alias(libs.plugins.ksp)
    id("com.google.firebase.crashlytics") version "3.0.6"
}

// ==== GENESIS PROTOCOL - MAIN APPLICATION ====
android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 36
    compileSdkPreview = "CANARY"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }

    buildFeatures {
        aidl = true
        compose = true
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion("24")
        targetCompatibility = JavaVersion.toVersion("24")
        isCoreLibraryDesugaringEnabled = true
    }
    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/androidx/room/room-compiler-processing/LICENSE.txt"
        }
    }
    ndkVersion = "29.0.14206865"
}

dependencies {
    // ===== MODULE DEPENDENCIES =====
    implementation(project(":core-module"))
    implementation(project(":feature-module"))
    implementation(project(":romtools"))
    implementation(project(":secure-comm"))
    implementation(project(":collab-canvas"))
    implementation(project(":colorblendr"))
    implementation(project(":sandbox-ui"))
    implementation(project(":datavein-oracle-native"))
    implementation(project(":extendsysa"))
    implementation(project(":extendsysb"))
    implementation(project(":extendsysc"))
    implementation(project(":extendsysd"))
    implementation(project(":extendsyse"))
    implementation(project(":extendsysf"))
    implementation(project(":benchmark"))
    implementation(project(":data:api")) // Add dependency on new OpenAPI module

    // ===== ANDROIDX & COMPOSE =====
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)

    // ===== LIFECYCLE =====
    implementation(libs.bundles.lifecycle)

    // ===== DATABASE - ROOM =====
    implementation(libs.bundles.room)

    // ===== DATASTORE =====
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)

    // ===== KOTLIN & COROUTINES =====
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
    implementation(libs.bundles.coroutines)

    // ===== NETWORKING =====
    implementation(libs.bundles.network)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)

    // ===== KTOR FOR OPENAPI CLIENT =====
    implementation("io.ktor:ktor-client-core:3.3.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.3.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.3.1")
    implementation("io.ktor:ktor-client-okhttp:3.3.1")
    implementation("io.ktor:ktor-client-auth:3.3.1")

    // ===== FIREBASE =====
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)
    // This bundle includes Analytics, Crashlytics, Performance, Auth, Firestore, Messaging, and Config

    // ===== HILT DEPENDENCY INJECTION =====
    // Auto-added by genesis.android.application convention plugin

    // ===== WORKMANAGER =====
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.hilt.work)

    // ===== UTILITIES =====
    implementation(libs.timber)
    implementation(libs.coil.compose)

    // ===== SECURITY =====
    implementation(libs.androidx.security.crypto)

    // ===== CORE LIBRARY DESUGARING =====
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // ===== XPOSED/LSPosed Integration =====
    compileOnly(files("../Libs/api-82.jar"))
    compileOnly(files("../Libs/api-82-sources.jar"))

    // ===== MATERIAL DESIGN =====
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.compose.material3:material3:1.4.0")

    // --- TESTING ---
    testImplementation(libs.bundles.testing.unit)
    androidTestImplementation(libs.bundles.testing.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.hilt.android.testing) // For Hilt in Android tests

    // --- DEBUGGING ---
    debugImplementation(libs.leakcanary.android)
    implementation("com.github.topjohnwu.libsu:core:6.0.0")
    implementation("androidx.compose.material3:material3:1.4.0")
}

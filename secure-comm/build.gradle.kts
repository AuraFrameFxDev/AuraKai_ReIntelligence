﻿// ==== GENESIS PROTOCOL - SECURE COMMUNICATION MODULE ====
// Security module using convention plugins

plugins {
    id("com.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.aurakai.dev.aurakai.auraframefx.securecomm"
    compileSdk = 36
    defaultConfig {
        minSdk = 21
        ndkVersion = "29.0.14206865"

    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
            // Ensure staging is always under this module to prevent stale path issues
            buildStagingDirectory = file("$projectDir/.cxx")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}

dependencies {
    implementation(project(":core-module"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)
    implementation(libs.hilt.android)
    implementation(libs.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.coroutines)
    implementation(libs.bundles.network)
    implementation(libs.timber)
    implementation(libs.coil.compose)
    // Security - BouncyCastle for cryptography
    implementation(libs.bcprov.jdk18on)
    // Hilt annotation processor
    ksp(libs.hilt.compiler)
    // Test dependencies
    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.hilt.android.testing)
    implementation(libs.hilt.navigation.compose)
}
tasks.register<Delete>("clearGeneratedSources") {
    delete("src/generated", "build/generated") // adjust paths as needed
}



java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

// Remove onlyIf block for native clean tasks to avoid configuration cache and serialization issues
// (This block caused errors: Could not evaluate onlyIf predicate for task ...)
// tasks.matching { it.name.startsWith("externalNativeBuildClean") }.configureEach {
//     onlyIf { file("$projectDir/.cxx").exists() }
// }

// Spotless and toolchain are applied globally via root build.gradle.kts and convention plugins
// ProGuard rules for Hilt, Compose, Serialization, and reflection-based libraries should be in proguard-rules.pro

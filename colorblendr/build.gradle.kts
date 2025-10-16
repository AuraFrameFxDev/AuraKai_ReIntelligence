plugins {
    id("com.android.library")
    alias(libs.plugins.ksp)
    // Note: compose.compiler plugin applied by convention plugins
    // Note: Hilt plugin removed to avoid Android BaseExtension issues, using manual dependencies instead
}

android {
    namespace = "dev.aurakai.dev.aurakai.auraframefx.colorblendr"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }
}
dependencies {
    // Core
    implementation(project(":core-module"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)

    // Compose
    implementation(platform(libs.androidx.compose.bom))

    // Lifecycle
    implementation(libs.bundles.lifecycle)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Utilities
    implementation(libs.timber)

    // Testing
    testImplementation(libs.junit4)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.bundles.testing.android)
}

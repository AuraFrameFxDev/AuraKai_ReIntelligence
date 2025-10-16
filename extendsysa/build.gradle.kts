// GENESIS PROTOCOL - MODULE A
plugins {
    id("com.android.library")
    // id("com.google.dagger.hilt.android") // Only apply if NOT included in Genesis plugin
    alias(libs.plugins.ksp)
}
    android {
        namespace = "dev.aurakai.dev.aurakai.auraframefx.module.a"
        compileSdk = 36

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_25
            targetCompatibility = JavaVersion.VERSION_25
        }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(25))
            }
        }

        buildFeatures {
            compose = true
        }
    }

dependencies {
    implementation(project(":core-module"))
    implementation(libs.androidx.core.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
}

    tasks.register("moduleAStatus") {
        group = "aegenesis"
        doLast {
            println("📦 MODULE A - Ready (Java 25)")

        }
    }

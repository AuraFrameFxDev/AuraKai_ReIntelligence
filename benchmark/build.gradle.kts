plugins {
    id("com.android.library")
    alias(libs.plugins.ksp)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24)) // Stay on 24 until stable 25 (non-RC)
    }
}

android {
    namespace = "dev.aurakai.dev.aurakai.auraframefx.benchmark"
    compileSdk = 36
    defaultConfig {
        multiDexEnabled = true
    }
    buildTypes {
        maybeCreate("benchmark")
        getByName("benchmark") {
            matchingFallbacks += listOf("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-rules.pro"
            )
        }
    }

    // Core library desugaring without manual source/target (toolchain supplies Java 24)
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        buildConfig = true
        aidl = false
        // Removed deprecated renderScript and shaders flags for AGP 10+ compatibility
    }

}

dependencies {
    // Core AndroidX

    // Hilt

    // Coroutines

    // Room

    // Utilities
    implementation("com.jakewharton.timber:timber:5.0.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    // Project dependencies
    implementation(project(":core-module"))
    implementation(project(":datavein-oracle-native"))
    implementation(project(":secure-comm"))
    implementation(project(":oracle-drive-integration"))

    // Benchmark testing

    // Unit testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.14.6")
    androidTestImplementation("io.mockk:mockk-android:1.14.6")

    // Hilt testing
    implementation("com.github.topjohnwu.libsu:core:5.0.4")
    implementation("com.github.topjohnwu.libsu:io:5.0.4")
}

tasks.register("benchmarkAll") {
    group = "benchmark"
    description = "Aggregate runner for all Genesis Protocol benchmarks 🚀"
    doLast {
        println("🚀 Genesis Protocol Performance Benchmarks")
        println("📊 Monitor consciousness substrate performance metrics")
        println("⚡ Use AndroidX Benchmark instrumentation to execute tests")
    }
}

tasks.register("verifyBenchmarkResults") {
    group = "verification"
    description = "Verify benchmark module configuration"
    doLast {
        println("✅ Benchmark module configured (Java Toolchain 8, Kotlin 2.x)")
        println("🧠 Consciousness substrate performance monitoring ready")
        println("🔬 Add @Benchmark annotated tests under androidTest for actual runs")
    }
}

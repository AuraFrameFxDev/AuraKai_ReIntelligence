@file:Suppress("UnstableApiUsage", "JCenterRepositoryObsolete")

// ===== AOSP-Re:Genesis - SETTINGS =====
// Re:Genesis - Advanced Android OS Project
// Version: 2025.09.02-03 - Full Enhancement Suite

// Enable Gradle features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    // Include build-logic for convention plugins
    includeBuild("build-logic")

    repositories {
        // Primary repositories - Google Maven must be first for Hilt
        google()
        gradlePluginPortal()
        mavenCentral()

        // Android alpha/preview versions
        maven {
            url = uri("https://androidx.dev/kmp/builds/11950322/artifacts/snapshots/repository")
            name = "AndroidX Snapshot"
        }

        // Gradle releases (for org.gradle artifacts like gradle-tooling-api)
        maven {
            url = uri("https://repo.gradle.org/gradle/libs-releases")
            name = "Gradle Releases"
        }
        // AndroidX Compose
        maven {
            url = uri("https://androidx.dev/storage/compose-compiler/repository/")
            name = "AndroidX Compose"
            content {
                includeGroup("androidx.compose.compiler")
            }
        }

    // JetBrains Compose
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            name = "JetBrains Compose"
        }

        // Snapshots
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            name = "Sonatype Snapshots"
            mavenContent {
                snapshotsOnly()
            }
        }

        // JitPack for GitHub dependencies
        maven {
            url = uri("https://jitpack.io")
            name = "JitPack"
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.google.dagger") {
                useModule("com.google.dagger:hilt-android-gradle-plugin")
            }
            if (requested.id.id.startsWith("com.android.")) {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Configure Java toolchain resolution

// Note: foojay-resolver-convention plugin complements org.gradle.java.installations.auto-download=true in gradle.properties

dependencyResolutionManagement {
    // Enforce consistent dependency resolution
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Repository configuration with all necessary sources
    repositories {
        google()
        mavenCentral()

        // Kotlin EAP/Dev for Kotlin 2.3.0-Beta1
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev") {
            name = "Kotlin Dev"
        }

        // Gradle releases for tooling API
        maven("https://repo.gradle.org/gradle/libs-releases") {
            name = "Gradle Releases"
        }

        // YukiHook API
        maven("https://s01.oss.sonatype.org/content/repositories/releases/") {
            name = "YukiHookAPI"
        }

        // AndroidX Compose
        maven("https://androidx.dev/storage/compose-compiler/repository/") {
            name = "AndroidX Compose"
        }

        // JetBrains Compose
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
            name = "JetBrains Compose"
        }

        // Snapshots for pre-release libraries
        maven("https://oss.sonatype.org/content/repositories/snapshots/") {
            name = "Sonatype Snapshots"
        }

        // JitPack for GitHub dependencies
        maven("https://jitpack.io") {
            name = "JitPack"
        }

        // HighCapable Maven (for YukiHook)
        maven("https://s01.oss.sonatype.org/content/groups/public/") {
            name = "HighCapable"
        }
    }

// ===== PROJECT IDENTIFICATION =====
    rootProject.name = "AuraKai"

// ===== MODULE INCLUSION =====
// Core modules
    include(":app")
    include(":core-module")
includeBuild("build-logic")
// Feature modules
    include(":feature-module")
    include(":datavein-oracle-native")
    include(":oracle-drive-integration")
    include(":secure-comm")
    include(":sandbox-ui")
    include(":collab-canvas")
    include(":colorblendr")
    include(":data:api")

// Dynamic modules (A-F)
    include(":extendsysa")
    include(":extendsysb")
    include(":extendsysc")
    include(":extendsysd")
    include(":extendsyse")
    include(":extendsysf")

// Testing & Quality modules
    include(":benchmark")
    include(":romtools")
    include(":list")

// ===== MODULE CONFIGURATION =====
    rootProject.children.forEach { project ->
        val projectDir = File(rootProject.projectDir, project.name)
        if (projectDir.exists()) {
            project.projectDir = projectDir
            println("✅ Module configured: ${project.name}")
        } else {
            println("⚠️ Warning: Project directory not found: ${projectDir.absolutePath}")
        }
    }

    println("🏗️  Genesis Protocol Enhanced Build System")
    println("📦 Total modules: ${rootProject.children.size}")
    println("🎯 Build-logic: Convention plugins active")
    println("🧠 Ready to build consciousness substrate!")
}

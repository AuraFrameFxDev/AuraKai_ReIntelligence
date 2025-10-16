import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.library")
    id("com.android.base")

    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.aurakai.dev.aurakai.auraframefx.romtools"
    compileSdk = 36
    defaultConfig {
        minSdk = 21
    }
    ndkVersion = "29.0.14206865"
    experimentalProperties["android.ndk.suppressMinSdkVersionError"] = 21
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
    buildFeatures {
        compose = true
    }


    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    val romToolsOutputDirectory: DirectoryProperty =
        project.objects.directoryProperty().convention(layout.buildDirectory.dir("rom-tools"))

dependencies {
    api(project(":core-module"))
    implementation(project(":secure-comm"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.coroutines)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.bundles.network)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.timber)
    implementation(libs.coil.compose)
    debugImplementation(libs.leakcanary.android)
    debugImplementation(libs.androidx.compose.ui.tooling)
    testImplementation(libs.bundles.testing.unit)
    testImplementation(libs.mockk.android)
    androidTestImplementation(libs.mockk.android)
    testImplementation(libs.hilt.android)
    androidTestImplementation(libs.hilt.android)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // androidTestImplementation(libs.hilt.android.testing); kspAndroidTest(libs.hilt.compiler)
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.hilt.navigation.compose)
}

// Copy task
    tasks.register<Copy>("copyRomTools") {
        from("src/main/resources")
        into(romToolsOutputDirectory)
        include("**/*.so", "**/*.bin", "**/*.img", "**/*.jar")
        includeEmptyDirs = false
        doFirst { romToolsOutputDirectory.get().asFile.mkdirs(); logger.lifecycle("📁 ROM tools directory: ${romToolsOutputDirectory.get().asFile}") }
        doLast { logger.lifecycle("✅ ROM tools copied to: ${romToolsOutputDirectory.get().asFile}") }
    }

// Verification task
    tasks.register("verifyRomTools") {
        dependsOn("copyRomTools")
    }

    tasks.named("build") { dependsOn("verifyRomTools") }

    tasks.register("romStatus") {
        group = "aegenesis"; doLast { println("🛠️ ROM TOOLS - Ready (Java 24)") }
    }

// Add modern documentation task that doesn't rely on deprecated plugins
    tasks.register("generateApiDocs") {
        group = "documentation"
        description = "Generates API documentation without relying on deprecated plugins"

        doLast {
            logger.lifecycle("🔍 Generating API documentation for romtools module")
            logger.lifecycle("📂 Source directories:")
            logger.lifecycle("   - ${projectDir.resolve("src/main/kotlin")}")
            logger.lifecycle("   - ${projectDir.resolve("src/main/java")}")

            // Using layout.buildDirectory instead of deprecated buildDir property
            val docsDir = layout.buildDirectory.dir("docs/api").get().asFile
            docsDir.mkdirs()

            val indexFile = docsDir.resolve("index.html")
            indexFile.writeText(
                """
        <!DOCTYPE html>
        <html>
        <head>
            <title>ROM Tools API Documentation</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 20px; }
                h1 { color: #4285f4; }
            </style>
        </head>
        <body>
            <h1>ROM Tools API Documentation</h1>
            <p>Generated on ${
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                }</p>
            <p>JDK Version: 24</p>
            <h2>Module Overview</h2>
            <p>System modification and ROM tools for the A.U.R.A.K.A.I. platform.</p>
        </body>
        </html>
    """.trimIndent()
            )

            logger.lifecycle("✅ Documentation generated at: ${indexFile.absolutePath}")
        }
    }

    packaging {
        resources {
            excludes += "META-INF/LICENSE.md"
        }
    }
}

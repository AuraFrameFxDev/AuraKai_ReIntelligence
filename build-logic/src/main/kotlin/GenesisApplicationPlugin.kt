import com.android.build.gradle.AppExtension
import org.gradle.api.JavaVersion
import org.gradle.api.JavaVersion.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * ===================================================================
 * GENESIS APPLICATION CONVENTION PLUGIN
 * ===================================================================
 *
 * The primary convention plugin for Android application modules.
 *
 * Plugin Application Order (Critical!):
 * 1. Android Application
 * 2. Hilt (Dependency Injection)
 * 3. KSP (Annotation Processing)
 * 4. Compose Compiler
 * 5. Google Services (Firebase)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisApplicationPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ===== STEP 1: APPLY PLUGINS IN ORDER =====
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.google.dagger.hilt.android")
                apply("com.google.devtools.ksp")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("com.google.gms.google-services")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            // ===== STEP 2: CONFIGURE ANDROID =====
            extensions.configure<AppExtension> {
                compileSdkVersion(libs.findVersion("compileSdk").get().toString().toInt())

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                    targetSdk = libs.findVersion("targetSdk").get().toString().toInt()
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    vectorDrawables {
                        useSupportLibrary = true
                    }
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }

                    getByName("debug") {
                        isMinifyEnabled = false
                        applicationIdSuffix = ".debug"
                        versionNameSuffix = "-DEBUG"
                    }
                }

                // ===== PACKAGING OPTIONS =====
                packagingOptions {
                    exclude("META-INF/AL2.0")
                    exclude("META-INF/LGPL2.1")
                    exclude("META-INF/DEPENDENCIES")
                    exclude("META-INF/LICENSE")
                    exclude("META-INF/LICENSE.txt")
                    exclude("META-INF/license.txt")
                    exclude("META-INF/NOTICE")
                    exclude("META-INF/NOTICE.txt")
                    exclude("META-INF/notice.txt")
                    exclude("META-INF/ASL2.0")
                    exclude("META-INF/*.kotlin_module")
                    exclude("META-INF/INDEX.LIST")
                    exclude("META-INF/LICENSE.md")
                    exclude("META-INF/gradle/incremental.annotation.processors")
                    exclude("META-INF/androidx/room/room-compiler-processing/LICENSE.txt")
                }

                // ===== LINT CONFIGURATION =====
                lintOptions {
                    isAbortOnError = false
                    isWarningsAsErrors = false
                    disable("InvalidPackage", "OldTargetApi", "GradleDependency")
                    isCheckReleaseBuilds = false
                }

                compileOptions {
                    sourceCompatibility = VERSION_24
                    targetCompatibility = VERSION_24
                }
            }



            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    // Set the JVM target version
                    // With Canary 5 and modern Kotlin, JVM_21 is fully supported

                    // Use the optIn property for enabling experimental APIs
                    optIn.addAll(
                        "kotlin.RequiresOptIn",
                        "kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "kotlinx.coroutines.FlowPreview",
                        "kotlin.time.ExperimentalTime",
                    )

                    // Keep using freeCompilerArgs for non-standard flags
                    freeCompilerArgs.addAll(
                        "-Xjvm-default=all",
                        "-Xcontext-receivers",
                    )
                }
            }


            // ===== STEP 4: KSP CLEANUP TASK =====
            tasks.register<Delete>("cleanKspCache") {
                group = "genesis"
                description = "🧹 Clean KSP caches to prevent annotation processing issues"

                delete(
                    layout.buildDirectory.dir("generated/ksp"),
                    layout.buildDirectory.dir("generated/source/ksp"),
                    layout.buildDirectory.dir("tmp/kapt3"),
                    layout.buildDirectory.dir("tmp/kotlin-classes"),
                    layout.buildDirectory.dir("kotlin")
                )
            }

            tasks.named("preBuild") {
                dependsOn("cleanKspCache")
            }

            // ===== STEP 5: CORE LIBRARY DESUGARING =====
            dependencies.add("coreLibraryDesugaring", libs.findLibrary("desugar.jdk.libs").get())
        }
    }
}


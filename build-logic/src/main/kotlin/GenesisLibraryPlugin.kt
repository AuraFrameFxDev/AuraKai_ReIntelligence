import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * ===================================================================
 * GENESIS LIBRARY CONVENTION PLUGIN
 * ===================================================================
 *
 * Convention plugin for Android library modules.
 *
 * Plugin Application Order:
 * 1. Android Library
 * 2. Kotlin Android
 * 3. Hilt (for DI)
 * 4. Compose Compiler
 * 5. Genesis Base (serialization)
 *
 * Java Support: 21 (latest supported)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // ===== STEP 1: APPLY PLUGINS =====
            with(pluginManager) {
                apply("com.android.library")
                // Delay Hilt application until after Android plugin is initialized
            }

            // Ensure Hilt is applied only after Android plugin is ready
            plugins.withId("com.android.library") {
                pluginManager.apply("com.google.dagger.hilt.android")
            }

            with(pluginManager) {
                apply("genesis.android.base") // Adds serialization
            }

            extensions.getByType<VersionCatalogsExtension>().named("libs")

            // ===== STEP 2: CONFIGURE ANDROID LIBRARY =====
            extensions.configure<LibraryExtension> {
                compileSdk = 36

                defaultConfig {
                    minSdk = 34
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                buildFeatures {
                    compose = true
                    buildConfig = false
                }

                // ===== JAVA 21 =====
                compileOptions {
                    sourceCompatibility = determineJavaVersion()
                    targetCompatibility = determineJavaVersion()
                }

                // ===== PACKAGING =====
                packaging {
                    resources.excludes.addAll(
                        listOf(
                            "META-INF/AL2.0",
                            "META-INF/LGPL2.1",
                            "META-INF/*.kotlin_module"
                        )
                    )
                }

                // ===== LINT =====
                lint {
                    abortOnError = false
                }
            }

            // ===== STEP 3: CONFIGURE KOTLIN =====
            tasks.withType(KotlinCompile::class.java).configureEach {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-Xjvm-default=all"
                    )
                }
            }
        }
    }

    private fun determineJavaVersion(): JavaVersion {
        return JavaVersion.VERSION_24
    }
}

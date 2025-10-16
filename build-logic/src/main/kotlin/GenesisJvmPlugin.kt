import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * ===================================================================
 * GENESIS JVM CONVENTION PLUGIN
 * ===================================================================
 *
 * Convention plugin for pure Kotlin/JVM modules (non-Android).
 *
 * Plugin Application Order:
 * 1. Kotlin JVM
 * 2. Serialization
 *
 * Java Support: 25 (primary), 24 (fallback)
 *
 * @since Genesis Protocol 1.0
 */
class GenesisJvmPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            // ===== JAVA CONFIGURATION =====
            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = determineJavaVersion()
                targetCompatibility = determineJavaVersion()

                toolchain {
                    languageVersion.set(
                        org.gradle.jvm.toolchain.JavaLanguageVersion.of(
                            determineJavaVersionNumber()
                        )
                    )
                }
            }

            // ===== KOTLIN JVM CONFIGURATION =====
            tasks.withType<KotlinCompile>().configureEach {
                compilerOptions {
                    jvmTarget.set(determineJvmTarget())
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-Xjvm-default=all",
                        "-Xcontext-receivers"
                    )
                }
            }
        }
    }

    private fun determineJavaVersion(): JavaVersion {
        return try {
            JavaVersion.VERSION_25
        } catch (e: Exception) {
            JavaVersion.VERSION_24
        }
    }

    private fun determineJavaVersionNumber(): Int {
        return try {
            25
        } catch (e: Exception) {
            24
        }
    }

    private fun determineJvmTarget(): JvmTarget {
        // JVM_25 is not available in Kotlin DSL as of 2025; use JVM_21 as the highest supported.
        return try {
            JvmTarget.JVM_21
        } catch (e: Exception) {
            JvmTarget.JVM_17
        }
    }
}

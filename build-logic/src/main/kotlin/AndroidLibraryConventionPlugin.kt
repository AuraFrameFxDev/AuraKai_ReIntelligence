// ==== GENESIS PROTOCOL - ANDROID LIBRARY CONVENTION ====
// Standard Android library configuration for all modules
// AGP 9.0+ with built-in Kotlin support

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    /**
     * Applies Android library conventions to the given Gradle project.
     *
     * Configures the project by applying the Android library and base plugins, setting Android SDK levels
     * from the version catalog, enforcing Java 24 for compilation, and configuring Kotlin Android compiler options.
     *
     * - Applies "com.android.library" and "genesis.android.base".
     * - Sets Android compileSdk and defaultConfig.minSdk from the `libs` version catalog.
     * - Sets Java sourceCompatibility and targetCompatibility to Java 24.
     * - If the Kotlin Android extension is present, sets Kotlin `jvmTarget` to JVM_24 and adds the compiler args
     *   `-opt-in=kotlin.RequiresOptIn` and `-Xjvm-default=all`.
     *
     * @param target The Gradle project to configure; this method mutates the project's plugins and extensions.
     */
    override fun apply(target: Project) {
        with(target) {
            // Do NOT apply the Hilt Gradle plugin in library modules (AGP 8+/9+)
            pluginManager.apply("com.android.library")

            // Apply Android library plugin and base plugin for Hilt + KSP
            with(pluginManager) {
                apply("genesis.android.base")  // Applies Hilt + KSP at the right time
                // ✅ REMOVED: AGP 9.0 has built-in Kotlin support
                // apply("org.jetbrains.kotlin.android")  // NO LONGER NEEDED
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

            extensions.configure<LibraryExtension> {
                compileSdk = libs.findVersion("compileSdk").get().toString().toInt()

                defaultConfig {
                    minSdk = libs.findVersion("minSdk").get().toString().toInt()
                }

                // ✅ Java 24 for consistency across all modules (Firebase requirement)
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_24
                    targetCompatibility = JavaVersion.VERSION_24
                }
            }
            // ✅ Configure Kotlin for Android
            extensions.findByType(KotlinAndroidProjectExtension::class.java)?.apply {
                // jvmToolchain(24) // Removed: not supported in Android modules
                compilerOptions {
                    jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
                    freeCompilerArgs.addAll(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-Xjvm-default=all"
                    )
                }
            }
        }
    }
}
package dev.aurakai.auraframefx.ai.config

/**
 * Global configuration object for AI services
 */
val config = AIConfig.createDefault()

/**
 * Alternative configuration instances for different scenarios
 */
object ConfigProvider {
    val default = AIConfig.createDefault()
    val testing = AIConfig.createForTesting()
    
    fun getConfigForEnvironment(isDevelopment: Boolean = true): AIConfig {
        return if (isDevelopment) {
            testing
        } else {
            default
        }
    }
}
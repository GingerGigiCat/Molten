package com.hackclub.molten

import io.github.cdimascio.dotenv.dotenv
import java.io.File

object EnvFiles {
    private val fileValues: Map<String, String> by lazy { load() }

    fun get(name: String): String? =
        System.getenv(name)?.takeIf { it.isNotBlank() }
            ?: fileValues[name]?.takeIf { it.isNotBlank() }

    private fun load(): Map<String, String> {
        val root = findProjectRoot() ?: File(System.getProperty("user.dir"))
        val merged = mutableMapOf<String, String>()
        for (filename in listOf(".env", ".env.local")) {
            val dotenv = dotenv {
                directory = root.absolutePath
                this.filename = filename
                ignoreIfMissing = true
                ignoreIfMalformed = true
            }
            dotenv.entries().forEach { merged[it.key] = it.value }
        }
        return merged
    }

    private fun findProjectRoot(): File? {
        var dir: File? = File(System.getProperty("user.dir")).canonicalFile
        repeat(6) {
            val current = dir ?: return null
            if (File(current, "settings.gradle.kts").isFile) return current
            dir = current.parentFile
        }
        return null
    }
}

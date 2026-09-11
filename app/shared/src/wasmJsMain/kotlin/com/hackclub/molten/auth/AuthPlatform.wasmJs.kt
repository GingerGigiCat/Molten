package com.hackclub.molten.auth

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js
import web.location.location
import web.storage.localStorage

actual fun platformHttpClientEngine(): HttpClientEngineFactory<*> = Js

actual fun getAuthPlatform(): AuthPlatform = WebAuthPlatform()

internal class WebAuthPlatform : AuthPlatform {
    override val loginAvailable: Boolean = true

    override fun startLogin() {
        location.href = "$apiBaseUrl/auth/login"
    }

    override fun readStoredSession(): String? = localStorage.getItem(SESSION_STORAGE_KEY)

    override fun storeSession(token: String) {
        localStorage.setItem(SESSION_STORAGE_KEY, token)
    }

    override fun clearSession() {
        localStorage.removeItem(SESSION_STORAGE_KEY)
    }

    override fun consumeHashSession(): String? {
        val hash = location.hash.removePrefix("#")
        if (hash.isBlank()) return null
        val session = hash.split("&")
            .map { it.split("=", limit = 2) }
            .firstOrNull { it.firstOrNull() == "session" }
            ?.getOrNull(1)
            ?.takeIf { it.isNotBlank() }
        if (session != null) {
            storeSession(session)
            location.hash = ""
        }
        return session
    }
}

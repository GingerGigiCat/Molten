package com.hackclub.molten.auth

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.cio.CIO

actual fun platformHttpClientEngine(): HttpClientEngineFactory<*> = CIO

actual fun getAuthPlatform(): AuthPlatform = JvmAuthPlatform()

private class JvmAuthPlatform : AuthPlatform {
    override val loginAvailable: Boolean = false

    override fun startLogin() = Unit

    override fun readStoredSession(): String? = null

    override fun storeSession(token: String) = Unit

    override fun clearSession() = Unit

    override fun consumeHashSession(): String? = null
}

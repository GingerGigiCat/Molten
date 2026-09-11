package com.hackclub.molten.auth

import com.hackclub.molten.MeResponse
import com.hackclub.molten.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class AuthState(
    private val platform: AuthPlatform = getAuthPlatform(),
    private val httpClient: HttpClient = createAuthHttpClient(),
) {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _notice = MutableStateFlow<String?>(null)
    val notice: StateFlow<String?> = _notice.asStateFlow()

    val loginAvailable: Boolean get() = platform.loginAvailable

    suspend fun bootstrap() {
        val token = platform.consumeHashSession() ?: platform.readStoredSession()
        if (token != null) {
            loadUser(token)
        }
    }

    fun signIn() {
        if (!platform.loginAvailable) {
            _notice.value = "Sign in is available in the web app for now."
            return
        }
        platform.startLogin()
    }

    fun signOut() {
        platform.clearSession()
        _user.value = null
        _notice.value = null
    }

    private suspend fun loadUser(token: String) {
        try {
            val response = httpClient.get("${platform.apiBaseUrl}/api/me") {
                bearerAuth(token)
            }
            if (response.status == HttpStatusCode.Unauthorized) {
                platform.clearSession()
                _user.value = null
                return
            }
            if (!response.status.isSuccess()) {
                _notice.value = "Could not load profile (${response.status}). ${response.bodyAsText()}"
                return
            }
            val me = response.body<MeResponse>()
            platform.storeSession(token)
            _user.value = me.user
        } catch (e: Exception) {
            _notice.value = e.message ?: "Could not reach the Molten server."
        }
    }
}

fun createAuthHttpClient(): HttpClient = HttpClient(platformHttpClientEngine()) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}

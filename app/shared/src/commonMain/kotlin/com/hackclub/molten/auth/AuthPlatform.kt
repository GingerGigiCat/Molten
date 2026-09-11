package com.hackclub.molten.auth

const val DEFAULT_API_BASE_URL = "http://localhost:8080"
const val SESSION_STORAGE_KEY = "molten.session"

interface AuthPlatform {
    val loginAvailable: Boolean
    val apiBaseUrl: String
        get() = DEFAULT_API_BASE_URL

    fun startLogin()
    fun readStoredSession(): String?
    fun storeSession(token: String)
    fun clearSession()
    fun consumeHashSession(): String?
}

expect fun getAuthPlatform(): AuthPlatform

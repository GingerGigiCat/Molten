package com.hackclub.molten

data class AuthConfig(
    val clientId: String,
    val clientSecret: String,
    val sessionSecret: String,
    val publicUrl: String,
    val webOrigin: String,
) {
    val callbackUrl: String get() = "${publicUrl.trimEnd('/')}/auth/callback"
    val oauthConfigured: Boolean get() = clientId.isNotBlank() && clientSecret.isNotBlank()

    companion object {
        const val COMMUNITY_SCOPES =
            "openid profile email name slack_id verification_status"

        const val AUTHORIZE_URL = "https://auth.hackclub.com/oauth/authorize"
        const val TOKEN_URL = "https://auth.hackclub.com/oauth/token"
        const val ME_URL = "https://auth.hackclub.com/api/v1/me"

        fun fromEnvironment(): AuthConfig = AuthConfig(
            clientId = env("HACKCLUB_CLIENT_ID", ""),
            clientSecret = env("HACKCLUB_CLIENT_SECRET", ""),
            sessionSecret = env(
                "SESSION_SECRET",
                "dev-insecure-molten-session-secret-change-me",
            ),
            publicUrl = env("PUBLIC_URL", "http://localhost:8080").trimEnd('/'),
            webOrigin = env("WEB_ORIGIN", "http://localhost:8082").trimEnd('/'),
        )

        private fun env(name: String, default: String): String =
            EnvFiles.get(name) ?: default
    }
}

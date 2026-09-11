package com.hackclub.molten

import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.server.auth.AuthenticationConfig
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.bearer
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.util.generateNonce

private const val OAUTH_STATE_COOKIE = "molten_oauth_state"

data class UserPrincipal(val user: User) : Principal

fun AuthenticationConfig.moltenSession(config: AuthConfig) {
    bearer("molten-session") {
        realm = "Molten"
        authenticate { credential ->
            SessionJwt.verify(credential.token, config.sessionSecret)?.let { UserPrincipal(it) }
        }
    }
}

fun Route.authRoutes(config: AuthConfig, hackClub: HackClubClient) {
    get("/auth/login") {
        if (!config.oauthConfigured) {
            call.respondText(
                "Hack Club Auth is not configured. Set HACKCLUB_CLIENT_ID and HACKCLUB_CLIENT_SECRET.",
                status = HttpStatusCode.ServiceUnavailable,
            )
            return@get
        }
        val state = generateNonce()
        call.response.cookies.append(
            Cookie(
                name = OAUTH_STATE_COOKIE,
                value = state,
                path = "/",
                httpOnly = true,
                maxAge = 600,
            )
        )
        call.respondRedirect(authorizeUrl(config, state))
    }

    get("/auth/callback") {
        if (!config.oauthConfigured) {
            call.respond(HttpStatusCode.ServiceUnavailable)
            return@get
        }

        val expected = call.request.cookies[OAUTH_STATE_COOKIE]
        val state = call.request.queryParameters["state"]
        val code = call.request.queryParameters["code"]
        val error = call.request.queryParameters["error"]

        if (error != null) {
            call.respondText("Hack Club Auth error: $error", status = HttpStatusCode.BadGateway)
            return@get
        }
        if (expected == null || state == null || state != expected || code.isNullOrBlank()) {
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        call.response.cookies.append(
            Cookie(name = OAUTH_STATE_COOKIE, value = "", path = "/", maxAge = 0)
        )

        val tokens = hackClub.exchangeCode(code)
        val user = hackClub.fetchMe(tokens.accessToken).identity.toUser()
        val session = SessionJwt.create(user, config.sessionSecret)
        call.respondRedirect("${config.webOrigin}/#session=$session")
    }

    post("/auth/logout") {
        call.respond(HttpStatusCode.NoContent)
    }

    authenticate("molten-session") {
        get("/api/me") {
            val principal = call.principal<UserPrincipal>()!!
            call.respond(MeResponse(principal.user))
        }
    }
}

private fun authorizeUrl(config: AuthConfig, state: String): String {
    return URLBuilder(AuthConfig.AUTHORIZE_URL).apply {
        parameters.append("client_id", config.clientId)
        parameters.append("redirect_uri", config.callbackUrl)
        parameters.append("response_type", "code")
        parameters.append("scope", AuthConfig.COMMUNITY_SCOPES)
        parameters.append("state", state)
    }.buildString()
}

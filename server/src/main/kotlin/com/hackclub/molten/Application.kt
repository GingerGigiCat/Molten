package com.hackclub.molten

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(config: AuthConfig = AuthConfig.fromEnvironment()) {
    val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val httpClient = HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json(json)
        }
    }
    monitor.subscribe(ApplicationStopped) {
        httpClient.close()
    }

    install(ContentNegotiation) {
        json(json)
    }
    install(Authentication) {
        moltenSession(config)
    }
    install(CORS) {
        val origin = Url(config.webOrigin)
        val host = if (origin.port == origin.protocol.defaultPort) {
            origin.host
        } else {
            "${origin.host}:${origin.port}"
        }
        allowHost(host, schemes = listOf(origin.protocol.name))
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
    }

    val hackClub = HackClubClient(httpClient, config)

    routing {
        get("/") {
            call.respondText(sayHello("Ktor"))
        }
        authRoutes(config, hackClub)
    }
}

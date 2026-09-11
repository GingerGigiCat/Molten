package com.hackclub.molten

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    private val testConfig = AuthConfig(
        clientId = "",
        clientSecret = "",
        sessionSecret = "test-molten-session-secret-32bytes-min",
        publicUrl = "http://localhost:8080",
        webOrigin = "http://localhost:8082",
    )

    @Test
    fun testRoot() = testApplication {
        application {
            module(testConfig)
        }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, Ktor!", response.bodyAsText())
    }

    @Test
    fun meUnauthorizedWithoutToken() = testApplication {
        application {
            module(testConfig)
        }
        val response = client.get("/api/me")
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun meUnauthorizedWithInvalidJwt() = testApplication {
        application {
            module(testConfig)
        }
        val response = client.get("/api/me") {
            header(HttpHeaders.Authorization, "Bearer not-a-real-jwt")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun meUnauthorizedWithForgedJwt() = testApplication {
        application {
            module(testConfig)
        }
        val forged = SessionJwt.create(
            User(id = "ident!forged", firstName = "Nope"),
            secret = "some-other-secret-that-is-also-32b!",
        )
        val response = client.get("/api/me") {
            header(HttpHeaders.Authorization, "Bearer $forged")
        }
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun meReturnsUserForValidSession() = testApplication {
        application {
            module(testConfig)
        }
        val user = User(id = "ident!test", firstName = "Heidi", email = "heidi@hackclub.com")
        val jwt = SessionJwt.create(user, testConfig.sessionSecret)
        val response = client.get("/api/me") {
            header(HttpHeaders.Authorization, "Bearer $jwt")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("ident!test"))
        assertTrue(body.contains("Heidi"))
    }

    @Test
    fun loginUnavailableWithoutCredentials() = testApplication {
        application {
            module(testConfig)
        }
        val response = client.get("/auth/login")
        assertEquals(HttpStatusCode.ServiceUnavailable, response.status)
    }
}

package com.hackclub.molten

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.Date

object SessionJwt {
    const val ISSUER = "molten"
    private val json = Json { ignoreUnknownKeys = true }

    fun create(user: User, secret: String, ttlSeconds: Long = 60L * 60L * 24L * 7L): String {
        val expiresAt = Date.from(Instant.now().plusSeconds(ttlSeconds))
        return JWT.create()
            .withIssuer(ISSUER)
            .withSubject(user.id)
            .withClaim("user", json.encodeToString(User.serializer(), user))
            .withExpiresAt(expiresAt)
            .sign(Algorithm.HMAC256(secret))
    }

    fun verify(token: String, secret: String): User? {
        return try {
            val decoded = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer(ISSUER)
                .build()
                .verify(token)
            val payload = decoded.getClaim("user").asString() ?: return null
            json.decodeFromString(User.serializer(), payload)
        } catch (_: Exception) {
            null
        }
    }
}

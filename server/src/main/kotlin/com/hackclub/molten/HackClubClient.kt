package com.hackclub.molten

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class HackClubClient(
    private val httpClient: HttpClient,
    private val config: AuthConfig,
) {
    suspend fun exchangeCode(code: String): TokenResponse {
        return httpClient.post(AuthConfig.TOKEN_URL) {
            contentType(ContentType.Application.Json)
            setBody(
                TokenRequest(
                    clientId = config.clientId,
                    clientSecret = config.clientSecret,
                    redirectUri = config.callbackUrl,
                    code = code,
                    grantType = "authorization_code",
                )
            )
        }.body()
    }

    suspend fun fetchMe(accessToken: String): HackClubMeResponse {
        return httpClient.get(AuthConfig.ME_URL) {
            bearerAuth(accessToken)
        }.body()
    }
}

@Serializable
private data class TokenRequest(
    @SerialName("client_id") val clientId: String,
    @SerialName("client_secret") val clientSecret: String,
    @SerialName("redirect_uri") val redirectUri: String,
    val code: String,
    @SerialName("grant_type") val grantType: String,
)

@Serializable
data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("id_token") val idToken: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("expires_in") val expiresIn: Long? = null,
    val scope: String? = null,
)

@Serializable
data class HackClubMeResponse(
    val identity: HackClubIdentity,
    val scopes: List<String> = emptyList(),
)

@Serializable
data class HackClubIdentity(
    val id: String,
    @SerialName("ysws_eligible") val yswsEligible: Boolean? = null,
    @SerialName("verification_status") val verificationStatus: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("primary_email") val primaryEmail: String? = null,
    @SerialName("slack_id") val slackId: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val birthday: String? = null,
    @SerialName("legal_first_name") val legalFirstName: String? = null,
    @SerialName("legal_last_name") val legalLastName: String? = null,
    val addresses: List<HackClubAddress>? = null,
)

@Serializable
data class HackClubAddress(
    val id: String? = null,
    @SerialName("first_name") val firstName: String? = null,
    @SerialName("last_name") val lastName: String? = null,
    @SerialName("line_1") val line1: String? = null,
    @SerialName("line_2") val line2: String? = null,
    val city: String? = null,
    val state: String? = null,
    @SerialName("postal_code") val postalCode: String? = null,
    val country: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    val primary: Boolean? = null,
)

fun HackClubIdentity.toUser(): User = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = primaryEmail,
    slackId = slackId,
    verificationStatus = verificationStatus,
    yswsEligible = yswsEligible,
    phoneNumber = phoneNumber,
    birthday = birthday,
    legalFirstName = legalFirstName,
    legalLastName = legalLastName,
    addresses = addresses?.map {
        UserAddress(
            id = it.id,
            firstName = it.firstName,
            lastName = it.lastName,
            line1 = it.line1,
            line2 = it.line2,
            city = it.city,
            state = it.state,
            postalCode = it.postalCode,
            country = it.country,
            phoneNumber = it.phoneNumber,
            primary = it.primary,
        )
    },
)

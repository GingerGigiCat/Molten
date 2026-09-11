package com.hackclub.molten

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val slackId: String? = null,
    val verificationStatus: String? = null,
    val yswsEligible: Boolean? = null,
    val phoneNumber: String? = null,
    val birthday: String? = null,
    val legalFirstName: String? = null,
    val legalLastName: String? = null,
    val addresses: List<UserAddress>? = null,
) {
    val displayName: String
        get() = listOfNotNull(firstName, lastName)
            .joinToString(" ")
            .ifBlank { email ?: id }
}

@Serializable
data class UserAddress(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val line1: String? = null,
    val line2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null,
    val country: String? = null,
    val phoneNumber: String? = null,
    val primary: Boolean? = null,
)

@Serializable
data class MeResponse(
    val user: User,
)

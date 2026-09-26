package com.hackclub.molten

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val demoUrl: String,
    val repoUrl: String,
    val readmeUrl: String,
    val approvalState: ApprovalState,
    val workedHours: Float,
    val approvedHours: Float,
    val deniedHours: Float,
    val reviews: List<Review>,
    val headerImgUrl: String,
    val screenshotImgUrl: String
)

@Serializable
enum class ApprovalState {
    @Serializable
    DraftState,
    @Serializable
    PendingReviewState,
    @Serializable
    PendingFraudState,
    @Serializable
    ApprovedState
}

@Serializable
data class Review(
    val id: String,
    val projectId: String,
    val externalComment: String,
    val internalComment: String, // also uh the internal comment should probably not be passed to the user frontend
    val timestamp: String, // ISO-8601

)
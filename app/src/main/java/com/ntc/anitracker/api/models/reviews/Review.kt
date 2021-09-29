package com.ntc.anitracker.api.models.reviews

data class Review(
    val content: String,
    val date: String?,
    val helpful_count: Int,
    val mal_id: Int,
    val reviewer: Reviewer,
    val type: Any,
    val url: String
)
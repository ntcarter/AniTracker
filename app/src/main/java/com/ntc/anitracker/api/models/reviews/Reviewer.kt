package com.ntc.anitracker.api.models.reviews

data class Reviewer(
    val episodes_seen: Int?,
    val chapters_read: Int?,
    val image_url: String,
    val scores: Scores,
    val url: String,
    val username: String
)
package com.ntc.anitracker.api.models.recommendations

data class Recommendation(
    val image_url: String,
    val mal_id: Int,
    val recommendation_count: Int,
    val recommendation_url: String,
    val title: String,
    val url: String
)
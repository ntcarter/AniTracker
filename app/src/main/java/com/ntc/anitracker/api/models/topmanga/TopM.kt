package com.ntc.anitracker.api.models.topmanga

data class TopM(
    val end_date: Any,
    val image_url: String,
    val mal_id: Int,
    val members: Int,
    val rank: Int,
    val score: Double,
    val start_date: String,
    val title: String,
    val type: String,
    val url: String,
    val volumes: Any
)
package com.ntc.anitracker.api.models.episode

data class Episode(
    val aired: String?,
    val episode_id: Int,
    val filler: Boolean,
    val forum_url: String,
    val recap: Boolean,
    val title: String,
    val title_japanese: String,
    val title_romanji: String,
    val video_url: String
)
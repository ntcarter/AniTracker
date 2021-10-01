package com.ntc.anitracker.api.models.anime

data class CombinedRelated(
    val mal_id: Int,
    val name: String,
    val relatedType: String,
    val animeType: String?
)
package com.ntc.anitracker.api.models.charactersandstaff

data class Staff(
    val image_url: String,
    val mal_id: Int,
    val name: String,
    val positions: List<String>,
    val url: String
)
package com.ntc.anitracker.api.models.topanime

data class TopAnime(
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val top: List<TopA>
)
package com.ntc.anitracker.api.models.topmanga

data class TopManga(
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val top: List<TopM>
)
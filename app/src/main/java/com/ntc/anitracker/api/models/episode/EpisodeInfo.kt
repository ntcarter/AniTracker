package com.ntc.anitracker.api.models.episode

data class EpisodeInfo(
    val episodes: List<Episode>,
    val episodes_last_page: Int,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String
)
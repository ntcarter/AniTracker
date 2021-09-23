package com.ntc.anitracker.api.models.recommendations

data class Recommendations(
    val recommendations: List<Recommendation>,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String
)
package com.ntc.anitracker.api.models.reviews

data class Reviews(
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val reviews: List<Review>?
)
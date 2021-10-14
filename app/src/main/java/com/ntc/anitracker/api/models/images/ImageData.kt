package com.ntc.anitracker.api.models.images

data class ImageData(
    val pictures: List<Picture>,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String
)
package com.ntc.anitracker.api.models.charactersandstaff

data class CharactersAndStaff(
    val characters: List<Character>,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val staff: List<Staff>
)
package com.ntc.anitracker.api.models.person

data class Person(
    val about: String?,
    val alternate_names: List<String>?,
    val anime_staff_positions: List<AnimeStaffPosition>?,
    val birthday: String?,
    val family_name: String?,
    val given_name: String?,
    val image_url: String,
    val mal_id: Int,
    val member_favorites: Int,
    val name: String?,
    val published_manga: List<PublishedManga>?,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val url: String,
    val voice_acting_roles: List<VoiceActingRole>?,
    val website_url: String?
)
package com.ntc.anitracker.api.models.characterdetails

import com.ntc.anitracker.api.models.charactersandstaff.VoiceActor

data class CharacterDetails(
    val about: String,
    val animeography: List<OGraphy>,
    val image_url: String,
    val mal_id: Int,
    val mangaography: List<OGraphy>,
    val member_favorites: Int,
    val name: String,
    val name_kanji: String,
    val nicknames: List<Any>,
    val request_cache_expiry: Int,
    val request_cached: Boolean,
    val request_hash: String,
    val url: String,
    val voice_actors: List<VoiceActor>
)
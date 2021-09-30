package com.ntc.anitracker.api.models.person

data class VoiceActingRole(
    val anime: PersonAnimeX,
    val character: PersonCharacter,
    val role: String
)
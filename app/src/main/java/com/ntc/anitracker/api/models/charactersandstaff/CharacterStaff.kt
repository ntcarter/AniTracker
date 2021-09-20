package com.ntc.anitracker.api.models.charactersandstaff

/**
 * Data class that combines the Character and Staff object, keeping only the necessary information
 */
data class CharacterStaff(
    val image_url: String,
    val name: String,
    val role: String?,
    val voice_actors: List<VoiceActor>?,
    val positions: List<String>?,
)

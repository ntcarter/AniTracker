package com.ntc.anitracker.api.models.reviews

data class Scores(
    val animation: Int?,
    val character: Int,
    val enjoyment: Int,
    val art: Int?,
    val overall: Int,
    val sound: Int?,
    val story: Int
)
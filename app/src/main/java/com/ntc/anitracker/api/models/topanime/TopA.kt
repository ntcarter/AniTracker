package com.ntc.anitracker.api.models.topanime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopA(
    val end_date: String,
    val episodes: Int,
    val image_url: String,
    val mal_id: Int,
    val members: Int,
    val rank: Int,
    val score: Double,
    val start_date: String,
    val title: String,
    val type: String,
    val url: String
) : Parcelable
package com.ntc.anitracker.api.models.anime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SideStory(
    val mal_id: Int,
    val name: String,
    val type: String,
    val url: String
): Parcelable
package com.ntc.anitracker.api.models.anime

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Related(
    val Adaptation: List<Adaptation>?,
    val Alternative_version: List<AlternativeVersion>?,
    val Character: List<CharacterRelated>?,
    val Other: List<Other>?,
    val Sequel: List<Sequel>?,
    val Side_story: List<SideStory>?
): Parcelable
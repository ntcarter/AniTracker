package com.ntc.anitracker.api.models.anime

data class Related(
    val Adaptation: List<Adaptation>,
    val Alternative_version: List<AlternativeVersion>,
    val Side_story: List<SideStory>,
    val Spin_off: List<SpinOff>
)
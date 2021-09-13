package com.ntc.anitracker.api.models.manga

data class Related(
    val Adaptation: List<Adaptation>,
    val Other: List<Other>,
    val Spin_off: List<SpinOff>
)
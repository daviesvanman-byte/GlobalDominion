package com.globaldominion.data

data class VoiceProfile(
    val personName: String,
    val accent: String,
    val dialect: String,
    val age: Int,
    val gender: String,
    val speakingRate: Float,
    val pitch: Float,
    val personalityTone: PersonalityTraits
)

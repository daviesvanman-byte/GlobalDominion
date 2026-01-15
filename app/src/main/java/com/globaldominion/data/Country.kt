package com.globaldominion.data

import androidx.compose.ui.graphics.Color

data class Country(
    val id: String,
    val name: String,
    val flag: String,
    val government: Government,
    var gdp: Float,
    var militaryPower: Float,
    var publicOpinion: PublicOpinion,
    var escalationRisk: Float,
    var technology: Float = 50f,
    var resources: Float = 50f,
    val color: Color = Color.Gray,
    val aiState: AIState? = null,
    val voiceProfile: VoiceProfile? = null,
    var relations: MutableMap<String, Float> = mutableMapOf(),
    var isPlayerControlled: Boolean = false
)

data class PublicOpinion(
    var governmentSupport: Float,
    var protestIntensity: Float
)

enum class RelationType {
    ALLIED, FRIENDLY, NEUTRAL, TENSE, HOSTILE, AT_WAR
}

fun Float.toRelationType(): RelationType = when {
    this >= 80f -> RelationType.ALLIED
    this >= 60f -> RelationType.FRIENDLY
    this >= 40f -> RelationType.NEUTRAL
    this >= 20f -> RelationType.TENSE
    this >= 0f -> RelationType.HOSTILE
    else -> RelationType.AT_WAR
}

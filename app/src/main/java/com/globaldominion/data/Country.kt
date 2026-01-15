package com.globaldominion.data

import androidx.compose.ui.graphics.Color

// Beta architecture Country model
data class Country(
    val id: String = "",
    val name: String,
    val flag: String = "🏳️",
    val government: Government? = null,
    var economy: Economy = Economy(gdp = 1000.0, debt = 500.0, stability = 50),
    var military: Military = Military(strength = 50, nuclearCapable = false, readiness = 50),
    var publicOpinion: PublicOpinion = PublicOpinion(governmentSupport = 50f, protestIntensity = 20f),
    var publicOpinionData: PublicOpinionData = PublicOpinionData(approval = 50, unrest = 20),
    var escalationRisk: Float = 0f,
    var technology: Float = 50f,
    var resources: Float = 50f,
    val color: Color = Color.Gray,
    val aiState: AIState? = null,
    val voiceProfile: VoiceProfile? = null,
    var relations: MutableMap<String, Float> = mutableMapOf(),
    var isPlayerControlled: Boolean = false,
    var alliance: String? = null
) {
    // Legacy compatibility properties
    var gdp: Float
        get() = economy.gdp.toFloat()
        set(value) { economy = economy.copy(gdp = value.toDouble()) }
    
    var militaryPower: Float
        get() = military.strength.toFloat()
        set(value) { military = military.copy(strength = value.toInt()) }
}

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

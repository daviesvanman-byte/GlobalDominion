package com.globaldominion.data

data class Country(
    val name: String,
    val government: Government,
    var gdp: Float,
    var militaryPower: Float,
    var publicOpinion: PublicOpinion,
    var escalationRisk: Float,
    val aiState: AIState? = null,
    val voiceProfile: VoiceProfile? = null
)

data class PublicOpinion(
    var governmentSupport: Float,
    var protestIntensity: Float
)

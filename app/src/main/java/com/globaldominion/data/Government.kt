package com.globaldominion.data

enum class GovernmentType { DEMOCRACY, MONARCHY, AUTHORITARIAN, FEDERAL }

enum class Expertise { MILITARY, ECONOMY, FOREIGN_POLICY, INTERNAL_SECURITY, PUBLIC_OPINION }

data class Advisor(
    val name: String,
    val expertise: Expertise,
    val loyalty: Float,
    val assertiveness: Float,
    val adviceEffectiveness: Float
)

data class Government(
    val type: GovernmentType,
    val advisors: MutableList<Advisor>,
    val legislativeBodyEffectiveness: Float,
    val intelligenceAgencyEffectiveness: Float,
    val publicCompliance: Float
)

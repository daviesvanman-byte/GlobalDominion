package com.globaldominion.data

enum class DecisionCategory { MILITARY, ECONOMY, TECHNOLOGY, FOREIGN_POLICY, DOMESTIC }

data class DecisionInput(
    val category: DecisionCategory,
    val text: String
)

data class Decision(
    val id: String,
    val title: String,
    val description: String,
    val category: DecisionCategory,
    val effects: List<DecisionEffect>,
    val cost: Float = 0f,
    val targetCountry: String? = null
)

data class DecisionEffect(
    val stat: StatType,
    val change: Float,
    val targetSelf: Boolean = true
)

enum class StatType {
    GDP, MILITARY, SUPPORT, PROTEST, TECHNOLOGY, RESOURCES, ESCALATION, RELATIONS
}

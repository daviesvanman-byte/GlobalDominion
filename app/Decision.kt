package com.globaldominion.data

enum class DecisionCategory { MILITARY, ECONOMY, TECHNOLOGY, FOREIGN_POLICY, DOMESTIC }

data class DecisionInput(
    val category: DecisionCategory,
    val text: String
)

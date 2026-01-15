package com.globaldominion.data

import com.globaldominion.core.WorldState

class CountryAI(
    val country: Country,
    val personalityTraits: PersonalityTraits,
    val riskTolerance: Float,
    val decisionSpeed: Float
) {
    fun generateDecision(world: WorldState): String {
        return "No decision yet."
    }

    fun updateNeeds(world: WorldState) {
        // Update AI needs based on world state
    }
}

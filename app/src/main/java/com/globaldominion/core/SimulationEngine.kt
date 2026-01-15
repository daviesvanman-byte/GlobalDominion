package com.globaldominion.core

import com.globaldominion.ai.CountryAI
import com.globaldominion.ai.Personality

object SimulationEngine {

    fun tick() {
        WorldStateManager.year++

        WorldStateManager.countries.forEach {
            val ai = CountryAI(Personality(
                aggressive = it.military.strength > 70,
                riskTolerance = 50,
                rationality = 60
            ))
            val result = ai.decide(it)
            WorldStateManager.news.add("${it.name}: $result")
        }
    }
    
    fun advanceTurn() {
        tick()
        
        // Update economic factors
        WorldStateManager.countries.forEach { country ->
            // Economic fluctuation
            val economicChange = (-5..5).random()
            country.economy = country.economy.copy(
                stability = (country.economy.stability + economicChange).coerceIn(0, 100)
            )
            
            // Public opinion shifts
            if (country.economy.stability < 30) {
                country.publicOpinionData.unrest += 5
                country.publicOpinionData.approval -= 3
            }
            
            // Military readiness decay
            if (country.military.readiness > 20) {
                country.military = country.military.copy(
                    readiness = country.military.readiness - 2
                )
            }
        }
    }
}

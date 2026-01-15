package com.globaldominion.systems

import com.globaldominion.core.WorldStateManager
import com.globaldominion.data.Country
import kotlin.random.Random

object ConsequenceEngine {
    
    fun processDecision(country: Country, decision: GameDecision) {
        // Check for cascading effects
        when (decision.type) {
            DecisionType.MILITARY -> processMilitaryConsequences(country, decision)
            DecisionType.ECONOMIC -> processEconomicConsequences(country, decision)
            DecisionType.DOMESTIC -> processDomesticConsequences(country, decision)
            DecisionType.DIPLOMATIC -> processDiplomaticConsequences(country, decision)
            DecisionType.TECHNOLOGY -> processTechnologyConsequences(country, decision)
        }
        
        // Random world reactions
        if (Random.nextFloat() < 0.3f) {
            generateWorldReaction(country, decision)
        }
    }
    
    private fun processMilitaryConsequences(country: Country, decision: GameDecision) {
        // Military buildup may increase tensions
        if (decision.id == "military_buildup") {
            country.escalationRisk += 5f
            
            // Other countries may react
            WorldStateManager.countries
                .filter { it.id != country.id && it.alliance != country.alliance }
                .forEach { other ->
                    if (Random.nextFloat() < 0.4f) {
                        other.military = other.military.copy(
                            readiness = (other.military.readiness + 5).coerceIn(0, 100)
                        )
                        NewsEngine.addMilitaryNews(
                            "increases military readiness in response to ${country.name}",
                            other.name
                        )
                    }
                }
        }
    }
    
    private fun processEconomicConsequences(country: Country, decision: GameDecision) {
        // Austerity may cause protests
        if (decision.id == "austerity" && country.publicOpinionData.unrest > 40) {
            if (Random.nextFloat() < 0.5f) {
                country.publicOpinionData.unrest += 10
                NewsEngine.addDomesticNews("Protests erupt over austerity measures", country.name)
            }
        }
        
        // Economic stimulus may affect allies
        if (decision.id == "stimulus" && country.alliance != null) {
            WorldStateManager.countries
                .filter { it.alliance == country.alliance && it.id != country.id }
                .forEach { ally ->
                    ally.economy = ally.economy.copy(
                        stability = (ally.economy.stability + 2).coerceIn(0, 100)
                    )
                }
        }
    }
    
    private fun processDomesticConsequences(country: Country, decision: GameDecision) {
        // Crackdown may cause international condemnation
        if (decision.id == "crackdown") {
            WorldStateManager.countries
                .filter { it.id != country.id }
                .forEach { other ->
                    // Democratic countries more likely to condemn
                    if (other.publicOpinionData.approval > 50 && Random.nextFloat() < 0.3f) {
                        NewsEngine.addDiplomaticNews(
                            "condemns human rights situation",
                            listOf(other.name, country.name)
                        )
                    }
                }
        }
    }
    
    private fun processDiplomaticConsequences(country: Country, decision: GameDecision) {
        // Diplomatic decisions affect relations
        country.escalationRisk = (country.escalationRisk - 3f).coerceIn(0f, 100f)
    }
    
    private fun processTechnologyConsequences(country: Country, decision: GameDecision) {
        // Tech advances may boost economy
        if (country.technology > 70 && Random.nextFloat() < 0.3f) {
            country.economy = country.economy.copy(
                gdp = country.economy.gdp * 1.02
            )
            NewsEngine.addEconomicNews("Tech sector drives economic growth", country.name)
        }
    }
    
    private fun generateWorldReaction(country: Country, decision: GameDecision) {
        val reactions = listOf(
            "International markets react to ${country.name}'s decision",
            "Analysts debate implications of ${country.name}'s ${decision.title}",
            "Global leaders watch ${country.name}'s moves closely",
            "Think tanks assess ${country.name}'s new policy direction"
        )
        NewsEngine.addBreakingNews(reactions.random())
    }
    
    fun processRandomEvent(): String? {
        if (Random.nextFloat() > 0.2f) return null
        
        val country = WorldStateManager.countries.randomOrNull() ?: return null
        
        val events = listOf(
            RandomEvent("Natural disaster strikes", { c ->
                c.economy = c.economy.copy(stability = c.economy.stability - 10)
                c.publicOpinionData.unrest += 5
            }),
            RandomEvent("Economic boom", { c ->
                c.economy = c.economy.copy(gdp = c.economy.gdp * 1.05)
                c.publicOpinionData.approval += 5
            }),
            RandomEvent("Political scandal", { c ->
                c.publicOpinionData.approval -= 10
                c.publicOpinionData.unrest += 8
            }),
            RandomEvent("Diplomatic breakthrough", { c ->
                c.escalationRisk -= 10f
            }),
            RandomEvent("Military incident", { c ->
                c.escalationRisk += 15f
                c.military = c.military.copy(readiness = c.military.readiness + 10)
            })
        )
        
        val event = events.random()
        event.effect(country)
        
        val headline = "${country.name}: ${event.name}"
        NewsEngine.addBreakingNews(headline)
        
        return headline
    }
}

private data class RandomEvent(
    val name: String,
    val effect: (Country) -> Unit
)

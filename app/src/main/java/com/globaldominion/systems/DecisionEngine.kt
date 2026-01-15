package com.globaldominion.systems

import com.globaldominion.core.WorldStateManager
import com.globaldominion.data.Country

enum class DecisionType {
    ECONOMIC, MILITARY, DIPLOMATIC, DOMESTIC, TECHNOLOGY
}

data class GameDecision(
    val id: String,
    val title: String,
    val description: String,
    val type: DecisionType,
    val cost: Int = 0,
    val effects: List<DecisionEffect>
)

data class DecisionEffect(
    val target: EffectTarget,
    val value: Int
)

enum class EffectTarget {
    GDP, DEBT, STABILITY, MILITARY_STRENGTH, MILITARY_READINESS,
    APPROVAL, UNREST, ESCALATION, TECHNOLOGY
}

object DecisionEngine {
    
    fun getAvailableDecisions(country: Country): List<GameDecision> {
        val decisions = mutableListOf<GameDecision>()
        
        // Economic decisions
        decisions.add(GameDecision(
            id = "stimulus",
            title = "Economic Stimulus",
            description = "Inject money into the economy to boost growth",
            type = DecisionType.ECONOMIC,
            cost = 100,
            effects = listOf(
                DecisionEffect(EffectTarget.GDP, 5),
                DecisionEffect(EffectTarget.DEBT, 10),
                DecisionEffect(EffectTarget.APPROVAL, 3)
            )
        ))
        
        decisions.add(GameDecision(
            id = "austerity",
            title = "Austerity Measures",
            description = "Cut spending to reduce debt",
            type = DecisionType.ECONOMIC,
            effects = listOf(
                DecisionEffect(EffectTarget.DEBT, -15),
                DecisionEffect(EffectTarget.APPROVAL, -10),
                DecisionEffect(EffectTarget.UNREST, 5)
            )
        ))
        
        // Military decisions
        decisions.add(GameDecision(
            id = "military_buildup",
            title = "Military Buildup",
            description = "Increase military strength and readiness",
            type = DecisionType.MILITARY,
            cost = 50,
            effects = listOf(
                DecisionEffect(EffectTarget.MILITARY_STRENGTH, 5),
                DecisionEffect(EffectTarget.MILITARY_READINESS, 10),
                DecisionEffect(EffectTarget.DEBT, 5)
            )
        ))
        
        decisions.add(GameDecision(
            id = "demobilize",
            title = "Demobilize Forces",
            description = "Reduce military spending",
            type = DecisionType.MILITARY,
            effects = listOf(
                DecisionEffect(EffectTarget.MILITARY_STRENGTH, -3),
                DecisionEffect(EffectTarget.DEBT, -5),
                DecisionEffect(EffectTarget.STABILITY, 2)
            )
        ))
        
        // Domestic decisions
        decisions.add(GameDecision(
            id = "social_programs",
            title = "Expand Social Programs",
            description = "Increase welfare and social safety nets",
            type = DecisionType.DOMESTIC,
            cost = 30,
            effects = listOf(
                DecisionEffect(EffectTarget.APPROVAL, 10),
                DecisionEffect(EffectTarget.UNREST, -8),
                DecisionEffect(EffectTarget.DEBT, 8)
            )
        ))
        
        decisions.add(GameDecision(
            id = "crackdown",
            title = "Security Crackdown",
            description = "Suppress civil unrest with force",
            type = DecisionType.DOMESTIC,
            effects = listOf(
                DecisionEffect(EffectTarget.UNREST, -15),
                DecisionEffect(EffectTarget.APPROVAL, -8),
                DecisionEffect(EffectTarget.STABILITY, 5)
            )
        ))
        
        // Technology decisions
        decisions.add(GameDecision(
            id = "research_funding",
            title = "Fund Research",
            description = "Increase R&D spending",
            type = DecisionType.TECHNOLOGY,
            cost = 40,
            effects = listOf(
                DecisionEffect(EffectTarget.TECHNOLOGY, 8),
                DecisionEffect(EffectTarget.DEBT, 5)
            )
        ))
        
        return decisions
    }
    
    fun executeDecision(country: Country, decision: GameDecision) {
        decision.effects.forEach { effect ->
            applyEffect(country, effect)
        }
        
        // Generate news
        NewsEngine.addBreakingNews(decision.title, country.name)
        
        // Trigger consequences
        ConsequenceEngine.processDecision(country, decision)
    }
    
    private fun applyEffect(country: Country, effect: DecisionEffect) {
        when (effect.target) {
            EffectTarget.GDP -> {
                country.economy = country.economy.copy(
                    gdp = country.economy.gdp + (country.economy.gdp * effect.value / 100)
                )
            }
            EffectTarget.DEBT -> {
                country.economy = country.economy.copy(
                    debt = country.economy.debt + (country.economy.debt * effect.value / 100)
                )
            }
            EffectTarget.STABILITY -> {
                country.economy = country.economy.copy(
                    stability = (country.economy.stability + effect.value).coerceIn(0, 100)
                )
            }
            EffectTarget.MILITARY_STRENGTH -> {
                country.military = country.military.copy(
                    strength = (country.military.strength + effect.value).coerceIn(0, 100)
                )
            }
            EffectTarget.MILITARY_READINESS -> {
                country.military = country.military.copy(
                    readiness = (country.military.readiness + effect.value).coerceIn(0, 100)
                )
            }
            EffectTarget.APPROVAL -> {
                country.publicOpinionData.approval = 
                    (country.publicOpinionData.approval + effect.value).coerceIn(0, 100)
            }
            EffectTarget.UNREST -> {
                country.publicOpinionData.unrest = 
                    (country.publicOpinionData.unrest + effect.value).coerceIn(0, 100)
            }
            EffectTarget.ESCALATION -> {
                country.escalationRisk = 
                    (country.escalationRisk + effect.value).coerceIn(0f, 100f)
            }
            EffectTarget.TECHNOLOGY -> {
                country.technology = 
                    (country.technology + effect.value).coerceIn(0f, 100f)
            }
        }
    }
}

package com.globaldominion.ai

import com.globaldominion.data.Country

enum class AdvisorType {
    MILITARY, ECONOMIC, DIPLOMATIC, INTELLIGENCE, DOMESTIC
}

data class Advisor(
    val name: String,
    val type: AdvisorType,
    val personality: Personality,
    val competence: Int = 50
)

class AdvisorAI(private val advisor: Advisor) {
    
    fun analyze(country: Country, situation: String): String {
        return when (advisor.type) {
            AdvisorType.MILITARY -> analyzeMilitary(country, situation)
            AdvisorType.ECONOMIC -> analyzeEconomic(country, situation)
            AdvisorType.DIPLOMATIC -> analyzeDiplomatic(country, situation)
            AdvisorType.INTELLIGENCE -> analyzeIntelligence(country, situation)
            AdvisorType.DOMESTIC -> analyzeDomestic(country, situation)
        }
    }
    
    private fun analyzeMilitary(country: Country, situation: String): String {
        return when {
            country.military.readiness < 30 -> "We need to increase military readiness immediately."
            country.military.strength < 40 -> "Our military strength is concerning. Consider defense spending."
            advisor.personality.aggressive -> "A show of force would be appropriate here."
            else -> "Our military position is stable."
        }
    }
    
    private fun analyzeEconomic(country: Country, situation: String): String {
        return when {
            country.economy.stability < 30 -> "Economic crisis imminent! Emergency measures needed."
            country.economy.debt > country.economy.gdp -> "Debt levels are unsustainable."
            country.economy.gdp < 500 -> "GDP is critically low. Focus on economic growth."
            else -> "Economy is performing within acceptable parameters."
        }
    }
    
    private fun analyzeDiplomatic(country: Country, situation: String): String {
        return when {
            country.alliance == null -> "We should consider joining an alliance for security."
            country.escalationRisk > 70 -> "Tensions are high. Diplomatic channels should be opened."
            else -> "Our diplomatic standing is stable."
        }
    }
    
    private fun analyzeIntelligence(country: Country, situation: String): String {
        return when {
            country.publicOpinionData.unrest > 50 -> "Intelligence suggests growing domestic unrest."
            country.escalationRisk > 60 -> "Foreign threats detected. Recommend heightened alert."
            else -> "No significant threats detected at this time."
        }
    }
    
    private fun analyzeDomestic(country: Country, situation: String): String {
        return when {
            country.publicOpinionData.approval < 30 -> "Public approval is critically low. Action needed."
            country.publicOpinionData.unrest > 60 -> "Civil unrest is reaching dangerous levels."
            else -> "Domestic situation is manageable."
        }
    }
    
    fun recommend(country: Country): List<String> {
        val recommendations = mutableListOf<String>()
        
        when (advisor.type) {
            AdvisorType.MILITARY -> {
                if (country.military.readiness < 50) recommendations.add("Increase military readiness")
                if (country.military.strength < 50) recommendations.add("Boost defense spending")
            }
            AdvisorType.ECONOMIC -> {
                if (country.economy.stability < 50) recommendations.add("Stabilize the economy")
                if (country.economy.debt > country.economy.gdp * 0.8) recommendations.add("Reduce national debt")
            }
            AdvisorType.DIPLOMATIC -> {
                if (country.escalationRisk > 50) recommendations.add("Open diplomatic channels")
                if (country.alliance == null) recommendations.add("Seek alliance membership")
            }
            AdvisorType.INTELLIGENCE -> {
                recommendations.add("Continue monitoring threats")
            }
            AdvisorType.DOMESTIC -> {
                if (country.publicOpinionData.approval < 50) recommendations.add("Improve public approval")
                if (country.publicOpinionData.unrest > 40) recommendations.add("Address civil unrest")
            }
        }
        
        return recommendations
    }
}

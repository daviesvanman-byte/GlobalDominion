package com.globaldominion.systems

import com.globaldominion.core.WorldStateManager

object NewsEngine {
    fun latest(): List<String> {
        return WorldStateManager.news.takeLast(5)
    }
    
    fun all(): List<String> {
        return WorldStateManager.news.toList()
    }
    
    fun addBreakingNews(headline: String, country: String? = null) {
        val prefix = country?.let { "[$it] " } ?: ""
        WorldStateManager.addNews("🔴 BREAKING: $prefix$headline")
    }
    
    fun addEconomicNews(headline: String, country: String) {
        WorldStateManager.addNews("📈 $country: $headline")
    }
    
    fun addMilitaryNews(headline: String, country: String) {
        WorldStateManager.addNews("⚔️ $country: $headline")
    }
    
    fun addDiplomaticNews(headline: String, countries: List<String>) {
        val involved = countries.joinToString(" & ")
        WorldStateManager.addNews("🤝 $involved: $headline")
    }
    
    fun addDomesticNews(headline: String, country: String) {
        WorldStateManager.addNews("🏠 $country: $headline")
    }
}

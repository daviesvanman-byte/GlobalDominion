package com.globaldominion.geopolitics

object EU {
    val alliance = Alliance(
        id = "EU",
        name = "European Union",
        type = AllianceType.ECONOMIC,
        members = mutableListOf(
            "germany", "france", "italy", "spain", "poland", "netherlands",
            "belgium", "greece", "portugal", "czech", "hungary", "romania",
            "bulgaria", "slovakia", "slovenia", "croatia", "austria",
            "ireland", "denmark", "finland", "sweden", "latvia", "lithuania",
            "estonia", "cyprus", "malta", "luxembourg"
        ),
        collectiveDefense = false,
        economicIntegration = true
    )
    
    // Eurozone members (subset of EU)
    val eurozoneMembers = listOf(
        "germany", "france", "italy", "spain", "netherlands", "belgium",
        "austria", "ireland", "portugal", "greece", "finland", "slovakia",
        "slovenia", "croatia", "latvia", "lithuania", "estonia", "cyprus",
        "malta", "luxembourg"
    )
    
    fun getTotalGDP(): Double {
        return alliance.getTotalGDP()
    }
    
    fun getEurozoneGDP(): Double {
        return alliance.getMemberCountries()
            .filter { eurozoneMembers.contains(it.id) }
            .sumOf { it.economy.gdp }
    }
    
    fun getAverageStability(): Int {
        val members = alliance.getMemberCountries()
        return if (members.isEmpty()) 0 else members.sumOf { it.economy.stability } / members.size
    }
    
    fun imposeSanctions(targetCountryId: String) {
        // EU sanctions affect trade and economy
        com.globaldominion.core.WorldStateManager.countries
            .find { it.id == targetCountryId }
            ?.let { target ->
                target.economy = target.economy.copy(
                    gdp = target.economy.gdp * 0.95,
                    stability = target.economy.stability - 5
                )
            }
    }
    
    init {
        AllianceManager.registerAlliance(alliance)
    }
}

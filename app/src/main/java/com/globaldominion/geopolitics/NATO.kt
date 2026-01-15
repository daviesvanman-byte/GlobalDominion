package com.globaldominion.geopolitics

object NATO {
    val alliance = Alliance(
        id = "NATO",
        name = "North Atlantic Treaty Organization",
        type = AllianceType.MILITARY,
        members = mutableListOf(
            "usa", "uk", "germany", "france", "canada", "italy", "spain",
            "poland", "netherlands", "belgium", "norway", "denmark",
            "portugal", "greece", "turkey", "czech", "hungary", "romania",
            "bulgaria", "slovakia", "slovenia", "croatia", "albania",
            "montenegro", "north_macedonia", "latvia", "lithuania", "estonia",
            "finland", "sweden"
        ),
        collectiveDefense = true,
        economicIntegration = false
    )
    
    // Article 5 - Collective Defense
    fun invokeArticle5(attackedCountry: String): List<String> {
        if (!alliance.members.contains(attackedCountry)) {
            return emptyList()
        }
        
        return alliance.members.filter { it != attackedCountry }
    }
    
    fun getTotalNuclearCapability(): Int {
        val nuclearMembers = listOf("usa", "uk", "france")
        return alliance.getMemberCountries()
            .filter { nuclearMembers.contains(it.id) }
            .count { it.military.nuclearCapable }
    }
    
    fun getReadinessLevel(): Int {
        val members = alliance.getMemberCountries()
        return if (members.isEmpty()) 0 else members.sumOf { it.military.readiness } / members.size
    }
    
    init {
        AllianceManager.registerAlliance(alliance)
    }
}

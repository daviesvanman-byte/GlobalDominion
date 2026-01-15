package com.globaldominion.geopolitics

import com.globaldominion.core.WorldStateManager
import com.globaldominion.data.Country

enum class AllianceType {
    MILITARY, ECONOMIC, POLITICAL, TRADE
}

data class Alliance(
    val id: String,
    val name: String,
    val type: AllianceType,
    val members: MutableList<String> = mutableListOf(),
    val collectiveDefense: Boolean = false,
    val economicIntegration: Boolean = false
) {
    fun addMember(countryId: String) {
        if (!members.contains(countryId)) {
            members.add(countryId)
            WorldStateManager.countries.find { it.id == countryId }?.let {
                it.alliance = id
            }
        }
    }
    
    fun removeMember(countryId: String) {
        members.remove(countryId)
        WorldStateManager.countries.find { it.id == countryId }?.let {
            it.alliance = null
        }
    }
    
    fun getMemberCountries(): List<Country> {
        return WorldStateManager.countries.filter { members.contains(it.id) }
    }
    
    fun getTotalMilitaryStrength(): Int {
        return getMemberCountries().sumOf { it.military.strength }
    }
    
    fun getTotalGDP(): Double {
        return getMemberCountries().sumOf { it.economy.gdp }
    }
}

object AllianceManager {
    private val alliances = mutableMapOf<String, Alliance>()
    
    fun registerAlliance(alliance: Alliance) {
        alliances[alliance.id] = alliance
    }
    
    fun getAlliance(id: String): Alliance? = alliances[id]
    
    fun getAllAlliances(): List<Alliance> = alliances.values.toList()
    
    fun getCountryAlliances(countryId: String): List<Alliance> {
        return alliances.values.filter { it.members.contains(countryId) }
    }
    
    fun areAllied(countryId1: String, countryId2: String): Boolean {
        return alliances.values.any { 
            it.members.contains(countryId1) && it.members.contains(countryId2) 
        }
    }
}

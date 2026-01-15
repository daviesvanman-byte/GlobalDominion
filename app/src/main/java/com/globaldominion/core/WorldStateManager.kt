package com.globaldominion.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.globaldominion.data.*

object WorldStateManager {
    var year = 2025
    val countries = mutableStateListOf<Country>()
    val news = mutableStateListOf<String>()
    val selectedCountry = mutableStateOf<Country?>(null)
    val playerCountry = mutableStateOf<Country?>(null)
    
    init {
        initializeWorld()
    }
    
    private fun initializeWorld() {
        val initialCountries = listOf(
            Country(
                id = "usa",
                name = "United States",
                flag = "🇺🇸",
                economy = Economy(gdp = 25000.0, debt = 31000.0, stability = 65),
                military = Military(strength = 100, nuclearCapable = true, readiness = 85),
                publicOpinionData = PublicOpinionData(approval = 45, unrest = 25),
                color = Color(0xFF3C3B6E),
                alliance = "NATO"
            ),
            Country(
                id = "china",
                name = "China",
                flag = "🇨🇳",
                economy = Economy(gdp = 18000.0, debt = 8000.0, stability = 75),
                military = Military(strength = 85, nuclearCapable = true, readiness = 80),
                publicOpinionData = PublicOpinionData(approval = 70, unrest = 15),
                color = Color(0xFFDE2910),
                alliance = null
            ),
            Country(
                id = "russia",
                name = "Russia",
                flag = "🇷🇺",
                economy = Economy(gdp = 2000.0, debt = 300.0, stability = 45),
                military = Military(strength = 80, nuclearCapable = true, readiness = 75),
                publicOpinionData = PublicOpinionData(approval = 60, unrest = 30),
                color = Color(0xFF0039A6),
                alliance = null
            ),
            Country(
                id = "uk",
                name = "United Kingdom",
                flag = "🇬🇧",
                economy = Economy(gdp = 3200.0, debt = 2800.0, stability = 60),
                military = Military(strength = 60, nuclearCapable = true, readiness = 70),
                publicOpinionData = PublicOpinionData(approval = 40, unrest = 35),
                color = Color(0xFF012169),
                alliance = "NATO"
            ),
            Country(
                id = "germany",
                name = "Germany",
                flag = "🇩🇪",
                economy = Economy(gdp = 4200.0, debt = 2500.0, stability = 70),
                military = Military(strength = 50, nuclearCapable = false, readiness = 55),
                publicOpinionData = PublicOpinionData(approval = 55, unrest = 20),
                color = Color(0xFFFFCC00),
                alliance = "NATO"
            ),
            Country(
                id = "france",
                name = "France",
                flag = "🇫🇷",
                economy = Economy(gdp = 2900.0, debt = 3100.0, stability = 55),
                military = Military(strength = 55, nuclearCapable = true, readiness = 65),
                publicOpinionData = PublicOpinionData(approval = 35, unrest = 45),
                color = Color(0xFF0055A4),
                alliance = "NATO"
            ),
            Country(
                id = "japan",
                name = "Japan",
                flag = "🇯🇵",
                economy = Economy(gdp = 4900.0, debt = 12000.0, stability = 65),
                military = Military(strength = 45, nuclearCapable = false, readiness = 60),
                publicOpinionData = PublicOpinionData(approval = 50, unrest = 15),
                color = Color(0xFFBC002D),
                alliance = null
            ),
            Country(
                id = "india",
                name = "India",
                flag = "🇮🇳",
                economy = Economy(gdp = 3500.0, debt = 2000.0, stability = 55),
                military = Military(strength = 65, nuclearCapable = true, readiness = 60),
                publicOpinionData = PublicOpinionData(approval = 55, unrest = 30),
                color = Color(0xFFFF9933),
                alliance = null
            ),
            Country(
                id = "brazil",
                name = "Brazil",
                flag = "🇧🇷",
                economy = Economy(gdp = 1900.0, debt = 1500.0, stability = 45),
                military = Military(strength = 40, nuclearCapable = false, readiness = 45),
                publicOpinionData = PublicOpinionData(approval = 40, unrest = 40),
                color = Color(0xFF009C3B),
                alliance = null
            ),
            Country(
                id = "australia",
                name = "Australia",
                flag = "🇦🇺",
                economy = Economy(gdp = 1700.0, debt = 800.0, stability = 70),
                military = Military(strength = 35, nuclearCapable = false, readiness = 55),
                publicOpinionData = PublicOpinionData(approval = 50, unrest = 15),
                color = Color(0xFF00008B),
                alliance = "NATO"
            )
        )
        
        countries.addAll(initialCountries)
        
        // Set USA as player country by default
        countries.find { it.id == "usa" }?.let {
            it.isPlayerControlled = true
            playerCountry.value = it
            selectedCountry.value = it
        }
        
        news.add("Welcome to Global Dominion - Year $year")
    }
    
    fun addNews(headline: String) {
        news.add(0, "[$year] $headline")
        if (news.size > 50) {
            news.removeAt(news.size - 1)
        }
    }
    
    fun reset() {
        year = 2025
        countries.clear()
        news.clear()
        initializeWorld()
    }
}

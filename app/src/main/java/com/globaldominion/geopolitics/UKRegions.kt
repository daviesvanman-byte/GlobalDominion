package com.globaldominion.geopolitics

data class UKRegion(
    val id: String,
    val name: String,
    val population: Long,
    var autonomyLevel: Int, // 0-100
    var independenceSupport: Int, // 0-100
    var economicOutput: Double,
    var politicalLeaning: PoliticalLeaning
)

enum class PoliticalLeaning {
    FAR_LEFT, LEFT, CENTER_LEFT, CENTER, CENTER_RIGHT, RIGHT, FAR_RIGHT
}

object UKRegions {
    val regions = mutableListOf(
        UKRegion(
            id = "england",
            name = "England",
            population = 56_000_000,
            autonomyLevel = 20,
            independenceSupport = 5,
            economicOutput = 2200.0,
            politicalLeaning = PoliticalLeaning.CENTER_RIGHT
        ),
        UKRegion(
            id = "scotland",
            name = "Scotland",
            population = 5_500_000,
            autonomyLevel = 60,
            independenceSupport = 45,
            economicOutput = 180.0,
            politicalLeaning = PoliticalLeaning.CENTER_LEFT
        ),
        UKRegion(
            id = "wales",
            name = "Wales",
            population = 3_100_000,
            autonomyLevel = 50,
            independenceSupport = 25,
            economicOutput = 80.0,
            politicalLeaning = PoliticalLeaning.LEFT
        ),
        UKRegion(
            id = "northern_ireland",
            name = "Northern Ireland",
            population = 1_900_000,
            autonomyLevel = 70,
            independenceSupport = 40, // Reunification with Ireland
            economicOutput = 55.0,
            politicalLeaning = PoliticalLeaning.CENTER
        )
    )
    
    fun getTotalPopulation(): Long = regions.sumOf { it.population }
    
    fun getTotalEconomicOutput(): Double = regions.sumOf { it.economicOutput }
    
    fun getAverageIndependenceSupport(): Int {
        return regions.sumOf { it.independenceSupport } / regions.size
    }
    
    fun processIndependenceMovement(regionId: String) {
        regions.find { it.id == regionId }?.let { region ->
            if (region.independenceSupport > 50) {
                // Independence referendum possible
                region.autonomyLevel = (region.autonomyLevel + 5).coerceAtMost(100)
            }
        }
    }
    
    fun updateRegionalPolitics() {
        regions.forEach { region ->
            // Economic factors affect independence support
            val ukCountry = com.globaldominion.core.WorldStateManager.countries
                .find { it.id == "uk" }
            
            ukCountry?.let { uk ->
                if (uk.economy.stability < 40) {
                    region.independenceSupport = (region.independenceSupport + 2).coerceAtMost(100)
                }
                if (uk.publicOpinionData.unrest > 50) {
                    region.independenceSupport = (region.independenceSupport + 1).coerceAtMost(100)
                }
            }
        }
    }
}

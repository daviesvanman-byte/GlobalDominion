package com.globaldominion.core

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.globaldominion.data.*
import kotlin.random.Random

class WorldState {
    val countries = mutableStateListOf<Country>()
    val newsEvents = mutableStateListOf<NewsEvent>()
    val turn = mutableStateOf(1)
    val year = mutableStateOf(2024)
    val selectedCountry = mutableStateOf<Country?>(null)
    val playerCountry = mutableStateOf<Country?>(null)
    
    init {
        initializeWorld()
    }
    
    private fun initializeWorld() {
        val initialCountries = listOf(
            createCountry("usa", "United States", "🇺🇸", GovernmentType.FEDERAL, Color(0xFF3C3B6E), gdp = 95f, military = 100f),
            createCountry("china", "China", "🇨🇳", GovernmentType.AUTHORITARIAN, Color(0xFFDE2910), gdp = 90f, military = 85f),
            createCountry("russia", "Russia", "🇷🇺", GovernmentType.AUTHORITARIAN, Color(0xFF0039A6), gdp = 55f, military = 80f),
            createCountry("uk", "United Kingdom", "🇬🇧", GovernmentType.DEMOCRACY, Color(0xFF012169), gdp = 70f, military = 60f),
            createCountry("germany", "Germany", "🇩🇪", GovernmentType.FEDERAL, Color(0xFFFFCC00), gdp = 75f, military = 50f),
            createCountry("france", "France", "🇫🇷", GovernmentType.DEMOCRACY, Color(0xFF0055A4), gdp = 68f, military = 55f),
            createCountry("japan", "Japan", "🇯🇵", GovernmentType.DEMOCRACY, Color(0xFFBC002D), gdp = 72f, military = 45f),
            createCountry("india", "India", "🇮🇳", GovernmentType.FEDERAL, Color(0xFFFF9933), gdp = 60f, military = 65f),
            createCountry("brazil", "Brazil", "🇧🇷", GovernmentType.FEDERAL, Color(0xFF009C3B), gdp = 50f, military = 40f),
            createCountry("australia", "Australia", "🇦🇺", GovernmentType.DEMOCRACY, Color(0xFF00008B), gdp = 55f, military = 35f)
        )
        
        countries.addAll(initialCountries)
        initializeRelations()
        
        // Set USA as player country by default
        countries.find { it.id == "usa" }?.let {
            it.isPlayerControlled = true
            playerCountry.value = it
            selectedCountry.value = it
        }
        
        addNewsEvent(NewsEvent(
            type = EventType.LEADER_ANNOUNCEMENT,
            country = "Global",
            headline = "Welcome to Global Dominion",
            description = "You are now in control. Make decisions wisely to lead your nation to prosperity.",
            severity = 0.5f
        ))
    }
    
    private fun createCountry(
        id: String,
        name: String,
        flag: String,
        govType: GovernmentType,
        color: Color,
        gdp: Float,
        military: Float
    ): Country {
        return Country(
            id = id,
            name = name,
            flag = flag,
            government = Government(
                type = govType,
                advisors = mutableListOf(),
                legislativeBodyEffectiveness = Random.nextFloat() * 0.5f + 0.5f,
                intelligenceAgencyEffectiveness = Random.nextFloat() * 0.5f + 0.5f,
                publicCompliance = Random.nextFloat() * 0.3f + 0.6f
            ),
            gdp = gdp,
            militaryPower = military,
            publicOpinion = PublicOpinion(
                governmentSupport = Random.nextFloat() * 30f + 50f,
                protestIntensity = Random.nextFloat() * 20f
            ),
            escalationRisk = Random.nextFloat() * 20f,
            technology = Random.nextFloat() * 30f + 40f,
            resources = Random.nextFloat() * 40f + 40f,
            color = color,
            aiState = AIState(
                beliefs = mutableMapOf(
                    BeliefType.PEACEFUL to Random.nextFloat(),
                    BeliefType.EXPANSIONIST to Random.nextFloat(),
                    BeliefType.PROTECTIONISM to Random.nextFloat()
                ),
                needs = mutableMapOf(
                    NeedType.SECURITY to Random.nextFloat(),
                    NeedType.ECONOMIC_GROWTH to Random.nextFloat()
                )
            )
        )
    }
    
    private fun initializeRelations() {
        // Set up initial diplomatic relations
        val alliances = mapOf(
            "usa" to listOf("uk", "germany", "france", "japan", "australia"),
            "china" to listOf("russia"),
            "russia" to listOf("china")
        )
        
        countries.forEach { country ->
            countries.forEach { other ->
                if (country.id != other.id) {
                    val relation = when {
                        alliances[country.id]?.contains(other.id) == true -> 70f + Random.nextFloat() * 20f
                        country.government.type == other.government.type -> 50f + Random.nextFloat() * 20f
                        else -> 30f + Random.nextFloat() * 30f
                    }
                    country.relations[other.id] = relation
                }
            }
        }
    }
    
    fun addNewsEvent(event: NewsEvent) {
        newsEvents.add(0, event)
        if (newsEvents.size > 50) {
            newsEvents.removeAt(newsEvents.size - 1)
        }
    }
    
    fun executeDecision(decision: Decision, actor: Country) {
        decision.effects.forEach { effect ->
            val target = if (effect.targetSelf) actor else {
                decision.targetCountry?.let { id -> countries.find { it.id == id } }
            }
            
            target?.let { country ->
                when (effect.stat) {
                    StatType.GDP -> country.gdp = (country.gdp + effect.change).coerceIn(0f, 100f)
                    StatType.MILITARY -> country.militaryPower = (country.militaryPower + effect.change).coerceIn(0f, 100f)
                    StatType.SUPPORT -> country.publicOpinion.governmentSupport = (country.publicOpinion.governmentSupport + effect.change).coerceIn(0f, 100f)
                    StatType.PROTEST -> country.publicOpinion.protestIntensity = (country.publicOpinion.protestIntensity + effect.change).coerceIn(0f, 100f)
                    StatType.TECHNOLOGY -> country.technology = (country.technology + effect.change).coerceIn(0f, 100f)
                    StatType.RESOURCES -> country.resources = (country.resources + effect.change).coerceIn(0f, 100f)
                    StatType.ESCALATION -> country.escalationRisk = (country.escalationRisk + effect.change).coerceIn(0f, 100f)
                    StatType.RELATIONS -> decision.targetCountry?.let { targetId ->
                        country.relations[targetId] = (country.relations[targetId] ?: 50f) + effect.change
                    }
                }
            }
        }
        
        addNewsEvent(NewsEvent(
            type = when (decision.category) {
                DecisionCategory.MILITARY -> EventType.MILITARY_ACTION
                DecisionCategory.ECONOMY -> EventType.ECONOMIC_CHANGE
                else -> EventType.LEADER_ANNOUNCEMENT
            },
            country = actor.name,
            headline = decision.title,
            description = "${actor.name} has decided to ${decision.description.lowercase()}",
            severity = decision.effects.sumOf { kotlin.math.abs(it.change.toDouble()).toFloat().toDouble() }.toFloat() / 100f
        ))
    }
    
    fun advanceTurn() {
        turn.value++
        if (turn.value % 4 == 0) {
            year.value++
        }
        
        // AI countries make decisions
        countries.filter { !it.isPlayerControlled }.forEach { country ->
            simulateAITurn(country)
        }
        
        // Random events
        if (Random.nextFloat() < 0.3f) {
            generateRandomEvent()
        }
        
        // Update all countries
        countries.forEach { country ->
            // Natural economic growth/decline
            country.gdp += (Random.nextFloat() - 0.5f) * 2f
            country.gdp = country.gdp.coerceIn(0f, 100f)
            
            // Public opinion shifts
            country.publicOpinion.governmentSupport += (Random.nextFloat() - 0.5f) * 3f
            country.publicOpinion.governmentSupport = country.publicOpinion.governmentSupport.coerceIn(0f, 100f)
        }
    }
    
    private fun simulateAITurn(country: Country) {
        val decisions = getAvailableDecisions(country)
        if (decisions.isNotEmpty() && Random.nextFloat() < 0.4f) {
            val decision = decisions.random()
            executeDecision(decision, country)
        }
    }
    
    private fun generateRandomEvent() {
        val eventTypes = listOf(
            NewsEvent(EventType.DISASTER, countries.random().name, "Natural Disaster Strikes", "A major earthquake has caused significant damage", 0.7f),
            NewsEvent(EventType.ECONOMIC_CHANGE, countries.random().name, "Market Volatility", "Stock markets experience unexpected fluctuations", 0.4f),
            NewsEvent(EventType.PUBLIC_PROTEST, countries.random().name, "Civil Unrest", "Citizens take to the streets demanding change", 0.5f)
        )
        addNewsEvent(eventTypes.random())
    }
    
    fun getAvailableDecisions(country: Country): List<Decision> {
        val decisions = mutableListOf<Decision>()
        
        // Economic decisions
        decisions.add(Decision(
            id = "invest_economy",
            title = "Invest in Economy",
            description = "Boost economic development through infrastructure spending",
            category = DecisionCategory.ECONOMY,
            effects = listOf(
                DecisionEffect(StatType.GDP, 5f),
                DecisionEffect(StatType.SUPPORT, 3f)
            )
        ))
        
        decisions.add(Decision(
            id = "austerity",
            title = "Implement Austerity",
            description = "Cut government spending to reduce debt",
            category = DecisionCategory.ECONOMY,
            effects = listOf(
                DecisionEffect(StatType.GDP, -3f),
                DecisionEffect(StatType.SUPPORT, -5f),
                DecisionEffect(StatType.RESOURCES, 8f)
            )
        ))
        
        // Military decisions
        decisions.add(Decision(
            id = "military_buildup",
            title = "Military Buildup",
            description = "Increase military spending and recruitment",
            category = DecisionCategory.MILITARY,
            effects = listOf(
                DecisionEffect(StatType.MILITARY, 8f),
                DecisionEffect(StatType.GDP, -3f),
                DecisionEffect(StatType.ESCALATION, 5f)
            )
        ))
        
        decisions.add(Decision(
            id = "peacekeeping",
            title = "Peacekeeping Mission",
            description = "Deploy forces for international peacekeeping",
            category = DecisionCategory.MILITARY,
            effects = listOf(
                DecisionEffect(StatType.MILITARY, -2f),
                DecisionEffect(StatType.SUPPORT, 5f),
                DecisionEffect(StatType.ESCALATION, -3f)
            )
        ))
        
        // Technology decisions
        decisions.add(Decision(
            id = "research_funding",
            title = "Fund Research",
            description = "Increase funding for scientific research",
            category = DecisionCategory.TECHNOLOGY,
            effects = listOf(
                DecisionEffect(StatType.TECHNOLOGY, 7f),
                DecisionEffect(StatType.GDP, -2f)
            )
        ))
        
        // Domestic decisions
        decisions.add(Decision(
            id = "social_programs",
            title = "Expand Social Programs",
            description = "Increase welfare and social safety nets",
            category = DecisionCategory.DOMESTIC,
            effects = listOf(
                DecisionEffect(StatType.SUPPORT, 8f),
                DecisionEffect(StatType.PROTEST, -5f),
                DecisionEffect(StatType.GDP, -4f)
            )
        ))
        
        decisions.add(Decision(
            id = "crack_down",
            title = "Crack Down on Protests",
            description = "Use force to suppress civil unrest",
            category = DecisionCategory.DOMESTIC,
            effects = listOf(
                DecisionEffect(StatType.PROTEST, -10f),
                DecisionEffect(StatType.SUPPORT, -8f),
                DecisionEffect(StatType.ESCALATION, 3f)
            )
        ))
        
        // Foreign policy decisions for each other country
        countries.filter { it.id != country.id }.forEach { other ->
            decisions.add(Decision(
                id = "diplomacy_${other.id}",
                title = "Diplomatic Outreach to ${other.name}",
                description = "Improve relations through diplomatic channels",
                category = DecisionCategory.FOREIGN_POLICY,
                effects = listOf(
                    DecisionEffect(StatType.RELATIONS, 10f, targetSelf = true)
                ),
                targetCountry = other.id
            ))
            
            if ((country.relations[other.id] ?: 50f) < 30f) {
                decisions.add(Decision(
                    id = "sanctions_${other.id}",
                    title = "Impose Sanctions on ${other.name}",
                    description = "Economic sanctions to pressure ${other.name}",
                    category = DecisionCategory.FOREIGN_POLICY,
                    effects = listOf(
                        DecisionEffect(StatType.RELATIONS, -15f, targetSelf = true),
                        DecisionEffect(StatType.GDP, -5f, targetSelf = false),
                        DecisionEffect(StatType.ESCALATION, 8f)
                    ),
                    targetCountry = other.id
                ))
            }
        }
        
        return decisions
    }
}

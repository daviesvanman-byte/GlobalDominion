package com.globaldominion.ai

import com.globaldominion.data.Country

class CountryAI(private val personality: Personality) {

    fun decide(country: Country): String {
        return if (country.publicOpinionData.unrest > 60) {
            country.publicOpinionData.approval -= 5
            "Imposed emergency measures"
        } else if (personality.aggressive && country.military.readiness > 70) {
            country.military.readiness -= 10
            "Military posturing increased"
        } else {
            "Maintained current policy"
        }
    }
}

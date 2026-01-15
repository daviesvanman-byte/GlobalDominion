package com.globaldominion.data

enum class EventType { LEADER_ANNOUNCEMENT, MILITARY_ACTION, DISASTER, ECONOMIC_CHANGE, PUBLIC_PROTEST }

data class NewsEvent(
    val type: EventType,
    val country: String,
    val headline: String,
    val description: String,
    val severity: Float
)

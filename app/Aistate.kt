package com.globaldominion.data

data class AIState(
    var beliefs: MutableMap<BeliefType, Float>,
    var needs: MutableMap<NeedType, Float>
)

enum class BeliefType { PEACEFUL, EXPANSIONIST, PROTECTIONISM, GLOBAL_LEADERSHIP, ENVIRONMENTAL_PRIORITY }

enum class NeedType { SECURITY, ECONOMIC_GROWTH, PUBLIC_SUPPORT, MILITARY_STRENGTH, RESOURCE_ACQUISITION, TECHNOLOGY_ADVANCEMENT }

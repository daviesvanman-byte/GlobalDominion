package com.globaldominion.data

enum class Role { PRESIDENT, PRIME_MINISTER, HEAD_OF_DEFENSE, HEAD_OF_SECURITY, ECONOMIC_ADVISOR, FOREIGN_MINISTER }

data class ConversationParticipant(
    val country: Country,
    val role: Role,
    val aiState: AIState?,
    val personality: PersonalityTraits? = null,
    val voiceProfile: VoiceProfile? = null
)

data class ConversationMessage(
    val sender: ConversationParticipant,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class Conversation(
    val participants: List<ConversationParticipant>,
    val messages: MutableList<ConversationMessage> = mutableListOf()
)

package com.lelestargazer.qurban_ticketing_system.member_shared.domain.model

enum class QurbanStatus {
    Recipient, Participant
}

fun QurbanStatus.isParticipant(): Boolean = this == QurbanStatus.Participant
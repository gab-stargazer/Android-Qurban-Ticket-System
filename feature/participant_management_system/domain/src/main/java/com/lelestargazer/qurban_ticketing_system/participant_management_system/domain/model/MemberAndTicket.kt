package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model

data class MemberAndTicket(
    val participantRecipient: ParticipantRecipient,
    val ticket: Ticket?,
)
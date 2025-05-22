package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model

data class ParticipantAndTicket(
    val participant: Participant,
    val ticket: Ticket?,
)
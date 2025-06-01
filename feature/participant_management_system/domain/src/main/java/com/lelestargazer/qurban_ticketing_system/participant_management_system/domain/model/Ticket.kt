package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model

import java.util.UUID

data class Ticket(
    val id: Int = 0,
    val participantID: UUID,
    val participantName: String,
    val ticketYear: Int,
    val hashCode: String,
    val claimStatus: TicketRedeemStatus,
)

package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity

import androidx.room.Embedded
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.ParticipantAndTicket

data class ParticipantAndTicketEntity(

    @Embedded
    val participant: ParticipantEntity,

    @Embedded
    val ticket: TicketEntity?,
)

fun ParticipantAndTicketEntity.toDomain(): ParticipantAndTicket =
    ParticipantAndTicket(
        participant = participant.toDomain(),
        ticket = ticket?.toDomain()
    )
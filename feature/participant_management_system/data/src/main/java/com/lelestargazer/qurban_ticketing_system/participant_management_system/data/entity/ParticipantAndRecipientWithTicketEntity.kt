package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.MemberAndTicket

data class ParticipantAndRecipientWithTicketEntity(

    @Embedded
    val participant: ParticipantRecipientEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "participant_id",
        entity = TicketEntity::class
    )
    val ticket: TicketEntity?,
)

fun ParticipantAndRecipientWithTicketEntity.toDomain(): MemberAndTicket =
    MemberAndTicket(
        participantRecipient = participant.toDomain(),
        ticket = ticket?.toDomain()
    )
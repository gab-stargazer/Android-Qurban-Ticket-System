package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.TicketRedeemStatus
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Ticket
import java.util.UUID

@Entity(
    tableName = "ticket_table"
)
data class TicketEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ticket_id")
    val id: Int = 0,

    @ColumnInfo("participant_id")
    val participantID: UUID,

    @ColumnInfo("participant_name")
    val participantName: String,

    @ColumnInfo("ticket_year")
    val ticketYear: Int,

    @ColumnInfo("hash_code")
    val hashCode: String,

    @ColumnInfo("claim_status")
    val claimStatus: TicketRedeemStatus,
)

fun TicketEntity.toDomain(): Ticket =
    Ticket(
        id = id,
        participantID = participantID,
        participantName = participantName,
        ticketYear = ticketYear,
        hashCode = hashCode,
        claimStatus = claimStatus
    )
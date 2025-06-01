package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.ParticipantRecipient
import java.util.UUID

@Entity(
    tableName = "participant_recipient_table"
)
data class ParticipantRecipientEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone")
    val phone: String,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "household_size")
    val householdSize: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "is_participant")
    val isParticipant: Boolean,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
)

fun ParticipantRecipientEntity.toDomain(): ParticipantRecipient =
    ParticipantRecipient(
        id = id,
        name = name,
        phoneNumber = phone,
        address = address,
        householdSize = householdSize,
        description = description,
        isParticipant = isParticipant,
        isActive = isActive
    )

fun ParticipantRecipient.toEntity(): ParticipantRecipientEntity =
    ParticipantRecipientEntity(
        id = id,
        name = name,
        phone = phoneNumber,
        address = address,
        householdSize = householdSize,
        description = description,
        isParticipant = isParticipant,
        isActive = isActive
    )
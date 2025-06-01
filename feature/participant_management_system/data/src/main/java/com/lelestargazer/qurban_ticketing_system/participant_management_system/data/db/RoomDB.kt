package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.converter.UUIDConverter
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.ParticipantDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.TicketDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantRecipientEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.TicketEntity

@Database(
    entities = [ParticipantRecipientEntity::class, TicketEntity::class],
    version = 1,
)
@TypeConverters(UUIDConverter::class)
abstract class RoomDB : RoomDatabase() {
    abstract fun participantDao(): ParticipantDao
    abstract fun ticketDao(): TicketDao
}
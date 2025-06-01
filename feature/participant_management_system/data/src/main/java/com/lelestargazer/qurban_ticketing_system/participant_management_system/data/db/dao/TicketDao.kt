package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.TicketEntity

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTicket(ticketEntity: TicketEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTickets(ticketEntity: List<TicketEntity>)

    @Update
    suspend fun updateTicket(ticketEntity: TicketEntity)
}
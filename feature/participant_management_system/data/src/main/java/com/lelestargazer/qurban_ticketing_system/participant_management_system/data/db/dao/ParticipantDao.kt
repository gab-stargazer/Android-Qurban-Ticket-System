package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantAndRecipientWithTicketEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantRecipientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertParticipant(participant: ParticipantRecipientEntity)

    @Update
    suspend fun updateParticipant(participant: ParticipantRecipientEntity)

    @Query("SELECT * FROM participant_recipient_table")
    fun readAllParticipant(): Flow<List<ParticipantRecipientEntity>>

    @Query("SELECT * FROM participant_recipient_table WHERE is_active = 1")
    fun getActiveParticipant(): Flow<List<ParticipantRecipientEntity>>

    @Query("SELECT * FROM participant_recipient_table WHERE is_active = 0")
    fun getInactiveParticipant(): Flow<List<ParticipantRecipientEntity>>

    @Transaction
    @Query(
       """
            SELECT * 
            FROM participant_recipient_table participant 
            LEFT JOIN ticket_table ticket 
            ON participant.id = ticket.participant_id 
            AND ticket.ticket_year = :currentYear
            WHERE participant.is_active = 1
       """
    )
    fun getParticipantAndRecipientWithCurrentYearTicket(currentYear: Int): Flow<List<ParticipantAndRecipientWithTicketEntity>>

    @Transaction
    @Query(
        """
           SELECT * 
            FROM participant_recipient_table participant 
            LEFT JOIN ticket_table ticket 
            ON participant.id = ticket.participant_id 
            AND ticket.ticket_year = :currentYear
            WHERE participant.is_active = 1 AND participant.is_participant = 0
       """
    )
    fun getRecipientWithCurrentYearTicket(currentYear: Int): Flow<List<ParticipantAndRecipientWithTicketEntity>>
}
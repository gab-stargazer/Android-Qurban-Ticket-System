package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.TicketEntity

@Dao
interface TicketDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTicket(ticketEntity: TicketEntity)

//    @Query(
//        """
//        SELECT p.*,
//               t.id as ticket_id,
//               t.participant_name as ticket_participant_name,
//               t.participant_id as ticket_participant_id,
//               t.ticket_year as ticket_ticket_year,
//               t.hash_code as ticket_hash_code,
//               t.claim_status as ticket_claim_status
//        FROM participant_table p
//        LEFT JOIN ticket_table t ON p.id = t.participant_id AND t.ticket_year = :currentYear
//    """
//    )
//    fun getParticipantsWithCurrentYearTicket(currentYear: Int): Flow<List<ParticipantAndTicketEntity>>
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertParticipant(participant: ParticipantEntity)

    @Update
    suspend fun updateParticipant(participant: ParticipantEntity)

    @Query("SELECT * FROM participant_table")
    fun readAllParticipant(): Flow<List<ParticipantEntity>>

    @Query("SELECT * FROM participant_table WHERE is_active = 1")
    fun getActiveParticipant(): Flow<List<ParticipantEntity>>

    @Query("SELECT * FROM participant_table WHERE is_active = 0")
    fun getInactiveParticipant(): Flow<List<ParticipantEntity>>
}
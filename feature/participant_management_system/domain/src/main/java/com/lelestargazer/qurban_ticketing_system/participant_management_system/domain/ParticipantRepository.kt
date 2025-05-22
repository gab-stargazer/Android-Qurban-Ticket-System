package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain

import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.ParticipantAndTicket
import kotlinx.coroutines.flow.Flow

interface ParticipantRepository {

    suspend fun insertParticipant(
        name: String,
        phoneNumber: String,
        address: String,
        householdSize: Int,
        description: String,
    )

    suspend fun updateParticipant(
        participant: Participant,
    )

    fun getActiveParticipants(): Flow<List<Participant>>

    fun getInactiveParticipants(): Flow<List<Participant>>

    fun getParticipantWithTicket(): Flow<List<ParticipantAndTicket>>
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.data

import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.dao.ParticipantDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.ParticipantRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParticipantRepositoryImpl(
    private val participantDao: ParticipantDao,
) : ParticipantRepository {

    override suspend fun insertParticipant(
        name: String,
        phoneNumber: String,
        address: String,
        householdSize: Int,
        description: String,
    ) {
        participantDao.insertParticipant(
            ParticipantEntity(
                name = name,
                phone = phoneNumber,
                address = address,
                householdSize = householdSize,
                description = description,
                isActive = true
            )
        )
    }

    override suspend fun updateParticipant(participant: Participant) {
        participantDao.updateParticipant(participant.toEntity())
    }

    override fun getActiveParticipants(): Flow<List<Participant>> {
        return participantDao.getActiveParticipant().map { it ->
            it.map(ParticipantEntity::toDomain)
        }
    }

    override fun getInactiveParticipants(): Flow<List<Participant>> {
        return participantDao.getInactiveParticipant().map { it ->
            it.map(ParticipantEntity::toDomain)
        }
    }
}
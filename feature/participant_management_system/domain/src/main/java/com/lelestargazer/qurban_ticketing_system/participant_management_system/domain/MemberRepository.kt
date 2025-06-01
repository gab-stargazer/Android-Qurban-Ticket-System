package com.lelestargazer.qurban_ticketing_system.participant_management_system.domain

import android.graphics.Bitmap
import arrow.core.Option
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.ParticipantRecipient
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.MemberAndTicket
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Ticket
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    suspend fun insertParticipant(
        name: String,
        phoneNumber: String,
        address: String,
        householdSize: Int,
        description: String,
        isParticipant: Boolean,
    )

    suspend fun updateParticipant(
        participantRecipient: ParticipantRecipient,
    )

    fun getActiveParticipants(): Flow<List<ParticipantRecipient>>

    fun getInactiveParticipants(): Flow<List<ParticipantRecipient>>

    fun getMemberAndTicket(): Flow<List<MemberAndTicket>>

    fun getRecipientWithTicket(): Flow<List<MemberAndTicket>>

    suspend fun createQrCoupons()

    fun getQrImage(qrHash: String): Bitmap

    suspend fun claimTicket(ticket: Ticket): Option<String>
}
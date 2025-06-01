package com.lelestargazer.qurban_ticketing_system.participant_management_system.data

import android.content.Context
import android.graphics.Bitmap
import arrow.core.Option
import arrow.core.raise.option
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.ParticipantDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.TicketDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantAndRecipientWithTicketEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.ParticipantRecipientEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.TicketEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.qr.QRGenerator
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.MemberRepository
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.MemberAndTicket
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.ParticipantRecipient
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Ticket
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.TicketRedeemStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.kotlincrypto.hash.sha3.SHA3_256
import java.util.Calendar

class MemberRepositoryImpl(
    private val participantDao: ParticipantDao,
    private val ticketDao: TicketDao,
    private val qrGenerator: QRGenerator,
    private val context: Context,
) : MemberRepository {

    override suspend fun insertParticipant(
        name: String,
        phoneNumber: String,
        address: String,
        householdSize: Int,
        description: String,
        isParticipant: Boolean,
    ) {
        participantDao.insertParticipant(
            ParticipantRecipientEntity(
                name = name,
                phone = phoneNumber,
                address = address,
                householdSize = householdSize,
                description = description,
                isParticipant = isParticipant,
                isActive = true
            )
        )
    }

    override suspend fun updateParticipant(participantRecipient: ParticipantRecipient) {
        participantDao.updateParticipant(participantRecipient.toEntity())
    }

    override fun getActiveParticipants(): Flow<List<ParticipantRecipient>> {
        return participantDao.getActiveParticipant().map { it ->
            it.map(ParticipantRecipientEntity::toDomain)
        }
    }

    override fun getInactiveParticipants(): Flow<List<ParticipantRecipient>> {
        return participantDao.getInactiveParticipant().map { it ->
            it.map(ParticipantRecipientEntity::toDomain)
        }
    }

    override fun getMemberAndTicket(): Flow<List<MemberAndTicket>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return participantDao.getParticipantAndRecipientWithCurrentYearTicket(year)
            .map {
                it.map(ParticipantAndRecipientWithTicketEntity::toDomain)
            }
    }

    override fun getRecipientWithTicket(): Flow<List<MemberAndTicket>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return participantDao.getRecipientWithCurrentYearTicket(year)
            .map { it.map(ParticipantAndRecipientWithTicketEntity::toDomain) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun createTickets() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val participantWithTicket =
            participantDao.getParticipantAndRecipientWithCurrentYearTicket(year)
        val participantWithoutTicket =
            participantWithTicket.first().filter { item -> item.ticket == null }

        if (participantWithoutTicket.isEmpty()) return

        val sha256 = SHA3_256()
        val newTickets = mutableListOf<TicketEntity>()
        participantWithoutTicket.forEach {
            val participant = it.participant
            val textToHash = "${participant.id} - ${year}}".encodeToByteArray()

            newTickets.add(
                TicketEntity(
                    participantID = participant.id,
                    participantName = participant.name,
                    ticketYear = year,
                    hashCode = sha256.digest(textToHash).toHexString(),
                    claimStatus = TicketRedeemStatus.NOT_CLAIMED
                )
            )
        }

        ticketDao.insertTickets(newTickets)
    }

    override suspend fun createQrCoupons() {
        createTickets()

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val participantWIthTicket: List<ParticipantAndRecipientWithTicketEntity> =
            participantDao
                .getParticipantAndRecipientWithCurrentYearTicket(currentYear)
                .first()
                .filter { it.ticket != null }

        val qrData = participantWIthTicket.map {

            it.ticket as TicketEntity

            val couponStatus =
                if (it.participant.isParticipant) {
                    "Peserta"
                } else {
                    "Penerima"
                }

            QRGenerator.QRGeneratorData(
                qrCode = it.ticket.hashCode,
                couponStatus = couponStatus,
                couponName = it.ticket.participantName + " (${it.participant.phone.takeLast(4)})"
            )
        }

        qrGenerator.saveCoupons(qrData)
    }

    override fun getQrImage(qrHash: String): Bitmap {
        return qrGenerator.getQrImage(qrHash)
    }

    override suspend fun claimTicket(ticket: Ticket): Option<String> = option {
        ticketDao.updateTicket(ticket.toEntity())
        context.getString(R.string.msg_ticket_successfully_claimed)
    }
}
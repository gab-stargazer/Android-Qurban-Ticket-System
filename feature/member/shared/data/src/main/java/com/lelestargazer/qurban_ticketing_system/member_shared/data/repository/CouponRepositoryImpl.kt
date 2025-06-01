package com.lelestargazer.qurban_ticketing_system.member_shared.data.repository

import android.content.Context
import android.graphics.Bitmap
import arrow.core.Option
import arrow.core.raise.option
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_coupon_successfully_claimed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_participant
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_recipient
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.QRGenerator
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.CouponEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberAndTicketEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.CouponRepository
import kotlinx.coroutines.flow.first
import org.kotlincrypto.hash.sha3.SHA3_256
import java.util.Calendar
import java.util.UUID

class CouponRepositoryImpl(
    private val memberDao: MemberDao,
    private val couponDao: CouponDao,
    private val qrGenerator: QRGenerator,
    private val context: Context,
) : CouponRepository {

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun createTickets() {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val participantWithTicket =
            memberDao.getMembersAndCoupons(year)
        val participantWithoutTicket =
            participantWithTicket.first().filter { item -> item.coupon == null }

        if (participantWithoutTicket.isEmpty()) return

        val sha256 = SHA3_256()
        val newCoupons = mutableListOf<CouponEntity>()
        participantWithoutTicket.forEach {
            val participant = it.member
            val textToHash = "${participant.id} - ${year}}".encodeToByteArray()

            newCoupons.add(
                CouponEntity(
                    couponID = UUID.randomUUID(),
                    participantID = participant.id,
                    memberName = participant.name,
                    year = year,
                    hashCode = sha256.digest(textToHash).toHexString(),
                    claimStatus = CouponRedeemStatus.NOT_CLAIMED
                )
            )
        }

        couponDao.insertCoupons(newCoupons)
    }

    override suspend fun createCoupons() {
        createTickets()

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val participantWIthTicket: List<MemberAndTicketEntity> =
            memberDao
                .getMembersAndCoupons(currentYear)
                .first()
                .filter { it.coupon != null }

        val qrData = participantWIthTicket.map {

            it.coupon as CouponEntity

            val couponStatus =
                if (it.member.isParticipant) {
                    context.getString(msg_member_participant)
                } else {
                    context.getString(msg_member_recipient)
                }

            QRGenerator.QRGeneratorData(
                qrCode = it.coupon.hashCode,
                couponStatus = couponStatus,
                couponName = it.coupon.memberName
            )
        }

        qrGenerator.saveCoupons(qrData)
    }

    override fun getQrImage(hash: String): Bitmap {
        return qrGenerator.getQrImage(hash)
    }

    override suspend fun claimCoupon(coupon: Coupon): Option<String> = option {
        couponDao.updateCoupon(coupon.toEntity())
        context.getString(msg_coupon_successfully_claimed)
    }
}
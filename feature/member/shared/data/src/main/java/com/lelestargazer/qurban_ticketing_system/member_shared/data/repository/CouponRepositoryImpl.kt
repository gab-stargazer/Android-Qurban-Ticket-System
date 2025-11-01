package com.lelestargazer.qurban_ticketing_system.member_shared.data.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import arrow.core.Either
import arrow.core.Option
import arrow.core.raise.either
import arrow.core.raise.ensureNotNull
import arrow.core.raise.option
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_coupon_not_found
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_coupon_successfully_claimed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_failed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_success
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_participant
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_recipient
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.QRGenerator
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.CouponEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberAndCouponEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.CouponRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import org.kotlincrypto.hash.sha3.SHA3_256
import java.util.Calendar
import java.util.UUID

@Single(
    binds = [CouponRepository::class],
    createdAtStart = true
)
class CouponRepositoryImpl(
    private val couponDao: CouponDao,
    private val qrGenerator: QRGenerator,
    private val context: Context,
) : CouponRepository {

    override fun getMembersAndCoupons(query: String): Flow<PagingData<MemberAndCoupon>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 50
            ),
            pagingSourceFactory = {
                couponDao.getMembersAndCoupons(
                    year,
                    query
                )
            }
        ).flow.map {
            it.map(MemberAndCouponEntity::toDomain)
        }
    }

    override fun getMembersAndCouponsAvailable(query: String): Flow<PagingData<MemberAndCoupon>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return Pager(
            config = PagingConfig(
                pageSize = 25,
                prefetchDistance = 10,
                initialLoadSize = 50
            ),
            pagingSourceFactory = {
                couponDao.getMembersAndCouponsAvailable(
                    year, query
                )
            }
        ).flow.map {
            it.map(MemberAndCouponEntity::toDomain)
        }
    }

    override fun getMembersAndCouponsUnclaimedCount(): Flow<Int> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return couponDao.getCountMembersAndCouponsUnclaimed(year)
    }

    override suspend fun getMembersAndCouponByHash(hash: String): Either<String, MemberAndCoupon> =
        either {
            val memberAndCoupon = couponDao.getMembersAndCouponsByHash(hash)
            ensureNotNull(memberAndCoupon) {
                context.getString(msg_coupon_not_found)
            }

            memberAndCoupon.toDomain()
        }

    @OptIn(ExperimentalStdlibApi::class)
    private suspend fun createTickets() = withContext(Dispatchers.IO) {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val participantWithTicket =
            couponDao.getMembersAndCoupons(year)
        val participantWithoutTicket =
            participantWithTicket.filter { item -> item.coupon == null }
        if (participantWithoutTicket.isEmpty()) return@withContext

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

    override suspend fun createCoupons(): Either<String, String> = Either.catch {
        createTickets()

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val participantWIthTicket: List<MemberAndCouponEntity> =
            couponDao
                .getMembersAndCoupons(currentYear)
                .filter { it.coupon != null }

        val qrData = participantWIthTicket.map {

            it.coupon as CouponEntity

            val couponStatus =
                if (it.member.status == QurbanStatus.Participant) {
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

        qrGenerator.saveCoupons(qrDataList = qrData)
        context.getString(msg_excel_create_members_success)
    }.mapLeft {
        Log.e(TAG, "createCoupons: ${it.stackTraceToString()}")
        context.getString(msg_excel_create_members_failed)
    }

    override fun getQrImage(hash: String): Bitmap {
        return qrGenerator.getQrImage(hash)
    }

    override suspend fun claimCoupon(coupon: Coupon): Option<String> = option {
        couponDao.updateCoupon(coupon.toEntity())
        context.getString(msg_coupon_successfully_claimed)
    }
}
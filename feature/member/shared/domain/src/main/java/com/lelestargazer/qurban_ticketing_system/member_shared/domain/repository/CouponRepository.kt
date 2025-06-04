package com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository

import android.graphics.Bitmap
import androidx.paging.PagingData
import arrow.core.Either
import arrow.core.Option
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import kotlinx.coroutines.flow.Flow

interface CouponRepository {

    fun getMembersAndCoupons(query: String): Flow<PagingData<MemberAndCoupon>>

    fun getMembersAndCouponsAvailable(query: String): Flow<PagingData<MemberAndCoupon>>

    fun getMembersAndCouponsUnclaimedCount(): Flow<Int>

    suspend fun getMembersAndCouponByHash(hash: String): Either<String, MemberAndCoupon>

    suspend fun createCoupons(): Either<String, String>

    suspend fun claimCoupon(coupon: Coupon): Option<String>

    fun getQrImage(hash: String): Bitmap
}
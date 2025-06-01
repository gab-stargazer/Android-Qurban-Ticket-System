package com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository

import android.graphics.Bitmap
import arrow.core.Option
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon

interface CouponRepository {

    suspend fun createCoupons()

    suspend fun claimCoupon(coupon: Coupon): Option<String>

    fun getQrImage(hash: String): Bitmap
}
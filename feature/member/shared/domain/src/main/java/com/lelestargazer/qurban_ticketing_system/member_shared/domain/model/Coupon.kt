package com.lelestargazer.qurban_ticketing_system.member_shared.domain.model

import java.util.UUID

data class Coupon(
    val couponID: UUID,
    val participantID: UUID,
    val participantName: String,
    val year: Int,
    val hashCode: String,
    val claimStatus: CouponRedeemStatus,
)

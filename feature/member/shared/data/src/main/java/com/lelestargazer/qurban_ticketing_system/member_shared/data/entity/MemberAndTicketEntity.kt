package com.lelestargazer.qurban_ticketing_system.member_shared.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon

data class MemberAndTicketEntity(

    @Embedded
    val member: MemberEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "member_id",
        entity = CouponEntity::class
    )
    val coupon: CouponEntity?,
)

fun MemberAndTicketEntity.toDomain(): MemberAndCoupon =
    MemberAndCoupon(
        member = member.toDomain(),
        coupon = coupon?.toDomain()
    )
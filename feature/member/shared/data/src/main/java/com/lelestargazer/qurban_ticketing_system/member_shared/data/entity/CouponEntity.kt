package com.lelestargazer.qurban_ticketing_system.member_shared.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import java.util.UUID

@Entity(
    tableName = "coupon_table",
    foreignKeys = [
        ForeignKey(
            entity = MemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["member_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class CouponEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val couponID: UUID,

    @ColumnInfo("member_id")
    val participantID: Long,

    @ColumnInfo("member_name")
    val memberName: String,

    @ColumnInfo("coupon_year")
    val year: Int,

    @ColumnInfo("hash_code")
    val hashCode: String,

    @ColumnInfo("claim_status")
    val claimStatus: CouponRedeemStatus,
)

fun CouponEntity.toDomain(): Coupon =
    Coupon(
        couponID = couponID,
        participantID = participantID,
        participantName = memberName,
        year = year,
        hashCode = hashCode,
        claimStatus = claimStatus
    )

fun Coupon.toEntity(): CouponEntity =
    CouponEntity(
        couponID = couponID,
        participantID = participantID,
        memberName = participantName,
        year = year,
        hashCode = hashCode,
        claimStatus = claimStatus
    )
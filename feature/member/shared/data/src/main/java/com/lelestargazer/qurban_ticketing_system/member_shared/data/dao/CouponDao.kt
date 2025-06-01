package com.lelestargazer.qurban_ticketing_system.member_shared.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.CouponEntity

@Dao
interface CouponDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCoupon(couponEntity: CouponEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCoupons(couponEntity: List<CouponEntity>)

    @Update
    suspend fun updateCoupon(couponEntity: CouponEntity)
}
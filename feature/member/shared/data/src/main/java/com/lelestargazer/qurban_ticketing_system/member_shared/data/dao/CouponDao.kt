package com.lelestargazer.qurban_ticketing_system.member_shared.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.CouponEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberAndCouponEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CouponDao {

    @Transaction
    @Query(
        """
            SELECT * 
            FROM member_table member
            LEFT JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.coupon_year = :currentYear
            WHERE member.is_active = 1
            ORDER BY name
       """
    )
    suspend fun getMembersAndCoupons(
        currentYear: Int,
    ): List<MemberAndCouponEntity>

    @Transaction
    @Query(
        """
            SELECT * 
            FROM member_table member
            JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.hash_code = :hash
            WHERE member.is_active = 1
            ORDER BY name
       """
    )
    suspend fun getMembersAndCouponsByHash(hash: String): MemberAndCouponEntity?

    @Transaction
    @Query(
        """
            SELECT * 
            FROM member_table member
            JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.coupon_year = :currentYear AND coupon.claim_status == "NOT_CLAIMED"
            WHERE member.is_active = 1 AND member.name LIKE '%' || :query || '%' 
            ORDER BY name
       """
    )
    fun getMembersAndCouponsAvailable(
        currentYear: Int,
        query: String,
    ): PagingSource<Int, MemberAndCouponEntity>

    @Transaction
    @Query(
        """
            SELECT * 
            FROM member_table member
            LEFT JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.coupon_year = :currentYear
            WHERE member.is_active = 1 AND member.name LIKE '%' || :query || '%' 
            ORDER BY name
       """
    )
    fun getMembersAndCoupons(
        currentYear: Int,
        query: String,
    ): PagingSource<Int, MemberAndCouponEntity>

    @Transaction
    @Query(
        """
            SELECT COUNT(*) 
            FROM member_table member
            JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.coupon_year = :currentYear AND coupon.claim_status == "NOT_CLAIMED"
            WHERE member.is_active = 1
            ORDER BY name
       """
    )
    fun getCountMembersAndCouponsUnclaimed(currentYear: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCoupon(couponEntity: CouponEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCoupons(couponEntity: List<CouponEntity>)

    @Update
    suspend fun updateCoupon(couponEntity: CouponEntity)
}
package com.lelestargazer.qurban_ticketing_system.member_shared.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberAndTicketEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemberDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMember(member: MemberEntity)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMembers(members: List<MemberEntity>)

    @Update
    suspend fun updateMember(member: MemberEntity)

    @Query("SELECT * FROM member_table")
    fun readAllParticipant(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM member_table WHERE is_active = 1")
    fun getActiveParticipant(): Flow<List<MemberEntity>>

    @Query("SELECT * FROM member_table WHERE is_active = 0")
    fun getInactiveParticipant(): Flow<List<MemberEntity>>

    @Transaction
    @Query(
       """
            SELECT * 
            FROM member_table member
            LEFT JOIN coupon_table coupon 
            ON member.id = coupon.member_id 
            AND coupon.coupon_year = :currentYear
            WHERE member.is_active = 1
       """
    )
    fun getMembersAndCoupons(currentYear: Int): Flow<List<MemberAndTicketEntity>>

    @Transaction
    @Query(
        """
           SELECT * 
            FROM member_table member 
            LEFT JOIN coupon_table coupon 
            ON member.id = coupon.member_id
            AND coupon.coupon_year = :currentYear
            WHERE member.is_active = 1 AND member.is_participant = 0
       """
    )
    fun getMemberWithCurrentYearCoupon(currentYear: Int): Flow<List<MemberAndTicketEntity>>
}
package com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository

import android.net.Uri
import arrow.core.Either
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    suspend fun insertMember(
        name: String,
        phoneNumber: String?,
        rt: Int,
        rw: Int,
        address: String,
        description: String,
        isParticipant: Boolean,
    )

    suspend fun updateMember(member: Member)

    suspend fun createMembersByExcel(uri: Uri): Either<String, String>

    fun getActiveMembers(): Flow<List<Member>>

    fun getInactiveMembers(): Flow<List<Member>>

    fun getMemberAndTicket(): Flow<List<MemberAndCoupon>>

    fun getMemberWithTicket(): Flow<List<MemberAndCoupon>>
}
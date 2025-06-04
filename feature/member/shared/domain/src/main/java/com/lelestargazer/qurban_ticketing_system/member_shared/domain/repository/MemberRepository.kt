package com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import arrow.core.Either
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
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

    fun getActiveMemberCount(): Flow<Int>

    suspend fun updateMember(member: Member)

    suspend fun createMembersByExcel(uri: Uri): Either<String, String>

    fun getMembers(query: String): Flow<PagingData<Member>>
}
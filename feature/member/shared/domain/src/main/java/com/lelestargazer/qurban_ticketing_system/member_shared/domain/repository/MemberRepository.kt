package com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository

import android.net.Uri
import androidx.paging.PagingData
import arrow.core.Either
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType
import kotlinx.coroutines.flow.Flow

interface MemberRepository {

    suspend fun insertMember(
        name: String,
        phoneNumber: String?,
        address: String?,
        qurbanStatus: QurbanStatus,
        qurbanType: QurbanType?
    )

    suspend fun importMember(member: List<Member>)

    fun insertMemberViaExcel(uri: Uri)

    fun getActiveMemberCount(): Flow<Int>

    suspend fun exportMembersToExcel(): Either<String, String>

    fun importMembersByExcel(uri: Uri): String

    suspend fun selectAllMembers(): List<Member>

    fun selectAllMembersAsPaging(query: String): Flow<PagingData<Member>>

    fun selectMembersByStatus(query: String, status: QurbanStatus): Flow<PagingData<Member>>

    suspend fun updateMember(member: Member)

    suspend fun deleteMember(member: Member)

    fun createCoupon(
        location: String,
        pickupDate: Long
    ): String
}
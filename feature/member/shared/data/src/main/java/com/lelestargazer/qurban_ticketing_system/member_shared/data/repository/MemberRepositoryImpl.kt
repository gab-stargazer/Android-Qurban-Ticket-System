package com.lelestargazer.qurban_ticketing_system.member_shared.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import arrow.core.Either
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_failed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_success
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.ExcelReader
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MemberRepositoryImpl(
    private val memberDao: MemberDao,
    private val excelReader: ExcelReader,
    private val context: Context,
) : MemberRepository {

    override suspend fun insertMember(
        name: String,
        phoneNumber: String?,
        rt: Int,
        rw: Int,
        address: String,
        description: String,
        isParticipant: Boolean,
    ) {
        memberDao.insertMember(
            MemberEntity(
                name = name,
                phone = phoneNumber,
                rt = rt,
                rw = rw,
                address = address,
                description = description,
                isParticipant = isParticipant,
                isActive = true
            )
        )
    }

    override suspend fun createMembersByExcel(uri: Uri): Either<String, String> = Either.catch {
        val members = excelReader.importMemberFromExcel(uri)
        memberDao.insertMembers(members)
        context.getString(msg_excel_create_members_success)
    }.mapLeft {
        context.getString(msg_excel_create_members_failed)
    }

    override fun getActiveMemberCount(): Flow<Int> {
        return memberDao.getActiveMemberCount()
    }

    override suspend fun updateMember(member: Member) {
        memberDao.updateMember(member.toEntity())
    }

    override fun getMembers(query: String): Flow<PagingData<Member>> {
        return Pager(
            config = PagingConfig(pageSize = 24, prefetchDistance = 12, initialLoadSize = 48),
            pagingSourceFactory = {
                memberDao.getAllMembers(query)
            }
        ).flow
            .map { it.map(MemberEntity::toDomain) }
            .flowOn(Dispatchers.IO)
    }
}
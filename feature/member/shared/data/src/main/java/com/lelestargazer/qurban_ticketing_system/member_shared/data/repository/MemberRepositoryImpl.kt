package com.lelestargazer.qurban_ticketing_system.member_shared.data.repository

import android.content.Context
import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import arrow.core.Either
import com.crispinlab.Snowflake
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_we_will_notify_when_import_finished
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.Excel
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.worker.ImportDataWorker
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single(
    binds = [MemberRepository::class],
    createdAtStart = true
)
class MemberRepositoryImpl(
    private val memberDao: MemberDao,
    private val excel: Excel,
    private val context: Context,
) : MemberRepository {

    override suspend fun insertMember(
        name: String,
        phoneNumber: String?,
        address: String?,
        qurbanStatus: QurbanStatus,
        qurbanType: QurbanType?
    ) {
        memberDao.insertMember(
            MemberEntity(
                id = Snowflake.create().nextId(),
                name = name,
                phone = phoneNumber,
                address = address,
                status = qurbanStatus,
                type = qurbanType,
            )
        )
    }

    override suspend fun importMember(member: List<Member>) {
        memberDao.insertMembers(member.map { it.toEntity() })
    }

    override fun insertMemberViaExcel(uri: Uri) {
        TODO("Not yet implemented")
    }

    override suspend fun exportMembersToExcel(): Either<String, String> = Either.catch {
        val members = memberDao.getActiveMembersAsList("")
        excel.exportMemberToExcel(members)

        "Sukses"
    }.mapLeft {
        println("Error: ${it.stackTraceToString()}")
        "Something Wrong"
    }

    override fun importMembersByExcel(uri: Uri): String {
        val inputData = Data.Builder()
            .putString(ImportDataWorker.INPUT_DATA_URL, uri.toString())
            .build()

        val workRequest = OneTimeWorkRequestBuilder<ImportDataWorker>()
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "import_member",
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = workRequest
        )

        return context.getString(msg_we_will_notify_when_import_finished)
    }

    override fun getActiveMemberCount(): Flow<Int> {
        return memberDao.getActiveMemberCount()
    }

    override fun selectAllMembers(query: String): Flow<PagingData<Member>> {
        return Pager(
            config = PagingConfig(pageSize = 24, prefetchDistance = 12, initialLoadSize = 48),
            pagingSourceFactory = {
                memberDao.selectAllMembers(query)
            }
        ).flow
            .map { it.map(MemberEntity::toDomain) }
            .flowOn(Dispatchers.IO)
    }

    override fun selectMembersByStatus(
        query: String,
        status: QurbanStatus
    ): Flow<PagingData<Member>> {
        return Pager(
            config = PagingConfig(pageSize = 24, prefetchDistance = 12, initialLoadSize = 48),
            pagingSourceFactory = {
                memberDao.selectMembersByStatus(query, status)
            }
        ).flow
            .map { it.map(MemberEntity::toDomain) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun updateMember(member: Member) {
        memberDao.updateMember(member.toEntity())
    }

    override suspend fun deleteMember(member: Member) {
        memberDao.deleteMember(member.toEntity())
    }
}
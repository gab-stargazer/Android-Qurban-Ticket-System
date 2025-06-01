package com.lelestargazer.qurban_ticketing_system.member_shared.data.repository

import android.content.Context
import android.net.Uri
import arrow.core.Either
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_failed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_excel_create_members_success
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.ExcelReader
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toDomain
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.toEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberAndTicketEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.QRGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

class MemberRepositoryImpl(
    private val memberDao: MemberDao,
    private val couponDao: CouponDao,
    private val qrGenerator: QRGenerator,
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

    override suspend fun updateMember(member: Member) {
        memberDao.updateMember(member.toEntity())
    }

    override fun getActiveMembers(): Flow<List<Member>> {
        return memberDao.getActiveParticipant().map { it ->
            it.map(MemberEntity::toDomain)
        }
    }

    override fun getInactiveMembers(): Flow<List<Member>> {
        return memberDao.getInactiveParticipant().map { it ->
            it.map(MemberEntity::toDomain)
        }
    }

    override fun getMemberAndTicket(): Flow<List<MemberAndCoupon>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return memberDao.getMembersAndCoupons(year)
            .map {
                it.map(MemberAndTicketEntity::toDomain)
            }
    }

    override fun getMemberWithTicket(): Flow<List<MemberAndCoupon>> {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        return memberDao.getMemberWithCurrentYearCoupon(year)
            .map { it.map(MemberAndTicketEntity::toDomain) }
    }
}
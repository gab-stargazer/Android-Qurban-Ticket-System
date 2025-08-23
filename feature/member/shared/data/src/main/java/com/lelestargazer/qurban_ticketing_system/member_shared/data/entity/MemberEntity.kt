package com.lelestargazer.qurban_ticketing_system.member_shared.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import java.util.UUID

@Entity(tableName = "member_table")
data class MemberEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phone: String?,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "rt")
    val rt: Int,

    @ColumnInfo(name = "rw")
    val rw: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "is_participant")
    val isParticipant: Boolean,

    @ColumnInfo(name = "is_cow")
    val isCow: Boolean?,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
)

fun MemberEntity.toDomain(): Member =
    Member(
        id = id,
        name = name,
        phoneNumber = phone,
        address = address,
        rt = rt,
        rw = rw,
        description = description,
        isParticipant = isParticipant,
        isCow = isCow,
        isActive = isActive
    )

fun Member.toEntity(): MemberEntity =
    MemberEntity(
        id = id,
        name = name,
        phone = phoneNumber,
        address = address,
        rt = rt,
        rw = rw,
        description = description,
        isParticipant = isParticipant,
        isCow = isCow,
        isActive = isActive
    )
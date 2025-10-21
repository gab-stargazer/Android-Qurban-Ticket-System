package com.lelestargazer.qurban_ticketing_system.member_shared.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType

@Entity(tableName = "member_table")
data class MemberEntity(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "phone_number")
    val phone: String?,

    @ColumnInfo(name = "address")
    val address: String?,

    @ColumnInfo(name = "status")
    val status: QurbanStatus,

    @ColumnInfo(name = "type")
    val type: QurbanType?
)

fun MemberEntity.toDomain(): Member =
    Member(
        id = id,
        name = name,
        phoneNumber = phone,
        address = address,
        status = status,
        type = type,
    )

fun Member.toEntity(): MemberEntity =
    MemberEntity(
        id = id,
        name = name,
        phone = phoneNumber,
        address = address,
        status = status,
        type = type,
    )
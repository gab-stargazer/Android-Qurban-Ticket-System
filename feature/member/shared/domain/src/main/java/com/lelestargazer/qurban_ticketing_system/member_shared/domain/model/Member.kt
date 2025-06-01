package com.lelestargazer.qurban_ticketing_system.member_shared.domain.model

import com.lelestargazer.qurban_ticketing_system.member_shared.domain.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Member(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val phoneNumber: String?,
    val address: String,
    val rt: Int,
    val rw: Int,
    val description: String,
    val isParticipant: Boolean = false,
    val isActive: Boolean = true,
)
package com.lelestargazer.qurban_ticketing_system.member_shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val id: Long,
    val name: String,
    val phoneNumber: String?,
    val address: String?,
    val status: QurbanStatus,
    val type: QurbanType?,
)
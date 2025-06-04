package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event

import androidx.paging.PagingData
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MemberManagementState(
    val query: String = "",
    val members: Flow<PagingData<Member>> = flowOf(),
    val activeParticipantRecipient: List<Member> = emptyList(),
    val inactiveParticipantRecipient: List<Member> = emptyList(),
    val openedParticipantType: OpenedParticipantType? = null,
    val openedParticipantIndex: Int? = null,
) {

    enum class OpenedParticipantType {
        ACTIVE, INACTIVE
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event

import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member

data class MemberManagementState(
    val query: String = "",
    val activeParticipantRecipient: List<Member> = emptyList(),
    val inactiveParticipantRecipient: List<Member> = emptyList(),
    val openedParticipantType: OpenedParticipantType? = null,
    val openedParticipantIndex: Int? = null,
) {

    enum class OpenedParticipantType {
        ACTIVE, INACTIVE
    }
}
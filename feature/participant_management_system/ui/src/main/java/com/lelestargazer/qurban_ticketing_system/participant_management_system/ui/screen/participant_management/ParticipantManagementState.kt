package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management

import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant

data class ParticipantManagementState(
    val query: String = "",
    val activeParticipant: List<Participant> = emptyList(),
    val inactiveParticipant: List<Participant> = emptyList(),
    val openedParticipantType: OpenedParticipantType? = null,
    val openedParticipantIndex: Int? = null,
) {

    enum class OpenedParticipantType {
        ACTIVE, INACTIVE
    }
}
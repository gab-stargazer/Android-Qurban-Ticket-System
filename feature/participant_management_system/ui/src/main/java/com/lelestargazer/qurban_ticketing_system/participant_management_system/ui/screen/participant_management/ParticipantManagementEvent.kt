package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management

import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementState.OpenedParticipantType

sealed class ParticipantManagementEvent {
    data class OnParticipantPressed(
        val type: OpenedParticipantType?,
        val index: Int?,
    ) : ParticipantManagementEvent()

    data class OnQueryChanged(
        val query: String,
    ) : ParticipantManagementEvent()

    data object OnNavigateToAddParticipant : ParticipantManagementEvent()

    data class OnNavigateToEditParticipant(
        val participant: Participant,
    ) : ParticipantManagementEvent()
}
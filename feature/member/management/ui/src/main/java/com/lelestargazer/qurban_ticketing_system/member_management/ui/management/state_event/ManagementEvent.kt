package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event

import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member

sealed class ManagementEvent {
    data class OnPressed(
        val index: Int?,
    ) : ManagementEvent()

    data class OnQueryChanged(
        val query: String,
    ) : ManagementEvent()

    data object OnNavigateToAdd : ManagementEvent()

    data class OnNavigateToEdit(
        val participantRecipient: Member,
    ) : ManagementEvent()

    data object OnBackPressed: ManagementEvent()
}
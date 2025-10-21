package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event

import android.net.Uri
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.FilterType
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member

sealed interface ManagementEvent {
    data class OnSearchQueryChanged(
        val query: String,
    ) : ManagementEvent

    data class OnFilterTypeChanged(
        val newFilterType: FilterType
    ) : ManagementEvent

    data class OnFilterMenuStateChanged(
        val newFilterMenuState: Boolean
    ) : ManagementEvent


    //  UI Click Event
    data object OnImportMemberClicked
        : ManagementEvent

    data class OnMemberClicked(
        val index: Int?,
    ) : ManagementEvent

    data object OnNavigateToAddClicked
        : ManagementEvent

    data class OnNavigateToEditClicked(
        val participantRecipient: Member,
    ) : ManagementEvent

    //  UI State Event
    data class OnFabMenuStateChanged(
        val newState: Boolean
    ) : ManagementEvent

    data object OnPermissionDialogDismissed
        : ManagementEvent

    data class OnImportData(
        val uri: Uri
    ) : ManagementEvent

    data class OnContactByPhoneNumberDialogStateChanged(
        val newState: Boolean,
        val phoneNumber: String?
    ) : ManagementEvent


    data object OnBackPressed : ManagementEvent
}
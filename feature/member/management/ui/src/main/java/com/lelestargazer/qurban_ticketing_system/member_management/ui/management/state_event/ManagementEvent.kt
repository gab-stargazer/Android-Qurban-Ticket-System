package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event

import android.net.Uri
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member

sealed interface ManagementEvent {
    data class OnPressed(
        val index: Int?,
    ) : ManagementEvent

    data class OnQueryChanged(
        val query: String,
    ) : ManagementEvent

    data object OnNavigateToAdd : ManagementEvent

    data class OnNavigateToEdit(
        val participantRecipient: Member,
    ) : ManagementEvent

    data object OnBackPressed : ManagementEvent

    data object OnDialogOpened : ManagementEvent
    data object OnDialogDismissed : ManagementEvent

    sealed interface BottomSheetEvent : ManagementEvent {
        data object OnExportData : BottomSheetEvent
        data class OnImportData(val uri: Uri) : BottomSheetEvent
        data object OnOpened : BottomSheetEvent
        data object OnDismissed : BottomSheetEvent
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event

import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType

sealed class AddEditUiEvent {

    data class OnNameChanged(val name: String) : AddEditUiEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : AddEditUiEvent()
    data class OnAddressChanged(val address: String) : AddEditUiEvent()
    data class OnStatusChanged(val newStatus: QurbanStatus) : AddEditUiEvent()
    data class OnTypeChanged(val newType: QurbanType) : AddEditUiEvent()
    data object OnBackPressed : AddEditUiEvent()
    data object OnAddEditPressed : AddEditUiEvent()
    data object OnDeletePressed : AddEditUiEvent()
}
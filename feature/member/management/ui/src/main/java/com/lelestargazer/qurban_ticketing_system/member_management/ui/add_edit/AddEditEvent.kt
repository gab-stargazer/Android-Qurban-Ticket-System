package com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit

sealed class AddEditEvent {

    data class OnNameChanged(val name: String) : AddEditEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : AddEditEvent()
    data class OnAddressChanged(val address: String) : AddEditEvent()
    data class OnAdditionalDescriptionChanged(val description: String) : AddEditEvent()
    data class OnStatusChanged(val isParticipant: Boolean) : AddEditEvent()
    data class OnIsActiveReceiverChanged(val isActive: Boolean) : AddEditEvent()
    data object OnAddEdit : AddEditEvent()
    data object OnBackPressed : AddEditEvent()
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit

sealed class ParticipantAddEditEvent {

    data class OnNameChanged(val name: String) : ParticipantAddEditEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : ParticipantAddEditEvent()
    data class OnAddressChanged(val address: String) : ParticipantAddEditEvent()
    data class OnHouseholdSizeChanged(val householdSize: Int?) : ParticipantAddEditEvent()
    data class OnAdditionalDescriptionChanged(val description: String) : ParticipantAddEditEvent()
    data class OnIsActiveChanged(val isActive: Boolean) : ParticipantAddEditEvent()
    data object OnAddEditParticipant : ParticipantAddEditEvent()
}
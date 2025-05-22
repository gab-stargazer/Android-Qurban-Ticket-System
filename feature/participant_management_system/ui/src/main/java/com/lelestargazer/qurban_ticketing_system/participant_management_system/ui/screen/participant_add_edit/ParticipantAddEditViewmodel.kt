package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.ParticipantRepository
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParticipantAddEditViewmodel(
    private val screenType: ParticipantAddOrEditType,
    private val initialData: Participant?,
    private val navController: NavHostController,
    private val repository: ParticipantRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        ParticipantAddEditState(
            screenType = screenType,
            name = initialData?.name.orEmpty(),
            phoneNumber = initialData?.phoneNumber.orEmpty(),
            address = initialData?.address.orEmpty(),
            householdSize = initialData?.householdSize,
            description = initialData?.description.orEmpty(),
            isActive = initialData?.isActive ?: true
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: ParticipantAddEditEvent) = viewModelScope.launch {
        when (event) {
            is ParticipantAddEditEvent.OnNameChanged -> _state.update {
                it.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is ParticipantAddEditEvent.OnPhoneNumberChanged -> _state.update {
                it.copy(
                    phoneNumber = event.phoneNumber,
                    phoneNumberError = null
                )
            }

            is ParticipantAddEditEvent.OnAddressChanged -> _state.update {
                it.copy(
                    address = event.address,
                    addressError = null
                )
            }

            is ParticipantAddEditEvent.OnHouseholdSizeChanged -> _state.update {
                it.copy(
                    householdSize = event.householdSize,
                    householdSizeError = null
                )
            }

            is ParticipantAddEditEvent.OnAdditionalDescriptionChanged -> _state.update {
                it.copy(
                    description = event.description
                )
            }

            is ParticipantAddEditEvent.OnIsActiveChanged -> _state.update {
                it.copy(
                    isActive = event.isActive
                )
            }

            ParticipantAddEditEvent.OnAddEditParticipant -> insertOrEditParticipant()
        }
    }

    private fun insertOrEditParticipant() = viewModelScope.launch {

        val currentState = state.value
        val nameError = currentState.validateName()
        val phoneNumberError = currentState.validatePhoneNumber()
        val addressError = currentState.validateAddress()
        val householdError = currentState.validateHouseholdSize()

        val errors = listOf(
            nameError,
            phoneNumberError,
            addressError,
            householdError
        )

        if (errors.any { it.isLeft() }) {
            _state.update {
                it.copy(
                    nameError = nameError.leftOrNull(),
                    phoneNumberError = phoneNumberError.leftOrNull(),
                    addressError = addressError.leftOrNull(),
                    householdSizeError = householdError.leftOrNull()
                )
            }
            return@launch
        }

        when (screenType) {
            ParticipantAddOrEditType.ADD -> {
                repository.insertParticipant(
                    name = currentState.name,
                    phoneNumber = currentState.phoneNumber,
                    address = currentState.address,
                    householdSize = currentState.householdSize ?: 1,
                    description = currentState.description,
                )
            }

            ParticipantAddOrEditType.EDIT -> {
                repository.updateParticipant(
                    Participant(
                        id = initialData?.id ?: throw Exception("Something went wrong"),
                        name = currentState.name,
                        phoneNumber = currentState.phoneNumber,
                        address = currentState.address,
                        householdSize = currentState.householdSize ?: 0,
                        description = currentState.description,
                        isActive = currentState.isActive
                    )
                )
            }
        }

        navController.popBackStack()
    }
}
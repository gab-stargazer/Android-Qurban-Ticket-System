package com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.EDIT
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditViewmodel(
    private val screenType: Type,
    private val initialData: Member?,
    private val navController: NavHostController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AddEditState(
            screenType = screenType,
            name = initialData?.name.orEmpty(),
            phoneNumber = initialData?.phoneNumber.orEmpty(),
            address = initialData?.address.orEmpty(),
            description = initialData?.description.orEmpty(),
            isParticipant = initialData?.isParticipant ?: false,
            isActive = initialData?.isActive ?: true
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: AddEditEvent) = viewModelScope.launch {
        when (event) {
            is AddEditEvent.OnNameChanged -> _state.update {
                it.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is AddEditEvent.OnPhoneNumberChanged -> _state.update {
                it.copy(
                    phoneNumber = event.phoneNumber,
                    phoneNumberError = null
                )
            }

            is AddEditEvent.OnAddressChanged -> _state.update {
                it.copy(
                    address = event.address,
                    addressError = null
                )
            }

            is AddEditEvent.OnAdditionalDescriptionChanged -> _state.update {
                it.copy(
                    description = event.description
                )
            }

            is AddEditEvent.OnStatusChanged ->
                when (event.isParticipant) {
                    true -> _state.update {
                        it.copy(
                            isParticipant = event.isParticipant,
                            isActive = true
                        )
                    }

                    false -> _state.update {
                        it.copy(
                            isParticipant = event.isParticipant
                        )
                    }
                }

            is AddEditEvent.OnIsActiveReceiverChanged ->
                when (event.isActive) {
                    true -> _state.update {
                        it.copy(
                            isActive = event.isActive
                        )
                    }

                    false -> _state.update {
                        it.copy(
                            isParticipant = false,
                            isActive = event.isActive
                        )
                    }
                }

            AddEditEvent.OnAddEdit -> insertOrEditParticipant()

            AddEditEvent.OnBackPressed -> navController.popBackStack()
        }
    }

    private fun insertOrEditParticipant() = viewModelScope.launch {

        val currentState = state.value
        val nameError = currentState.validateName()
        val phoneNumberError = currentState.validatePhoneNumber()
        val addressError = currentState.validateAddress()

        val errors = listOf(
            nameError,
            phoneNumberError,
            addressError
        )

        if (errors.any { it.isLeft() }) {
            _state.update {
                it.copy(
                    nameError = nameError.leftOrNull(),
                    phoneNumberError = phoneNumberError.leftOrNull(),
                    addressError = addressError.leftOrNull(),
                )
            }
            return@launch
        }

        when (screenType) {
            ADD -> {
                repository.insertMember(
                    name = currentState.name,
                    phoneNumber = currentState.phoneNumber,
                    rt = 0,
                    rw = 0,
                    address = currentState.address,
                    description = currentState.description,
                    isParticipant = currentState.isParticipant
                )
            }

            EDIT -> {
                repository.updateMember(
                    Member(
                        id = initialData?.id ?: throw Exception("Something went wrong"),
                        name = currentState.name,
                        phoneNumber = currentState.phoneNumber,
                        rt = 0,
                        rw = 0,
                        address = currentState.address,
                        description = currentState.description,
                        isParticipant = currentState.isParticipant,
                        isActive = currentState.isActive
                    )
                )
            }
        }

        navController.popBackStack()
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.EDIT
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.address
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.isLoading
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.qurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.qurbanType
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.isParticipant
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.Provided

@KoinViewModel
class AddEditViewmodel(
    @Provided private val screenType: ScreenType,
    @Provided private val initialData: Member?,
    @Provided private val navController: NavHostController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AddEditUiState(
            screenType = screenType,
            name = initialData?.name.orEmpty(),
            phoneNumber = initialData?.phoneNumber.orEmpty(),
            address = initialData?.address.orEmpty()
        )
    )
    val state = _state.asStateFlow()

    fun onEvent(event: AddEditUiEvent) = viewModelScope.launch {
        when (event) {
            is AddEditUiEvent.OnNameChanged -> _state.update { currentState ->
                currentState.copy(
                    name = event.name,
                    nameError = null
                )
            }

            is AddEditUiEvent.OnPhoneNumberChanged -> _state.update { currentState ->
                currentState.copy(
                    phoneNumber = event.phoneNumber.trim(),
                    phoneNumberError = null
                )
            }

            is AddEditUiEvent.OnAddressChanged -> _state.update { currentState ->
                AddEditUiState.address.set(
                    source = currentState,
                    focus = event.address
                )
            }


            is AddEditUiEvent.OnStatusChanged -> _state.update { currentState ->
                AddEditUiState.qurbanStatus.set(
                    source = currentState,
                    focus = event.newStatus
                )
            }

            is AddEditUiEvent.OnTypeChanged -> _state.update { currentState ->
                AddEditUiState.qurbanType.set(
                    source = currentState,
                    focus = event.newType
                )
            }


            AddEditUiEvent.OnBackPressed -> navController.popBackStack()

            AddEditUiEvent.OnAddEditPressed -> insertOrEditParticipant()

            AddEditUiEvent.OnDeletePressed -> viewModelScope.launch {
                initialData?.let { member ->
                    _state.update { currentSate ->
                        AddEditUiState.isLoading.set(
                            source = currentSate,
                            focus = true
                        )
                    }

                    repository.deleteMember(member)
                    navController.popBackStack()
                }
            }
        }
    }

    private fun insertOrEditParticipant() = viewModelScope.launch {

        val currentState = _state.updateAndGet { currentState ->
            AddEditUiState.isLoading.set(
                currentState,
                true
            )
        }

        val nameError = currentState.validateName()
        val phoneNumberError = currentState.validatePhoneNumber()

        val errors = listOf(
            nameError,
            phoneNumberError
        )

        if (errors.any { it.isLeft() }) {
            _state.update {
                it.copy(
                    nameError = nameError.leftOrNull(),
                    phoneNumberError = phoneNumberError.leftOrNull(),
                )
            }
            return@launch
        }

        when (screenType) {
            ADD -> {
                repository.insertMember(
                    name = currentState.name,
                    phoneNumber = currentState.phoneNumber.ifBlank { null },
                    address = currentState.address.ifBlank { null },
                    qurbanStatus = currentState.qurbanStatus,
                    qurbanType =
                        if (currentState.qurbanStatus.isParticipant())
                            currentState.qurbanType
                        else null
                )
            }

            EDIT -> {
                repository.updateMember(
                    Member(
                        id = initialData?.id
                            ?: throw Exception("ID is Null when trying to update member"),
                        name = currentState.name,
                        phoneNumber = currentState.phoneNumber.ifBlank { null },
                        address = currentState.address.ifBlank { null },
                        status = currentState.qurbanStatus,
                        type =
                            if (currentState.qurbanStatus.isParticipant())
                                currentState.qurbanType
                            else null
                    )
                )
            }
        }

        navController.popBackStack()
    }
}
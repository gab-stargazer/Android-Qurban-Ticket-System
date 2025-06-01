package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.EDIT
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagementViewModel(
    private val navController: NavHostController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _activeParticipantRecipient: Flow<List<Member>> =
        repository.getActiveMembers()
    private val _inactiveParticipantRecipient: Flow<List<Member>> =
        repository.getInactiveMembers()

    private val _currentState = MutableStateFlow(MemberManagementState())

    val state = combine(
        flow = _activeParticipantRecipient,
        flow2 = _inactiveParticipantRecipient,
        flow3 = _currentState
    ) { activeMember, inactiveMember, currentState ->
        if (currentState.query.isNotBlank()) {
            MemberManagementState(
                query = currentState.query,
                activeParticipantRecipient = activeMember.filter { member: Member ->
                    member.name.lowercase().contains(currentState.query.lowercase())
                },
                inactiveParticipantRecipient = inactiveMember.filter { member: Member ->
                    member.name.lowercase().contains(currentState.query.lowercase())
                },
                openedParticipantType = currentState.openedParticipantType,
                openedParticipantIndex = currentState.openedParticipantIndex
            )
        } else {
            MemberManagementState(
                query = currentState.query,
                activeParticipantRecipient = activeMember,
                inactiveParticipantRecipient = inactiveMember,
                openedParticipantType = currentState.openedParticipantType,
                openedParticipantIndex = currentState.openedParticipantIndex
            )
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MemberManagementState()
    )

    fun onEvent(event: ManagementEvent) = viewModelScope.launch {
        when (event) {
            is ManagementEvent.OnPressed -> _currentState.update {
                it.copy(
                    openedParticipantType = event.type,
                    openedParticipantIndex = event.index
                )
            }

            is ManagementEvent.OnQueryChanged -> _currentState.update {
                it.copy(
                    query = event.query,
                    openedParticipantType = null,
                    openedParticipantIndex = null
                )
            }

            ManagementEvent.OnNavigateToAdd -> {
                navController.navigate(
                    MemberAddEdit(
                        type = ADD,
                        participantRecipient = null
                    )
                )
            }

            is ManagementEvent.OnNavigateToEdit -> {
                navController.navigate(
                    MemberAddEdit(
                        type = EDIT,
                        participantRecipient = event.participantRecipient
                    )
                )
            }

            ManagementEvent.OnBackPressed -> navController.popBackStack()
        }
    }
}
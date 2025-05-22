package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.ParticipantRepository
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType.ADD
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType.EDIT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ParticipantManagementViewModel(
    private val navController: NavHostController,
    private val repository: ParticipantRepository,
) : ViewModel() {

    private val _activeParticipant: Flow<List<Participant>> =
        repository.getActiveParticipants()
    private val _inactiveParticipant: Flow<List<Participant>> =
        repository.getInactiveParticipants()

    private val _currentState = MutableStateFlow(ParticipantManagementState())

    val state = combine(
        flow = _activeParticipant,
        flow2 = _inactiveParticipant,
        flow3 = _currentState
    ) { activeParticipant, inactiveParticipant, currentState ->
        if (currentState.query.isNotBlank()) {
            ParticipantManagementState(
                query = currentState.query,
                activeParticipant = activeParticipant.filter { participant: Participant ->
                    participant.name.lowercase().contains(currentState.query.lowercase())
                },
                inactiveParticipant = inactiveParticipant.filter { participant: Participant ->
                    participant.name.lowercase().contains(currentState.query.lowercase())
                },
                openedParticipantType = currentState.openedParticipantType,
                openedParticipantIndex = currentState.openedParticipantIndex
            )
        } else {
            ParticipantManagementState(
                query = currentState.query,
                activeParticipant = activeParticipant,
                inactiveParticipant = inactiveParticipant,
                openedParticipantType = currentState.openedParticipantType,
                openedParticipantIndex = currentState.openedParticipantIndex
            )
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = ParticipantManagementState()
    )

    fun onEvent(event: ParticipantManagementEvent) = viewModelScope.launch {
        when (event) {
            is ParticipantManagementEvent.OnParticipantPressed -> _currentState.update {
                it.copy(
                    openedParticipantType = event.type,
                    openedParticipantIndex = event.index
                )
            }

            is ParticipantManagementEvent.OnQueryChanged -> _currentState.update {
                it.copy(
                    query = event.query,
                    openedParticipantType = null,
                    openedParticipantIndex = null
                )
            }

            ParticipantManagementEvent.OnNavigateToAddParticipant -> {
                navController.navigate(
                    ManagementRoute.ParticipantAddOrEdit(
                        type = ADD,
                        participant = null
                    )
                )
            }

            is ParticipantManagementEvent.OnNavigateToEditParticipant -> {
                navController.navigate(
                    ManagementRoute.ParticipantAddOrEdit(
                        type = EDIT,
                        participant = event.participant
                    )
                )
            }
        }
    }
}
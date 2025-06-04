package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.EDIT
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManagementViewModel(
    private val navController: NavHostController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _members: Flow<PagingData<Member>> = _searchQuery
        .flatMapLatest { repository.getMembers(it) }
        .debounce(150)

    private val _currentState: MutableStateFlow<MemberManagementState> =
        MutableStateFlow(MemberManagementState())

    val state = combine(
        flow = _searchQuery,
        flow2 = _currentState,
    ) { query, state ->
        MemberManagementState(
            query = query,
            members = _members,
            openedParticipantType = state.openedParticipantType,
            openedParticipantIndex = state.openedParticipantIndex
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MemberManagementState()
    )

    fun onEvent(event: ManagementEvent) = viewModelScope.launch {
        when (event) {
            is ManagementEvent.OnPressed -> {
                if (event.index == _currentState.value.openedParticipantIndex) {
                    _currentState.update {
                        it.copy(
                            openedParticipantIndex = null
                        )
                    }
                } else {
                    _currentState.update {
                        it.copy(
                            openedParticipantIndex = event.index
                        )
                    }
                }
            }

            is ManagementEvent.OnQueryChanged -> {
                _currentState.update {
                    it.copy(
                        openedParticipantType = null,
                        openedParticipantIndex = null
                    )
                }

                _searchQuery.update { event.query }
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
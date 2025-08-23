package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import arrow.optics.copy
import com.lelestargazer.qurban_ticketing_system.common.UiController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.BottomSheetEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.bottomSheetState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.isDialogOpened
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.isLoading
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.isOpened
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.openedParticipantIndex
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.openedParticipantType
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
    private val uiController: UiController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _members: Flow<PagingData<Member>> = _searchQuery
        .flatMapLatest { repository.getActiveMembers(it) }
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
            openedParticipantIndex = state.openedParticipantIndex,

            isBottomSheetOpened = state.isBottomSheetOpened,

            isDialogOpened = state.isDialogOpened,

            bottomSheetState = state.bottomSheetState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MemberManagementState()
    )

    fun onEvent(event: ManagementEvent) = viewModelScope.launch {
        when {
            event is ManagementEvent.OnPressed -> {
                _currentState.update { state_ ->
                    MemberManagementState.openedParticipantIndex.set(
                        source = state_,
                        focus =
                            if (event.index == _currentState.value.openedParticipantIndex) {
                                null
                            } else {
                                event.index
                            }
                    )
                }
            }

            event is ManagementEvent.OnQueryChanged -> {
                _currentState.update { state_ ->
                    with(state_) {
                        copy {
                            MemberManagementState.openedParticipantIndex.set(null)
                            MemberManagementState.openedParticipantType.set(null)
                        }
                    }
                }

                _searchQuery.update { event.query }
            }

            event is ManagementEvent.OnNavigateToAdd -> {
                uiController.navController.navigate(
                    MemberAddEdit(
                        type = ADD,
                        participantRecipient = null
                    )
                )
            }

            event is ManagementEvent.OnNavigateToEdit -> {
                uiController.navController.navigate(
                    MemberAddEdit(
                        type = EDIT,
                        participantRecipient = event.participantRecipient
                    )
                )
            }

            event is ManagementEvent.OnBackPressed -> uiController.navController.popBackStack()

            event is ManagementEvent.OnDialogDismissed -> _currentState.update {
                MemberManagementState.isDialogOpened.set(it, false)
            }

            event is ManagementEvent.OnDialogOpened -> _currentState.update {
                MemberManagementState.isDialogOpened.set(it, true)
            }

            event is BottomSheetEvent -> onBottomSheetEvent(event)
        }
    }

    private fun onBottomSheetEvent(event: BottomSheetEvent) = viewModelScope.launch {
        when (event) {
            BottomSheetEvent.OnExportData -> {
                repository.exportMembersToExcel()
            }

            is BottomSheetEvent.OnImportData -> {
                _currentState.update {
                    MemberManagementState.bottomSheetState.isLoading.set(
                        it,
                        true
                    )
                }

                repository.importMembersByExcel(
                    uri = event.uri
                ).fold(
                    ifLeft = {
                        _currentState.update {
                            MemberManagementState.bottomSheetState.set(
                                it,
                                MemberManagementState.BottomSheetState()
                            )
                        }
                        uiController.snackBarHost.showSnackbar(it)
                    }, ifRight = {
                        _currentState.update {
                            MemberManagementState.bottomSheetState.set(
                                it,
                                MemberManagementState.BottomSheetState()
                            )
                        }
                        uiController.snackBarHost.showSnackbar(it)
                    }
                )
            }

            BottomSheetEvent.OnOpened -> _currentState.update {
                MemberManagementState.bottomSheetState.isOpened.set(
                    source = it,
                    focus = true
                )
            }

            BottomSheetEvent.OnDismissed -> _currentState.update {
                MemberManagementState.bottomSheetState.isOpened.set(
                    source = it,
                    focus = false
                )
            }
        }
    }
}
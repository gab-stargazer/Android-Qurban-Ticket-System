package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import arrow.optics.copy
import com.lelestargazer.qurban_ticketing_system.common.UiController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.error_location_is_empty
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.error_selected_date_is_empty
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.FilterType
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.DialogCreateCouponEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.datePickerStateError
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.dialogCreateCouponState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.isContactByPhoneNumberDialogOpened
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.isDialogCreateCouponShowed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.isFabMenuExpanded
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.isFilterMenuOpened
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.isNotificationPermissionDialogOpened
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.location
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.locationError
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.openedParticipantIndex
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.phoneNumber
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ManagementViewModel(
    private val uiController: UiController,
    private val repository: MemberRepository,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val _filterType: MutableStateFlow<FilterType> = MutableStateFlow(FilterType.All)


    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val _members: Flow<PagingData<Member>> = combine(
        _searchQuery,
        _filterType
    ) { searchQuery, filterType ->
        Pair(searchQuery, filterType)
    }.flatMapLatest { pair ->
        val searchQuery = pair.first
        val status = pair.second

        when (status) {
            FilterType.All -> repository.selectAllMembers(searchQuery)

            FilterType.Participant -> repository.selectMembersByStatus(
                searchQuery,
                QurbanStatus.Participant
            )

            FilterType.Recipient -> repository.selectMembersByStatus(
                searchQuery,
                QurbanStatus.Recipient
            )
        }
    }

    private val _currentState: MutableStateFlow<MemberManagementState> =
        MutableStateFlow(MemberManagementState())

    val state = combine(
        flow = _searchQuery,
        flow2 = _filterType,
        flow3 = _currentState,
    ) { searchQuery, filterType, state ->
        MemberManagementState(
            // Search and Filter
            searchQuery = searchQuery,
            filterType = filterType,

            //  Visibility State
            isFilterMenuOpened = state.isFilterMenuOpened,
            isFabMenuExpanded = state.isFabMenuExpanded,
            isNotificationPermissionDialogOpened = state.isNotificationPermissionDialogOpened,

            //  Contact By Phone Number
            isContactByPhoneNumberDialogOpened = state.isContactByPhoneNumberDialogOpened,
            phoneNumber = state.phoneNumber,

            //  Dialog Create Coupon
            isDialogCreateCouponShowed = state.isDialogCreateCouponShowed,
            dialogCreateCouponState = state.dialogCreateCouponState,

            members = _members,
            openedParticipantIndex = state.openedParticipantIndex,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = MemberManagementState()
    )

    fun onEvent(event: ManagementEvent) = viewModelScope.launch {
        when (event) {
            is ManagementEvent.OnSearchQueryChanged -> {
                _currentState.update { currentState ->
                    currentState.copy(
                        openedParticipantIndex = null
                    )
                }

                _searchQuery.update { event.query }
            }

            is ManagementEvent.OnFilterTypeChanged -> _filterType.update { event.newFilterType }

            is ManagementEvent.OnFilterMenuStateChanged -> _currentState.update { currentState ->
                MemberManagementState.isFilterMenuOpened.set(
                    source = currentState,
                    focus = event.newFilterMenuState
                )
            }

            is ManagementEvent.OnMemberClicked -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.openedParticipantIndex set
                                if (event.index == currentState.openedParticipantIndex) {
                                    null
                                } else {
                                    event.index
                                }

                        MemberManagementState.isFabMenuExpanded set false
                    }
                }
            }

            is ManagementEvent.OnBackPressed -> uiController.navController.popBackStack()

            //  Ui Click Event
            ManagementEvent.OnImportMemberClicked -> _currentState.update { currentState ->
                currentState.copy {
                    MemberManagementState.isNotificationPermissionDialogOpened set true
                    MemberManagementState.isFabMenuExpanded set false
                }
            }

            is ManagementEvent.OnNavigateToAddClicked -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.isFabMenuExpanded set false
                        MemberManagementState.openedParticipantIndex set null
                    }
                }

                uiController.navController.navigate(
                    MemberAddEdit(
                        screenType = MemberAddEdit.ScreenType.ADD,
                        participantRecipient = null
                    )
                )
            }

            is ManagementEvent.OnNavigateToEditClicked -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.isFabMenuExpanded set false
                        MemberManagementState.openedParticipantIndex set null
                    }
                }

                uiController.navController.navigate(
                    MemberAddEdit(
                        screenType = MemberAddEdit.ScreenType.EDIT,
                        participantRecipient = event.participantRecipient
                    )
                )
            }

            //  UI State Event
            is ManagementEvent.OnFabMenuStateChanged -> _currentState.update { currentState ->
                MemberManagementState.isFabMenuExpanded.set(
                    source = currentState,
                    focus = event.newState
                )
            }

            ManagementEvent.OnPermissionDialogDismissed -> _currentState.update { currentState ->
                MemberManagementState.isNotificationPermissionDialogOpened.set(
                    source = currentState,
                    focus = false
                )
            }

            is ManagementEvent.OnImportData -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.isNotificationPermissionDialogOpened set false
                        MemberManagementState.isFabMenuExpanded set false
                    }
                }

                repository.importMembersByExcel(event.uri)
            }

            is ManagementEvent.OnContactByPhoneNumberDialogStateChanged -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.isContactByPhoneNumberDialogOpened set event.newState
                        MemberManagementState.phoneNumber set event.phoneNumber
                    }
                }
            }

            is ManagementEvent.OnCreateCouponDialogShowed -> {
                _currentState.update { currentState ->
                    currentState.copy {
                        MemberManagementState.isDialogCreateCouponShowed set event.isShown
                        MemberManagementState.isFabMenuExpanded set false
                    }
                }
            }

            //  Create Coupon Dialog Event
            is DialogCreateCouponEvent.OnLocationChanged -> _currentState.update { currentState ->
                currentState.copy {
                    MemberManagementState.dialogCreateCouponState.location set event.location
                    MemberManagementState.dialogCreateCouponState.locationError set null
                }
            }

            DialogCreateCouponEvent.OnSelectedPickupDateChanged -> _currentState.update { currentState ->
                MemberManagementState.dialogCreateCouponState.datePickerStateError.set(
                    source = currentState,
                    focus = null
                )
            }

            DialogCreateCouponEvent.OnCreateCoupon -> viewModelScope.launch {
                val currentState = state.value
                val locationError: Int? =
                    if (currentState.dialogCreateCouponState.location.isBlank())
                        error_location_is_empty
                    else null

                val selectedDateError =
                    if (currentState.dialogCreateCouponState.datePickerState.selectedDateMillis == null)
                        error_selected_date_is_empty
                    else null

                val errors = listOf(locationError, selectedDateError)
                if (errors.any { error -> error != null }) {
                    _currentState.update { currentState_ ->
                        currentState_.copy {
                            MemberManagementState.dialogCreateCouponState.locationError set locationError
                            MemberManagementState.dialogCreateCouponState.datePickerStateError set selectedDateError
                        }
                    }
                    return@launch
                }


            }
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.ParticipantManagementSystemState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ParticipantManagementViewModel : ViewModel() {

    val state: StateFlow<ParticipantManagementSystemState>
        field = MutableStateFlow(ParticipantManagementSystemState())
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.di

import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditViewmodel
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val participantManagementUiModule = module {
    viewModel {
        ParticipantAddEditViewmodel(get(), getOrNull(), get(), get())
    }

    viewModelOf(::ParticipantManagementViewModel)
}
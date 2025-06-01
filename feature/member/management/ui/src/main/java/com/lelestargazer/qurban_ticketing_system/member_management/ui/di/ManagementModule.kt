package com.lelestargazer.qurban_ticketing_system.member_management.ui.di

import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditViewmodel
import com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export.ImportExportViewModel
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.viewmodel.ManagementViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val managementUiModule = module {
    viewModel {
        AddEditViewmodel(get(), getOrNull(), get(), get())
    }
    viewModelOf(::ManagementViewModel)
    viewModelOf(::ImportExportViewModel)
}
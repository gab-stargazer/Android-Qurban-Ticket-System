package com.lelestargazer.qurban_ticketing_system.member_management.ui.route

import androidx.annotation.Keep
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditScreen
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditViewmodel
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.navType
import com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export.ImportExportScreen
import com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export.ImportExportViewModel
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.screen.ManagementScreen
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.viewmodel.ManagementViewModel
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute.ImportExport
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute.Management
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.theme.LocalParentNavigator
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

@Serializable
data class MemberAddEdit(
    val type: Type,
    val participantRecipient: Member?,

    ) {

    @Keep
    enum class Type {
        ADD, EDIT
    }
}

fun NavGraphBuilder.managementRoute() {
    composable<Management> {
        val navController = LocalParentNavigator.current
        val vm: ManagementViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    navController
                )
            }
        )

        val state by vm.state.collectAsStateWithLifecycle()
        ManagementScreen(
            state = state,
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }

    composable<MemberAddEdit>(
        typeMap = mapOf(
            typeOf<MemberAddEdit.Type>() to NavType.EnumType(MemberAddEdit.Type::class.java),
            typeOf<Member?>() to navType
        )
    ) {
        val args = it.toRoute<MemberAddEdit>()
        val navController = LocalParentNavigator.current

        val vm: AddEditViewmodel =
            koinViewModel(
                parameters = {
                    parametersOf(
                        args.type,
                        args.participantRecipient,
                        navController
                    )
                }
            )

        val state by vm.state.collectAsStateWithLifecycle()
        AddEditScreen(
            state = state,
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }

    composable<ImportExport> {
        val navController = LocalParentNavigator.current

        val vm: ImportExportViewModel =
            koinViewModel(
                parameters = {
                    parametersOf(navController)
                }
            )

        ImportExportScreen(
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
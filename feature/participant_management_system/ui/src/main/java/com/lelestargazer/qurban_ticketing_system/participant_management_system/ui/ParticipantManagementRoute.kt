package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.lelestargazer.qurban_ticketing_system.common.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantManagement
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditScreen
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditViewmodel
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementScreen
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.typeOf

fun NavGraphBuilder.participantManagementRoute() {
    composable<ParticipantManagement> {
        val navController = LocalParentNavigator.current
        val vm: ParticipantManagementViewModel = koinViewModel(
            parameters = {
                parametersOf(
                    navController
                )
            }
        )
        val state = vm.state.collectAsStateWithLifecycle()
        ParticipantManagementScreen(
            state = state.value,
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }

    composable<ManagementRoute.ParticipantAddOrEdit>(
        typeMap = mapOf(
            typeOf<ParticipantAddOrEditType>() to NavType.EnumType(ParticipantAddOrEditType::class.java),
            typeOf<Participant?>() to navType
        )
    ) {
        val args = it.toRoute<ManagementRoute.ParticipantAddOrEdit>()
        val navController = LocalParentNavigator.current

        val vm: ParticipantAddEditViewmodel =
            koinViewModel(
                parameters = {
                    parametersOf(
                        args.type,
                        args.participant,
                        navController
                    )
                }
            )

        val state = vm.state.collectAsStateWithLifecycle()
        ParticipantAddEditScreen(
            state = state.value,
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
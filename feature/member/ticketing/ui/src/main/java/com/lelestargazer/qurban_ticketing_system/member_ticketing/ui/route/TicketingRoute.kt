package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.route

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.lelestargazer.qurban_ticketing_system.common.UiController
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute.Ticketing
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingScreen
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingViewmodel
import com.lelestargazer.qurban_ticketing_system.theme.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.theme.LocalSnackbarHost
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.ticketingRoute() {
    composable<Ticketing> {
        val navController = LocalParentNavigator.current
        val snackBarHost = LocalSnackbarHost.current
        val vm = koinViewModel<TicketingViewmodel>(
            parameters = {
                parametersOf(
                    UiController(navController, snackBarHost)
                )
            }
        )

        val state by vm.state.collectAsStateWithLifecycle()
        TicketingScreen(
            state = state,
            onEvent = vm::onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}
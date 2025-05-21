package com.lelestargazer.qurban_ticketing_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lelestargazer.qurban_ticketing_system.common.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.common.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.common.shared.CustomPadding
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.participantManagementRoute
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantManagement
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QurbanTicketingSystemTheme {
                val navController: NavHostController = rememberNavController()

                CompositionLocalProvider(
                    LocalParentNavigator provides navController,
                    LocalScreenPadding provides CustomPadding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    NavHost(
                        navController,
                        ParticipantManagement,
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                    ) {
                        participantManagementRoute()
                    }
                }

            }
        }
    }
}
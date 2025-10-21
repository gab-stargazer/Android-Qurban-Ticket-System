package com.lelestargazer.qurban_ticketing_system

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.managementRoute
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute.ImportExport
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.route.ticketingRoute
import com.lelestargazer.qurban_ticketing_system.theme.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.theme.LocalSnackbarHost
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.analytics.logEvent(
            FirebaseAnalytics.Event.APP_OPEN,
            null
        )

        enableEdgeToEdge()
        setContent {
            QurbanTicketingSystemTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(LocalSnackbarHost.current)
                    },
                    modifier = Modifier
                        .systemBarsPadding()
                ) { _ ->
                    NavHost(
                        navController = LocalParentNavigator.current,
                        startDestination = MemberRoute.HomeScreen,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        managementRoute()
                        ticketingRoute()

                        composable<MemberRoute.HomeScreen> {
                            val navController = LocalParentNavigator.current
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Button(
                                    onClick = {
                                        navController.navigate(MemberRoute.Management)
                                    }
                                ) { Text("Manajemen") }

                                Button(
                                    onClick = {
                                        navController.navigate(MemberRoute.Ticketing)
                                    }
                                ) {
                                    Text("Ticketing")
                                }

                                Button(
                                    onClick = {
                                        navController.navigate(ImportExport)
                                    }
                                ) {
                                    Text("Impor Expor Data")
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}
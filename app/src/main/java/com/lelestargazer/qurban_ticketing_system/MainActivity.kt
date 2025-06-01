package com.lelestargazer.qurban_ticketing_system

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.managementRoute
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute.ImportExport
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.route.ticketingRoute
import com.lelestargazer.qurban_ticketing_system.theme.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.theme.LocalSnackbarHost
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import java.io.File

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QurbanTicketingSystemTheme {
                val navController: NavHostController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val context = LocalContext.current

                val launcher = rememberLauncherForActivityResult(
                    ActivityResultContracts.OpenDocument()
                ) {
                    println(File(it!!.path))
                }

                CompositionLocalProvider(
                    LocalParentNavigator provides navController,
                    LocalSnackbarHost provides snackbarHostState
                ) {
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(snackbarHostState)
                        },
                        modifier = Modifier
                            .systemBarsPadding()
                    ) { _ ->
                        NavHost(
                            navController,
                            MemberRoute.HomeScreen,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            managementRoute()
                            ticketingRoute()

                            composable<MemberRoute.HomeScreen> {
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
}
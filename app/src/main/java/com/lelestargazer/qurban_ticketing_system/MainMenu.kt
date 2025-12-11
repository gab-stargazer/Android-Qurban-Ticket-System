package com.lelestargazer.qurban_ticketing_system

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.lelestargazer.qurban_ticketing_system.common.handleWhenLifecycleResumed
import com.lelestargazer.qurban_ticketing_system.member_shared.common.MemberRoute
import com.lelestargazer.qurban_ticketing_system.theme.LocalParentNavigator
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.component.ManagementTicketingBanner

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MainMenu() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    val navController = LocalParentNavigator.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ManagementTicketingBanner(
            title = "Menu Utama",
            isMainMenu = true,
            onBackPressed = {}
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            item {
                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed(
                            onResumed = {
                                navController.navigate(MemberRoute.Management)
                            }
                        )
                    },
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier.padding(
                        vertical = LocalScreenPadding.current.vertical,
                        horizontal = LocalScreenPadding.current.horizontal / 2
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "Manajemen\nUser",
                            style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        lifecycle.handleWhenLifecycleResumed(
                            onResumed = {

                            }
                        )
                    },
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier.padding(
                        vertical = LocalScreenPadding.current.vertical,
                        horizontal = LocalScreenPadding.current.horizontal / 2
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )

                        Text(
                            text = "Informasi\nAplikasi",
                            style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }
        }
    }
}
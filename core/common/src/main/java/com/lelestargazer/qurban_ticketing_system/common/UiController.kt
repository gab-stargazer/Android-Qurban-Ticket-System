package com.lelestargazer.qurban_ticketing_system.common

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController

data class UiController(
    val navController: NavHostController,
    val snackBarHost: SnackbarHostState
)
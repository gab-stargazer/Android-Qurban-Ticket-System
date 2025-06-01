package com.lelestargazer.qurban_ticketing_system.theme

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController

val LocalParentNavigator: ProvidableCompositionLocal<NavHostController> =
    compositionLocalOf { error("Navcontroller Haven't been initialized") }

val LocalScreenPadding: ProvidableCompositionLocal<CustomPadding> =
    compositionLocalOf { error("Local Padding haven't been initialized") }

val LocalSnackbarHost: ProvidableCompositionLocal<SnackbarHostState> =
    compositionLocalOf { error("Snackbar Host haven't been initialized") }
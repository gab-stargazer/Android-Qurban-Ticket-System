package com.lelestargazer.qurban_ticketing_system.common

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.lelestargazer.qurban_ticketing_system.common.shared.CustomPadding

val LocalParentNavigator: ProvidableCompositionLocal<NavHostController> =
    compositionLocalOf { error("Navcontroller Haven't been declared") }

val LocalScreenPadding: ProvidableCompositionLocal<CustomPadding> =
    compositionLocalOf { error("Local Padding haven't been initialized") }
package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import arrow.optics.optics

@optics
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi")
data class DialogCreateCouponState(
    val location: String = "",
    @StringRes val locationError: Int? = null,

    val datePickerState: DatePickerState = DatePickerState(
        locale = CalendarLocale.of("id", "ID"),
        initialDisplayMode = DisplayMode.Picker,
    ),
    @StringRes val datePickerStateError: Int? = null
) {
    companion object
}

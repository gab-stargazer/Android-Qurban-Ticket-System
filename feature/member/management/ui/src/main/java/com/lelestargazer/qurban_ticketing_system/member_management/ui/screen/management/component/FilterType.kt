package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component

import androidx.annotation.StringRes
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R

enum class FilterType(@get:StringRes val uiText: Int) {
    All(R.string.filter_type_all),
    Participant(R.string.filter_type_participant),
    Recipient(R.string.filter_type_recipient)
}
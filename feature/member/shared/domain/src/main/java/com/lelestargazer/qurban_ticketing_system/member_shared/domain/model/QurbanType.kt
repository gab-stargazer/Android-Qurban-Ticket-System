package com.lelestargazer.qurban_ticketing_system.member_shared.domain.model

import androidx.annotation.StringRes
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.R

enum class QurbanType(
    @get:StringRes val uiText: Int
) {
    Cow(R.string.qurban_type_cow), Goat(R.string.qurban_type_goat), Sheep(R.string.qurban_type_sheep)
}
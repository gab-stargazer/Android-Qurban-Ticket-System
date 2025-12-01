package com.lelestargazer.qurban_ticketing_system.theme.component

import androidx.compose.material3.SelectableDates
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant

@OptIn(ExperimentalTime::class)
object FutureSelectableDate : SelectableDates {


    private val today = Clock.System.now()
    private val zonedDateTime = ZonedDateTime.ofInstant(
        today.toJavaInstant(),
        ZoneId.systemDefault()
    )

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis > today.toEpochMilliseconds()
    }

    override fun isSelectableYear(year: Int): Boolean {
        return year >= zonedDateTime.year
    }
}
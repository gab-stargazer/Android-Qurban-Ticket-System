package com.lelestargazer.qurban_ticketing_system.common

import android.annotation.SuppressLint
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun String?.ifNotBlank(block: (String) -> Unit) {
    this?.let {
        if (it.isNotBlank()) {
            block(this)
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalTime::class)
fun Long.toFormattedDate(): String {
    val instant = Instant.fromEpochMilliseconds(this).toJavaInstant()

    val zonedDateTime: ZonedDateTime =
        ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formattedDate: String =
        zonedDateTime.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.of("id-ID")))

    return formattedDate
}
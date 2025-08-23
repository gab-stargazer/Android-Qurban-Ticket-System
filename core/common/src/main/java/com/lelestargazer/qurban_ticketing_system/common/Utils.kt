package com.lelestargazer.qurban_ticketing_system.common

fun String?.ifNotBlank(block: (String) -> Unit) {
    this?.let {
        if (it.isNotBlank()) {
            block(this)
        }
    }
}
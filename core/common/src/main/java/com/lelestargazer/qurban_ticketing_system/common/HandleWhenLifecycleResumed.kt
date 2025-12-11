package com.lelestargazer.qurban_ticketing_system.common

import androidx.lifecycle.Lifecycle

fun Lifecycle.State.handleWhenLifecycleResumed(
    onResumed: () -> Unit
) {
    if (this.isAtLeast(Lifecycle.State.RESUMED)) onResumed()
}
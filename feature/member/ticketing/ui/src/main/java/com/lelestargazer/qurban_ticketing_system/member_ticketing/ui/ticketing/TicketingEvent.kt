package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType

sealed class TicketingEvent {
    data object OnGenerateTicket : TicketingEvent()

    data object ClaimTicket : TicketingEvent()

    data class OnQueryChanged(
        val query: String,
    ) : TicketingEvent()

    data class OnBottomSheetOpened(
        val qrHash: String?,
        val type: TicketingType,
    ) : TicketingEvent()

    data object OnBottomSheetDismissed : TicketingEvent()

    data object OnBackPressed : TicketingEvent()
}
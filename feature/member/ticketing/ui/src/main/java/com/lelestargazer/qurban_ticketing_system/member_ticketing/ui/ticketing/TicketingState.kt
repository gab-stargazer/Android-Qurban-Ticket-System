package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import android.graphics.Bitmap
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon

data class TicketingState(
    val query: String = "",
    val isLoading: Boolean = false,
    val participantAndRecipient: List<MemberAndCoupon> = emptyList(),
    val isBottomSheetOpened: Boolean = false,
    val sheetState: TicketingBottomSheetState? = null,
) {
    data class TicketingBottomSheetState(
        val qrImage: Bitmap,
        val ticketingType: TicketingType,
        val selectedMember: MemberAndCoupon,
    ) {

        enum class TicketingType {
            MANUAL, SCAN
        }
    }
}

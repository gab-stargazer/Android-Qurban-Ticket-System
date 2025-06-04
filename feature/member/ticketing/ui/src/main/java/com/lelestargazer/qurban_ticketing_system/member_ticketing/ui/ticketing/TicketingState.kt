package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import android.graphics.Bitmap
import androidx.paging.PagingData
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.ViewType.ALL_MEMBERS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class TicketingState(
    val memberAndCoupon: Flow<PagingData<MemberAndCoupon>> = flowOf(),
    val query: String = "",
    val displayType: ViewType = ALL_MEMBERS,
    val totalMember: Int = 0,
    val totalUnclaimedCoupon: Int = 0,
    val isLoading: Boolean = false,
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

    enum class ViewType {
        ALL_MEMBERS, AVAILABLE_COUPONS
    }
}

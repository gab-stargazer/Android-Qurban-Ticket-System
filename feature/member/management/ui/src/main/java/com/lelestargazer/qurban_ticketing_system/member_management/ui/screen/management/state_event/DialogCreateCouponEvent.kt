package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event

sealed interface DialogCreateCouponEvent : ManagementEvent {
    data class OnLocationChanged(val location: String) : DialogCreateCouponEvent
    data object OnSelectedPickupDateChanged : DialogCreateCouponEvent
    data object OnCreateCoupon : DialogCreateCouponEvent
}
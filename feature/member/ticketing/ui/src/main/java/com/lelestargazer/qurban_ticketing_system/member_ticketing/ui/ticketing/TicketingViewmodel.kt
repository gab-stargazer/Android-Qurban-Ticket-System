package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestargazer.qurban_ticketing_system.common.UiController
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.CouponRepository
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.MANUAL
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.SCAN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketingViewmodel(
    private val uiController: UiController,
    private val memberRepository: MemberRepository,
    private val couponRepository: CouponRepository,
) : ViewModel() {

    private val _screenState = MutableStateFlow(TicketingState())
    private val _memberAndTicket = memberRepository.getMemberAndTicket()

    val state = combine(
        flow = _screenState,
        flow2 = _memberAndTicket
    ) { currentState, memberAndTicket ->
        if (currentState.query.isNotBlank()) {
            TicketingState(
                query = currentState.query,
                isLoading = currentState.isLoading,
                participantAndRecipient = memberAndTicket.filter { memberAndCoupon ->
                    memberAndCoupon.member.name
                        .lowercase()
                        .contains(
                            currentState.query.lowercase()
                        )
                },
                isBottomSheetOpened = currentState.isBottomSheetOpened,
                sheetState = currentState.sheetState
            )
        } else {
            TicketingState(
                query = currentState.query,
                isLoading = currentState.isLoading,
                participantAndRecipient = memberAndTicket,
                isBottomSheetOpened = currentState.isBottomSheetOpened,
                sheetState = currentState.sheetState
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TicketingState(
            participantAndRecipient = emptyList()
        )
    )

    fun onEvent(event: TicketingEvent) = viewModelScope.launch {
        when (event) {
            TicketingEvent.OnGenerateTicket -> {
                _screenState.update {
                    it.copy(
                        isLoading = true
                    )
                }
                couponRepository.createCoupons()
                _screenState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }

            is TicketingEvent.ClaimTicket -> {
                val currentState = state.value
                val ticket =
                    ((currentState.sheetState as TicketingBottomSheetState).selectedMember.coupon as Coupon).copy(
                        claimStatus = when (currentState.sheetState.ticketingType) {
                            MANUAL -> CouponRedeemStatus.CLAIMED_BY_ADMIN
                            SCAN -> CouponRedeemStatus.CLAIMED_BY_SCAN
                        }
                    )

                couponRepository
                    .claimCoupon(ticket)
                    .onSome { msg ->
                        _screenState.update { state ->
                            state.copy(
                                isBottomSheetOpened = false,
                                sheetState = null
                            )
                        }
                        uiController.snackBarHost.showSnackbar(message = msg)
                    }
                    .onNone {

                    }
            }

            is TicketingEvent.OnQueryChanged -> {
                _screenState.update {
                    it.copy(
                        query = event.query
                    )
                }
            }

            is TicketingEvent.OnBottomSheetOpened -> {
                if (event.type == MANUAL && event.qrHash == null) {
                    //TODO: Toast no ticket and return
                    return@launch
                }

                val _currentState = state.value
                val hashCode = event.qrHash as String
                try {
                    val participantIndex =
                        _currentState
                            .participantAndRecipient
                            .mapNotNull { it.coupon }
                            .indexOfFirst { it.hashCode == hashCode }

                    _screenState.update {
                        it.copy(
                            isBottomSheetOpened = true,
                            sheetState = TicketingBottomSheetState(
                                qrImage = couponRepository.getQrImage(hashCode),
                                ticketingType = event.type,
                                selectedMember = _currentState.participantAndRecipient[participantIndex]
                            )
                        )
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onEvent: ${e.stackTraceToString()}")
                }
            }

            TicketingEvent.OnBottomSheetDismissed -> {
                _screenState.update {
                    it.copy(
                        isBottomSheetOpened = false,
                        sheetState = null
                    )
                }
            }

            TicketingEvent.OnBackPressed -> {
                uiController.navController.popBackStack()
            }
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestargazer.qurban_ticketing_system.common.UiController
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.CouponRepository
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.MANUAL
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.SCAN
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.ViewType.ALL_MEMBERS
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.ViewType.AVAILABLE_COUPONS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TicketingViewmodel(
    private val uiController: UiController,
    private val memberRepository: MemberRepository,
    private val couponRepository: CouponRepository,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<String> =
        MutableStateFlow("")
    private val _displayType: MutableStateFlow<TicketingState.ViewType> =
        MutableStateFlow(ALL_MEMBERS)
    private val _queryAndDisplayType: Flow<Pair<String, TicketingState.ViewType>> =
        combine(
            flow = _searchQuery,
            flow2 = _displayType
        ) { query, type ->
            Pair(query, type)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _memberAndCouponPaging: Flow<PagingData<MemberAndCoupon>> =
        _queryAndDisplayType.flatMapLatest { it ->
            when (it.second) {
                ALL_MEMBERS -> couponRepository.getMembersAndCoupons(
                    it.first
                )

                AVAILABLE_COUPONS -> couponRepository.getMembersAndCouponsAvailable(
                    it.first
                )
            }
        }.cachedIn(viewModelScope)

    private val _totalMember = memberRepository.getActiveMemberCount()
    private val _totalUnclaimedCoupon = couponRepository.getMembersAndCouponsUnclaimedCount()


    private val _screenState = MutableStateFlow(TicketingState())

    val state = combine(
        flow = _screenState,
        flow2 = _searchQuery,
        flow3 = _totalMember,
        flow4 = _totalUnclaimedCoupon,
        flow5 = _displayType
    ) { currentState, searchQuery, totalMember, totalUnclaimedCoupon, displayType ->
        TicketingState(
            query = searchQuery,
            memberAndCoupon = _memberAndCouponPaging,
            displayType = displayType,
            totalMember = totalMember,
            totalUnclaimedCoupon = totalUnclaimedCoupon,
            isLoading = currentState.isLoading,
            isBottomSheetOpened = currentState.isBottomSheetOpened,
            sheetState = currentState.sheetState
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = TicketingState()
    )

    fun onEvent(event: TicketingEvent) = viewModelScope.launch {
        when (event) {
            TicketingEvent.OnGenerateTicket -> {
                _screenState.update {
                    it.copy(
                        isLoading = true
                    )
                }

                couponRepository.createCoupons().fold(
                    ifLeft = {
                        uiController.snackBarHost.showSnackbar(it)
                    }, ifRight = {
                        uiController.snackBarHost.showSnackbar(it)
                    }
                )

                _screenState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }

            is TicketingEvent.OnClaimTicket -> {
                _screenState.update { it.copy(isLoading = true) }
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
                                isLoading = false,
                                isBottomSheetOpened = false,
                                sheetState = null
                            )
                        }
                        uiController.snackBarHost.showSnackbar(message = msg)
                    }
            }

            is TicketingEvent.OnQueryChanged -> {
                _searchQuery.update { event.query }
            }

            is TicketingEvent.OnBottomSheetOpened -> {
                val hashCode = event.qrHash ?: return@launch

                couponRepository
                    .getMembersAndCouponByHash(hashCode)
                    .onLeft {
                        uiController.snackBarHost.showSnackbar(it)
                    }.onRight { memberAndCoupon ->
                        _screenState.update { state ->
                            state.copy(
                                isBottomSheetOpened = true,
                                sheetState = TicketingBottomSheetState(
                                    qrImage = couponRepository.getQrImage(hashCode),
                                    ticketingType = event.type,
                                    selectedMember = memberAndCoupon
                                )
                            )
                        }
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

            is TicketingEvent.OnViewTypeSwitch -> {
                _displayType.update { event.displayType }
            }
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event

import androidx.paging.PagingData
import arrow.optics.optics
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.FilterType
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@optics
data class MemberManagementState(
    val searchQuery: String = "",
    val filterType: FilterType = FilterType.All,
    val isFilterMenuOpened: Boolean = false,
    val isFabMenuExpanded: Boolean = false,
    val isNotificationPermissionDialogOpened: Boolean = false,

    //  Contact By Phone Number
    val isContactByPhoneNumberDialogOpened: Boolean = false,
    val phoneNumber: String? = null,


    val members: Flow<PagingData<Member>> = flowOf(),
    val openedParticipantIndex: Int? = null,
) {

    companion object
}
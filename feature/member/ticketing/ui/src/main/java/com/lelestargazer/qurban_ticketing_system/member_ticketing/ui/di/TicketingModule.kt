package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.di

import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ticketingUiModule = module {
    viewModelOf(::TicketingViewmodel)
}
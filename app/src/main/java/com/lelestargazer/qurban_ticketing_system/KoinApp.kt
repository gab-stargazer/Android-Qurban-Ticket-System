package com.lelestargazer.qurban_ticketing_system

import com.lelestargazer.qurban_ticketing_system.member_management.ui.di.ManagementModule
import com.lelestargazer.qurban_ticketing_system.member_shared.data.di.DbModule
import org.koin.core.annotation.KoinApplication

@KoinApplication(modules = [DbModule::class, ManagementModule::class])
object KoinApp
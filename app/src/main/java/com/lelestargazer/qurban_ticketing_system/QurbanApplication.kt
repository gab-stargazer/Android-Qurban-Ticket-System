package com.lelestargazer.qurban_ticketing_system

import android.app.Application
import com.lelestargazer.qurban_ticketing_system.member_management.ui.di.managementUiModule
import com.lelestargazer.qurban_ticketing_system.member_shared.data.di.memberDataModule
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.di.ticketingUiModule
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.di.dbModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QurbanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QurbanApplication)
            modules(listOf(dbModule, memberDataModule, managementUiModule, ticketingUiModule))
        }
    }
}
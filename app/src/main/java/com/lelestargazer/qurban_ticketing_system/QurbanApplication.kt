package com.lelestargazer.qurban_ticketing_system

import android.app.Application
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.di.dbModule
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.di.participantManagementUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QurbanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@QurbanApplication)
            modules(listOf(dbModule, participantManagementUiModule))
        }
    }
}
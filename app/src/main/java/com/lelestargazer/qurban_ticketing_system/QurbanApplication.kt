package com.lelestargazer.qurban_ticketing_system

import android.app.Application
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.di.ticketingUiModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.logger.Level
import org.koin.ksp.generated.startKoin


class QurbanApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        KoinApp.startKoin {
            androidContext(this@QurbanApplication)
            workManagerFactory()
            modules(listOf(ticketingUiModule))
            printLogger(Level.INFO)
        }
    }
}
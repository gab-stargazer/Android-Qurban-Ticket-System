package com.lelestargazer.qurban_ticketing_system.participant_management_system.data.di

import androidx.room.Room
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.MemberRepositoryImpl
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.ParticipantDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.dao.TicketDao
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.db.RoomDB
import com.lelestargazer.qurban_ticketing_system.participant_management_system.data.qr.QRGenerator
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.MemberRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dbModule = module {
    single<RoomDB>(createdAtStart = true) {
        Room.databaseBuilder(androidContext(), RoomDB::class.java, "qurban-db").build()
    }

    single<ParticipantDao> {
        get<RoomDB>().participantDao()
    }

    single<TicketDao> {
        get<RoomDB>().ticketDao()
    }

    singleOf(::MemberRepositoryImpl) { bind<MemberRepository>() }
    singleOf(::QRGenerator)
}
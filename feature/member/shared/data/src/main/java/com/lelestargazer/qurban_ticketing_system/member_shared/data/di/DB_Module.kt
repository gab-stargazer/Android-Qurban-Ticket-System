package com.lelestargazer.qurban_ticketing_system.member_shared.data.di

import androidx.room.Room
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.ExcelReader
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.db.ApplicationDB
import com.lelestargazer.qurban_ticketing_system.member_shared.data.repository.MemberRepositoryImpl
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.MemberRepository
import com.lelestargazer.qurban_ticketing_system.member_shared.data.addon.QRGenerator
import com.lelestargazer.qurban_ticketing_system.member_shared.data.repository.CouponRepositoryImpl
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.repository.CouponRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val memberDataModule = module {
    single<ApplicationDB>(createdAtStart = true) {
        Room.databaseBuilder(androidContext(), ApplicationDB::class.java, "app-db").build()
    }

    single<MemberDao> {
        get<ApplicationDB>().memberDao()
    }

    single<CouponDao> {
        get<ApplicationDB>().couponDao()
    }

    singleOf(::MemberRepositoryImpl) { bind<MemberRepository>() }
    singleOf(::CouponRepositoryImpl) { bind<CouponRepository>() }
    singleOf(::QRGenerator)
    singleOf(::ExcelReader)
}
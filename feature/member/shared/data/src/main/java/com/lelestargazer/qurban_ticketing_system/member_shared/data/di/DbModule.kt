package com.lelestargazer.qurban_ticketing_system.member_shared.data.di

import android.content.Context
import androidx.room.Room
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.db.ApplicationDB
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.lelestargazer.qurban_ticketing_system.member_shared.data")
class DbModule {

    @Single
    fun provideDB(context: Context): ApplicationDB {
        return Room
            .databaseBuilder(
                context = context,
                klass = ApplicationDB::class.java,
                name = "app.db"
            )
            .build()
    }

    @Single
    fun provideMemberDao(db: ApplicationDB): MemberDao {
        return db.memberDao()
    }

    @Single
    fun provideCouponDao(db: ApplicationDB): CouponDao {
        return db.couponDao()
    }
}
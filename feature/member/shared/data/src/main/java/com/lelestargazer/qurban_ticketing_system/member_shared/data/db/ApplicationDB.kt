package com.lelestargazer.qurban_ticketing_system.member_shared.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lelestargazer.qurban_ticketing_system.member_shared.data.converter.UUIDConverter
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.MemberDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.dao.CouponDao
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.MemberEntity
import com.lelestargazer.qurban_ticketing_system.member_shared.data.entity.CouponEntity

@Database(
    entities = [MemberEntity::class, CouponEntity::class],
    version = 1,
)
@TypeConverters(UUIDConverter::class)
abstract class ApplicationDB : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun couponDao(): CouponDao
}
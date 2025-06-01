package com.lelestargazer.qurban_ticketing_system.member_shared.data.converter

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    fun uuidFromString(string: String?): UUID? {
        return if (string == null) null else UUID.fromString(string)
    }
}
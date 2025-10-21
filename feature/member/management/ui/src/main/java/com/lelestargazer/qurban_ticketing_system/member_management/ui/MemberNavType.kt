package com.lelestargazer.qurban_ticketing_system.member_management.ui

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import kotlinx.serialization.json.Json

val navType = object : NavType<Member?>(
    isNullableAllowed = true
) {
    override fun put(bundle: SavedState, key: String, value: Member?) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(bundle: SavedState, key: String): Member? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Member? {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Member?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

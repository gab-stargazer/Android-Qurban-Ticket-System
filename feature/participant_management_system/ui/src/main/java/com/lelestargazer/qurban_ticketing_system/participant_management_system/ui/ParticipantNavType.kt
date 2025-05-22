package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui

import android.net.Uri
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant
import kotlinx.serialization.json.Json

val navType = object : NavType<Participant?>(
    isNullableAllowed = true
) {
    override fun put(bundle: SavedState, key: String, value: Participant?) {
        bundle.putString(key, Json.encodeToString(value))
    }

    override fun get(bundle: SavedState, key: String): Participant? {
        return Json.decodeFromString(bundle.getString(key) ?: return null)
    }

    override fun parseValue(value: String): Participant? {
        return Json.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Participant?): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

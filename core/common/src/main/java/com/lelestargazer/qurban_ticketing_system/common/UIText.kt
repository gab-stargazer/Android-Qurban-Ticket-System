package com.lelestargazer.qurban_ticketing_system.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UIText {

    data class MessageString(val message: String) : UIText()
    data class ResourceID(
        @get:StringRes val id: Int,
        val args: List<Any>
    ) : UIText()

    fun asText(context: Context): String {
        return when (this) {
            is MessageString -> message
            is ResourceID -> context.getString(id, *args.toTypedArray())
        }
    }

    @Composable
    fun asText(): String {
        return when (this) {
            is MessageString -> message
            is ResourceID -> stringResource(id, *args.toTypedArray())
        }
    }
}
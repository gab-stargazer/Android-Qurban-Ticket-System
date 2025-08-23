package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnDialogDismissed

@Composable
fun ManagementAddEditDialog(
    onEvent: (ManagementEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = {
            onEvent(OnDialogDismissed)
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Halo")
        }
    }
}
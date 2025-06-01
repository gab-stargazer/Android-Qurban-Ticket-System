package com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lelestargazer.qurban_ticketing_system.member_management.ui.import_export.ImportExportEvent.CreateMembersByExcel

@Composable
fun ImportExportScreen(
    onEvent: (ImportExportEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        try {
            onEvent(CreateMembersByExcel(it as Uri))
        } catch (e: Exception) {
            Log.e("IMPORT_EXPORT_SCREEN", "ImportExportScreen: ${e.stackTraceToString()}")
        }
    }

    Column(modifier = modifier) {
        Button(
            onClick = {
                launcher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            }
        ) {
            Text("Buat Member dari data excel")
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.component

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.BottomSheetEvent.OnDismissed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.BottomSheetEvent.OnImportData
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagementBottomSheetMenu(
    state: MemberManagementState.BottomSheetState,
    onEvent: (ManagementEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        try {
            onEvent(OnImportData(it as Uri))
        } catch (e: Exception) {
            Log.e("", "Import Excel: ${e.stackTraceToString()}")
        }
    }

    ModalBottomSheet(
        onDismissRequest = {
            onEvent(OnDismissed)
        }
    ) {
        AnimatedContent(state.isLoading) { isLoading ->
            when (isLoading) {
                true ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            space = screenPadding.vertical,
                            alignment = Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = screenPadding.vertical,
                                horizontal = screenPadding.horizontal
                            )
                    ) {
                        Text(stringResource(R.string.tv_please_wait), textAlign = TextAlign.Center)
                        LinearProgressIndicator()
                    }

                false ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = screenPadding.horizontal,
                                vertical = screenPadding.vertical
                            )
                    ) {
                        Text(
                            text = stringResource(R.string.tv_management_menu),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )

                        Button(
                            onClick = {
onEvent(ManagementEvent.BottomSheetEvent.OnExportData)
                            },
                            shape = RoundedCornerShape(25),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                stringResource(R.string.tv_export_data),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        Button(
                            onClick = {
                                launcher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            },
                            shape = RoundedCornerShape(25),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                stringResource(R.string.tv_import_data),
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBottomSheet() {
    QurbanTicketingSystemTheme {
        Surface {
            ManagementBottomSheetMenu(
                state = MemberManagementState.BottomSheetState(),
                onEvent = {

                }
            )
        }
    }
}
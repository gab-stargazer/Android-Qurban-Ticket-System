package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnStatusChanged
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import com.lelestargazer.qurban_ticketing_system.theme.component.CustomTextField


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QurbanStatusDropdownMenu(
    status: QurbanStatus,
    onQurbanStatusChanged: (AddEditUiEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    var isQurbanStatusExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isQurbanStatusExpanded,
        onExpandedChange = { newState ->
            // STOPSHIP: Investigate whether this is an IDE issue or not
            isQurbanStatusExpanded = newState
        },
        modifier = modifier.padding(horizontal = screenPadding.horizontal)
    ) {
        CustomTextField(
            value =
                stringResource(
                    when (status) {
                        QurbanStatus.Recipient -> R.string.form_qurban_status_recipient
                        QurbanStatus.Participant -> R.string.form_qurban_status_participant
                    }
                ),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.label_qurban_status),
                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isQurbanStatusExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = isQurbanStatusExpanded,
            onDismissRequest = {
                isQurbanStatusExpanded = false
            },
        ) {
            QurbanStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                when (status) {
                                    QurbanStatus.Recipient -> R.string.form_qurban_status_recipient
                                    QurbanStatus.Participant -> R.string.form_qurban_status_participant
                                }
                            ),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    onClick = {
                        onQurbanStatusChanged(OnStatusChanged(status))
                        isQurbanStatusExpanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewQurbanStatusDropdownMenu() {
    QurbanTicketingSystemTheme {
        QurbanStatusDropdownMenu(
            status = QurbanStatus.Recipient,
            onQurbanStatusChanged = {

            },
            focusManager = LocalFocusManager.current,
            modifier = Modifier.padding(
                vertical = LocalScreenPadding.current.vertical
            )
        )
    }
}
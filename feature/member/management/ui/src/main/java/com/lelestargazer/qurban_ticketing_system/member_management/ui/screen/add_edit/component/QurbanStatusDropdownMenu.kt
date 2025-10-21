package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
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
import com.lelestargazer.qurban_ticketing_system.theme.containerColor


@OptIn(ExperimentalMaterial3Api::class)
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
        TextField(
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
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isQurbanStatusExpanded)
            },
            textStyle = MaterialTheme.typography.bodySmall,
            colors = TextFieldDefaults.colors(
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                errorLabelColor = Color.Red.copy(0.75F),
                unfocusedContainerColor = containerColor,
                focusedContainerColor = containerColor,
                errorContainerColor = containerColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            shape = RoundedCornerShape(25F),
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
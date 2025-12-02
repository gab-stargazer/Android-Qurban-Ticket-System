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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnTypeChanged
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanType
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.component.CustomTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QurbanTypeDropdownMenu(
    type: QurbanType,
    onQurbanTypeChanged: (AddEditUiEvent) -> Unit,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    var isQurbanTypeExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = isQurbanTypeExpanded,
        onExpandedChange = { newState ->
            // STOPSHIP: Check this issue
            isQurbanTypeExpanded = newState
        },
        modifier = modifier
            .padding(horizontal = screenPadding.horizontal)
    ) {
        CustomTextField(
            value =
                stringResource(
                    when (type) {
                        QurbanType.Cow -> R.string.form_qurban_type_cow
                        QurbanType.Goat -> R.string.form_qurban_type_goat
                        QurbanType.Sheep -> R.string.form_qurban_type_sheep
                    }
                ),
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    text = stringResource(R.string.label_qurban_type),
                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isQurbanTypeExpanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = isQurbanTypeExpanded,
            onDismissRequest = {
                isQurbanTypeExpanded = false
            }
        ) {
            QurbanType.entries.forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(
                                when (type) {
                                    QurbanType.Cow -> R.string.form_qurban_type_cow
                                    QurbanType.Goat -> R.string.form_qurban_type_goat
                                    QurbanType.Sheep -> R.string.form_qurban_type_sheep
                                }
                            ),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    onClick = {
                        onQurbanTypeChanged(OnTypeChanged(type))
                        isQurbanTypeExpanded = false
                        focusManager.clearFocus()
                    }
                )
            }
        }
    }
}
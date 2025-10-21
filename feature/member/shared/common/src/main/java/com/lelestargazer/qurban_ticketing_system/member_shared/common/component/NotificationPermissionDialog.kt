package com.lelestargazer.qurban_ticketing_system.member_shared.common.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.lelestargazer.qurban_ticketing_system.common.R.string.btn_continue_without_permission
import com.lelestargazer.qurban_ticketing_system.common.R.string.btn_give_permission
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotificationPermissionDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    onDeny: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirmation
            ) {
                Text(
                    text = stringResource(btn_give_permission),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDeny
            ) {
                Text(
                    text = stringResource(btn_continue_without_permission),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.permission_title_notification),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        },
        text = {
            Text(
                text = stringResource(R.string.permission_body_notification),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    textAlign = TextAlign.Justify
                )
            )
        },
        modifier = modifier
    )
}
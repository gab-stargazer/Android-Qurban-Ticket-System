package com.lelestargazer.qurban_ticketing_system.member_shared.common.component

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ContactByPhoneNumberDialog(
    phoneNumber: String,
    onDismiss: () -> Unit,
    onContactByWhatsapp: () -> Unit,
    onContactByPhone: () -> Unit
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(25F),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    horizontal = screenPadding.horizontal,
                    vertical = screenPadding.vertical
                )
            ) {
                Text(
                    text = stringResource(R.string.tv_contact_by_number),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = screenPadding.vertical)
                ) {
                    Button(
                        onClick = {
                            //  TODO: Whatsapp Intent
                            onContactByWhatsapp()
                        },
                        shape = RoundedCornerShape(25F),
                        modifier = Modifier.weight(1F)

                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Whatsapp,
                                contentDescription = null,
                                modifier = Modifier.padding(9.dp)
                            )

                            Text(
                                text = stringResource(R.string.btn_whatsapp),
                                style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Button(
                        shape = RoundedCornerShape(25F),
                        onClick = {
                            if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                                scope.launch {
                                    onContactByPhone()
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = "tel:$phoneNumber".toUri()
                                    }

                                    context.startActivity(intent)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        modifier = Modifier.weight(1F)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                modifier = Modifier.padding(9.dp)
                            )

                            Text(
                                text = stringResource(R.string.btn_phone),
                                style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PreviewContactByPhoneNumberDialog() {
    QurbanTicketingSystemTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(
                horizontal = LocalScreenPadding.current.horizontal,
                vertical = LocalScreenPadding.current.vertical
            )
        ) {
            ContactByPhoneNumberDialog(
                phoneNumber = "",
                onContactByWhatsapp = {},
                onContactByPhone = {},
                onDismiss = {}
            )
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestargazer.qurban_ticketing_system.common.R.string.tv_title_app_name
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.btn_add_participant
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.btn_edit_participant
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnAddEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnAdditionalDescriptionChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnAddressChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnBackPressed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnIsActiveReceiverChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnNameChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnStatusChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.add_edit.AddEditEvent.OnPhoneNumberChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.Type.EDIT
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

@Composable
fun AddEditScreen(
    state: AddEditState,
    onEvent: (AddEditEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = LocalFocusManager.current
    val context = LocalContext.current
    val screenPadding = LocalScreenPadding.current

    Column(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                contentAlignment = Alignment.BottomStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.banner_add_edit),
                    contentDescription = null,
                    alignment = Alignment.Center,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    when (state.screenType) {
                        ADD -> stringResource(R.string.tv_banner_add_participant_recipient)
                        EDIT -> stringResource(R.string.tv_banner_edit_participant_recipient)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(
                        horizontal = screenPadding.horizontal,
                        vertical = 24.dp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = screenPadding.horizontal)
                    .padding(top = screenPadding.vertical)
            ) {
                IconButton(
                    onClick = {
                        onEvent(OnBackPressed)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    stringResource(tv_title_app_name),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(
                    vertical = screenPadding.vertical,
                    horizontal = screenPadding.horizontal
                )
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { newName ->
                        onEvent(OnNameChanged(newName))
                    },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_name),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    isError = state.nameError != null,
                    supportingText = {
                        AnimatedVisibility(state.nameError != null) {
                            state.nameError?.let {
                                Text(
                                    it.asText(context),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                        )
                    },
                    value = state.phoneNumber,
                    onValueChange = { newPhoneNumber ->
                        onEvent(OnPhoneNumberChanged(newPhoneNumber))
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_phone_number),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    isError = state.phoneNumberError != null,
                    supportingText = {
                        AnimatedVisibility(state.phoneNumberError != null) {
                            state.phoneNumberError?.let {
                                Text(
                                    state.phoneNumberError.asText(context),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.home),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    value = state.address,
                    onValueChange = { newAddress ->
                        onEvent(OnAddressChanged(newAddress))
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_address),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    isError = state.addressError != null,
                    supportingText = {
                        AnimatedVisibility(state.addressError != null) {
                            state.addressError?.let {
                                Text(
                                    text = state.addressError.asText(context),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                )

                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                        )
                    },
                    value = state.description,
                    onValueChange = { newDescription ->
                        onEvent(OnAdditionalDescriptionChanged(newDescription))
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_additional_description),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusRequester.clearFocus()
                        }
                    ),
                    shape = RoundedCornerShape(25F),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            Text(
                stringResource(R.string.form_supporting_additional_description),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.tv_qurban_title_participant),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    AnimatedContent(state.isParticipant) {
                        when (it) {
                            true -> Text(
                                text = stringResource(R.string.tv_qurban_active_participant),
                                style = MaterialTheme.typography.labelSmall
                            )

                            false -> Text(
                                text = stringResource(R.string.tv_qurban_inactive_participant),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }

                }

                Spacer(Modifier.weight(1f))

                Switch(
                    checked = state.isParticipant,
                    onCheckedChange = {
                        onEvent(OnStatusChanged(it))
                    },
                    enabled = state.isActive
                )
            }

            if (state.screenType == EDIT) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.tv_qurban_title_recipient),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        AnimatedContent(state.isActive) {
                            when (it) {
                                true -> Text(
                                    text = stringResource(R.string.tv_qurban_active_recipient),
                                    style = MaterialTheme.typography.labelSmall
                                )

                                false -> Text(
                                    text = stringResource(R.string.tv_qurban_inactive_recipient),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }

                    }

                    Spacer(Modifier.weight(1f))

                    Switch(
                        checked = state.isActive,
                        onCheckedChange = {
                            onEvent(OnIsActiveReceiverChanged(it))
                        }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    onEvent(OnAddEdit)
                },
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when (state.screenType) {
                        ADD -> stringResource(btn_add_participant)

                        EDIT -> stringResource(btn_edit_participant)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }


        }
    }
}

@Preview
@Composable
private fun PreviewParticipantAddScreen() {
    QurbanTicketingSystemTheme {
        Surface {
            AddEditScreen(
                state = AddEditState(
                    screenType = EDIT,
                    name = "John",
                    phoneNumber = "+62822523232344",
                    address = "Jl. Asia Afrika",
                    description = "",
                    isActive = true
                ),
                onEvent = {

                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            )
        }
    }
}
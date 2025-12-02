package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.btn_add_member
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.btn_delete_member
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.btn_edit_member
import com.lelestargazer.qurban_ticketing_system.member_management.ui.preview_state.AddEditPreviewState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.EDIT
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component.AddEditHeader
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component.QurbanStatusDropdownMenu
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component.QurbanTypeDropdownMenu
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnAddressChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnDeletePressed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnNameChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiEvent.OnPhoneNumberChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.state_event.AddEditUiState
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import com.lelestargazer.qurban_ticketing_system.theme.component.CustomTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddEditScreen(
    state: AddEditUiState,
    onEvent: (AddEditUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
    ) {
        AddEditHeader(
            screenType = state.screenType,
            onBackPressed = {
                onEvent(AddEditUiEvent.OnBackPressed)
            }
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(top = screenPadding.vertical)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.tv_participant_recipient_information),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(vertical = screenPadding.vertical)
            )

            CustomTextField(
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
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                isError = state.nameError != null,
                supportingText = {
                    AnimatedVisibility(state.nameError != null) {
                        state.nameError?.let { nameError ->
                            Text(
                                text = nameError.asText(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Red
                                )
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenPadding.horizontal)
            )

            val phoneNumberPaddingTop by animateDpAsState(
                when (state.nameError != null) {
                    true -> 9.dp
                    false -> 0.dp
                }
            )

            CustomTextField(
                value = state.phoneNumber,
                onValueChange = { newPhoneNumber ->
                    onEvent(OnPhoneNumberChanged(newPhoneNumber))
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_phone_number),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                isError = state.phoneNumberError != null,
                supportingText = {
                    AnimatedVisibility(state.phoneNumberError != null) {
                        state.phoneNumberError?.let { phoneNumberError ->
                            Text(
                                text = phoneNumberError.asText(),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.Red
                                )
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = phoneNumberPaddingTop)
                    .padding(horizontal = screenPadding.horizontal)
            )

            val addressPaddingTop by animateDpAsState(
                when (state.phoneNumberError != null) {
                    true -> 9.dp
                    false -> 0.dp
                }
            )

            CustomTextField(
                value = state.address,
                onValueChange = { newAddress ->
                    onEvent(OnAddressChanged(newAddress))
                },
                label = {
                    Text(
                        text = stringResource(id = R.string.label_address),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.home),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = addressPaddingTop)
                    .padding(horizontal = screenPadding.horizontal)
            )

            Text(
                text = stringResource(R.string.tv_participant_recipient_status),
                style = MaterialTheme.typography.titleSmallEmphasized.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier
                    .padding(vertical = screenPadding.vertical)
            )

            QurbanStatusDropdownMenu(
                status = state.qurbanStatus,
                onQurbanStatusChanged = onEvent,
                focusManager = focusManager
            )

            AnimatedVisibility(
                state.qurbanStatus == QurbanStatus.Participant,
                modifier = Modifier.padding(top = 9.dp)
            ) {
                QurbanTypeDropdownMenu(
                    type = state.qurbanType,
                    onQurbanTypeChanged = onEvent,
                    focusManager = focusManager
                )
            }

            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .animateContentSize()
            )

            Button(
                onClick = {
                    onEvent(AddEditUiEvent.OnAddEditPressed)
                },
                shape = RoundedCornerShape(25),
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenPadding.horizontal)
                    .padding(
                        top = screenPadding.vertical,
                        bottom = when (state.screenType) {
                            ADD -> screenPadding.vertical
                            EDIT -> 0.dp
                        }
                    )
            ) {
                Text(
                    stringResource(
                        when (state.screenType) {
                            ADD -> btn_add_member
                            EDIT -> btn_edit_member
                        }
                    ),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            if (state.screenType == EDIT) {
                Button(
                    onClick = {
                        onEvent(OnDeletePressed)
                    },
                    shape = RoundedCornerShape(25),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.85F)
                    ),
                    enabled = !state.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenPadding.horizontal)
                        .padding(
                            top = 3.dp,
                            bottom = screenPadding.vertical
                        )
                ) {
                    Text(
                        stringResource(id = btn_delete_member),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewParticipantAddScreen(
    @PreviewParameter(AddEditPreviewState::class) state: AddEditUiState
) {
    QurbanTicketingSystemTheme {
        Surface {
            AddEditScreen(
                state = state,
                onEvent = {

                },
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            )
        }
    }
}
package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestargazer.qurban_ticketing_system.common.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.R
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.route.ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType.EDIT
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnAddEditParticipant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnAdditionalDescriptionChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnAddressChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnHouseholdSizeChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnIsActiveChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnNameChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_add_edit.ParticipantAddEditEvent.OnPhoneNumberChanged
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

@Composable
fun ParticipantAddEditScreen(
    state: ParticipantAddEditState,
    onEvent: (ParticipantAddEditEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = LocalFocusManager.current
    val context = LocalContext.current
    val screenPadding = LocalScreenPadding.current

    Column(
        modifier = modifier
            .padding(
                vertical = screenPadding.vertical,
                horizontal = screenPadding.horizontal
            )
    ) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { newName ->
                onEvent(OnNameChanged(newName))
            },
            singleLine = true,
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.form_name))
            },
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
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            singleLine = true,
            leadingIcon = {
                Icon(Icons.Default.Phone, null)
            },
            value = state.phoneNumber,
            onValueChange = { newPhoneNumber ->
                onEvent(OnPhoneNumberChanged(newPhoneNumber))
            },
            label = {
                Text(stringResource(R.string.form_phone_number))
            },
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
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            leadingIcon = {
                Icon(Icons.Default.House, null)
            },
            value = state.address,
            onValueChange = { newAddress ->
                onEvent(OnAddressChanged(newAddress))
            },
            label = {
                Text(stringResource(R.string.form_address))
            },
            isError = state.addressError != null,
            supportingText = {
                AnimatedVisibility(state.addressError != null) {
                    state.addressError?.let {
                        Text(
                            state.addressError.asText(context),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            value =
                if (state.householdSize != null) state.householdSize.toString()
                else "",
            onValueChange = { newHouseholdSize ->
                onEvent(OnHouseholdSizeChanged(newHouseholdSize.toIntOrNull()))
            },
            singleLine = true,
            label = {
                Text(stringResource(R.string.form_household_size))
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Groups,
                    null
                )
            },
            isError = state.householdSizeError != null,
            supportingText = {
                AnimatedVisibility(state.householdSizeError != null) {
                    state.householdSizeError?.let {
                        Text(
                            state.householdSizeError.asText(context),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red)
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        OutlinedTextField(
            leadingIcon = {
                Icon(
                    Icons.Default.Info,
                    null
                )
            },
            value = state.description,
            onValueChange = { newDescription ->
                onEvent(OnAdditionalDescriptionChanged(newDescription))
            },
            label = {
                Text(stringResource(R.string.form_additional_description))
            },
            supportingText = {
                Text(
                    stringResource(R.string.form_supporting_additional_description),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusRequester.clearFocus()
                }
            ),
            modifier = Modifier
                .fillMaxWidth()
        )

        if (state.screenType == EDIT) {
            LaunchedEffect(Unit) {
                Log.d(TAG, "ParticipantAddEditScreen: $state")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column {
                Text(
                    text = stringResource(R.string.tv_participant_status),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Switch(
                        checked = state.isActive,
                        onCheckedChange = {
                            onEvent(OnIsActiveChanged(it))
                        }
                    )

                    Text(
                        text =
                            if (state.isActive) {
                                stringResource(R.string.tv_qurban_active_participant)
                            } else {
                                stringResource(R.string.tv_qurban_inactive_participant)
                            },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(onClick = {
            onEvent(OnAddEditParticipant)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                when (state.screenType) {
                    ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType.ADD -> stringResource(
                        R.string.btn_add_participant
                    )

                    EDIT -> stringResource(
                        R.string.btn_edit_participant
                    )
                }
            )
        }


    }
}

@Preview
@Composable
private fun PreviewParticipantAddScreen() {
    QurbanTicketingSystemTheme {
        Surface {
            ParticipantAddEditScreen(
                state = ParticipantAddEditState(
                    screenType = ManagementRoute.ParticipantAddOrEdit.ParticipantAddOrEditType.ADD,
                    name = "John",
                    phoneNumber = "+62822523232344",
                    address = "Jl. Asia Afrika",
                    householdSize = 3,
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
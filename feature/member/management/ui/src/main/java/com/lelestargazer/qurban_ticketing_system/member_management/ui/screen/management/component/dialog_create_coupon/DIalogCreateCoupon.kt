package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.dialog_create_coupon

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lelestargazer.qurban_ticketing_system.common.toFormattedDate
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.dialog_create_coupon_label_location
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.dialog_create_coupon_no_pickup_date
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.dialog_create_coupon_pickup_date
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.dialog_create_coupon_title
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.DialogCreateCouponEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.DialogCreateCouponEvent.OnCreateCoupon
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.DialogCreateCouponState
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.btn_cancel
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.btn_create_coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.btn_select
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.component.CustomTextField
import com.lelestargazer.qurban_ticketing_system.theme.component.FutureSelectableDate

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DialogCreateCoupon(
    state: DialogCreateCouponState,
    onEvent: (DialogCreateCouponEvent) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {

        LaunchedEffect(state.datePickerState.selectedDateMillis) {
            onEvent(DialogCreateCouponEvent.OnSelectedPickupDateChanged)
        }

        val focusManager = LocalFocusManager.current
        val keyboardManager = LocalSoftwareKeyboardController.current

        Box(
            modifier = Modifier.padding(horizontal = LocalScreenPadding.current.horizontal)
        ) {
            ElevatedCard(
                shape = RoundedCornerShape(25F),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier =
                        Modifier
                            .padding(
                                horizontal = LocalScreenPadding.current.horizontal,
                                vertical = LocalScreenPadding.current.vertical
                            )
                ) {
                    Text(
                        stringResource(id = dialog_create_coupon_title),
                        style = MaterialTheme.typography.titleMediumEmphasized.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    CustomTextField(
                        value = state.location,
                        onValueChange = { newLocation ->
                            onEvent(DialogCreateCouponEvent.OnLocationChanged(newLocation))
                        },
                        label = {
                            Text(
                                text = stringResource(dialog_create_coupon_label_location),
                                style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null
                            )
                        },
                        isError = state.locationError != null,
                        supportingText = {
                            AnimatedVisibility(
                                visible = state.locationError != null,
                                enter = expandVertically() + fadeIn()
                            ) {
                                state.locationError?.let { locationError ->
                                    Text(
                                        stringResource(id = locationError),
                                        style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardManager?.hide()
                                focusManager.clearFocus(true)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = LocalScreenPadding.current.vertical)
                    )

                    var shouldShownDatePicker by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CustomTextField(
                            value = state.datePickerState.selectedDateMillis?.toFormattedDate()
                                ?: stringResource(dialog_create_coupon_no_pickup_date),
                            onValueChange = {},
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        shouldShownDatePicker = !shouldShownDatePicker
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = stringResource(dialog_create_coupon_pickup_date),
                                    style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            readOnly = true,
                            isError = state.datePickerStateError != null,
                            supportingText = {
                                Column {
                                    AnimatedVisibility(
                                        visible = state.datePickerStateError != null,
                                        enter = expandVertically() + fadeIn()
                                    ) {
                                        state.datePickerStateError?.let { datePickerStateError ->
                                            Text(
                                                stringResource(id = datePickerStateError),
                                                style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                                    color = MaterialTheme.colorScheme.error
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        )

                        if (shouldShownDatePicker) {
                            val newDatePickerState = rememberDatePickerState(
                                initialDisplayMode = DisplayMode.Picker,
                                selectableDates = FutureSelectableDate
                            )

                            DatePickerDialog(
                                onDismissRequest = {
                                    shouldShownDatePicker = false
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            shouldShownDatePicker = false
                                            state.datePickerState.selectedDateMillis =
                                                newDatePickerState.selectedDateMillis
                                        }
                                    ) {
                                        Text(stringResource(btn_select))
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = {
                                            shouldShownDatePicker = false
                                        }
                                    ) {
                                        Text(stringResource(btn_cancel))
                                    }
                                }
                            ) {
                                DatePicker(state = newDatePickerState)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            onEvent(OnCreateCoupon)
                        },
                        shape = RoundedCornerShape(25F),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = LocalScreenPadding.current.vertical)
                    ) {
                        Text(
                            text = stringResource(id = btn_create_coupon),
                            style = MaterialTheme.typography.labelMediumEmphasized.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
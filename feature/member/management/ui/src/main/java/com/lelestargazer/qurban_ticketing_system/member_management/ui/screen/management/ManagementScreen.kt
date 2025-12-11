package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.lelestargazer.qurban_ticketing_system.common.R.drawable.ic_import
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.form_search_name
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.FilterType
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.dialog_create_coupon.DialogCreateCoupon
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.member_item.MemberItem
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnBackPressed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnContactByPhoneNumberDialogStateChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnFilterMenuStateChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnImportData
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnImportMemberClicked
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnMemberClicked
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnNavigateToEditClicked
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnPermissionDialogDismissed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.ManagementEvent.OnSearchQueryChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.btn_create_coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.common.component.ContactByPhoneNumberDialog
import com.lelestargazer.qurban_ticketing_system.member_shared.common.component.NotificationPermissionDialog
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import com.lelestargazer.qurban_ticketing_system.theme.component.CustomTextField
import com.lelestargazer.qurban_ticketing_system.theme.component.ManagementTicketingBanner


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun ManagementScreen(
    state: MemberManagementState,
    onEvent: (ManagementEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    val screenPadding = LocalScreenPadding.current
    val keyboardManager = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val members = state.members.collectAsLazyPagingItems()

    val excelSelectionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) {
        it?.let { uri ->
            onEvent(OnImportData(uri))
        }
    }

    val notificationPermission =
        rememberPermissionState(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionResult = { isGranted ->
                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            }
        )

    if (state.isNotificationPermissionDialogOpened) {
        NotificationPermissionDialog(
            onDismiss = {
                onEvent(OnPermissionDialogDismissed)
            },
            onConfirmation = {
                onEvent(OnPermissionDialogDismissed)
                notificationPermission.launchPermissionRequest()
            },
            onDeny = {
                onEvent(OnPermissionDialogDismissed)
                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            }
        )
    }

    if (state.isContactByPhoneNumberDialogOpened) {
        ContactByPhoneNumberDialog(
            phoneNumber = state.phoneNumber.orEmpty(),
            onDismiss = {
                onEvent(
                    OnContactByPhoneNumberDialogStateChanged(
                        newState = false,
                        phoneNumber = null
                    )
                )
            },
            onContactByPhone = {
                onEvent(
                    OnContactByPhoneNumberDialogStateChanged(
                        newState = false,
                        phoneNumber = null
                    )
                )
            },
            onContactByWhatsapp = {
                onEvent(
                    OnContactByPhoneNumberDialogStateChanged(
                        newState = false,
                        phoneNumber = null
                    )
                )
            }
        )
    }

    if (state.isDialogCreateCouponShowed) {
        DialogCreateCoupon(
            state = state.dialogCreateCouponState,
            onEvent = onEvent,
            onDismiss = {
                onEvent(ManagementEvent.OnCreateCouponDialogShowed(false))
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButtonMenu(
                expanded = state.isFabMenuExpanded,
                button = {
                    ToggleFloatingActionButton(
                        checked = state.isFabMenuExpanded,
                        onCheckedChange = { isFabMenuExpanded ->
                            onEvent(ManagementEvent.OnFabMenuStateChanged(isFabMenuExpanded))
                        },
                    ) {
                        AnimatedContent(state.isFabMenuExpanded) { isExpanded ->
                            when (isExpanded) {
                                true -> {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }

                                false -> {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            ) {
                FloatingActionButtonMenuItem(
                    onClick = {
                        if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                            //  Check for notification only
                            if (Build.VERSION.SDK_INT >= 33 && notificationPermission.status.isGranted) {
                                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            } else if (Build.VERSION.SDK_INT >= 33) {
                                onEvent(OnImportMemberClicked)
                            } else {
                                excelSelectionLauncher.launch(arrayOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(ic_import),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.btn_import_data),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                FloatingActionButtonMenuItem(
                    onClick = {
                        onEvent(ManagementEvent.OnNavigateToAddClicked)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(R.string.btn_management_add_member),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                FloatingActionButtonMenuItem(
                    onClick = {
                        onEvent(ManagementEvent.OnCreateCouponDialogShowed(true))
                    },
                    icon = {

                    },
                    text = {
                        Text(
                            text = stringResource(btn_create_coupon),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
        ) {

            ManagementTicketingBanner(
                title = stringResource(id = R.string.tv_management_banner_title),
                isMainMenu = false,
                onBackPressed = {
                    onEvent(OnBackPressed)
                }
            )

            CustomTextField(
                value = state.searchQuery,
                onValueChange = { newQuery ->
                    onEvent(OnSearchQueryChanged(newQuery))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onEvent(OnFilterMenuStateChanged(true))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = state.isFilterMenuOpened,
                        onDismissRequest = {
                            onEvent(OnFilterMenuStateChanged(false))
                        }
                    ) {
                        FilterType.entries.forEach { filterType ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(filterType.uiText),
                                        style = MaterialTheme.typography.bodySmallEmphasized.copy(
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                },
                                onClick = {
                                    onEvent(ManagementEvent.OnFilterTypeChanged(filterType))
                                    onEvent(OnFilterMenuStateChanged(false))
                                }
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = stringResource(form_search_name),
                        style = MaterialTheme.typography.labelMediumEmphasized.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                singleLine = true,
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
                    .padding(
                        horizontal = screenPadding.horizontal,
                        vertical = 9.dp
                    )
            )

            AnimatedContent(members.loadState.refresh is LoadState.Loading) { isLoading ->
                when (isLoading) {
                    true -> Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LoadingIndicator()
                    }

                    false -> {
                        AnimatedContent(members.loadState.refresh is LoadState.NotLoading && members.itemSnapshotList.isEmpty()) { isDataEmpty ->
                            when (isDataEmpty) {
                                true -> Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Text(
                                        stringResource(R.string.tv_no_participant_data),
                                        style = MaterialTheme.typography.titleSmallEmphasized.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }

                                false -> {
                                    LazyColumn(
                                        contentPadding = PaddingValues(
                                            start = screenPadding.horizontal,
                                            end = screenPadding.horizontal,
                                            bottom = screenPadding.vertical
                                        ),
                                        verticalArrangement = Arrangement.spacedBy(9.dp)
                                    ) {
                                        items(count = members.itemCount) { index ->
                                            members[index]?.let { member ->
                                                val isMemberSelected =
                                                    index == state.openedParticipantIndex
                                                MemberItem(
                                                    member = member,
                                                    isParticipantSelected = isMemberSelected,
                                                    onClick = {
                                                        onEvent(OnMemberClicked(index))
                                                        keyboardManager?.hide()
                                                        focusManager.clearFocus()
                                                    },
                                                    onContactByPhoneNumberClicked = {
                                                        onEvent(
                                                            OnContactByPhoneNumberDialogStateChanged(
                                                                newState = true,
                                                                phoneNumber = member.phoneNumber
                                                            )
                                                        )
                                                    },
                                                    onNavigateToEdit = {
                                                        onEvent(OnNavigateToEditClicked(member))
                                                    },
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .animateItem()
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewParticipantManagementSystem() {
    QurbanTicketingSystemTheme {
        ManagementScreen(
            state = MemberManagementState(
                openedParticipantIndex = 1
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
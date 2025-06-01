package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.component.ManagementItem
import com.lelestargazer.qurban_ticketing_system.member_shared.common.component.ManagementTicketingBanner
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnBackPressed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnNavigateToAdd
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnNavigateToEdit
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnPressed
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.ManagementEvent.OnQueryChanged
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState.OpenedParticipantType.ACTIVE
import com.lelestargazer.qurban_ticketing_system.member_management.ui.management.state_event.MemberManagementState.OpenedParticipantType.INACTIVE
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

/**
 *  This screen is for managing user only, not for ticketing purpose
 */
@Composable
fun ManagementScreen(
    state: MemberManagementState,
    onEvent: (ManagementEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val input = LocalSoftwareKeyboardController.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(OnNavigateToAdd)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
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
                onBack = {
                    onEvent(OnBackPressed)
                }
            )

            TextField(
                value = state.query,
                onValueChange = { newQuery ->
                    onEvent(OnQueryChanged(newQuery))
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        null
                    )
                },
                label = {
                    Text(
                        stringResource(R.string.form_search_name),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color(0xFFE6E0E9),
                    focusedLabelColor = Color.Black
                ),
                singleLine = true,
                shape = RoundedCornerShape(25F),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        input?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = screenPadding.vertical)
                    .padding(horizontal = screenPadding.horizontal)
                    .border(1.dp, Color.Black, RoundedCornerShape(25F))
            )

            AnimatedContent(
                state.activeParticipantRecipient.isNotEmpty() ||
                        state.inactiveParticipantRecipient.isNotEmpty()
            ) { isExist ->
                when (isExist) {
                    true -> {
                        LazyColumn(
                            state = rememberLazyListState(),
                            contentPadding = PaddingValues(bottom = 48.dp, top = 0.dp),
                        ) {
                            item {
                                if (state.activeParticipantRecipient.isNotEmpty()) {
                                    Text(
                                        text = stringResource(id = R.string.tv_qurban_title_active_participant_recipient),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier
                                            .padding(horizontal = screenPadding.horizontal)
                                            .animateItem()
                                    )
                                }
                            }

                            itemsIndexed(
                                items = state.activeParticipantRecipient,
                                contentType = { index: Int, member: Member -> member },
                                key = { index: Int, member: Member -> member.id }
                            ) { index: Int, member: Member ->
                                val isParticipantSelected: Boolean =
                                    state.openedParticipantType == ACTIVE &&
                                            state.openedParticipantIndex == index

                                ManagementItem(
                                    member = member,
                                    isParticipantSelected = isParticipantSelected,
                                    onClick = {
                                        if (isParticipantSelected) {
                                            onEvent(
                                                OnPressed(
                                                    type = null,
                                                    index = null
                                                )
                                            )
                                        } else {
                                            onEvent(
                                                OnPressed(
                                                    type = ACTIVE,
                                                    index = index
                                                )
                                            )
                                        }
                                    },
                                    onNavigateToEdit = {
                                        onEvent(
                                            OnNavigateToEdit(member)
                                        )
                                    }
                                )
                            }

                            item {
                                if (state.inactiveParticipantRecipient.isNotEmpty()) {
                                    Text(
                                        text = stringResource(R.string.tv_qurban_title_inactive_participant_recipient),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        modifier = Modifier
                                            .padding(horizontal = screenPadding.horizontal)
                                            .animateItem()
                                    )
                                }
                            }

                            itemsIndexed(
                                state.inactiveParticipantRecipient,
                                contentType = { index: Int, item: Member ->
                                    item
                                },
                                key = { index: Int, item: Member ->
                                    item.id
                                }
                            ) { index, member ->
                                val isParticipantSelected: Boolean =
                                    state.openedParticipantType == INACTIVE &&
                                            state.openedParticipantIndex == index

                                ManagementItem(
                                    member = member,
                                    isParticipantSelected = isParticipantSelected,
                                    onClick = {
                                        if (isParticipantSelected) {
                                            onEvent(
                                                OnPressed(
                                                    type = null,
                                                    index = null
                                                )
                                            )
                                        } else {
                                            onEvent(
                                                OnPressed(
                                                    type = INACTIVE,
                                                    index = index
                                                )
                                            )
                                        }
                                    },
                                    onNavigateToEdit = {
                                        onEvent(
                                            OnNavigateToEdit(member)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    false -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.tv_no_participant_data),
                                style = MaterialTheme.typography.titleMedium.copy(
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

@Preview
@Composable
private fun PreviewParticipantManagementSystem() {
    QurbanTicketingSystemTheme {
        ManagementScreen(
            state = MemberManagementState(
                openedParticipantType = ACTIVE,
                openedParticipantIndex = 1
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
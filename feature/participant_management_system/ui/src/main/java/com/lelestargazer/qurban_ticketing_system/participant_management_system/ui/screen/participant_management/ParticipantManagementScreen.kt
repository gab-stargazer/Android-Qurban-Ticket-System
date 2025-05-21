package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestargazer.qurban_ticketing_system.common.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.R
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.component.ParticipantItem
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementEvent.OnNavigateToAddParticipant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementEvent.OnNavigateToEditParticipant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementEvent.OnParticipantPressed
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementEvent.OnQueryChanged
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementState.OpenedParticipantType.ACTIVE
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen.participant_management.ParticipantManagementState.OpenedParticipantType.INACTIVE
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

/**
 *  This screen is for managing user only, not for ticketing purpose
 */
@Composable
fun ParticipantManagementScreen(
    state: ParticipantManagementState,
    onEvent: (ParticipantManagementEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(OnNavigateToAddParticipant)
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
                    Text(stringResource(R.string.form_search_name))
                },
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(15F),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = screenPadding.vertical)
                    .padding(horizontal = screenPadding.horizontal)
            )

            if (state.activeParticipant.isEmpty() && state.inactiveParticipant.isEmpty()) {
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
            } else {
                LazyColumn(
                    state = rememberLazyListState(),
                    contentPadding = PaddingValues(bottom = 48.dp, top = 0.dp),
                ) {
                    item {
                        Text(
                            text = stringResource(id = R.string.tv_qurban_active_participant),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .padding(horizontal = screenPadding.horizontal)
                                .animateItem()
                        )
                    }

                    /**
                     * This is active section of the participant
                     */
                    itemsIndexed(
                        items = state.activeParticipant,
                        contentType = { index: Int, participant: Participant -> participant },
                        key = { index: Int, participant: Participant -> participant.id }
                    ) { index: Int, participant: Participant ->
                        val isParticipantSelected: Boolean =
                            state.openedParticipantType == ACTIVE &&
                                    state.openedParticipantIndex == index

                        ParticipantItem(
                            participant = participant,
                            isParticipantSelected = isParticipantSelected,
                            onClick = {
                                if (isParticipantSelected) {
                                    onEvent(
                                        OnParticipantPressed(
                                            type = null,
                                            index = null
                                        )
                                    )
                                } else {
                                    onEvent(
                                        OnParticipantPressed(
                                            type = ACTIVE,
                                            index = index
                                        )
                                    )
                                }
                            },
                            onNavigateToEdit = {
                                onEvent(
                                    OnNavigateToEditParticipant(participant)
                                )
                            }
                        )
                    }

                    item {
                        Text(
                            text = stringResource(R.string.tv_qurban_inactive_participant),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier
                                .padding(horizontal = screenPadding.horizontal)
                                .animateItem()
                        )
                    }

                    itemsIndexed(
                        state.inactiveParticipant,
                        contentType = { index: Int, item: Participant ->
                            item
                        },
                        key = { index: Int, item: Participant ->
                            item.id
                        }
                    ) { index, participant ->
                        val isParticipantSelected: Boolean =
                            state.openedParticipantType == INACTIVE &&
                                    state.openedParticipantIndex == index

                        ParticipantItem(
                            participant = participant,
                            isParticipantSelected = isParticipantSelected,
                            onClick = {
                                if (isParticipantSelected) {
                                    onEvent(
                                        OnParticipantPressed(
                                            type = null,
                                            index = null
                                        )
                                    )
                                } else {
                                    onEvent(
                                        OnParticipantPressed(
                                            type = INACTIVE,
                                            index = index
                                        )
                                    )
                                }
                            },
                            onNavigateToEdit = {
                                onEvent(
                                    OnNavigateToEditParticipant(participant)
                                )
                            }
                        )
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
        ParticipantManagementScreen(
            state = ParticipantManagementState(
                openedParticipantType = ACTIVE,
                openedParticipantIndex = 1
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
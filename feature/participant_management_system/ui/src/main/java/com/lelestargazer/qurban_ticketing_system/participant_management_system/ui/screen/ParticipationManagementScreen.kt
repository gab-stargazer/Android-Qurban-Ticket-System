package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.R

data class ParticipantManagementSystemState(
    val participants: List<Participant> = generateParticipantList()
)



@Composable
private fun ParticipantManagementSystem(
    state: ParticipantManagementSystemState, modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //  Trigger Navigation to Add Participant
                }) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }) { innerPadding ->
        // LIst Participant
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = 16.dp, top = 16.dp
            ), verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(count = state.participants.size) { index: Int ->
                val participant = state.participants[index]
                var isExpanded by remember {
                    mutableStateOf(false)
                }

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isExpanded = !isExpanded
                        }
                        .padding(horizontal = 4.dp)) {
                    Column(
                        modifier = Modifier
                            .padding(
                                start = 8.dp, end = 16.dp
                            )
                            .padding(bottom = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(end = 4.dp)
                            ) {
                                Text(stringResource(R.string.name, participant.name))
                                Text(stringResource(R.string.address, participant.address))
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check, contentDescription = null
                                )

                                Text(
                                    "Sudah\nKlaim", style = MaterialTheme.typography.bodySmall.copy(

                                    )
                                )
                            }
                        }



                        AnimatedVisibility(
                            isExpanded, enter = expandVertically(), exit = shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier.height(4.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 64.dp)
                                        .height(1.dp)
                                        .background(Color.Black)
                                )

                                Spacer(modifier.height(4.dp))

                                Text(
                                    stringResource(
                                        R.string.household_size,
                                        participant.householdSize.toString()
                                    )
                                )

                                participant.description?.let { _ ->
                                    Text(
                                        stringResource(
                                            R.string.additional_description, participant.description
                                        )
                                    )
                                }

                                Button(
                                    onClick = {
                                        // Trigger Buka Whatsapp
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 12.dp, vertical = 0.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Whatsapp,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "Hubungi Via Whatsapp",
                                        style = MaterialTheme.typography.bodyMedium.copy()
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

@Preview
@Composable
private fun PreviewParticipantManagementSystem() {
    MaterialTheme {
        Surface {
            ParticipantManagementSystem(
                state = ParticipantManagementSystemState(), modifier = Modifier.fillMaxSize()
            )
        }
    }
}
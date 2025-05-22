package com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.lelestargazer.qurban_ticketing_system.common.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.participant_management_system.domain.model.Participant
import com.lelestargazer.qurban_ticketing_system.participant_management_system.ui.R

@Composable
fun LazyItemScope.ParticipantItem(
    participant: Participant,
    isParticipantSelected: Boolean,
    onClick: () -> Unit,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    Column(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .animateItem()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = screenPadding.vertical)
            ) {
                Icon(
                    imageVector =
                        when (participant.isActive) {
                            true -> Icons.Default.Person
                            false -> Icons.Default.PersonOff
                        },
                    contentDescription = null,
                )
                Text(
                    when(participant.isActive) {
                        true -> stringResource(R.string.tv_active_participant)
                        false -> stringResource(R.string.tv_nonactive_participant)
                    },
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier
                    .weight(4f)
                    .padding(end = screenPadding.horizontal)
            ) {

                Text(stringResource(R.string.tv_name, participant.name))
                Text(
                    stringResource(
                        R.string.tv_phone_number,
                        participant.phoneNumber
                    )
                )
            }
        }


        AnimatedVisibility(isParticipantSelected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(Color.LightGray)
            ) {
                Column(
                    modifier = Modifier
                        .weight(5F)
                        .fillMaxHeight()
                        .padding(
                            vertical = screenPadding.vertical,
                            horizontal = screenPadding.horizontal
                        )
                ) {
                    Text(
                        stringResource(
                            R.string.tv_address,
                            participant.address
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        stringResource(
                            R.string.tv_household_size,
                            participant.householdSize.toString()
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (participant.description.isNotBlank()) {
                        Text(
                            stringResource(
                                R.string.tv_additional_description,
                                participant.description
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                        .background(Color.Blue)
                        .clickable(onClick = onNavigateToEdit)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
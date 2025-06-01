package com.lelestargazer.qurban_ticketing_system.member_management.ui.management.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
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
import androidx.compose.ui.unit.dp
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_additional_description
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_management_address
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_household_size
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_qurban_active_participant_short
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_qurban_active_recipient_short
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R.string.tv_qurban_inactive_recipient_short
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_participant
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.msg_member_recipient
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding

@Composable
fun LazyItemScope.ManagementItem(
    member: Member,
    isParticipantSelected: Boolean,
    onClick: () -> Unit,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val backgroundColor = animateColorAsState(
        when (isParticipantSelected) {
            true -> Color(0xFFE6E0E9)
            false -> MaterialTheme.colorScheme.surface
        }
    )

    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .animateItem()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor.value)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(2f)
                    .padding(
                        vertical = screenPadding.vertical,
                        horizontal = screenPadding.horizontal
                    )
            ) {
                Icon(
                    imageVector =
                        when (member.isActive) {
                            true -> Icons.Default.Person
                            false -> Icons.Default.PersonOff
                        },
                    contentDescription = null,
                )

                when (member.isParticipant) {
                    true -> Text(
                        text = stringResource(tv_qurban_active_participant_short),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )

                    false -> Text(
                        when (member.isActive) {
                            true -> stringResource(tv_qurban_active_recipient_short)
                            false -> stringResource(tv_qurban_inactive_recipient_short)
                        },
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(3f)
                    .padding(horizontal = 4.dp)
            ) {

                Text(
                    text = stringResource(R.string.tv_management_name, member.name),
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(
                        R.string.tv_management_status,
                        stringResource(
                            if (member.isParticipant) {
                                msg_member_participant
                            } else {
                                msg_member_recipient
                            }
                        )
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1f)
            ) {
                AnimatedContent(targetState = isParticipantSelected) {
                    when (it) {
                        true -> Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = null
                        )

                        false -> Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            }
        }


        AnimatedVisibility(isParticipantSelected) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(Color(0xFFCCCCCC))
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .weight(5F)
                        .fillMaxHeight()
                        .padding(
                            vertical = screenPadding.vertical,
                            horizontal = screenPadding.horizontal
                        )
                ) {
                    Text(
                        text = stringResource(
                            tv_management_address,
                            member.address
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        stringResource(
                            tv_household_size,
                            ""
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (member.description.isNotBlank()) {
                        Text(
                            text = stringResource(
                                tv_additional_description,
                                member.description
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1F)
                        .fillMaxHeight()
                        .background(Color(0xFF84AE92))
                        .clickable(onClick = onNavigateToEdit)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}
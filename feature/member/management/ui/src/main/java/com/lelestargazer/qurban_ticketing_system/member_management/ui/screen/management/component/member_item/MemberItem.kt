package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.management.component.member_item

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Member
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import com.lelestargazer.qurban_ticketing_system.theme.containerColor

@Composable
fun MemberItem(
    member: Member,
    isParticipantSelected: Boolean,
    onClick: () -> Unit,
    onContactByPhoneNumberClicked: () -> Unit,
    onNavigateToEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()

    ElevatedCard(
        shape = RoundedCornerShape(25F),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor
        ),
        content = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(
                        horizontal = screenPadding.horizontal,
                        vertical = screenPadding.vertical
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )

                    Column {
                        Text(
                            text = member.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = stringResource(
                                when (member.status) {
                                    QurbanStatus.Recipient -> R.string.form_qurban_status_recipient
                                    QurbanStatus.Participant -> R.string.form_qurban_status_participant
                                }
                            ),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.weight(1F))

                    AnimatedContent(member.phoneNumber != null || member.address != null) { isMemberDataExist ->
                        when (isMemberDataExist) {
                            true -> {
                                IconButton(
                                    onClick = onClick
                                ) {
                                    AnimatedContent(isParticipantSelected) { isSelected ->
                                        when (isSelected) {
                                            true -> {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropUp,
                                                    contentDescription = null
                                                )
                                            }

                                            false -> {
                                                Icon(
                                                    imageVector = Icons.Default.ArrowDropDown,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            false -> {
                                IconButton(
                                    onClick = {
                                        if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                                            onNavigateToEdit()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = isParticipantSelected && (member.phoneNumber != null || member.address != null),
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(
                                horizontal = screenPadding.horizontal
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = screenPadding.horizontal,
                                    vertical = screenPadding.vertical
                                )
                        ) {
                            Column {
                                member.phoneNumber?.let { phoneNumber ->
                                    Text(
                                        text = stringResource(
                                            R.string.tv_management_phone_number,
                                            phoneNumber
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                member.address?.let { address ->
                                    Text(
                                        text = stringResource(
                                            R.string.tv_management_address,
                                            address
                                        ),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1F))

                            member.phoneNumber?.let {
                                IconButton(
                                    onClick = {
                                        onContactByPhoneNumberClicked()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = null
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                                        onNavigateToEdit()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewMemberItem(
    @PreviewParameter(MemberItemPreview::class) member: Member
) {
    QurbanTicketingSystemTheme {
        MemberItem(
            member = member,
            isParticipantSelected = member.phoneNumber != null || member.address != null,
            onClick = {},
            onContactByPhoneNumberClicked = {},
            onNavigateToEdit = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = LocalScreenPadding.current.horizontal,
                    vertical = LocalScreenPadding.current.vertical
                )
        )
    }
}
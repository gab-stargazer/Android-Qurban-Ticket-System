package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_form_value
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_description
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.MemberAndCoupon
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.R
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding

@Composable
fun LazyItemScope.TicketingItem(
    index: Int,
    memberAndCoupon: MemberAndCoupon,
    onClicked: (MemberAndCoupon) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val member = memberAndCoupon.member
    val coupon = memberAndCoupon.coupon
    val isEven = index % 2 == 0

    Column(
        modifier = modifier
            .animateItem()
            .clickable {
                onClicked(memberAndCoupon)
            }
    ) {
        HorizontalDivider()
        Row(
            Modifier
                .height(IntrinsicSize.Min)
                .background(
                    if (isEven) {
                        MaterialTheme.colorScheme.surfaceContainerLowest
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(6F)
                    .padding(start = screenPadding.horizontal)
                    .padding(vertical = screenPadding.vertical)
            ) {
                Row {
                    Text(
                        text = stringResource(com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_name),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.weight(2F)
                    )
                    Text(
                        text = stringResource(tv_form_value, member.name),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(6F)
                    )
                }

                Row {
                    Text(
                        text = stringResource(com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_address),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.weight(2F)
                    )
                    Text(
                        text = stringResource(
                            tv_form_value,
                            member.address.ifBlank { "RT${member.rt} / RW${member.rw}" }
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(6F)
                    )
                }

                if (member.description.isNotBlank()) {
                    Row {
                        Text(
                            text = stringResource(tv_member_description),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.weight(2F)
                        )
                        Text(
                            text = stringResource(tv_form_value, member.description),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(6F)
                        )
                    }
                }
            }
            VerticalDivider()
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(2F)
                    .fillMaxHeight()
            ) {
                if (coupon == null) {
                    Text(
                        stringResource(
                            id = R.string.tv_ticketing_not_created
                        ),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                } else {
                    Column {
                        Text(
                            stringResource(
                                R.string.tv_ticketing_status
                            ),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text =
                                when (coupon.claimStatus) {
                                    CouponRedeemStatus.NOT_CLAIMED -> stringResource(
                                        R.string.tv_ticket_not_claimed
                                    )

                                    CouponRedeemStatus.CLAIMED_BY_SCAN -> stringResource(
                                        R.string.tv_ticket_claimed_by_qr_short
                                    )

                                    CouponRedeemStatus.CLAIMED_BY_ADMIN -> stringResource(
                                        R.string.tv_ticket_claimed_by_admin_short
                                    )
                                },
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
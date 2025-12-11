package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_form_value
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_participant
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_recipient
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_member_search
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R.string.tv_no_data
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.Coupon
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.CouponRedeemStatus
import com.lelestargazer.qurban_ticketing_system.member_shared.domain.model.QurbanStatus
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.R
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingEvent.OnBackPressed
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingEvent.OnBottomSheetDismissed
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingEvent.OnGenerateTicket
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingEvent.OnQueryChanged
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.MANUAL
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.TicketingBottomSheetState.TicketingType.SCAN
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.TicketingState.ViewType
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.component.TicketingItem
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.component.TicketingLoading
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme
import com.lelestargazer.qurban_ticketing_system.theme.component.ManagementTicketingBanner
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TicketingScreen(
    state: TicketingState,
    onEvent: (TicketingEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    val input = LocalSoftwareKeyboardController.current
    val readWritePermission = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
    )


    //  TODO: Handle API 32
    val manageAllFilePermission =
        rememberPermissionState(Manifest.permission.MANAGE_EXTERNAL_STORAGE)


    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val qrScanner = rememberLauncherForActivityResult(ScanQRCode()) { qrResult ->
        if (qrResult is QRResult.QRSuccess && qrResult.content.rawValue != null) {
            onEvent(
                TicketingEvent.OnBottomSheetOpened(
                    qrHash = qrResult.content.rawValue,
                    type = SCAN
                )
            )
        }
    }

    val memberAndCoupons = state.memberAndCoupon.collectAsLazyPagingItems()

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (cameraPermission.status.isGranted) {
                        qrScanner.launch(null)
                    } else {
                        cameraPermission.launchPermissionRequest()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    ) { paddingValues ->
        if (state.isBottomSheetOpened && state.sheetState != null) {
            val selectedMember = state.sheetState.selectedMember.member
            val ticket = state.sheetState.selectedMember.coupon as Coupon

            ModalBottomSheet(
                onDismissRequest = {
                    onEvent(OnBottomSheetDismissed)
                },
            ) {
                if (ticket.claimStatus != CouponRedeemStatus.NOT_CLAIMED) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = screenPadding.horizontal,
                                vertical = screenPadding.vertical
                            )
                    ) {
                        Text(
                            text = when (ticket.claimStatus) {
                                CouponRedeemStatus.CLAIMED_BY_SCAN -> stringResource(R.string.tv_ticket_claimed_by_qr)
                                CouponRedeemStatus.CLAIMED_BY_ADMIN -> stringResource(R.string.tv_ticket_claimed_by_admin)
                                else -> ""
                            },
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = screenPadding.vertical,
                                horizontal = screenPadding.horizontal
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = screenPadding.vertical)
                        ) {
                            Image(
                                bitmap = state.sheetState.qrImage.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.size(128.dp)
                            )
                        }

                        Column(
                            Modifier.fillMaxWidth()
                        ) {

                            val memberType =
                                if (selectedMember.status == QurbanStatus.Participant) {
                                    stringResource(tv_member_participant)
                                } else {
                                    stringResource(tv_member_recipient)
                                }

                            Row {
                                Text(
                                    text = stringResource(
                                        R.string.tv_member_ticketing_name,
                                        memberType
                                    ),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.weight(4F)
                                )

                                Text(
                                    text = stringResource(
                                        tv_form_value,
                                        selectedMember.name
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(6F)
                                )
                            }

                            Row {
                                Text(
                                    text = stringResource(
                                        R.string.tv_member_ticketing_phone,
                                        memberType,
                                    ), style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    modifier = Modifier.weight(4F)
                                )

                                Text(
                                    text = stringResource(
                                        tv_form_value,
                                        selectedMember.phoneNumber.orEmpty()
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.weight(6F)
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.padding(top = screenPadding.vertical)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    // TODO: WA
                                },
                                shape = RoundedCornerShape(25),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_call_whatsapp),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Button(
                                onClick = {
                                    onEvent(TicketingEvent.OnClaimTicket)
                                },
                                shape = RoundedCornerShape(25),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = screenPadding.vertical)
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_ticketing_claim_coupon),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            ManagementTicketingBanner(
                title = "",
                isMainMenu = false,
                onBackPressed = {
                    onEvent(OnBackPressed)
                }
            )

            AnimatedContent(state.isLoading) { isLoading ->
                when (isLoading) {
                    true -> {
                        TicketingLoading()
                    }

                    false -> {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(screenPadding.horizontal),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Min)
                                    .padding(top = screenPadding.vertical)
                                    .padding(horizontal = screenPadding.horizontal)
                            ) {

                                TextField(
                                    value = state.query,
                                    onValueChange = { newQuery ->
                                        onEvent(OnQueryChanged(newQuery))
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(
                                            stringResource(tv_member_search),
                                            style = MaterialTheme.typography.bodySmall.copy(
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
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            input?.hide()
                                        }
                                    ),
                                    shape = RoundedCornerShape(25F),
                                    modifier = Modifier
                                        .weight(1F)
                                        .border(1.dp, Color.Black, RoundedCornerShape(25F))
                                )

                                Button(
                                    onClick = {
                                        if (Build.VERSION.SDK_INT > 30) {
                                            onEvent(OnGenerateTicket)
                                        } else {
                                            if (readWritePermission.allPermissionsGranted) {
                                                onEvent(OnGenerateTicket)
                                            } else {
                                                readWritePermission.launchMultiplePermissionRequest()
                                            }
                                        }
                                    },
                                    shape = RoundedCornerShape(25),
                                    modifier = Modifier.fillMaxHeight(),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = null
                                    )
                                }
                            }

                            LazyRow(
                                contentPadding = PaddingValues(
                                    start = screenPadding.horizontal,
                                    end = screenPadding.horizontal
                                ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(screenPadding.horizontal),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = screenPadding.vertical)
                            ) {
                                item {
                                    FilterChip(
                                        selected = state.displayType == ViewType.AVAILABLE_COUPONS,
                                        onClick = {
                                            when (state.displayType) {
                                                ViewType.ALL_MEMBERS -> onEvent(
                                                    TicketingEvent.OnViewTypeSwitch(
                                                        ViewType.AVAILABLE_COUPONS
                                                    )
                                                )

                                                ViewType.AVAILABLE_COUPONS -> onEvent(
                                                    TicketingEvent.OnViewTypeSwitch(
                                                        ViewType.ALL_MEMBERS
                                                    )
                                                )
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = stringResource(
                                                    R.string.tv_total_unclaimed_coupon,
                                                    state.totalUnclaimedCoupon
                                                ), style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }
                                    )
                                }

                                item {
                                    FilterChip(
                                        selected = false,
                                        enabled = false,
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = stringResource(
                                                    R.string.tv_total_member,
                                                    state.totalMember
                                                ),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                        }, colors = FilterChipDefaults.filterChipColors(
                                            disabledLabelColor = MaterialTheme.colorScheme.onSurface
                                        ),
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                }
                            }

                            AnimatedContent(memberAndCoupons.loadState.refresh is LoadState.Loading) {
                                when (it) {
                                    true ->
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            CircularProgressIndicator()
                                        }

                                    false ->
                                        AnimatedContent(memberAndCoupons.loadState.refresh is LoadState.NotLoading && memberAndCoupons.itemCount > 0) { isMemberExist ->
                                            when (isMemberExist) {
                                                true ->
                                                    LazyColumn(
                                                        contentPadding = PaddingValues(
                                                            vertical = screenPadding.vertical
                                                        ),
                                                        modifier = Modifier.fillMaxSize()
                                                    ) {
                                                        items(
                                                            count = memberAndCoupons.itemCount
                                                        ) { index ->
                                                            memberAndCoupons[index]?.let { memberAndCoupon ->
                                                                TicketingItem(
                                                                    index = index,
                                                                    memberAndCoupon = memberAndCoupon,
                                                                    onClicked = { itemMemberAndCoupon ->
                                                                        onEvent(
                                                                            TicketingEvent.OnBottomSheetOpened(
                                                                                qrHash = itemMemberAndCoupon.coupon?.hashCode,
                                                                                type = MANUAL
                                                                            )
                                                                        )
                                                                    },
                                                                    modifier = Modifier.fillMaxWidth()
                                                                )
                                                            }
                                                        }
                                                    }

                                                false -> Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    Text(
                                                        text = stringResource(tv_no_data),
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
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewParticipantTicketingScreen() {
    QurbanTicketingSystemTheme {
        TicketingScreen(
            state = TicketingState(),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
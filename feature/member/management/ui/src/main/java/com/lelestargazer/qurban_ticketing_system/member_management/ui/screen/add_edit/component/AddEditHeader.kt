package com.lelestargazer.qurban_ticketing_system.member_management.ui.screen.add_edit.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.lelestargazer.qurban_ticketing_system.common.R.string.btn_back
import com.lelestargazer.qurban_ticketing_system.common.R.string.tv_title_app_name
import com.lelestargazer.qurban_ticketing_system.member_management.ui.R
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.ADD
import com.lelestargazer.qurban_ticketing_system.member_management.ui.route.MemberAddEdit.ScreenType.EDIT
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding
import com.lelestargazer.qurban_ticketing_system.theme.QurbanTicketingSystemTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddEditHeader(
    screenType: ScreenType,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenPadding = LocalScreenPadding.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycle by lifecycleOwner.lifecycle.currentStateAsState()
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = modifier
        ) {
            Image(
                painter = painterResource(R.drawable.banner_add_edit),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                when (screenType) {
                    ADD -> stringResource(R.string.tv_banner_add_participant_recipient)
                    EDIT -> stringResource(R.string.tv_banner_edit_participant_recipient)
                },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                ),
                modifier = Modifier.padding(
                    horizontal = screenPadding.horizontal,
                    vertical = 24.dp
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = screenPadding.horizontal)
                .padding(top = screenPadding.vertical)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = {
                        if (lifecycle.isAtLeast(Lifecycle.State.RESUMED)) {
                            onBackPressed()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                    text = stringResource(id = btn_back),
                    style = MaterialTheme.typography.titleSmallEmphasized.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                )
            }

            Text(
                stringResource(tv_title_app_name),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
private fun PreviewAddEditHeader() {
    QurbanTicketingSystemTheme {
        AddEditHeader(
            screenType = ADD,
            onBackPressed = {

            }
        )
    }
}
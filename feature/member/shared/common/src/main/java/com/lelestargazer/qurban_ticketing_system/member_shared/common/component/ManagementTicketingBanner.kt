package com.lelestargazer.qurban_ticketing_system.member_shared.common.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lelestargazer.qurban_ticketing_system.common.R.string.tv_title_app_name
import com.lelestargazer.qurban_ticketing_system.member_shared.common.R
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding

@Composable
fun ManagementTicketingBanner(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = LocalScreenPadding.current
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.banner_management_ticketing),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
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
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                stringResource(tv_title_app_name),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            )
        }
    }
}
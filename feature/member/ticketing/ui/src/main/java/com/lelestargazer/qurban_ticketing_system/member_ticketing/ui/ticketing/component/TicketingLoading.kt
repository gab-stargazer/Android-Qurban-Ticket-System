package com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.ticketing.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.lelestargazer.qurban_ticketing_system.member_ticketing.ui.R
import com.lelestargazer.qurban_ticketing_system.theme.LocalScreenPadding

@Composable
internal fun TicketingLoading(modifier: Modifier = Modifier) {
    val screenPadding = LocalScreenPadding.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = screenPadding.vertical,
            alignment = Alignment.CenterVertically
        ),
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = screenPadding.horizontal,
                vertical = screenPadding.vertical
            )
    ) {

        CircularProgressIndicator()
        Text(
            stringResource(id = R.string.msg_loading_ticketing),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        )
    }
}
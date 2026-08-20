package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shraddhacalendar.R
import com.shraddhacalendar.ui.theme.*

@Composable
fun TopDedicationBanner(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(SurfaceCard)
            .border(1.dp, PrimarySaffron.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            // Line 1: Developed and Managed by Gururaj Kulkarni
            Text(
                text = stringResource(R.string.developed_by),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                fontWeight = FontWeight.Bold,
                color = PrimarySaffronDark,
                textAlign = TextAlign.Center
            )

            // Line 2: Dedicated in loving memory of my beloved father Sri Pranesh Kulkarni
            Text(
                text = "🌸 " + stringResource(R.string.dedicated_to_father) + " 🌸",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // Line 3: Dedicated to the service of Sri Hari, Vayu, and Sri 108 Uttaradi Math Parampara
            Text(
                text = stringResource(R.string.dedicated_to_matha),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

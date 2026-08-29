package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
    tradition: com.shraddhacalendar.core.models.MadhwaTradition = com.shraddhacalendar.core.models.MadhwaTradition.UTTARADI_MATHA,
    modifier: Modifier = Modifier
) {
    val invocation = when (tradition) {
        com.shraddhacalendar.core.models.MadhwaTradition.UTTARADI_MATHA -> stringResource(R.string.invocation_um)
        com.shraddhacalendar.core.models.MadhwaTradition.MANTRALAYA_MUTT -> stringResource(R.string.invocation_srs)
        com.shraddhacalendar.core.models.MadhwaTradition.UDUPI_ASHTA_MATHA -> stringResource(R.string.invocation_udupi)
    }

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
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Opening Devotional Invocation
            Text(
                text = "🕉️ $invocation 🕉️",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.5.sp),
                fontWeight = FontWeight.Bold,
                color = PrimarySaffronDark,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp),
                color = PrimarySaffron.copy(alpha = 0.25f),
                thickness = 0.8.dp
            )

            // 1. Dedication to Sri Hari, Sri Vayu and Sri 108 Uttaradi Math Parampara
            Text(
                text = stringResource(R.string.dedication_service),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                fontWeight = FontWeight.Medium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            // 2. Dedication in memory of father Late Sri Pranesh Kulkarni
            Text(
                text = "🌸 " + stringResource(R.string.dedication_father) + " 🌸",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // 3. Developer attribution
            Text(
                text = stringResource(R.string.developed_by),
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                fontWeight = FontWeight.Bold,
                color = PrimarySaffronDark,
                textAlign = TextAlign.Center
            )
        }
    }
}

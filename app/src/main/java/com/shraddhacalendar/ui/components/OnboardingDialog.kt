package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shraddhacalendar.R
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.ui.theme.*

@Composable
fun OnboardingDialog(
    initialTradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA,
    onConfirm: (MadhwaTradition) -> Unit
) {
    var selectedTradition by remember { mutableStateOf(initialTradition) }

    Dialog(
        onDismissRequest = { /* Require explicit selection */ },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "🕉️ " + stringResource(R.string.onboarding_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                    fontWeight = FontWeight.Bold,
                    color = PrimarySaffronDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.onboarding_subtitle),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 17.sp
                )

                HorizontalDivider(color = PrimarySaffron.copy(alpha = 0.2f), thickness = 0.8.dp)

                // Option 1: Sri Uttaradi Matha
                TraditionOptionRow(
                    tradition = MadhwaTradition.UTTARADI_MATHA,
                    title = stringResource(R.string.tradition_um),
                    subtitle = stringResource(R.string.tradition_um_subtitle),
                    isSelected = selectedTradition == MadhwaTradition.UTTARADI_MATHA,
                    onClick = { selectedTradition = MadhwaTradition.UTTARADI_MATHA }
                )

                // Option 2: Mantralaya (SRS Mutt)
                TraditionOptionRow(
                    tradition = MadhwaTradition.MANTRALAYA_MUTT,
                    title = stringResource(R.string.tradition_srs),
                    subtitle = stringResource(R.string.tradition_srs_subtitle),
                    isSelected = selectedTradition == MadhwaTradition.MANTRALAYA_MUTT,
                    onClick = { selectedTradition = MadhwaTradition.MANTRALAYA_MUTT }
                )

                // Option 3: Udupi Ashta Mathas
                TraditionOptionRow(
                    tradition = MadhwaTradition.UDUPI_ASHTA_MATHA,
                    title = stringResource(R.string.tradition_udupi),
                    subtitle = stringResource(R.string.tradition_udupi_subtitle),
                    isSelected = selectedTradition == MadhwaTradition.UDUPI_ASHTA_MATHA,
                    onClick = { selectedTradition = MadhwaTradition.UDUPI_ASHTA_MATHA }
                )

                Spacer(modifier = Modifier.height(6.dp))

                Button(
                    onClick = { onConfirm(selectedTradition) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_confirm),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = SurfaceCard
                    )
                }
            }
        }
    }
}

@Composable
private fun TraditionOptionRow(
    tradition: MadhwaTradition,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PrimarySaffron else CardBorder
    val bgColor = if (isSelected) PrimarySaffron.copy(alpha = 0.08f) else SurfaceCard

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(if (isSelected) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isSelected) PrimarySaffron else TextSecondary.copy(alpha = 0.5f),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.5.sp),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isSelected) PrimarySaffronDark else TextPrimary
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.5.sp),
                color = TextSecondary,
                lineHeight = 14.sp
            )
        }
    }
}

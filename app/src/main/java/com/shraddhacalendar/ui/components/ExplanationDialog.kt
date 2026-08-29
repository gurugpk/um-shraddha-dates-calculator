package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.ui.theme.*

@Composable
fun ExplanationDialog(
    event: ShraddhaEvent,
    locationName: String,
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(R.string.details),
                            tint = PrimarySaffron
                        )
                        Text(
                            text = stringResource(R.string.calculation_details),
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Event info card
                val localizedDay = PanchangaLocalizer.localizeDayOfWeek(event.dayOfWeek, currentLanguage)
                val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, currentLanguage)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceCardVariant)
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = localizedTraditionalName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = "${event.gregorianDate} ($localizedDay)",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = PanchangaLocalizer.localizeFullPanchanga(event.tithi, currentLanguage),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.local_astronomical_timings),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))

                DetailRow(stringResource(R.string.location_label_short), locationName)
                DetailRow(stringResource(R.string.sunrise), "${event.kalaDetails.sunrise}")
                DetailRow(stringResource(R.string.sunset), "${event.kalaDetails.sunset}")
                DetailRow(stringResource(R.string.daylight_dinmana), "${event.kalaDetails.dinmanaMinutes / 60}h ${event.kalaDetails.dinmanaMinutes % 60}m")
                DetailRow(stringResource(R.string.aparahna_kala_4th), "${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd}")
                DetailRow(stringResource(R.string.kutapa_muhurta_8th), "${event.kalaDetails.kutapaStart} - ${event.kalaDetails.kutapaEnd}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.panchanga_determination_trace),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = PanchangaLocalizer.localizeExplanation(event.explanation, currentLanguage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(BackgroundWarm)
                        .padding(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(stringResource(R.string.close), color = SurfaceCard)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary
        )
    }
}

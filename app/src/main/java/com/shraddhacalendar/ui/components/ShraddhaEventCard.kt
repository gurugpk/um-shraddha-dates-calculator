package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.core.models.ShraddhaType
import com.shraddhacalendar.ui.theme.*
import java.time.format.DateTimeFormatter

@Composable
fun ShraddhaEventCard(
    event: ShraddhaEvent,
    locationName: String,
    language: AppLanguage = AppLanguage.ENGLISH,
    isCalendarEnabled: Boolean = false,
    onCalendarToggle: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDetailsDialog by remember { mutableStateOf(false) }

    val isVarshika = event.type == ShraddhaType.VARSHIKA
    val isUna = event.type == ShraddhaType.UNA_RITE

    val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
    val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, language)
    val localizedPaksha = PanchangaLocalizer.localizePaksha(event.tithi.tithi.paksha, language)
    val localizedTithi = PanchangaLocalizer.localizeTithi(event.tithi.tithi, language)

    val cardBorderColor = when {
        isVarshika -> PrimarySaffron
        isUna -> SecondaryGold
        else -> DividerColor
    }

    val headerBgColor = when {
        isVarshika -> PrimarySaffron.copy(alpha = 0.12f)
        isUna -> SecondaryGold.copy(alpha = 0.08f)
        else -> SurfaceCardVariant
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isVarshika) 1.5.dp else 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isVarshika) 3.dp else 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header: Traditional Name & Sequence
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerBgColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = localizedTraditionalName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isVarshika) PrimarySaffronDark else TextPrimary
                    )
                }

                IconButton(onClick = { showDetailsDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Details",
                        tint = PrimarySaffron
                    )
                }
            }

            // Body: Gregorian Date & Panchanga
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Gregorian Date Row
                val dateFormatted = event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                Text(
                    text = "$dateFormatted (${event.dayOfWeek})",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                // Panchanga Info Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PanchangaChip(text = localizedMasa)
                    PanchangaChip(text = "$localizedPaksha $localizedTithi")
                }

                // Aparahna Timing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.aparahna_window) + ":",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Text(
                        text = "${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimarySaffronDark
                    )
                }

                HorizontalDivider(color = DividerColor.copy(alpha = 0.5f))

                // Individual Calendar Toggle Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = if (isCalendarEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                            contentDescription = null,
                            tint = if (isCalendarEnabled) PrimarySaffron else TextTertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = if (isCalendarEnabled) stringResource(R.string.calendar_reminder_on) else stringResource(R.string.calendar_reminder_off),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isCalendarEnabled) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isCalendarEnabled) PrimarySaffronDark else TextSecondary
                        )
                    }

                    Switch(
                        checked = isCalendarEnabled,
                        onCheckedChange = onCalendarToggle,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceCard,
                            checkedTrackColor = PrimarySaffron,
                            uncheckedThumbColor = TextTertiary,
                            uncheckedTrackColor = SurfaceCardVariant
                        )
                    )
                }
            }
        }
    }

    if (showDetailsDialog) {
        ExplanationDialog(
            event = event,
            locationName = locationName,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
private fun PanchangaChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceCardVariant)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )
    }
}

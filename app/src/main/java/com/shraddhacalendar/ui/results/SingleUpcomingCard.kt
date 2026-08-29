package com.shraddhacalendar.ui.results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import com.shraddhacalendar.ui.components.ExplanationDialog
import com.shraddhacalendar.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun SingleUpcomingCard(
    event: ShraddhaEvent,
    locationName: String,
    language: AppLanguage = AppLanguage.ENGLISH,
    isCalendarEnabled: Boolean = false,
    onCalendarToggle: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showDetailsDialog by remember { mutableStateOf(false) }

    val daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), event.gregorianDate)
    val countdownText = PanchangaLocalizer.localizeDaysRemaining(daysRemaining, language)

    val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
    val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, language)
    val localizedPaksha = PanchangaLocalizer.localizePaksha(event.tithi.tithi.paksha, language)
    val localizedTithi = PanchangaLocalizer.localizeTithi(event.tithi.tithi, language)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(2.dp, PrimarySaffron, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Badge & Ceremony Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.next_upcoming_shraddha).uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimarySaffronDark,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimarySaffron.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = countdownText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                }
            }

            Text(
                text = localizedTraditionalName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            // Date Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = PrimarySaffron,
                    modifier = Modifier.size(24.dp)
                )
                val dayName = PanchangaLocalizer.localizeDayOfWeek(event.dayOfWeek, language)
                val datePart = event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                val formattedDate = "$dayName, $datePart"
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            HorizontalDivider(color = DividerColor)

            // Panchanga Summary Rows
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PanchangaRow(stringResource(R.string.tithi), "$localizedTithi ($localizedPaksha)")
                PanchangaRow(stringResource(R.string.masa), localizedMasa)
                PanchangaRow(stringResource(R.string.samvatsara), PanchangaLocalizer.localizeSamvatsara(event.tithi.samvatsara, language))
                PanchangaRow(stringResource(R.string.location_label), PanchangaLocalizer.localizeLocation(locationName, language))
                PanchangaRow(stringResource(R.string.aparahna_window), "${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd}")
            }

            HorizontalDivider(color = DividerColor.copy(alpha = 0.5f))

            // Calendar Toggle Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isCalendarEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsNone,
                        contentDescription = null,
                        tint = if (isCalendarEnabled) PrimarySaffron else TextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (isCalendarEnabled) stringResource(R.string.calendar_reminder_on) else stringResource(R.string.calendar_reminder_off),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isCalendarEnabled) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isCalendarEnabled) PrimarySaffronDark else TextSecondary
                    )
                }

                Switch(
                    checked = isCalendarEnabled,
                    onCheckedChange = onCalendarToggle,
                    colors = shraddhaSwitchColors()
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = { showDetailsDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimarySaffron)
            ) {
                Icon(Icons.Default.Info, contentDescription = stringResource(R.string.details), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.view_astronomical_trace))
            }
        }
    }

    if (showDetailsDialog) {
        ExplanationDialog(
            event = event,
            locationName = locationName,
            currentLanguage = language,
            onDismiss = { showDetailsDialog = false }
        )
    }
}

@Composable
private fun PanchangaRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextPrimary)
    }
}

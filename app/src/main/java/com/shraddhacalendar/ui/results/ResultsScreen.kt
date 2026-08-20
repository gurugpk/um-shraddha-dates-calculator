package com.shraddhacalendar.ui.results

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shraddhacalendar.R
import com.shraddhacalendar.core.calendar.makeEntityKey
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.ShraddhaCalculationResult
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.ui.components.ExpandableYearSection
import com.shraddhacalendar.ui.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    result: ShraddhaCalculationResult,
    language: AppLanguage = AppLanguage.ENGLISH,
    isCalendarActive: (String) -> Boolean = { false },
    isAllCalendarActive: Boolean = false,
    onToggleIndividualCalendar: (String, Boolean, ShraddhaEvent) -> Unit = { _, _, _ -> },
    onToggleAllCalendar: (Boolean) -> Unit = {},
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val person = result.personRecord
    val locationName = person.location.displayName

    val localizedMasa = PanchangaLocalizer.localizeMasa(result.mrutaTithi.masa, result.mrutaTithi.isAdhikaMasa, language)
    val localizedPaksha = PanchangaLocalizer.localizePaksha(result.mrutaTithi.tithi.paksha, language)
    val localizedTithi = PanchangaLocalizer.localizeTithi(result.mrutaTithi.tithi, language)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.results_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.close))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceCard)
            )
        },
        containerColor = BackgroundWarm
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Person & Death Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = PrimarySaffron,
                            modifier = Modifier.size(28.dp)
                        )
                        Column {
                            Text(
                                text = person.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${stringResource(R.string.death_summary_prefix)} ${person.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))} at ${person.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    HorizontalDivider(color = DividerColor)

                    // Calculated Mruta Panchanga Details Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCardVariant)
                            .padding(14.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = stringResource(R.string.death_panchanga_details).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = "${result.mrutaTithi.samvatsara} Nama Samvatsara, $localizedMasa, $localizedPaksha $localizedTithi",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${stringResource(R.string.location_label)}: $locationName",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // Global "Add All to Calendar" Master Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimarySaffron.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PrimarySaffronDark
                        )
                        Column {
                            Text(
                                text = stringResource(R.string.add_all_to_calendar),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "2-day & 1-day reminders",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = isAllCalendarActive,
                        onCheckedChange = onToggleAllCalendar,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceCard,
                            checkedTrackColor = PrimarySaffron,
                            uncheckedThumbColor = TextTertiary,
                            uncheckedTrackColor = SurfaceCardVariant
                        )
                    )
                }
            }

            // Results Content: If Death > 1 Year -> Single Upcoming Card, else 5-Year Accordion
            if (result.isDeathOlderThanOneYear && result.nextUpcomingShraddha != null) {
                val upcoming = result.nextUpcomingShraddha
                val entityKey = makeEntityKey(person.name, upcoming.gregorianDate, upcoming.sequenceNumber)
                SingleUpcomingCard(
                    event = upcoming,
                    locationName = locationName,
                    language = language,
                    isCalendarEnabled = isCalendarActive(entityKey),
                    onCalendarToggle = { enabled -> onToggleIndividualCalendar(entityKey, enabled, upcoming) }
                )
            } else {
                result.yearlySections.forEach { section ->
                    ExpandableYearSection(
                        section = section,
                        personName = person.name,
                        locationName = locationName,
                        language = language,
                        isCalendarEnabled = isCalendarActive,
                        onCalendarToggle = onToggleIndividualCalendar
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

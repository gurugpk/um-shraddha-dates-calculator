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
import androidx.compose.runtime.*
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

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    result: ShraddhaCalculationResult,
    language: AppLanguage = AppLanguage.ENGLISH,
    isCalendarActive: (String) -> Boolean = { false },
    isAllCalendarActive: Boolean = false,
    isSaved: Boolean = false,
    onToggleSave: () -> Unit = {},
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
                actions = {
                    IconButton(onClick = onToggleSave) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = stringResource(R.string.save_profile_btn),
                            tint = PrimarySaffronDark
                        )
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
            Spacer(modifier = Modifier.height(2.dp))

            // Dedicated Top Banner
            com.shraddhacalendar.ui.components.TopDedicationBanner()

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
                                text = PanchangaLocalizer.localizePersonName(person.name, language),
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
                                text = PanchangaLocalizer.localizeFullPanchanga(result.mrutaTithi, language),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                            Text(
                                text = "${stringResource(R.string.location_label)}: ${PanchangaLocalizer.localizeLocation(locationName, language)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }

            // 1. Permanent Save Profile Card (Symmetrical Master Toggle Card)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onToggleSave() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSaved) PrimarySaffron.copy(alpha = 0.12f) else PrimarySaffron.copy(alpha = 0.05f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSaved) PrimarySaffronDark.copy(alpha = 0.6f) else PrimarySaffron.copy(alpha = 0.2f)
                ),
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSaved) PrimarySaffron.copy(alpha = 0.2f) else SurfaceCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = PrimarySaffronDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = if (isSaved) stringResource(R.string.saved_badge) else stringResource(R.string.save_profile_btn),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = if (isSaved) stringResource(R.string.saved_profile_subtitle) else stringResource(R.string.save_profile_subtitle),
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = isSaved,
                        onCheckedChange = { onToggleSave() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = SurfaceCard,
                            checkedTrackColor = PrimarySaffron,
                            uncheckedThumbColor = TextTertiary,
                            uncheckedTrackColor = SurfaceCardVariant
                        )
                    )
                }
            }

            // 2. Global "Add All to Calendar" Master Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onToggleAllCalendar(!isAllCalendarActive) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAllCalendarActive) PrimarySaffron.copy(alpha = 0.12f) else PrimarySaffron.copy(alpha = 0.05f)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isAllCalendarActive) PrimarySaffronDark.copy(alpha = 0.6f) else PrimarySaffron.copy(alpha = 0.2f)
                ),
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
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isAllCalendarActive) PrimarySaffron.copy(alpha = 0.2f) else SurfaceCard),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = PrimarySaffronDark,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = stringResource(R.string.add_all_to_calendar),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = stringResource(R.string.calendar_sync_subtitle),
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

            // Export / Share Print-Ready PDF Button
            val context = androidx.compose.ui.platform.LocalContext.current
            val coroutineScope = androidx.compose.runtime.rememberCoroutineScope()
            var isExportingPdf by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

            Button(
                onClick = {
                    if (!isExportingPdf) {
                        isExportingPdf = true
                        kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).run {
                            try {
                                val pdfFile = com.shraddhacalendar.core.pdf.ShraddhaPdfExporter.generateAndSharePdf(
                                    context = context,
                                    result = result,
                                    language = language
                                )
                                val shareUri = com.shraddhacalendar.core.pdf.ShraddhaPdfExporter.getShareUri(context, pdfFile)
                                val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "application/pdf"
                                    putExtra(android.content.Intent.EXTRA_STREAM, shareUri)
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, "Shraddha Calendar - ${person.name}")
                                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                val chooser = android.content.Intent.createChooser(
                                    shareIntent,
                                    context.getString(R.string.pdf_share_title)
                                ).apply {
                                    addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                context.startActivity(chooser)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                isExportingPdf = false
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimarySaffronDark,
                    contentColor = androidx.compose.ui.graphics.Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isExportingPdf) {
                        CircularProgressIndicator(
                            color = androidx.compose.ui.graphics.Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Text(
                            text = stringResource(R.string.pdf_generating),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.export_pdf),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
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

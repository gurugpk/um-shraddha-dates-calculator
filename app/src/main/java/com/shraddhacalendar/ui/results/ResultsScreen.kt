package com.shraddhacalendar.ui.results

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.pdf.ShraddhaPdfExporter
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.TimingExplanationGenerator
import com.shraddhacalendar.core.shraddha.MissingPersonGuidanceRepository
import com.shraddhacalendar.core.shraddha.ShastricCircumstanceRepository
import com.shraddhacalendar.ui.components.CircumstanceGuidanceCard
import com.shraddhacalendar.ui.components.MissingPersonAdvisoryCard
import com.shraddhacalendar.ui.components.CeremonyDetailDialog
import com.shraddhacalendar.ui.components.EkadashiGuidanceDialog
import com.shraddhacalendar.ui.components.PanchaKalaGuideDialog
import com.shraddhacalendar.ui.components.TopDedicationBanner
import com.shraddhacalendar.ui.theme.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    result: ShraddhaCalculationResult,
    currentLanguage: AppLanguage,
    activeCalendarEntities: Set<String>,
    isAllCalendarActive: Boolean,
    isCurrentResultSaved: Boolean,
    onToggleSaveProfile: () -> Unit,
    onToggleAllCalendar: () -> Unit,
    onToggleEventCalendar: (ShraddhaEvent) -> Unit,
    onNewCalculationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedTraceEvent by remember { mutableStateOf<ShraddhaEvent?>(null) }
    var selectedCeremonyInfo by remember { mutableStateOf<EducationalCeremonyInfo?>(null) }
    var selectedEkadashiEvent by remember { mutableStateOf<ShraddhaEvent?>(null) }
    var isPanchaKalaGuideOpen by remember { mutableStateOf(false) }

    // Compute the year containing the next upcoming observance to expand dynamically
    val upcomingYearIndex = remember(result) {
        val nextDate = result.nextUpcomingObservance?.gregorianDate
        if (nextDate != null) {
            result.yearlyObservanceGroups.find { group ->
                group.varshikaEvent.gregorianDate == nextDate ||
                    group.pakshaEvent?.gregorianDate == nextDate ||
                    group.masikas.any { it.gregorianDate == nextDate }
            }?.yearIndex ?: 1
        } else {
            1
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.results_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = PanchangaLocalizer.localizeTradition(result.personRecord.tradition, currentLanguage),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                },
                actions = {
                    if (!result.personRecord.isMissingUnconfirmed) {
                        IconButton(onClick = { isPanchaKalaGuideOpen = true }) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = stringResource(R.string.pancha_kala_guide_btn),
                                tint = PrimarySaffronDark
                            )
                        }
                    }
                    IconButton(onClick = onNewCalculationClick) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.new_calculation_btn), tint = PrimarySaffronDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SurfaceCard)
            )
        },
        containerColor = BackgroundWarm
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 32.dp)
        ) {
            // 1. Top Devotional Dedication Banner
            item {
                TopDedicationBanner(tradition = result.tradition)
            }

            if (result.personRecord.isMissingUnconfirmed) {
                // Missing Person Shastric Advisory Flow
                item {
                    val missingGuidance = MissingPersonGuidanceRepository.getGuidance(
                        ageAtDisappearance = result.personRecord.ageAtDisappearance,
                        lastSeenDate = result.personRecord.lastSeenDate,
                        language = currentLanguage,
                        tradition = result.tradition
                    )
                    MissingPersonAdvisoryCard(
                        guidance = missingGuidance,
                        onConfirmDemiseClick = onNewCalculationClick
                    )
                }

                // Save Profile action for Missing Person
                item {
                    MasterActionsCard(
                        isSaved = isCurrentResultSaved,
                        isAllCalendarActive = false,
                        onToggleSave = onToggleSaveProfile,
                        onToggleAllCalendar = {},
                        showCalendarOption = false
                    )
                }
            } else {
                // Confirmed Demise Standard Flow
                // 2. Demise & Panchanga Summary Card
                item {
                    DemiseSummaryCard(
                        result = result,
                        language = currentLanguage,
                        onOpenPanchaKalaGuide = { isPanchaKalaGuideOpen = true }
                    )
                }

                // 3. Dosha / Traditional Considerations Alert Card
                item {
                    DoshaStatusCard(doshaResult = PanchangaLocalizer.localizeDoshaResult(result.doshaEvaluation, currentLanguage))
                }

                // 4. Durmarana / Circumstance Shastric Guidance Card (if unnatural demise or special circumstance)
                if (result.personRecord.demiseCircumstance != DemiseCircumstance.NATURAL) {
                    item {
                        val circumstanceGuidance = ShastricCircumstanceRepository.getGuidance(
                            circumstance = result.personRecord.demiseCircumstance,
                            language = currentLanguage,
                            tradition = result.tradition
                        )
                        CircumstanceGuidanceCard(guidance = circumstanceGuidance)
                    }
                }

                // 5. Highlighted Next Upcoming Observance Banner
                item {
                    val nextEvent = result.nextUpcomingObservance
                    if (nextEvent != null) {
                        NextUpcomingObservanceCard(
                            event = nextEvent,
                            personRecord = result.personRecord,
                            language = currentLanguage,
                            isCalendarScheduled = activeCalendarEntities.contains(
                                "${result.personRecord.name}_${nextEvent.gregorianDate}_${nextEvent.traditionalName}"
                            ),
                            onToggleCalendar = { onToggleEventCalendar(nextEvent) },
                            onViewTrace = { selectedTraceEvent = nextEvent },
                            onViewInfo = {
                                selectedCeremonyInfo = EducationalContentRepository.findInfoForEvent(nextEvent)
                            },
                            onViewEkadashiGuidance = { selectedEkadashiEvent = nextEvent }
                        )
                    }
                }

                // 6. Master Action Bar (Save Profile Toggle + Add All to Calendar)
                item {
                    MasterActionsCard(
                        isSaved = isCurrentResultSaved,
                        isAllCalendarActive = isAllCalendarActive,
                        onToggleSave = onToggleSaveProfile,
                        onToggleAllCalendar = onToggleAllCalendar
                    )
                }

                // 6. Chronological Year Groups (Year 1 with Masikas drill-down, Year 2+ with Shraddha & Paksha)
                items(result.yearlyObservanceGroups, key = { it.yearIndex }) { group ->
                    YearlyObservanceAccordion(
                        group = group,
                        personRecord = result.personRecord,
                        language = currentLanguage,
                        activeCalendarEntities = activeCalendarEntities,
                        initialExpanded = group.yearIndex == upcomingYearIndex,
                        onToggleCalendar = onToggleEventCalendar,
                        onViewTrace = { selectedTraceEvent = it },
                        onViewInfo = { event ->
                            selectedCeremonyInfo = EducationalContentRepository.findInfoForEvent(event)
                        },
                        onViewEkadashiGuidance = { selectedEkadashiEvent = it },
                        onExportYearPdf = { yearIndex ->
                            try {
                                ShraddhaPdfExporter.generateAndSharePdf(context, result, currentLanguage, targetYearIndex = yearIndex)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error generating PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                        }
                    )
                }

                // 7. PDF Export Button
                item {
                    Button(
                        onClick = {
                            try {
                                ShraddhaPdfExporter.generateAndSharePdf(context, result, currentLanguage)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error generating PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = SurfaceCard)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.export_pdf),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = SurfaceCard
                        )
                    }
                }
            }

            // 8. Universal Application Disclaimer
            item {
                Text(
                    text = stringResource(R.string.app_disclaimer),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                    color = TextSecondary.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }
        }
    }

    // Trace Dialog
    if (selectedTraceEvent != null) {
        TraceDetailDialog(
            event = selectedTraceEvent!!,
            location = result.personRecord.location,
            language = currentLanguage,
            onOpenPanchaKalaGuide = {
                selectedTraceEvent = null
                isPanchaKalaGuideOpen = true
            },
            onDismiss = { selectedTraceEvent = null }
        )
    }

    // Pancha Kala Guide Dialog
    if (isPanchaKalaGuideOpen) {
        PanchaKalaGuideDialog(
            date = result.nextUpcomingObservance?.gregorianDate ?: java.time.LocalDate.now(),
            location = result.personRecord.location,
            language = currentLanguage,
            onDismiss = { isPanchaKalaGuideOpen = false }
        )
    }

    // Educational Ceremony Info Dialog
    if (selectedCeremonyInfo != null) {
        CeremonyDetailDialog(
            info = selectedCeremonyInfo!!,
            language = currentLanguage,
            onDismiss = { selectedCeremonyInfo = null }
        )
    }

    // Ekadashi Shastric Guidance Dialog
    if (selectedEkadashiEvent != null) {
        EkadashiGuidanceDialog(
            ekadashiDate = selectedEkadashiEvent!!.gregorianDate,
            dvadashiDate = selectedEkadashiEvent!!.dvadashiDate,
            language = currentLanguage,
            onDismiss = { selectedEkadashiEvent = null }
        )
    }
}


@Composable
private fun DemiseSummaryCard(
    result: ShraddhaCalculationResult,
    language: AppLanguage,
    onOpenPanchaKalaGuide: () -> Unit
) {
    val person = result.personRecord
    val localizedName = PanchangaLocalizer.localizePersonName(person.name, language)
    val localizedLocation = PanchangaLocalizer.localizeLocation(person.location.displayName, language)
    val fullPanchanga = PanchangaLocalizer.localizeFullPanchanga(result.mrutaTithi, language)
    val localizedRelationship = PanchangaLocalizer.localizeRelationship(person.relationship, language)
    val localizedTradition = PanchangaLocalizer.localizeTradition(person.tradition, language)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = localizedName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                    Text(
                        text = "$localizedRelationship • $localizedTradition",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimarySaffron.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = stringResource(R.string.tithi),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = CardBorder.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(stringResource(R.string.date_time_demise_label), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = "${person.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))} at ${person.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(stringResource(R.string.location_label), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(localizedLocation, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = SurfaceBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(stringResource(R.string.mruta_tithi_at_demise), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(fullPanchanga, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = PrimarySaffronDark)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onOpenPanchaKalaGuide,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "📖 " + stringResource(R.string.pancha_kala_guide_btn),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                }
            }
        }
    }
}

@Composable
private fun DoshaStatusCard(doshaResult: DoshaEvaluationResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (doshaResult.hasDosha) Color(0xFFFFF8E7) else Color(0xFFF1F8F1)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (doshaResult.hasDosha) PrimarySaffron.copy(alpha = 0.4f) else Color(0xFF81C784)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (doshaResult.hasDosha) stringResource(R.string.dosha_alert) else stringResource(R.string.dosha_clear),
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                    fontWeight = FontWeight.Bold,
                    color = if (doshaResult.hasDosha) Color(0xFFB75500) else Color(0xFF2E7D32)
                )
            }

            if (doshaResult.hasDosha) {
                doshaResult.doshas.forEach { dosha ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(dosha.title, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(dosha.conditionDescription, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = TextSecondary)
                        Text("${stringResource(R.string.remedy_label)} ${dosha.prescribedRemedy}", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp), color = TextPrimary)
                        Text("${stringResource(R.string.source_label)} ${dosha.scripturalSource}", style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp), color = TextSecondary)
                    }
                }
                Text(
                    text = "${stringResource(R.string.dosha_header)}: ${doshaResult.generalAdvice}",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = PrimarySaffronDark
                )
            } else {
                Text(
                    text = stringResource(R.string.demise_no_dosha_desc),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = Color(0xFF388E3C)
                )
            }
        }
    }
}

@Composable
private fun NextUpcomingObservanceCard(
    event: ShraddhaEvent,
    personRecord: PersonDeathRecord,
    language: AppLanguage,
    isCalendarScheduled: Boolean,
    onToggleCalendar: () -> Unit,
    onViewTrace: () -> Unit,
    onViewInfo: () -> Unit,
    onViewEkadashiGuidance: (() -> Unit)? = null
) {
    val daysRemaining = ChronoUnit.DAYS.between(java.time.LocalDate.now(), event.gregorianDate)
    val formattedDate = event.gregorianDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
    val localizedTithi = PanchangaLocalizer.localizeFullPanchanga(event.sunrisePanchanga, language)
    val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, PrimarySaffron, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PrimarySaffronDark
                ) {
                    Text(
                        text = "🌟 " + stringResource(R.string.next_upcoming_observance).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        fontWeight = FontWeight.Bold,
                        color = SurfaceCard,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                if (daysRemaining >= 0) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrimarySaffron.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = PanchangaLocalizer.localizeDaysRemaining(daysRemaining, language),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Text(
                text = localizedTraditionalName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PrimarySaffronDark
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            if (event.isEkadashiObservance) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1B5E20).copy(alpha = 0.10f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "🌿 " + PanchangaLocalizer.localizeEkadashiShiftNote(language),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20),
                        fontSize = 11.sp
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${stringResource(R.string.aparahna_window)}: ${event.kalaDetails.aparahnaStart.format(DateTimeFormatter.ofPattern("hh:mm a"))} — ${event.kalaDetails.aparahnaEnd.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
            }

            Text(
                text = "${stringResource(R.string.tithi)}: $localizedTithi",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )

            val timingAnalysis = remember(event, personRecord.location, language) {
                TimingExplanationGenerator.generateAnalysis(event, personRecord.location, language)
            }
            if (timingAnalysis.isSunriseDifferentFromRitual) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PrimarySaffron.copy(alpha = 0.12f),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "🌅 ${stringResource(R.string.sunrise_tithi_label)}: ${timingAnalysis.sunriseTithi.name} ➔ 🕒 ${stringResource(R.string.aparahna_tithi_label)}: ${timingAnalysis.targetTithi.name}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark,
                        fontSize = 11.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(onClick = onViewInfo, contentPadding = PaddingValues(0.dp)) {
                        Text("ℹ️ " + stringResource(R.string.scriptural_significance), fontSize = 11.sp, color = PrimarySaffronDark, fontWeight = FontWeight.Bold)
                    }
                    TextButton(onClick = onViewTrace, contentPadding = PaddingValues(0.dp)) {
                        Text("🔍 " + stringResource(R.string.view_trace_btn), fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    }
                    if (event.isEkadashiObservance && onViewEkadashiGuidance != null) {
                        TextButton(onClick = onViewEkadashiGuidance, contentPadding = PaddingValues(0.dp)) {
                            Text("🌿 " + PanchangaLocalizer.localizeEkadashiButton(language), fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                IconButton(onClick = onToggleCalendar) {
                    Icon(
                        imageVector = if (isCalendarScheduled) Icons.Default.NotificationsActive else Icons.Default.NotificationAdd,
                        contentDescription = null,
                        tint = if (isCalendarScheduled) PrimarySaffron else TextSecondary
                    )
                }
            }
        }
    }
}


@Composable
private fun MasterActionsCard(
    isSaved: Boolean,
    isAllCalendarActive: Boolean,
    onToggleSave: () -> Unit,
    onToggleAllCalendar: () -> Unit,
    showCalendarOption: Boolean = true
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Save Profile Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleSave() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = if (isSaved) PrimarySaffron else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isSaved) stringResource(R.string.saved_badge) else stringResource(R.string.save_profile_btn),
                            style = MaterialTheme.typography.bodyMedium,
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
                    colors = shraddhaSwitchColors()
                )
            }

            if (showCalendarOption) {
                HorizontalDivider(color = CardBorder.copy(alpha = 0.5f))

                // Calendar Reminders Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleAllCalendar() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isAllCalendarActive) Icons.Default.NotificationsActive else Icons.Default.NotificationAdd,
                            contentDescription = null,
                            tint = if (isAllCalendarActive) PrimarySaffron else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.add_all_to_calendar),
                                style = MaterialTheme.typography.bodyMedium,
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
                        onCheckedChange = { onToggleAllCalendar() },
                        colors = shraddhaSwitchColors()
                    )
                }
            }
        }
    }
}

@Composable
private fun YearlyObservanceAccordion(
    group: YearlyObservanceGroup,
    personRecord: PersonDeathRecord,
    language: AppLanguage,
    activeCalendarEntities: Set<String>,
    initialExpanded: Boolean = false,
    onToggleCalendar: (ShraddhaEvent) -> Unit,
    onViewTrace: (ShraddhaEvent) -> Unit,
    onViewInfo: (ShraddhaEvent) -> Unit,
    onViewEkadashiGuidance: ((ShraddhaEvent) -> Unit)? = null,
    onExportYearPdf: (Int) -> Unit = {}
) {
    var isExpanded by remember { mutableStateOf(initialExpanded) }
    var isMasikasExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = PanchangaLocalizer.localizeYearTitleString(group.yearTitle, language),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = PanchangaLocalizer.localizeSamvatsara(group.varshikaEvent.tithi.samvatsara, language),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = { onExportYearPdf(group.yearIndex) },
                        modifier = Modifier.size(36.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = PrimarySaffron.copy(alpha = 0.12f),
                            contentColor = PrimarySaffronDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = stringResource(R.string.export_year_pdf, group.yearIndex),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                            tint = PrimarySaffronDark
                        )
                    }
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    HorizontalDivider(color = CardBorder.copy(alpha = 0.5f))

                    // Year 1: Masikas Drill-down
                    if (group.yearIndex == 1 && group.masikas.isNotEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, if (isMasikasExpanded) PrimarySaffron else CardBorder, RoundedCornerShape(12.dp)),
                            color = if (isMasikasExpanded) SurfaceCardVariant else SurfaceBackground
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { isMasikasExpanded = !isMasikasExpanded },
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = PrimarySaffron,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "${stringResource(R.string.tab_masikas)} (${group.masikas.size})",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isMasikasExpanded) PrimarySaffronDark else TextPrimary,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    FilledTonalIconButton(
                                        onClick = { isMasikasExpanded = !isMasikasExpanded },
                                        modifier = Modifier.size(32.dp),
                                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                                            containerColor = if (isMasikasExpanded) PrimarySaffron else PrimarySaffron.copy(alpha = 0.12f),
                                            contentColor = if (isMasikasExpanded) Color.White else PrimarySaffronDark
                                        )
                                    ) {
                                        Icon(
                                            imageVector = if (isMasikasExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = if (isMasikasExpanded) stringResource(R.string.collapse) else stringResource(R.string.expand),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                AnimatedVisibility(visible = isMasikasExpanded) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 10.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        group.masikas.forEach { masikaEvent ->
                                            EventItemRow(
                                                event = masikaEvent,
                                                personRecord = personRecord,
                                                language = language,
                                                isScheduled = activeCalendarEntities.contains(
                                                    "${personRecord.name}_${masikaEvent.gregorianDate}_${masikaEvent.traditionalName}"
                                                ),
                                                onToggleCalendar = { onToggleCalendar(masikaEvent) },
                                                onViewTrace = { onViewTrace(masikaEvent) },
                                                onViewInfo = { onViewInfo(masikaEvent) },
                                                onViewEkadashiGuidance = if (onViewEkadashiGuidance != null) { { onViewEkadashiGuidance(masikaEvent) } } else null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 1. Annual Varshika Shraddha Section
                    SubmenuSection(
                        title = "🪔 " + stringResource(R.string.tab_shraddha),
                        event = group.varshikaEvent,
                        personRecord = personRecord,
                        language = language,
                        isScheduled = activeCalendarEntities.contains(
                            "${personRecord.name}_${group.varshikaEvent.gregorianDate}_${group.varshikaEvent.traditionalName}"
                        ),
                        onToggleCalendar = { onToggleCalendar(group.varshikaEvent) },
                        onViewTrace = { onViewTrace(group.varshikaEvent) },
                        onViewInfo = { onViewInfo(group.varshikaEvent) },
                        onViewEkadashiGuidance = if (onViewEkadashiGuidance != null) { { onViewEkadashiGuidance(group.varshikaEvent) } } else null
                    )

                    // 2. Mahalaya Paksha Section
                    if (group.pakshaEvent != null) {
                        SubmenuSection(
                            title = "🍂 " + stringResource(R.string.tab_paksha),
                            event = group.pakshaEvent,
                            personRecord = personRecord,
                            language = language,
                            isScheduled = activeCalendarEntities.contains(
                                "${personRecord.name}_${group.pakshaEvent.gregorianDate}_${group.pakshaEvent.traditionalName}"
                            ),
                            onToggleCalendar = { onToggleCalendar(group.pakshaEvent) },
                            onViewTrace = { onViewTrace(group.pakshaEvent) },
                            onViewInfo = { onViewInfo(group.pakshaEvent) },
                            onViewEkadashiGuidance = if (onViewEkadashiGuidance != null) { { onViewEkadashiGuidance(group.pakshaEvent) } } else null
                        )
                    } else if (group.pakshaNotApplicableReason != null) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = SurfaceBackground
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "🍂 " + stringResource(R.string.tab_paksha),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = stringResource(R.string.paksha_not_applicable_y1),
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
private fun SubmenuSection(
    title: String,
    event: ShraddhaEvent,
    personRecord: PersonDeathRecord,
    language: AppLanguage,
    isScheduled: Boolean,
    onToggleCalendar: () -> Unit,
    onViewTrace: () -> Unit,
    onViewInfo: () -> Unit,
    onViewEkadashiGuidance: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp)),
        color = SurfaceCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimarySaffronDark
                )

                IconButton(onClick = onToggleCalendar, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = if (isScheduled) Icons.Default.NotificationsActive else Icons.Default.NotificationAdd,
                        contentDescription = null,
                        tint = if (isScheduled) PrimarySaffron else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            EventItemRow(
                event = event,
                personRecord = personRecord,
                language = language,
                isScheduled = isScheduled,
                onToggleCalendar = onToggleCalendar,
                onViewTrace = onViewTrace,
                onViewInfo = onViewInfo,
                onViewEkadashiGuidance = onViewEkadashiGuidance
            )
        }
    }
}

@Composable
private fun EventItemRow(
    event: ShraddhaEvent,
    personRecord: PersonDeathRecord,
    language: AppLanguage,
    isScheduled: Boolean,
    onToggleCalendar: () -> Unit,
    onViewTrace: () -> Unit,
    onViewInfo: () -> Unit,
    onViewEkadashiGuidance: (() -> Unit)? = null
) {
    val formattedDate = event.gregorianDate.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"))
    val localizedTithi = PanchangaLocalizer.localizeFullPanchanga(event.sunrisePanchanga, language)
    val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceBackground, RoundedCornerShape(8.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = localizedTraditionalName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "📅 $formattedDate",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = PrimarySaffronDark
            )
            Text(
                text = "🕒 ${event.kalaDetails.aparahnaStart.format(DateTimeFormatter.ofPattern("hh:mm a"))} - ${event.kalaDetails.aparahnaEnd.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }

        if (event.isEkadashiObservance) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF1B5E20).copy(alpha = 0.10f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = "🌿 " + PanchangaLocalizer.localizeEkadashiShiftNote(language),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20),
                    fontSize = 10.5.sp
                )
            }
        }

        Text(
            text = localizedTithi,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )

        val timingAnalysis = remember(event, personRecord.location, language) {
            TimingExplanationGenerator.generateAnalysis(event, personRecord.location, language)
        }
        if (timingAnalysis.isSunriseDifferentFromRitual) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = PrimarySaffron.copy(alpha = 0.12f),
                modifier = Modifier.padding(top = 2.dp)
            ) {
                Text(
                    text = "🌅 ${stringResource(R.string.sunrise_tithi_label)}: ${timingAnalysis.sunriseTithi.name} ➔ 🕒 ${stringResource(R.string.aparahna_tithi_label)}: ${timingAnalysis.targetTithi.name}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimarySaffronDark,
                    fontSize = 10.5.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (event.isEkadashiObservance && onViewEkadashiGuidance != null) {
                TextButton(onClick = onViewEkadashiGuidance, contentPadding = PaddingValues(horizontal = 4.dp)) {
                    Text("🌿 " + PanchangaLocalizer.localizeEkadashiButton(language), fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
            TextButton(onClick = onViewInfo, contentPadding = PaddingValues(horizontal = 4.dp)) {
                Text("ℹ️ " + stringResource(R.string.info_btn), fontSize = 11.sp, color = PrimarySaffronDark, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = onViewTrace, contentPadding = PaddingValues(horizontal = 4.dp)) {
                Text("🔍 " + stringResource(R.string.view_trace_btn), fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
private fun TraceDetailDialog(
    event: ShraddhaEvent,
    location: GeoLocation,
    language: AppLanguage,
    onOpenPanchaKalaGuide: () -> Unit,
    onDismiss: () -> Unit
) {
    val analysis = remember(event, location, language) {
        TimingExplanationGenerator.generateAnalysis(event, location, language)
    }
    val localizedTraditionalName = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
    val localizedDay = PanchangaLocalizer.localizeDayOfWeek(event.dayOfWeek, language)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.trace_dialog_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = "$localizedTraditionalName (${event.gregorianDate} - $localizedDay)",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = CardBorder.copy(alpha = 0.6f))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 1. Astronomical Timings Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "☀️ " + stringResource(R.string.local_astronomical_timings),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = "• ${stringResource(R.string.sunrise)}: ${analysis.sunriseTime} | ${stringResource(R.string.sunset)}: ${analysis.sunsetTime}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                            Text(
                                text = "• ${stringResource(R.string.aparahna_window)}: ${analysis.aparahnaStart} — ${analysis.aparahnaEnd}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = "• ${stringResource(R.string.kutapa_muhurta_8th)}: ${analysis.kutapaStart} — ${analysis.kutapaEnd}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }

                    // 2. Tithi & Transition Analysis Card
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "🌙 " + stringResource(R.string.tithi) + " & " + stringResource(R.string.panchanga_determination_trace),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = "• ${stringResource(R.string.sunrise_tithi_label)}: ${analysis.sunriseTithi.name} (${analysis.sunriseTithi.paksha.displayName})",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary
                            )
                            Text(
                                text = "• ${stringResource(R.string.aparahna_tithi_label)}: ${analysis.aparahnaTithi.name} (${analysis.aparahnaTithi.paksha.displayName}) [${analysis.targetOverlapMinutes} mins]",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            if (analysis.isSunriseDifferentFromRitual) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = PrimarySaffron.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "ℹ️ ${stringResource(R.string.sunrise_tithi_label)} (${analysis.sunriseTithi.name}) ≠ ${stringResource(R.string.aparahna_tithi_label)} (${analysis.aparahnaTithi.name})",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PrimarySaffronDark,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // 3. Canonical Scriptural Injunction Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceCardVariant,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimarySaffron.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "📜 " + stringResource(R.string.canonical_injunction_title),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = analysis.canonicalProhibitionShloka,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = analysis.canonicalProhibitionMeaning,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 4. Layperson Q&A Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceBackground,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "💡 " + stringResource(R.string.layperson_qa_title),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = analysis.whyThisDateExplanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                            HorizontalDivider(color = CardBorder.copy(alpha = 0.4f))
                            Text(
                                text = analysis.whyNotMorningExplanation,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 5. Link to 5 Kalas Guide
                    OutlinedButton(
                        onClick = onOpenPanchaKalaGuide,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimarySaffronDark)
                    ) {
                        Text(
                            text = "📖 " + stringResource(R.string.explore_pancha_kalas),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(stringResource(R.string.close), color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

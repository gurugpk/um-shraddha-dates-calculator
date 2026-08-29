package com.shraddhacalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.TempleHindu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.EducationalCeremonyInfo
import com.shraddhacalendar.core.shraddha.EducationalContentLocalizer
import com.shraddhacalendar.ui.theme.*

@Composable
fun CeremonyDetailDialog(
    info: EducationalCeremonyInfo,
    language: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit
) {
    val localizedInfo = EducationalContentLocalizer.getLocalizedInfo(info, language)

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Row with Title & Close Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = localizedInfo.titleEnglish,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = localizedInfo.titleSanskrit,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.close),
                            tint = TextSecondary
                        )
                    }
                }

                HorizontalDivider(color = PrimarySaffron.copy(alpha = 0.25f), thickness = 0.8.dp)

                // Day / Observance Timing Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PrimarySaffron.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🗓️ " + localizedInfo.dayTiming,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            fontWeight = FontWeight.SemiBold,
                            color = PrimarySaffronDark
                        )
                    }
                }

                // 1. Stage in the Soul's Afterlife Journey & Yatana Sharira
                DetailSection(
                    icon = "🚶‍♂️",
                    title = stringResource(R.string.yatana_sharira_section_title),
                    subtitle = localizedInfo.soulJourneyStation,
                    description = if (localizedInfo.pretaConditionAndYatanaDeha.isNotBlank()) {
                        "${localizedInfo.stationDescription}\n\n${localizedInfo.pretaConditionAndYatanaDeha}"
                    } else {
                        localizedInfo.stationDescription
                    }
                )

                // 2. Role of Pinda Pradana & Relief
                if (localizedInfo.pindaSignificanceAndRelief.isNotBlank()) {
                    DetailSection(
                        icon = "🌾",
                        title = stringResource(R.string.pinda_pradana_section_title),
                        subtitle = "",
                        description = localizedInfo.pindaSignificanceAndRelief
                    )
                }

                // 3. Spiritual Impact & Why It Is Needed
                DetailSection(
                    icon = "✨",
                    title = stringResource(R.string.spiritual_impact_title),
                    subtitle = localizedInfo.spiritualSignificance,
                    description = localizedInfo.whyNeeded
                )

                // 4. Classical Sanskrit Verse (if present)
                if (!localizedInfo.classicalVerse.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PrimarySaffron.copy(alpha = 0.07f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PrimarySaffron.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🕉️", fontSize = 13.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.classical_verse_title),
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.5.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = PrimarySaffronDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = localizedInfo.classicalVerse,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 12.sp,
                                    lineHeight = 18.sp,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                                ),
                                fontWeight = FontWeight.Medium,
                                color = PrimarySaffronDark
                            )
                        }
                    }
                }

                // 5. Scriptural Canonical Source
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SurfaceBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = PrimarySaffron,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.canonical_source_title),
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.sp),
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = localizedInfo.scripturalCitation,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextPrimary
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        fontWeight = FontWeight.Bold,
                        color = SurfaceCard
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    icon: String,
    title: String,
    subtitle: String,
    description: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 13.sp),
                fontWeight = FontWeight.Bold,
                color = PrimarySaffronDark
            )
        }

        if (subtitle.isNotBlank()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
        }

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
            color = TextSecondary,
            lineHeight = 16.sp
        )
    }
}

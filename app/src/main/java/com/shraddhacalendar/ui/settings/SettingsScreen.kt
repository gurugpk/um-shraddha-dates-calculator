package com.shraddhacalendar.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.ui.components.TopDedicationBanner
import com.shraddhacalendar.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    selectedTradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA,
    onLanguageSelected: (AppLanguage) -> Unit,
    onTraditionSelected: (MadhwaTradition) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dedicated Top Banner
            TopDedicationBanner(tradition = selectedTradition)

            // 1. Madhwa Lineage / Matha Tradition Selection Card
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PrimarySaffronDark)
                        Text(
                            text = stringResource(R.string.tradition_section),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = stringResource(R.string.tradition_section_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    HorizontalDivider(color = DividerColor)

                    MadhwaTradition.entries.forEach { trad ->
                        val isSelected = trad == selectedTradition
                        val bg = if (isSelected) PrimarySaffron.copy(alpha = 0.08f) else SurfaceCard
                        val border = if (isSelected) PrimarySaffron else CardBorder

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .border(if (isSelected) 1.5.dp else 1.dp, border, RoundedCornerShape(12.dp))
                                .clickable { onTraditionSelected(trad) }
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onTraditionSelected(trad) },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimarySaffronDark)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = PanchangaLocalizer.localizeTradition(trad, currentLanguage),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) PrimarySaffronDark else TextPrimary
                                )
                                Text(
                                    text = trad.guruParamparaName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // 2. Language Selection Card
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = null,
                            tint = PrimarySaffronDark
                        )
                        Text(
                            text = stringResource(R.string.language_section),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Text(
                        text = stringResource(R.string.select_language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    HorizontalDivider(color = DividerColor)

                    // Native Script Language Options
                    val languages = listOf(
                        Triple(AppLanguage.KANNADA, "ಕನ್ನಡ", "Kannada"),
                        Triple(AppLanguage.ENGLISH, "English", "English"),
                        Triple(AppLanguage.TELUGU, "తెలుగు", "Telugu"),
                        Triple(AppLanguage.SANSKRIT, "संस्कृतम्", "Sanskrit (Devanagari)"),
                        Triple(AppLanguage.TAMIL, "தமிழ்", "Tamil")
                    )

                    languages.forEach { (lang, nativeName, englishName) ->
                        val isSelected = lang == currentLanguage
                        val bg = if (isSelected) PrimarySaffron.copy(alpha = 0.08f) else SurfaceCard
                        val border = if (isSelected) PrimarySaffron else CardBorder

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(bg)
                                .border(if (isSelected) 1.5.dp else 1.dp, border, RoundedCornerShape(12.dp))
                                .clickable { onLanguageSelected(lang) }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onLanguageSelected(lang) },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimarySaffronDark)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = nativeName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) PrimarySaffronDark else TextPrimary
                                )
                                Text(
                                    text = englishName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // 3. Calendar Settings Card
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PrimarySaffronDark
                        )
                        Text(
                            text = stringResource(R.string.calendar_section),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    HorizontalDivider(color = DividerColor)

                    Text(
                        text = stringResource(R.string.calendar_reminders_rule),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = stringResource(R.string.calendar_reminders_rule_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            // 4. About & Disclaimers Card
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = PrimarySaffronDark
                        )
                        Text(
                            text = stringResource(R.string.about_section),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    HorizontalDivider(color = DividerColor)

                    Text(
                        text = stringResource(R.string.about_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )

                    Text(
                        text = stringResource(R.string.app_disclaimer),
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = PrimarySaffronDark,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

package com.shraddhacalendar.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
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
import com.shraddhacalendar.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
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
            com.shraddhacalendar.ui.components.TopDedicationBanner()

            // 1. Language Selection Card
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
                    AppLanguage.entries.forEach { lang ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onLanguageSelected(lang) }
                                .padding(vertical = 10.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = lang.nativeDisplayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (lang == currentLanguage) FontWeight.Bold else FontWeight.Normal,
                                color = if (lang == currentLanguage) PrimarySaffronDark else TextPrimary
                            )

                            RadioButton(
                                selected = (lang == currentLanguage),
                                onClick = { onLanguageSelected(lang) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = PrimarySaffronDark,
                                    unselectedColor = TextTertiary
                                )
                            )
                        }
                    }
                }
            }

            // 2. Calendar Settings & Automated Reminders Info Card
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

                    Text(
                        text = stringResource(R.string.calendar_reminders_rule),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCardVariant)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.calendar_reminders_rule_desc),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
                        )
                    }
                }
            }

            // 3. About Section Card
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

                    Text(
                        text = stringResource(R.string.about_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    HorizontalDivider(color = DividerColor)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.version_label),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "1.0.0",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    HorizontalDivider(color = DividerColor)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SurfaceCardVariant)
                            .padding(vertical = 12.dp, horizontal = 14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🕉️ " + stringResource(R.string.invocation_header) + " 🕉️",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                                color = PrimarySaffron.copy(alpha = 0.25f),
                                thickness = 0.8.dp
                            )
                            Text(
                                text = stringResource(R.string.dedication_service),
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                text = "🌸 " + stringResource(R.string.dedication_father) + " 🌸",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                text = stringResource(R.string.developed_by),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

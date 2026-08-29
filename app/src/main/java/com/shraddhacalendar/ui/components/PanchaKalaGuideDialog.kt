package com.shraddhacalendar.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.shraddha.*
import com.shraddhacalendar.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PanchaKalaGuideDialog(
    date: LocalDate = LocalDate.now(),
    location: GeoLocation,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val panchaKalas = remember(date, location, language) {
        PanchaKalaRepository.getPanchaKalas(date, location, language)
    }

    val devaPujaGuide = remember(language) {
        PanchaKalaRepository.getKartruDevaPujaGuide(language)
    }

    var isDevaPujaExpanded by remember { mutableStateOf(true) }

    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    val dialogTitle = when (language) {
        AppLanguage.KANNADA -> "ದಿನದ ೫ ಪವಿತ್ರ ಕಾಲಗಳು (ಪಂಚ ಕಾಲಗಳು)"
        AppLanguage.SANSKRIT -> "दिनस्य पञ्च पवित्रकालाः (पञ्चकालाः)"
        AppLanguage.TELUGU -> "దినంలోని 5 పవిత్ర కాలాలు (పంచ కాలాలు)"
        AppLanguage.TAMIL -> "நாளின் 5 புனித காலங்கள் (பஞ்ச காலங்கள்)"
        AppLanguage.ENGLISH -> "The 5 Sacred Divisions of the Day (Pancha Kalas)"
    }

    val dialogSubtitle = when (language) {
        AppLanguage.KANNADA -> "ದಿನಮಾನವನ್ನು (ಸೂರ್ಯೋದಯದಿಂದ ಸೂರ್ಯಾಸ್ತ) ೫ ಸಮಾನ ಭಾಗಗಳಾಗಿ ವಿಂಗಡಿಸಿದ ಶಾಸ್ತ್ರೀಯ ಕಾಲ ವಿಭಾಗ"
        AppLanguage.SANSKRIT -> "सूर्योदयात् सूर्यास्तपर्यन्तं पञ्चधा विभक्तः शास्त्रसम्मतः कालविभागः"
        AppLanguage.TELUGU -> "సూర్యోదయం నుండి సూర్యాస్తమయం వరకు గల దినమానాన్ని 5 భాగాలుగా విభజించిన శాస్త్ర కాల విభాగం"
        AppLanguage.TAMIL -> "சூரியோதயம் முதல் அஸ்தமனம் வரை 5 சம பிரிவுகளாக வகுக்கப்பட்ட சாஸ்திர கால பிரிவு"
        AppLanguage.ENGLISH -> "Dinamana (sunrise to sunset) divided into 5 equal parts according to Dharma Shastra"
    }

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
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = PrimarySaffronDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = dialogTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark
                            )
                            Text(
                                text = dialogSubtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = CardBorder.copy(alpha = 0.6f))

                // Scrollable Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 5 Kala Cards
                    panchaKalas.forEach { item ->
                        KalaCardView(item = item, timeFormatter = timeFormatter, language = language)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Deva Puja Injunction on Shraddha Days Section
                    KartruDevaPujaSection(
                        guide = devaPujaGuide,
                        isExpanded = isDevaPujaExpanded,
                        onToggleExpand = { isDevaPujaExpanded = !isDevaPujaExpanded },
                        language = language
                    )
                }

                // Close Button
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun KalaCardView(
    item: PanchaKalaItem,
    timeFormatter: DateTimeFormatter,
    language: AppLanguage
) {
    val startStr = item.startTime.format(timeFormatter)
    val endStr = item.endTime.format(timeFormatter)

    val cardBg = if (item.isSacredAncestralWindow) {
        PrimarySaffron.copy(alpha = 0.08f)
    } else {
        SurfaceBackground
    }

    val borderColor = if (item.isSacredAncestralWindow) {
        PrimarySaffronDark
    } else {
        CardBorder.copy(alpha = 0.5f)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = cardBg,
        border = androidx.compose.foundation.BorderStroke(if (item.isSacredAncestralWindow) 1.5.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isSacredAncestralWindow) PrimarySaffronDark else TextPrimary
                    )
                    Text(
                        text = item.divisionLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (item.isSacredAncestralWindow) PrimarySaffron else PrimarySaffron.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "🕒 $startStr - $endStr",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isSacredAncestralWindow) Color.White else PrimarySaffronDark,
                        fontSize = 11.sp
                    )
                }
            }

            if (item.isSacredAncestralWindow) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = PrimarySaffronDark
                ) {
                    Text(
                        text = when (language) {
                            AppLanguage.KANNADA -> "👑 ಮುಖ್ಯ ಪಿತೃ ಶ್ರಾದ್ಧ ಕಾಲ (ವಾರ್ಷಿಕ / ಪಕ್ಷ ಶ್ರಾದ್ಧ ಕಡ್ಡಾಯ)"
                            AppLanguage.SANSKRIT -> "👑 मुख्यः पितृश्राद्धकालः (अपराह्नव्याप्तिः)"
                            AppLanguage.TELUGU -> "👑 ప్రధాన పితృ శ్రాద్ధ కాలం (వార్షిక / పక్ష శ్రాద్ధం)"
                            AppLanguage.TAMIL -> "👑 முதன்மை பித்ரு சிராத்த காலம்"
                            AppLanguage.ENGLISH -> "👑 Exclusive Sacred Pitru Shraddha Window"
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 10.5.sp
                    )
                }
            }

            // Shloka Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = SurfaceCard
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = item.shlokaNativeScript,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimarySaffronDark,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.meaning,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            // Prescribed Duties
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = when (language) {
                        AppLanguage.KANNADA -> "✅ ವಿಹಿತ ಕಾರ್ಯಗಳು (ಮಾಡಬೇಕಾದ ಕರ್ತವ್ಯಗಳು):"
                        AppLanguage.SANSKRIT -> "✅ विहितानि कर्माणि:"
                        AppLanguage.TELUGU -> "✅ విహిత కార్యాలు (ఆచరించవలసినవి):"
                        AppLanguage.TAMIL -> "✅ செய்ய வேண்டிய காரியங்கள்:"
                        AppLanguage.ENGLISH -> "✅ Prescribed Duties (Vihita Kāryas):"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentGreen
                )
                item.prescribedDuties.forEach { duty ->
                    Text(
                        text = "• $duty",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.5.sp
                    )
                }
            }

            // Prohibited Duties
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = when (language) {
                        AppLanguage.KANNADA -> "❌ ನಿಷಿದ್ಧ ಕಾರ್ಯಗಳು:"
                        AppLanguage.SANSKRIT -> "❌ निषिद्धानि कर्माणि:"
                        AppLanguage.TELUGU -> "❌ నిషిద్ధ కార్యాలు:"
                        AppLanguage.TAMIL -> "❌ செய்யக்கூடாதவை:"
                        AppLanguage.ENGLISH -> "❌ Prohibited Actions (Niṣiddha):"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentMaroon
                )
                item.prohibitedDuties.forEach { nishiddha ->
                    Text(
                        text = "• $nishiddha",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 11.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun KartruDevaPujaSection(
    guide: KartruDevaPujaGuide,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    language: AppLanguage
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCardVariant,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimarySaffron)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Toggle Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = when (language) {
                            AppLanguage.KANNADA -> "🪔 ಶ್ರಾದ್ಧ ದಿನದಂದು ಕರ್ತೃವಿಗೆ ದೇವಪೂಜಾ ನಿಯಮ"
                            AppLanguage.SANSKRIT -> "🪔 श्राद्धदिने कर्तुः देवपूजाविधिः"
                            AppLanguage.TELUGU -> "🪔 శ్రాద్ధ దినాన కర్తకు దేవపూజా నియమం"
                            AppLanguage.TAMIL -> "🪔 சிராத்த தினத்தில் கர்த்தாவுக்கு தேவபூஜை விதி"
                            AppLanguage.ENGLISH -> "🪔 Injunction on Deva Pūjā for Kartru on Śrāddha Day"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                    Text(
                        text = "Dharma Sindhu & Nirnaya Sindhu Vidhi",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                FilledTonalIconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = PrimarySaffron,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Shloka Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceCard
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = guide.canonicalShlokaNative,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = PrimarySaffronDark,
                                lineHeight = 18.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = guide.shlokaMeaning,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Philosophical Intro
                    Text(
                        text = guide.philosophicalRationaleIntro,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    // 3 Rationales
                    Text(
                        text = when (language) {
                            AppLanguage.KANNADA -> "ಶಾಸ್ತ್ರೀಯ ಮತ್ತು ತತ್ತ್ವಜ್ಞಾನದ ೩ ಮುಖ್ಯ ಕಾರಣಗಳು:"
                            AppLanguage.SANSKRIT -> "शास्त्रसम्मतत्रिविधहेतवः:"
                            AppLanguage.TELUGU -> "శాస్త్ర మరియు తాత్త్విక 3 ముఖ్య కారణాలు:"
                            AppLanguage.TAMIL -> "சாஸ்திரபூர்வமான 3 முக்கிய காரணங்கள்:"
                            AppLanguage.ENGLISH -> "Three Spiritual & Philosophical Reasons:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )

                    guide.rationales.forEach { rationale ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceBackground
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = rationale.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = rationale.subtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimarySaffronDark,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = rationale.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // 3 Canonical Options
                    Text(
                        text = when (language) {
                            AppLanguage.KANNADA -> "ಧರ್ಮಸಿಂಧು ಸೂಚಿಸಿದ ೩ ಶಾಸ್ತ್ರೋಕ್ತ ಪರಿಹಾರಗಳು:"
                            AppLanguage.SANSKRIT -> "धर्मसिन्धूक्ताः त्रयः विकल्पाः:"
                            AppLanguage.TELUGU -> "ధర్మసింధు నిర్దేశించిన 3 శాస్త్రోక్త పరిష్కారాలు:"
                            AppLanguage.TAMIL -> "தர்மசிந்து கூறும் 3 சாஸ்திர வழிகள்:"
                            AppLanguage.ENGLISH -> "Three Canonical Options from Dharma Sindhu:"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )

                    guide.canonicalOptions.forEach { opt ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            color = SurfaceCard,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = opt.title,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimarySaffronDark
                                )
                                Text(
                                    text = opt.ruleSubtitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = opt.practicalPractice,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    fontSize = 11.5.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

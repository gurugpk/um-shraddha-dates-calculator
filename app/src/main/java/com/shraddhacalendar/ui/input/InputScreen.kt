package com.shraddhacalendar.ui.input

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.DemiseCircumstance
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.PersonDemiseStatus
import com.shraddhacalendar.core.shraddha.ShastricCircumstanceRepository
import com.shraddhacalendar.ui.components.LocationPickerSheet
import com.shraddhacalendar.ui.components.TopDedicationBanner
import com.shraddhacalendar.ui.theme.*
import com.shraddhacalendar.ui.viewmodel.ShraddhaUiState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    uiState: ShraddhaUiState,
    onPersonNameChange: (String) -> Unit,
    onRelationshipChange: (FamilyRelationship) -> Unit,
    onDeathDateChange: (LocalDate) -> Unit,
    onDeathTimeChange: (LocalTime) -> Unit,
    onLocationChange: (GeoLocation) -> Unit,
    onTraditionChange: (MadhwaTradition) -> Unit,
    onDemiseStatusChange: (PersonDemiseStatus) -> Unit,
    onDemiseCircumstanceChange: (DemiseCircumstance) -> Unit,
    onLastSeenDateChange: (LocalDate?) -> Unit,
    onAgeAtDisappearanceChange: (Int?) -> Unit,
    onCalculateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showLastSeenDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showLocationSheet by remember { mutableStateOf(false) }
    var isRelationshipExpanded by remember { mutableStateOf(false) }
    var isTraditionExpanded by remember { mutableStateOf(false) }
    var isCircumstanceExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = stringResource(R.string.app_subtitle),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(2.dp))

            // Dedicated Top Banner
            TopDedicationBanner(tradition = uiState.selectedTradition)

            // Tradition Selector Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.tradition_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    ExposedDropdownMenuBox(
                        expanded = isTraditionExpanded,
                        onExpandedChange = {
                            isTraditionExpanded = it
                            if (it) {
                                isRelationshipExpanded = false
                                isCircumstanceExpanded = false
                            }
                        }
                    ) {
                        OutlinedTextField(
                            value = PanchangaLocalizer.localizeTradition(uiState.selectedTradition, uiState.currentLanguage),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTraditionExpanded) },
                            leadingIcon = { Icon(Icons.Default.AccountBalance, contentDescription = null, tint = PrimarySaffron) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(10.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isTraditionExpanded,
                            onDismissRequest = { isTraditionExpanded = false }
                        ) {
                            MadhwaTradition.entries.forEach { trad ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(PanchangaLocalizer.localizeTradition(trad, uiState.currentLanguage), fontWeight = FontWeight.Bold)
                                            Text(trad.guruParamparaName, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        onTraditionChange(trad)
                                        isTraditionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Status Selector Card (Confirmed Demise vs Missing / Death Unconfirmed)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.demise_status_label),
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = uiState.demiseStatus == PersonDemiseStatus.CONFIRMED_DEMISE,
                            onClick = { onDemiseStatusChange(PersonDemiseStatus.CONFIRMED_DEMISE) },
                            label = {
                                Text(
                                    stringResource(R.string.status_confirmed_demise),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            leadingIcon = if (uiState.demiseStatus == PersonDemiseStatus.CONFIRMED_DEMISE) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimarySaffron.copy(alpha = 0.18f),
                                selectedLabelColor = PrimarySaffronDark
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        FilterChip(
                            selected = uiState.demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED,
                            onClick = { onDemiseStatusChange(PersonDemiseStatus.MISSING_UNCONFIRMED) },
                            label = {
                                Text(
                                    stringResource(R.string.status_missing_unconfirmed),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            leadingIcon = if (uiState.demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFFFFF3E0),
                                selectedLabelColor = Color(0xFFE65100)
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Main Input Card
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
                    Text(
                        text = if (uiState.demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED) {
                            stringResource(R.string.status_missing_unconfirmed)
                        } else {
                            stringResource(R.string.date_time_demise_label)
                        },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    // 1. Person Name
                    OutlinedTextField(
                        value = uiState.personName,
                        onValueChange = onPersonNameChange,
                        label = { Text(stringResource(R.string.person_name_label)) },
                        placeholder = { Text(stringResource(R.string.person_name_hint)) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimarySaffron) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.validationError != null && uiState.personName.isBlank()
                    )

                    // 2. Relationship Dropdown
                    ExposedDropdownMenuBox(
                        expanded = isRelationshipExpanded,
                        onExpandedChange = {
                            isRelationshipExpanded = it
                            if (it) {
                                isTraditionExpanded = false
                                isCircumstanceExpanded = false
                            }
                        }
                    ) {
                        OutlinedTextField(
                            value = PanchangaLocalizer.localizeRelationship(uiState.relationship, uiState.currentLanguage),
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.relationship_label)) },
                            leadingIcon = { Icon(Icons.Default.People, contentDescription = null, tint = PrimarySaffron) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRelationshipExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = isRelationshipExpanded,
                            onDismissRequest = { isRelationshipExpanded = false }
                        ) {
                            FamilyRelationship.entries.forEach { rel ->
                                DropdownMenuItem(
                                    text = { Text(PanchangaLocalizer.localizeRelationship(rel, uiState.currentLanguage)) },
                                    onClick = {
                                        onRelationshipChange(rel)
                                        isRelationshipExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    if (uiState.demiseStatus == PersonDemiseStatus.CONFIRMED_DEMISE) {
                        // 3. Circumstance / Type of Demise Dropdown
                        val circumstanceGuidance = ShastricCircumstanceRepository.getGuidance(
                            uiState.demiseCircumstance,
                            uiState.currentLanguage,
                            uiState.selectedTradition
                        )

                        ExposedDropdownMenuBox(
                            expanded = isCircumstanceExpanded,
                            onExpandedChange = {
                                isCircumstanceExpanded = it
                                if (it) {
                                    isTraditionExpanded = false
                                    isRelationshipExpanded = false
                                }
                            }
                        ) {
                            OutlinedTextField(
                                value = "${circumstanceGuidance.localizedName} (${circumstanceGuidance.sanskritTermLocalScript})",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text(stringResource(R.string.demise_circumstance_label)) },
                                leadingIcon = { Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = PrimarySaffron) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCircumstanceExpanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp)
                            )
                            ExposedDropdownMenu(
                                expanded = isCircumstanceExpanded,
                                onDismissRequest = { isCircumstanceExpanded = false }
                            ) {
                                DemiseCircumstance.entries.forEach { circ ->
                                    val g = ShastricCircumstanceRepository.getGuidance(
                                        circ,
                                        uiState.currentLanguage,
                                        uiState.selectedTradition
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Column {
                                                Text(
                                                    text = g.localizedName,
                                                    fontWeight = if (circ == uiState.demiseCircumstance) FontWeight.Bold else FontWeight.Normal
                                                )
                                                Text(
                                                    text = g.sanskritTermLocalScript,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = TextSecondary
                                                )
                                            }
                                        },
                                        onClick = {
                                            onDemiseCircumstanceChange(circ)
                                            isCircumstanceExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // 4. Date of Death
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.date_of_death_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                    .clickable { showDatePicker = true }
                                    .padding(horizontal = 14.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = uiState.deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }

                        // 5. Exact Time of Death
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.time_of_death_label),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = stringResource(R.string.tithi),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = PrimarySaffronDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                    .clickable { showTimePicker = true }
                                    .padding(horizontal = 14.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Schedule, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = uiState.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    } else {
                        // Missing Person Inputs
                        // Date Last Seen
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = stringResource(R.string.last_seen_date_label),
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                    .clickable { showLastSeenDatePicker = true }
                                    .padding(horizontal = 14.dp, vertical = 13.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = uiState.lastSeenDate?.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) ?: "Tap to select date",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (uiState.lastSeenDate != null) TextPrimary else TextSecondary
                                )
                            }
                        }

                        // Age at Disappearance
                        OutlinedTextField(
                            value = uiState.ageAtDisappearance?.toString() ?: "",
                            onValueChange = { str ->
                                val intVal = str.filter { it.isDigit() }.toIntOrNull()
                                onAgeAtDisappearanceChange(intVal)
                            },
                            label = { Text(stringResource(R.string.age_at_disappearance_label)) },
                            placeholder = { Text("e.g. 45") },
                            leadingIcon = { Icon(Icons.Default.Timelapse, contentDescription = null, tint = Color(0xFFE65100)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Shastric Banner for Missing Person
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF3E0),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFB74D))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = stringResource(R.string.missing_person_banner_text),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFBF360C)
                                )
                            }
                        }
                    }

                    // Location
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.location_label),
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                .clickable { showLocationSheet = true }
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.selectedLocation.city,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${uiState.selectedLocation.state}, ${uiState.selectedLocation.country}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            Text(
                                text = stringResource(R.string.edit_profile),
                                style = MaterialTheme.typography.labelSmall,
                                color = PrimarySaffronDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Validation Error Message
                    if (uiState.validationError != null) {
                        Text(
                            text = uiState.validationError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Calculate / Consult Shastra Button
                    Button(
                        onClick = onCalculateClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron),
                        enabled = !uiState.isCalculating
                    ) {
                        if (uiState.isCalculating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(stringResource(R.string.calculating))
                        } else {
                            Icon(
                                imageVector = if (uiState.demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED) Icons.Default.MenuBook else Icons.Default.Calculate,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (uiState.demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED) {
                                    stringResource(R.string.scriptural_basis_label)
                                } else {
                                    stringResource(R.string.calculate_btn)
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Guidelines Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.input_guidelines_title),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = PrimarySaffronDark
                        )
                        Text(
                            text = stringResource(R.string.input_guidelines_desc),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Universal Disclaimer Footer
            Text(
                text = stringResource(R.string.app_disclaimer),
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                color = TextSecondary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Date Pickers & Time Pickers
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = uiState.deathDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            onDeathDateChange(selectedDate)
                        }
                        showDatePicker = false
                    }) {
                        Text(stringResource(R.string.ok), fontWeight = FontWeight.Bold, color = PrimarySaffron)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(stringResource(R.string.cancel), color = TextSecondary)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showLastSeenDatePicker) {
            val lastSeenState = rememberDatePickerState(
                initialSelectedDateMillis = (uiState.lastSeenDate ?: LocalDate.now()).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
            DatePickerDialog(
                onDismissRequest = { showLastSeenDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        lastSeenState.selectedDateMillis?.let { millis ->
                            val selectedDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                            onLastSeenDateChange(selectedDate)
                        }
                        showLastSeenDatePicker = false
                    }) {
                        Text(stringResource(R.string.ok), fontWeight = FontWeight.Bold, color = PrimarySaffron)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLastSeenDatePicker = false }) {
                        Text(stringResource(R.string.cancel), color = TextSecondary)
                    }
                }
            ) {
                DatePicker(state = lastSeenState)
            }
        }

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = uiState.deathTime.hour,
                initialMinute = uiState.deathTime.minute
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        onDeathTimeChange(LocalTime.of(timePickerState.hour, timePickerState.minute))
                        showTimePicker = false
                    }) {
                        Text(stringResource(R.string.ok), fontWeight = FontWeight.Bold, color = PrimarySaffron)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) {
                        Text(stringResource(R.string.cancel), color = TextSecondary)
                    }
                },
                text = {
                    TimePicker(state = timePickerState)
                }
            )
        }

        if (showLocationSheet) {
            LocationPickerSheet(
                onLocationSelected = { loc ->
                    onLocationChange(loc)
                    showLocationSheet = false
                },
                onDismiss = { showLocationSheet = false }
            )
        }
    }
}

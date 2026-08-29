package com.shraddhacalendar.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.data.local.SavedProfileItem
import com.shraddhacalendar.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSavedProfileDialog(
    profile: SavedProfileItem,
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onDismiss: () -> Unit,
    onSave: (PersonDeathRecord) -> Unit
) {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf(profile.personName) }
    var selectedRelationship by remember { mutableStateOf(FamilyRelationship.fromId(profile.relationship)) }
    var selectedTradition by remember { mutableStateOf(MadhwaTradition.fromId(profile.traditionId)) }
    var deathDate by remember { mutableStateOf(profile.deathDate) }
    var deathTime by remember { mutableStateOf(profile.deathTime) }
    var selectedLocation by remember { mutableStateOf(profile.location) }
    var notes by rememberSaveable { mutableStateOf(profile.notes ?: "") }

    var isRelationshipExpanded by rememberSaveable { mutableStateOf(false) }
    var isTraditionExpanded by rememberSaveable { mutableStateOf(false) }
    var isLocationPickerOpen by rememberSaveable { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.edit_profile_title),
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                        fontWeight = FontWeight.Bold,
                        color = PrimarySaffronDark
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = PrimarySaffron.copy(alpha = 0.2f), thickness = 0.8.dp)

                // 1. Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.person_name_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // 2. Relationship Dropdown
                ExposedDropdownMenuBox(
                    expanded = isRelationshipExpanded,
                    onExpandedChange = { isRelationshipExpanded = it }
                ) {
                    OutlinedTextField(
                        value = PanchangaLocalizer.localizeRelationship(selectedRelationship, currentLanguage),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.relationship_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRelationshipExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isRelationshipExpanded,
                        onDismissRequest = { isRelationshipExpanded = false }
                    ) {
                        FamilyRelationship.entries.forEach { rel ->
                            DropdownMenuItem(
                                text = { Text(PanchangaLocalizer.localizeRelationship(rel, currentLanguage)) },
                                onClick = {
                                    selectedRelationship = rel
                                    isRelationshipExpanded = false
                                }
                            )
                        }
                    }
                }

                // 3. Tradition Dropdown
                ExposedDropdownMenuBox(
                    expanded = isTraditionExpanded,
                    onExpandedChange = { isTraditionExpanded = it }
                ) {
                    OutlinedTextField(
                        value = PanchangaLocalizer.localizeTradition(selectedTradition, currentLanguage),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.tradition_label)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTraditionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isTraditionExpanded,
                        onDismissRequest = { isTraditionExpanded = false }
                    ) {
                        MadhwaTradition.entries.forEach { trad ->
                            DropdownMenuItem(
                                text = { Text(PanchangaLocalizer.localizeTradition(trad, currentLanguage)) },
                                onClick = {
                                    selectedTradition = trad
                                    isTraditionExpanded = false
                                }
                            )
                        }
                    }
                }

                // 4. Date Picker Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .clickable {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    deathDate = LocalDate.of(year, month + 1, dayOfMonth)
                                },
                                deathDate.year,
                                deathDate.monthValue - 1,
                                deathDate.dayOfMonth
                            ).show()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(stringResource(R.string.date_of_death_label), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(deathDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                }

                // 5. Time Picker Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .clickable {
                            TimePickerDialog(
                                context,
                                { _, hourOfDay, minute ->
                                    deathTime = LocalTime.of(hourOfDay, minute)
                                },
                                deathTime.hour,
                                deathTime.minute,
                                false
                            ).show()
                        }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(stringResource(R.string.time_of_death_label), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(deathTime.format(DateTimeFormatter.ofPattern("hh:mm a")), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                }

                // 6. Location Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .clickable { isLocationPickerOpen = true }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimarySaffron, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(stringResource(R.string.location_label), style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        Text(selectedLocation.displayName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    }
                    Text(stringResource(R.string.change_btn), style = MaterialTheme.typography.labelSmall, color = PrimarySaffronDark, fontWeight = FontWeight.Bold)
                }

                // Save Button
                Button(
                    onClick = {
                        val updatedRecord = PersonDeathRecord(
                            id = profile.id,
                            name = name.ifBlank { profile.personName },
                            deathDate = deathDate,
                            deathTime = deathTime,
                            location = selectedLocation,
                            relationship = selectedRelationship,
                            tradition = selectedTradition,
                            notes = notes
                        )
                        onSave(updatedRecord)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffron)
                ) {
                    Text(stringResource(R.string.save_changes), fontWeight = FontWeight.Bold, color = SurfaceCard)
                }
            }
        }
    }

    if (isLocationPickerOpen) {
        LocationPickerSheet(
            onLocationSelected = { loc ->
                selectedLocation = loc
                isLocationPickerOpen = false
            },
            onDismiss = { isLocationPickerOpen = false }
        )
    }
}

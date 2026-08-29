package com.shraddhacalendar.ui.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.data.local.SavedProfileItem
import com.shraddhacalendar.ui.components.EditSavedProfileDialog
import com.shraddhacalendar.ui.components.TopDedicationBanner
import com.shraddhacalendar.ui.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedScreen(
    savedProfiles: List<SavedProfileItem>,
    currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    onSelectProfile: (SavedProfileItem) -> Unit,
    onEditProfile: (SavedProfileItem) -> Unit,
    onSaveEditedProfile: (PersonDeathRecord) -> Unit,
    onDeleteProfile: (Long) -> Unit,
    onNavigateToCalculator: () -> Unit,
    editingProfile: SavedProfileItem? = null,
    onDismissEdit: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var profileToDelete by remember { mutableStateOf<SavedProfileItem?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.saved_title),
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
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Dedicated Top Banner
            TopDedicationBanner()

            if (savedProfiles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(PrimarySaffron.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = PrimarySaffronDark,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = stringResource(R.string.saved_empty),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )

                            Text(
                                text = stringResource(R.string.saved_empty_desc),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = onNavigateToCalculator,
                                colors = ButtonDefaults.buttonColors(containerColor = PrimarySaffronDark),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stringResource(R.string.nav_calculator),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedProfiles, key = { it.id }) { item ->
                        val rel = FamilyRelationship.fromId(item.relationship)
                        val trad = MadhwaTradition.fromId(item.traditionId)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onSelectProfile(item) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // Line 1: Avatar + Person Name + Action Buttons
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(PrimarySaffron.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = PrimarySaffronDark,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }

                                        Text(
                                            text = PanchangaLocalizer.localizePersonName(item.personName, currentLanguage),
                                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                            fontWeight = FontWeight.Bold,
                                            color = PrimarySaffronDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(onClick = { onEditProfile(item) }, modifier = Modifier.size(32.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Edit,
                                                contentDescription = stringResource(R.string.edit_profile),
                                                tint = PrimarySaffronDark,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }

                                        IconButton(onClick = { profileToDelete = item }, modifier = Modifier.size(32.dp)) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = stringResource(R.string.delete),
                                                tint = TextSecondary.copy(alpha = 0.6f),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }

                                // Line 2: Horizontal Relationship & Tradition Badges
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = PrimarySaffron.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = PanchangaLocalizer.localizeRelationship(rel, currentLanguage),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = PrimarySaffronDark,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }

                                    Surface(
                                        color = SurfaceBackground,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = PanchangaLocalizer.localizeTradition(trad, currentLanguage),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextSecondary,
                                            fontWeight = FontWeight.Medium,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                HorizontalDivider(color = CardBorder.copy(alpha = 0.4f))

                                // Line 3: Compact single-row date, time, and location
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = PrimarySaffron,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = "${item.deathDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))} • ${item.deathTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = PrimarySaffron,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = PanchangaLocalizer.localizeLocation(item.location.displayName, currentLanguage),
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                            color = TextSecondary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (profileToDelete != null) {
        AlertDialog(
            onDismissRequest = { profileToDelete = null },
            title = {
                Text(
                    text = stringResource(R.string.remove_profile_confirm_title),
                    fontWeight = FontWeight.Bold,
                    color = PrimarySaffronDark
                )
            },
            text = {
                val name = profileToDelete?.let { PanchangaLocalizer.localizePersonName(it.personName, currentLanguage) } ?: ""
                Text(
                    text = stringResource(R.string.remove_profile_confirm_msg, name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        profileToDelete?.let { onDeleteProfile(it.id) }
                        profileToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.delete), color = SurfaceCard)
                }
            },
            dismissButton = {
                TextButton(onClick = { profileToDelete = null }) {
                    Text(stringResource(R.string.cancel), color = TextSecondary)
                }
            }
        )
    }

    // Edit Profile Dialog
    if (editingProfile != null) {
        EditSavedProfileDialog(
            profile = editingProfile,
            currentLanguage = currentLanguage,
            onDismiss = onDismissEdit,
            onSave = { updated ->
                onSaveEditedProfile(updated)
            }
        )
    }
}

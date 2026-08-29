package com.shraddhacalendar

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shraddhacalendar.core.localization.LocaleManager
import com.shraddhacalendar.ui.components.CalendarPermissionDialog
import com.shraddhacalendar.ui.components.OnboardingDialog
import com.shraddhacalendar.ui.input.InputScreen
import com.shraddhacalendar.ui.recents.RecentsScreen
import com.shraddhacalendar.ui.results.ResultsScreen
import com.shraddhacalendar.ui.saved.SavedScreen
import com.shraddhacalendar.ui.settings.SettingsScreen
import com.shraddhacalendar.ui.theme.*
import com.shraddhacalendar.ui.viewmodel.AppTab
import com.shraddhacalendar.ui.viewmodel.ShraddhaViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val viewModel: ShraddhaViewModel by viewModels()

    override fun attachBaseContext(newBase: Context) {
        val language = LocaleManager.getSavedLanguage(newBase)
        val locale = Locale(language.code)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            val locale = remember(uiState.currentLanguage) {
                Locale(uiState.currentLanguage.code)
            }

            val localizedConfiguration = remember(uiState.currentLanguage) {
                Configuration(resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
            }

            val activityContext = this@MainActivity
            val localizedContext = remember(uiState.currentLanguage) {
                val config = Configuration(activityContext.resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
                activityContext.createConfigurationContext(config)
            }

            LaunchedEffect(uiState.currentLanguage) {
                Locale.setDefault(locale)
                val config = Configuration(resources.configuration).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
                @Suppress("DEPRECATION")
                resources.updateConfiguration(config, resources.displayMetrics)
            }

            var pendingCalendarAction by remember { mutableStateOf<(() -> Unit)?>(null) }
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val writeGranted = permissions[Manifest.permission.WRITE_CALENDAR] == true
                val readGranted = permissions[Manifest.permission.READ_CALENDAR] == true
                if (writeGranted || readGranted) {
                    pendingCalendarAction?.invoke()
                    pendingCalendarAction = null
                    viewModel.syncActiveCalendarEvents()
                } else {
                    pendingCalendarAction = null
                }
            }

            // Request POST_NOTIFICATIONS on Android 13+
            LaunchedEffect(Unit) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    if (androidx.core.content.ContextCompat.checkSelfPermission(
                            activityContext,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                    }
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfiguration,
                LocalContext provides localizedContext
            ) {
                key(uiState.currentLanguage) {
                    ShraddhaCalendarTheme {
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    containerColor = SurfaceCard,
                                    tonalElevation = 8.dp
                                ) {
                                    NavigationBarItem(
                                        selected = uiState.selectedTab == AppTab.CALCULATOR,
                                        onClick = { viewModel.selectTab(AppTab.CALCULATOR) },
                                        icon = { Icon(Icons.Default.Calculate, contentDescription = stringResource(R.string.nav_calculator)) },
                                        label = {
                                            Text(
                                                stringResource(R.string.nav_calculator),
                                                fontWeight = if (uiState.selectedTab == AppTab.CALCULATOR) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = PrimarySaffronDark,
                                            selectedTextColor = PrimarySaffronDark,
                                            indicatorColor = PrimarySaffron.copy(alpha = 0.15f)
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = uiState.selectedTab == AppTab.SAVED,
                                        onClick = { viewModel.selectTab(AppTab.SAVED) },
                                        icon = { Icon(Icons.Default.Bookmark, contentDescription = stringResource(R.string.nav_saved)) },
                                        label = {
                                            Text(
                                                stringResource(R.string.nav_saved),
                                                fontWeight = if (uiState.selectedTab == AppTab.SAVED) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = PrimarySaffronDark,
                                            selectedTextColor = PrimarySaffronDark,
                                            indicatorColor = PrimarySaffron.copy(alpha = 0.15f)
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = uiState.selectedTab == AppTab.RECENTS,
                                        onClick = { viewModel.selectTab(AppTab.RECENTS) },
                                        icon = { Icon(Icons.Default.History, contentDescription = stringResource(R.string.nav_recents)) },
                                        label = {
                                            Text(
                                                stringResource(R.string.nav_recents),
                                                fontWeight = if (uiState.selectedTab == AppTab.RECENTS) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = PrimarySaffronDark,
                                            selectedTextColor = PrimarySaffronDark,
                                            indicatorColor = PrimarySaffron.copy(alpha = 0.15f)
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = uiState.selectedTab == AppTab.SETTINGS,
                                        onClick = { viewModel.selectTab(AppTab.SETTINGS) },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_settings)) },
                                        label = {
                                            Text(
                                                stringResource(R.string.nav_settings),
                                                fontWeight = if (uiState.selectedTab == AppTab.SETTINGS) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = PrimarySaffronDark,
                                            selectedTextColor = PrimarySaffronDark,
                                            indicatorColor = PrimarySaffron.copy(alpha = 0.15f)
                                        )
                                    )
                                }
                            },
                            containerColor = BackgroundWarm
                        ) { padding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                            ) {
                                when (uiState.selectedTab) {
                                    AppTab.CALCULATOR -> {
                                        if (uiState.calculationResult != null) {
                                            ResultsScreen(
                                                result = uiState.calculationResult!!,
                                                currentLanguage = uiState.currentLanguage,
                                                activeCalendarEntities = uiState.activeCalendarEntities,
                                                isAllCalendarActive = uiState.isAllCalendarActive,
                                                isCurrentResultSaved = uiState.isCurrentResultSaved,
                                                onToggleSaveProfile = { viewModel.toggleSaveCurrentProfile() },
                                                onToggleAllCalendar = {
                                                    if (!viewModel.hasCalendarPermission()) {
                                                        pendingCalendarAction = { viewModel.toggleAllCalendarEvents() }
                                                        permissionLauncher.launch(
                                                            arrayOf(
                                                                Manifest.permission.READ_CALENDAR,
                                                                Manifest.permission.WRITE_CALENDAR
                                                            )
                                                        )
                                                    } else {
                                                        viewModel.toggleAllCalendarEvents()
                                                    }
                                                },
                                                onToggleEventCalendar = { event ->
                                                    if (!viewModel.hasCalendarPermission()) {
                                                        pendingCalendarAction = { viewModel.toggleCalendarEvent(event) }
                                                        permissionLauncher.launch(
                                                            arrayOf(
                                                                Manifest.permission.READ_CALENDAR,
                                                                Manifest.permission.WRITE_CALENDAR
                                                            )
                                                        )
                                                    } else {
                                                        viewModel.toggleCalendarEvent(event)
                                                    }
                                                },
                                                onNewCalculationClick = { viewModel.resetCalculator() }
                                            )
                                        } else {
                                            InputScreen(
                                                uiState = uiState,
                                                onPersonNameChange = { viewModel.onPersonNameChange(it) },
                                                onRelationshipChange = { viewModel.onRelationshipChange(it) },
                                                onDeathDateChange = { viewModel.onDeathDateChange(it) },
                                                onDeathTimeChange = { viewModel.onDeathTimeChange(it) },
                                                onLocationChange = { viewModel.onLocationChange(it) },
                                                onTraditionChange = { viewModel.setTradition(it) },
                                                onDemiseStatusChange = { viewModel.onDemiseStatusChange(it) },
                                                onDemiseCircumstanceChange = { viewModel.onDemiseCircumstanceChange(it) },
                                                onLastSeenDateChange = { viewModel.onLastSeenDateChange(it) },
                                                onAgeAtDisappearanceChange = { viewModel.onAgeAtDisappearanceChange(it) },
                                                onCalculateClick = { viewModel.calculateShraddha() }
                                            )
                                        }
                                    }

                                    AppTab.SAVED -> {
                                        SavedScreen(
                                            savedProfiles = uiState.savedProfiles,
                                            currentLanguage = uiState.currentLanguage,
                                            onSelectProfile = { item -> viewModel.openSavedProfile(item) },
                                            onEditProfile = { profile -> viewModel.startEditingProfile(profile) },
                                            onSaveEditedProfile = { updated -> viewModel.saveEditedProfile(updated) },
                                            onDeleteProfile = { id -> viewModel.deleteSavedProfile(uiState.savedProfiles.first { it.id == id }) },
                                            onNavigateToCalculator = { viewModel.selectTab(AppTab.CALCULATOR) },
                                            editingProfile = uiState.editingProfile,
                                            onDismissEdit = { viewModel.dismissEditingProfile() }
                                        )
                                    }

                                    AppTab.RECENTS -> {
                                        RecentsScreen(
                                            recentSearches = uiState.recentSearches,
                                            currentLanguage = uiState.currentLanguage,
                                            onSelectRecent = { item -> viewModel.openRecentSearch(item) },
                                            onEditRecent = { item -> viewModel.editRecentSearch(item) },
                                            onDeleteRecent = { id -> viewModel.deleteRecentSearch(uiState.recentSearches.first { it.id == id }) },
                                            onClearAll = { viewModel.clearAllRecentSearches() }
                                        )
                                    }

                                    AppTab.SETTINGS -> {
                                        SettingsScreen(
                                            currentLanguage = uiState.currentLanguage,
                                            selectedTradition = uiState.selectedTradition,
                                            onLanguageSelected = { lang -> viewModel.setLanguage(lang) },
                                            onTraditionSelected = { trad -> viewModel.setTradition(trad) }
                                        )
                                    }
                                }
                            }

                            // First-Launch Onboarding Dialog
                            if (!uiState.hasCompletedOnboarding) {
                                OnboardingDialog(
                                    initialTradition = uiState.selectedTradition,
                                    onConfirm = { trad -> viewModel.completeOnboarding(trad) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

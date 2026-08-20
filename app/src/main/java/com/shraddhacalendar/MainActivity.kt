package com.shraddhacalendar

import android.Manifest
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
import com.shraddhacalendar.ui.components.CalendarPermissionDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Handle notification deep-link
        handleNotificationIntent(intent)

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            val locale = remember(uiState.currentLanguage) {
                Locale(uiState.currentLanguage.code)
            }

            // Dynamically update Locale & Activity configuration without breaking Activity context
            SideEffect {
                Locale.setDefault(locale)
                val config = Configuration(resources.configuration)
                config.setLocale(locale)
                @Suppress("DEPRECATION")
                resources.updateConfiguration(config, resources.displayMetrics)
            }

            val localizedConfiguration = remember(uiState.currentLanguage) {
                Configuration(resources.configuration).apply {
                    setLocale(locale)
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides localizedConfiguration
            ) {
                key(uiState.currentLanguage) {
                    ShraddhaCalendarTheme {
                        val permissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestMultiplePermissions()
                        ) { permissions ->
                            val granted = permissions.values.all { it }
                            if (granted) {
                                viewModel.onCalendarPermissionGranted()
                            } else {
                                viewModel.dismissCalendarPermissionDialog()
                            }
                        }

                        // Request POST_NOTIFICATIONS on Android 13+
                        val context = LocalContext.current
                        LaunchedEffect(Unit) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                if (androidx.core.content.ContextCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.POST_NOTIFICATIONS
                                    ) != android.content.pm.PackageManager.PERMISSION_GRANTED
                                ) {
                                    permissionLauncher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                                }
                            }
                        }

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
                                                language = uiState.currentLanguage,
                                                isCalendarActive = { key -> uiState.activeCalendarEntities.contains(key) },
                                                isAllCalendarActive = uiState.isAllCalendarActive,
                                                isSaved = uiState.isCurrentResultSaved,
                                                onToggleSave = { viewModel.toggleSaveCurrentResult() },
                                                onToggleIndividualCalendar = { key, enable, event ->
                                                    viewModel.toggleIndividualCalendar(key, enable, event) {
                                                        permissionLauncher.launch(
                                                            arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
                                                        )
                                                    }
                                                },
                                                onToggleAllCalendar = { enable ->
                                                    viewModel.toggleAllCalendar(enable) {
                                                        permissionLauncher.launch(
                                                            arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
                                                        )
                                                    }
                                                },
                                                onBackClick = { viewModel.resetCalculation() }
                                            )
                                        } else {
                                            InputScreen(
                                                uiState = uiState,
                                                onPersonNameChange = { viewModel.onPersonNameChange(it) },
                                                onDeathDateChange = { viewModel.onDeathDateChange(it) },
                                                onDeathTimeChange = { viewModel.onDeathTimeChange(it) },
                                                onLocationChange = { viewModel.onLocationChange(it) },
                                                onCalculateClick = { viewModel.calculateShraddha() }
                                            )
                                        }
                                    }

                                    AppTab.SAVED -> {
                                        SavedScreen(
                                            savedProfiles = uiState.savedProfiles,
                                            currentLanguage = uiState.currentLanguage,
                                            onSelectProfile = { item -> viewModel.reopenSavedProfile(item) },
                                            onDeleteProfile = { id -> viewModel.deleteSavedProfile(id) },
                                            onNavigateToCalculator = { viewModel.selectTab(AppTab.CALCULATOR) }
                                        )
                                    }

                                    AppTab.RECENTS -> {
                                        RecentsScreen(
                                            recentSearches = uiState.recentSearches,
                                            currentLanguage = uiState.currentLanguage,
                                            onSelectRecent = { record -> viewModel.reopenRecentSearch(record) },
                                            onDeleteRecent = { id -> viewModel.deleteRecentSearch(id) },
                                            onClearAll = { viewModel.clearAllRecentSearches() }
                                        )
                                    }

                                    AppTab.SETTINGS -> {
                                        SettingsScreen(
                                            currentLanguage = uiState.currentLanguage,
                                            onLanguageSelected = { lang -> viewModel.setLanguage(lang) }
                                        )
                                    }
                                }
                            }

                            if (uiState.showCalendarPermissionRationale) {
                                CalendarPermissionDialog(
                                    onConfirm = {
                                        permissionLauncher.launch(
                                            arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
                                        )
                                    },
                                    onDismiss = { viewModel.dismissCalendarPermissionDialog() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationIntent(intent)
    }

    private fun handleNotificationIntent(intent: android.content.Intent?) {
        if (intent == null) return
        val fromNotif = intent.getBooleanExtra(com.shraddhacalendar.core.notification.ShraddhaNotificationHelper.EXTRA_FROM_NOTIFICATION, false)
        if (fromNotif) {
            val personName = intent.getStringExtra(com.shraddhacalendar.core.notification.ShraddhaNotificationHelper.EXTRA_PERSON_NAME)
            if (!personName.isNullOrBlank()) {
                viewModel.handleNotificationDeepLink(personName)
            }
        }
    }
}

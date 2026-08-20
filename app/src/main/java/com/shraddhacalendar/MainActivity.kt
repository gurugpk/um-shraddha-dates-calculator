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
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shraddhacalendar.ui.components.CalendarPermissionDialog
import com.shraddhacalendar.ui.input.InputScreen
import com.shraddhacalendar.ui.recents.RecentsScreen
import com.shraddhacalendar.ui.results.ResultsScreen
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

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            // Update configuration on language change
            LaunchedEffect(uiState.currentLanguage) {
                try {
                    val locale = Locale(uiState.currentLanguage.code)
                    Locale.setDefault(locale)
                    val resources = context.resources
                    val config = Configuration(resources.configuration)
                    config.setLocale(locale)
                    @Suppress("DEPRECATION")
                    resources.updateConfiguration(config, resources.displayMetrics)
                } catch (_: Exception) {
                }
            }

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

                            AppTab.RECENTS -> {
                                RecentsScreen(
                                    recentSearches = uiState.recentSearches,
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

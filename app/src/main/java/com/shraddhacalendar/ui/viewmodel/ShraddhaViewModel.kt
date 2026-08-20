package com.shraddhacalendar.ui.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shraddhacalendar.core.calendar.CalendarManager
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.LocaleManager
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.models.ShraddhaCalculationResult
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.local.CalendarMappingRepository
import com.shraddhacalendar.data.local.RecentSearchItem
import com.shraddhacalendar.data.local.RecentSearchRepository
import com.shraddhacalendar.data.location.CityDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

enum class AppTab {
    CALCULATOR,
    RECENTS,
    SETTINGS
}

data class ShraddhaUiState(
    val selectedTab: AppTab = AppTab.CALCULATOR,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val personName: String = "",
    val deathDate: LocalDate = LocalDate.now(),
    val deathTime: LocalTime = LocalTime.of(12, 0),
    val selectedLocation: GeoLocation = CityDatabase.CITIES.first(),
    val isCalculating: Boolean = false,
    val calculationResult: ShraddhaCalculationResult? = null,
    val validationError: String? = null,
    val activeCalendarEntities: Set<String> = emptySet(),
    val isAllCalendarActive: Boolean = false,
    val showCalendarPermissionRationale: Boolean = false,
    val pendingCalendarAction: (() -> Unit)? = null,
    val recentSearches: List<RecentSearchItem> = emptyList()
)

class ShraddhaViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val recentSearchRepo = RecentSearchRepository(context)
    private val calendarMappingRepo = CalendarMappingRepository(context)
    private val calendarManager = CalendarManager(context)

    private val _uiState = MutableStateFlow(
        ShraddhaUiState(
            currentLanguage = LocaleManager.getSavedLanguage(context)
        )
    )
    val uiState: StateFlow<ShraddhaUiState> = _uiState.asStateFlow()

    init {
        loadRecentSearches()
        syncActiveCalendarEvents()
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        if (tab == AppTab.RECENTS) {
            loadRecentSearches()
        }
    }

    fun setLanguage(language: AppLanguage) {
        LocaleManager.saveLanguage(context, language)
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun onPersonNameChange(name: String) {
        _uiState.update { it.copy(personName = name, validationError = null) }
    }

    fun onDeathDateChange(date: LocalDate) {
        _uiState.update { it.copy(deathDate = date, validationError = null) }
    }

    fun onDeathTimeChange(time: LocalTime) {
        _uiState.update { it.copy(deathTime = time, validationError = null) }
    }

    fun onLocationChange(location: GeoLocation) {
        _uiState.update { it.copy(selectedLocation = location, validationError = null) }
    }

    fun calculateShraddha() {
        val currentState = _uiState.value
        if (currentState.personName.isBlank()) {
            _uiState.update { it.copy(validationError = "Please enter the Person's Name") }
            return
        }

        _uiState.update { it.copy(isCalculating = true, validationError = null) }

        viewModelScope.launch {
            try {
                val record = PersonDeathRecord(
                    name = currentState.personName.trim(),
                    deathDate = currentState.deathDate,
                    deathTime = currentState.deathTime,
                    location = currentState.selectedLocation
                )

                val result = ShraddhaCalculator.calculate(
                    personRecord = record,
                    currentDate = LocalDate.now()
                )

                // Save to Recents (Max 10 FIFO)
                recentSearchRepo.saveRecentSearch(record)
                loadRecentSearches()

                _uiState.update {
                    it.copy(
                        isCalculating = false,
                        calculationResult = result,
                        validationError = null
                    )
                }

                syncActiveCalendarEvents()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCalculating = false,
                        validationError = "Calculation error: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    fun reopenRecentSearch(person: PersonDeathRecord) {
        _uiState.update {
            it.copy(
                personName = person.name,
                deathDate = person.deathDate,
                deathTime = person.deathTime,
                selectedLocation = person.location,
                selectedTab = AppTab.CALCULATOR
            )
        }
        calculateShraddha()
    }

    fun deleteRecentSearch(id: Long) {
        viewModelScope.launch {
            recentSearchRepo.deleteRecentSearch(id)
            loadRecentSearches()
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            recentSearchRepo.clearAllHistory()
            loadRecentSearches()
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            try {
                val recents = recentSearchRepo.getRecentSearches()
                _uiState.update { it.copy(recentSearches = recents) }
            } catch (_: Exception) {
            }
        }
    }

    private fun syncActiveCalendarEvents() {
        viewModelScope.launch {
            try {
                val result = _uiState.value.calculationResult
                val person = result?.personRecord
                val activeKeys = mutableSetOf<String>()

                if (result != null && person != null) {
                    val allEvents = if (result.isDeathOlderThanOneYear && result.nextUpcomingShraddha != null) {
                        listOf(result.nextUpcomingShraddha)
                    } else {
                        result.yearlySections.flatMap { it.events }
                    }

                    allEvents.forEach { ev ->
                        val key = com.shraddhacalendar.core.calendar.makeEntityKey(person.name, ev.gregorianDate, ev.sequenceNumber)
                        if (calendarManager.isEventActive(key, person.name, ev)) {
                            activeKeys.add(key)
                        }
                    }
                } else {
                    val mappings = calendarMappingRepo.getAllActiveMappings()
                    mappings.forEach { (key, _) ->
                        if (calendarManager.isEventActive(key, person?.name)) {
                            activeKeys.add(key)
                        }
                    }
                }
                updateCalendarActiveState(activeKeys)
            } catch (_: Exception) {
            }
        }
    }

    private fun updateCalendarActiveState(activeKeys: Set<String>) {
        val result = _uiState.value.calculationResult
        val totalEvents = if (result != null) {
            if (result.isDeathOlderThanOneYear && result.nextUpcomingShraddha != null) 1
            else result.yearlySections.sumOf { it.events.size }
        } else 0

        val allActive = totalEvents > 0 && activeKeys.size >= totalEvents
        _uiState.update {
            it.copy(
                activeCalendarEntities = activeKeys,
                isAllCalendarActive = allActive
            )
        }
    }

    fun toggleIndividualCalendar(entityKey: String, enable: Boolean, event: ShraddhaEvent, onNeedPermission: () -> Unit) {
        if (!calendarManager.hasCalendarPermission()) {
            _uiState.update {
                it.copy(
                    showCalendarPermissionRationale = true,
                    pendingCalendarAction = { toggleIndividualCalendar(entityKey, enable, event, onNeedPermission) }
                )
            }
            return
        }

        viewModelScope.launch {
            val person = _uiState.value.calculationResult?.personRecord ?: return@launch
            val lang = _uiState.value.currentLanguage

            val success = if (enable) {
                calendarManager.addShraddhaToCalendar(person, event, entityKey, lang)
            } else {
                calendarManager.removeShraddhaFromCalendar(entityKey, person.name, event)
            }

            withContext(Dispatchers.Main) {
                if (enable) {
                    if (success) {
                        Toast.makeText(context, "Added meeting event to Google Calendar with 2-day & 1-day reminders", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Could not add to Google Calendar. Please check Google Calendar account.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (success) {
                        Toast.makeText(context, "Removed from Google Calendar", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Could not remove from Google Calendar", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val updated = _uiState.value.activeCalendarEntities.toMutableSet()
            if (enable && success) updated.add(entityKey) else updated.remove(entityKey)
            updateCalendarActiveState(updated)
        }
    }

    fun toggleAllCalendar(enable: Boolean, onNeedPermission: () -> Unit) {
        if (!calendarManager.hasCalendarPermission()) {
            _uiState.update {
                it.copy(
                    showCalendarPermissionRationale = true,
                    pendingCalendarAction = { toggleAllCalendar(enable, onNeedPermission) }
                )
            }
            return
        }

        val result = _uiState.value.calculationResult ?: return
        val person = result.personRecord
        val lang = _uiState.value.currentLanguage

        viewModelScope.launch {
            val eventsToProcess = if (result.isDeathOlderThanOneYear && result.nextUpcomingShraddha != null) {
                listOf(result.nextUpcomingShraddha)
            } else {
                result.yearlySections.flatMap { it.events }
            }

            val updated = _uiState.value.activeCalendarEntities.toMutableSet()
            var anyAdded = false

            eventsToProcess.forEach { ev ->
                val entityKey = com.shraddhacalendar.core.calendar.makeEntityKey(person.name, ev.gregorianDate, ev.sequenceNumber)
                if (enable) {
                    val ok = calendarManager.addShraddhaToCalendar(person, ev, entityKey, lang)
                    if (ok) {
                        updated.add(entityKey)
                        anyAdded = true
                    }
                } else {
                    calendarManager.removeShraddhaFromCalendar(entityKey, person.name, ev)
                    updated.remove(entityKey)
                }
            }

            withContext(Dispatchers.Main) {
                if (enable) {
                    if (anyAdded) {
                        Toast.makeText(context, "All Shraddhas added to Google Calendar with reminders", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Could not add to Google Calendar. Please check Google Calendar sync.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "All Shraddhas removed from Google Calendar", Toast.LENGTH_SHORT).show()
                }
            }

            updateCalendarActiveState(updated)
        }
    }

    fun dismissCalendarPermissionDialog() {
        _uiState.update { it.copy(showCalendarPermissionRationale = false, pendingCalendarAction = null) }
    }

    fun onCalendarPermissionGranted() {
        _uiState.update { it.copy(showCalendarPermissionRationale = false) }
        val action = _uiState.value.pendingCalendarAction
        _uiState.update { it.copy(pendingCalendarAction = null) }
        action?.invoke()
    }

    fun resetCalculation() {
        _uiState.update { it.copy(calculationResult = null, validationError = null) }
    }
}

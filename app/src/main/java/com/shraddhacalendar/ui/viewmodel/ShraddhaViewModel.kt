package com.shraddhacalendar.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shraddhacalendar.core.calendar.CalendarManager
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.LocaleManager
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.local.*
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
    SAVED,
    RECENTS,
    SETTINGS
}

data class ShraddhaUiState(
    val selectedTab: AppTab = AppTab.CALCULATOR,
    val currentLanguage: AppLanguage = AppLanguage.ENGLISH,
    val selectedTradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA,
    val hasCompletedOnboarding: Boolean = true,
    val personName: String = "",
    val relationship: FamilyRelationship = FamilyRelationship.OTHER,
    val deathDate: LocalDate = LocalDate.now(),
    val deathTime: LocalTime = LocalTime.of(12, 0),
    val selectedLocation: GeoLocation = CityDatabase.CITIES.first(),
    val isCalculating: Boolean = false,
    val calculationResult: ShraddhaCalculationResult? = null,
    val validationError: String? = null,
    val activeCalendarEntities: Set<String> = emptySet(),
    val isAllCalendarActive: Boolean = false,
    val isCurrentResultSaved: Boolean = false,
    val showCalendarPermissionRationale: Boolean = false,
    val pendingCalendarAction: (() -> Unit)? = null,
    val savedProfiles: List<SavedProfileItem> = emptyList(),
    val recentSearches: List<RecentSearchItem> = emptyList(),
    val editingProfile: SavedProfileItem? = null,
    val selectedCeremonyInfo: EducationalCeremonyInfo? = null
)

class ShraddhaViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val prefs = context.getSharedPreferences("madwa_shraddha_prefs", Context.MODE_PRIVATE)
    private val recentSearchRepo = RecentSearchRepository(context)
    private val savedProfilesRepo = SavedProfilesRepository(context)
    private val calendarMappingRepo = CalendarMappingRepository(context)
    private val calendarManager = CalendarManager(context)

    private val _uiState = MutableStateFlow(
        ShraddhaUiState(
            currentLanguage = LocaleManager.getSavedLanguage(context),
            selectedTradition = getSavedTradition(),
            hasCompletedOnboarding = prefs.getBoolean("has_completed_onboarding", false)
        )
    )
    val uiState: StateFlow<ShraddhaUiState> = _uiState.asStateFlow()

    init {
        loadSavedProfiles()
        loadRecentSearches()
        syncActiveCalendarEvents()
    }

    private fun getSavedTradition(): MadhwaTradition {
        val savedId = prefs.getString("selected_tradition", MadhwaTradition.UTTARADI_MATHA.id)
        return MadhwaTradition.fromId(savedId)
    }

    fun setTradition(tradition: MadhwaTradition) {
        prefs.edit().putString("selected_tradition", tradition.id).apply()
        _uiState.update { it.copy(selectedTradition = tradition) }
        // If there's an active calculation, recalculate with the new tradition
        val currentRecord = _uiState.value.calculationResult?.personRecord
        if (currentRecord != null) {
            val updatedRecord = currentRecord.copy(tradition = tradition)
            calculateForRecord(updatedRecord)
        }
    }

    fun completeOnboarding(tradition: MadhwaTradition) {
        prefs.edit()
            .putBoolean("has_completed_onboarding", true)
            .putString("selected_tradition", tradition.id)
            .apply()
        _uiState.update {
            it.copy(
                hasCompletedOnboarding = true,
                selectedTradition = tradition
            )
        }
    }

    fun selectTab(tab: AppTab) {
        if (tab == AppTab.CALCULATOR) {
            _uiState.update {
                it.copy(
                    selectedTab = tab,
                    personName = "",
                    relationship = FamilyRelationship.OTHER,
                    deathDate = LocalDate.now(),
                    deathTime = LocalTime.of(12, 0),
                    selectedLocation = CityDatabase.CITIES.first(),
                    calculationResult = null,
                    validationError = null,
                    isCurrentResultSaved = false
                )
            }
        } else {
            _uiState.update { it.copy(selectedTab = tab) }
            if (tab == AppTab.SAVED) {
                loadSavedProfiles()
            } else if (tab == AppTab.RECENTS) {
                loadRecentSearches()
            }
        }
    }

    fun resetCalculator() {
        _uiState.update {
            it.copy(
                personName = "",
                relationship = FamilyRelationship.OTHER,
                deathDate = LocalDate.now(),
                deathTime = LocalTime.of(12, 0),
                selectedLocation = CityDatabase.CITIES.first(),
                calculationResult = null,
                validationError = null,
                isCurrentResultSaved = false
            )
        }
    }

    fun setLanguage(language: AppLanguage) {
        LocaleManager.saveLanguage(context, language)
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun onPersonNameChange(name: String) {
        _uiState.update { it.copy(personName = name, validationError = null) }
    }

    fun onRelationshipChange(rel: FamilyRelationship) {
        _uiState.update { it.copy(relationship = rel) }
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

        val record = PersonDeathRecord(
            name = currentState.personName.trim(),
            deathDate = currentState.deathDate,
            deathTime = currentState.deathTime,
            location = currentState.selectedLocation,
            relationship = currentState.relationship,
            tradition = currentState.selectedTradition
        )

        calculateForRecord(record)
    }

    private fun calculateForRecord(record: PersonDeathRecord) {
        _uiState.update { it.copy(isCalculating = true, validationError = null) }

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.Default) {
                    ShraddhaCalculator.calculate(record)
                }

                // Save to recent searches
                withContext(Dispatchers.IO) {
                    recentSearchRepo.saveRecentSearch(record)
                }

                // Check if profile is saved
                val isSaved = withContext(Dispatchers.IO) {
                    savedProfilesRepo.isProfileSaved(record.name, record.deathDate)
                }

                _uiState.update {
                    it.copy(
                        isCalculating = false,
                        calculationResult = result,
                        isCurrentResultSaved = isSaved,
                        selectedTradition = record.tradition,
                        relationship = record.relationship
                    )
                }

                loadRecentSearches()
                syncActiveCalendarEvents()
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCalculating = false,
                        validationError = "Calculation error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun openSavedProfile(profile: SavedProfileItem) {
        val record = profile.toPersonDeathRecord()
        _uiState.update {
            it.copy(
                personName = record.name,
                relationship = record.relationship,
                deathDate = record.deathDate,
                deathTime = record.deathTime,
                selectedLocation = record.location,
                selectedTradition = record.tradition,
                selectedTab = AppTab.CALCULATOR
            )
        }
        calculateForRecord(record)
    }

    fun openRecentSearch(recent: RecentSearchItem) {
        val record = PersonDeathRecord(
            name = recent.personName,
            deathDate = recent.deathDate,
            deathTime = recent.deathTime,
            location = recent.location,
            tradition = _uiState.value.selectedTradition
        )
        _uiState.update {
            it.copy(
                personName = record.name,
                deathDate = record.deathDate,
                deathTime = record.deathTime,
                selectedLocation = record.location,
                selectedTab = AppTab.CALCULATOR
            )
        }
        calculateForRecord(record)
    }

    fun editRecentSearch(recent: RecentSearchItem) {
        _uiState.update {
            it.copy(
                selectedTab = AppTab.CALCULATOR,
                personName = recent.personName,
                relationship = FamilyRelationship.OTHER,
                deathDate = recent.deathDate,
                deathTime = recent.deathTime,
                selectedLocation = recent.location,
                calculationResult = null,
                validationError = null,
                isCurrentResultSaved = false
            )
        }
    }

    fun startEditingProfile(profile: SavedProfileItem) {
        _uiState.update { it.copy(editingProfile = profile) }
    }

    fun dismissEditingProfile() {
        _uiState.update { it.copy(editingProfile = null) }
    }

    fun saveEditedProfile(updatedRecord: PersonDeathRecord) {
        val editing = _uiState.value.editingProfile ?: return
        viewModelScope.launch(Dispatchers.IO) {
            savedProfilesRepo.updateProfile(
                id = editing.id,
                record = updatedRecord,
                relationship = updatedRecord.relationship.id,
                notes = updatedRecord.notes,
                traditionId = updatedRecord.tradition.id
            )

            loadSavedProfiles()

            val currentResult = _uiState.value.calculationResult
            if (currentResult != null && currentResult.personRecord.id == editing.id) {
                withContext(Dispatchers.Main) {
                    calculateForRecord(updatedRecord)
                }
            }
        }
        _uiState.update { it.copy(editingProfile = null) }
    }

    fun showCeremonyInfo(info: EducationalCeremonyInfo) {
        _uiState.update { it.copy(selectedCeremonyInfo = info) }
    }

    fun dismissCeremonyInfo() {
        _uiState.update { it.copy(selectedCeremonyInfo = null) }
    }

    fun toggleSaveCurrentProfile() {
        val result = _uiState.value.calculationResult ?: return
        val currentlySaved = _uiState.value.isCurrentResultSaved

        viewModelScope.launch(Dispatchers.IO) {
            if (currentlySaved) {
                savedProfilesRepo.deleteSavedProfileByRecord(
                    personName = result.personRecord.name,
                    deathDate = result.personRecord.deathDate
                )
                _uiState.update { it.copy(isCurrentResultSaved = false) }
            } else {
                savedProfilesRepo.saveProfile(
                    record = result.personRecord,
                    relationship = result.personRecord.relationship.id,
                    notes = result.personRecord.notes,
                    traditionId = result.personRecord.tradition.id
                )
                _uiState.update { it.copy(isCurrentResultSaved = true) }
            }
            loadSavedProfiles()
        }
    }

    fun deleteSavedProfile(profile: SavedProfileItem) {
        viewModelScope.launch(Dispatchers.IO) {
            savedProfilesRepo.deleteSavedProfile(profile.id)
            val currentResult = _uiState.value.calculationResult
            if (currentResult != null && currentResult.personRecord.name == profile.personName && currentResult.personRecord.deathDate == profile.deathDate) {
                _uiState.update { it.copy(isCurrentResultSaved = false) }
            }
            loadSavedProfiles()
        }
    }

    fun clearAllSavedProfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            savedProfilesRepo.clearAllSavedProfiles()
            _uiState.update { it.copy(isCurrentResultSaved = false) }
            loadSavedProfiles()
        }
    }

    fun deleteRecentSearch(recent: RecentSearchItem) {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearchRepo.deleteRecentSearch(recent.id)
            loadRecentSearches()
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            recentSearchRepo.clearAllHistory()
            loadRecentSearches()
        }
    }

    fun loadSavedProfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = savedProfilesRepo.getAllSaved()
            _uiState.update { it.copy(savedProfiles = list) }
        }
    }

    fun loadRecentSearches() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = recentSearchRepo.getRecentSearches()
            _uiState.update { it.copy(recentSearches = list) }
        }
    }

    fun toggleAllCalendarEvents() {
        val result = _uiState.value.calculationResult ?: return
        val isAllActive = _uiState.value.isAllCalendarActive
        val language = _uiState.value.currentLanguage

        viewModelScope.launch(Dispatchers.IO) {
            val allEvents = mutableListOf<ShraddhaEvent>()
            result.yearlyObservanceGroups.forEach { grp ->
                allEvents.addAll(grp.masikas)
                allEvents.add(grp.varshikaEvent)
                if (grp.pakshaEvent != null) allEvents.add(grp.pakshaEvent)
            }

            if (isAllActive) {
                allEvents.forEach { event ->
                    val entityKey = "${result.personRecord.name}_${event.gregorianDate}_${event.traditionalName}"
                    calendarManager.removeShraddhaFromCalendar(
                        entityKey = entityKey,
                        personName = result.personRecord.name,
                        event = event
                    )
                }
            } else {
                allEvents.forEach { event ->
                    val entityKey = "${result.personRecord.name}_${event.gregorianDate}_${event.traditionalName}"
                    calendarManager.addShraddhaToCalendar(
                        entityKey = entityKey,
                        person = result.personRecord,
                        event = event,
                        language = language
                    )
                }
            }
            syncActiveCalendarEvents()
        }
    }

    fun toggleCalendarEvent(event: ShraddhaEvent) {
        val result = _uiState.value.calculationResult ?: return
        val language = _uiState.value.currentLanguage

        viewModelScope.launch(Dispatchers.IO) {
            val entityKey = "${result.personRecord.name}_${event.gregorianDate}_${event.traditionalName}"
            val existingEventId = calendarMappingRepo.getEventId(entityKey)
            if (existingEventId != null && existingEventId > 0) {
                calendarManager.removeShraddhaFromCalendar(
                    entityKey = entityKey,
                    personName = result.personRecord.name,
                    event = event
                )
            } else {
                calendarManager.addShraddhaToCalendar(
                    entityKey = entityKey,
                    person = result.personRecord,
                    event = event,
                    language = language
                )
            }
            syncActiveCalendarEvents()
        }
    }

    fun syncActiveCalendarEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            val activeMappings = calendarMappingRepo.getAllActiveMappings()
            val scheduled = activeMappings.keys
            val result = _uiState.value.calculationResult
            val isAll = if (result != null) {
                val allEvents = mutableListOf<ShraddhaEvent>()
                result.yearlyObservanceGroups.forEach { grp ->
                    allEvents.addAll(grp.masikas)
                    allEvents.add(grp.varshikaEvent)
                    if (grp.pakshaEvent != null) allEvents.add(grp.pakshaEvent)
                }
                allEvents.isNotEmpty() && allEvents.all { event ->
                    val key = "${result.personRecord.name}_${event.gregorianDate}_${event.traditionalName}"
                    scheduled.contains(key)
                }
            } else false

            _uiState.update {
                it.copy(
                    activeCalendarEntities = scheduled,
                    isAllCalendarActive = isAll
                )
            }
        }
    }

    fun hasCalendarPermission(): Boolean = calendarManager.hasCalendarPermission()
}

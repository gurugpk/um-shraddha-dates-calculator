# Project: Pitru Panchanga Day-Timing Indicators Enhancement

## Architecture
- **Core Domain Engine**: Shodasha Masika and Varshika calculation engines (`MasikaShraddhaCalculator.kt`, `AparahnaVyaptiEngine.kt`, `DinmanaCalculator.kt`).
- **Localization Subsystem**: Multi-language localization layer (`PanchangaLocalizer.kt`) supporting 5 languages: English, Kannada, Sanskrit (Devanagari), Telugu, and Tamil.
- **Educational Repository & Localizer**: Scriptural content and Garuda Purana Preta Khanda Yama Marga journey mapping (`EducationalContentRepository.kt`, `EducationalContentLocalizer.kt`).
- **Notification & Calendar Integration**: `ShraddhaNotificationHelper.kt` and `CalendarManager.kt` propagating localized ceremony titles and timings.
- **Presentation Layer**: Jetpack Compose UI (`ResultsScreen.kt`, `YearlyObservanceAccordion.kt`, `ShraddhaEventCard.kt`, `CeremonyDetailDialog.kt`, `SettingsScreen.kt`).
- **Target Device Platform**: Android 16 (API 36), OnePlus 13 (`d72a8b23`), 1440x3168 640dpi.

## Feature Inventory
| # | Feature | Description | Milestone | Source | Status |
|---|---------|-------------|-----------|--------|--------|
| F1 | Masika 1 Day Timing | Adya Masika (13th Day) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F2 | Masika 2 Day Timing | Unmasika (27th Day) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F3 | Masika 3 Day Timing | Dvitiya Masika (2nd Month Tithi) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F4 | Masika 4 Day Timing | Traipakshika (45th Day) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F5 | Masika 5-8 Day Timing | Tritiya, Chaturtha, Panchama, Shashtha Masikas with month tithi labels | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F6 | Masika 9 Day Timing | Una-Shanmasika (~170th Day / Godana) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F7 | Masika 10-15 Day Timing | Saptama through Dvadasha Masikas with month tithi labels | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F8 | Masika 16 Day Timing | Unabdika (~340th Day / Una-Varshika) across 5 languages | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F9 | Annual Varshika Timing | Prathama Varshika Shraddha (1st Anniversary) & subsequent Varshikas | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F10 | Educational Content Parity | 20 ceremonies mapped in EducationalContentLocalizer for KN, SA, TE, TA | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F11 | PanchangaLocalizer Fix | Fix non-UTF8 corruption byte and truncated when branches | M1 | ORIGINAL_REQUEST §R1 | DONE |
| F12 | Notification Regression Test | Sync notification formatting assertions in NotificationSchedulerRegressionTest | M2 | ORIGINAL_REQUEST §R2 | DONE |
| F13 | Calendar Manager Test | Sync title and description assertions in CalendarManagerTest | M2 | ORIGINAL_REQUEST §R2 | DONE |
| F14 | UI/UX Fidelity Test | Sync localized ceremony strings in UiUxFunctionalityTest | M2 | ORIGINAL_REQUEST §R2 | DONE |
| F15 | Sanskrit Regex & 16 Masika Parity Test | Fix Devanagari regex for brackets and add 16 Masika parity in LanguageLocalizationRegressionTest | M2 | ORIGINAL_REQUEST §R2 | DONE |
| F16 | Unit Test Suite 100% Pass | 100% pass across all test classes via ./gradlew testDebugUnitTest | M2 | ORIGINAL_REQUEST §R2 | DONE |
| F17 | Debug APK Build | Clean build of pitru_panchanga.apk via ./gradlew assembleDebug | M3 | ORIGINAL_REQUEST §R3 | DONE |
| F18 | OnePlus 13 Deployment | Deploy APK to connected OnePlus 13 (d72a8b23) via ADB | M3 | ORIGINAL_REQUEST §R3 | DONE |
| F19 | UI Navigation & Verification | Calculate dates, navigate to Masikas accordion and detail dialogs | M3 | ORIGINAL_REQUEST §R3 | DONE |
| F20 | Screenshot Capture | Capture high-res screenshots in English and Kannada showing cards and dialogs | M3 | ORIGINAL_REQUEST §R3 | DONE |

## Milestones
| # | Name | Scope | Dependencies | Status |
|---|------|-------|-------------|--------|
| M1 | Localization & Descriptors Implementation | F1-F11: PanchangaLocalizer.kt, EducationalContentRepository.kt, EducationalContentLocalizer.kt | none | DONE |
| M2 | Multi-Language Test Parity & Verification | F12-F16: LanguageLocalizationRegressionTest.kt, NotificationSchedulerRegressionTest.kt, CalendarManagerTest.kt, UiUxFunctionalityTest.kt | M1 | DONE |
| M3 | Device Build, Deploy & Live Screenshots | F17-F20: ./gradlew assembleDebug, adb install on OnePlus 13 (d72a8b23), UI verification & screenshots in Kannada and English | M1, M2 | DONE |

## Interface Contracts
### PanchangaLocalizer ↔ UI / Notifications / Calendar
- `PanchangaLocalizer.localizeTraditionalName(name: String, language: AppLanguage): String`
  - Input: Raw ceremony name e.g. `"Masika 1 — Adya Masika"`, `"Masika 9 — Una-Shanmasika"`, `"Yearly Shraddha — Prathama Varshika Shraddha"`.
  - Output: Fully localized ceremony string with prefix and day timing (e.g. `"Masika 1 — Adya Masika (13th Day)"`, `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)"`, `"मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)"`, `"మాసికం 1 — ఆద్య మాసికం (13వ రోజు)"`, `"மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)"`).
- `EducationalContentRepository.findInfoForEvent(traditionalName: String): CeremonyEducationalInfo?`
  - Matches raw or localized ceremony names against `CONTENT_MAP`.
- `EducationalContentLocalizer.localize(info: CeremonyEducationalInfo, language: AppLanguage): CeremonyEducationalInfo`
  - Returns localized `CeremonyEducationalInfo` with 8 localized fields across all 5 languages.

## Code Layout
- `app/src/main/java/com/shraddhacalendar/core/localization/PanchangaLocalizer.kt` (Owned by M1 Worker)
- `app/src/main/java/com/shraddhacalendar/core/shraddha/EducationalContentRepository.kt` (Owned by M1 Worker)
- `app/src/main/java/com/shraddhacalendar/core/shraddha/EducationalContentLocalizer.kt` (Owned by M1 Worker)
- `app/src/test/java/com/shraddhacalendar/regression/LanguageLocalizationRegressionTest.kt` (Owned by M2 Worker)
- `app/src/test/java/com/shraddhacalendar/regression/NotificationSchedulerRegressionTest.kt` (Owned by M2 Worker)
- `app/src/test/java/com/shraddhacalendar/calendar/CalendarManagerTest.kt` (Owned by M2 Worker)
- `app/src/test/java/com/shraddhacalendar/ui/UiUxFunctionalityTest.kt` (Owned by M2 Worker)
- `.agents/worker_m3_device_deploy/` (Live high-res OnePlus 13 screenshots)

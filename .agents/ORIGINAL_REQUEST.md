# Original User Request

## 2026-08-27T07:44:11Z

Enhance Pitru Panchanga with comprehensive day-timing indicators for all 16 Masikas and annual ceremonies across English, Kannada, Sanskrit, Telugu, and Tamil, and validate end-to-end on OnePlus 13.

Working directory: /Users/gkulkarni/myworkspace/shraddha-calulator
Integrity mode: development

## Requirements

### R1. Comprehensive Masika Day Timing & Ceremony Descriptors
Add explicit day timing and interval descriptions to all 16 Masikas and Varshika ceremonies in `PanchangaLocalizer.kt`, `EducationalContentRepository.kt`, and `EducationalContentLocalizer.kt` across all 5 supported languages:
- **Masika 1**: Adya Masika (13th Day) / ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) / आद्यमासिकम् (१३ तमदिनम्) / ಆద్య మాసికం (13వ రోజు) / ஆத்ய மாஸிகம் (13ஆம் நாள்)
- **Masika 2**: Unmasika (27th Day) / ಊನಮಾಸಿಕ (೨೭ನೇ ದಿನ) / ऊनमासिकम् (२७ तमदिनम्) / ఊనమాసికం (27వ రోజు) / ஊநமாஸிகம் (27ஆம் நாள்)
- **Masika 3**: Dvitiya Masika (2nd Month Tithi) / ದ್ವಿತೀಯ ಮಾಸಿಕ (೨ನೇ ಮಾಸಿಕ ತಿಥಿ)
- **Masika 4**: Traipakshika (45th Day) / ತ್ರೈಪಕ್ಷಿಕ (೪೫ನೇ ದಿನ) / त्रैपाक्षिकम् (४५ तमदिनम्) / త్రైపాక్షికం (45వ రోజు) / த்ரைபாக்ஷிகம் (45ஆம் நாள்)
- **Masika 5 to 8**: Tritiya, Chaturtha, Panchama, Shashtha Masikas with month tithi labels
- **Masika 9**: Una-Shanmasika (~170th Day / Godana) / ಊನಷಾಣ್ಮಾಸಿಕ (೧೭೦ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)
- **Masika 10 to 15**: Saptama through Dvadasha Masikas with month tithi labels
- **Masika 16**: Unabdika (~340th Day / Una-Varshika) / ಊನಾಬ್ದಿಕ (೩೪೦ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)
- **Yearly**: Prathama Varshika Shraddha (1st Anniversary) / ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (೧ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)

### R2. Multi-Language Test Suite Parity
Update and expand unit test suites in `LanguageLocalizationRegressionTest.kt`, `NotificationSchedulerRegressionTest.kt`, and `CalendarManagerTest.kt` ensuring 100% test pass rate for all day timing strings across English, Kannada, Sanskrit, Telugu, and Tamil.

### R3. Build, Deploy, and Live Device Verification
Build debug APK, deploy cleanly to connected OnePlus 13 via USB (`d72a8b23`), navigate to calculation results in Kannada and English, and capture screenshots confirming the day timing labels are clearly displayed on ceremony cards and dialogs.

## Acceptance Criteria

### Day Timing Consistency
- [ ] Every Masika ceremony card and detail dialog shows explicit day timing (e.g. "13th Day", "27th Day", "45th Day", "2nd Month Tithi", etc.)
- [ ] No regression in Shastric authenticity or Sanskrit Devanagari script

### Test Suite Verification
- [ ] `./gradlew testDebugUnitTest` passes 100% with 0 failures

### On-Device Live Verification
- [ ] App is installed and launched on OnePlus 13 (`d72a8b23`)
- [ ] Screenshots captured showing the updated Masika day labels in Kannada and English

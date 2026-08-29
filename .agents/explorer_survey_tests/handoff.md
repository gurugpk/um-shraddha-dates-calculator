# R2 Survey Report: Multi-Language Test Suite Parity and Unit Tests

**Working Directory**: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/explorer_survey_tests`  
**Workspace Root**: `/Users/gkulkarni/myworkspace/shraddha-calulator`  
**Target Milestone**: R2 (Multi-Language Test Suite Parity)

---

## 1. Observation

Direct observations across the codebase, 20 test suite files, and localization engines:

### 1.1 Complete Test Suite Inventory (20 Files)
All test files reside under `app/src/test/java/com/shraddhacalendar/`:
1. `com.shraddhacalendar.astro.AstroCalculationsTest`
2. `com.shraddhacalendar.calendar.CalendarManagerTest`
3. `com.shraddhacalendar.localization.PanchangaLocalizationTest`
4. `com.shraddhacalendar.panchang.PanchangCalculationsTest`
5. `com.shraddhacalendar.regression.EndToEndFullFlowRegressionTest`
6. `com.shraddhacalendar.regression.GlobalLocationRegressionTest`
7. `com.shraddhacalendar.regression.LanguageLocalizationRegressionTest`
8. `com.shraddhacalendar.regression.NotificationSchedulerRegressionTest`
9. `com.shraddhacalendar.regression.PanchangaEdgeCasesRegressionTest`
10. `com.shraddhacalendar.regression.RecentsRepositoryRegressionTest`
11. `com.shraddhacalendar.regression.SavedProfilesRepositoryRegressionTest`
12. `com.shraddhacalendar.shraddha.AcharyaValidationTest`
13. `com.shraddhacalendar.shraddha.BhadrapadaMahalayaTest`
14. `com.shraddhacalendar.shraddha.MasikaSequenceValidationTest`
15. `com.shraddhacalendar.shraddha.RealWorldPanchangaValidationTest`
16. `com.shraddhacalendar.shraddha.ShraddhaCalculationsTest`
17. `com.shraddhacalendar.shraddha.VarshikaDateCalculationTest`
18. `com.shraddhacalendar.tradition.DoshaDetectorTest`
19. `com.shraddhacalendar.tradition.TraditionEnginesTest`
20. `com.shraddhacalendar.ui.UiUxFunctionalityTest`

---

### 1.2 Exact Test Failure Points with R1 Day-Timing Enhancements

When explicit day timing is added to ceremony names in `PanchangaLocalizer.kt` (e.g., `"Adya Masika (13th Day)"`, `"ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)"`, `"आद्यमासिकम् (१३ तमदिनम्)"`, `"ఆద్య మాసికం (13వ రోజు)"`, `"ஆத்ய மாஸிகம் (13ஆம் நாள்)"`), the following tests will directly fail due to hardcoded legacy string assertions:

#### A. `NotificationSchedulerRegressionTest.kt`
- **File**: `app/src/test/java/com/shraddhacalendar/regression/NotificationSchedulerRegressionTest.kt`
- **Method**: `testNotificationMessageFormattingInAll5Languages()` (Lines 63–88)
- **Verbatim Failure Points**:
  - **English (Line 63-64)**:
    - *Expected*: `"Pranesh Kulkarni — Masika 1 — Adya Masika is in 2 days, on 21 August 2026."`
    - *Actual*: `"Pranesh Kulkarni — Masika 1 — Adya Masika (13th Day) is in 2 days, on 21 August 2026."`
  - **Kannada (Line 69-70)**:
    - *Expected*: `"Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ ಇನ್ನೂ ೨ ದಿನಗಳಲ್ಲಿದೆ, 21 August 2026 ರಂದು."`
    - *Actual*: `"Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) ಇನ್ನೂ ೨ ದಿನಗಳಲ್ಲಿದೆ, 21 August 2026 ರಂದು."`
  - **Sanskrit (Line 75-76)**:
    - *Expected*: `"Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् दिनद्वयानन्तरम् अस्ति, 21 August 2026 दिनाङ्के।"`
    - *Actual*: `"Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्) दिनद्वयानन्तरम् अस्ति, 21 August 2026 दिनाङ्के।"`
  - **Telugu (Line 81-82)**:
    - *Expected*: `"Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం మరో 2 రోజుల్లో ఉంది, 21 August 2026 తేదీన."`
    - *Actual*: `"Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం (13వ రోజు) మరో 2 రోజుల్లో ఉంది, 21 August 2026 తేదీన."`
  - **Tamil (Line 87-88)**:
    - *Expected*: `"Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் இன்னும் 2 நாட்களில் உள்ளது, 21 August 2026 அன்று."`
    - *Actual*: `"Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்) இன்னும் 2 நாட்களில் உள்ளது, 21 August 2026 அன்று."`

#### B. `CalendarManagerTest.kt`
- **File**: `app/src/test/java/com/shraddhacalendar/calendar/CalendarManagerTest.kt`
- **Method 1**: `testEventTitleWithPersonName()` (Lines 27–32)
  - **English (Line 27)**:
    - *Expected*: `"Masika 1 — Adya Masika — Pranesh Kulkarni"`
    - *Actual*: `"Masika 1 — Adya Masika (13th Day) — Pranesh Kulkarni"`
  - **Kannada (Line 31)**:
    - *Expected*: `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ — Pranesh Kulkarni"`
    - *Actual*: `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) — Pranesh Kulkarni"`
- **Method 2**: `testDescriptionContent()` (Line 68)
  - **English (Line 68)**:
    - *Expected*: `assertEquals("Masika 1 — Adya Masika — Pranesh Kulkarni", title)`
    - *Actual*: `title` contains `"Masika 1 — Adya Masika (13th Day) — Pranesh Kulkarni"`

#### C. `UiUxFunctionalityTest.kt`
- **File**: `app/src/test/java/com/shraddhacalendar/ui/UiUxFunctionalityTest.kt`
- **Method**: `testAllLanguagesLocalizationFidelity()` (Lines 52–64)
  - **Kannada (Line 52)**: Expected `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ"`, Actual `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)"`
  - **Sanskrit (Line 55)**: Expected `"मासिकम् 1 — आद्यमासिकम्"`, Actual `"मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)"`
  - **Telugu (Line 58)**: Expected `"మాసికం 1 — ఆద్య మాసికం"`, Actual `"మాసికం 1 — ఆద్య మాసికం (13వ రోజు)"`
  - **Tamil (Line 61)**: Expected `"மாஸிகம் 1 — ஆத்ய மாஸிகம்"`, Actual `"மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)"`
  - **English (Line 64)**: Expected `"Masika 1 — Adya Masika"`, Actual `"Masika 1 — Adya Masika (13th Day)"`

#### D. `LanguageLocalizationRegressionTest.kt`
- **File**: `app/src/test/java/com/shraddhacalendar/regression/LanguageLocalizationRegressionTest.kt`
- **Method**: `testSanskritDevanagariExclusivity()` (Lines 94–105)
  - Current Sanskrit Regex: `Regex("[\\u0900-\\u097F\\s—\\d]+")`
  - When `PanchangaLocalizer.localizeTraditionalName("Masika 1 — Adya Masika", AppLanguage.SANSKRIT)` returns `"मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)"`, the parenthesis characters `(` and `)` are rejected by this regex, causing `assertTrue(ritualSa.matches(devanagariRegex))` to fail.
  - Furthermore, `testRitualCeremonyLocalizationAcrossAll5Languages()` only tests 6 sample rituals using generic `isNotBlank()` assertions, lacking exhaustive string assertions for all 16 Masikas.

---

### 1.3 Canonical 5-Language Masika & Ceremony Translation Matrix

| Masika # | Raw Ceremony Key / Name | English | Kannada (ಕನ್ನಡ) | Sanskrit (संस्कृतम्) | Telugu (తెలుగు) | Tamil (தமிழ்) |
|---|---|---|---|---|---|---|
| **1** | Adya Masika | `Masika 1 — Adya Masika (13th Day)` | `ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)` | `मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)` | `మాసికం 1 — ఆద్య మాసికం (13వ రోజు)` | `மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)` |
| **2** | Unmasika | `Masika 2 — Unmasika (27th Day)` | `ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (೨೭ನೇ ದಿನ)` | `मासिकम् 2 — ऊनमासिकम् (२७ तमदिनम्)` | `మాసికం 2 — ఊనమాసికం (27వ రోజు)` | `மாஸிகம் 2 — ஊநமாஸிகம் (27ஆம் நாள்)` |
| **3** | Dvitiya Masika | `Masika 3 — Dvitiya Masika (2nd Month Tithi)` | `ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (೨ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 3 — द्वितीयमासिकम् (द्वितीयमासतिथिः)` | `మాసికం 3 — ద్వితీయ మాసికం (2వ మాస తిథి)` | `மாஸிகம் 3 — த்விதீய மாஸிகம் (2ஆம் மாத திதி)` |
| **4** | Traipakshika | `Masika 4 — Traipakshika (45th Day)` | `ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (೪೫ನೇ ದಿನ)` | `मासिकम् 4 — त्रैपाक्षिकम् (४५ तमदिनम्)` | `మాసికం 4 — త్రైపాక్షికం (45వ రోజు)` | `மாஸிகம் 4 — த்ரைபாக்ஷிகம் (45ஆம் நாள்)` |
| **5** | Tritiya Masika | `Masika 5 — Tritiya Masika (3rd Month Tithi)` | `ಮಾಸಿಕ 5 — ತೃತೀಯ ಮಾಸಿಕ (೩ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 5 — तृतीयमासिकम् (तृतीयमासतिथिः)` | `మాసికం 5 — తృతీయ మాసికం (3వ మాస తిథి)` | `மாஸிகம் 5 — திருதீய மாஸிகம் (3ஆம் மாத திதி)` |
| **6** | Chaturtha Masika | `Masika 6 — Chaturtha Masika (4th Month Tithi)` | `ಮಾಸಿಕ 6 — ಚತುರ್ಥ ಮಾಸಿಕ (೪ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 6 — चतुर्थमासिकम् (चतुर्थमासतिथिः)` | `మాసికం 6 — చతుర్థ మాసికం (4వ మాస తిథి)` | `மாஸிகம் 6 — சதுர்த்த மாஸிகம் (4ஆம் மாத திதி)` |
| **7** | Panchama Masika | `Masika 7 — Panchama Masika (5th Month Tithi)` | `ಮಾಸಿಕ 7 — ಪಂಚಮ ಮಾಸಿಕ (೫ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 7 — पञ्चममासिकम् (पञ्चममासतिथिः)` | `మాసికం 7 — పంచమ మాసికం (5వ మాస తిథి)` | `மாஸிகம் 7 — பஞ்சம மாஸிகம் (5ஆம் மாத திதி)` |
| **8** | Shashtha Masika | `Masika 8 — Shashtha Masika (6th Month Tithi)` | `ಮಾಸಿಕ 8 — ಷಷ್ಠ ಮಾಸಿಕ (೬ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 8 — षष्ठमासिकम् (षष्ठमासतिथिः)` | `మాసికం 8 — షష్ఠ మాసికం (6వ మాస తిథి)` | `மாஸிகம் 8 — ஷஷ்ட மாஸிகம் (6ஆம் மாத திதி)` |
| **9** | Una-Shanmasika | `Masika 9 — Una-Shanmasika (~170th Day / Godana)` | `ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (೧೭೦ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)` | `मासिकम् 9 — ऊनषाण्मासिकम् (१७० तमदिनम् / गोदानसहितम्)` | `మాసికం 9 — ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)` | `மாஸிகம் 9 — ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)` |
| **10** | Saptama Masika | `Masika 10 — Saptama Masika (7th Month Tithi)` | `ಮಾಸಿಕ 10 — ಸಪ್ತಮ ಮಾಸಿಕ (೭ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 10 — सप्तममासिकम् (सप्तममासतिथिः)` | `మాసికం 10 — సప్తమ మాసికం (7వ మాస తిథి)` | `மாஸிகம் 10 — ஸப்தம மாஸிகம் (7ஆம் மாத திதி)` |
| **11** | Ashtama Masika | `Masika 11 — Ashtama Masika (8th Month Tithi)` | `ಮಾಸಿಕ 11 — ಅಷ್ಟಮ ಮಾಸಿಕ (೮ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 11 — अष्टममासिकम् (अष्टममासतिथिः)` | `మాసికం 11 — అష్టమ మాసికం (8వ మాస తిథి)` | `மாஸிகம் 11 — அஷ்டம மாஸிகம் (8ஆம் மாத திதி)` |
| **12** | Navama Masika | `Masika 12 — Navama Masika (9th Month Tithi)` | `ಮಾಸಿಕ 12 — ನವಮ ಮಾಸಿಕ (೯ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 12 — नवममासिकम् (नवममासतिथिः)` | `మాసికం 12 — నవమ మాసికం (9వ మాస తిథి)` | `மாஸிகம் 12 — நவம மாஸிகம் (9ஆம் மாத திதி)` |
| **13** | Dashama Masika | `Masika 13 — Dashama Masika (10th Month Tithi)` | `ಮಾಸಿಕ 13 — ದಶಮ ಮಾಸಿಕ (೧೦ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 13 — दशममासिकम् (दशममासतिथिः)` | `ಮಾసికం 13 — దశమ మాసికం (10వ మాస తిథి)` | `மாஸிகம் 13 — தசம மாஸிகம் (10ஆம் மாத திதி)` |
| **14** | Ekadasha Masika | `Masika 14 — Ekadasha Masika (11th Month Tithi)` | `ಮಾಸಿಕ 14 — ಏಕಾದಶ ಮಾಸಿಕ (೧೧ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 14 — एकादशमासिकम् (एकादशमासतिथिः)` | `மாసికం 14 — ఏకాదశ మాసికం (11వ మాస తిథి)` | `மாஸிகம் 14 — ஏகாதச மாஸிகம் (11ஆம் மாத திதி)` |
| **15** | Dvadasha Masika | `Masika 15 — Dvadasha Masika (12th Month Tithi)` | `ಮಾಸಿಕ 15 — ದ್ವಾದಶ ಮಾಸಿಕ (೧೨ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `मासिकम् 15 — द्वादशमासिकम् (द्वादशमासतिथिः)` | `மாసికం 15 — ద్వాదశ మాసికం (12వ మాస తిథి)` | `மாஸிகம் 15 — த்வாதச மாஸிகம் (12ஆம் மாத திதி)` |
| **16** | Unabdika | `Masika 16 — Unabdika (~340th Day / Una-Varshika)` | `ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (೩೪೦ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)` | `मासिकम् 16 — ऊनाब्दिकम् (३४० तमदिनम् / ऊनवार्षिकम्)` | `மாసికం 16 — ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)` | `மாஸிகம் 16 — ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)` |
| **Annual 1** | Prathama Varshika | `Prathama Varshika Shraddha (1st Anniversary)` | `ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (೧ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)` | `प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)` | `ప్రథమ వార్షిక శ్రాద్ధం (1వ వార్షికం)` | `ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)` |
| **Paksha** | Mahalaya Paksha | `Mahalaya Paksha Shraddha (Pitru Paksha)` | `ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)` | `महालयपक्षश्राद्धम् (पितृपक्षः)` | `మహాలయ పక్ష శ్రాద్ధం (పితృ పక్షం)` | `மஹாலய பக்ஷ ஷ்ராத்தம் (பித்ரு பக்ஷம்)` |

---

## 2. Logic Chain

1. **Localizer Architecture**:
   - `MasikaShraddhaCalculator` assigns sequence-based traditional names (e.g. `Masika 1 — Adya Masika`, `Masika 2 — Unmasika`, `Yearly Shraddha — Prathama Varshika Shraddha`).
   - `PanchangaLocalizer.localizeTraditionalName()` parses both sequence prefixes (`Masika (\d+) — (.+)`) and yearly prefixes (`(?:Yearly Shraddha|Annual Shraddha) — (.+)`), and translates the inner ritual string via `translateRitualName()`.
2. **Impact on Dependent Components**:
   - `ShraddhaNotificationHelper` passes `event.traditionalName` through `PanchangaLocalizer.localizeTraditionalName(name, language)` when building alarm notifications.
   - `CalendarManager` passes `event.traditionalName` through `PanchangaLocalizer.localizeTraditionalName(name, language)` when constructing Google / Android calendar event titles and descriptions.
   - UI Composable cards (`ShraddhaEventCard`, `SingleUpcomingCard`, `ExplanationDialog`) display this localized string directly.
3. **Test Invariant Deductions**:
   - Tests that assert exact string equality on notification messages (`NotificationSchedulerRegressionTest`), calendar titles (`CalendarManagerTest`), and UI fidelity (`UiUxFunctionalityTest`) must be synchronized with the enhanced day-timing strings.
   - Tests that use `.contains("Adya Masika")` (such as `EndToEndFullFlowRegressionTest`, `MasikaSequenceValidationTest`, `AcharyaValidationTest`, `ShraddhaCalculationsTest`) will continue passing without alteration because the substring remains present.
   - Sanskrit regex validator in `LanguageLocalizationRegressionTest` must be updated to permit `()~/-.:` in Devanagari script tests.

---

## 3. Caveats

1. **No Source Code Edits**: Per Explorer constraints, no production or test source files were modified during this investigation.
2. **Adhika Masa Case**: When a death occurs in an Adhika Masa year, 18 Masikas are generated (including `Trayodasha Masika`). `PanchangaLocalizer` must handle `Trayodasha Masika` as `Trayodasha Masika (13th Month Tithi)` / `ತ್ರಯೋದಶ ಮಾಸಿಕ (೧೩ನೇ ಮಾಸಿಕ ತಿಥಿ)` / `त्रयोदशमासिकम् (त्रयोदशमासतिथिः)` / `త్రయోదశ మాసికం (13వ మాస తిథి)` / `த்ரயோதச மாஸிகம் (13ஆம் மாத திதி)`.

---

## 4. Conclusion & Required Test Suite Expansions

To achieve 100% test coverage and parity for R2, the implementer must make the following targeted updates:

### 4.1 Update `NotificationSchedulerRegressionTest.kt`
Update `testNotificationMessageFormattingInAll5Languages()`:
```kotlin
// English
val ritualEn = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.ENGLISH)
assertEquals("Pranesh Kulkarni — Masika 1 — Adya Masika (13th Day) is in 2 days, on 21 August 2026.", String.format(en2Day, personName, ritualEn, dateFormatted))
assertEquals("Pranesh Kulkarni — Masika 1 — Adya Masika (13th Day) is tomorrow, 21 August 2026.", String.format(en1Day, personName, ritualEn, dateFormatted))

// Kannada
val ritualKn = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.KANNADA)
assertEquals("Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) ಇನ್ನೂ ೨ ದಿನಗಳಲ್ಲಿದೆ, 21 August 2026 ರಂದು.", String.format(kn2Day, personName, ritualKn, dateFormatted))
assertEquals("Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) ನಾಳೆ ಇದೆ, 21 August 2026 ರಂದು.", String.format(kn1Day, personName, ritualKn, dateFormatted))

// Sanskrit (Devanagari)
val ritualSa = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.SANSKRIT)
assertEquals("Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्) दिनद्वयानन्तरम् अस्ति, 21 August 2026 दिनाङ्के।", String.format(sa2Day, personName, ritualSa, dateFormatted))
assertEquals("Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्) श्वः अस्ति, 21 August 2026 दिनाङ्के।", String.format(sa1Day, personName, ritualSa, dateFormatted))

// Telugu
val ritualTe = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.TELUGU)
assertEquals("Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం (13వ రోజు) మరో 2 రోజుల్లో ఉంది, 21 August 2026 తేదీన.", String.format(te2Day, personName, ritualTe, dateFormatted))
assertEquals("Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం (13వ రోజు) రేపు ఉంది, 21 August 2026 తేదీన.", String.format(te1Day, personName, ritualTe, dateFormatted))

// Tamil
val ritualTa = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.TAMIL)
assertEquals("Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்) இன்னும் 2 நாட்களில் உள்ளது, 21 August 2026 அன்று.", String.format(ta2Day, personName, ritualTa, dateFormatted))
assertEquals("Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்) நாளை உள்ளது, 21 August 2026 அன்று.", String.format(ta1Day, personName, ritualTa, dateFormatted))
```

### 4.2 Update `CalendarManagerTest.kt`
Update `testEventTitleWithPersonName()` and `testDescriptionContent()`:
```kotlin
@Test
fun testEventTitleWithPersonName() {
    val personName = "Pranesh Kulkarni"
    val rawCeremonyName = "Masika 1 — Adya Masika"
    val localizedEnglish = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.ENGLISH)
    val titleEnglish = "$localizedEnglish — $personName"
    assertEquals("Masika 1 — Adya Masika (13th Day) — Pranesh Kulkarni", titleEnglish)

    val localizedKannada = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.KANNADA)
    val titleKannada = "$localizedKannada — $personName"
    assertEquals("ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ) — Pranesh Kulkarni", titleKannada)

    val localizedSanskrit = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.SANSKRIT)
    assertEquals("मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्) — Pranesh Kulkarni", "$localizedSanskrit — $personName")

    val localizedTelugu = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.TELUGU)
    assertEquals("మాసికం 1 — ఆద్య మాసికం (13వ రోజు) — Pranesh Kulkarni", "$localizedTelugu — $personName")

    val localizedTamil = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.TAMIL)
    assertEquals("மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்) — Pranesh Kulkarni", "$localizedTamil — $personName")
}
```

### 4.3 Update `UiUxFunctionalityTest.kt`
Update `testAllLanguagesLocalizationFidelity()`:
```kotlin
@Test
fun testAllLanguagesLocalizationFidelity() {
    val traditionalName = "Masika 1 — Adya Masika"

    val kn = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.KANNADA)
    assertEquals("ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)", kn)

    val sa = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.SANSKRIT)
    assertEquals("मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)", sa)

    val te = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.TELUGU)
    assertEquals("మాసికం 1 — ఆద్య మాసికం (13వ రోజు)", te)

    val ta = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.TAMIL)
    assertEquals("மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)", ta)

    val en = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.ENGLISH)
    assertEquals("Masika 1 — Adya Masika (13th Day)", en)
}
```

### 4.4 Expand `LanguageLocalizationRegressionTest.kt`
1. Update regex in `testSanskritDevanagariExclusivity()`:
```kotlin
val devanagariRegex = Regex("[\\u0900-\\u097F\\s—\\d()~/.:-]+")
```
2. Add dedicated test `testComprehensive16MasikaDayTimingParity()`:
```kotlin
@Test
fun testComprehensive16MasikaDayTimingParity() {
    val ceremonies = listOf(
        "Masika 1 — Adya Masika" to listOf(
            "Masika 1 — Adya Masika (13th Day)",
            "ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)",
            "मासिकम् 1 — आद्यमासिकम् (१३ तमदिनम्)",
            "మాసికం 1 — ఆద్య మాసికం (13వ రోజు)",
            "மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)"
        ),
        "Masika 2 — Unmasika" to listOf(
            "Masika 2 — Unmasika (27th Day)",
            "ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (೨೭ನೇ ದಿನ)",
            "मासिकम् 2 — ऊनमासिकम् (२७ तमदिनम्)",
            "మాసికం 2 — ఊనమాసికం (27వ రోజు)",
            "மாஸிகம் 2 — ஊநமாஸிகம் (27ஆம் நாள்)"
        ),
        "Masika 3 — Dvitiya Masika" to listOf(
            "Masika 3 — Dvitiya Masika (2nd Month Tithi)",
            "ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (೨ನೇ ಮಾಸಿಕ ತಿಥಿ)",
            "मासिकम् 3 — द्वितीयमासिकम् (द्वितीयमासतिथिः)",
            "మాసికం 3 — ద్వితీయ మాసికం (2వ మాస తిథి)",
            "மாஸிகம் 3 — த்விதீய மாஸிகம் (2ஆம் மாத திதி)"
        ),
        "Masika 4 — Traipakshika" to listOf(
            "Masika 4 — Traipakshika (45th Day)",
            "ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (೪೫ನೇ ದಿನ)",
            "मासिकम् 4 — त्रैपाक्षिकम् (४೫ तमदिनम्)",
            "మాసికం 4 — త్రైపాక్షికం (45వ రోజు)",
            "மாஸிகம் 4 — த்ரைபாக்ஷிகம் (45ஆம் நாள்)"
        ),
        "Masika 9 — Una-Shanmasika (with Godana)" to listOf(
            "Masika 9 — Una-Shanmasika (~170th Day / Godana)",
            "ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (೧೭೦ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)",
            "मासिकम् 9 — ऊनषाण्मासिकम् (१७० तमदिनम् / गोदानसहितम्)",
            "మాసికం 9 — ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)",
            "மாஸிகம் 9 — ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)"
        ),
        "Masika 16 — Unabdika (Una-Varshika)" to listOf(
            "Masika 16 — Unabdika (~340th Day / Una-Varshika)",
            "ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (೩೪೦ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)",
            "मासिकम् 16 — ऊनाब्दिकम् (३४० तमदिनम् / ऊनवार्षिकम्)",
            "మాసికం 16 — ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)",
            "மாஸிகம் 16 — ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)"
        ),
        "Yearly Shraddha — Prathama Varshika Shraddha" to listOf(
            "Prathama Varshika Shraddha (1st Anniversary)",
            "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (೧ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
            "प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)",
            "ప్రథమ వార్షిక శ్రాద్ధం (1వ వార్షికం)",
            "ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)"
        )
    )

    val langs = listOf(AppLanguage.ENGLISH, AppLanguage.KANNADA, AppLanguage.SANSKRIT, AppLanguage.TELUGU, AppLanguage.TAMIL)

    ceremonies.forEach { (raw, expectedList) ->
        langs.forEachIndexed { idx, lang ->
            val actual = PanchangaLocalizer.localizeTraditionalName(raw, lang)
            assertEquals("Ceremony '$raw' in $lang must match expected day-timing string", expectedList[idx], actual)
        }
    }
}
```

---

## 5. Verification Method

To independently verify after code edits:
1. **Full Test Suite Execution**:
   ```bash
   ./gradlew testDebugUnitTest --no-daemon
   ```
   *Expected Result*: `BUILD SUCCESSFUL` with all unit tests across all 20 test classes passing (0 failures, 0 errors).
2. **Targeted Test Execution**:
   ```bash
   ./gradlew testDebugUnitTest --tests "com.shraddhacalendar.regression.LanguageLocalizationRegressionTest"
   ./gradlew testDebugUnitTest --tests "com.shraddhacalendar.regression.NotificationSchedulerRegressionTest"
   ./gradlew testDebugUnitTest --tests "com.shraddhacalendar.calendar.CalendarManagerTest"
   ./gradlew testDebugUnitTest --tests "com.shraddhacalendar.ui.UiUxFunctionalityTest"
   ```
3. **Invalidation Condition**: Any assertion failure where a test expects a legacy ceremony string without day timing or where Sanskrit regex rejects valid Devanagari bracket characters.

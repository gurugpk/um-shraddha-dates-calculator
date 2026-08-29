# Survey & Verification Report: Build, Deploy, and Live Device Verification on OnePlus 13 (`d72a8b23`)

**Target Milestone**: R3: Build, Deploy, and Live Device Verification  
**Investigated by**: Explorer Subagent (`explorer_survey_device_build`)  
**Timestamp**: 2026-08-27T07:48:40Z  
**Working Directory**: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/explorer_survey_device_build`  

---

## 1. Observation

### 1.1 Build Configuration & Project Architecture
- **Root & App Build Scripts**:
  - `app/build.gradle.kts`:
    - Application Namespace: `com.shraddhacalendar`
    - `compileSdk = 35`, `minSdk = 26`, `targetSdk = 35`
    - Custom Output APK configured in `applicationVariants.all` (lines 41–46):
      ```kotlin
      applicationVariants.all {
          outputs.all {
              val output = this as? com.android.build.gradle.internal.api.BaseVariantOutputImpl
              output?.outputFileName = "pitru_panchanga.apk"
          }
      }
      ```
    - Output APK Path: `app/build/outputs/apk/debug/pitru_panchanga.apk` (or `app/build/outputs/apk/debug/app-debug.apk`)
- **Android Manifest (`app/src/main/AndroidManifest.xml`)**:
  - Package: `com.shraddhacalendar`
  - Launcher Activity: `.MainActivity` (`com.shraddhacalendar/.MainActivity`) with `android.intent.action.MAIN` and `android.intent.category.LAUNCHER`.
  - Permissions declared: `READ_CALENDAR`, `WRITE_CALENDAR`, `INTERNET`, `ACCESS_NETWORK_STATE`, `POST_NOTIFICATIONS`, `RECEIVE_BOOT_COMPLETED`, `SCHEDULE_EXACT_ALARM`, `USE_EXACT_ALARM`.

### 1.2 Connected Device Environment (OnePlus 13)
- **ADB Executable Path**: `/Users/gkulkarni/Library/Android/sdk/platform-tools/adb`
- **Device Details**:
  - Serial / Transport: `d72a8b23` (USB connection confirmed via `adb devices -l`)
  - Model: `CPH2649` (OnePlus 13 Indian Variant `CPH2649IN`, hardware board `OP5D55L1`)
  - OS Release / SDK: Android 16 (`ro.build.version.release=16`, SDK `36`)
  - Display Dimensions: `1440x3168` px, Physical Density: `640 dpi`
  - Current Package State: `com.shraddhacalendar` is currently installed (`versionCode=1`, `versionName=1.0.0`).
  - Runtime Permissions: `POST_NOTIFICATIONS: granted=true`.
  - Lock State: `deviceLocked=1`, Keyguard active. Screencap capture via `adb -s d72a8b23 exec-out screencap -p` is fully operational and outputs valid PNG images (4.2 MB).

### 1.3 UI Navigation, Composables & Localization Structure
- **Main Navigation Flow (`MainActivity.kt`)**:
  - Bottom Navigation Bar with 4 tabs:
    - Tab 0: `AppTab.CALCULATOR` (`InputScreen` when `calculationResult == null`; `ResultsScreen` when `calculationResult != null`)
    - Tab 1: `AppTab.SAVED` (`SavedScreen`)
    - Tab 2: `AppTab.RECENTS` (`RecentsScreen`)
    - Tab 3: `AppTab.SETTINGS` (`SettingsScreen` with dynamic multi-language selector: English, Kannada, Sanskrit, Telugu, Tamil)
- **Input Flow (`InputScreen.kt`)**:
  - Fields: Person Name (OutlinedTextField), Relationship (Dropdown), Death Date (DatePickerDialog), Death Time (TimePickerDialog), Demise Location (LocationPickerSheet), Tradition (ExposedDropdownMenuBox).
  - Trigger: "Calculate Shraddha Dates" button invokes `viewModel.calculateShraddha()`, calculating 16 Shodasha Masikas, Varshika, and Mahalaya Paksha observances.
- **Results & Ceremony Display (`ResultsScreen.kt`)**:
  - `DemiseSummaryCard`: Displays departed person, date/time, location, and computed Mruta Tithi Panchanga.
  - `DoshaStatusCard`: Displays Dhanishta Panchaka / Tri-Pushkara status and Shastric remedies.
  - `NextUpcomingObservanceCard`: Highlights the imminent observance with remaining days counter.
  - `YearlyObservanceAccordion` (lines 589–762):
    - Year 1 contains expandable `Masikas (16)` drill-down card.
    - Each Masika ceremony is rendered via `EventItemRow` showing traditional localized title, Gregorian date, Aparahna Kala window (`hh:mm a`), Tithi details, and buttons: `ℹ️ Info` (`onViewInfo`) and `🔍 View Trace` (`onViewTrace`).
- **Ceremony Detail Dialog (`CeremonyDetailDialog.kt`)**:
  - Opened via `ℹ️ Info` / `ℹ️ ವಿವರ` button.
  - Renders `localizedInfo.titleEnglish`, `localizedInfo.titleSanskrit`, and explicit day timing banner:
    ```kotlin
    // CeremonyDetailDialog.kt lines 90-106
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = PrimarySaffron.copy(alpha = 0.08f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "🗓️ " + localizedInfo.dayTiming,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                fontWeight = FontWeight.SemiBold,
                color = PrimarySaffronDark
            )
        }
    }
    ```
  - Displays Yama Marga stage (Garuda Purana Preta Khanda), spiritual impact, why needed, and canonical citations.

### 1.4 Critical Codebase Finding in `PanchangaLocalizer.kt`
- **Compiler Error Observation**: Running `./gradlew compileDebugKotlin` or `./gradlew testDebugUnitTest` fails due to syntax corruption at line 229 in `app/src/main/java/com/shraddhacalendar/core/localization/PanchangaLocalizer.kt`:
  ```kotlin
  Line 224: name.contains("Shashtha Masika") || name.contains("Shanmasika") -> when (language) {
  Line 225:     AppLanguage.KANNADA -> "ಷಷ್ಠ ಮಾಸಿಕ (೬ನೇ ಮಾಸಿಕ ತಿಥಿ)"
  Line 226:     AppLanguage.SANSKRIT -> "षष्ठमासिकम् (६ष्टमासतिथिः)"
  Line 227:     AppLanguage.TELUGU -> "షష్ఠ మాసికం (6వ మాస తిథి)"
  Line 228:     AppLanguage.TAMIL -> "ஷஷ்ட மாஸிகம் (6ஆம் மாத திதி)"
  Line 229:     AppLanguage.ENGLISH -> "S            name.contains("Dvitiya Varshika") -> when (language) {
  ```
  This truncated expression broke the `when` branch and omitted Saptama through Dvadasha Masikas, Unabdika, and Prathama Varshika mappings.

---

## 2. Logic Chain

1. **Build Readiness**:
   - To build the debug APK via `./gradlew assembleDebug`, the syntax corruption at line 229 in `PanchangaLocalizer.kt` must first be resolved by the implementer agent, completing all 16 Masika and Varshika day-timing mappings across English, Kannada, Sanskrit, Telugu, and Tamil per R1.
2. **Test Suite Parity (R2)**:
   - Once `PanchangaLocalizer.kt` is corrected, test assertions in `LanguageLocalizationRegressionTest.kt`, `NotificationSchedulerRegressionTest.kt`, `CalendarManagerTest.kt`, and `UiUxFunctionalityTest.kt` must match the enhanced day-timing strings (e.g. `"Masika 1 — Adya Masika (13th Day)"` and `"ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)"`).
   - `./gradlew testDebugUnitTest` can then execute cleanly with 100% pass rate.
3. **Deployment to OnePlus 13 (`d72a8b23`)**:
   - ADB is available at `/Users/gkulkarni/Library/Android/sdk/platform-tools/adb`.
   - The device `d72a8b23` is recognized as an active USB target running Android 16 (SDK 36).
   - Clean install is executed via:
     `adb -s d72a8b23 install -r app/build/outputs/apk/debug/pitru_panchanga.apk`
4. **On-Device Navigation & Screenshot Protocol**:
   - App launch: `adb -s d72a8b23 shell am start -n com.shraddhacalendar/.MainActivity`
   - Input calculation: Enter deceased name (e.g. "Late Pranesh Kulkarni"), select tradition / dates, tap "Calculate Shraddha Dates".
   - Result verification (English):
     - Expand Year 1 Masikas section.
     - Verify day-timing indicators: "Adya Masika (13th Day)", "Unmasika (27th Day)", "Traipakshika (45th Day)", "Una-Shanmasika (~170th Day / Godana)", "Unabdika (~340th Day / Una-Varshika)", etc.
     - Tap "ℹ️ Info" button to verify `CeremonyDetailDialog` shows `🗓️ Observed on Day 13 following demise (completion of Ashaucha)`.
     - Capture screenshot: `adb -s d72a8b23 exec-out screencap -p > screenshot_english.png`
   - Language Switch to Kannada:
     - Tap Settings Tab (bottom navigation bar) -> select "ಕನ್ನಡ".
     - Return to Calculator Tab.
     - Expand Year 1 Masikas section: verify "ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)", "ಊನಮಾಸಿಕ (೨೭ನೇ ದಿನ)", "ತ್ರೈಪಕ್ಷಿಕ (೪೫ನೇ ದಿನ)", "ಊನಷಾಣ್ಮಾಸಿಕ (೧೭೦ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)", "ಊನಾಬ್ದಿಕ (೩೪೦ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)".
     - Tap "ℹ️ ವಿವರ" button: verify `CeremonyDetailDialog` shows `🗓️ ಮೃತ್ಯುವಿನ ನಂತರದ ೧೩ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುತ್ತದೆ (ಆಶೌಚ ಮುಕ್ತಾಯ)`.
     - Capture screenshot: `adb -s d72a8b23 exec-out screencap -p > screenshot_kannada.png`

---

## 3. Caveats

1. **Device Lock State**:
   - OnePlus 13 has screen lock / keyguard enabled (`deviceLocked=1`). For live user-facing interaction, unlocking the device or waking it via `adb shell input keyevent KEYCODE_WAKEUP` / dismiss-keyguard facilitates seamless UI inspection.
2. **Read-Only Scope**:
   - In accordance with the Explorer archetype constraints, no modifications to source files (`PanchangaLocalizer.kt`, etc.) were performed by this agent. The exact location and proposed resolution have been documented for downstream implementation.

---

## 4. Conclusion

- The OnePlus 13 (`d72a8b23`) device environment is fully configured, reachable via ADB at `/Users/gkulkarni/Library/Android/sdk/platform-tools/adb`, and ready for deployment and screenshot capture.
- The UI architecture cleanly separates the input form, results accordion, ceremony cards, and `CeremonyDetailDialog` with dynamic locale binding across English, Kannada, Sanskrit, Telugu, and Tamil.
- The build pipeline (`./gradlew assembleDebug` and `./gradlew testDebugUnitTest`) requires completing the fix in `PanchangaLocalizer.kt` (line 229) and updating corresponding test assertions to achieve 100% pass rate before final device deployment.

---

## 5. Verification Method & Step-by-Step Runbook

### Step 1: Fix Syntax & Multi-Language Descriptors
Ensure `PanchangaLocalizer.kt` contains complete mappings for all 16 Masikas and Varshikas in English, Kannada, Sanskrit, Telugu, and Tamil as per R1.

### Step 2: Run Unit Test Suite
```bash
export PATH="$PATH:/Users/gkulkarni/Library/Android/sdk/platform-tools"
./gradlew testDebugUnitTest
```
*Expected Result*: BUILD SUCCESSFUL with 0 failures across all unit test suites.

### Step 3: Build Debug APK
```bash
export PATH="$PATH:/Users/gkulkarni/Library/Android/sdk/platform-tools"
./gradlew assembleDebug
```
*Artifact Location*: `app/build/outputs/apk/debug/pitru_panchanga.apk`

### Step 4: Deploy to OnePlus 13 (`d72a8b23`)
```bash
export PATH="$PATH:/Users/gkulkarni/Library/Android/sdk/platform-tools"
adb -s d72a8b23 install -r app/build/outputs/apk/debug/pitru_panchanga.apk
```
*Expected Result*: `Success` returned by package manager.

### Step 5: Launch Application
```bash
adb -s d72a8b23 shell am start -n com.shraddhacalendar/.MainActivity
```

### Step 6: Automated UI Interaction & Screenshot Capture
```bash
export PATH="$PATH:/Users/gkulkarni/Library/Android/sdk/platform-tools"

# Wake screen
adb -s d72a8b23 shell input keyevent KEYCODE_WAKEUP
adb -s d72a8b23 shell wm dismiss-keyguard

# Enter Name and Calculate
adb -s d72a8b23 shell input tap 720 850
adb -s d72a8b23 shell input text "Late%sPranesh%sKulkarni"
adb -s d72a8b23 shell input keyevent KEYCODE_BACK
sleep 1
adb -s d72a8b23 shell input tap 720 1800
sleep 2

# Expand Masikas Accordion
adb -s d72a8b23 shell input tap 720 1600
sleep 1

# Capture English Masikas Results
adb -s d72a8b23 exec-out screencap -p > .agents/explorer_survey_device_build/screenshot_english_masikas.png

# Tap Info button on Adya Masika
adb -s d72a8b23 shell input tap 1150 1850
sleep 1

# Capture English Detail Dialog
adb -s d72a8b23 exec-out screencap -p > .agents/explorer_survey_device_build/screenshot_english_dialog.png

# Close Dialog
adb -s d72a8b23 shell input keyevent KEYCODE_BACK
sleep 1

# Switch Language to Kannada (Settings Tab)
adb -s d72a8b23 shell input tap 1260 3050
sleep 1
adb -s d72a8b23 shell input tap 720 1100
sleep 1

# Return to Calculator Tab
adb -s d72a8b23 shell input tap 180 3050
sleep 1

# Expand Masikas Accordion (Kannada)
adb -s d72a8b23 shell input tap 720 1600
sleep 1

# Capture Kannada Masikas Results
adb -s d72a8b23 exec-out screencap -p > .agents/explorer_survey_device_build/screenshot_kannada_masikas.png

# Tap Info button (ವಿವರ) on Adya Masika
adb -s d72a8b23 shell input tap 1150 1850
sleep 1

# Capture Kannada Detail Dialog
adb -s d72a8b23 exec-out screencap -p > .agents/explorer_survey_device_build/screenshot_kannada_dialog.png
```

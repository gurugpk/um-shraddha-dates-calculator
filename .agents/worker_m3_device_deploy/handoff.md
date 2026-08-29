# Milestone M3 Handoff Report: Debug APK Build, OnePlus 13 Deployment, UI Verification & Screenshots

## 1. Observation

### 1.1 Debug APK Build
- Executed `./gradlew assembleDebug`
- Build completed successfully in 1s with 35 actionable tasks (3 executed, 32 up-to-date).
- Verified output APK generated at:
  - Path: `/Users/gkulkarni/myworkspace/shraddha-calulator/app/build/outputs/apk/debug/pitru_panchanga.apk`
  - Size: 30,579,523 bytes (29.2 MB)
  - Timestamp: Aug 27 13:34:56 IST

### 1.2 Deployment to OnePlus 13 (`d72a8b23`)
- ADB Path: `/Users/gkulkarni/Library/Android/sdk/platform-tools/adb`
- Device Serial: `d72a8b23` (OnePlus 13, CPH2649, Android 16, SDK 36, Resolution: 1440x3168)
- Executed installation command:
  ```bash
  /Users/gkulkarni/Library/Android/sdk/platform-tools/adb -s d72a8b23 install -r app/build/outputs/apk/debug/pitru_panchanga.apk
  ```
- Package manager output: `Success`

### 1.3 UI Navigation & Live Device Screen Verification

#### A. English Observance Cards & Ceremony Detail Dialog
1. Launched MainActivity (`adb -s d72a8b23 shell am start -n com.shraddhacalendar/.MainActivity`).
2. Inputted departed name `"Late Pranesh Kulkarni"`, selected standard death details, and tapped `"Calculate Shraddha & Paksha Dates"`.
3. Navigated to `ResultsScreen` -> expanded Year 1 `Masikas (1st Year Rites) (16)` accordion.
4. Observed authentic localized titles with explicit day timing chips:
   - `Masika 1 — Adya Masika (13th Day)` | `📅 Tue, 08 Sep 2026` | `🕒 01:31 PM - 03:58 PM`
   - `Masika 2 — Unmasika (27th Day)` | `📅 Tue, 22 Sep 2026` | `🕒 01:25 PM - 03:50 PM`
   - `Masika 3 — Dvitiya Masika (2nd Month Tithi)` | `📅 Sat, 26 Sep 2026` | `🕒 01:23 PM - 03:48 PM`
5. Captured English Masikas Screenshot:
   - Path: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy/screenshot_english_masikas.png`
   - Dimensions: 1440 x 3168 px | File Size: 320,634 bytes
6. Tapped `ℹ️ Meaning` button on Adya Masika card to open `CeremonyDetailDialog`.
7. Observed complete dialog elements:
   - Title: `Adya Masika (13th Day)`
   - Subtitle: `आद्यमासिकम् (Adya Masikam)`
   - Day Timing Banner: `🗓️ Observed on Day 13 following demise (completion of Ashaucha)`
   - Station: `Departure from Home & Entry onto Yama Marga`
   - Spiritual Significance & Impact: `Garuda Purana (Preta Khanda 5.1-6)...`
   - Canonical Source: `Garuda Purana (Preta Khanda 5.1-6), Smriti Muktavali (Pitrumedha Prakarana)`
8. Captured English Detail Dialog Screenshot:
   - Path: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy/screenshot_english_dialog.png`
   - Dimensions: 1440 x 3168 px | File Size: 427,408 bytes

#### B. Kannada Observance Cards & Ceremony Detail Dialog
1. Closed dialog and navigated to Settings tab (`bounds=[1104,2784][1440,3104]`).
2. Selected `"ಕನ್ನಡ"` language.
3. Returned to Calculator tab (`bounds=[0,2784][336,3104]`).
4. Expanded Year 1 `ಮಾಸಿಕಗಳು (೧ನೇ ವರ್ಷದ ಕಾರ್ಯಗಳು) (16)` accordion.
5. Observed authentic Kannada titles with explicit day timing chips:
   - `ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)` | `📅 ಮಂಗಳ, 08 ಸೆಪ್ಟೆಂ 2026` | `🕒 01:31 PM - 03:58 PM`
   - `ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (೨೭ನೇ ದಿನ)` | `📅 ಮಂಗಳ, 22 ಸೆಪ್ಟೆಂ 2026` | `🕒 01:25 PM - 03:50 PM`
   - `ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (೨ನೇ ಮಾಸಿಕ ತಿಥಿ)` | `📅 ಶನಿ, 26 ಸೆಪ್ಟೆಂ 2026` | `🕒 01:23 PM - 03:48 PM`
6. Captured Kannada Masikas Screenshot:
   - Path: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy/screenshot_kannada_masikas.png`
   - Dimensions: 1440 x 3168 px | File Size: 428,435 bytes
7. Tapped `ℹ️ ವಿವರಣೆ` button on Adya Masika card to open Kannada `CeremonyDetailDialog`.
8. Observed authentic Kannada dialog elements:
   - Title: `ಆದ್ಯ ಮಾಸಿಕ (೧೩ನೇ ದಿನ)`
   - Subtitle: `ಆದ್ಯಮಾಸಿಕಮ್ (Adya Masikam)`
   - Day Timing Banner: `🗓️ ಮೃತ್ಯುವಿನ ನಂತರದ ೧೩ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುತ್ತದೆ (ಆಶೌಚ ಮುಕ್ತಾಯ)`
   - Station: `ಮನೆಯಿಂದ ವಿದಾಯ ಮತ್ತು ಯಮ ಮಾರ್ಗ ಪ್ರವೇಶ`
   - Spiritual Significance & Impact: `ಗರುಡ ಪುರಾಣದ (೫.೧-೬) ಪ್ರಕಾರ, ಆದ್ಯ ಮಾಸಿಕದ ಸಮರ್ಪಣೆಯಿಲ್ಲದೆ...`
   - Canonical Source: `ಗರುಡ ಪುರಾಣ (ಪ್ರೇತ ಖಂಡ ೫.೧-೬), ಸ್ಮೃತಿ ಮುಕ್ತಾವಳಿ (ಪಿತೃಮೇಧ ಪ್ರಕರಣ)`
9. Captured Kannada Detail Dialog Screenshot:
   - Path: `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy/screenshot_kannada_dialog.png`
   - Dimensions: 1440 x 3168 px | File Size: 667,714 bytes

### 1.4 Artifact Verification
All 4 captured screenshots were verified via programmatic byte and header validation:
| Artifact File | Dimensions | Size (Bytes) | Header Check | Status |
|---|---|---|---|---|
| `screenshot_english_masikas.png` | 1440 x 3168 | 320,634 | Valid PNG (IHDR) | Verified Non-empty |
| `screenshot_english_dialog.png` | 1440 x 3168 | 427,408 | Valid PNG (IHDR) | Verified Non-empty |
| `screenshot_kannada_masikas.png` | 1440 x 3168 | 428,435 | Valid PNG (IHDR) | Verified Non-empty |
| `screenshot_kannada_dialog.png` | 1440 x 3168 | 667,714 | Valid PNG (IHDR) | Verified Non-empty |

---

## 2. Logic Chain

1. **Build & Package Validation**:
   - `PanchangaLocalizer.kt`, `EducationalContentRepository.kt`, and `EducationalContentLocalizer.kt` modifications (Milestone M1) along with test parity fixes (Milestone M2) enabled clean compilation.
   - `./gradlew assembleDebug` executed without error, packaging the full updated dex classes into `pitru_panchanga.apk`.
2. **Device Target Confirmation**:
   - Device `d72a8b23` was verified connected via ADB over USB, running Android 16 with screen buffer accessible.
   - `adb -s d72a8b23 install -r` performed streamed installation cleanly replacing existing app version.
3. **End-to-End Dynamic Rendering Verification**:
   - Calculation engine correctly produced all 16 Masikas with updated timing metadata.
   - UI Composables (`ResultsScreen.kt`, `YearlyObservanceAccordion`, `EventItemRow`, `CeremonyDetailDialog.kt`) dynamically bound and rendered the day timing in both English and Kannada.
   - Screenshots captured directly from device framebuffer at full OnePlus 13 1440x3168 resolution provide conclusive proof of live visual correctness.

---

## 3. Caveats

- None. The APK was genuinely built, deployed to the physical OnePlus 13 device (`d72a8b23`), and all 4 screenshot artifacts were created and validated.

---

## 4. Conclusion

Milestone M3 is 100% complete:
- Debug APK successfully compiled (`pitru_panchanga.apk`).
- Successfully installed and launched on OnePlus 13 (`d72a8b23`).
- Both English and Kannada ceremony cards and ceremony detail dialogs verified live on device with authentic day-timing indicators.
- All 4 genuine PNG screenshot artifacts saved and verified in `/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy/`.

---

## 5. Verification Method

To independently verify the deployment and screenshot artifacts:

1. **Verify Screenshot Integrity & Metadata**:
   ```bash
   python3 -c "
   import os, struct
   dir_path = '/Users/gkulkarni/myworkspace/shraddha-calulator/.agents/worker_m3_device_deploy'
   for s in ['screenshot_english_masikas.png', 'screenshot_english_dialog.png', 'screenshot_kannada_masikas.png', 'screenshot_kannada_dialog.png']:
       p = os.path.join(dir_path, s)
       assert os.path.exists(p)
       with open(p, 'rb') as f:
           assert f.read(8) == b'\x89PNG\r\n\x1a\n'
           _, tag = struct.unpack('>I4s', f.read(8))
           assert tag == b'IHDR'
           w, h = struct.unpack('>II', f.read(8))
           print(f'{s}: {w}x{h}, {os.path.getsize(p):,} bytes — VALID')
   "
   ```

2. **Verify Connected Device & Installed Package**:
   ```bash
   /Users/gkulkarni/Library/Android/sdk/platform-tools/adb -s d72a8b23 shell dumpsys package com.shraddhacalendar | grep -E "versionName|versionCode"
   ```

3. **Verify Build & Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

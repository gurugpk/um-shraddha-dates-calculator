# E2E Test Infra: Pitru Panchanga Day-Timing Indicators

## Test Philosophy
- Multi-Language Parity & Regression Verification across all 5 supported languages (English, Kannada, Sanskrit, Telugu, Tamil).
- Systematic 4-tier verification covering Feature Coverage, Boundary/Corner cases, Cross-Feature/Language Combinations, and Real-World Workload Testing.

## Feature Inventory & Test Coverage Matrix
| # | Feature | Source | Tier 1 (Unit) | Tier 2 (Boundary) | Tier 3 (Cross-Language) | Tier 4 (Device E2E) |
|---|---------|--------|:-------------:|:-----------------:|:-----------------------:|:-------------------:|
| 1 | Adya Masika (13th Day) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 2 | Unmasika (27th Day) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 3 | Dvitiya Masika (2nd Month Tithi) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 4 | Traipakshika (45th Day) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 5 | Tritiya to Shashtha Masikas | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 6 | Una-Shanmasika (~170th Day / Godana) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 7 | Saptama to Dvadasha Masikas | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 8 | Unabdika (~340th Day / Una-Varshika) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 9 | Prathama Varshika (1st Anniversary) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 10 | Educational Content (19 ceremonies) | ORIGINAL_REQUEST §R1 | ✓ | ✓ | ✓ | ✓ |
| 11 | Notifications 5-Language Strings | ORIGINAL_REQUEST §R2 | ✓ | ✓ | ✓ | N/A |
| 12 | Calendar Manager 5-Language Titles | ORIGINAL_REQUEST §R2 | ✓ | ✓ | ✓ | N/A |
| 13 | Sanskrit Devanagari Script Regex | ORIGINAL_REQUEST §R2 | ✓ | ✓ | ✓ | N/A |
| 14 | On-Device UI & Screenshots | ORIGINAL_REQUEST §R3 | N/A | N/A | N/A | ✓ |

## Test Architecture
- Unit/Regression Runner: `./gradlew testDebugUnitTest` (20 test classes)
- Target Test Suites:
  - `com.shraddhacalendar.regression.LanguageLocalizationRegressionTest`
  - `com.shraddhacalendar.regression.NotificationSchedulerRegressionTest`
  - `com.shraddhacalendar.calendar.CalendarManagerTest`
  - `com.shraddhacalendar.ui.UiUxFunctionalityTest`
- Device UI Runner: ADB on OnePlus 13 (`d72a8b23`) with screenshot captures for English and Kannada.

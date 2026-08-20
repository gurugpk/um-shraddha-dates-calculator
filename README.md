# UM Shraddha Dates Calculator 🕉️

> **Authoritative Astronomical Shraddha & Masika Calendar for Sri Uttaradi Math Tradition**  
> *Developed and Managed by **Gururaj Kulkarni***

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-purple.svg)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%2F%20Material%203-blue.svg)](https://developer.android.com/jetpack/compose)
[![Panchanga](https://img.shields.io/badge/Panchanga-Sri%20Uttaradi%20Math-orange.svg)](https://www.uttaradimath.org)
[![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)](LICENSE)

---

## 📖 Overview

**UM Shraddha Dates Calculator** is a high-precision, offline-first Android application that automates the calculation of **Masika (monthly)** and **Varshika (annual)** Shraddha ceremony dates strictly according to **Sri Uttaradi Math Panchanga** and **Dvaita / Madhwa Smriti traditions** (*Smriti Muktavali, Dharmasindhu, Nirnayasindhu*).

It eliminates manual calendar guesswork by using **high-precision astronomical algorithms** to calculate exact lunar tithis, leap months (*Adhika Masa*), and the sacred **Aparahna Kala (ceremony time window)** adjusted for any city worldwide.

---

## ✨ Key Features

### 1. 📅 Complete 16 Shodasha Rites (Year 1)
Generates the unbroken traditional ritual sequence for recent bereavements:
1. **Adya Masika (Masika 1)** — 13th Day after death (completion of Ashaucha)
2. **Unmasika (Masika 2)** — 27th Day interval rite
3. **Dwitiya Masika (Masika 3)** — 1st Lunar Death Tithi
4. **Traipakshika (Masika 4)** — 45th Day interval rite
5. **Tritiya Masika to Dvadasha Masika (Masikas 5–15)** — Monthly lunar death tithis
6. **Una-Shanmasika with Godana (Masika 9)** — Observed in 6th month before Saptama Masika
7. **Unabdika (Masika 16)** — Day 350 interval rite
8. **Prathama Varshika Shraddha** — 1st Annual Shraddha Ceremony
9. **5-Year Future Forecast** — Expandable accordion with Varshika dates for Years 2 to 5.

### 2. ☀️ High-Precision Aparahna Kala Calculation
- Computes local sunrise, sunset, Dinmana, and the exact **Aparahna Window** ($3/5$ to $4/5$ of daytime: `HH:mm:ss` to `HH:mm:ss`) tailored to the coordinates of the chosen city.

### 3. 🌐 5 Languages (Native Script)
Switch seamlessly at runtime between:
- **English**
- **ಕನ್ನಡ (Kannada)**
- **संस्कृतम् (Sanskrit)**
- **తెలుగు (Telugu)**
- **தமிழ் (Tamil)**

### 4. 📅 1-Tap Google Calendar Sync & Reminders
- Schedule meeting reservations directly in **Google Calendar**.
- **Automated Advance Notifications**: Sets **2-day before (48h)** and **1-day before (24h)** reminders for family preparations.
- Multi-tier deletion purge on untoggle with real-time UI refresh.

### 5. 🌍 500+ Built-in Cities & Global Timezones
- Pre-packaged offline database of Indian cities, sacred pilgrimage centers (*Mantralayam, Udupi, Kashi, Gaya, Tirupati, Rameswaram*), and major NRI hubs worldwide (USA, UK, UAE, Singapore, Australia, Canada, Europe).

### 6. 🔒 100% Offline, Ad-Free & Private
- Zero internet required for calculations.
- No analytics, no ads, and no data collection. All search history (max 10 FIFO) is stored securely on-device.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+ (100% Coroutines & Flow)
- **UI Toolkit**: Jetpack Compose + Material 3 design system
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture principles
- **Local Storage**: Android SQLite via SQLiteOpenHelper
- **Calendar Integration**: Android CalendarProvider (`CalendarContract.Events`, `CalendarContract.Reminders`)
- **Astronomy Engine**: Pure algorithmic Ephemeris calculation of Solar & Lunar Longitudes, Tithi Vyapti, and Dinmana

---

## 🚀 Building & Running from Source

### Prerequisites
- Android Studio Ladybug (2024.2.1+) or newer
- JDK 17
- Android SDK 35 (compileSdk 35, minSdk 26)

### Build Debug APK
```bash
git clone https://github.com/<your-username>/um-shraddha-dates-calculator.git
cd um-shraddha-dates-calculator
./gradlew assembleDebug
```
The compiled APK will be located at:
`app/build/outputs/apk/debug/um_shraddha_masika_calulator.apk`

### Run All Unit & Integration Tests
```bash
./gradlew test
```

---

## 📱 Publishing to Google Play Store

To generate an Android App Bundle (`.aab`) for Google Play Store upload:
```bash
./gradlew bundleRelease
```
The output file will be at:
`app/build/outputs/bundle/release/app-release.aab`

---

## 📜 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer & Maintainer

**Developed and Managed by:**  
**Gururaj Kulkarni**  
*Dedicated to the service of Sri Hari, Vayu, and Sri 108 Uttaradi Math Parampara.*

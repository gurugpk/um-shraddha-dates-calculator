package com.shraddhacalendar.core.localization

import com.shraddhacalendar.core.models.*

/**
 * Localizes authentic traditional Panchanga terms and Shraddha ceremony names
 * into English, Kannada, Sanskrit, Telugu, and Tamil.
 */
object PanchangaLocalizer {

    fun localizeTithi(tithi: TithiInfo, language: AppLanguage): String {
        val names = when (language) {
            AppLanguage.ENGLISH -> listOf(
                "Prathama", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
                "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
                "Ekadashi", "Dvadashi", "Trayodashi", "Chaturdashi", "Purnima",
                "Prathama", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
                "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
                "Ekadashi", "Dvadashi", "Trayodashi", "Chaturdashi", "Amavasya"
            )
            AppLanguage.KANNADA -> listOf(
                "ಪ್ರಥಮಾ", "ದ್ವಿತೀಯಾ", "ತೃತೀಯಾ", "ಚತುರ್ಥೀ", "ಪಂಚಮೀ",
                "ಷಷ್ಠೀ", "ಸಪ್ತಮೀ", "ಅಷ್ಟಮೀ", "ನವಮೀ", "ದಶಮೀ",
                "ಏಕಾದಶೀ", "ದ್ವಾದಶೀ", "ತ್ರಯೋದಶೀ", "ಚತುರ್ದಶೀ", "ಪೂರ್ಣಿಮಾ",
                "ಪ್ರಥಮಾ", "ದ್ವಿತೀಯಾ", "ತೃತೀಯಾ", "ಚತುರ್ಥೀ", "ಪಂಚಮೀ",
                "ಷಷ್ಠೀ", "ಸಪ್ತಮೀ", "ಅಷ್ಟಮೀ", "ನವಮೀ", "ದಶಮೀ",
                "ಏಕಾದಶೀ", "ದ್ವಾದಶೀ", "ತ್ರಯೋದಶೀ", "ಚತುರ್ದಶೀ", "ಅಮಾವಾಸ್ಯಾ"
            )
            AppLanguage.SANSKRIT -> listOf(
                "ಪ್ರಥಮಾ", "ದ್ವಿತೀಯಾ", "ತೃತೀಯಾ", "ಚತುರ್ಥೀ", "ಪಂಚಮೀ",
                "ಷಷ್ಠೀ", "ಸಪ್ತಮೀ", "ಅಷ್ಟಮೀ", "ನವಮೀ", "ದಶಮೀ",
                "ಏಕಾದಶೀ", "ದ್ವಾದಶೀ", "ತ್ರಯೋದಶೀ", "ಚತುರ್ದಶೀ", "ಪೂರ್ಣಿಮಾ",
                "ಪ್ರಥಮಾ", "ದ್ವಿತೀಯಾ", "ತೃತೀಯಾ", "ಚತುರ್ಥೀ", "ಪಂಚಮೀ",
                "ಷಷ್ಠೀ", "ಸಪ್ತಮೀ", "ಅಷ್ಟಮೀ", "ನವಮೀ", "ದಶಮೀ",
                "ಏಕಾದಶೀ", "ದ್ವಾದಶೀ", "ತ್ರಯೋದಶೀ", "ಚತುರ್ದಶೀ", "ಅಮಾವಾಸ್ಯಾ"
            )
            AppLanguage.TELUGU -> listOf(
                "ప్రథమ", "ద్వితీయ", "తృతీయ", "చతుర్థి", "పంచమి",
                "షష్ఠి", "సప్తమి", "అష్టమి", "నవమి", "దశమి",
                "ఏకాదశి", "ద్వాదశి", "త్రయోదశి", "చతుర్దశి", "పూర్ణిమ",
                "ప్రథమ", "ద్వితీయ", "తృతీయ", "చతుర్థి", "పంచమి",
                "షష్ఠి", "సప్తమి", "అష్టమి", "నవమి", "దశమి",
                "ఏకాదశి", "ద్వాదశి", "త్రయోదశి", "చతుర్దశి", "అమావాస్య"
            )
            AppLanguage.TAMIL -> listOf(
                "பிரதமை", "த்விதீயை", "திருதியை", "சதுர்த்தி", "பஞ்சமி",
                "ஷஷ்டி", "ஸப்தமி", "அஷ்டமி", "நவமி", "தசமி",
                "ஏகாதசி", "த்வாதசி", "த்ரயோதசி", "சதுர்தசி", "பௌர்ணமி",
                "பிரதமை", "த்விதீயை", "திருதியை", "சதுர்த்தி", "பஞ்சமி",
                "ஷஷ்டி", "ஸப்தமி", "அஷ்டமி", "நவமி", "தசமி",
                "ஏகாதசி", "த்வாதசி", "த்ரயோதசி", "சதுர்தசி", "அமாவாசை"
            )
        }
        val idx = (tithi.number - 1).coerceIn(0, 29)
        return names[idx]
    }

    fun localizePaksha(paksha: Paksha, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> paksha.displayName
            AppLanguage.KANNADA -> if (paksha == Paksha.SHUKLA) "ಶುಕ್ಲ ಪಕ್ಷ" else "ಕೃಷ್ಣ ಪಕ್ಷ"
            AppLanguage.SANSKRIT -> if (paksha == Paksha.SHUKLA) "ಶುಕ್ಲಪಕ್ಷಃ" else "ಕೃಷ್ಣಪಕ್ಷಃ"
            AppLanguage.TELUGU -> if (paksha == Paksha.SHUKLA) "శుక్ల పక్షం" else "కృష్ణ పక్షం"
            AppLanguage.TAMIL -> if (paksha == Paksha.SHUKLA) "சுக்ல பக்ஷம்" else "க்ருஷ்ண பக்ஷம்"
        }
    }

    fun localizeMasa(masa: LunarMonth, isAdhika: Boolean, language: AppLanguage): String {
        val prefix = when (language) {
            AppLanguage.ENGLISH -> if (isAdhika) "Adhika" else "Nija"
            AppLanguage.KANNADA -> if (isAdhika) "ಅಧಿಕ" else "ನಿಜ"
            AppLanguage.SANSKRIT -> if (isAdhika) "ಅಧಿಕ" else "ನಿಜ"
            AppLanguage.TELUGU -> if (isAdhika) "అధిక" else "నిజ"
            AppLanguage.TAMIL -> if (isAdhika) "அதிக" else "நிஜ"
        }

        val baseName = when (language) {
            AppLanguage.ENGLISH -> masa.traditionalName
            AppLanguage.KANNADA -> when (masa) {
                LunarMonth.CHAITRA -> "ಚೈತ್ರ"
                LunarMonth.VAISHAKHA -> "ವೈಶಾಖ"
                LunarMonth.JYESHTHA -> "ಜ್ಯೇಷ್ಠ"
                LunarMonth.ASHADHA -> "ಆಷಾಢ"
                LunarMonth.SHRAVANA -> "ಶ್ರಾವಣ"
                LunarMonth.BHADRAPADA -> "ಭಾದ್ರಪದ"
                LunarMonth.ASHVINA -> "ಆಶ್ವಯುಜ"
                LunarMonth.KARTIKA -> "ಕಾರ್ತೀಕ"
                LunarMonth.MARGASHIRSHA -> "ಮಾರ್ಗಶಿರ"
                LunarMonth.PUSHYA -> "ಪುಷ್ಯ"
                LunarMonth.MAGHA -> "ಮಾಘ"
                LunarMonth.PHALGUNA -> "ಫಾಲ್ಗುಣ"
            }
            AppLanguage.SANSKRIT -> when (masa) {
                LunarMonth.CHAITRA -> "ಚೈತ್ರಃ"
                LunarMonth.VAISHAKHA -> "ವೈಶಾಖಃ"
                LunarMonth.JYESHTHA -> "ಜ್ಯೇಷ್ಠಃ"
                LunarMonth.ASHADHA -> "ಆಷಾಢಃ"
                LunarMonth.SHRAVANA -> "ಶ್ರಾವಣಃ"
                LunarMonth.BHADRAPADA -> "ಭಾದ್ರಪದಃ"
                LunarMonth.ASHVINA -> "ಆಶ್ವಯುಜಃ"
                LunarMonth.KARTIKA -> "ಕಾರ್ತೀಕಃ"
                LunarMonth.MARGASHIRSHA -> "ಮಾರ್ಗಶೀರ್ಷಃ"
                LunarMonth.PUSHYA -> "ಪುಷ್ಯಃ"
                LunarMonth.MAGHA -> "ಮಾಘಃ"
                LunarMonth.PHALGUNA -> "ಫಾಲ್ಗುಣಃ"
            }
            AppLanguage.TELUGU -> when (masa) {
                LunarMonth.CHAITRA -> "చైత్ర"
                LunarMonth.VAISHAKHA -> "వైశాఖ"
                LunarMonth.JYESHTHA -> "జ్యేష్ఠ"
                LunarMonth.ASHADHA -> "ఆషాఢ"
                LunarMonth.SHRAVANA -> "శ్రావణ"
                LunarMonth.BHADRAPADA -> "భాద్రపద"
                LunarMonth.ASHVINA -> "ఆశ్వయుజ"
                LunarMonth.KARTIKA -> "కార్తీక"
                LunarMonth.MARGASHIRSHA -> "మార్గశిర"
                LunarMonth.PUSHYA -> "పుష్య"
                LunarMonth.MAGHA -> "మాఘ"
                LunarMonth.PHALGUNA -> "ఫాల్గుణ"
            }
            AppLanguage.TAMIL -> when (masa) {
                LunarMonth.CHAITRA -> "சித்திரை"
                LunarMonth.VAISHAKHA -> "வைகாசி"
                LunarMonth.JYESHTHA -> "ஆனி (ஜ்யேஷ்டா)"
                LunarMonth.ASHADHA -> "ஆடி (ஆஷாட)"
                LunarMonth.SHRAVANA -> "ஆவணி (ஷ்ராவண)"
                LunarMonth.BHADRAPADA -> "புரட்டாசி (பாத்ரபத)"
                LunarMonth.ASHVINA -> "ஐப்பசி (ஆஷ்வின)"
                LunarMonth.KARTIKA -> "கார்த்திகை"
                LunarMonth.MARGASHIRSHA -> "மார்கழி"
                LunarMonth.PUSHYA -> "தை (புஷ்ய)"
                LunarMonth.MAGHA -> "மாசி (மாக)"
                LunarMonth.PHALGUNA -> "பங்குனி (பால்குண)"
            }
        }
        return "$prefix $baseName"
    }

    fun localizeTraditionalName(name: String, language: AppLanguage): String {
        if (language == AppLanguage.ENGLISH) return name

        // Extract sequence number if present, e.g. "Masika 1 — Adya Masika" -> seq=1, clean="Adya Masika"
        val seqMatch = Regex("""Masika\s+(\d+)\s*—\s*(.+)""").find(name)
        val varshikaMatch = Regex("""(?:Yearly Shraddha\s*—\s*)?(.+Varshika.+)""").find(name)

        if (seqMatch != null) {
            val seq = seqMatch.groupValues[1]
            val ritual = seqMatch.groupValues[2].trim()
            val localizedRitual = translateRitualName(ritual, language)
            val masikaWord = when (language) {
                AppLanguage.KANNADA -> "ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "మాసికం"
                AppLanguage.TAMIL -> "மாஸிகம்"
                else -> "Masika"
            }
            return "$masikaWord $seq — $localizedRitual"
        }

        if (varshikaMatch != null) {
            val ritual = varshikaMatch.groupValues[1].trim()
            val localizedRitual = translateRitualName(ritual, language)
            val yearlyWord = when (language) {
                AppLanguage.KANNADA -> "ಸಾಂವತ್ಸರಿಕ ಶ್ರಾದ್ಧ —"
                AppLanguage.SANSKRIT -> "ಸಾಂವತ್ಸರಿಕ ಶ್ರಾದ್ಧಮ್ —"
                AppLanguage.TELUGU -> "సాంవత్సరిక శ్రాద్ధం —"
                AppLanguage.TAMIL -> "ஸாம்வத்ஸரிக ஷ்ராத்தம் —"
                else -> "Yearly Shraddha —"
            }
            return "$yearlyWord $localizedRitual"
        }

        return translateRitualName(name, language)
    }

    private fun translateRitualName(name: String, language: AppLanguage): String {
        return when {
            name.contains("Adya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಆದ್ಯ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಆದ್ಯಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "ఆద్య మాసికం"
                AppLanguage.TAMIL -> "ஆத்ய மாஸிகம்"
                else -> name
            }
            name.contains("Unmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಊನಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "ఊనమాసికం"
                AppLanguage.TAMIL -> "ஊநமாஸிகம்"
                else -> name
            }
            name.contains("Dwitiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ದ್ವಿತೀಯಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "ద్వితీయ మాసికం"
                AppLanguage.TAMIL -> "த்விதீய மாஸிகம்"
                else -> name
            }
            name.contains("Traipakshika") -> when (language) {
                AppLanguage.KANNADA -> "ತ್ರೈಪಕ್ಷಿಕ"
                AppLanguage.SANSKRIT -> "ತ್ರೈಪಾಕ್ಷಿಕಮ್"
                AppLanguage.TELUGU -> "త్రైపాక్షికం"
                AppLanguage.TAMIL -> "த்ரைபாக்ஷிகம்"
                else -> name
            }
            name.contains("Tritiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ತೃತೀಯಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "తృతీయ మాసికం"
                AppLanguage.TAMIL -> "திருதீய மாஸிகம்"
                else -> name
            }
            name.contains("Chaturtha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಚತುರ್ಥಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "చతుర్థ మాసికం"
                AppLanguage.TAMIL -> "சதுர்த்த மாஸிகம்"
                else -> name
            }
            name.contains("Panchama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಪಂಚಮಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "పంచమ మాసికం"
                AppLanguage.TAMIL -> "பஞ்சம மாஸிகம்"
                else -> name
            }
            name.contains("Shashtha Masika") || name.contains("Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಷಷ್ಠ ಮಾಸಿಕ (ಷಾಣ್ಮಾಸಿಕ)"
                AppLanguage.SANSKRIT -> "ಷಷ್ಠಮಾಸಿಕಮ್ (ಷಾಣ್ಮಾಸಿಕಮ್)"
                AppLanguage.TELUGU -> "షష్ఠ మాసికం (షాణ్మాసికం)"
                AppLanguage.TAMIL -> "ஷஷ்ட மாஸிகம் (ஷாண்மாஸிகம்)"
                else -> name
            }
            name.contains("Una-Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಷಾಣ್ಮಾಸಿಕ (ಗೋದಾನ ಸಹಿತ)"
                AppLanguage.SANSKRIT -> "ಊನಷಾಣ್ಮಾಸಿಕಮ್ (ಗೋದಾನಸಹಿತಮ್)"
                AppLanguage.TELUGU -> "ఊనషాణ్మాసికం (గోదాన సహితం)"
                AppLanguage.TAMIL -> "ஊநஷாண்மாஸிகம் (கோதானத்துடன்)"
                else -> name
            }
            name.contains("Saptama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಸಪ್ತಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಸಪ್ತಮಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "సప్తమ మాసికం"
                AppLanguage.TAMIL -> "ஸப்தம மாஸிகம்"
                else -> name
            }
            name.contains("Ashtama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಅಷ್ಟಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಅಷ್ಟಮಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "అష్టమ మాసికం"
                AppLanguage.TAMIL -> "அஷ்டம மாஸிகம்"
                else -> name
            }
            name.contains("Navama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ನವಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ನವಮಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "నవమ మాసికం"
                AppLanguage.TAMIL -> "நவம மாஸிகம்"
                else -> name
            }
            name.contains("Dashama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದಶಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ದಶಮಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "దశమ మాసికం"
                AppLanguage.TAMIL -> "தசம மாஸிகம்"
                else -> name
            }
            name.contains("Ekadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಏಕಾದಶ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ಏಕಾದಶಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "ఏకాదశ మాసికం"
                AppLanguage.TAMIL -> "ஏகாதச மாஸிகம்"
                else -> name
            }
            name.contains("Dvadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಾದಶ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ದ್ವಾದಶಮಾಸಿಕಮ್"
                AppLanguage.TELUGU -> "ద్వాదశ మాసికం"
                AppLanguage.TAMIL -> "த்வாதச மாஸிகம்"
                else -> name
            }
            name.contains("Unabdika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಾಬ್ದಿಕ (ಊನವಾರ್ಷಿಕ)"
                AppLanguage.SANSKRIT -> "ಊನಾಬ್ದಿಕಮ್ (ಊನವಾರ್ಷಿಕಮ್)"
                AppLanguage.TELUGU -> "ఊనాబ్దికం (ఊనవార్షికం)"
                AppLanguage.TAMIL -> "ஊனாப்திகம் (ஊநவார்ஷிகம்)"
                else -> name
            }
            name.contains("Prathama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "ಪ್ರಥಮವಾರ್ಷಿಕಶ್ರಾದ್ಧಮ್"
                AppLanguage.TELUGU -> "ప్రథమ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "ப்ரதம வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Dvitiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "ದ್ವಿತೀಯವಾರ್ಷಿಕಶ್ರಾದ್ಧಮ್"
                AppLanguage.TELUGU -> "ద్వితీయ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "த்விதீய வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Tritiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "ತೃತೀಯವಾರ್ಷಿಕಶ್ರಾದ್ಧಮ್"
                AppLanguage.TELUGU -> "తృతీయ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "திருதீய வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Chaturtha Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "ಚತುರ್ಥವಾರ್ಷಿಕಶ್ರಾದ್ಧಮ್"
                AppLanguage.TELUGU -> "చతుర్థ వార్షಿಕ శ్రాద్ధం"
                AppLanguage.TAMIL -> "சதுர்த்த வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Panchama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "ಪಂಚಮವಾರ್ಷಿಕಶ್ರಾದ್ಧಮ್"
                AppLanguage.TELUGU -> "పంచమ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "பஞ்சம வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            else -> name
        }
    }
}

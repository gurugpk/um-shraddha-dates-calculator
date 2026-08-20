package com.shraddhacalendar.core.localization

import com.shraddhacalendar.core.models.*

/**
 * Localizes authentic traditional Panchanga terms and Shraddha ceremony names
 * into English, Kannada, Sanskrit (Devanagari), Telugu, and Tamil.
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
                "प्रथमा", "द्वितीया", "तृतीया", "चतुर्थी", "पञ्चमी",
                "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी",
                "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "पूर्णिमा",
                "प्रथमा", "द्वितीया", "तृतीया", "चतुर्थी", "पञ्चमी",
                "षष्ठी", "सप्तमी", "अष्टमी", "नवमी", "दशमी",
                "एकादशी", "द्वादशी", "त्रयोदशी", "चतुर्दशी", "अमावास्या"
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
            AppLanguage.SANSKRIT -> if (paksha == Paksha.SHUKLA) "शुक्लपक्षः" else "कृष्णपक्षः"
            AppLanguage.TELUGU -> if (paksha == Paksha.SHUKLA) "శుక్ల పక్షం" else "కృష్ణ పక్షం"
            AppLanguage.TAMIL -> if (paksha == Paksha.SHUKLA) "சுக்ல பக்ஷம்" else "க்ருஷ்ண பக்ஷம்"
        }
    }

    fun localizeMasa(masa: LunarMonth, isAdhika: Boolean, language: AppLanguage): String {
        val prefix = when (language) {
            AppLanguage.ENGLISH -> if (isAdhika) "Adhika" else "Nija"
            AppLanguage.KANNADA -> if (isAdhika) "ಅಧಿಕ" else "ನಿಜ"
            AppLanguage.SANSKRIT -> if (isAdhika) "अधिक" else "निज"
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
                LunarMonth.CHAITRA -> "चैत्रः"
                LunarMonth.VAISHAKHA -> "वैशाखः"
                LunarMonth.JYESHTHA -> "ज्येष्ठः"
                LunarMonth.ASHADHA -> "आषाढः"
                LunarMonth.SHRAVANA -> "श्रावणः"
                LunarMonth.BHADRAPADA -> "भाद्रपदः"
                LunarMonth.ASHVINA -> "आश्वयुजः"
                LunarMonth.KARTIKA -> "कार्तिकः"
                LunarMonth.MARGASHIRSHA -> "मार्गशीर्षः"
                LunarMonth.PUSHYA -> "पुष्यः"
                LunarMonth.MAGHA -> "माघः"
                LunarMonth.PHALGUNA -> "फाल्गुनः"
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
                AppLanguage.SANSKRIT -> "मासिकम्"
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
                AppLanguage.SANSKRIT -> "सांवत्सरिकश्राद्धम् —"
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
                AppLanguage.SANSKRIT -> "आद्यमासिकम्"
                AppLanguage.TELUGU -> "ఆద్య మాసికం"
                AppLanguage.TAMIL -> "ஆத்ய மாஸிகம்"
                else -> name
            }
            name.contains("Unmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "ऊनमासिकम्"
                AppLanguage.TELUGU -> "ఊనమాసికం"
                AppLanguage.TAMIL -> "ஊநமாஸிகம்"
                else -> name
            }
            name.contains("Dwitiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "द्वितीयमासिकम्"
                AppLanguage.TELUGU -> "ద్వితీయ మాసికం"
                AppLanguage.TAMIL -> "த்விதீய மாஸிகம்"
                else -> name
            }
            name.contains("Traipakshika") -> when (language) {
                AppLanguage.KANNADA -> "ತ್ರೈಪಕ್ಷಿಕ"
                AppLanguage.SANSKRIT -> "त्रैपाक्षिकम्"
                AppLanguage.TELUGU -> "త్రైపాక్షికం"
                AppLanguage.TAMIL -> "த்ரைபாக்ஷிகம்"
                else -> name
            }
            name.contains("Tritiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "तृतीयमासिकम्"
                AppLanguage.TELUGU -> "తృతీయ మాసికం"
                AppLanguage.TAMIL -> "திருதீய மாஸிகம்"
                else -> name
            }
            name.contains("Chaturtha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "चतुर्थमासिकम्"
                AppLanguage.TELUGU -> "చతుర్థ మాసికం"
                AppLanguage.TAMIL -> "சதுர்த்த மாஸிகம்"
                else -> name
            }
            name.contains("Panchama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "पञ्चममासिकम्"
                AppLanguage.TELUGU -> "పంచమ మాసికం"
                AppLanguage.TAMIL -> "பஞ்சம மாஸிகம்"
                else -> name
            }
            name.contains("Shashtha Masika") || name.contains("Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಷಷ್ಠ ಮಾಸಿಕ (ಷಾಣ್ಮಾಸಿಕ)"
                AppLanguage.SANSKRIT -> "षष्ठमासिकम् (षाण्मासिकम्)"
                AppLanguage.TELUGU -> "షష్ఠ మాసికం (షాణ్మాసికం)"
                AppLanguage.TAMIL -> "ஷஷ்ட மாஸிகம் (ஷாண்மாஸிகம்)"
                else -> name
            }
            name.contains("Una-Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಷಾಣ್ಮಾಸಿಕ (ಗೋದಾನ ಸಹಿತ)"
                AppLanguage.SANSKRIT -> "ऊनषाण्मासिकम् (गोदानसहितम्)"
                AppLanguage.TELUGU -> "ఊనషాణ్మాసికం (గోదాన సహితం)"
                AppLanguage.TAMIL -> "ஊநஷாண்மாஸிகம் (கோதானத்துடன்)"
                else -> name
            }
            name.contains("Saptama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಸಪ್ತಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "सप्तममासिकम्"
                AppLanguage.TELUGU -> "సప్తమ మాసికం"
                AppLanguage.TAMIL -> "ஸப்தம மாஸிகம்"
                else -> name
            }
            name.contains("Ashtama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಅಷ್ಟಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "अष्टममासिकम्"
                AppLanguage.TELUGU -> "అష్టమ మాసికం"
                AppLanguage.TAMIL -> "அஷ்டம மாஸிகம்"
                else -> name
            }
            name.contains("Navama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ನವಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "नवममासिकम्"
                AppLanguage.TELUGU -> "నవమ మాసికం"
                AppLanguage.TAMIL -> "நவம மாஸிகம்"
                else -> name
            }
            name.contains("Dashama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದಶಮ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "दशममासिकम्"
                AppLanguage.TELUGU -> "దశమ మాసికం"
                AppLanguage.TAMIL -> "தசம மாஸிகம்"
                else -> name
            }
            name.contains("Ekadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಏಕಾದಶ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "एकादशमासिकम्"
                AppLanguage.TELUGU -> "ఏకాదశ మాసికం"
                AppLanguage.TAMIL -> "ஏகாதச மாஸிகம்"
                else -> name
            }
            name.contains("Dvadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಾದಶ ಮಾಸಿಕ"
                AppLanguage.SANSKRIT -> "द्वादशमासिकम्"
                AppLanguage.TELUGU -> "ద్వాదశ మాసికం"
                AppLanguage.TAMIL -> "த்வாதச மாஸிகம்"
                else -> name
            }
            name.contains("Unabdika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಾಬ್ದಿಕ (ಊನವಾರ್ಷಿಕ)"
                AppLanguage.SANSKRIT -> "ऊनाब्दिकम् (ऊनवार्षिकम्)"
                AppLanguage.TELUGU -> "ఊనాబ్దికం (ఊనవార్షికం)"
                AppLanguage.TAMIL -> "ஊனாப்திகம் (ஊநவார்ஷிகம்)"
                else -> name
            }
            name.contains("Prathama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "प्रथमवार्षिकश्राद्धम्"
                AppLanguage.TELUGU -> "ప్రథమ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "ப்ரதம வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Dvitiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "द्वितीयवार्षिकश्राद्धम्"
                AppLanguage.TELUGU -> "ద్వితీయ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "த்விதீய வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Tritiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "तृतीयवार्षिकश्राद्धम्"
                AppLanguage.TELUGU -> "తృతీయ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "திருதீய வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Chaturtha Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "चतुर्थवार्षिकश्राद्धम्"
                AppLanguage.TELUGU -> "చతుర్థ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "சதுர்த்த வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            name.contains("Panchama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ"
                AppLanguage.SANSKRIT -> "पञ्चमवार्षिकश्राद्धम्"
                AppLanguage.TELUGU -> "పంచమ వార్షిక శ్రాద్ధం"
                AppLanguage.TAMIL -> "பஞ்சம வார்ஷிக ஷ்ராத்தம்"
                else -> name
            }
            else -> name
        }
    }

    fun localizePersonName(name: String, language: AppLanguage): String {
        return IndicTransliterator.transliterate(name, language)
    }

    fun localizeLocation(location: String, language: AppLanguage): String {
        return IndicTransliterator.transliterate(location, language)
    }

    fun localizeSamvatsara(samvatsara: String, language: AppLanguage): String {
        val clean = samvatsara.trim()
        val baseName = when (language) {
            AppLanguage.ENGLISH -> clean
            AppLanguage.KANNADA -> SAMVATSARA_KN[clean] ?: IndicTransliterator.transliterate(clean, language)
            AppLanguage.SANSKRIT -> SAMVATSARA_SA[clean] ?: IndicTransliterator.transliterate(clean, language)
            AppLanguage.TELUGU -> SAMVATSARA_TE[clean] ?: IndicTransliterator.transliterate(clean, language)
            AppLanguage.TAMIL -> SAMVATSARA_TA[clean] ?: IndicTransliterator.transliterate(clean, language)
        }

        val suffix = when (language) {
            AppLanguage.ENGLISH -> "Nama Samvatsara"
            AppLanguage.KANNADA -> "ನಾಮ ಸಂವತ್ಸರ"
            AppLanguage.SANSKRIT -> "नामसंवत्सरः"
            AppLanguage.TELUGU -> "నామ సంవత్సరం"
            AppLanguage.TAMIL -> "நாம ஸம்வத்ஸரம்"
        }

        return "$baseName $suffix"
    }

    fun localizeFullPanchanga(tithi: PanchangaTithi, language: AppLanguage): String {
        val samvatsara = localizeSamvatsara(tithi.samvatsara, language)
        val masa = localizeMasa(tithi.masa, tithi.isAdhikaMasa, language)
        val paksha = localizePaksha(tithi.tithi.paksha, language)
        val tithiName = localizeTithi(tithi.tithi, language)

        return when (language) {
            AppLanguage.ENGLISH -> "$samvatsara, $masa, $paksha, $tithiName"
            AppLanguage.KANNADA -> "$samvatsara, $masa, $paksha, $tithiName"
            AppLanguage.SANSKRIT -> "$samvatsara, $masa, $paksha, $tithiName"
            AppLanguage.TELUGU -> "$samvatsara, $masa, $paksha, $tithiName"
            AppLanguage.TAMIL -> "$samvatsara, $masa, $paksha $tithiName"
        }
    }

    private val SAMVATSARA_KN = mapOf(
        "Prabhava" to "ಪ್ರಭವ", "Vibhava" to "ವಿಭವ", "Shukla" to "ಶುಕ್ಲ", "Pramoda" to "ಪ್ರಮೋದ", "Prajotpatti" to "ಪ್ರಜೋತ್ಪತ್ತಿ",
        "Angirasa" to "ಆಂಗೀರಸ", "Shrimukha" to "ಶ್ರೀಮುಖ", "Bhava" to "ಭಾವ", "Yuva" to "ಯುವ", "Dhatru" to "ಧಾತು",
        "Ishvara" to "ಈಶ್ವರ", "Bahudhanya" to "ಬಹುಧಾನ್ಯ", "Pramathi" to "ಪ್ರಮಾಥಿ", "Vikrama" to "ವಿಕ್ರಮ", "Vrisha" to "ವೃಷ",
        "Chitrabhanu" to "ಚಿತ್ರಭಾನು", "Svabhanu" to "ಸ್ವಭಾನು", "Tarana" to "ತಾರಣ", "Parthiva" to "ಪಾರ್ಥಿವ", "Vyaya" to "ವ್ಯಯ",
        "Sarvajit" to "ಸರ್ವಜಿತ್", "Sarvadhari" to "ಸರ್ವಧಾರಿ", "Virodhi" to "ವಿರೋಧಿ", "Vikruti" to "ವಿಕೃತಿ", "Khara" to "ಖರ",
        "Nandana" to "ನಂದನ", "Vijaya" to "ವಿಜಯ", "Jaya" to "ಜಯ", "Manmatha" to "ಮನ್ಮಥ", "Durmukha" to "ದುರ್ಮುಖ",
        "Hevilambi" to "ಹೇವಿಳಂಬಿ", "Vilambi" to "ವಿಳಂಬಿ", "Vikari" to "ವಿಕಾರಿ", "Sharvari" to "ಶಾರ್ವರಿ", "Plava" to "ಪ್ಲವ",
        "Shubhakrit" to "ಶುಭಕೃತ್", "Shobhakrit" to "ಶೋಭಕೃತ್", "Krodhi" to "ಕ್ರೋಧಿ", "Vishvavasu" to "ವಿಶ್ವಾವಸು", "Parabhava" to "ಪರಾಭವ",
        "Plavanga" to "ಪ್ಲವಂಗ", "Kilaka" to "ಕೀಲಕ", "Saumya" to "ಸೌಮ್ಯ", "Sadharana" to "ಸಾಧಾರಣ", "Virodhikrit" to "ವಿರೋಧಿಕೃತ್",
        "Paridhavi" to "ಪರಿಧಾವಿ", "Pramadicha" to "ಪ್ರಮಾದೀಚ", "Ananda" to "ಆನಂದ", "Rakshasa" to "ರಾಕ್ಷಸ", "Nala" to "ನಳ",
        "Pingala" to "ಪಿಂಗಲ", "Kalayukti" to "ಕಾಲಯುಕ್ತಿ", "Siddharthi" to "ಸಿದ್ಧಾರ್ಥಿ", "Raudra" to "ರೌದ್ರ", "Durmati" to "ದುರ್ಮತಿ",
        "Dundubhi" to "ದುಂದುಭಿ", "Rudhirodgari" to "ರುಧಿರೋದಾಗಾರಿ", "Raktakshi" to "ರಕ್ತಾಕ್ಷಿ", "Krodhana" to "ಕ್ರೋಧನ", "Kshaya" to "ಕ್ಷಯ"
    )

    private val SAMVATSARA_SA = mapOf(
        "Prabhava" to "प्रभवः", "Vibhava" to "विभवः", "Shukla" to "शुक्लः", "Pramoda" to "प्रमोदः", "Prajotpatti" to "प्रजोत्पत्तिः",
        "Angirasa" to "आङ्गीरसः", "Shrimukha" to "श्रीमुखः", "Bhava" to "भावः", "Yuva" to "युवः", "Dhatru" to "धाता",
        "Ishvara" to "ईश्वरः", "Bahudhanya" to "बहुधान्यः", "Pramathi" to "प्रमाथी", "Vikrama" to "विक्रमः", "Vrisha" to "वृषः",
        "Chitrabhanu" to "चित्रभानुः", "Svabhanu" to "स्वभानुः", "Tarana" to "तारणः", "Parthiva" to "पार्थिवः", "Vyaya" to "व्ययः",
        "Sarvajit" to "सर्वजित्", "Sarvadhari" to "सर्वधारी", "Virodhi" to "विरोधी", "Vikruti" to "विकृतिः", "Khara" to "खरः",
        "Nandana" to "नन्दनः", "Vijaya" to "विजयः", "Jaya" to "जयः", "Manmatha" to "मन्मथः", "Durmukha" to "दुर्मुखः",
        "Hevilambi" to "हेविलम्बी", "Vilambi" to "विलम्बी", "Vikari" to "विकारी", "Sharvari" to "शार्वरी", "Plava" to "प्लवः",
        "Shubhakrit" to "शुभकृत्", "Shobhakrit" to "शोभकृत्", "Krodhi" to "क्रोधी", "Vishvavasu" to "विश्वावसुः", "Parabhava" to "पराभवः",
        "Plavanga" to "प्लवङ्गः", "Kilaka" to "कीलकः", "Saumya" to "सौम्यः", "Sadharana" to "साधारणः", "Virodhikrit" to "विरोधकृत्",
        "Paridhavi" to "परिधावी", "Pramadicha" to "प्रमादीचः", "Ananda" to "आनन्दः", "Rakshasa" to "राक्षसः", "Nala" to "नलः",
        "Pingala" to "पिङ्गलः", "Kalayukti" to "कालयुक्तिः", "Siddharthi" to "सिद्धार्थी", "Raudra" to "रौद्रः", "Durmati" to "दुर्मतिः",
        "Dundubhi" to "दुन्दुभिः", "Rudhirodgari" to "रुधिरोद्गारी", "Raktakshi" to "रक्ताक्षी", "Krodhana" to "क्रोधनः", "Kshaya" to "क्षयः"
    )

    private val SAMVATSARA_TE = mapOf(
        "Prabhava" to "ప్రభవ", "Vibhava" to "విభవ", "Shukla" to "శుక్ల", "Pramoda" to "ప్రమోద", "Prajotpatti" to "ప్రజోత్పత్తి",
        "Angirasa" to "ఆంగీరస", "Shrimukha" to "శ్రీముఖ", "Bhava" to "భావ", "Yuva" to "యువ", "Dhatru" to "ధాత",
        "Ishvara" to "ఈశ్వర", "Bahudhanya" to "బహుధాన్య", "Pramathi" to "ప్రమాథి", "Vikrama" to "విక్రమ", "Vrisha" to "వృష",
        "Chitrabhanu" to "చిత్రభాను", "Svabhanu" to "స్వభాను", "Tarana" to "తారణ", "Parthiva" to "పార్థివ", "Vyaya" to "వ్యయ",
        "Sarvajit" to "సర్వజిత్", "Sarvadhari" to "సర్వధారి", "Virodhi" to "విరోధి", "Vikruti" to "వికృతి", "Khara" to "ఖర",
        "Nandana" to "నందన", "Vijaya" to "విజయ", "Jaya" to "జయ", "Manmatha" to "మన్మథ", "Durmukha" to "దుర్ముఖ",
        "Hevilambi" to "హేవిలంబి", "Vilambi" to "విలంబి", "Vikari" to "వికారి", "Sharvari" to "శార్వరి", "Plava" to "ప్లవ",
        "Shubhakrit" to "శుభకృత్", "Shobhakrit" to "శోభకృత్", "Krodhi" to "క్రోధి", "Vishvavasu" to "విశ్వావసు", "Parabhava" to "పరాభవ",
        "Plavanga" to "ప్లవంగ", "Kilaka" to "కీలక", "Saumya" to "సౌమ్య", "Sadharana" to "సాధారణ", "Virodhikrit" to "విరోధికృత్",
        "Paridhavi" to "పరిధావి", "Pramadicha" to "ప్రమాదీచ", "Ananda" to "ఆనంద", "Rakshasa" to "రాక్షస", "Nala" to "నల",
        "Pingala" to "పింగల", "Kalayukti" to "కాలయుక్తి", "Siddharthi" to "సిద్ధార్థి", "Raudra" to "రౌద్ర", "Durmati" to "దుర్మతి",
        "Dundubhi" to "దుందుభి", "Rudhirodgari" to "రుధిరోద్గారి", "Raktakshi" to "రక్తాక్షి", "Krodhana" to "క్రోధన", "Kshaya" to "క్షయ"
    )

    private val SAMVATSARA_TA = mapOf(
        "Prabhava" to "ப்ரபவ", "Vibhava" to "விபவ", "Shukla" to "சுக்ல", "Pramoda" to "ப்ரமோத", "Prajotpatti" to "ப்ரஜோத்பத்தி",
        "Angirasa" to "ஆங்கீரஸ", "Shrimukha" to "ஸ்ரீமுக", "Bhava" to "பாவ", "Yuva" to "யுவ", "Dhatru" to "தாது",
        "Ishvara" to "ஈஸ்வர", "Bahudhanya" to "பஹுதான்ய", "Pramathi" to "ப்ரமாதி", "Vikrama" to "விக்ரம", "Vrisha" to "வ்ருஷ",
        "Chitrabhanu" to "சித்ரபானு", "Svabhanu" to "ஸ்வபானு", "Tarana" to "தாரண", "Parthiva" to "பார்த்திவ", "Vyaya" to "வ்யய",
        "Sarvajit" to "ஸர்வஜித்", "Sarvadhari" to "ஸர்வதாரி", "Virodhi" to "விரோதி", "Vikruti" to "விக்ருதி", "Khara" to "கர",
        "Nandana" to "நந்தன", "Vijaya" to "விஜய", "Jaya" to "ஜய", "Manmatha" to "மன்மத", "Durmukha" to "துர்முக",
        "Hevilambi" to "ஹேவிளம்பி", "Vilambi" to "விளம்பி", "Vikari" to "விகாரி", "Sharvari" to "சார்வரி", "Plava" to "ப்ளவ",
        "Shubhakrit" to "சுபக்ருத்", "Shobhakrit" to "சோபக்ருத்", "Krodhi" to "க்ரோதி", "Vishvavasu" to "விஸ்வாவஸு", "Parabhava" to "பராபவ",
        "Plavanga" to "ப்ளவங்க", "Kilaka" to "கீலக", "Saumya" to "ஸௌம்ய", "Sadharana" to "ஸாதாரண", "Virodhikrit" to "விரோதிக்ருத்",
        "Paridhavi" to "பரிதாவி", "Pramadicha" to "ப்ரமாதீச", "Ananda" to "ஆனந்த", "Rakshasa" to "ராக்ஷஸ", "Nala" to "நள",
        "Pingala" to "பிங்கல", "Kalayukti" to "காலயுக்தி", "Siddharthi" to "ஸித்தார்த்தி", "Raudra" to "ரௌத்ர", "Durmati" to "துர்மதி",
        "Dundubhi" to "துந்துபி", "Rudhirodgari" to "ருதிரோத்காரி", "Raktakshi" to "ரக்தாக்ஷி", "Krodhana" to "க்ரோதன", "Kshaya" to "க்ஷய"
    )
}

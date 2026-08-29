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
        // Extract sequence number if present, e.g. "Masika 1 — Adya Masika" -> seq=1, clean="Adya Masika"
        val seqMatch = Regex("""Masika\s+(\d+)\s*—\s*(.+)""").find(name)
        val yearlyPrefixMatch = Regex("""(?:Yearly Shraddha|Annual Shraddha)\s*—\s*(.+)""").find(name)

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

        if (yearlyPrefixMatch != null) {
            val ritual = yearlyPrefixMatch.groupValues[1].trim()
            return translateRitualName(ritual, language)
        }

        return translateRitualName(name, language)
    }

    private fun translateRitualName(name: String, language: AppLanguage): String {
        return when {
            name.contains("Adya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)"
                AppLanguage.SANSKRIT -> "आद्यमासिकम् (13 तमदिनम्)"
                AppLanguage.TELUGU -> "ఆద్య మాసికం (13వ రోజు)"
                AppLanguage.TAMIL -> "ஆத்ய மாஸிகம் (13ஆம் நாள்)"
                AppLanguage.ENGLISH -> "Adya Masika (13th Day)"
            }
            name.contains("Unmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಮಾಸಿಕ (27ನೇ ದಿನ)"
                AppLanguage.SANSKRIT -> "ऊनमासिकम् (27 तमदिनम्)"
                AppLanguage.TELUGU -> "ఊనమాసికం (27వ రోజు)"
                AppLanguage.TAMIL -> "ஊநமாஸிகம் (27ஆம் நாள்)"
                AppLanguage.ENGLISH -> "Unmasika (27th Day)"
            }
            name.contains("Dwitiya Masika") || name.contains("Dvitiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ಮಾಸಿಕ (2ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "द्वितीयमासिकम् (द्वितीयमासतिथिः)"
                AppLanguage.TELUGU -> "ద్వితీయ మాసికం (2వ మాస తిథి)"
                AppLanguage.TAMIL -> "த்விதீய மாஸிகம் (2ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Dvitiya Masika (2nd Month Tithi)"
            }
            name.contains("Traipakshika") -> when (language) {
                AppLanguage.KANNADA -> "ತ್ರೈಪಕ್ಷಿಕ (45ನೇ ದಿನ)"
                AppLanguage.SANSKRIT -> "त्रैपाक्षिकम् (45 तमदिनम्)"
                AppLanguage.TELUGU -> "త్రైపాక్షికం (45వ రోజు)"
                AppLanguage.TAMIL -> "த்ரைபாக்ஷிகம் (45ஆம் நாள்)"
                AppLanguage.ENGLISH -> "Traipakshika (45th Day)"
            }
            name.contains("Tritiya Masika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ಮಾಸಿಕ (3ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "तृतीयमासिकम् (तृतीयमासतिथिः)"
                AppLanguage.TELUGU -> "తృతీయ మాసికం (3వ మాస తిథి)"
                AppLanguage.TAMIL -> "திருதீய மாஸிகம் (3ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Tritiya Masika (3rd Month Tithi)"
            }
            name.contains("Chaturtha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ಮಾಸಿಕ (4ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "चतुर्थमासिकम् (चतुर्थमासतिथिः)"
                AppLanguage.TELUGU -> "చతుర్థ మాసికం (4వ మాస తిథి)"
                AppLanguage.TAMIL -> "சதுர்த்த மாஸிகம் (4ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Chaturtha Masika (4th Month Tithi)"
            }
            name.contains("Panchama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ಮಾಸಿಕ (5ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "पञ्चममासिकम् (पञ्चममासतिथिः)"
                AppLanguage.TELUGU -> "పంచమ మాసికం (5వ మాస తిథి)"
                AppLanguage.TAMIL -> "பஞ்சம மாஸிகம் (5ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Panchama Masika (5th Month Tithi)"
            }
            name.contains("Una-Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಷಾಣ್ಮಾಸಿಕ (170ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)"
                AppLanguage.SANSKRIT -> "ऊनषाण्मासिकम् (170 तमदिनम् / गोदानसहितम्)"
                AppLanguage.TELUGU -> "ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)"
                AppLanguage.TAMIL -> "ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)"
                AppLanguage.ENGLISH -> "Una-Shanmasika (~170th Day / Godana)"
            }
            name.contains("Shashtha Masika") || name.contains("Shanmasika") -> when (language) {
                AppLanguage.KANNADA -> if (name.contains("Shanmasika")) "ಷಾಣ್ಮಾಸಿಕ (6ನೇ ಮಾಸಿಕ ತಿಥಿ)" else "ಷಷ್ಠ ಮಾಸಿಕ (6ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> if (name.contains("Shanmasika")) "षाण्मासिकम् (षष्ठमासतिथिः)" else "षष्ठमासिकम् (षष्ठमासतिथिः)"
                AppLanguage.TELUGU -> if (name.contains("Shanmasika")) "షాణ్మాసికం (6వ మాస తిథి)" else "షష్ఠ మాసికం (6వ మాస తిథి)"
                AppLanguage.TAMIL -> if (name.contains("Shanmasika")) "ஷான்மாஸிகம் (6ஆம் மாத திதி)" else "ஷஷ்ட மாஸிகம் (6ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> if (name.contains("Shanmasika")) "Shanmasika (6th Month Tithi)" else "Shashtha Masika (6th Month Tithi)"
            }
            name.contains("Saptama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಸಪ್ತಮ ಮಾಸಿಕ (7ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "सप्तममासिकम् (सप्तममासतिथिः)"
                AppLanguage.TELUGU -> "సప్తమ మాసికం (7వ మాస తిథి)"
                AppLanguage.TAMIL -> "ஸப்தம மாஸிகம் (7ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Saptama Masika (7th Month Tithi)"
            }
            name.contains("Ashtama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಅಷ್ಟಮ ಮಾಸಿಕ (8ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "अष्टममासिकम् (अष्टममासतिथिः)"
                AppLanguage.TELUGU -> "అష్టమ మాసికం (8వ మాస తిథి)"
                AppLanguage.TAMIL -> "அஷ்டம மாஸிகம் (8ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Ashtama Masika (8th Month Tithi)"
            }
            name.contains("Navama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ನವಮ ಮಾಸಿಕ (9ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "नवममासिकम् (नवममासतिथिः)"
                AppLanguage.TELUGU -> "నవమ మాసికం (9వ మాస తిథి)"
                AppLanguage.TAMIL -> "நவம மாஸிகம் (9ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Navama Masika (9th Month Tithi)"
            }
            name.contains("Dashama Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದಶಮ ಮಾಸಿಕ (10ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "दशममासिकम् (दशममासतिथिः)"
                AppLanguage.TELUGU -> "దశమ మాసికం (10వ మాస తిథి)"
                AppLanguage.TAMIL -> "தசம மாஸிகம் (10ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Dashama Masika (10th Month Tithi)"
            }
            name.contains("Ekadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ಏಕಾದಶ ಮಾಸಿಕ (11ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "एकादशमासिकम् (एकादशमासतिथिः)"
                AppLanguage.TELUGU -> "ఏకాదశ మాసికం (11వ మాస తిథి)"
                AppLanguage.TAMIL -> "ஏகாதச மாஸிகம் (11ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Ekadasha Masika (11th Month Tithi)"
            }
            name.contains("Unabdika") -> when (language) {
                AppLanguage.KANNADA -> "ಊನಾಬ್ದಿಕ (340ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)"
                AppLanguage.SANSKRIT -> "ऊनाब्दिकम् (340 तमदिनम् / ऊनवार्षिकम्)"
                AppLanguage.TELUGU -> "ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)"
                AppLanguage.TAMIL -> "ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)"
                AppLanguage.ENGLISH -> "Unabdika (~340th Day / Una-Varshika)"
            }
            name.contains("Dvadasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಾದಶ ಮಾಸಿಕ (12ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "द्वादशमासिकम् (द्वादशमासतिथिः)"
                AppLanguage.TELUGU -> "ద్వాదశ మాసికం (12వ మాస తిథి)"
                AppLanguage.TAMIL -> "த்வாதச மாஸிகம் (12ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Dvadasha Masika (12th Month Tithi)"
            }
            name.contains("Trayodasha Masika") -> when (language) {
                AppLanguage.KANNADA -> "ತ್ರಯೋದಶ ಮಾಸಿಕ (13ನೇ ಮಾಸಿಕ ತಿಥಿ)"
                AppLanguage.SANSKRIT -> "त्रयोदशमासिकम् (त्रयोदशमासतिथिः)"
                AppLanguage.TELUGU -> "త్రయోదశ మాసికం (13వ మాస తిథి)"
                AppLanguage.TAMIL -> "த்ரயோதச மாஸிகம் (13ஆம் மாத திதி)"
                AppLanguage.ENGLISH -> "Trayodasha Masika (13th Month Tithi)"
            }
            name.contains("Prathama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)"
                AppLanguage.TELUGU -> "ప్రథమ వార్షిక శ్రాద్ధం (1వ ఏడు)"
                AppLanguage.TAMIL -> "ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Prathama Varshika Shraddha (1st Death Anniversary)"
            }
            name.contains("Dvitiya Varshika") || name.contains("Dwitiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ದ್ವಿತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (2ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "द्वितीयवार्षिकश्राद्धम् (द्वितीयवर्षीयम्)"
                AppLanguage.TELUGU -> "ద్వితీయ వార్షిక శ్రాద్ధం (2వ ఏడు)"
                AppLanguage.TAMIL -> "த்விதீய வார்ஷிக ஷ்ராத்தம் (2ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Dvitiya Varshika Shraddha (2nd Death Anniversary)"
            }
            name.contains("Tritiya Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ತೃತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (3ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "तृतीयवार्षिकश्राद्धम् (तृतीयवर्षीयम्)"
                AppLanguage.TELUGU -> "తృతీయ వార్షిక శ్రాద్ధం (3వ ఏడు)"
                AppLanguage.TAMIL -> "திருதீய வார்ஷிக ஷ்ராத்தம் (3ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Tritiya Varshika Shraddha (3rd Death Anniversary)"
            }
            name.contains("Chaturtha Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಚತುರ್ಥ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (4ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "चतुर्थवार्षिकश्राद्धम् (चतुर्थवर्षीयम्)"
                AppLanguage.TELUGU -> "చతుర్థ వార్షిక శ్రాద్ధం (4వ ఏడు)"
                AppLanguage.TAMIL -> "சதுர்த்த வார்ஷிக ஷ்ராத்தம் (4ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Chaturtha Varshika Shraddha (4th Death Anniversary)"
            }
            name.contains("Panchama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಪಂಚಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (5ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "पञ्चमवार्षिकश्राद्धम् (पञ्चमवर्षीयम्)"
                AppLanguage.TELUGU -> "పంచమ వార్షిక శ్రాద్ధం (5వ ఏడు)"
                AppLanguage.TAMIL -> "பஞ்சம வார்ஷிக ஷ்ராத்தம் (5ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Panchama Varshika Shraddha (5th Death Anniversary)"
            }
            name.contains("Shashtha Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಷಷ್ಠ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (6ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "षष्ठवार्षिकश्राद्धम् (षष्ठवर्षीयम्)"
                AppLanguage.TELUGU -> "షష్ఠ వార్షిక శ్రాద్ధం (6వ ఏడు)"
                AppLanguage.TAMIL -> "ஷஷ்ட வார்ஷிக ஷ்ராத்தம் (6ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Shashtha Varshika Shraddha (6th Death Anniversary)"
            }
            name.contains("Saptama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಸಪ್ತಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (7ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "सप्तमवार्षिकश्राद्धम् (सप्तमवर्षीयम्)"
                AppLanguage.TELUGU -> "సప్తమ వార్షిక శ్రాద్ధం (7వ ఏడు)"
                AppLanguage.TAMIL -> "ஸப்தம வார்ஷிக ஷ்ராத்தம் (7ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Saptama Varshika Shraddha (7th Death Anniversary)"
            }
            name.contains("Ashtama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ಅಷ್ಟಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (8ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "अष्टमवार्षिकश्राद्धम् (अष्टमवर्षीयम्)"
                AppLanguage.TELUGU -> "అష్టమ వార్షిక శ్రాద్ధం (8వ ఏడు)"
                AppLanguage.TAMIL -> "அஷ்டம வார்ஷிக ஷ்ராத்தம் (8ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Ashtama Varshika Shraddha (8th Death Anniversary)"
            }
            name.contains("Navama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ನವಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (9ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "नवमवार्षिकश्राद्धम् (नवमवर्षीयम्)"
                AppLanguage.TELUGU -> "నవమ వార్షిక శ్రాద్ధం (9వ ఏడు)"
                AppLanguage.TAMIL -> "நவம வார்ஷிக ஷ்ராத்தம் (9ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Navama Varshika Shraddha (9th Death Anniversary)"
            }
            name.contains("Dashama Varshika") -> when (language) {
                AppLanguage.KANNADA -> "ದಶಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (10ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)"
                AppLanguage.SANSKRIT -> "दशमवार्षिकश्राद्धम् (दशमवर्षीयम्)"
                AppLanguage.TELUGU -> "దశమ వార్షిక శ్రాద్ధం (10వ ఏడు)"
                AppLanguage.TAMIL -> "தசம வார்ஷிக ஷ்ராத்தம் (10ஆம் ஆண்டு)"
                AppLanguage.ENGLISH -> "Dashama Varshika Shraddha (10th Death Anniversary)"
            }
            name.contains("Mahalaya") || name.contains("Pitru Paksha") -> when (language) {
                AppLanguage.KANNADA -> "ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)"
                AppLanguage.SANSKRIT -> "महालयपक्षश्राद्धम् (पितृपक्षः)"
                AppLanguage.TELUGU -> "మహాలయ పక్ష శ్రాద్ధం (పితృ పక్షం)"
                AppLanguage.TAMIL -> "மஹாலய பக்ஷ ஷ்ராத்தம் (பித்ரு பக்ஷம்)"
                AppLanguage.ENGLISH -> "Mahalaya Paksha Shraddha (Pitru Paksha)"
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

    fun localizeTradition(tradition: MadhwaTradition, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> tradition.displayNameEnglish
            AppLanguage.KANNADA -> when (tradition) {
                MadhwaTradition.UTTARADI_MATHA -> "ಶ್ರೀ ಉತ್ತರಾದಿ ಮಠ"
                MadhwaTradition.MANTRALAYA_MUTT -> "ಶ್ರೀ ರಾಘವೇಂದ್ರ ಸ್ವಾಮಿ ಮಠ (ಮಂತ್ರಾಲಯ)"
                MadhwaTradition.UDUPI_ASHTA_MATHA -> "ಶ್ರೀ ಉಡುಪಿ ಅಷ್ಟ ಮಠ"
            }
            AppLanguage.SANSKRIT -> when (tradition) {
                MadhwaTradition.UTTARADI_MATHA -> "श्रीउत्तरादिमठः"
                MadhwaTradition.MANTRALAYA_MUTT -> "श्रीराघवेंद्रस्वामिमठः (मन्त्रालयम्)"
                MadhwaTradition.UDUPI_ASHTA_MATHA -> "श्रीउडुपी-अष्टमठः"
            }
            AppLanguage.TELUGU -> when (tradition) {
                MadhwaTradition.UTTARADI_MATHA -> "శ్రీ ఉత్తరాది మఠం"
                MadhwaTradition.MANTRALAYA_MUTT -> "శ్రీ రాఘవేంద్ర స్వామి మఠం (మంత్రాలయం)"
                MadhwaTradition.UDUPI_ASHTA_MATHA -> "శ్రీ ఉడుపి అష్ట మఠం"
            }
            AppLanguage.TAMIL -> when (tradition) {
                MadhwaTradition.UTTARADI_MATHA -> "ஸ்ரீ உத்தராதி மடம்"
                MadhwaTradition.MANTRALAYA_MUTT -> "ஸ்ரீ ராகவேந்திர சுவாமி மடம் (மந்த்ராலயம்)"
                MadhwaTradition.UDUPI_ASHTA_MATHA -> "ஸ்ரீ உடுப்பி அஷ்ட மடம்"
            }
        }
    }

    fun localizeRelationship(relationship: FamilyRelationship, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> relationship.displayNameEnglish
            AppLanguage.KANNADA -> when (relationship) {
                FamilyRelationship.FATHER -> "ತಂದೆ"
                FamilyRelationship.MOTHER -> "ತಾಯಿ"
                FamilyRelationship.GRANDFATHER -> "ಅಜ್ಜ"
                FamilyRelationship.GRANDMOTHER -> "ಅಜ್ಜಿ"
                FamilyRelationship.HUSBAND -> "ಪತಿ"
                FamilyRelationship.WIFE -> "ಪತ್ನಿ"
                FamilyRelationship.BROTHER -> "ಸಹೋದರ"
                FamilyRelationship.SISTER -> "ಸಹೋದರಿ"
                FamilyRelationship.SON -> "ಮಗ"
                FamilyRelationship.DAUGHTER -> "ಮಗಳು"
                FamilyRelationship.UNCLE -> "ಚಿಕ್ಕಪ್ಪ / ದೊಡ್ಡಪ್ಪ"
                FamilyRelationship.AUNT -> "ಚಿಕ್ಕಮ್ಮ / ದೊಡ್ಡಮ್ಮ"
                FamilyRelationship.OTHER -> "ಇತರ ಬಂಧುಗಳು"
            }
            AppLanguage.SANSKRIT -> when (relationship) {
                FamilyRelationship.FATHER -> "पिता"
                FamilyRelationship.MOTHER -> "माता"
                FamilyRelationship.GRANDFATHER -> "पितामहः"
                FamilyRelationship.GRANDMOTHER -> "पितामही"
                FamilyRelationship.HUSBAND -> "पतिः"
                FamilyRelationship.WIFE -> "पत्नी"
                FamilyRelationship.BROTHER -> "भ्राता"
                FamilyRelationship.SISTER -> "भगिनी"
                FamilyRelationship.SON -> "पुत्रः"
                FamilyRelationship.DAUGHTER -> "पुत्री"
                FamilyRelationship.UNCLE -> "पितृव्यः"
                FamilyRelationship.AUNT -> "पितृष्वसा"
                FamilyRelationship.OTHER -> "अन्यबान्धवाः"
            }
            AppLanguage.TELUGU -> when (relationship) {
                FamilyRelationship.FATHER -> "తండ్రి"
                FamilyRelationship.MOTHER -> "తల్లి"
                FamilyRelationship.GRANDFATHER -> "తాతయ్య"
                FamilyRelationship.GRANDMOTHER -> "నానమ్మ"
                FamilyRelationship.HUSBAND -> "భర్త"
                FamilyRelationship.WIFE -> "భార్య"
                FamilyRelationship.BROTHER -> "సోదరుడు"
                FamilyRelationship.SISTER -> "సోదరి"
                FamilyRelationship.SON -> "కుమారుడు"
                FamilyRelationship.DAUGHTER -> "కుమార్తె"
                FamilyRelationship.UNCLE -> "బాబాయ్ / పెదనాన్న"
                FamilyRelationship.AUNT -> "పిన్ని / పెద్దమ్మ"
                FamilyRelationship.OTHER -> "ఇతర బంధువులు"
            }
            AppLanguage.TAMIL -> when (relationship) {
                FamilyRelationship.FATHER -> "தந்தை"
                FamilyRelationship.MOTHER -> "தாய்"
                FamilyRelationship.GRANDFATHER -> "தாத்தா"
                FamilyRelationship.GRANDMOTHER -> "பாட்டி"
                FamilyRelationship.HUSBAND -> "கணவர்"
                FamilyRelationship.WIFE -> "மனைவி"
                FamilyRelationship.BROTHER -> "சகோதரர்"
                FamilyRelationship.SISTER -> "சகோதரி"
                FamilyRelationship.SON -> "மகன்"
                FamilyRelationship.DAUGHTER -> "மகள்"
                FamilyRelationship.UNCLE -> "சித்தப்பா / பெரியப்பா"
                FamilyRelationship.AUNT -> "சித்தி / பெரியம்மா"
                FamilyRelationship.OTHER -> "மற்ற உறவினர்கள்"
            }
        }
    }

    fun localizeDaysRemaining(days: Long, language: AppLanguage): String {
        return when (language) {
            AppLanguage.ENGLISH -> when {
                days == 0L -> "Today"
                days == 1L -> "Tomorrow"
                days > 1L -> "$days Days Remaining"
                days == -1L -> "Yesterday"
                else -> "${-days} Days Ago"
            }
            AppLanguage.KANNADA -> when {
                days == 0L -> "ಇಂದು"
                days == 1L -> "ನಾಳೆ"
                days > 1L -> "$days ದಿನಗಳು ಉಳಿದಿವೆ"
                days == -1L -> "ನಿನ್ನೆ"
                else -> "${-days} ದಿನಗಳ ಹಿಂದೆ"
            }
            AppLanguage.SANSKRIT -> when {
                days == 0L -> "अद्य"
                days == 1L -> "श्वः"
                days > 1L -> "$days दिनानि अवशिष्टानि"
                days == -1L -> "ह्यः"
                else -> "${-days} दिनेभ्यः पूर्वम्"
            }
            AppLanguage.TELUGU -> when {
                days == 0L -> "ఈరోజు"
                days == 1L -> "రేపు"
                days > 1L -> "$days రోజులు మిగిలి ఉన్నాయి"
                days == -1L -> "నిన్న"
                else -> "${-days} రోజుల క్రితం"
            }
            AppLanguage.TAMIL -> when {
                days == 0L -> "இன்று"
                days == 1L -> "நாளை"
                days > 1L -> "$days நாட்கள் உள்ளன"
                days == -1L -> "நேற்று"
                else -> "${-days} நாட்களுக்கு முன்"
            }
        }
    }

    fun localizeYearTitle(year: Int, startYear: Int, endYear: Int, language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> "ವರ್ಷ $year ($startYear - $endYear)"
            AppLanguage.SANSKRIT -> "$year-वर्षम् ($startYear - $endYear)"
            AppLanguage.TELUGU -> "సంవత్సరం $year ($startYear - $endYear)"
            AppLanguage.TAMIL -> "ஆண்டு $year ($startYear - $endYear)"
            AppLanguage.ENGLISH -> "Year $year ($startYear - $endYear)"
        }
    }

    fun localizeYearTitleString(rawTitle: String, language: AppLanguage): String {
        if (language == AppLanguage.ENGLISH) return rawTitle
        val match = Regex("""Year\s+(\d+)\s*\((.+)\)""").find(rawTitle)
        if (match != null) {
            val yIdx = match.groupValues[1].toIntOrNull() ?: 1
            val years = match.groupValues[2].split("-").map { it.trim().toIntOrNull() ?: 0 }
            val startYear = if (years.isNotEmpty()) years[0] else 0
            val endYear = if (years.size > 1) years[1] else 0
            return localizeYearTitle(yIdx, startYear, endYear, language)
        }
        return rawTitle
    }

    fun localizeExplanation(explanation: String, language: AppLanguage): String {
        if (language == AppLanguage.ENGLISH) return explanation
        val lower = explanation.lowercase()
        return when (language) {
            AppLanguage.KANNADA -> {
                when {
                    lower.contains("13th day") -> "ಮರಣದ ನಂತರದ 13ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುತ್ತದೆ (ಆಶೌಚ ಮುಕ್ತಾಯ)"
                    lower.contains("day 27") -> "ಮರಣದ ನಂತರದ 27ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುವ ಊನಮಾಸಿಕ ವಿಧಿ"
                    lower.contains("day 45") -> "ಮರಣದ ನಂತರದ 45ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುವ ತ್ರೈಪಾಕ್ಷಿಕ ವಿಧಿ"
                    lower.contains("godana") || lower.contains("una-shanmasika") -> "ವೈತರಣೀ ಗೋದಾನ ಸಹಿತ ಊನ-ಷಾಣ್ಮಾಸಿಕ ವಿಧಿ"
                    lower.contains("day 350") || lower.contains("unabdika") -> "1 ವರ್ಷ ಮುಗಿಯುವ ಮುನ್ನ 350ನೇ ದಿನದಂದು ಆಚರಿಸಲಾಗುವ ಊನಾಬ್ದಿಕ ವಿಧಿ"
                    lower.contains("eka aparahna vyapti") -> "ಏಕ ಅಪರಾಹ್ಣ ವ್ಯಾಪ್ತಿ: ತಿಥಿಯು ಅಪರಾಹ್ಣ ಕಾಲದಲ್ಲಿ ಮಾತ್ರ ವ್ಯಾಪಿಸಿರುವುದರಿಂದ ನಿರ್ಧರಿಸಲಾಗಿದೆ."
                    lower.contains("ubhaya vyapti") -> "ಉಭಯ ವ್ಯಾಪ್ತಿ: ಗರಿಷ್ಠ ಅಪರಾಹ್ಣ ವ್ಯಾಪ್ತಿಯುಳ್ಳ ದಿನವನ್ನು ಆಯ್ಕೆ ಮಾಡಲಾಗಿದೆ."
                    lower.contains("mahalaya") -> "ಭಾದ್ರಪದ ಕೃಷ್ಣ ಪಕ್ಷದಲ್ಲಿ (ಮಹಾಲಯ ಪಿತೃ ಪಕ್ಷ) ಮೃತ ತಿಥಿಯಂದು ಅಪರಾಹ್ಣ ಕಾಲದಲ್ಲಿ ಆಚರಿಸಲಾಗುತ್ತದೆ."
                    lower.contains("annual death anniversary") || lower.contains("varshika") -> "ಮಧ್ವ ಪಂಚಾಂಗ ಹಾಗೂ ಅಪರಾಹ್ಣ ವ್ಯಾಪ್ತಿ ನಿಯಮಗಳ ಪ್ರಕಾರ ನಿರ್ಧರಿಸಲಾದ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ ದಿನ."
                    else -> explanation
                }
            }
            AppLanguage.SANSKRIT -> {
                when {
                    lower.contains("13th day") -> "मरणानन्तरं त्रयोदशेऽह्नि आचर्यते (आशौचान्तः)"
                    lower.contains("day 27") -> "मरणानन्तरं 27तमेऽह्नि आचर्यते (ऊनमासिकम्)"
                    lower.contains("day 45") -> "मरणानन्तरं 45तमेऽह्नि आचर्यते (त्रैपाक्षिकम्)"
                    lower.contains("godana") || lower.contains("una-shanmasika") -> "वैतरणीगोदानसहितम् ऊनषाण्मासिकम्"
                    lower.contains("day 350") || lower.contains("unabdika") -> "संवत्सरान्ते 350मे दिने आचर्यते (ऊनाब्दिकम्)"
                    lower.contains("eka aparahna vyapti") -> "एक-अपराह्णव्याप्तिः: तिथिरपराह्णकाले एव व्याप्ता इति निर्णयः।"
                    lower.contains("ubhaya vyapti") -> "उभयव्याप्तिः: अधिकापराह्णव्याप्तियुक्ते दिने निर्णयः कृतः।"
                    lower.contains("mahalaya") -> "भाद्रपदकृष्णपक्षे (महालयपितृपक्षे) मृत्युतिथौ अपराह्णकाले आचर्यते।"
                    lower.contains("annual death anniversary") || lower.contains("varshika") -> "स्मृतिमुक्तावली-धर्मसिन्ध्वनुसारम् अपराह्णव्याप्तियुक्ते दिने वार्षिकश्राद्धनिर्णयः।"
                    else -> explanation
                }
            }
            AppLanguage.TELUGU -> {
                when {
                    lower.contains("13th day") -> "మరణం తర్వాతి 13వ రోజున ఆచరిస్తారు (ఆశౌచ సమాప్తి)"
                    lower.contains("day 27") -> "మరణం తర్వాతి 27వ రోజున ఆచరించే ఊనమాసిక విధి"
                    lower.contains("day 45") -> "మరణం తర్వాతి 45వ రోజున ఆచరించే త్రైపాక్షిక విధి"
                    lower.contains("godana") || lower.contains("una-shanmasika") -> "వైతరణీ గోదాన సహిత ఊన-షాణ్మాసిక విధి"
                    lower.contains("day 350") || lower.contains("unabdika") -> "1 సంవత్సరం పూర్తయ్యే ముందు 350వ రోజున ఆచరించే ఊనాబ్దిక విధి"
                    lower.contains("eka aparahna vyapti") -> "ఏక అపరాహ్ణ వ్యాప్తి: తిథి అపరాహ్ణ కాలంలో మాత్రమే వ్యాపించినందున నిర్ణయించబడింది."
                    lower.contains("ubhaya vyapti") -> "ఉభయ వ్యాప్తి: గరిష్ఠ అపరాహ్ణ వ్యాప్తి ఉన్న రోజును ఎంపిక చేయడమైనది."
                    lower.contains("mahalaya") -> "భాద్రపద కృష్ణ పక్షంలో (మహాలయ పితృ పక్షం) మరణ తిథిన అపరాహ్ణ కాలంలో ఆచరిస్తారు."
                    lower.contains("annual death anniversary") || lower.contains("varshika") -> "మధ్వ పంచాంగం మరియు అపరాహ్ణ వ్యాప్తి నియమాల ప్రకారం నిర్ణయించబడిన వార్షిక శ్రాద్ధ దినం."
                    else -> explanation
                }
            }
            AppLanguage.TAMIL -> {
                when {
                    lower.contains("13th day") -> "மறைவிற்குப் பின் 13வது நாளில் அனுஷ்டிக்கப்படுகிறது (ஆசௌச முடிவு)"
                    lower.contains("day 27") -> "மறைவிற்குப் பின் 27வது நாளில் அனுஷ்டிக்கப்படும் ஊநமாஸிக காரியம்"
                    lower.contains("day 45") -> "மறைவிற்குப் பின் 45வது நாளில் அனுஷ்டிக்கப்படும் த்ரைபாக்ஷிக காரியம்"
                    lower.contains("godana") || lower.contains("una-shanmasika") -> "வைதரணி கோதானத்துடன் கூடிய ஊந-ஷான்மாஸிக காரியம்"
                    lower.contains("day 350") || lower.contains("unabdika") -> "1 வருடம் முடிவதற்குள் 350வது நாளில் அனுஷ்டிக்கப்படும் ஊநாப்திக காரியம்"
                    lower.contains("eka aparahna vyapti") -> "ஏக அபராஹ்ண வியாப்தி: திதி அபராஹ்ண காலத்தில் மட்டுமே வியாபித்துள்ளதால் தீர்மானிக்கப்பட்டது."
                    lower.contains("ubhaya vyapti") -> "உபய வியாப்தி: அதிக அபராஹ்ண வியாப்தி உள்ள நாள் தேர்ந்தெடுக்கப்பட்டது."
                    lower.contains("mahalaya") -> "பாத்ரபத கிருஷ்ண பக்ஷத்தில் (மஹாலய பித்ரு பக்ஷம்) மறைந்த திதியில் அபராஹ்ண காலத்தில் அனுஷ்டிக்கப்படுகிறது."
                    lower.contains("annual death anniversary") || lower.contains("varshika") -> "மத்வ பஞ்சாங்க அபராஹ்ண வியாப்தி விதிகளின்படி நிர்ணயிக்கப்பட்ட வருடாந்திர வார்ஷிக ஸ்ராத்த நாள்."
                    else -> explanation
                }
            }
            AppLanguage.ENGLISH -> explanation
        }
    }

    fun localizeDayOfWeek(dayOfWeek: String, language: AppLanguage): String {
        val upper = dayOfWeek.trim().uppercase()
        return when (language) {
            AppLanguage.ENGLISH -> upper.substring(0, 1) + upper.substring(1).lowercase()
            AppLanguage.KANNADA -> when (upper) {
                "SUNDAY" -> "ಭಾನುವಾರ"
                "MONDAY" -> "ಸೋಮವಾರ"
                "TUESDAY" -> "ಮಂಗಳವಾರ"
                "WEDNESDAY" -> "ಬುಧವಾರ"
                "THURSDAY" -> "ಗುರುವಾರ"
                "FRIDAY" -> "ಶುಕ್ರವಾರ"
                "SATURDAY" -> "ಶನಿವಾರ"
                else -> dayOfWeek
            }
            AppLanguage.SANSKRIT -> when (upper) {
                "SUNDAY" -> "रविवासरः"
                "MONDAY" -> "सोमवासरः"
                "TUESDAY" -> "मङ्गलवासरः"
                "WEDNESDAY" -> "बुधवासरः"
                "THURSDAY" -> "गुरुवासरः"
                "FRIDAY" -> "शुक्रवासरः"
                "SATURDAY" -> "शनिवासरः"
                else -> dayOfWeek
            }
            AppLanguage.TELUGU -> when (upper) {
                "SUNDAY" -> "ఆదివారం"
                "MONDAY" -> "సోమవారం"
                "TUESDAY" -> "మంగళవారం"
                "WEDNESDAY" -> "బుధవారం"
                "THURSDAY" -> "గురువారం"
                "FRIDAY" -> "శుక్రవారం"
                "SATURDAY" -> "శనివారం"
                else -> dayOfWeek
            }
            AppLanguage.TAMIL -> when (upper) {
                "SUNDAY" -> "ஞாயிற்றுக்கிழமை"
                "MONDAY" -> "திங்கட்கிழமை"
                "TUESDAY" -> "செவ்வாய்க்கிழமை"
                "WEDNESDAY" -> "புதன்கிழமை"
                "THURSDAY" -> "வியாழக்கிழமை"
                "FRIDAY" -> "வெள்ளிக்கிழமை"
                "SATURDAY" -> "சனிக்கிழமை"
                else -> dayOfWeek
            }
        }
    }

    private fun localizeDoshaRecord(dosha: DoshaRecord, language: AppLanguage): DoshaRecord {
        return when (language) {
            AppLanguage.KANNADA -> when (dosha.type) {
                DoshaType.DHANISHTA_PANCHAKA -> dosha.copy(
                    title = "ಧನಿಷ್ಠಾ ಪಂಚಕ ಮರಣ (ಧನಿಷ್ಠಾ ಪಂಚಕಂ)",
                    significance = "ಧರ್ಮ ಸಿಂಧು ಹಾಗೂ ಸ್ಮೃತಿ ಮುಕ್ತಾವಳಿಯ ಪ್ರಕಾರ ಧನಿಷ್ಠಾ ಪಂಚಕದಲ್ಲಿ ಸಂಭವಿಸಿದ ಮರಣಕ್ಕೆ ಶಾಸ್ತ್ರೋಕ್ತ ಪಂಚಕ ಶಾಂತಿ ಅತ್ಯಗತ್ಯ.",
                    prescribedRemedy = "ಪಂಚಕ ಶಾಂತಿ ಹೋಮ, ಪುತ್ತಳೀ ವಿಧಾನ (5 ದರ್ಭೆಯ ಪ್ರತಿಕೃತಿಗಳ ದಹನ), ಮತ್ತು ಕಾಂಸ್ಯ ಪಾತ್ರ ದಾನ (ತುಪ್ಪ ತುಂಬಿದ ಕಂಚಿನ ಪಾತ್ರೆ).",
                    scripturalSource = "ಧರ್ಮ ಸಿಂಧು (ಆಶೌಚ ಪ್ರಕರಣ), ನಿರ್ಣಯ ಸಿಂಧು, ಸ್ಮೃತಿ ಮುಕ್ತಾವಳಿ"
                )
                DoshaType.TRI_PUSHKARA_YOGA -> dosha.copy(
                    title = "ತ್ರಿಪುಷ್ಕರ ಯೋಗ ಮೃತ್ಯು (ತ್ರಿಪುಷ್ಕರ ಯೋಗಃ)",
                    significance = "ಮುಹೂರ್ತ ಚಿಂತಾಮಣಿ ಮತ್ತು ಧರ್ಮ ಶಾಸ್ತ್ರಗಳ ಪ್ರಕಾರ ತ್ರಿಪುಷ್ಕರ ಯೋಗದಲ್ಲಿ ಸಂಭವಿಸುವ ಘಟನೆಗಳು ಪುನರಾವರ್ತನೆಯಾಗದಂತೆ ಶಾಂತಿ ಮಾಡಿಸುವುದು ಹಿತಕರ.",
                    prescribedRemedy = "ತ್ರಿಪುಷ್ಕರ ಶಾಂತಿ ಜಪ, ತಿಲ ಹೋಮ ಮತ್ತು ಬ್ರಾಹ್ಮಣರಿಗೆ ಸುವರ್ಣ ಅಥವಾ ಧಾನ್ಯ ದಾನ.",
                    scripturalSource = "ಮುಹೂರ್ತ ಚಿಂತಾಮಣಿ, ಸ್ಮೃತಿ ಮುಕ್ತಾವಳಿ, ಧರ್ಮ ಸಿಂಧು"
                )
                else -> dosha
            }
            AppLanguage.SANSKRIT -> when (dosha.type) {
                DoshaType.DHANISHTA_PANCHAKA -> dosha.copy(
                    title = "धनिष्ठापञ्चकमृत्युः",
                    significance = "धर्मसिन्धौ स्मृतिमुक्तावल्यां च धनिष्ठापञ्चकमृत्युदोषशान्तये पञ्चकशान्तिविधानमुक्तम्।",
                    prescribedRemedy = "पञ्चकशान्तिहोमः, पुत्तलविधानम् (दर्भपिष्टपुत्तलदहनम्), कांस्यपात्रदानं च।",
                    scripturalSource = "धर्मसिन्धुः (आशौचप्रकरणम्), निर्णयसिन्धुः, स्मृतिमुक्तावली"
                )
                DoshaType.TRI_PUSHKARA_YOGA -> dosha.copy(
                    title = "त्रिपुष्करयोगमृत्युः",
                    significance = "मुहूर्तचिन्तामणौ त्रिपुष्करयोगदोषनिवारणार्थं शान्तिः प्रोक्ता।",
                    prescribedRemedy = "त्रिपुष्करशान्तिजपः, तिलहोमः, ब्राह्मणाय गो-धान್ಯदानं च।",
                    scripturalSource = "मुहूर्तचिन्तामणिः, स्मृतिमुक्तावली, धर्मसिन्धुः"
                )
                else -> dosha
            }
            AppLanguage.TELUGU -> when (dosha.type) {
                DoshaType.DHANISHTA_PANCHAKA -> dosha.copy(
                    title = "ధనిష్ఠా పంచక మృత్యువు",
                    significance = "ధర్మ సింధు మరియు స్మృతి ముక్తావళి ప్రకారం ధనిష్ఠా పంచకంలో మరణిస్తే శాంతి విధి ఆచరించడం ముఖ్యం.",
                    prescribedRemedy = "పంచక శాంతి హోమం, పుత్తడి విధానం (5 దర్భ బొమ్మల దహనం), కాంస్య పాత్ర దానం (నెయ్యితో కూడిన కంచు పాత్ర).",
                    scripturalSource = "ధర్మ సింధు, నిర్ణయ సింధు, స్మృతి ముక్తావళి"
                )
                DoshaType.TRI_PUSHKARA_YOGA -> dosha.copy(
                    title = "త్రిపుష్కర యోగ మృత్యువు",
                    significance = "ముహూర్త చింతామణి ప్రకారం త్రిపుష్కర యోగ శాంతి చేయడం శ్రేయస్కరం.",
                    prescribedRemedy = "త్రిపుష్కర శాంతి జపం, తిల హోమం, దానాలు.",
                    scripturalSource = "ముహూర్త చింతామణి, స్మృతి ముక్తావళి, ధర్మ సింధు"
                )
                else -> dosha
            }
            AppLanguage.TAMIL -> when (dosha.type) {
                DoshaType.DHANISHTA_PANCHAKA -> dosha.copy(
                    title = "தநிஷ்டா பஞ்சக மரணம்",
                    significance = "தர்ம சிந்து மற்றும் ஸ்மிருதி முக்தாவளியின்படி பஞ்சக சாந்தி செய்வது அவசியம்.",
                    prescribedRemedy = "பஞ்சக சாந்தி ஹோமம், புத்ஸல தானம் மற்றும் வெண்கல பாத்திர தானம்.",
                    scripturalSource = "தர்ம சிந்து, நிர்ணய சிந்து, ஸ்மிருதி முக்தாவளி"
                )
                DoshaType.TRI_PUSHKARA_YOGA -> dosha.copy(
                    title = "த்ரிபுஷ்கர யோக மரணம்",
                    significance = "முகூர்த்த சிந்தாமணியின்படி த்ரிபுஷ்கர சாந்தி ஜபம் செய்வது நல்லது.",
                    prescribedRemedy = "த்ரிபுஷ்கர சாந்தி ஜபம், தில ஹோமம், தானங்கள்.",
                    scripturalSource = "முகூர்த்த சிந்தாமணி, ஸ்மிருதி முக்தாவளி, தர்ம சிந்து"
                )
                else -> dosha
            }
            AppLanguage.ENGLISH -> dosha
        }
    }

    fun localizeDoshaResult(result: DoshaEvaluationResult, language: AppLanguage): DoshaEvaluationResult {
        if (language == AppLanguage.ENGLISH || !result.hasDosha) return result
        val localizedDoshas = result.doshas.map { localizeDoshaRecord(it, language) }
        val advice = when (language) {
            AppLanguage.KANNADA -> "ಮರಣ ಕಾಲದಲ್ಲಿ ವಿಶೇಷ ದೋಷ ಕಂಡುಬಂದಿರುವುದರಿಂದ ಶಾಸ್ತ್ರಜ್ಞ ವಿದ್ವಾಂಸರ ಮಾರ್ಗದರ್ಶನದಲ್ಲಿ ನಿಗದಿತ ಪ್ರಾಯಶ್ಚಿತ್ತ ಹಾಗೂ ಶಾಂತಿ ಹೋಮವನ್ನು ನೆರವೇರಿಸುವುದು ಹಿತಕರ."
            AppLanguage.SANSKRIT -> "मृत्युकाले दोषसद्भावात् शास्त्रोक्तप्रायश्चित्त-शान्तिहोमाचरणं कर्तव्यम्।"
            AppLanguage.TELUGU -> "మరణ కాలంలో దోషం ఉన్నందున శాస్త్రోక్త ప్రాయశ్చిత్త, శాంతి హోమాలను ఆచరించడం శ్రేయస్కరం."
            AppLanguage.TAMIL -> "மரண காலத்தில் தோஷம் உள்ளதால் சாஸ்திரோக்த பிராயச்சித்த சாந்தி ஹோமங்களை செய்வது நல்லது."
            AppLanguage.ENGLISH -> result.generalAdvice
        }
        return result.copy(doshas = localizedDoshas, generalAdvice = advice)
    }

    fun localizeEkadashiBadge(dvadashiDateStr: String, language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> "🌿 ಏಕಾದಶಿ ವ್ರತ (ಉಪವಾಸ) ➔ ಪೂರ್ಣ ಅನ್ನಶ್ರಾದ್ಧವು ದ್ವಾದಶಿಯಂದು ($dvadashiDateStr)"
            AppLanguage.SANSKRIT -> "🌿 एकादशीव्रतम् (उपवासः) ➔ सम्पूर्णम् अन्नश्राद्धं द्वादश्याम् ($dvadashiDateStr)"
            AppLanguage.TELUGU -> "🌿 ఏకాదశి వ్రతం (ఉపవాసం) ➔ సంపూర్ణ అన్నశ్రాద్ధం ద్వాదశి నాడు ($dvadashiDateStr)"
            AppLanguage.TAMIL -> "🌿 ஏகாதசி விரதம் (உபவாசம்) ➔ முழு அன்ன சிராத்தம் துவாதசியில் ($dvadashiDateStr)"
            AppLanguage.ENGLISH -> "🌿 Ekadashi Vrata (Fasting) ➔ Full Anna-Shraddha on Dvadashi ($dvadashiDateStr)"
        }
    }

    fun localizeEkadashiShiftNote(language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> "ಏಕಾದಶಿ ದಿನ ಪತ್ತೆಯಾಗಿದೆ; ಆದ್ದರಿಂದ ಆಚರಣೆಯನ್ನು ದ್ವಾದಶಿಗೆ ಸ್ಥಳಾಂತರಿಸಲಾಗಿದೆ."
            AppLanguage.SANSKRIT -> "एकादशीतिथिः प्राप्ता; अतः श्राद्धानुष्ठानं द्वादश्यां क्रियते।"
            AppLanguage.TELUGU -> "ఏకాదశి తిథి గుర్తించబడింది; అందువల్ల శ్రాద్ధం ద్వాదశికి మార్చబడింది."
            AppLanguage.TAMIL -> "ஏகாதசி திதி கண்டறியப்பட்டது; எனவே சிராத்த அனுஷ்டானம் துவாதசிக்கு மாற்றப்பட்டுள்ளது."
            AppLanguage.ENGLISH -> "Ekadashi date detected; hence moving the ritual to Dvadashi."
        }
    }

    fun localizeEkadashiButton(language: AppLanguage): String {
        return when (language) {
            AppLanguage.KANNADA -> "ಏಕಾದಶಿ ಶಾಸ್ತ್ರ ಮಾರ್ಗದರ್ಶನ"
            AppLanguage.SANSKRIT -> "एकादशीशास्त्रनिर्णयः"
            AppLanguage.TELUGU -> "ఏకాదశి శాస్త్ర నిర్ణయం"
            AppLanguage.TAMIL -> "ஏகாதசி சாஸ்திர வழிகாட்டல்"
            AppLanguage.ENGLISH -> "Ekadashi Guidance"
        }
    }
}

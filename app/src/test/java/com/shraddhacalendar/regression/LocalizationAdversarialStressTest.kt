package com.shraddhacalendar.regression

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EducationalContentLocalizer
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import org.junit.Assert.*
import org.junit.Test

/**
 * Adversarial Stress Test Suite designed by Challenger 1.
 * Exhaustively stress-tests PanchangaLocalizer, EducationalContentLocalizer,
 * and EducationalContentRepository across all 16 Masikas + Adhika + Varshikas 1-10 + Mahalaya
 * across all 5 languages, Unicode script ranges, and boundary edge cases.
 */
class LocalizationAdversarialStressTest {

    // Unicode Script Range Regexes
    private val devanagariStrictRegex = Regex("^[\\u0900-\\u097F\\s\\d()~/.:,;—–-]+$")
    private val kannadaStrictRegex = Regex("^[\\u0C80-\\u0CFF\\s\\d()~/.:,;—–-]+$")
    private val teluguStrictRegex = Regex("^[\\u0C00-\\u0C7F\\s\\d()~/.:,;—–-]+$")
    private val tamilStrictRegex = Regex("^[\\u0B80-\\u0BFF\\s\\d()~/.:,;—–-]+$")

    // Script Disallowance checks
    private fun containsDevanagari(s: String) = s.any { it.code in 0x0900..0x097F }
    private fun containsKannada(s: String) = s.any { it.code in 0x0C80..0x0CFF }
    private fun containsTelugu(s: String) = s.any { it.code in 0x0C00..0x0C7F }
    private fun containsTamil(s: String) = s.any { it.code in 0x0B80..0x0BFF }
    private fun containsLatinLetters(s: String) = s.any { it in 'a'..'z' || it in 'A'..'Z' }

    @Test
    fun testAll16MasikasAdhikaVarshikas1to10AndMahalayaAllLanguages() {
        val testData = listOf(
            // Masika 1
            "Masika 1 — Adya Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 1 — Adya Masika (13th Day)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)",
                AppLanguage.SANSKRIT to "मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्)",
                AppLanguage.TELUGU to "మాసికం 1 — ఆద్య మాసికం (13వ రోజు)",
                AppLanguage.TAMIL to "மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)"
            ),
            // Masika 2
            "Masika 2 — Unmasika" to mapOf(
                AppLanguage.ENGLISH to "Masika 2 — Unmasika (27th Day)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (27ನೇ ದಿನ)",
                AppLanguage.SANSKRIT to "मासिकम् 2 — ऊनमासिकम् (27 तमदिनम्)",
                AppLanguage.TELUGU to "మాసికం 2 — ఊనమాసికం (27వ రోజు)",
                AppLanguage.TAMIL to "மாஸிகம் 2 — ஊநமாஸிகம் (27ஆம் நாள்)"
            ),
            // Masika 3
            "Masika 3 — Dvitiya Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 3 — Dvitiya Masika (2nd Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (2ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 3 — द्वितीयमासिकम् (द्वितीयमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 3 — ద్వితీయ మాసికం (2వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 3 — த்விதீய மாஸிகம் (2ஆம் மாத திதி)"
            ),
            // Masika 4
            "Masika 4 — Traipakshika" to mapOf(
                AppLanguage.ENGLISH to "Masika 4 — Traipakshika (45th Day)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (45ನೇ ದಿನ)",
                AppLanguage.SANSKRIT to "मासिकम् 4 — त्रैपाक्षिकम् (45 तमदिनम्)",
                AppLanguage.TELUGU to "మాసికం 4 — త్రైపాక్షికం (45వ రోజు)",
                AppLanguage.TAMIL to "மாஸிகம் 4 — த்ரைபாக்ஷிகம் (45ஆம் நாள்)"
            ),
            // Masika 5
            "Masika 5 — Tritiya Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 5 — Tritiya Masika (3rd Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 5 — ತೃತೀಯ ಮಾಸಿಕ (3ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 5 — तृतीयमासिकम् (तृतीयमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 5 — తృతీయ మాసికం (3వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 5 — திருதீய மாஸிகம் (3ஆம் மாத திதி)"
            ),
            // Masika 6
            "Masika 6 — Chaturtha Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 6 — Chaturtha Masika (4th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 6 — ಚತುರ್ಥ ಮಾಸಿಕ (4ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 6 — चतुर्थमासिकम् (चतुर्थमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 6 — చతుర్థ మాసికం (4వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 6 — சதுர்த்த மாஸிகம் (4ஆம் மாத திதி)"
            ),
            // Masika 7
            "Masika 7 — Panchama Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 7 — Panchama Masika (5th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 7 — ಪಂಚಮ ಮಾಸಿಕ (5ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 7 — पञ्चममासिकम् (पञ्चममासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 7 — పంచమ మాసికం (5వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 7 — பஞ்சம மாஸிகம் (5ஆம் மாத திதி)"
            ),
            // Masika 8
            "Masika 8 — Shanmasika" to mapOf(
                AppLanguage.ENGLISH to "Masika 8 — Shanmasika (6th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 8 — ಷಾಣ್ಮಾಸಿಕ (6ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 8 — षाण्मासिकम् (षष्ठमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 8 — షాణ్మాసికం (6వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 8 — ஷான்மாஸிகம் (6ஆம் மாத திதி)"
            ),
            // Masika 9
            "Masika 9 — Una-Shanmasika (with Godana)" to mapOf(
                AppLanguage.ENGLISH to "Masika 9 — Una-Shanmasika (~170th Day / Godana)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (170ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)",
                AppLanguage.SANSKRIT to "मासिकम् 9 — ऊनषाण्मासिकम् (170 तमदिनम् / गोदानसहितम्)",
                AppLanguage.TELUGU to "మాసికం 9 — ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)",
                AppLanguage.TAMIL to "மாஸிகம் 9 — ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)"
            ),
            // Masika 10
            "Masika 10 — Saptama Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 10 — Saptama Masika (7th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 10 — ಸಪ್ತಮ ಮಾಸಿಕ (7ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 10 — सप्तममासिकम् (सप्तममासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 10 — సప్తమ మాసికం (7వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 10 — ஸப்தம மாஸிகம் (7ஆம் மாத திதி)"
            ),
            // Masika 11
            "Masika 11 — Ashtama Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 11 — Ashtama Masika (8th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 11 — ಅಷ್ಟಮ ಮಾಸಿಕ (8ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 11 — अष्टममासिकम् (अष्टममासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 11 — అష్టమ మాసికం (8వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 11 — அஷ்டம மாஸிகம் (8ஆம் மாத திதி)"
            ),
            // Masika 12
            "Masika 12 — Navama Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 12 — Navama Masika (9th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 12 — ನವಮ ಮಾಸಿಕ (9ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 12 — नवममासिकम् (नवममासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 12 — నవమ మాసికం (9వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 12 — நவம மாஸிகம் (9ஆம் மாத திதி)"
            ),
            // Masika 13
            "Masika 13 — Dashama Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 13 — Dashama Masika (10th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 13 — ದಶಮ ಮಾಸಿಕ (10ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 13 — दशममासिकम् (दशममासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 13 — దశమ మాసికం (10వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 13 — தசம மாஸிகம் (10ஆம் மாத திதி)"
            ),
            // Masika 14
            "Masika 14 — Ekadasha Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 14 — Ekadasha Masika (11th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 14 — ಏಕಾದಶ ಮಾಸಿಕ (11ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 14 — एकादशमासिकम् (एकादशमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 14 — ఏకాదశ మాసికం (11వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 14 — ஏகாதச மாஸிகம் (11ஆம் மாத திதி)"
            ),
            // Masika 15
            "Masika 15 — Dvadasha Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 15 — Dvadasha Masika (12th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 15 — ದ್ವಾದಶ ಮಾಸಿಕ (12ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 15 — द्वादशमासिकम् (द्वादशमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 15 — ద్వాదశ మాసికం (12వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 15 — த்வாதச மாஸிகம் (12ஆம் மாத திதி)"
            ),
            // Masika 16
            "Masika 16 — Unabdika (Una-Varshika)" to mapOf(
                AppLanguage.ENGLISH to "Masika 16 — Unabdika (~340th Day / Una-Varshika)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (340ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)",
                AppLanguage.SANSKRIT to "मासिकम् 16 — ऊनाब्दिकम् (340 तमदिनम् / ऊनवार्षिकम्)",
                AppLanguage.TELUGU to "మాసికం 16 — ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)",
                AppLanguage.TAMIL to "மாஸிகம் 16 — ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)"
            ),
            // Adhika / Trayodasha Masika
            "Masika 17 — Trayodasha Masika" to mapOf(
                AppLanguage.ENGLISH to "Masika 17 — Trayodasha Masika (13th Month Tithi)",
                AppLanguage.KANNADA to "ಮಾಸಿಕ 17 — ತ್ರಯೋದಶ ಮಾಸಿಕ (13ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                AppLanguage.SANSKRIT to "मासिकम् 17 — त्रयोदशमासिकम् (त्रयोदशमासतिथिः)",
                AppLanguage.TELUGU to "మాసికం 17 — త్రయోదశ మాసికం (13వ మాస తిథి)",
                AppLanguage.TAMIL to "மாஸிகம் 17 — த்ரயோதச மாஸிகம் (13ஆம் மாத திதி)"
            ),
            // Varshika 1
            "Yearly Shraddha — Prathama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Prathama Varshika Shraddha (1st Death Anniversary)",
                AppLanguage.KANNADA to "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)",
                AppLanguage.TELUGU to "ప్రథమ వార్షిక శ్రాద్ధం (1వ ఏడు)",
                AppLanguage.TAMIL to "ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)"
            ),
            // Varshika 2
            "Yearly Shraddha — Dvitiya Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Dvitiya Varshika Shraddha (2nd Death Anniversary)",
                AppLanguage.KANNADA to "ದ್ವಿತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (2ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "द्वितीयवार्षिकश्राद्धम् (द्वितीयवर्षीयम्)",
                AppLanguage.TELUGU to "ద్వితీయ వార్షిక శ్రాద్ధం (2వ ఏడు)",
                AppLanguage.TAMIL to "த்விதீய வார்ஷிக ஷ்ராத்தம் (2ஆம் ஆண்டு)"
            ),
            // Varshika 3
            "Yearly Shraddha — Tritiya Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Tritiya Varshika Shraddha (3rd Death Anniversary)",
                AppLanguage.KANNADA to "ತೃತೀಯ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (3ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "तृतीयवार्षिकश्राद्धम् (तृतीयवर्षीयम्)",
                AppLanguage.TELUGU to "తృతీయ వార్షిక శ్రాద్ధం (3వ ఏడు)",
                AppLanguage.TAMIL to "திருதீய வார்ஷிக ஷ்ராத்தம் (3ஆம் ஆண்டு)"
            ),
            // Varshika 4
            "Yearly Shraddha — Chaturtha Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Chaturtha Varshika Shraddha (4th Death Anniversary)",
                AppLanguage.KANNADA to "ಚತುರ್ಥ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (4ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "चतुर्थवार्षिकश्राद्धम् (चतुर्थवर्षीयम्)",
                AppLanguage.TELUGU to "చతుర్థ వార్షిక శ్రాద్ధం (4వ ఏడు)",
                AppLanguage.TAMIL to "சதுர்த்த வார்ஷிக ஷ்ராத்தம் (4ஆம் ஆண்டு)"
            ),
            // Varshika 5
            "Yearly Shraddha — Panchama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Panchama Varshika Shraddha (5th Death Anniversary)",
                AppLanguage.KANNADA to "ಪಂಚಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (5ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "पञ्चमवार्षिकश्राद्धम् (पञ्चमवर्षीयम्)",
                AppLanguage.TELUGU to "పంచమ వార్షిక శ్రాద్ధం (5వ ఏడు)",
                AppLanguage.TAMIL to "பஞ்சம வார்ஷிக ஷ்ராத்தம் (5ஆம் ஆண்டு)"
            ),
            // Varshika 6
            "Yearly Shraddha — Shashtha Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Shashtha Varshika Shraddha (6th Death Anniversary)",
                AppLanguage.KANNADA to "ಷಷ್ಠ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (6ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "षष्ठवार्षिकश्राद्धम् (षष्ठवर्षीयम्)",
                AppLanguage.TELUGU to "షష్ఠ వార్షిక శ్రాద్ధం (6వ ఏడు)",
                AppLanguage.TAMIL to "ஷஷ்ட வார்ஷிக ஷ்ராத்தம் (6ஆம் ஆண்டு)"
            ),
            // Varshika 7
            "Yearly Shraddha — Saptama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Saptama Varshika Shraddha (7th Death Anniversary)",
                AppLanguage.KANNADA to "ಸಪ್ತಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (7ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "सप्तमवार्षिकश्राद्धम् (सप्तमवर्षीयम्)",
                AppLanguage.TELUGU to "సప్తమ వార్షిక శ్రాద్ధం (7వ ఏడు)",
                AppLanguage.TAMIL to "ஸப்தம வார்ஷிக ஷ்ராத்தம் (7ஆம் ஆண்டு)"
            ),
            // Varshika 8
            "Yearly Shraddha — Ashtama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Ashtama Varshika Shraddha (8th Death Anniversary)",
                AppLanguage.KANNADA to "ಅಷ್ಟಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (8ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "अष्टमवार्षिकश्राद्धम् (अष्टमवर्षीयम्)",
                AppLanguage.TELUGU to "అష్టమ వార్షిక శ్రాద్ధం (8వ ఏడు)",
                AppLanguage.TAMIL to "அஷ்டம வார்ஷிக ஷ்ராத்தம் (8ஆம் ஆண்டு)"
            ),
            // Varshika 9
            "Yearly Shraddha — Navama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Navama Varshika Shraddha (9th Death Anniversary)",
                AppLanguage.KANNADA to "ನವಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (9ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "नवमवार्षिकश्राद्धम् (नवमवर्षीयम्)",
                AppLanguage.TELUGU to "నవమ వార్షిక శ్రాద్ధం (9వ ఏడు)",
                AppLanguage.TAMIL to "நவம வார்ஷிக ஷ்ராத்தம் (9ஆம் ஆண்டு)"
            ),
            // Varshika 10
            "Yearly Shraddha — Dashama Varshika Shraddha" to mapOf(
                AppLanguage.ENGLISH to "Dashama Varshika Shraddha (10th Death Anniversary)",
                AppLanguage.KANNADA to "ದಶಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (10ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                AppLanguage.SANSKRIT to "दशमवार्षिकश्राद्धम् (दशमवर्षीयम्)",
                AppLanguage.TELUGU to "దశమ వార్షిక శ్రాద్ధం (10వ ఏడు)",
                AppLanguage.TAMIL to "தசம வார்ஷிக ஷ்ராத்தம் (10ஆம் ஆண்டு)"
            ),
            // Mahalaya Paksha
            "Mahalaya Paksha Shraddha (Pitru Paksha)" to mapOf(
                AppLanguage.ENGLISH to "Mahalaya Paksha Shraddha (Pitru Paksha)",
                AppLanguage.KANNADA to "ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)",
                AppLanguage.SANSKRIT to "महालयपक्षश्राद्धम् (पितृपक्षः)",
                AppLanguage.TELUGU to "మహాలయ పక్ష శ్రాద్ధం (పితృ పక్షం)",
                AppLanguage.TAMIL to "மஹாலய பக்ஷ ஷ்ராத்தம் (பித்ரு பக்ஷம்)"
            )
        )

        testData.forEach { (input, expectations) ->
            expectations.forEach { (lang, expected) ->
                val actual = PanchangaLocalizer.localizeTraditionalName(input, lang)
                assertEquals(
                    "Failure localizing '$input' for language $lang",
                    expected,
                    actual
                )
            }
        }
    }

    @Test
    fun testUnicodeScriptExclusivityStrict() {
        val testNames = listOf(
            "Masika 1 — Adya Masika",
            "Masika 2 — Unmasika",
            "Masika 3 — Dvitiya Masika",
            "Masika 4 — Traipakshika",
            "Masika 5 — Tritiya Masika",
            "Masika 6 — Chaturtha Masika",
            "Masika 7 — Panchama Masika",
            "Masika 8 — Shanmasika",
            "Masika 9 — Una-Shanmasika (with Godana)",
            "Masika 10 — Saptama Masika",
            "Masika 11 — Ashtama Masika",
            "Masika 12 — Navama Masika",
            "Masika 13 — Dashama Masika",
            "Masika 14 — Ekadasha Masika",
            "Masika 15 — Dvadasha Masika",
            "Masika 16 — Unabdika (Una-Varshika)",
            "Masika 17 — Trayodasha Masika",
            "Yearly Shraddha — Prathama Varshika Shraddha",
            "Yearly Shraddha — Dvitiya Varshika Shraddha",
            "Yearly Shraddha — Tritiya Varshika Shraddha",
            "Yearly Shraddha — Chaturtha Varshika Shraddha",
            "Yearly Shraddha — Panchama Varshika Shraddha",
            "Yearly Shraddha — Shashtha Varshika Shraddha",
            "Yearly Shraddha — Saptama Varshika Shraddha",
            "Yearly Shraddha — Ashtama Varshika Shraddha",
            "Yearly Shraddha — Navama Varshika Shraddha",
            "Yearly Shraddha — Dashama Varshika Shraddha",
            "Mahalaya Paksha Shraddha (Pitru Paksha)"
        )

        testNames.forEach { name ->
            val sa = PanchangaLocalizer.localizeTraditionalName(name, AppLanguage.SANSKRIT)
            val kn = PanchangaLocalizer.localizeTraditionalName(name, AppLanguage.KANNADA)
            val te = PanchangaLocalizer.localizeTraditionalName(name, AppLanguage.TELUGU)
            val ta = PanchangaLocalizer.localizeTraditionalName(name, AppLanguage.TAMIL)

            // Sanskrit: Must be strict Devanagari + allowed punctuation
            assertTrue("Sanskrit string '$sa' must match Devanagari regex", devanagariStrictRegex.matches(sa))
            assertFalse("Sanskrit string '$sa' must NOT contain Kannada script", containsKannada(sa))
            assertFalse("Sanskrit string '$sa' must NOT contain Telugu script", containsTelugu(sa))
            assertFalse("Sanskrit string '$sa' must NOT contain Tamil script", containsTamil(sa))
            assertFalse("Sanskrit string '$sa' must NOT contain Latin letters", containsLatinLetters(sa))

            // Kannada: Must be strict Kannada + allowed punctuation
            assertTrue("Kannada string '$kn' must match Kannada regex", kannadaStrictRegex.matches(kn))
            assertFalse("Kannada string '$kn' must NOT contain Devanagari script", containsDevanagari(kn))
            assertFalse("Kannada string '$kn' must NOT contain Telugu script", containsTelugu(kn))
            assertFalse("Kannada string '$kn' must NOT contain Tamil script", containsTamil(kn))
            assertFalse("Kannada string '$kn' must NOT contain Latin letters", containsLatinLetters(kn))

            // Telugu: Must be strict Telugu + allowed punctuation
            assertTrue("Telugu string '$te' must match Telugu regex", teluguStrictRegex.matches(te))
            assertFalse("Telugu string '$te' must NOT contain Devanagari script", containsDevanagari(te))
            assertFalse("Telugu string '$te' must NOT contain Kannada script", containsKannada(te))
            assertFalse("Telugu string '$te' must NOT contain Tamil script", containsTamil(te))
            assertFalse("Telugu string '$te' must NOT contain Latin letters", containsLatinLetters(te))

            // Tamil: Must be strict Tamil + allowed punctuation
            assertTrue("Tamil string '$ta' must match Tamil regex", tamilStrictRegex.matches(ta))
            assertFalse("Tamil string '$ta' must NOT contain Devanagari script", containsDevanagari(ta))
            assertFalse("Tamil string '$ta' must NOT contain Kannada script", containsKannada(ta))
            assertFalse("Tamil string '$ta' must NOT contain Telugu script", containsTelugu(ta))
            assertFalse("Tamil string '$ta' must NOT contain Latin letters", containsLatinLetters(ta))
        }
    }

    @Test
    fun testEducationalContentAll20CeremoniesAnd5Languages() {
        val expectedKeys = listOf(
            "adya_masika", "unmasika", "dvitiya_masika", "traipakshika",
            "tritiya_masika", "chaturtha_masika", "panchama_masika", "shashtha_masika",
            "una_shanmasika", "saptama_masika", "ashtama_masika", "navama_masika",
            "dashama_masika", "ekadasha_masika", "dvadasha_masika", "trayodasha_masika",
            "unabdika", "prathama_varshika", "annual_varshika", "mahalaya_paksha"
        )

        assertEquals("Must have exactly 20 ceremony keys defined", 20, expectedKeys.size)

        expectedKeys.forEach { key ->
            val baseInfo = EducationalContentRepository.getInfo(key)
            assertNotNull("Base info for key '$key' must exist in repository", baseInfo)

            AppLanguage.entries.forEach { lang ->
                val locInfo = EducationalContentLocalizer.getLocalizedInfo(baseInfo!!, lang)

                assertTrue("titleEnglish in $lang for $key must not be blank", locInfo.titleEnglish.isNotBlank())
                assertTrue("titleSanskrit in $lang for $key must not be blank", locInfo.titleSanskrit.isNotBlank())
                assertTrue("dayTiming in $lang for $key must not be blank", locInfo.dayTiming.isNotBlank())
                assertTrue("soulJourneyStation in $lang for $key must not be blank", locInfo.soulJourneyStation.isNotBlank())
                assertTrue("stationDescription in $lang for $key must not be blank", locInfo.stationDescription.isNotBlank())
                assertTrue("spiritualSignificance in $lang for $key must not be blank", locInfo.spiritualSignificance.isNotBlank())
                assertTrue("whyNeeded in $lang for $key must not be blank", locInfo.whyNeeded.isNotBlank())
                assertTrue("scripturalCitation in $lang for $key must not be blank", locInfo.scripturalCitation.isNotBlank())

                // Check script containment for non-English languages
                when (lang) {
                    AppLanguage.KANNADA -> {
                        assertTrue("Kannada title must contain Kannada chars: ${locInfo.titleEnglish}", containsKannada(locInfo.titleEnglish))
                        assertTrue("Kannada timing must contain Kannada chars: ${locInfo.dayTiming}", containsKannada(locInfo.dayTiming))
                        assertTrue("Kannada station must contain Kannada chars: ${locInfo.soulJourneyStation}", containsKannada(locInfo.soulJourneyStation))
                    }
                    AppLanguage.SANSKRIT -> {
                        assertTrue("Sanskrit title must contain Devanagari chars: ${locInfo.titleEnglish}", containsDevanagari(locInfo.titleEnglish))
                        assertTrue("Sanskrit timing must contain Devanagari chars: ${locInfo.dayTiming}", containsDevanagari(locInfo.dayTiming))
                        assertTrue("Sanskrit station must contain Devanagari chars: ${locInfo.soulJourneyStation}", containsDevanagari(locInfo.soulJourneyStation))
                    }
                    AppLanguage.TELUGU -> {
                        assertTrue("Telugu title must contain Telugu chars: ${locInfo.titleEnglish}", containsTelugu(locInfo.titleEnglish))
                        assertTrue("Telugu timing must contain Telugu chars: ${locInfo.dayTiming}", containsTelugu(locInfo.dayTiming))
                        assertTrue("Telugu station must contain Telugu chars: ${locInfo.soulJourneyStation}", containsTelugu(locInfo.soulJourneyStation))
                    }
                    AppLanguage.TAMIL -> {
                        assertTrue("Tamil title must contain Tamil chars: ${locInfo.titleEnglish}", containsTamil(locInfo.titleEnglish))
                        assertTrue("Tamil timing must contain Tamil chars: ${locInfo.dayTiming}", containsTamil(locInfo.dayTiming))
                        assertTrue("Tamil station must contain Tamil chars: ${locInfo.soulJourneyStation}", containsTamil(locInfo.soulJourneyStation))
                    }
                    AppLanguage.ENGLISH -> {
                        assertTrue("English title must contain Latin chars: ${locInfo.titleEnglish}", containsLatinLetters(locInfo.titleEnglish))
                    }
                }
            }
        }
    }

    @Test
    fun testEducationalRepositoryEventResolutionAllLanguagesAndFuzzyMatching() {
        val testLookups = listOf(
            // English formatted
            "Masika 1 — Adya Masika (13th Day)" to "adya_masika",
            "Masika 2 — Unmasika (27th Day)" to "unmasika",
            "Masika 3 — Dvitiya Masika (2nd Month Tithi)" to "dvitiya_masika",
            "Masika 4 — Traipakshika (45th Day)" to "traipakshika",
            "Masika 9 — Una-Shanmasika (~170th Day / Godana)" to "una_shanmasika",
            "Masika 16 — Unabdika (~340th Day / Una-Varshika)" to "unabdika",
            "Prathama Varshika Shraddha (1st Death Anniversary)" to "prathama_varshika",
            "Mahalaya Paksha Shraddha (Pitru Paksha)" to "mahalaya_paksha",

            // Kannada formatted
            "ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)" to "adya_masika",
            "ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (27ನೇ ದಿನ)" to "unmasika",
            "ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (2ನೇ ಮಾಸಿಕ ತಿಥಿ)" to "dvitiya_masika",
            "ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (45ನೇ ದಿನ)" to "traipakshika",
            "ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (170ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)" to "una_shanmasika",
            "ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (340ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)" to "unabdika",
            "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)" to "prathama_varshika",
            "ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)" to "mahalaya_paksha",

            // Sanskrit formatted
            "मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्)" to "adya_masika",
            "मासिकम् 2 — ऊनमासिकम् (27 तमदिनम्)" to "unmasika",
            "मासिकम् 4 — त्रैपाक्षिकम् (45 तमदिनम्)" to "traipakshika",
            "मासिकम् 9 — ऊनषाण्मासिकम् (170 तमदिनम् / गोदानसहितम्)" to "una_shanmasika",
            "मासिकम् 16 — ऊनाब्दिकम् (340 तमदिनम् / ऊनवार्षिकम्)" to "unabdika",
            "प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)" to "prathama_varshika",
            "महालयपक्षश्राद्धम् (पितृपक्षः)" to "mahalaya_paksha",

            // Telugu formatted
            "మాసికం 1 — ఆద్య మాసికం (13వ రోజు)" to "adya_masika",
            "మాసికం 2 — ఊనమాసికం (27వ రోజు)" to "unmasika",
            "మాసికం 9 — ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)" to "una_shanmasika",
            "మాసికం 16 — ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)" to "unabdika",
            "ప్రథమ వార్షిక శ్రాద్ధం (1వ ఏడు)" to "prathama_varshika",
            "మహాలయ పక్ష శ్రాద్ధం (పితృ పక్షం)" to "mahalaya_paksha",

            // Tamil formatted
            "மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)" to "adya_masika",
            "மாஸிகம் 2 — ஊநமாஸிகம் (27ஆம் நாள்)" to "unmasika",
            "மாஸிகம் 9 — ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)" to "una_shanmasika",
            "மாஸிகம் 16 — ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)" to "unabdika",
            "ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)" to "prathama_varshika",
            "மஹாலய பக்ஷ ஷ்ராத்தம் (பித்ரு பக்ஷம்)" to "mahalaya_paksha",

            // Sequence fallback tests
            "Masika 1" to "adya_masika",
            "Masika 2" to "unmasika",
            "Masika 3" to "dvitiya_masika",
            "Masika 4" to "traipakshika",
            "Masika 5" to "tritiya_masika",
            "Masika 6" to "chaturtha_masika",
            "Masika 7" to "panchama_masika",
            "Masika 8" to "shashtha_masika",
            "Masika 9" to "una_shanmasika",
            "Masika 10" to "saptama_masika",
            "Masika 11" to "ashtama_masika",
            "Masika 12" to "navama_masika",
            "Masika 13" to "dashama_masika",
            "Masika 14" to "ekadasha_masika",
            "Masika 15" to "dvadasha_masika",
            "Masika 16" to "unabdika",
            "ಮಾಸಿಕ 9" to "una_shanmasika",
            "मासिकम् 16" to "unabdika",
            "మాసికం 4" to "traipakshika",
            "மாஸிகம் 1" to "adya_masika"
        )

        testLookups.forEach { (query, expectedKey) ->
            val info = EducationalContentRepository.findInfoForEvent(query)
            assertNotNull("findInfoForEvent('$query') must not return null", info)
            assertEquals("findInfoForEvent('$query') should map to key '$expectedKey'", expectedKey, info?.ceremonyKey)
        }
    }

    @Test
    fun testEdgeCasesWeirdPrefixesMissingParamsFallbackBehavior() {
        // 1. Weird prefixes & Spacing variations
        val weirdPrefix1 = "Special Masika 1 — Adya Masika"
        // seqMatch checks Masika\s+(\d+)\s*—\s*(.+)
        val localizedWeird1 = PanchangaLocalizer.localizeTraditionalName(weirdPrefix1, AppLanguage.KANNADA)
        assertTrue("Weird prefix 1 should translate ritual: $localizedWeird1", localizedWeird1.contains("ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)"))

        val weirdPrefix2 = "Annual Shraddha — Prathama Varshika Shraddha"
        val localizedWeird2 = PanchangaLocalizer.localizeTraditionalName(weirdPrefix2, AppLanguage.KANNADA)
        assertEquals("ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)", localizedWeird2)

        val tightSpacing = "Masika 1—Adya Masika"
        val localizedTight = PanchangaLocalizer.localizeTraditionalName(tightSpacing, AppLanguage.ENGLISH)
        assertEquals("Masika 1 — Adya Masika (13th Day)", localizedTight)

        // 2. Fallback on completely unrecognized ceremony names
        val unknown = "Random Unknown Ritual Ceremony 99"
        val locUnknownEn = PanchangaLocalizer.localizeTraditionalName(unknown, AppLanguage.ENGLISH)
        val locUnknownKn = PanchangaLocalizer.localizeTraditionalName(unknown, AppLanguage.KANNADA)
        assertEquals("Unknown ceremony should return original string in EN", unknown, locUnknownEn)
        assertEquals("Unknown ceremony should return original string in KN", unknown, locUnknownKn)

        val unknownEdu = EducationalContentRepository.findInfoForEvent("Random Unknown Ceremony")
        assertEquals("Unrecognized event in repository should default to annual_varshika", "annual_varshika", unknownEdu?.ceremonyKey)

        // 3. Tithi bounds
        val outOfBoundsTithi = TithiInfo(0, "Invalid", Paksha.SHUKLA, 0)
        val tithi0 = PanchangaLocalizer.localizeTithi(outOfBoundsTithi, AppLanguage.KANNADA)
        assertNotNull("Tithi 0 should coerce to valid tithi without throwing", tithi0)
        assertEquals("ಪ್ರಥಮಾ", tithi0)

        val outOfBoundsTithi35 = TithiInfo(35, "Invalid", Paksha.KRISHNA, 35)
        val tithi35 = PanchangaLocalizer.localizeTithi(outOfBoundsTithi35, AppLanguage.KANNADA)
        assertNotNull("Tithi 35 should coerce to valid tithi without throwing", tithi35)
        assertEquals("ಅಮಾವಾಸ್ಯಾ", tithi35)

        // 4. Days remaining bounds
        assertEquals("ಇಂದು", PanchangaLocalizer.localizeDaysRemaining(0L, AppLanguage.KANNADA))
        assertEquals("1000000 ದಿನಗಳು ಉಳಿದಿವೆ", PanchangaLocalizer.localizeDaysRemaining(1000000L, AppLanguage.KANNADA))

        // 5. Year title bounds
        assertEquals("ವರ್ಷ 0 (0 - 0)", PanchangaLocalizer.localizeYearTitle(0, 0, 0, AppLanguage.KANNADA))
        assertEquals("Year 0 (0 - 0)", PanchangaLocalizer.localizeYearTitle(0, 0, 0, AppLanguage.ENGLISH))
        assertEquals("ವರ್ಷ 100 (2126 - 2127)", PanchangaLocalizer.localizeYearTitle(100, 2126, 2127, AppLanguage.KANNADA))

        // 6. Day of week case insensitivity & unknown
        assertEquals("ಸೋಮವಾರ", PanchangaLocalizer.localizeDayOfWeek("monday", AppLanguage.KANNADA))
        assertEquals("ಮಂಗಳವಾರ", PanchangaLocalizer.localizeDayOfWeek("TUESDAY", AppLanguage.KANNADA))
        assertEquals("UNKNOWN_DAY", PanchangaLocalizer.localizeDayOfWeek("UNKNOWN_DAY", AppLanguage.KANNADA))

        // 7. Explanations fallback
        val randomExplanation = "Custom computed astronomical event with no keywords"
        assertEquals(randomExplanation, PanchangaLocalizer.localizeExplanation(randomExplanation, AppLanguage.KANNADA))
    }
}

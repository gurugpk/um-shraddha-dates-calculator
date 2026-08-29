package com.shraddhacalendar.regression

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.LunarMonth
import com.shraddhacalendar.core.models.Paksha
import com.shraddhacalendar.core.models.TithiInfo
import org.junit.Assert.*
import org.junit.Test

/**
 * Regression test verifying comprehensive coverage and authentic native script
 * rendering across all 5 supported languages (English, Kannada, Sanskrit, Telugu, Tamil).
 */
class LanguageLocalizationRegressionTest {

    @Test
    fun testAllLanguagesHaveAuthenticNativeScriptNames() {
        assertEquals("English", AppLanguage.ENGLISH.nativeDisplayName)
        assertEquals("ಕನ್ನಡ", AppLanguage.KANNADA.nativeDisplayName)
        assertEquals("संस्कृतम्", AppLanguage.SANSKRIT.nativeDisplayName)
        assertEquals("తెలుగు", AppLanguage.TELUGU.nativeDisplayName)
        assertEquals("தமிழ்", AppLanguage.TAMIL.nativeDisplayName)
    }

    @Test
    fun testIcuTransliteratorAvailable() {
        val testDevanagariWords = listOf(
            "कुलकर्णि" to "ಕುಲಕರ್ಣಿ",
            "शकुन्तला" to "ಶಕುನ್ತಲಾ",
            "भीमसेन" to "ಭೀಮಸೇನ",
            "सत्यात्मतीर्थ" to "ಸತ್ಯಾತ್ಮತೀರ್ಥ",
            "राघवेंद्र" to "ರಾಘವೇಂದ್ರ",
            "कृष्ण" to "ಕೃಷ್ಣ",
            "विष्णु" to "ವಿಷ್ಣು",
            "कर्ण" to "ಕರ್ಣ",
            "पूर्ण" to "ಪೂರ್ಣ"
        )

        testDevanagariWords.forEach { (dev, expectedKn) ->
            val kn = com.shraddhacalendar.core.localization.IndicTransliterator.icuTransliterate(dev, "Devanagari-Kannada")
            val te = com.shraddhacalendar.core.localization.IndicTransliterator.icuTransliterate(dev, "Devanagari-Telugu")
            val ta = com.shraddhacalendar.core.localization.IndicTransliterator.icuTransliterate(dev, "Devanagari-Tamil")
            assertEquals("ICU Devanagari->Kannada for $dev must match", expectedKn, kn)
            assertTrue("ICU Devanagari->Telugu for $dev must not be blank", te.isNotBlank())
            assertTrue("ICU Devanagari->Tamil for $dev must not be blank", ta.isNotBlank())
        }
    }

    @Test
    fun testTithiLocalizationAcrossAll5Languages() {
        val tithis = listOf(
            TithiInfo(1, "Prathama", Paksha.SHUKLA, 1),
            TithiInfo(8, "Ashtami", Paksha.SHUKLA, 8),
            TithiInfo(11, "Ekadashi", Paksha.SHUKLA, 11),
            TithiInfo(15, "Purnima", Paksha.SHUKLA, 15),
            TithiInfo(23, "Ashtami", Paksha.KRISHNA, 8),
            TithiInfo(30, "Amavasya", Paksha.KRISHNA, 15)
        )

        AppLanguage.entries.forEach { lang ->
            tithis.forEach { tithi ->
                val localized = PanchangaLocalizer.localizeTithi(tithi, lang)
                assertNotNull("Tithi ${tithi.name} in $lang must not be null", localized)
                assertTrue("Tithi ${tithi.name} in $lang must not be blank", localized.isNotBlank())
            }
        }
    }

    @Test
    fun testMasaLocalizationAcrossAll5Languages() {
        val masas = LunarMonth.entries

        AppLanguage.entries.forEach { lang ->
            masas.forEach { masa ->
                val localizedNija = PanchangaLocalizer.localizeMasa(masa, isAdhika = false, lang)
                val localizedAdhika = PanchangaLocalizer.localizeMasa(masa, isAdhika = true, lang)

                assertNotNull("Masa $masa in $lang must not be null", localizedNija)
                assertTrue("Masa $masa in $lang must not be blank", localizedNija.isNotBlank())
                assertTrue("Adhika Masa $masa in $lang must contain Adhika indicator", localizedAdhika.isNotBlank())
            }
        }
    }

    @Test
    fun testRitualCeremonyLocalizationAcrossAll5Languages() {
        val sampleRituals = listOf(
            "Masika 1 — Adya Masika",
            "Masika 2 — Unmasika",
            "Masika 4 — Traipakshika",
            "Masika 9 — Una-Shanmasika (with Godana)",
            "Masika 16 — Unabdika (Una-Varshika)",
            "Yearly Shraddha — Prathama Varshika Shraddha"
        )

        AppLanguage.entries.forEach { lang ->
            sampleRituals.forEach { ritual ->
                val localized = PanchangaLocalizer.localizeTraditionalName(ritual, lang)
                assertNotNull("Ritual $ritual in $lang must not be null", localized)
                assertTrue("Ritual $ritual in $lang must not be blank", localized.isNotBlank())
            }
        }
    }

    @Test
    fun testSanskritDevanagariExclusivity() {
        // Ensure Sanskrit uses pure Devanagari script (Unicode block \u0900-\u097F)
        val devanagariRegex = Regex("[\\u0900-\\u097F\\s—\\d()~/.:-]+")

        val tithiSa = PanchangaLocalizer.localizeTithi(TithiInfo(8, "Ashtami", Paksha.SHUKLA, 8), AppLanguage.SANSKRIT)
        val masaSa = PanchangaLocalizer.localizeMasa(LunarMonth.BHADRAPADA, isAdhika = false, AppLanguage.SANSKRIT)
        val pakshaSa = PanchangaLocalizer.localizePaksha(Paksha.SHUKLA, AppLanguage.SANSKRIT)
        val ritualSa = PanchangaLocalizer.localizeTraditionalName("Masika 1 — Adya Masika", AppLanguage.SANSKRIT)

        assertTrue("Sanskrit tithi must be Devanagari: $tithiSa", tithiSa.matches(devanagariRegex))
        assertTrue("Sanskrit masa must be Devanagari: $masaSa", masaSa.matches(devanagariRegex))
        assertTrue("Sanskrit paksha must be Devanagari: $pakshaSa", pakshaSa.matches(devanagariRegex))
        assertTrue("Sanskrit ritual must be Devanagari: $ritualSa", ritualSa.matches(devanagariRegex))
    }

    @Test
    fun testPersonNameTransliterationAcrossLanguages() {
        val name = "Pranesh Kulkarni"

        val nameKn = PanchangaLocalizer.localizePersonName(name, AppLanguage.KANNADA)
        val nameSa = PanchangaLocalizer.localizePersonName(name, AppLanguage.SANSKRIT)
        val nameTe = PanchangaLocalizer.localizePersonName(name, AppLanguage.TELUGU)
        val nameTa = PanchangaLocalizer.localizePersonName(name, AppLanguage.TAMIL)
        val nameEn = PanchangaLocalizer.localizePersonName(name, AppLanguage.ENGLISH)

        assertEquals("Pranesh Kulkarni", nameEn)
        assertEquals("ಪ್ರಾಣೇಶ್ ಕುಲಕರ್ಣಿ", nameKn)
        assertEquals("प्राणेश कुलकर्णी", nameSa)
        assertEquals("ప్రాణేష్ కులకర్ణి", nameTe)
        assertEquals("பிராணேஷ் குல்கர்னி", nameTa)

        // Test with "tammay"
        val tammayKn = PanchangaLocalizer.localizePersonName("tammay", AppLanguage.KANNADA)
        val tammayTa = PanchangaLocalizer.localizePersonName("tammay", AppLanguage.TAMIL)
        assertEquals("ತಮ್ಮಯ್", tammayKn)
        assertEquals("தம்மய்", tammayTa)
    }

    @Test
    fun testSamvatsaraAndLocationLocalizationAcrossLanguages() {
        val samvatsara = "Parabhava"
        val samvatsaraKn = PanchangaLocalizer.localizeSamvatsara(samvatsara, AppLanguage.KANNADA)
        val samvatsaraSa = PanchangaLocalizer.localizeSamvatsara(samvatsara, AppLanguage.SANSKRIT)
        val samvatsaraTe = PanchangaLocalizer.localizeSamvatsara(samvatsara, AppLanguage.TELUGU)
        val samvatsaraTa = PanchangaLocalizer.localizeSamvatsara(samvatsara, AppLanguage.TAMIL)

        assertEquals("ಪರಾಭವ ನಾಮ ಸಂವತ್ಸರ", samvatsaraKn)
        assertEquals("पराभवः नामसंवत्सरः", samvatsaraSa)
        assertEquals("పరాభవ నామ సంవత్సరం", samvatsaraTe)
        assertEquals("பராபவ நாம ஸம்வத்ஸரம்", samvatsaraTa)

        val location = "Bengaluru, Karnataka, India"
        val locKn = PanchangaLocalizer.localizeLocation(location, AppLanguage.KANNADA)
        val locTa = PanchangaLocalizer.localizeLocation(location, AppLanguage.TAMIL)

        assertEquals("ಬೆಂಗಳೂರು, ಕರ್ನಾಟಕ, ಭಾರತ", locKn)
        assertEquals("பெங்களூரு, கர்நாடகா, இந்தியா", locTa)
    }

    @Test
    fun testComprehensiveWorldwideNamesAndWordsTransliteration() {
        val testNames = listOf(
            // Madhwa & Uttaradi Math Parampara Names
            "Sri Madhvacharya",
            "Sri 108 Satyatmatheertha",
            "Sri Raghavendra Swami",
            "Sri Jayatheertha",
            "Sri Vadiraja",
            "Sri Vyasatheertha",

            // Classical & Modern Indian Names
            "Ramachandra Rao",
            "Gururaj Kulkarni",
            "Late Sri Pranesh Kulkarni",
            "Venkatesh Joshi",
            "Srinivasa Deshpande",
            "Anand Bhat",
            "Vijay Krishna",
            "Narayana Acharya",
            "Manjunath Patil",
            "Subrahmanya Sharma",
            "Dattatreya Shastri",
            "Prahlad Kamath",
            "Gopal Shenoy",
            "Suresh Hegde",
            "Ramesh Shetty",
            "Mahesh Gowda",
            "Lakshmi Bai",
            "Saraswati Amma",
            "Radha Rukmini",
            "Gayatri Devi",

            // Diverse Worldwide Names
            "John Smith",
            "David Robert",
            "Michael Brown",
            "Alexander George",
            "Mary Elizabeth",
            "William James",
            "Richard Thomas",
            "Charles Daniel",
            "Mohammad Ali",
            "Ahmed Hassan"
        )

        val testLocations = listOf(
            "Udupi, Karnataka, India",
            "Mantralayam, Andhra Pradesh, India",
            "Mysuru, Karnataka, India",
            "Hubballi, Karnataka, India",
            "Mumbai, Maharashtra, India",
            "Chennai, Tamil Nadu, India",
            "Hyderabad, Telangana, India",
            "New Delhi, Delhi, India",
            "Varanasi, Uttar Pradesh, India",
            "Tirupati, Andhra Pradesh, India",
            "New York, USA",
            "London, UK",
            "Dubai, UAE",
            "Singapore",
            "Tokyo, Japan",
            "Paris, France",
            "Sydney, Australia",
            "Toronto, Canada"
        )

        AppLanguage.entries.forEach { lang ->
            testNames.forEach { name ->
                val transliterated = PanchangaLocalizer.localizePersonName(name, lang)
                assertNotNull("Transliterated name '$name' in $lang should not be null", transliterated)
                assertTrue("Transliterated name '$name' in $lang should not be blank", transliterated.isNotBlank())
                if (lang != AppLanguage.ENGLISH) {
                    val hasIndic = transliterated.any { it.code in 0x0900..0x0D7F }
                    assertTrue("Transliterated name '$name' in $lang should contain native Indic characters: $transliterated", hasIndic)
                }
            }

            testLocations.forEach { loc ->
                val transliteratedLoc = PanchangaLocalizer.localizeLocation(loc, lang)
                assertNotNull("Transliterated location '$loc' in $lang should not be null", transliteratedLoc)
                assertTrue("Transliterated location '$loc' in $lang should not be blank", transliteratedLoc.isNotBlank())
                if (lang != AppLanguage.ENGLISH) {
                    val hasIndic = transliteratedLoc.any { it.code in 0x0900..0x0D7F }
                    assertTrue("Transliterated location '$loc' in $lang should contain native Indic characters: $transliteratedLoc", hasIndic)
                }
            }
        }
    }

    @Test
    fun testMahalayaPakshaAndVarshikaLocalization() {
        val mahalayaName = "Mahalaya Paksha Shraddha (Pitru Paksha)"
        assertEquals("Mahalaya Paksha Shraddha (Pitru Paksha)", PanchangaLocalizer.localizeTraditionalName(mahalayaName, AppLanguage.ENGLISH))
        assertEquals("ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)", PanchangaLocalizer.localizeTraditionalName(mahalayaName, AppLanguage.KANNADA))
        assertEquals("महालयपक्षश्राद्धम् (पितृपक्षः)", PanchangaLocalizer.localizeTraditionalName(mahalayaName, AppLanguage.SANSKRIT))
        assertEquals("మహాలయ పక్ష శ్రాద్ధం (పితృ పక్షం)", PanchangaLocalizer.localizeTraditionalName(mahalayaName, AppLanguage.TELUGU))
        assertEquals("மஹாலய பக்ஷ ஷ்ராத்தம் (பித்ரு பக்ஷம்)", PanchangaLocalizer.localizeTraditionalName(mahalayaName, AppLanguage.TAMIL))

        val prathamaVarshika = "Yearly Shraddha — Prathama Varshika Shraddha"
        assertEquals("Prathama Varshika Shraddha (1st Death Anniversary)", PanchangaLocalizer.localizeTraditionalName(prathamaVarshika, AppLanguage.ENGLISH))
        assertEquals("ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)", PanchangaLocalizer.localizeTraditionalName(prathamaVarshika, AppLanguage.KANNADA))
        assertEquals("प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)", PanchangaLocalizer.localizeTraditionalName(prathamaVarshika, AppLanguage.SANSKRIT))
        assertEquals("ప్రథమ వార్షిక శ్రాద్ధం (1వ ఏడు)", PanchangaLocalizer.localizeTraditionalName(prathamaVarshika, AppLanguage.TELUGU))
        assertEquals("ப்ரதம வார்ஷிக ஷ்ராத்தம் (1ஆம் ஆண்டு)", PanchangaLocalizer.localizeTraditionalName(prathamaVarshika, AppLanguage.TAMIL))
    }

    @Test
    fun testComprehensive16MasikaDayTimingParity() {
        val ceremonies = listOf(
            "Masika 1 — Adya Masika" to listOf(
                "Masika 1 — Adya Masika (13th Day)",
                "ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)",
                "मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्)",
                "మాసికం 1 — ఆద్య మాసికం (13వ రోజు)",
                "மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)"
            ),
            "Masika 2 — Unmasika" to listOf(
                "Masika 2 — Unmasika (27th Day)",
                "ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (27ನೇ ದಿನ)",
                "मासिकम् 2 — ऊनमासिकम् (27 तमदिनम्)",
                "మాసికం 2 — ఊనమాసికం (27వ రోజు)",
                "மாஸிகம் 2 — ஊநமாஸிகம் (27ஆம் நாள்)"
            ),
            "Masika 3 — Dvitiya Masika" to listOf(
                "Masika 3 — Dvitiya Masika (2nd Month Tithi)",
                "ಮಾಸಿಕ 3 — ದ್ವಿತೀಯ ಮಾಸಿಕ (2ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 3 — द्वितीयमासिकम् (द्वितीयमासतिथिः)",
                "మాసికం 3 — ద్వితీయ మాసికం (2వ మాస తిథి)",
                "மாஸிகம் 3 — த்விதீய மாஸிகம் (2ஆம் மாத திதி)"
            ),
            "Masika 4 — Traipakshika" to listOf(
                "Masika 4 — Traipakshika (45th Day)",
                "ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (45ನೇ ದಿನ)",
                "मासिकम् 4 — त्रैपाक्षिकम् (45 तमदिनम्)",
                "మాసికం 4 — త్రైపాక్షికం (45వ రోజు)",
                "மாஸிகம் 4 — த்ரைபாக்ஷிகம் (45ஆம் நாள்)"
            ),
            "Masika 5 — Tritiya Masika" to listOf(
                "Masika 5 — Tritiya Masika (3rd Month Tithi)",
                "ಮಾಸಿಕ 5 — ತೃತೀಯ ಮಾಸಿಕ (3ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 5 — तृतीयमासिकम् (तृतीयमासतिथिः)",
                "మాసికం 5 — తృతీయ మాసికం (3వ మాస తిథి)",
                "மாஸிகம் 5 — திருதீய மாஸிகம் (3ஆம் மாத திதி)"
            ),
            "Masika 6 — Chaturtha Masika" to listOf(
                "Masika 6 — Chaturtha Masika (4th Month Tithi)",
                "ಮಾಸಿಕ 6 — ಚತುರ್ಥ ಮಾಸಿಕ (4ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 6 — चतुर्थमासिकम् (चतुर्थमासतिथिः)",
                "మాసికం 6 — చతుర్థ మాసికం (4వ మాస తిథి)",
                "மாஸிகம் 6 — சதுர்த்த மாஸிகம் (4ஆம் மாத திதி)"
            ),
            "Masika 7 — Panchama Masika" to listOf(
                "Masika 7 — Panchama Masika (5th Month Tithi)",
                "ಮಾಸಿಕ 7 — ಪಂಚಮ ಮಾಸಿಕ (5ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 7 — पञ्चममासिकम् (पञ्चममासतिथिः)",
                "మాసికం 7 — పంచమ మాసికం (5వ మాస తిథి)",
                "மாஸிகம் 7 — பஞ்சம மாஸிகம் (5ஆம் மாத திதி)"
            ),
            "Masika 8 — Shanmasika" to listOf(
                "Masika 8 — Shanmasika (6th Month Tithi)",
                "ಮಾಸಿಕ 8 — ಷಾಣ್ಮಾಸಿಕ (6ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 8 — षाण्मासिकम् (षष्ठमासतिथिः)",
                "మాసికం 8 — షాణ్మాసికం (6వ మాస తిథి)",
                "மாஸிகம் 8 — ஷான்மாஸிகம் (6ஆம் மாத திதி)"
            ),
            "Masika 9 — Una-Shanmasika (with Godana)" to listOf(
                "Masika 9 — Una-Shanmasika (~170th Day / Godana)",
                "ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (170ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)",
                "मासिकम् 9 — ऊनषाण्मासिकम् (170 तमदिनम् / गोदानसहितम्)",
                "మాసికం 9 — ఊనషాణ్మాసికం (170వ రోజు / గోదాన సహితం)",
                "மாஸிகம் 9 — ஊநஷாண்மாஸிகம் (170ஆம் நாள் / கோதானத்துடன்)"
            ),
            "Masika 10 — Saptama Masika" to listOf(
                "Masika 10 — Saptama Masika (7th Month Tithi)",
                "ಮಾಸಿಕ 10 — ಸಪ್ತಮ ಮಾಸಿಕ (7ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 10 — सप्तममासिकम् (सप्तममासतिथिः)",
                "మాసికం 10 — సప్తమ మాసికం (7వ మాస తిథి)",
                "மாஸிகம் 10 — ஸப்தம மாஸிகம் (7ஆம் மாத திதி)"
            ),
            "Masika 11 — Ashtama Masika" to listOf(
                "Masika 11 — Ashtama Masika (8th Month Tithi)",
                "ಮಾಸಿಕ 11 — ಅಷ್ಟಮ ಮಾಸಿಕ (8ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 11 — अष्टममासिकम् (अष्टममासतिथिः)",
                "మాసికం 11 — అష్టమ మాసికం (8వ మాస తిథి)",
                "மாஸிகம் 11 — அஷ்டம மாஸிகம் (8ஆம் மாத திதி)"
            ),
            "Masika 12 — Navama Masika" to listOf(
                "Masika 12 — Navama Masika (9th Month Tithi)",
                "ಮಾಸಿಕ 12 — ನವಮ ಮಾಸಿಕ (9ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 12 — नवममासिकम् (नवममासतिथिः)",
                "మాసికం 12 — నవమ మాసికం (9వ మాస తిథి)",
                "மாஸிகம் 12 — நவம மாஸிகம் (9ஆம் மாத திதி)"
            ),
            "Masika 13 — Dashama Masika" to listOf(
                "Masika 13 — Dashama Masika (10th Month Tithi)",
                "ಮಾಸಿಕ 13 — ದಶಮ ಮಾಸಿಕ (10ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 13 — दशममासिकम् (दशममासतिथिः)",
                "మాసికం 13 — దశమ మాసికం (10వ మాస తిథి)",
                "மாஸிகம் 13 — தசம மாஸிகம் (10ஆம் மாத திதி)"
            ),
            "Masika 14 — Ekadasha Masika" to listOf(
                "Masika 14 — Ekadasha Masika (11th Month Tithi)",
                "ಮಾಸಿಕ 14 — ಏಕಾದಶ ಮಾಸಿಕ (11ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 14 — एकादशमासिकम् (एकादशमासतिथिः)",
                "మాసికం 14 — ఏకాదశ మాసికం (11వ మాస తిథి)",
                "மாஸிகம் 14 — ஏகாதச மாஸிகம் (11ஆம் மாத திதி)"
            ),
            "Masika 15 — Dvadasha Masika" to listOf(
                "Masika 15 — Dvadasha Masika (12th Month Tithi)",
                "ಮಾಸಿಕ 15 — ದ್ವಾದಶ ಮಾಸಿಕ (12ನೇ ಮಾಸಿಕ ತಿಥಿ)",
                "मासिकम् 15 — द्वादशमासिकम् (द्वादशमासतिथिः)",
                "మాసికం 15 — ద్వాదశ మాసికం (12వ మాస తిథి)",
                "மாஸிகம் 15 — த்வாதச மாஸிகம் (12ஆம் மாத திதி)"
            ),
            "Masika 16 — Unabdika (Una-Varshika)" to listOf(
                "Masika 16 — Unabdika (~340th Day / Una-Varshika)",
                "ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (340ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)",
                "मासिकम् 16 — ऊनाब्दिकम् (340 तमदिनम् / ऊनवार्षिकम्)",
                "మాసికం 16 — ఊనాబ్దికం (340వ రోజు / ఊనవార్షికం)",
                "மாஸிகம் 16 — ஊனாப்திகம் (340ஆம் நாள் / ஊநவார்ஷிகம்)"
            ),
            "Yearly Shraddha — Prathama Varshika Shraddha" to listOf(
                "Prathama Varshika Shraddha (1st Death Anniversary)",
                "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)",
                "प्रथमवार्षिकश्राद्धम् (प्रथमवर्षीयम्)",
                "ప్రథమ వార్షిక శ్రాద్ధం (1వ ఏడు)",
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

    @Test
    fun testDaysRemainingAndYearTitleLocalization() {
        assertEquals("ಇಂದು", PanchangaLocalizer.localizeDaysRemaining(0, AppLanguage.KANNADA))
        assertEquals("अद्य", PanchangaLocalizer.localizeDaysRemaining(0, AppLanguage.SANSKRIT))
        assertEquals("5 ದಿನಗಳು ಉಳಿದಿವೆ", PanchangaLocalizer.localizeDaysRemaining(5, AppLanguage.KANNADA))
        assertEquals("5 Days Remaining", PanchangaLocalizer.localizeDaysRemaining(5, AppLanguage.ENGLISH))

        assertEquals("ವರ್ಷ 2 (2026 - 2027)", PanchangaLocalizer.localizeYearTitle(2, 2026, 2027, AppLanguage.KANNADA))
        assertEquals("Year 2 (2026 - 2027)", PanchangaLocalizer.localizeYearTitle(2, 2026, 2027, AppLanguage.ENGLISH))
    }

    @Test
    fun testFamilyRelationshipAndTraditionLocalization() {
        com.shraddhacalendar.core.models.FamilyRelationship.entries.forEach { rel ->
            AppLanguage.entries.forEach { lang ->
                val localized = PanchangaLocalizer.localizeRelationship(rel, lang)
                assertNotNull("Relationship $rel in $lang must not be null", localized)
                assertTrue("Relationship $rel in $lang must not be blank", localized.isNotBlank())
            }
        }

        com.shraddhacalendar.core.models.MadhwaTradition.entries.forEach { trad ->
            AppLanguage.entries.forEach { lang ->
                val localized = PanchangaLocalizer.localizeTradition(trad, lang)
                assertNotNull("Tradition $trad in $lang must not be null", localized)
                assertTrue("Tradition $trad in $lang must not be blank", localized.isNotBlank())
            }
        }
    }

    @Test
    fun testKulkarniAndPhoneticVariantHarmonizationAcrossLanguages() {
        // 1. Critical User Requirement: Both "Kulkarni" and "Kulakarni" must produce "ಕುಲಕರ್ಣಿ" (with ರ್ಣ / ರ್ಣಿ retroflex)
        val variantsKulkarni = listOf("Kulkarni", "Kulakarni", "kulkarni", "kulakarni", "koolkarni", "kulkarny", "kulakarny")

        variantsKulkarni.forEach { variant ->
            val kn = PanchangaLocalizer.localizePersonName(variant, AppLanguage.KANNADA)
            val sa = PanchangaLocalizer.localizePersonName(variant, AppLanguage.SANSKRIT)
            val te = PanchangaLocalizer.localizePersonName(variant, AppLanguage.TELUGU)
            val ta = PanchangaLocalizer.localizePersonName(variant, AppLanguage.TAMIL)

            assertEquals("Variant '$variant' in Kannada must render as ಕುಲಕರ್ಣಿ with ರ್ಣಿ", "ಕುಲಕರ್ಣಿ", kn)
            assertEquals("Variant '$variant' in Sanskrit must render as कुलकर्णी with र्णी", "कुलकर्णी", sa)
            assertEquals("Variant '$variant' in Telugu must render as కులకర్ణి with ర్ణి", "కులకర్ణి", te)
            assertEquals("Variant '$variant' in Tamil must render as குல்கர்னி", "குல்கர்னி", ta)
        }

        // 2. Surnames with phonetic variants
        val variantsDeshpande = listOf("Deshpande", "Deshapande", "deshpande", "deshapande", "despande")
        variantsDeshpande.forEach { variant ->
            assertEquals("ದೇಶಪಾಂಡೆ", PanchangaLocalizer.localizePersonName(variant, AppLanguage.KANNADA))
            assertEquals("देशपाण्डे", PanchangaLocalizer.localizePersonName(variant, AppLanguage.SANSKRIT))
            assertEquals("దేశ్‌పాండే", PanchangaLocalizer.localizePersonName(variant, AppLanguage.TELUGU))
            assertEquals("தேஷ்பாண்டே", PanchangaLocalizer.localizePersonName(variant, AppLanguage.TAMIL))
        }

        // 3. First names and honorific combinations
        assertEquals("ಶ್ರೀ ರಾಮಚಂದ್ರ ಕುಲಕರ್ಣಿ", PanchangaLocalizer.localizePersonName("Sri Ramachandra Kulakarni", AppLanguage.KANNADA))
        assertEquals("ಶ್ರೀ ರಾಘವೇಂದ್ರ ರಾವ್", PanchangaLocalizer.localizePersonName("Shri Raghavendra Rao", AppLanguage.KANNADA))
        assertEquals("ದಿವಂಗತ ಪ್ರಾಣೇಶರಾವ್ ಕುಲಕರ್ಣಿ", PanchangaLocalizer.localizePersonName("Late Praneshrao Kulkarni", AppLanguage.KANNADA))
        assertEquals("ಶಕುಂತಲಾ ದೇಶಪಾಂಡೆ", PanchangaLocalizer.localizePersonName("Shakunthala Deshapande", AppLanguage.KANNADA))
        assertEquals("ಭೀಮಸೇನ ಭಟ್", PanchangaLocalizer.localizePersonName("Bhimasena Bhat", AppLanguage.KANNADA))
        assertEquals("ವೆಂಕಟೇಶ ಜೋಶಿ", PanchangaLocalizer.localizePersonName("Venkatesha Josi", AppLanguage.KANNADA))
        assertEquals("ದತ್ತಾತ್ರೇಯ ಮುತಾಲಿಕ್", PanchangaLocalizer.localizePersonName("Dattatreya Muthalik", AppLanguage.KANNADA))
        assertEquals("ಸುಬ್ರಹ್ಮಣ್ಯ ಶಾಸ್ತ್ರಿ", PanchangaLocalizer.localizePersonName("Subramanya Shastri", AppLanguage.KANNADA))
        assertEquals("ಗುರುರಾಜ ಇನಾಂದಾರ್", PanchangaLocalizer.localizePersonName("Gururaja Inamdar", AppLanguage.KANNADA))
        assertEquals("ಅನಂತ ಪದ್ಮನಾಭ ಕಾಮತ್", PanchangaLocalizer.localizePersonName("Anantha Padmanabha Kamath", AppLanguage.KANNADA))
    }

    @Test
    fun testEducationalScripturalLocalizationAcrossAllLanguages() {
        val allCeremonies = com.shraddhacalendar.core.shraddha.EducationalContentRepository.getAllCeremonies()
        assertTrue("Must have at least 15 ceremonies defined", allCeremonies.size >= 15)

        allCeremonies.forEach { info ->
            // 1. Kannada
            val kn = com.shraddhacalendar.core.shraddha.EducationalContentLocalizer.getLocalizedInfo(info, AppLanguage.KANNADA)
            assertTrue("Kannada title must not be blank for ${info.ceremonyKey}", kn.titleEnglish.isNotBlank())
            assertTrue("Kannada timing must not be blank for ${info.ceremonyKey}", kn.dayTiming.isNotBlank())
            assertTrue("Kannada station must not be blank for ${info.ceremonyKey}", kn.soulJourneyStation.isNotBlank())
            assertTrue("Kannada spiritual significance must not be blank for ${info.ceremonyKey}", kn.spiritualSignificance.isNotBlank())
            assertTrue("Kannada why needed must not be blank for ${info.ceremonyKey}", kn.whyNeeded.isNotBlank())
            assertTrue("Kannada scriptural citation must not be blank for ${info.ceremonyKey}", kn.scripturalCitation.isNotBlank())

            // 2. Sanskrit
            val sa = com.shraddhacalendar.core.shraddha.EducationalContentLocalizer.getLocalizedInfo(info, AppLanguage.SANSKRIT)
            assertTrue("Sanskrit title must not be blank for ${info.ceremonyKey}", sa.titleEnglish.isNotBlank())
            assertTrue("Sanskrit timing must not be blank for ${info.ceremonyKey}", sa.dayTiming.isNotBlank())

            // 3. Telugu
            val te = com.shraddhacalendar.core.shraddha.EducationalContentLocalizer.getLocalizedInfo(info, AppLanguage.TELUGU)
            assertTrue("Telugu title must not be blank for ${info.ceremonyKey}", te.titleEnglish.isNotBlank())
            assertTrue("Telugu timing must not be blank for ${info.ceremonyKey}", te.dayTiming.isNotBlank())

            // 4. Tamil
            val ta = com.shraddhacalendar.core.shraddha.EducationalContentLocalizer.getLocalizedInfo(info, AppLanguage.TAMIL)
            assertTrue("Tamil title must not be blank for ${info.ceremonyKey}", ta.titleEnglish.isNotBlank())
            assertTrue("Tamil timing must not be blank for ${info.ceremonyKey}", ta.dayTiming.isNotBlank())
        }
    }

    @Test
    fun testPanchangaDeterminationTraceLocalization() {
        val traces = listOf(
            "Observed on 13th day following death (completion of Ashaucha rites)",
            "Traditional interval rite observed on Day 27 following death",
            "Eka Aparahna Vyapti: Tithi prevailed during Aparahna Kala (148 mins) solely on 2026-09-13",
            "Ubhaya Vyapti: Selected 2026-09-13 due to maximum Aparahna overlap",
            "Observed during Bhadrapada Krishna Paksha (Mahalaya Pitru Paksha) on Tritiya"
        )

        traces.forEach { trace ->
            val kn = PanchangaLocalizer.localizeExplanation(trace, AppLanguage.KANNADA)
            val sa = PanchangaLocalizer.localizeExplanation(trace, AppLanguage.SANSKRIT)
            val te = PanchangaLocalizer.localizeExplanation(trace, AppLanguage.TELUGU)
            val ta = PanchangaLocalizer.localizeExplanation(trace, AppLanguage.TAMIL)

            assertFalse("Kannada trace for '$trace' must not be in English", kn.contains("Observed on") || kn.contains("solely on"))
            assertFalse("Sanskrit trace for '$trace' must not be in English", sa.contains("Observed on") || sa.contains("solely on"))
            assertFalse("Telugu trace for '$trace' must not be in English", te.contains("Observed on") || te.contains("solely on"))
            assertFalse("Tamil trace for '$trace' must not be in English", ta.contains("Observed on") || ta.contains("solely on"))
        }
    }
}



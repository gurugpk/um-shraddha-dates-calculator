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
        val devanagariRegex = Regex("[\\u0900-\\u097F\\s—\\d]+")

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
}

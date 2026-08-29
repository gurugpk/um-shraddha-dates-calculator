package com.shraddhacalendar.regression

import com.shraddhacalendar.core.calendar.makeEntityKey
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.shraddha.AparahnaVyaptiEngine
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.MasikaShraddhaCalculator
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.core.tradition.TraditionEngineFactory
import com.shraddhacalendar.data.location.CityDatabase
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Adversarial Stress Test Suite created by Challenger 2.
 * Rigorously stress-tests:
 * 1. Leap year (Feb 29) and leap month (Adhika Masa) boundaries.
 * 2. Multi-tradition engine parity and tradition overrides (UM, SRS, Udupi).
 * 3. 16/17 Shodasha Masika day timings, intervals, and sequence monotonicity.
 * 4. Year 1 Preta Avastha rules (Null Mahalaya Paksha in Year 1).
 * 5. Multi-language notification message formatting across all 16 Masikas in 5 languages.
 * 6. Worldwide city timezone conversions and high-latitude / southern hemisphere solar calculations.
 */
class AdversarialCalculationTraditionTest {

    private val bengaluru = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
    private val udupi = GeoLocation("Udupi", "Karnataka", "India", 13.3409, 74.7421, "Asia/Kolkata")
    private val mantralayam = GeoLocation("Mantralayam", "Andhra Pradesh", "India", 15.9338, 77.4297, "Asia/Kolkata")
    private val london = GeoLocation("London", "Greater London", "United Kingdom", 51.5074, -0.1278, "Europe/London")
    private val sydney = GeoLocation("Sydney", "New South Wales", "Australia", -33.8688, 151.2093, "Australia/Sydney")

    @Test
    fun testAdhikaMasaDemiseAndYear1Masikas() {
        // Demise during Adhika Jyeshtha in 2026 (approx May-June 2026)
        val deathDate = LocalDate.of(2026, 5, 25)
        val deathTime = LocalTime.of(10, 30)
        val person = PersonDeathRecord(
            name = "Adhika Demise Test",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2027, 6, 1))
        assertNotNull(result)
        assertNotNull(result.mrutaTithi)

        val year1 = result.yearlySections[0]
        assertTrue("Year 1 with Adhika Masa must have at least 17 events (16 rites + Adhika + Varshika)", year1.events.size >= 17)

        // Verify sequence is strictly monotonic
        for (i in 0 until year1.events.size - 1) {
            val curr = year1.events[i]
            val next = year1.events[i + 1]
            assertTrue(
                "Masika events must be in ascending chronological order: ${curr.traditionalName} (${curr.gregorianDate}) <= ${next.traditionalName} (${next.gregorianDate})",
                !curr.gregorianDate.isAfter(next.gregorianDate)
            )
        }

        // Fixed day rites must be exact
        val adya = year1.events.first { it.traditionalName.contains("Adya Masika") }
        assertEquals(deathDate.plusDays(12), adya.gregorianDate)

        val unmasika = year1.events.first { it.traditionalName.contains("Unmasika") && !it.traditionalName.contains("Una-Shanmasika") }
        assertEquals(deathDate.plusDays(26), unmasika.gregorianDate)

        val traipakshika = year1.events.first { it.traditionalName.contains("Traipakshika") }
        assertEquals(deathDate.plusDays(44), traipakshika.gregorianDate)

        val unaShan = year1.events.first { it.traditionalName.contains("Una-Shanmasika") }
        assertEquals(deathDate.plusDays(163), unaShan.gregorianDate)

        val unabdika = year1.events.first { it.traditionalName.contains("Unabdika") }
        assertEquals(deathDate.plusDays(350), unabdika.gregorianDate)
    }

    @Test
    fun testLeapYearFeb29DemiseCalculations() {
        val deathDate = LocalDate.of(2024, 2, 29)
        val deathTime = LocalTime.of(15, 45)
        val person = PersonDeathRecord(
            name = "Leap Day Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 3, 1))
        assertNotNull(result)
        // Feb 29, 2024 is Magha/Phalguna prior to Ugadi (which falls on Apr 9, 2024), so Samvatsara is Shobhakritu
        assertEquals("Shobhakritu", result.mrutaTithi.samvatsara)

        val year1 = result.yearlySections[0]
        val adya = year1.events.first { it.traditionalName.contains("Adya Masika") }
        assertEquals(LocalDate.of(2024, 3, 12), adya.gregorianDate)

        val unmasika = year1.events.first { it.traditionalName.contains("Unmasika") && !it.traditionalName.contains("Una-Shanmasika") }
        assertEquals(LocalDate.of(2024, 3, 26), unmasika.gregorianDate)

        val prathama = year1.events.last()
        assertEquals(ShraddhaType.VARSHIKA, prathama.type)
        assertTrue(prathama.gregorianDate.year == 2025)
    }

    @Test
    fun testAllThreeMadhwaTraditionsParityAndDifferences() {
        val deathDate = LocalDate.of(2025, 7, 10)
        val deathTime = LocalTime.of(11, 0)
        val person = PersonDeathRecord(
            name = "Parampara Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru
        )

        val umEngine = TraditionEngineFactory.getEngine(MadhwaTradition.UTTARADI_MATHA)
        val srsEngine = TraditionEngineFactory.getEngine(MadhwaTradition.MANTRALAYA_MUTT)
        val udupiEngine = TraditionEngineFactory.getEngine(MadhwaTradition.UDUPI_ASHTA_MATHA)

        // Check tradition metadata
        assertEquals("Sri Uttaradi Matha", umEngine.tradition.displayNameEnglish)
        assertEquals("Mantralaya (Sri Raghavendra Swamy Mutt)", srsEngine.tradition.displayNameEnglish)
        assertEquals("Udupi Ashta Mathas", udupiEngine.tradition.displayNameEnglish)

        assertTrue(umEngine.tradition.invocationHeaderSanskrit.contains("सत्यात्म"))
        assertTrue(srsEngine.tradition.invocationHeaderSanskrit.contains("राघवेंद्राय"))
        assertTrue(udupiEngine.tradition.invocationHeaderSanskrit.contains("विष्णवे"))

        val mrutaUM = umEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val mrutaSRS = srsEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val mrutaUdupi = udupiEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)

        assertEquals(mrutaUM.tithi.number, mrutaSRS.tithi.number)
        assertEquals(mrutaUM.tithi.number, mrutaUdupi.tithi.number)
        assertEquals(mrutaUM.masa, mrutaSRS.masa)
        assertEquals(mrutaUM.masa, mrutaUdupi.masa)

        val groupsUM = umEngine.calculateYearlyObservanceGroups(person, mrutaUM, LocalDate.of(2027, 8, 1))
        val groupsSRS = srsEngine.calculateYearlyObservanceGroups(person, mrutaSRS, LocalDate.of(2027, 8, 1))
        val groupsUdupi = udupiEngine.calculateYearlyObservanceGroups(person, mrutaUdupi, LocalDate.of(2027, 8, 1))

        assertEquals(groupsUM.size, groupsSRS.size)
        assertEquals(groupsUM.size, groupsUdupi.size)

        // Check Year 1 rules across all 3 traditions:
        // 1. Exactly 16 or 17 Masikas (17 if Year 1 spans an Adhika Masa like 2026 Adhika Jyeshtha)
        // 2. 1 Varshika event
        // 3. Paksha event is present because Prathama Varshika (July 2026) < Mahalaya (Sep 2026)
        listOf(groupsUM[0], groupsSRS[0], groupsUdupi[0]).forEach { yr1 ->
            assertEquals(1, yr1.yearIndex)
            assertTrue("Year 1 must have 16 or 17 Masikas depending on Adhika Masa", yr1.masikas.size in 16..17)
            assertNotNull(yr1.varshikaEvent)
            assertNotNull("Paksha must be applicable in Year 1 since Sapindikarana (Prathama Varshika) precedes Mahalaya", yr1.pakshaEvent)
            assertEquals(ObservanceCategory.MAHALAYA_PAKSHA, yr1.pakshaEvent!!.observanceCategory)
        }

        // Check Year 2 across all 3 traditions:
        // Varshika and Mahalaya Paksha are both present
        listOf(groupsUM[1], groupsSRS[1], groupsUdupi[1]).forEach { yr2 ->
            assertEquals(2, yr2.yearIndex)
            assertTrue(yr2.masikas.isEmpty())
            assertNotNull(yr2.varshikaEvent)
            assertNotNull(yr2.pakshaEvent)
            assertEquals(ObservanceCategory.MAHALAYA_PAKSHA, yr2.pakshaEvent!!.observanceCategory)
        }
    }

    @Test
    fun testAll16MasikaNotificationFormattingInAll5Languages() {
        val personName = "Sri Pranesh Kulkarni"
        val dateFormatted = "15 September 2026"

        val masikaList = listOf(
            "Masika 1 — Adya Masika" to "Adya Masika (13th Day)",
            "Masika 2 — Unmasika" to "Unmasika (27th Day)",
            "Masika 3 — Dvitiya Masika" to "Dvitiya Masika (2nd Month Tithi)",
            "Masika 4 — Traipakshika" to "Traipakshika (45th Day)",
            "Masika 5 — Tritiya Masika" to "Tritiya Masika (3rd Month Tithi)",
            "Masika 6 — Chaturtha Masika" to "Chaturtha Masika (4th Month Tithi)",
            "Masika 7 — Panchama Masika" to "Panchama Masika (5th Month Tithi)",
            "Masika 8 — Shashtha Masika" to "Shashtha Masika (6th Month Tithi)",
            "Masika 9 — Una-Shanmasika (with Godana)" to "Una-Shanmasika (~170th Day / Godana)",
            "Masika 10 — Saptama Masika" to "Saptama Masika (7th Month Tithi)",
            "Masika 11 — Ashtama Masika" to "Ashtama Masika (8th Month Tithi)",
            "Masika 12 — Navama Masika" to "Navama Masika (9th Month Tithi)",
            "Masika 13 — Dashama Masika" to "Dashama Masika (10th Month Tithi)",
            "Masika 14 — Ekadasha Masika" to "Ekadasha Masika (11th Month Tithi)",
            "Masika 15 — Dvadasha Masika" to "Dvadasha Masika (12th Month Tithi)",
            "Masika 16 — Unabdika (Una-Varshika)" to "Unabdika (~340th Day / Una-Varshika)",
            "Yearly Shraddha — Prathama Varshika Shraddha" to "Prathama Varshika Shraddha (1st Death Anniversary)"
        )

        val templates2Day = mapOf(
            AppLanguage.ENGLISH to "%1\$s — %2\$s is in 2 days, on %3\$s.",
            AppLanguage.KANNADA to "%1\$s — %2\$s ಇನ್ನೂ 2 ದಿನಗಳಲ್ಲಿದೆ, %3\$s ರಂದು.",
            AppLanguage.SANSKRIT to "%1\$s — %2\$s दिनद्वयानन्तरम् अस्ति, %3\$s दिनाङ्के।",
            AppLanguage.TELUGU to "%1\$s — %2\$s మరో 2 రోజుల్లో ఉంది, %3\$s తేదీన.",
            AppLanguage.TAMIL to "%1\$s — %2\$s இன்னும் 2 நாட்களில் உள்ளது, %3\$s அன்று."
        )

        AppLanguage.entries.forEach { lang ->
            masikaList.forEach { (raw, expectedEnSuffix) ->
                val localizedName = PanchangaLocalizer.localizeTraditionalName(raw, lang)
                assertNotNull("Localized name for $raw in $lang must not be null", localizedName)
                assertTrue("Localized name for $raw in $lang must not be blank", localizedName.isNotBlank())

                // Ensure indicator parenthesis are present
                assertTrue("Localized name '$localizedName' in $lang must contain day timing indicators",
                    localizedName.contains("(") && localizedName.contains(")"))

                val template = templates2Day[lang]!!
                val formattedMessage = String.format(template, personName, localizedName, dateFormatted)

                assertTrue("Notification message must contain person name", formattedMessage.contains(personName))
                assertTrue("Notification message must contain localized ritual", formattedMessage.contains(localizedName))
                assertTrue("Notification message must contain date", formattedMessage.contains(dateFormatted))
            }
        }
    }

    @Test
    fun testHighLatitudeAndSouthernHemisphereSolarAparahna() {
        val summerSolstice = LocalDate.of(2026, 6, 21)
        val winterSolstice = LocalDate.of(2026, 12, 21)

        // London, UK (51.5° N)
        val lonSummer = DinmanaCalculator.calculateDayKala(summerSolstice, london)
        val lonWinter = DinmanaCalculator.calculateDayKala(winterSolstice, london)

        assertTrue("London summer dinmana (${lonSummer.dinmanaMinutes}m) must be > 950m", lonSummer.dinmanaMinutes > 950)
        assertTrue("London winter dinmana (${lonWinter.dinmanaMinutes}m) must be < 500m", lonWinter.dinmanaMinutes < 500)
        assertTrue("London summer dinmana must exceed winter dinmana", lonSummer.dinmanaMinutes > lonWinter.dinmanaMinutes)
        assertTrue("Aparahna start must precede Aparahna end in London summer", lonSummer.aparahnaStart.isBefore(lonSummer.aparahnaEnd))

        // Sydney, Australia (-33.8° S)
        val sydJune = DinmanaCalculator.calculateDayKala(summerSolstice, sydney)
        val sydDec = DinmanaCalculator.calculateDayKala(winterSolstice, sydney)

        assertTrue("Sydney Dec dinmana (${sydDec.dinmanaMinutes}m) must be > June dinmana (${sydJune.dinmanaMinutes}m)",
            sydDec.dinmanaMinutes > sydJune.dinmanaMinutes)
        assertTrue("Aparahna start must precede Aparahna end in Sydney", sydDec.aparahnaStart.isBefore(sydDec.aparahnaEnd))
    }

    @Test
    fun testEducationalContentRepositoryLookupAll16MasikaVariants() {
        val queries = listOf(
            "Masika 1 — Adya Masika (13th Day)" to "adya_masika",
            "ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)" to "adya_masika",
            "मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्)" to "adya_masika",
            "మాసికం 1 — ఆద్య మాసికం (13వ రోజు)" to "adya_masika",
            "மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)" to "adya_masika",

            "Masika 2 — Unmasika (27th Day)" to "unmasika",
            "ಮಾಸಿಕ 2 — ಊನಮಾಸಿಕ (27ನೇ ದಿನ)" to "unmasika",

            "Masika 4 — Traipakshika (45th Day)" to "traipakshika",
            "ಮಾಸಿಕ 4 — ತ್ರೈಪಕ್ಷಿಕ (45ನೇ ದಿನ)" to "traipakshika",

            "Masika 9 — Una-Shanmasika (~170th Day / Godana)" to "una_shanmasika",
            "ಮಾಸಿಕ 9 — ಊನಷಾಣ್ಮಾಸಿಕ (170ನೇ ದಿನ / ಗೋದಾನ ಸಹಿತ)" to "una_shanmasika",

            "Masika 16 — Unabdika (~340th Day / Una-Varshika)" to "unabdika",
            "ಮಾಸಿಕ 16 — ಊನಾಬ್ದಿಕ (340ನೇ ದಿನ / ಊನವಾರ್ಷಿಕ)" to "unabdika",

            "Prathama Varshika Shraddha (1st Anniversary)" to "prathama_varshika",
            "ಪ್ರಥಮ ವಾರ್ಷಿಕ ಶ್ರಾದ್ಧ (1ನೇ ವರ್ಷದ ಶ್ರಾದ್ಧ)" to "prathama_varshika",

            "Mahalaya Paksha Shraddha (Pitru Paksha)" to "mahalaya_paksha",
            "ಮಹಾಲಯ ಪಕ್ಷ ಶ್ರಾದ್ಧ (ಪಿತೃ ಪಕ್ಷ)" to "mahalaya_paksha"
        )

        queries.forEach { (query, expectedKey) ->
            val info = EducationalContentRepository.findInfoForEvent(query)
            assertNotNull("Query '$query' must match an educational ceremony", info)
            assertEquals("Query '$query' must map to ceremonyKey '$expectedKey'", expectedKey, info!!.ceremonyKey)
        }
    }
}

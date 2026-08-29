package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.core.tradition.MantralayaTraditionEngine
import com.shraddhacalendar.core.tradition.TraditionEngineFactory
import com.shraddhacalendar.core.tradition.UdupiAshtaMathaTraditionEngine
import com.shraddhacalendar.core.tradition.UttaradiMathaTraditionEngine
import com.shraddhacalendar.data.location.CityDatabase
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Authoritative Validation Test Suite representing the scrutiny of 3 senior Acharyas:
 * 1. Acharya Srimad Ananda Tirtha (Sri Uttaradi Matha)
 * 2. Acharya Raghavendra (Sri Raghavendra Swamy Mutt, Mantralayam)
 * 3. Acharya Vadiraja (Sri Udupi Ashta Mathas)
 */
class AcharyaValidationTest {

    private val bagalkot = CityDatabase.search("Bagalkot").firstOrNull() ?: GeoLocation(
        city = "Bagalkot",
        state = "Karnataka",
        country = "India",
        latitude = 16.1817,
        longitude = 75.6958,
        timezoneId = "Asia/Kolkata"
    )

    private val bengaluru = CityDatabase.search("Bengaluru").first()
    private val mantralayam = CityDatabase.search("Mantralayam").first()
    private val udupi = CityDatabase.search("Udupi").first()
    private val dharwad = CityDatabase.search("Dharwad").firstOrNull() ?: CityDatabase.search("Hubli").first()
    private val chennai = CityDatabase.search("Chennai").first()
    private val newYork = CityDatabase.search("New York").first()
    private val london = CityDatabase.search("London").first()

    // =========================================================================
    // SECTION 1: ACHARYA SRIMAD ANANDA TIRTHA (SRI UTTARADI MATHA) — 5 TESTS
    // =========================================================================

    @Test
    fun testUM_TC1_LakshmiBai_BhadrapadaShuklaTritiyaDeath() {
        val deathDate = LocalDate.of(2025, 8, 26)
        val deathTime = LocalTime.of(12, 0)
        val person = PersonDeathRecord(
            name = "Smt. Lakshmi Bai",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bagalkot,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 27))
        val zoneId = ZoneId.of(bagalkot.timezoneId)

        // 1. Validate Death Mruta Tithi
        assertEquals("Vishvavasu", result.mrutaTithi.samvatsara)
        assertEquals(LunarMonth.BHADRAPADA, result.mrutaTithi.masa)
        assertFalse(result.mrutaTithi.isAdhikaMasa)
        assertEquals(Paksha.SHUKLA, result.mrutaTithi.tithi.paksha)
        assertEquals("Tritiya", result.mrutaTithi.tithi.name)
        assertEquals(3, result.mrutaTithi.tithi.number)

        val year1 = result.yearlySections[0]
        assertEquals(1, year1.yearIndex)
        val events = year1.events

        // 2. Strict Chronological Ordering of all Year 1 events
        for (i in 0 until events.size - 1) {
            assertTrue(
                "Event ${events[i].traditionalName} (${events[i].gregorianDate}) must be <= ${events[i+1].traditionalName} (${events[i+1].gregorianDate})",
                !events[i].gregorianDate.isAfter(events[i+1].gregorianDate)
            )
        }

        // 3. Fixed Day-Interval Rites
        val adya = events.first { it.traditionalName.contains("Adya Masika") }
        assertEquals("Adya Masika must be on Day 13", deathDate.plusDays(12), adya.gregorianDate)

        val unmasika = events.first { it.traditionalName.contains("Unmasika") && !it.traditionalName.contains("Una-Shanmasika") }
        assertEquals("Unmasika must be on Day 27", deathDate.plusDays(26), unmasika.gregorianDate)

        val traipakshika = events.first { it.traditionalName.contains("Traipakshika") }
        assertEquals("Traipakshika must be on Day 45", deathDate.plusDays(44), traipakshika.gregorianDate)

        val unaShan = events.first { it.traditionalName.contains("Una-Shanmasika") }
        assertEquals("Una-Shanmasika must be on Day 164", deathDate.plusDays(163), unaShan.gregorianDate)

        val unabdika = events.first { it.traditionalName.contains("Unabdika") }
        assertEquals("Unabdika must be on Day 351", deathDate.plusDays(350), unabdika.gregorianDate)

        // 4. Adhika Masa Handling in Year 1 (2026 has Adhika Jyeshtha)
        val adhikaEvent = events.firstOrNull { it.traditionalName.contains("Adhika Masika") }
        assertNotNull("2026 Adhika Jyeshtha must produce an Adhika Masika event", adhikaEvent)
        assertTrue(adhikaEvent!!.tithi.isAdhikaMasa)

        // 5. Prathama Varshika Shraddha must be in Nija Bhadrapada Shukla Tritiya of Parabhava
        val prathamaVarshika = events.last()
        assertEquals(ShraddhaType.VARSHIKA, prathamaVarshika.type)
        val pZdt = ZonedDateTime.of(prathamaVarshika.gregorianDate, prathamaVarshika.kalaDetails.aparahnaStart, zoneId)
        val pPanchanga = MasaCalculator.getFullPanchangaTithi(pZdt)

        assertEquals("Prathama Varshika must be in Parabhava Samvatsara", "Parabhava", pPanchanga.samvatsara)
        assertEquals("Prathama Varshika must be in Nija Bhadrapada", LunarMonth.BHADRAPADA, pPanchanga.masa)
        assertFalse("Prathama Varshika must not be in Adhika Masa", pPanchanga.isAdhikaMasa)
        assertEquals("Prathama Varshika must be Shukla Paksha", Paksha.SHUKLA, pPanchanga.tithi.paksha)
        assertEquals(LocalDate.of(2026, 9, 13), prathamaVarshika.gregorianDate)

        // 6. Year 1 Paksha is APPLICABLE because Prathama Varshika (Sapindikarana) on 13 Sep 2026 is BEFORE Mahalaya Paksha
        val y1Paksha = result.yearlyObservanceGroups[0].pakshaEvent
        assertNotNull("Year 1 observance group must have pakshaEvent since Sapindikarana is completed before Mahalaya", y1Paksha)
        assertEquals(ObservanceCategory.MAHALAYA_PAKSHA, y1Paksha!!.observanceCategory)
        assertTrue("Mahalaya date (${y1Paksha.gregorianDate}) must be after Prathama Varshika (${prathamaVarshika.gregorianDate})", y1Paksha.gregorianDate.isAfter(prathamaVarshika.gregorianDate))

        // 7. Year 2 Dvitiya Varshika must be in Nija Bhadrapada Shukla Tritiya of Plavanga
        val year2 = result.yearlySections[1]
        val dvitiyaVarshika = year2.events.first { it.type == ShraddhaType.VARSHIKA }
        val dZdt = ZonedDateTime.of(dvitiyaVarshika.gregorianDate, dvitiyaVarshika.kalaDetails.aparahnaStart, zoneId)
        val dPanchanga = MasaCalculator.getFullPanchangaTithi(dZdt)
        assertEquals("Plavanga", dPanchanga.samvatsara)
        assertEquals(LunarMonth.BHADRAPADA, dPanchanga.masa)
        assertFalse(dPanchanga.isAdhikaMasa)
        assertEquals(LocalDate.of(2027, 9, 3), dvitiyaVarshika.gregorianDate)

        // 8. Year 2 Mahalaya Paksha must be in Bhadrapada Krishna Paksha
        val mahalaya = year2.events.first { it.type == ShraddhaType.MAHALAYA_PAKSHA }
        val mZdt = ZonedDateTime.of(mahalaya.gregorianDate, mahalaya.kalaDetails.aparahnaStart, zoneId)
        val mPanchanga = MasaCalculator.getFullPanchangaTithi(mZdt)
        assertEquals(LunarMonth.BHADRAPADA, mPanchanga.masa)
        assertEquals(Paksha.KRISHNA, mPanchanga.tithi.paksha)
    }

    @Test
    fun testUM_TC2_KrishnaPakshaDeath_MultiYearChain() {
        val deathDate = LocalDate.of(2020, 8, 17)
        val deathTime = LocalTime.of(8, 0)
        val person = PersonDeathRecord(
            name = "Shakuntala",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))
        val zoneId = ZoneId.of(bengaluru.timezoneId)

        assertEquals("Sharvari", result.mrutaTithi.samvatsara)
        assertEquals(LunarMonth.SHRAVANA, result.mrutaTithi.masa)
        assertEquals(Paksha.KRISHNA, result.mrutaTithi.tithi.paksha)

        // Verify across all multi-year sections
        for (section in result.yearlySections) {
            val varshika = section.events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            assertNotNull("Year ${section.yearIndex} must have Varshika event", varshika)

            val zdt = ZonedDateTime.of(varshika!!.gregorianDate, varshika.kalaDetails.aparahnaStart, zoneId)
            val panchanga = MasaCalculator.getFullPanchangaTithi(zdt)

            assertEquals("Year ${section.yearIndex} must be in Nija Shravana", LunarMonth.SHRAVANA, panchanga.masa)
            assertFalse("Year ${section.yearIndex} must not be in Adhika Masa", panchanga.isAdhikaMasa)
            assertEquals("Year ${section.yearIndex} must be Krishna Paksha", Paksha.KRISHNA, panchanga.tithi.paksha)
            assertTrue("Aparahna window must be valid", varshika.kalaDetails.aparahnaEnd.isAfter(varshika.kalaDetails.aparahnaStart))

            if (section.yearIndex > 1) {
                val paksha = section.events.firstOrNull { it.type == ShraddhaType.MAHALAYA_PAKSHA }
                assertNotNull("Year ${section.yearIndex} must have Mahalaya Paksha event", paksha)
                val pzdt = ZonedDateTime.of(paksha!!.gregorianDate, paksha.kalaDetails.aparahnaStart, zoneId)
                val pp = MasaCalculator.getFullPanchangaTithi(pzdt)
                assertEquals(LunarMonth.BHADRAPADA, pp.masa)
                assertEquals(Paksha.KRISHNA, pp.tithi.paksha)
            }
        }
    }

    @Test
    fun testUM_TC3_AdhikaMasaYearDeath() {
        val deathDate = LocalDate.of(2023, 7, 18)
        val deathTime = LocalTime.of(14, 0)
        val person = PersonDeathRecord(
            name = "Adhika Year Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = udupi,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 1))
        val zoneId = ZoneId.of(udupi.timezoneId)

        for (section in result.yearlySections) {
            val varshika = section.events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            if (varshika != null) {
                assertEquals("Varshika must match Nija death masa", result.mrutaTithi.masa, varshika.tithi.masa)
                assertFalse("Varshika must never be in Adhika masa", varshika.tithi.isAdhikaMasa)
            }
        }
    }

    @Test
    fun testUM_TC4_PurnimaDeath_EdgeTithi() {
        val deathDate = LocalDate.of(2025, 9, 12)
        val deathTime = LocalTime.of(23, 0) // Late night
        val person = PersonDeathRecord(
            name = "Purnima Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = dharwad,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 10, 1))
        assertNotNull(result.mrutaTithi)
        assertTrue(result.yearlySections.isNotEmpty())

        val year1 = result.yearlySections[0]
        assertTrue(year1.events.size >= 16)
        val varshika = year1.events.last()
        assertEquals(ShraddhaType.VARSHIKA, varshika.type)
    }

    @Test
    fun testUM_TC5_AmavasyaDeath_EdgeTithi() {
        val deathDate = LocalDate.of(2025, 9, 21)
        val deathTime = LocalTime.of(6, 0) // Early morning
        val person = PersonDeathRecord(
            name = "Amavasya Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 10, 1))
        assertNotNull(result.mrutaTithi)
        assertTrue(result.yearlySections.isNotEmpty())

        val year1 = result.yearlySections[0]
        for (i in 0 until year1.events.size - 1) {
            assertTrue(!year1.events[i].gregorianDate.isAfter(year1.events[i+1].gregorianDate))
        }
    }

    // =========================================================================
    // SECTION 2: ACHARYA RAGHAVENDRA (MANTRALAYA MUTT) — 5 TESTS
    // =========================================================================

    @Test
    fun testSRS_TC1_CrossTraditionParity_LakshmiBai() {
        val deathDate = LocalDate.of(2025, 8, 26)
        val deathTime = LocalTime.of(12, 0)
        val person = PersonDeathRecord(
            name = "Smt. Lakshmi Bai",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bagalkot
        )

        val umEngine = UttaradiMathaTraditionEngine()
        val srsEngine = MantralayaTraditionEngine()

        val mrutaUM = umEngine.calculateMrutaTithi(deathDate, deathTime, bagalkot)
        val mrutaSRS = srsEngine.calculateMrutaTithi(deathDate, deathTime, bagalkot)
        assertEquals(mrutaUM.tithi.number, mrutaSRS.tithi.number)
        assertEquals(mrutaUM.masa, mrutaSRS.masa)

        val groupsUM = umEngine.calculateYearlyObservanceGroups(person, mrutaUM, LocalDate.of(2027, 10, 1))
        val groupsSRS = srsEngine.calculateYearlyObservanceGroups(person, mrutaSRS, LocalDate.of(2027, 10, 1))

        assertEquals("Year count must match between UM and SRS", groupsUM.size, groupsSRS.size)

        for (yr in groupsUM.indices) {
            val gUM = groupsUM[yr]
            val gSRS = groupsSRS[yr]

            assertEquals("Year index must match", gUM.yearIndex, gSRS.yearIndex)
            assertEquals("Masikas count must match", gUM.masikas.size, gSRS.masikas.size)

            for (m in gUM.masikas.indices) {
                assertEquals(
                    "Year ${gUM.yearIndex} Masika $m date must match between UM and SRS",
                    gUM.masikas[m].gregorianDate,
                    gSRS.masikas[m].gregorianDate
                )
            }

            if (gUM.varshikaEvent != null) {
                assertNotNull(gSRS.varshikaEvent)
                assertEquals(
                    "Year ${gUM.yearIndex} Varshika date must match between UM and SRS",
                    gUM.varshikaEvent!!.gregorianDate,
                    gSRS.varshikaEvent!!.gregorianDate
                )
            }

            if (gUM.pakshaEvent != null) {
                assertNotNull(gSRS.pakshaEvent)
                assertEquals(
                    "Year ${gUM.yearIndex} Mahalaya Paksha date must match between UM and SRS",
                    gUM.pakshaEvent!!.gregorianDate,
                    gSRS.pakshaEvent!!.gregorianDate
                )
            }
        }
    }

    @Test
    fun testSRS_TC2_CrossTraditionParity_Shakuntala() {
        val deathDate = LocalDate.of(2020, 8, 17)
        val deathTime = LocalTime.of(8, 0)
        val person = PersonDeathRecord(name = "Shakuntala", deathDate = deathDate, deathTime = deathTime, location = bengaluru)

        val um = UttaradiMathaTraditionEngine()
        val srs = MantralayaTraditionEngine()

        val mUM = um.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val mSRS = srs.calculateMrutaTithi(deathDate, deathTime, bengaluru)

        val gUM = um.calculateYearlyObservanceGroups(person, mUM, LocalDate.of(2026, 8, 20))
        val gSRS = srs.calculateYearlyObservanceGroups(person, mSRS, LocalDate.of(2026, 8, 20))

        for (i in gUM.indices) {
            val vUM = gUM[i].varshikaEvent
            val vSRS = gSRS[i].varshikaEvent
            if (vUM != null && vSRS != null) {
                assertEquals("Year ${gUM[i].yearIndex} Varshika date parity", vUM.gregorianDate, vSRS.gregorianDate)
            }
        }
    }

    @Test
    fun testSRS_TC3_MantralayamLocation_AparahnaCoordinates() {
        val deathDate = LocalDate.of(2025, 11, 10)
        val deathTime = LocalTime.of(13, 15)
        val person = PersonDeathRecord(
            name = "Rayara Bhakta",
            deathDate = deathDate,
            deathTime = deathTime,
            location = mantralayam,
            tradition = MadhwaTradition.MANTRALAYA_MUTT
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 12, 1))
        assertNotNull(result)
        assertEquals("Mantralayam", result.personRecord.location.city)
        val year1 = result.yearlySections[0]
        assertTrue(year1.events.size >= 16)
    }

    @Test
    fun testSRS_TC4_LongRange10YearProgression() {
        val deathDate = LocalDate.of(2020, 1, 1)
        val deathTime = LocalTime.of(10, 0)
        val person = PersonDeathRecord(
            name = "Long Range Test",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.MANTRALAYA_MUTT
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2030, 1, 1))
        val zoneId = ZoneId.of(bengaluru.timezoneId)

        assertTrue("Must calculate at least 10 annual sections", result.yearlySections.size >= 10)

        var prevDate = result.yearlySections[0].events.last().gregorianDate
        for (i in 1 until result.yearlySections.size) {
            val varshika = result.yearlySections[i].events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            if (varshika != null) {
                val zdt = ZonedDateTime.of(varshika.gregorianDate, varshika.kalaDetails.aparahnaStart, zoneId)
                val panchanga = MasaCalculator.getFullPanchangaTithi(zdt)
                assertEquals(result.mrutaTithi.masa, panchanga.masa)
                assertFalse(panchanga.isAdhikaMasa)

                val daysDiff = java.time.temporal.ChronoUnit.DAYS.between(prevDate, varshika.gregorianDate)
                assertTrue("Annual interval should be between 320 and 390 days (was $daysDiff)", daysDiff in 320..390)
                prevDate = varshika.gregorianDate
            }
        }
    }

    @Test
    fun testSRS_TC5_MargashirshaWinterDeath() {
        val deathDate = LocalDate.of(2024, 12, 15)
        val deathTime = LocalTime.of(16, 0)
        val person = PersonDeathRecord(
            name = "Winter Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = chennai,
            tradition = MadhwaTradition.MANTRALAYA_MUTT
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 1, 1))
        assertNotNull(result)
        val year1 = result.yearlySections[0]
        val varshika = year1.events.last()
        assertEquals(ShraddhaType.VARSHIKA, varshika.type)
        assertTrue(varshika.kalaDetails.aparahnaEnd.isAfter(varshika.kalaDetails.aparahnaStart))
    }

    // =========================================================================
    // SECTION 3: ACHARYA VADIRAJA (UDUPI ASHTA MATHAS) — 5 TESTS
    // =========================================================================

    @Test
    fun testUPM_TC1_CrossTraditionParity_LakshmiBai() {
        val deathDate = LocalDate.of(2025, 8, 26)
        val deathTime = LocalTime.of(12, 0)
        val person = PersonDeathRecord(name = "Smt. Lakshmi Bai", deathDate = deathDate, deathTime = deathTime, location = bagalkot)

        val um = UttaradiMathaTraditionEngine()
        val udupi = UdupiAshtaMathaTraditionEngine()

        val mUM = um.calculateMrutaTithi(deathDate, deathTime, bagalkot)
        val mUdupi = udupi.calculateMrutaTithi(deathDate, deathTime, bagalkot)

        val gUM = um.calculateYearlyObservanceGroups(person, mUM, LocalDate.of(2027, 10, 1))
        val gUdupi = udupi.calculateYearlyObservanceGroups(person, mUdupi, LocalDate.of(2027, 10, 1))

        assertEquals(gUM.size, gUdupi.size)
        for (i in gUM.indices) {
            val vUM = gUM[i].varshikaEvent
            val vUdupi = gUdupi[i].varshikaEvent
            if (vUM != null && vUdupi != null) {
                assertEquals("Year ${gUM[i].yearIndex} Varshika date parity between UM and Udupi", vUM.gregorianDate, vUdupi.gregorianDate)
            }
        }
    }

    @Test
    fun testUPM_TC2_UdupiLocation_CoastalCoordinates() {
        val deathDate = LocalDate.of(2025, 3, 5)
        val deathTime = LocalTime.of(11, 30)
        val person = PersonDeathRecord(
            name = "Udupi Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = udupi,
            tradition = MadhwaTradition.UDUPI_ASHTA_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 4, 1))
        assertNotNull(result)
        assertEquals("Udupi", result.personRecord.location.city)
        val year1 = result.yearlySections[0]
        assertTrue(year1.events.size >= 16)
    }

    @Test
    fun testUPM_TC3_InternationalLocation_NewYork() {
        val deathDate = LocalDate.of(2025, 6, 15)
        val deathTime = LocalTime.of(14, 0)
        val person = PersonDeathRecord(
            name = "NRI Devotee NY",
            deathDate = deathDate,
            deathTime = deathTime,
            location = newYork,
            tradition = MadhwaTradition.UDUPI_ASHTA_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 7, 1))
        assertNotNull(result)
        assertEquals("New York", result.personRecord.location.city)
        assertEquals("America/New_York", result.personRecord.location.timezoneId)
        val year1 = result.yearlySections[0]
        assertTrue(year1.events.size >= 16)
    }

    @Test
    fun testUPM_TC4_InternationalLocation_London() {
        val deathDate = LocalDate.of(2024, 12, 20)
        val deathTime = LocalTime.of(10, 0)
        val person = PersonDeathRecord(
            name = "NRI Devotee London",
            deathDate = deathDate,
            deathTime = deathTime,
            location = london,
            tradition = MadhwaTradition.UDUPI_ASHTA_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 1, 1))
        assertNotNull(result)
        assertEquals("Europe/London", result.personRecord.location.timezoneId)
        val year1 = result.yearlySections[0]
        val varshika = year1.events.last()
        assertEquals(ShraddhaType.VARSHIKA, varshika.type)
    }

    @Test
    fun testUPM_TC5_ChaitraYearBoundaryDeath() {
        val deathDate = LocalDate.of(2026, 3, 31)
        val deathTime = LocalTime.of(12, 0)
        val person = PersonDeathRecord(
            name = "Chaitra Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UDUPI_ASHTA_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2027, 5, 1))
        assertNotNull(result)
        val year1 = result.yearlySections[0]
        assertTrue(year1.events.size >= 16)
        val prathama = year1.events.last()
        assertEquals(ShraddhaType.VARSHIKA, prathama.type)
    }
}

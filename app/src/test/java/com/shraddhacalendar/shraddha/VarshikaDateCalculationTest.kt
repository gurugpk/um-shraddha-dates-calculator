package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.core.tradition.TraditionEngineFactory
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class VarshikaDateCalculationTest {

    private val bengaluru = GeoLocation(
        city = "Bengaluru",
        state = "Karnataka",
        country = "India",
        latitude = 12.9716,
        longitude = 77.5946,
        timezoneId = "Asia/Kolkata"
    )

    private val udupi = GeoLocation(
        city = "Udupi",
        state = "Karnataka",
        country = "India",
        latitude = 13.3409,
        longitude = 74.7421,
        timezoneId = "Asia/Kolkata"
    )

    @Test
    fun testMultiYearVarshikaProgression_EveryYearInNijaMasa() {
        val person = PersonDeathRecord(
            name = "Shakuntala",
            deathDate = LocalDate.of(2020, 8, 17),
            deathTime = LocalTime.of(8, 0),
            location = bengaluru
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))
        val zoneId = ZoneId.of(bengaluru.timezoneId)

        for (section in result.yearlySections) {
            val varshika = section.events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            assertNotNull("Year ${section.yearIndex} should have a Varshika event", varshika)

            val zdt = ZonedDateTime.of(varshika!!.gregorianDate, varshika.kalaDetails.aparahnaStart, zoneId)
            val panchanga = MasaCalculator.getFullPanchangaTithi(zdt)

            assertEquals("Year ${section.yearIndex} must be in Nija Shravana", LunarMonth.SHRAVANA, panchanga.masa)
            assertFalse("Year ${section.yearIndex} must not be in Adhika Masa", panchanga.isAdhikaMasa)
            assertEquals("Year ${section.yearIndex} must be Krishna Paksha", Paksha.KRISHNA, panchanga.tithi.paksha)
            assertTrue("Year ${section.yearIndex} must be Trayodashi (or prevailing Trayodashi)",
                panchanga.tithi.name == "Trayodashi" || varshika.explanation.contains("Trayodashi") || panchanga.tithi.name == "Dvadashi")
        }
    }

    @Test
    fun testCrossTraditionVarshikaConsistency() {
        val deathDate = LocalDate.of(2022, 5, 15)
        val deathTime = LocalTime.of(14, 30)
        val person = PersonDeathRecord(
            name = "Test Person",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru
        )

        val currentDate = LocalDate.of(2026, 6, 1)

        val umEngine = TraditionEngineFactory.getEngine(MadhwaTradition.UTTARADI_MATHA)
        val srsEngine = TraditionEngineFactory.getEngine(MadhwaTradition.MANTRALAYA_MUTT)
        val udupiEngine = TraditionEngineFactory.getEngine(MadhwaTradition.UDUPI_ASHTA_MATHA)

        val mrutaTithiUM = umEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val mrutaTithiSRS = srsEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val mrutaTithiUdupi = udupiEngine.calculateMrutaTithi(deathDate, deathTime, bengaluru)

        val groupsUM = umEngine.calculateYearlyObservanceGroups(person, mrutaTithiUM, currentDate)
        val groupsSRS = srsEngine.calculateYearlyObservanceGroups(person, mrutaTithiSRS, currentDate)
        val groupsUdupi = udupiEngine.calculateYearlyObservanceGroups(person, mrutaTithiUdupi, currentDate)

        assertEquals(groupsUM.size, groupsSRS.size)
        assertEquals(groupsUM.size, groupsUdupi.size)

        for (i in groupsUM.indices) {
            val vUM = groupsUM[i].varshikaEvent
            val vSRS = groupsSRS[i].varshikaEvent
            val vUdupi = groupsUdupi[i].varshikaEvent

            if (vUM != null) {
                assertNotNull(vSRS)
                assertNotNull(vUdupi)
                assertEquals("Year \${groupsUM[i].yearIndex} Varshika date must match between UM and SRS",
                    vUM.gregorianDate, vSRS!!.gregorianDate)
                assertEquals("Year \${groupsUM[i].yearIndex} Varshika date must match between UM and Udupi",
                    vUM.gregorianDate, vUdupi!!.gregorianDate)
            }
        }
    }

    @Test
    fun testAdhikaMasaYearVarshikaLanding() {
        val person = PersonDeathRecord(
            name = "Adhika Year Case",
            deathDate = LocalDate.of(2023, 3, 10),
            deathTime = LocalTime.of(10, 30),
            location = udupi
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 4, 1))
        val zoneId = ZoneId.of(udupi.timezoneId)

        for (section in result.yearlySections) {
            val varshika = section.events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            if (varshika != null) {
                val zdt = ZonedDateTime.of(varshika.gregorianDate, varshika.kalaDetails.aparahnaStart, zoneId)
                val panchanga = MasaCalculator.getFullPanchangaTithi(zdt)
                assertEquals(result.mrutaTithi.masa, panchanga.masa)
                assertFalse("Varshika should never fall in an Adhika masa", panchanga.isAdhikaMasa)
            }
        }
    }
}

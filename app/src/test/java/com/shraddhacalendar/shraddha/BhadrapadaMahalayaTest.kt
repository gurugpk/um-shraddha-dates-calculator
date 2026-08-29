package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.shraddha.BhadrapadaFinder
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class BhadrapadaMahalayaTest {

    private val bengaluru = GeoLocation(
        city = "Bengaluru",
        state = "Karnataka",
        country = "India",
        latitude = 12.9716,
        longitude = 77.5946,
        timezoneId = "Asia/Kolkata"
    )

    @Test
    fun testBhadrapadaWindowCalculation_MultipleYears() {
        for (year in 2021..2028) {
            val window = BhadrapadaFinder.findBhadrapadaKrishnaPaksha(year)
            assertNotNull(window)
            assertTrue("Amavasya should be in Sep or Oct for year $year",
                window.amavasyaDate.monthValue in 9..10)
            assertTrue("Krishna paksha should start before Amavasya",
                window.krishnaPakshaStart.isBefore(window.amavasyaDate))
        }
    }

    @Test
    fun testMahalayaPakshaEvent_AlwaysInBhadrapada() {
        val person = PersonDeathRecord(
            name = "Mahalaya Test Person",
            deathDate = LocalDate.of(2021, 2, 10),
            deathTime = LocalTime.of(11, 0),
            location = bengaluru
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 11, 1))
        val zoneId = ZoneId.of(bengaluru.timezoneId)

        for (section in result.yearlySections) {
            if (section.yearIndex > 1) {
                val paksha = section.events.firstOrNull { it.type == ShraddhaType.MAHALAYA_PAKSHA }
                assertNotNull("Year ${section.yearIndex} must have Mahalaya Paksha event", paksha)

                val zdt = ZonedDateTime.of(paksha!!.gregorianDate, paksha.kalaDetails.aparahnaStart, zoneId)
                val panchanga = MasaCalculator.getFullPanchangaTithi(zdt)

                assertEquals("Mahalaya must be in Bhadrapada", LunarMonth.BHADRAPADA, panchanga.masa)
                assertFalse("Mahalaya must not be in Adhika Masa", panchanga.isAdhikaMasa)
                assertEquals("Mahalaya must be in Krishna Paksha", Paksha.KRISHNA, panchanga.tithi.paksha)
            }
        }
    }
}

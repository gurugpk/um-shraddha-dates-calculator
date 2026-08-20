package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.models.ShraddhaType
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ShraddhaCalculationsTest {

    private val bengaluru = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
    private val newYork = GeoLocation("New York", "New York", "United States", 40.7128, -74.0060, "America/New_York")

    @Test
    fun testRecentDeathGeneratesFull16ShodashaRitesInYear1() {
        val deathDate = LocalDate.of(2026, 8, 15)
        val deathTime = LocalTime.of(14, 30) // 2:30 PM (Mandatory)
        val person = PersonDeathRecord(name = "Test Deceased", deathDate = deathDate, deathTime = deathTime, location = bengaluru)

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))

        assertFalse(result.isDeathOlderThanOneYear)
        assertNull(result.nextUpcomingShraddha)
        assertEquals(5, result.yearlySections.size)

        val year1 = result.yearlySections[0]
        assertEquals(1, year1.yearIndex)
        assertTrue(year1.isExpandedByDefault)
        assertTrue("Year 1 should contain 16+ Shodasha events", year1.events.size >= 16)

        // Check traditional names sequence
        val eventNames = year1.events.map { it.traditionalName }
        assertTrue(eventNames.any { it.contains("Adya Masika") })
        assertTrue(eventNames.any { it.contains("Unmasika") })
        assertTrue(eventNames.any { it.contains("Dwitiya Masika") })
        assertTrue(eventNames.any { it.contains("Traipakshika") })
        assertTrue(eventNames.any { it.contains("Tritiya Masika") })
        assertTrue(eventNames.any { it.contains("Chaturtha Masika") })
        assertTrue(eventNames.any { it.contains("Panchama Masika") })
        assertTrue(eventNames.any { it.contains("Una-Shanmasika") })
        assertTrue(eventNames.any { it.contains("Saptama Masika") })
        assertTrue(eventNames.any { it.contains("Unabdika") })
        assertTrue(eventNames.any { it.contains("Yearly Shraddha") || it.contains("Prathama Varshika") })

        // Check Years 2..5
        for (i in 1..4) {
            val section = result.yearlySections[i]
            assertEquals(i + 1, section.yearIndex)
            assertFalse(section.isExpandedByDefault)
            assertEquals(1, section.events.size)
            assertEquals(ShraddhaType.VARSHIKA, section.events[0].type)
        }
    }

    @Test
    fun testDeathOlderThanOneYearShowsSingleNextUpcoming() {
        // Death in 2024 (2 years ago relative to 2026)
        val deathDate = LocalDate.of(2024, 1, 10)
        val deathTime = LocalTime.of(8, 0)
        val person = PersonDeathRecord(name = "Past Ancestor", deathDate = deathDate, deathTime = deathTime, location = bengaluru)

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))

        assertTrue(result.isDeathOlderThanOneYear)
        assertNotNull(result.nextUpcomingShraddha)
        assertTrue(result.yearlySections.isEmpty())

        val upcoming = result.nextUpcomingShraddha!!
        assertEquals(ShraddhaType.VARSHIKA, upcoming.type)
        assertFalse(upcoming.gregorianDate.isBefore(LocalDate.of(2026, 8, 20)))
    }

    @Test
    fun testLocationSensitivityBengaluruVsNewYork() {
        val deathDate = LocalDate.of(2026, 8, 15)
        val deathTime = LocalTime.of(12, 0)

        val personBlr = PersonDeathRecord(name = "BLR Person", deathDate = deathDate, deathTime = deathTime, location = bengaluru)
        val personNy = PersonDeathRecord(name = "NY Person", deathDate = deathDate, deathTime = deathTime, location = newYork)

        val resBlr = ShraddhaCalculator.calculate(personBlr, currentDate = LocalDate.of(2026, 8, 15))
        val resNy = ShraddhaCalculator.calculate(personNy, currentDate = LocalDate.of(2026, 8, 15))

        val blrKala = resBlr.yearlySections[0].events[0].kalaDetails
        val nyKala = resNy.yearlySections[0].events[0].kalaDetails

        // Sunrise and Aparahna timings in local time are specific to their respective latitudes & longitudes
        assertNotEquals(blrKala.dinmanaMinutes, nyKala.dinmanaMinutes)
    }

    @Test
    fun testBoundaryConditions() {
        val today = LocalDate.of(2026, 8, 20)

        // Death today
        val personToday = PersonDeathRecord(name = "Person Today", deathDate = today, deathTime = LocalTime.of(10, 0), location = bengaluru)
        val resToday = ShraddhaCalculator.calculate(personToday, currentDate = today)
        assertFalse(resToday.isDeathOlderThanOneYear)

        // Death 360 days ago
        val date360Ago = today.minusDays(360)
        val person360 = PersonDeathRecord(name = "Person 360", deathDate = date360Ago, deathTime = LocalTime.of(10, 0), location = bengaluru)
        val res360 = ShraddhaCalculator.calculate(person360, currentDate = today)
        assertNotNull(res360)
    }
}

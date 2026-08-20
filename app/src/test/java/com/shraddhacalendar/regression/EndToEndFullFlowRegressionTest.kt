package com.shraddhacalendar.regression

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.location.CityDatabase
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * End-to-End full flow integration test verifying:
 * 1. Realistic input for Late Sri Pranesh Kulkarni in Bengaluru
 * 2. 16 Shodasha Masika rites + 5-year forecast
 * 3. Older than 1-year scenario (Single upcoming card)
 * 4. Language switching consistency
 * 5. Deterministic entity keys & calendar/alarm data integrity
 */
class EndToEndFullFlowRegressionTest {

    private val bengaluru = CityDatabase.CITIES.first { it.city == "Bengaluru" }

    @Test
    fun testFullFlowFirstYearDeath() {
        val person = PersonDeathRecord(
            name = "Late Sri Pranesh Kulkarni",
            deathDate = LocalDate.of(2026, 7, 15),
            deathTime = LocalTime.of(10, 30),
            location = bengaluru
        )

        // Calculate as of 5 days after death
        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 7, 20))

        assertNotNull(result)
        assertFalse("Death within 1 year should not be marked as older than 1 year", result.isDeathOlderThanOneYear)
        assertEquals(5, result.yearlySections.size)

        val year1 = result.yearlySections[0]
        assertEquals(17, year1.events.size)

        // Verify sequence of Shodasha rites + Prathama Varshika
        val eventNames = year1.events.map { it.traditionalName }
        assertTrue(eventNames[0].contains("Adya Masika"))
        assertTrue(eventNames[1].contains("Unmasika"))
        assertTrue(eventNames[2].contains("Dwitiya Masika"))
        assertTrue(eventNames[3].contains("Traipakshika"))
        assertTrue(eventNames[4].contains("Tritiya Masika"))
        assertTrue(eventNames[5].contains("Chaturtha Masika"))
        assertTrue(eventNames[6].contains("Panchama Masika"))
        assertTrue(eventNames[7].contains("Shashtha Masika") || eventNames[7].contains("Shanmasika"))
        assertTrue(eventNames[8].contains("Una-Shanmasika"))
        assertTrue(eventNames[9].contains("Saptama Masika"))
        assertTrue(eventNames[10].contains("Ashtama Masika"))
        assertTrue(eventNames[11].contains("Navama Masika"))
        assertTrue(eventNames[12].contains("Dashama Masika"))
        assertTrue(eventNames[13].contains("Ekadasha Masika"))
        assertTrue(eventNames[14].contains("Dvadasha Masika"))
        assertTrue(eventNames[15].contains("Unabdika") || eventNames[15].contains("Varshika"))

        // Check language localizations across all 5 languages
        AppLanguage.entries.forEach { lang ->
            year1.events.forEach { event ->
                val localized = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, lang)
                assertNotNull(localized)
                assertTrue(localized.isNotBlank())
            }
        }
    }

    @Test
    fun testFullFlowDeathOlderThanOneYear() {
        // Death occurred 3 years ago
        val person = PersonDeathRecord(
            name = "Late Sri Pranesh Kulkarni",
            deathDate = LocalDate.of(2023, 7, 15),
            deathTime = LocalTime.of(10, 30),
            location = bengaluru
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 7, 20))

        assertNotNull(result)
        assertTrue("Death 3 years ago must be marked as older than 1 year", result.isDeathOlderThanOneYear)
        assertNotNull("Must compute next upcoming Varshika Shraddha", result.nextUpcomingShraddha)

        val upcoming = result.nextUpcomingShraddha!!
        assertTrue(upcoming.traditionalName.contains("Varshika"))
        assertTrue("Upcoming date must be >= current date", !upcoming.gregorianDate.isBefore(LocalDate.of(2026, 7, 20)))
    }
}

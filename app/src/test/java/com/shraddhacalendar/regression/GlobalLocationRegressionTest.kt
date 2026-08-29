package com.shraddhacalendar.regression

import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.location.CityDatabase
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Regression test suite verifying Panchanga & Shraddha calculations across
 * 12+ world representative locations with diverse timezones, DST rules,
 * and hemispheric solar trajectories.
 */
class GlobalLocationRegressionTest {

    private val deathDate = LocalDate.of(2026, 7, 20)
    private val deathTime = LocalTime.of(14, 30)

    @Test
    fun testGlobalLocationsCalculations() {
        val testCities = listOf(
            "Bengaluru",     // India (Asia/Kolkata)
            "Udupi",         // India Pilgrimage
            "Mantralayam",   // India Pilgrimage
            "Varanasi",      // India North
            "New York",      // USA East (EDT/EST with DST)
            "San Francisco", // USA West (PDT/PST with DST)
            "Chicago",       // USA Central (CDT/CST with DST)
            "London",        // UK (BST/GMT with DST)
            "Toronto",       // Canada (EDT/EST with DST)
            "Sydney",        // Australia Southern Hemisphere (AEST/AEDT)
            "Dubai",         // Middle East (Asia/Dubai, No DST)
            "Singapore",     // Equatorial (Asia/Singapore, UTC+8)
            "Tokyo",         // Japan (Asia/Tokyo, UTC+9, No DST)
            "Paris",         // Western Europe (CEST/CET with DST)
            "Auckland"       // Southern Hemisphere / Pacific
        )

        testCities.forEach { cityName ->
            val location = CityDatabase.CITIES.firstOrNull { it.city.contains(cityName, ignoreCase = true) }
            assertNotNull("City $cityName must exist in CityDatabase", location)

            val person = PersonDeathRecord(
                name = "Test Person ($cityName)",
                deathDate = deathDate,
                deathTime = deathTime,
                location = location!!
            )

            val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 7, 25))
            assertNotNull("Result for $cityName must not be null", result)

            // Verify Mruta Tithi
            assertNotNull(result.mrutaTithi.samvatsara)
            assertNotNull(result.mrutaTithi.masa)
            assertNotNull(result.mrutaTithi.tithi)

            // Verify Year 1 contains all 16 Shodasha ceremonies + Prathama Varshika (17 total events)
            val year1 = result.yearlySections[0]
            assertTrue("Year 1 must have at least 16-17 events for $cityName", year1.events.size >= 16)
            assertEquals("Year 1 must have 17 events (16 Shodasha rites + Prathama Varshika) for $cityName", 17, year1.events.size)

            // Verify Aparahna and Sun timings are physically valid
            year1.events.forEach { event ->
                val kala = event.kalaDetails
                assertTrue("Sunrise before sunset in $cityName on ${event.gregorianDate}", kala.sunrise.isBefore(kala.sunset))
                assertTrue("Dinmana positive in $cityName", kala.dinmanaMinutes > 0)
                assertTrue("Aparahna start before Aparahna end in $cityName", kala.aparahnaStart.isBefore(kala.aparahnaEnd))
                assertTrue("Aparahna start after sunrise in $cityName", kala.aparahnaStart.isAfter(kala.sunrise))
            }

            // Verify year sections
            assertTrue("Must have year sections for $cityName", result.yearlySections.isNotEmpty())
        }
    }
}

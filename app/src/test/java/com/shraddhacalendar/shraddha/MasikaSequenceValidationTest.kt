package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.MasikaShraddhaCalculator
import com.shraddhacalendar.core.tradition.UttaradiMathaTraditionEngine
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class MasikaSequenceValidationTest {

    private val bengaluru = GeoLocation(
        city = "Bengaluru",
        state = "Karnataka",
        country = "India",
        latitude = 12.9716,
        longitude = 77.5946,
        timezoneId = "Asia/Kolkata"
    )

    @Test
    fun testShodashaMasikas_StrictChronologicalOrder() {
        val deathDate = LocalDate.of(2025, 4, 10)
        val deathTime = LocalTime.of(9, 30)
        val person = PersonDeathRecord(
            name = "Masika Sequence Test",
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru
        )

        val engine = UttaradiMathaTraditionEngine()
        val mrutaTithi = engine.calculateMrutaTithi(deathDate, deathTime, bengaluru)
        val events = MasikaShraddhaCalculator.calculateYear1Events(person, mrutaTithi)

        // Must have at least 17 events (16 rites + 1st Varshika)
        assertTrue(events.size >= 17)

        // Strict chronological ordering
        for (i in 0 until events.size - 1) {
            val curr = events[i]
            val next = events[i + 1]
            assertTrue("Event \${curr.traditionalName} (\${curr.gregorianDate}) must be on or before \${next.traditionalName} (\${next.gregorianDate})",
                !curr.gregorianDate.isAfter(next.gregorianDate))
        }

        // Fixed-interval rites exact dates
        val adya = events.first { it.traditionalName.contains("Adya Masika") }
        assertEquals(deathDate.plusDays(12), adya.gregorianDate)

        val unmasika = events.first { it.traditionalName.contains("Unmasika") && !it.traditionalName.contains("Una-Shanmasika") }
        assertEquals(deathDate.plusDays(26), unmasika.gregorianDate)

        val traipakshika = events.first { it.traditionalName.contains("Traipakshika") }
        assertEquals(deathDate.plusDays(44), traipakshika.gregorianDate)

        val unaShan = events.first { it.traditionalName.contains("Una-Shanmasika") }
        assertEquals(deathDate.plusDays(163), unaShan.gregorianDate)

        val unabdika = events.first { it.traditionalName.contains("Unabdika") }
        assertEquals(deathDate.plusDays(350), unabdika.gregorianDate)
    }
}

package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.*
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AdhikaTestTrace {
    @Test
    fun traceAdhikaYear() {
        val deathDate = LocalDate.of(2023, 7, 18)
        val deathTime = LocalTime.of(14, 0)
        val udupi = GeoLocation(
            city = "Udupi",
            state = "Karnataka",
            country = "India",
            latitude = 13.3409,
            longitude = 74.7421,
            timezoneId = "Asia/Kolkata"
        )
        val person = PersonDeathRecord(
            name = "Adhika Year Devotee",
            deathDate = deathDate,
            deathTime = deathTime,
            location = udupi,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 1))
        println("=== ADHIKA TEST TRACE ===")
        println("Death Mruta Tithi: Masa=${result.mrutaTithi.masa}, Adhika=${result.mrutaTithi.isAdhikaMasa}, Tithi=${result.mrutaTithi.tithi.name} (#${result.mrutaTithi.tithi.number})")

        val zoneId = ZoneId.of(udupi.timezoneId)
        for (section in result.yearlySections) {
            val varshika = section.events.firstOrNull { it.type == ShraddhaType.VARSHIKA }
            if (varshika != null) {
                val zdt = ZonedDateTime.of(varshika.gregorianDate, varshika.kalaDetails.aparahnaStart, zoneId)
                val panchangaAtAp = MasaCalculator.getFullPanchangaTithi(zdt)
                println("Year ${section.yearIndex}: Date=${varshika.gregorianDate} | EventTithi=${varshika.tithi.tithi.name} | EventMasa=${varshika.tithi.masa} | AtApStartMasa=${panchangaAtAp.masa} | AtApStartTithi=${panchangaAtAp.tithi.name} (#${panchangaAtAp.tithi.number})")
            }
        }
    }
}

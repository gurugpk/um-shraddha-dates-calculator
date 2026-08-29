package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.*
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class ShakuntalaYear5TraceTest {

    @Test
    fun traceAugust2025() {
        val location = GeoLocation(
            city = "Bengaluru",
            state = "Karnataka",
            country = "India",
            latitude = 12.9716,
            longitude = 77.5946,
            timezoneId = "Asia/Kolkata"
        )
        val zoneId = ZoneId.of("Asia/Kolkata")
        val scanStart = LocalDate.of(2025, 8, 19)
        val scanEnd = LocalDate.of(2025, 8, 23)
        var d = scanStart
        println("=== YEAR 5 (AUGUST 2025) TRACE ===")
        while (!d.isAfter(scanEnd)) {
            val kala = DinmanaCalculator.calculateDayKala(d, location)
            val sunriseZdt = ZonedDateTime.of(d, kala.sunrise, zoneId)
            val sunsetZdt = ZonedDateTime.of(d, kala.sunset, zoneId)
            val apStartZdt = ZonedDateTime.of(d, kala.aparahnaStart, zoneId)
            val apEndZdt = ZonedDateTime.of(d, kala.aparahnaEnd, zoneId)

            val sunriseTithi = TithiCalculator.getTithiAt(sunriseZdt)
            val apStartTithi = TithiCalculator.getTithiAt(apStartZdt)
            val apEndTithi = TithiCalculator.getTithiAt(apEndZdt)
            val panchangaAtAp = MasaCalculator.getFullPanchangaTithi(apStartZdt)

            println("\nDate: $d (${d.dayOfWeek})")
            println("  Sunrise (${kala.sunrise}): Tithi ${sunriseTithi.name} (#${sunriseTithi.number}, ${sunriseTithi.paksha})")
            println("  Aparahna (${kala.aparahnaStart} to ${kala.aparahnaEnd}):")
            println("    Start Tithi: ${apStartTithi.name} (#${apStartTithi.number})")
            println("    End Tithi:   ${apEndTithi.name} (#${apEndTithi.number})")
            println("  Panchanga at Ap Start: Masa=${panchangaAtAp.masa}, Adhika=${panchangaAtAp.isAdhikaMasa}, Samvatsara=${panchangaAtAp.samvatsara}, Tithi=${panchangaAtAp.tithi.name}")

            var sample = sunriseZdt
            var prevTithi = -1
            while (sample.isBefore(sunsetZdt)) {
                val t = TithiCalculator.getTithiAt(sample)
                if (prevTithi != -1 && t.number != prevTithi) {
                    println("  * Tithi Transition from #$prevTithi to #${t.number} (${t.name}) at ${sample.toLocalTime()}")
                }
                prevTithi = t.number
                sample = sample.plusSeconds(60)
            }

            var trayodashiSec = 0L
            sample = apStartZdt
            while (sample.isBefore(apEndZdt)) {
                val t = TithiCalculator.getTithiAt(sample)
                if (t.number == 28) trayodashiSec += 60
                sample = sample.plusSeconds(60)
            }
            println("  Aparahna Trayodashi (Tithi 28) overlap: ${trayodashiSec / 60} mins")

            d = d.plusDays(1)
        }
    }
}

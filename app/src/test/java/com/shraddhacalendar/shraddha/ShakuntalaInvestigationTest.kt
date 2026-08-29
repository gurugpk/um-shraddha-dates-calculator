package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.*
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class ShakuntalaInvestigationTest {

    @Test
    fun testShakuntalaCalculationTrace() {
        val deathDate = LocalDate.of(2020, 8, 17)
        val deathTime = LocalTime.of(12, 0)
        val location = GeoLocation(
            city = "Bengaluru",
            state = "Karnataka",
            country = "India",
            latitude = 12.9716,
            longitude = 77.5946,
            timezoneId = "Asia/Kolkata"
        )
        val record = PersonDeathRecord(
            name = "Shakuntala",
            relationship = FamilyRelationship.MOTHER,
            deathDate = deathDate,
            deathTime = deathTime,
            location = location,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)

        println("=== SHAKUNTALA DEMISE PANCHANGA ===")
        println("Death Date: ${result.personRecord.deathDate}")
        println("Samvatsara: ${result.mrutaTithi.samvatsara}")
        println("Masa: ${result.mrutaTithi.masa} (Adhika: ${result.mrutaTithi.isAdhikaMasa})")
        println("Paksha: ${result.mrutaTithi.tithi.paksha}")
        println("Tithi: ${result.mrutaTithi.tithi.name} (Tithi #${result.mrutaTithi.tithi.number})")

        println("\n=== DETAILED ASTRONOMICAL TRACE FOR SEPTEMBER 2026 (YEAR 6) ===")
        val zoneId = ZoneId.of("Asia/Kolkata")
        val scanStart = LocalDate.of(2026, 9, 6)
        val scanEnd = LocalDate.of(2026, 9, 11)
        var d = scanStart
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

            // Sample every minute from sunrise to sunset to find exact transition times
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

            var dvadashiSec = 0L
            var trayodashiSec = 0L
            sample = apStartZdt
            while (sample.isBefore(apEndZdt)) {
                val t = TithiCalculator.getTithiAt(sample)
                if (t.number == 27) dvadashiSec += 60
                if (t.number == 28) trayodashiSec += 60
                sample = sample.plusSeconds(60)
            }
            println("  Aparahna Dvadashi (Tithi 27) overlap: ${dvadashiSec / 60} mins")
            println("  Aparahna Trayodashi (Tithi 28) overlap: ${trayodashiSec / 60} mins")

            // Daytime overlap
            sample = sunriseZdt
            var dayDvadashiSec = 0L
            var dayTrayodashiSec = 0L
            while (sample.isBefore(sunsetZdt)) {
                val t = TithiCalculator.getTithiAt(sample)
                if (t.number == 27) dayDvadashiSec += 60
                if (t.number == 28) dayTrayodashiSec += 60
                sample = sample.plusSeconds(60)
            }
            println("  Daytime Dvadashi (#27) overlap: ${dayDvadashiSec / 60} mins")
            println("  Daytime Trayodashi (#28) overlap: ${dayTrayodashiSec / 60} mins")

            d = d.plusDays(1)
        }
    }

    @Test
    fun testPraneshKulkarniMasikasDetailedTrace() {
        val deathDate = LocalDate.of(2026, 7, 3)
        val deathTime = LocalTime.of(5, 0)
        val bengaluru = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")

        val record = PersonDeathRecord(
            name = "Pranesh Kulakarni",
            relationship = FamilyRelationship.FATHER,
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)

        println("=================================================================")
        println("=== PRANESH KULAKARNI (05:00 AM) DEMISE PANCHANGA ===")
        println("=================================================================")
        println("Death Date: ${result.personRecord.deathDate} (${result.personRecord.deathDate.dayOfWeek}) at $deathTime")
        println("Samvatsara: ${result.mrutaTithi.samvatsara}")
        println("Masa: ${result.mrutaTithi.masa} (Adhika: ${result.mrutaTithi.isAdhikaMasa})")
        println("Paksha: ${result.mrutaTithi.tithi.paksha}")
        println("Tithi: ${result.mrutaTithi.tithi.name} (Tithi #${result.mrutaTithi.tithi.number})")

        val zoneId = ZoneId.of("Asia/Kolkata")
        val deathZdt = ZonedDateTime.of(deathDate, deathTime, zoneId)
        val exactDeathTithi = TithiCalculator.getTithiAt(deathZdt)
        val kala = DinmanaCalculator.calculateDayKala(deathDate, bengaluru)
        val sunriseZdt = ZonedDateTime.of(deathDate, kala.sunrise, zoneId)
        val sunriseTithi = TithiCalculator.getTithiAt(sunriseZdt)

        println("\nExact Death Moment (05:00 AM): ${exactDeathTithi.name} (${exactDeathTithi.paksha}, #${exactDeathTithi.number})")
        println("Sunrise Moment (${kala.sunrise}): ${sunriseTithi.name} (${sunriseTithi.paksha}, #${sunriseTithi.number})")

        // Also check night of 2nd July / morning 3rd July transitions
        var sample = ZonedDateTime.of(deathDate.minusDays(1), LocalTime.of(18, 0), zoneId)
        val endSample = ZonedDateTime.of(deathDate, LocalTime.of(12, 0), zoneId)
        var prev = -1
        println("\n--- Transitions from 02 July 6 PM to 03 July 12 PM ---")
        while (sample.isBefore(endSample)) {
            val t = TithiCalculator.getTithiAt(sample)
            if (prev != -1 && t.number != prev) {
                println("  Tithi transition #$prev -> #${t.number} (${t.name}, ${t.paksha}) at ${sample.toLocalDate()} ${sample.toLocalTime()}")
            }
            prev = t.number
            sample = sample.plusSeconds(60)
        }

        println("\n=================================================================")
        println("=== CURRENT APP RESULT: YEAR 1 (SHODASHA) EVENTS ===")
        println("=================================================================")
        val y1Events = result.yearlySections.first().events
        y1Events.forEach { ev ->
            val daysFromDeath = java.time.temporal.ChronoUnit.DAYS.between(deathDate, ev.gregorianDate) + 1
            println(String.format(
                "%-32s | %-12s | %-10s | Day %-3d | Masa: %-15s | Paksha: %-7s | Tithi: %-15s (#%02d) | %s",
                ev.traditionalName,
                ev.gregorianDate,
                ev.dayOfWeek,
                daysFromDeath,
                ev.tithi.masaDisplayName,
                ev.tithi.tithi.paksha,
                ev.tithi.tithi.name,
                ev.tithi.tithi.number,
                ev.explanation
            ))
        }

        println("\n=================================================================")
        println("=== DETAILED ASTRONOMICAL AUDIT OF SPECIFIC MILESTONES ===")
        println("=================================================================")

        fun scanDateRange(label: String, start: LocalDate, end: LocalDate, targetTithiNum: Int? = null) {
            println("\n--- $label (Scan: $start to $end) ---")
            var d = start
            while (!d.isAfter(end)) {
                val kala = DinmanaCalculator.calculateDayKala(d, bengaluru)
                val sunriseZdt = ZonedDateTime.of(d, kala.sunrise, zoneId)
                val sunsetZdt = ZonedDateTime.of(d, kala.sunset, zoneId)
                val apStartZdt = ZonedDateTime.of(d, kala.aparahnaStart, zoneId)
                val apEndZdt = ZonedDateTime.of(d, kala.aparahnaEnd, zoneId)

                val sunriseTithi = TithiCalculator.getTithiAt(sunriseZdt)
                val apStartTithi = TithiCalculator.getTithiAt(apStartZdt)
                val apEndTithi = TithiCalculator.getTithiAt(apEndZdt)
                val fullPanchanga = MasaCalculator.getFullPanchangaTithi(apStartZdt)

                val daysFromDeath = java.time.temporal.ChronoUnit.DAYS.between(deathDate, d) + 1

                // Sample every minute for transitions
                val transitions = mutableListOf<String>()
                var sample = sunriseZdt
                var prevTithi = -1
                while (sample.isBefore(sunsetZdt)) {
                    val t = TithiCalculator.getTithiAt(sample)
                    if (prevTithi != -1 && t.number != prevTithi) {
                        transitions.add("#$prevTithi -> #${t.number} (${t.name}) at ${sample.toLocalTime()}")
                    }
                    prevTithi = t.number
                    sample = sample.plusSeconds(60)
                }

                var aparahnaTithiOverlap = ""
                if (targetTithiNum != null) {
                    var overlapSec = 0L
                    sample = apStartZdt
                    while (sample.isBefore(apEndZdt)) {
                        val t = TithiCalculator.getTithiAt(sample)
                        if (t.number == targetTithiNum) overlapSec += 60
                        sample = sample.plusSeconds(60)
                    }
                    aparahnaTithiOverlap = " | Target(#$targetTithiNum) ApOverlap: ${overlapSec / 60}m"
                }

                println(String.format(
                    "Date: %s (%-9s, Day %03d) | Sunrise: %s (%-14s #%02d) | Aparahna [%s - %s]: Start=%-14s (#%02d), End=%-14s (#%02d) | Masa: %-12s%s | Transitions: %s",
                    d,
                    d.dayOfWeek,
                    daysFromDeath,
                    kala.sunrise,
                    "${sunriseTithi.name} (${sunriseTithi.paksha})",
                    sunriseTithi.number,
                    kala.aparahnaStart,
                    kala.aparahnaEnd,
                    "${apStartTithi.name} (${apStartTithi.paksha})",
                    apStartTithi.number,
                    "${apEndTithi.name} (${apEndTithi.paksha})",
                    apEndTithi.number,
                    fullPanchanga.masaDisplayName,
                    aparahnaTithiOverlap,
                    if (transitions.isEmpty()) "None" else transitions.joinToString("; ")
                ))
                d = d.plusDays(1)
            }
        }

        scanDateRange("1. DEMISE & ADYA MASIKA (July 2026)", LocalDate.of(2026, 7, 3), LocalDate.of(2026, 7, 18), 18)
        scanDateRange("2. UNMASIKA & 1ST MONTH (Late July / Early Aug 2026)", LocalDate.of(2026, 7, 26), LocalDate.of(2026, 8, 3), 18)
        scanDateRange("3. TRAIPAKSHIKA (Mid August 2026)", LocalDate.of(2026, 8, 14), LocalDate.of(2026, 8, 20), 18)
        scanDateRange("4. SHASHTHA MASIKA / SHANMASIKA (Late Nov 2026)", LocalDate.of(2026, 11, 24), LocalDate.of(2026, 11, 30), 18)
        scanDateRange("5. UNA-SHANMASIKA (Dec 2026)", LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 18), 18)
        scanDateRange("6. UNABDIKA & PRATHAMA VARSHIKA (June 2027)", LocalDate.of(2027, 6, 15), LocalDate.of(2027, 6, 25), 18)
    }

    @Test
    fun testEkadashiDemise() {
        // Person died on Shukla Ekadashi (e.g. 25 June 2026 at 10:00 AM)
        val deathDate = LocalDate.of(2026, 6, 25)
        val deathTime = LocalTime.of(10, 0)
        val bengaluru = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        val record = PersonDeathRecord(
            name = "Ekadashi Test Person",
            relationship = FamilyRelationship.FATHER,
            deathDate = deathDate,
            deathTime = deathTime,
            location = bengaluru,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)
        println("=== EKADASHI DEMISE CALCULATION RESULT ===")
        println("Death Date: ${result.personRecord.deathDate}")
        println("Mruta Tithi: ${result.mrutaTithi.tithi.name} (${result.mrutaTithi.tithi.paksha}) #${result.mrutaTithi.tithi.number}")
        println("Masa: ${result.mrutaTithi.masaDisplayName}")
        println("\n=== YEAR 1 MASIKAS ===")
        val y1 = result.yearlyObservanceGroups.first()
        y1.masikas.forEachIndexed { i, m ->
            println(String.format("Masika %02d: %-35s | Date: %s | Tithi: %s (%s)", i+1, m.traditionalName, m.gregorianDate, m.tithi.tithi.name, m.tithi.tithi.paksha))
        }
        println("\nVarshika 1: ${y1.varshikaEvent.traditionalName} | Date: ${y1.varshikaEvent.gregorianDate} | Tithi: ${y1.varshikaEvent.tithi.tithi.name} (${y1.varshikaEvent.tithi.tithi.paksha})")
        println("Paksha 1: ${y1.pakshaEvent?.traditionalName} | Date: ${y1.pakshaEvent?.gregorianDate} | Reason: ${y1.pakshaNotApplicableReason}")

        println("\n=== SUBSEQUENT YEARS VARSHIKAS & PAKSHAS ===")
        result.yearlyObservanceGroups.drop(1).forEach { grp ->
            println(String.format("%s: Varshika on %s (Tithi: %s %s) | Paksha on %s (Tithi: %s %s)",
                grp.yearTitle,
                grp.varshikaEvent.gregorianDate,
                grp.varshikaEvent.tithi.tithi.name,
                grp.varshikaEvent.tithi.tithi.paksha,
                grp.pakshaEvent?.gregorianDate ?: "None",
                grp.pakshaEvent?.tithi?.tithi?.name ?: "",
                grp.pakshaEvent?.tithi?.tithi?.paksha ?: ""
            ))
        }
    }
}


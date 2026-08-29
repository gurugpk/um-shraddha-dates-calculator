package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.astro.JulianDay
import com.shraddhacalendar.core.astro.SunCoordinates
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Astronomically locates the Bhadrapada Krishna Paksha (Pitru Paksha / Mahalaya Paksha)
 * window for any given year, instead of using a hardcoded September 20th anchor.
 *
 * In the Amanta (Amavasyanta) lunar calendar:
 * - Bhadrapada month ends at the Amavasyā (New Moon) when the Sun is in
 *   Kanya rāshi (Virgo, Nirayana longitude 150°–180°).
 * - Bhadrapada Krishna Paksha (Pitru Paksha) spans the 15 tithis
 *   from Purnima to Amavasyā of Bhadrapada month.
 *
 * Algorithm:
 * 1. Find all New Moons between August 15 and November 15 of the given year
 * 2. For each New Moon, check if the Sun's Nirayana longitude is in Kanya (150°-180°)
 * 3. The matching New Moon marks the end of Bhadrapada (= Bhadrapada Amavasyā)
 * 4. The Pitru Paksha window starts ~15 days before that Amavasyā
 */
data class BhadrapadaWindow(
    val krishnaPakshaStart: LocalDate,  // Approximate start (day after Purnima)
    val amavasyaDate: LocalDate          // Bhadrapada Amavasyā (end of Pitru Paksha)
)

object BhadrapadaFinder {

    /**
     * Finds the Bhadrapada Krishna Paksha window for the given year.
     * Returns the approximate start and end dates of Pitru Paksha.
     */
    fun findBhadrapadaKrishnaPaksha(year: Int): BhadrapadaWindow {
        val istZone = ZoneId.of("Asia/Kolkata")

        // Scan for New Moons between Aug 15 and Nov 15
        // Bhadrapada Amavasyā typically falls between Sep 1 and Oct 15
        val scanStart = LocalDate.of(year, 8, 15)
        val scanEnd = LocalDate.of(year, 11, 15)
        val scanStartJd = JulianDay.fromZonedDateTime(
            ZonedDateTime.of(scanStart, LocalTime.NOON, istZone)
        )
        val scanEndJd = JulianDay.fromZonedDateTime(
            ZonedDateTime.of(scanEnd, LocalTime.NOON, istZone)
        )

        // Find all New Moons in the scan window
        val newMoons = mutableListOf<Double>()
        var searchJd = scanStartJd
        while (searchJd < scanEndJd) {
            val nmJd = findNextNewMoon(searchJd)
            if (nmJd <= scanEndJd) {
                newMoons.add(nmJd)
            }
            searchJd = nmJd + 25.0 // Skip ahead past this New Moon
        }

        // Find the New Moon where Sun is in Kanya rāshi (150° - 180° Nirayana)
        // This is the Bhadrapada Amavasyā in the Amanta system
        var bhadrapadaAmavasyaJd: Double? = null

        for (nmJd in newMoons) {
            val sunLong = SunCoordinates.getNirayanaLongitude(nmJd)
            val rashi = (Math.floor(sunLong / 30.0).toInt()) % 12
            // Kanya rāshi = index 5 (Mesha=0, Vrishabha=1, Mithuna=2, Karka=3, Simha=4, Kanya=5)
            if (rashi == 5) {
                bhadrapadaAmavasyaJd = nmJd
                break
            }
        }

        // If no exact Kanya match (extremely rare), look for the closest to Kanya
        if (bhadrapadaAmavasyaJd == null) {
            bhadrapadaAmavasyaJd = newMoons.minByOrNull { nmJd ->
                val sunLong = SunCoordinates.getNirayanaLongitude(nmJd)
                // Distance from center of Kanya (165°)
                val diff = Math.abs(sunLong - 165.0)
                if (diff > 180) 360 - diff else diff
            } ?: JulianDay.fromZonedDateTime(
                ZonedDateTime.of(LocalDate.of(year, 9, 20), LocalTime.NOON, istZone)
            )
        }

        val amavasyaDate = JulianDay.toLocalDateTime(bhadrapadaAmavasyaJd).toLocalDate()
        // Krishna Paksha starts ~15 days before Amavasyā (day after Purnima)
        val krishnaPakshaStart = amavasyaDate.minusDays(16)

        return BhadrapadaWindow(
            krishnaPakshaStart = krishnaPakshaStart,
            amavasyaDate = amavasyaDate
        )
    }

    /**
     * Calculates the Mahalaya Paksha Shraddha event for a given year.
     * Finds the death tithi occurrence within the astronomically-determined
     * Bhadrapada Krishna Paksha window, with masa validation.
     */
    fun calculateMahalayaPakshaEvent(
        year: Int,
        mrutaTithi: PanchangaTithi,
        location: GeoLocation,
        zoneId: ZoneId
    ): ShraddhaEvent? {
        val bhadrapadaWindow = findBhadrapadaKrishnaPaksha(year)

        // Convert death tithi to Krishna Paksha tithi number (16..30)
        val pakshaTithiNum = if (mrutaTithi.tithi.paksha == Paksha.KRISHNA) {
            mrutaTithi.tithi.number  // Already 16..30
        } else {
            15 + mrutaTithi.tithi.pakshaTithiNumber  // Map Shukla 1..15 -> Krishna 16..30
        }

        // Search centered on the midpoint of the Bhadrapada Krishna Paksha window
        val windowMidpoint = bhadrapadaWindow.krishnaPakshaStart.plusDays(8)

        val selectedPaksha = AparahnaVyaptiEngine.findShraddhaDate(
            targetTithiNumber = pakshaTithiNum,
            approximateDate = windowMidpoint,
            location = location,
            searchWindowDays = 12  // ±12 days covers the full Krishna Paksha window
        )

        val pZdt = ZonedDateTime.of(selectedPaksha.date, selectedPaksha.kalaDetails.aparahnaStart, zoneId)
        val pPanchanga = MasaCalculator.getFullPanchangaTithi(pZdt)

        // Validate: the result should be in Bhadrapada or close to the Amavasyā date
        // Accept if it's within the Bhadrapada window (±2 days tolerance for edge cases)
        val isInWindow = !selectedPaksha.date.isBefore(bhadrapadaWindow.krishnaPakshaStart.minusDays(2)) &&
                !selectedPaksha.date.isAfter(bhadrapadaWindow.amavasyaDate.plusDays(2))

        if (!isInWindow) {
            // Retry with a tighter search around the Amavasyā
            val retryResult = AparahnaVyaptiEngine.findShraddhaDate(
                targetTithiNumber = pakshaTithiNum,
                approximateDate = bhadrapadaWindow.amavasyaDate.minusDays(7),
                location = location,
                searchWindowDays = 10
            )
            val retryZdt = ZonedDateTime.of(retryResult.date, retryResult.kalaDetails.aparahnaStart, zoneId)
            val retryPanchanga = MasaCalculator.getFullPanchangaTithi(retryZdt)
            val retrySunriseZdt = ZonedDateTime.of(retryResult.date, retryResult.kalaDetails.sunrise, zoneId)
            val retrySunrisePanchanga = MasaCalculator.getFullPanchangaTithi(retrySunriseZdt)

            val isRetryEkadashi = retryPanchanga.tithi.pakshaTithiNumber == 11
            val retryRitualDate = if (isRetryEkadashi) retryResult.date.plusDays(1) else retryResult.date
            val retryRitualKala = if (isRetryEkadashi) DinmanaCalculator.calculateDayKala(retryRitualDate, location) else retryResult.kalaDetails
            val retryRitualSunriseZdt = ZonedDateTime.of(retryRitualDate, retryRitualKala.sunrise, zoneId)
            val retryRitualSunrisePanchanga = if (isRetryEkadashi) MasaCalculator.getFullPanchangaTithi(retryRitualSunriseZdt) else retrySunrisePanchanga
            val retryRitualAparahnaZdt = ZonedDateTime.of(retryRitualDate, retryRitualKala.aparahnaStart, zoneId)
            val retryRitualAparahnaPanchanga = if (isRetryEkadashi) MasaCalculator.getFullPanchangaTithi(retryRitualAparahnaZdt) else retryPanchanga

            return ShraddhaEvent(
                sequenceNumber = 2,
                type = ShraddhaType.MAHALAYA_PAKSHA,
                traditionalName = "Mahalaya Paksha Shraddha (Pitru Paksha)",
                gregorianDate = retryRitualDate,
                dayOfWeek = retryRitualDate.dayOfWeek.name,
                tithi = retryRitualAparahnaPanchanga,
                kalaDetails = retryRitualKala,
                explanation = if (isRetryEkadashi) {
                    "Ekadashi demise tithi detected on ${retryResult.date}. Per Shastras (Padma Purana / Nirnaya Sindhu), Anna-Shraddha ritual is observed on Dvadashi ($retryRitualDate). ${retryResult.evaluationReason}"
                } else {
                    "Observed during Bhadrapada Krishna Paksha (Mahalaya Pitru Paksha) on ${retryPanchanga.tithi.name}. ${retryResult.evaluationReason}"
                },
                observanceCategory = ObservanceCategory.MAHALAYA_PAKSHA,
                sunrisePanchanga = retryRitualSunrisePanchanga,
                isEkadashiShifted = isRetryEkadashi,
                ekadashiDate = if (isRetryEkadashi) retryResult.date else null
            )
        }

        val sunriseZdt = ZonedDateTime.of(selectedPaksha.date, selectedPaksha.kalaDetails.sunrise, zoneId)
        val sunrisePanchanga = MasaCalculator.getFullPanchangaTithi(sunriseZdt)

        val isEkadashi = pPanchanga.tithi.pakshaTithiNumber == 11
        val ritualDate = if (isEkadashi) selectedPaksha.date.plusDays(1) else selectedPaksha.date
        val ritualKala = if (isEkadashi) DinmanaCalculator.calculateDayKala(ritualDate, location) else selectedPaksha.kalaDetails
        val ritualSunriseZdt = ZonedDateTime.of(ritualDate, ritualKala.sunrise, zoneId)
        val ritualSunrisePanchanga = if (isEkadashi) MasaCalculator.getFullPanchangaTithi(ritualSunriseZdt) else sunrisePanchanga
        val ritualAparahnaZdt = ZonedDateTime.of(ritualDate, ritualKala.aparahnaStart, zoneId)
        val ritualAparahnaPanchanga = if (isEkadashi) MasaCalculator.getFullPanchangaTithi(ritualAparahnaZdt) else pPanchanga

        return ShraddhaEvent(
            sequenceNumber = 2,
            type = ShraddhaType.MAHALAYA_PAKSHA,
            traditionalName = "Mahalaya Paksha Shraddha (Pitru Paksha)",
            gregorianDate = ritualDate,
            dayOfWeek = ritualDate.dayOfWeek.name,
            tithi = ritualAparahnaPanchanga,
            kalaDetails = ritualKala,
            explanation = if (isEkadashi) {
                "Ekadashi demise tithi detected on ${selectedPaksha.date}. Per Shastras (Padma Purana / Nirnaya Sindhu), Anna-Shraddha ritual is observed on Dvadashi ($ritualDate). ${selectedPaksha.evaluationReason}"
            } else {
                "Observed during Bhadrapada Krishna Paksha (Mahalaya Pitru Paksha) on ${pPanchanga.tithi.name}. ${selectedPaksha.evaluationReason}"
            },
            observanceCategory = ObservanceCategory.MAHALAYA_PAKSHA,
            sunrisePanchanga = ritualSunrisePanchanga,
            isEkadashiShifted = isEkadashi,
            ekadashiDate = if (isEkadashi) selectedPaksha.date else null
        )
    }

    /**
     * Finds the next New Moon after [startJd] using elongation tracking.
     */
    private fun findNextNewMoon(startJd: Double): Double {
        // Scan in 1-day steps to find where elongation crosses 360° → 0°
        var step = startJd
        while (step < startJd + 32.0) {
            val el1 = TithiCalculator.getLunarElongation(step)
            val el2 = TithiCalculator.getLunarElongation(step + 1.0)
            if (el1 > 300.0 && el2 < 60.0) {
                // Found the bracket — refine with binary search
                return refineNewMoon(step, step + 1.0)
            }
            step += 1.0
        }
        return startJd + 29.5 // Fallback: approximate synodic period
    }

    /**
     * Binary search to find exact New Moon JD within [low, high].
     */
    private fun refineNewMoon(low: Double, high: Double): Double {
        var lo = low
        var hi = high
        for (i in 0 until 30) {
            val mid = (lo + hi) / 2.0
            val angle = TithiCalculator.getLunarElongation(mid)
            if (angle > 180.0) {
                lo = mid  // Approaching 360°/0°
            } else {
                hi = mid  // Past 0°
            }
        }
        return (lo + hi) / 2.0
    }
}

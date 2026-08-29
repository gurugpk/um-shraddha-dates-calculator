package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Shared utility for finding Varshika (annual) Shraddha dates using chain-forward
 * lunar year tracking. Instead of anchoring to a fixed Gregorian date (which drifts
 * ~11 days/year relative to the lunar calendar), this advances from the previous
 * year's actual Varshika date by ~354 days (one lunar year).
 *
 * Algorithm:
 *  1. Start from prevVarshikaDate + 354 days as approximate search center
 *  2. Use AparahnaVyaptiEngine to find the best tithi match
 *  3. Validate the result is in the correct Nija masa (not Adhika)
 *  4. If validation fails, try offsets (+29, -29, +58, -58 days) to find the
 *     correct lunar month occurrence
 *  5. Final fallback: widen search window to ±30 days
 */
data class VarshikaSearchResult(
    val selectedDay: SelectedShraddhaDay,
    val panchanga: PanchangaTithi, // Ritual / Aparahna Panchanga
    val sunrisePanchanga: PanchangaTithi = panchanga // Sunrise Panchanga
)

object VarshikaDateFinder {

    /**
     * Finds the Varshika Shraddha date for a given year, using the previous year's
     * Varshika date as the anchor point.
     *
     * Scans a ±50-day window around the estimated lunar year date (prevVarshikaDate + 354 days)
     * to find all candidate days where the target tithi occurs during daylight, verifies they
     * belong to the target Nija Masa (not Adhika), and applies Aparahna Vyapti rules to select
     * the optimal day.
     *
     * @param prevVarshikaDate The Gregorian date of the previous year's Varshika Shraddha
     * @param targetTithiNumber The tithi number (1..30) of the death tithi
     * @param targetMasa The lunar month in which Varshika must fall (Nija masa of death)
     * @param location The geographic location for Aparahna calculations
     * @return VarshikaSearchResult with the found date and its Panchanga details
     */
    fun findVarshikaDate(
        prevVarshikaDate: LocalDate,
        targetTithiNumber: Int,
        targetMasa: LunarMonth,
        location: GeoLocation
    ): VarshikaSearchResult {
        val zoneId = ZoneId.of(location.timezoneId)

        // Estimated anchor: 354 days (1 lunar year) from previous Varshika
        val approxDate = prevVarshikaDate.plusDays(354)

        // Scan ±50 days (101 days total) to comfortably cover both Adhika masa jumps (+30d)
        // and lunar year drift (-11d)
        val startDate = approxDate.minusDays(50)
        val endDate = approxDate.plusDays(50)

        data class CandidateDay(
            val evaluation: AparahnaEvaluation,
            val panchanga: PanchangaTithi,
            val sunrisePanchanga: PanchangaTithi
        )

        val matchingCandidates = mutableListOf<CandidateDay>()

        var curr = startDate
        while (!curr.isAfter(endDate)) {
            val kala = com.shraddhacalendar.core.panchang.DinmanaCalculator.calculateDayKala(curr, location)
            val aparahnaStartZdt = ZonedDateTime.of(curr, kala.aparahnaStart, zoneId)
            val aparahnaEndZdt = ZonedDateTime.of(curr, kala.aparahnaEnd, zoneId)
            val sunriseZdt = ZonedDateTime.of(curr, kala.sunrise, zoneId)
            val sunsetZdt = ZonedDateTime.of(curr, kala.sunset, zoneId)

            // Check Aparahna overlap
            var aparahnaOverlapSec = 0.0
            var sample = aparahnaStartZdt
            val stepSeconds = 300L // 5 mins
            while (sample.isBefore(aparahnaEndZdt)) {
                val tithi = com.shraddhacalendar.core.panchang.TithiCalculator.getTithiAt(sample)
                if (tithi.number == targetTithiNumber) {
                    aparahnaOverlapSec += stepSeconds
                }
                sample = sample.plusSeconds(stepSeconds)
            }

            // Check Daylight overlap
            var daytimeOverlapSec = 0.0
            sample = sunriseZdt
            val daytimeStepSeconds = 600L // 10 mins
            while (sample.isBefore(sunsetZdt)) {
                val tithi = com.shraddhacalendar.core.panchang.TithiCalculator.getTithiAt(sample)
                if (tithi.number == targetTithiNumber) {
                    daytimeOverlapSec += daytimeStepSeconds
                }
                sample = sample.plusSeconds(daytimeStepSeconds)
            }

            val aparahnaMinutes = aparahnaOverlapSec / 60.0
            val daytimeMinutes = daytimeOverlapSec / 60.0

            if (aparahnaMinutes > 0 || daytimeMinutes > 0) {
                // Find a moment during Aparahna (or daytime) where target tithi is active
                var targetSampleZdt = aparahnaStartZdt
                var s = aparahnaStartZdt
                while (s.isBefore(aparahnaEndZdt)) {
                    val t = com.shraddhacalendar.core.panchang.TithiCalculator.getTithiAt(s)
                    if (t.number == targetTithiNumber) {
                        targetSampleZdt = s
                        break
                    }
                    s = s.plusSeconds(stepSeconds)
                }
                if (targetSampleZdt == aparahnaStartZdt && com.shraddhacalendar.core.panchang.TithiCalculator.getTithiAt(targetSampleZdt).number != targetTithiNumber) {
                    s = sunriseZdt
                    while (s.isBefore(sunsetZdt)) {
                        val t = com.shraddhacalendar.core.panchang.TithiCalculator.getTithiAt(s)
                        if (t.number == targetTithiNumber) {
                            targetSampleZdt = s
                            break
                        }
                        s = s.plusSeconds(daytimeStepSeconds)
                    }
                }

                val panchanga = MasaCalculator.getFullPanchangaTithi(targetSampleZdt)
                val sunrisePanchanga = MasaCalculator.getFullPanchangaTithi(sunriseZdt)

                if (panchanga.masa == targetMasa && !panchanga.isAdhikaMasa) {
                    val eval = AparahnaEvaluation(
                        date = curr,
                        kalaDetails = kala,
                        aparahnaOverlapMinutes = aparahnaMinutes,
                        touchesAparahna = aparahnaMinutes > 0,
                        touchesKutapa = false,
                        totalDaytimeOverlapMinutes = daytimeMinutes
                    )
                    matchingCandidates.add(CandidateDay(eval, panchanga, sunrisePanchanga))
                }
            }

            curr = curr.plusDays(1)
        }

        if (matchingCandidates.isNotEmpty()) {
            val aparahnaDays = matchingCandidates.filter { it.evaluation.aparahnaOverlapMinutes > 0 }

            val bestCandidate = when {
                // Case 1: Exactly 1 day with Aparahna overlap
                aparahnaDays.size == 1 -> aparahnaDays.first()

                // Case 2: Multiple days with Aparahna overlap (Ubhaya Vyapti) -> pick max overlap
                aparahnaDays.size > 1 -> aparahnaDays.maxByOrNull { it.evaluation.aparahnaOverlapMinutes } ?: aparahnaDays.first()

                // Case 3: No Aparahna overlap -> pick max daytime overlap
                else -> matchingCandidates.maxByOrNull { it.evaluation.totalDaytimeOverlapMinutes } ?: matchingCandidates.first()
            }

            val reason = when {
                aparahnaDays.size == 1 -> "Eka Aparahna Vyapti: Tithi prevailed during Aparahna Kala (${bestCandidate.evaluation.aparahnaOverlapMinutes.toInt()} mins) solely on ${bestCandidate.evaluation.date}"
                aparahnaDays.size > 1 -> "Ubhaya Vyapti: Selected ${bestCandidate.evaluation.date} due to maximum Aparahna overlap (${bestCandidate.evaluation.aparahnaOverlapMinutes.toInt()} mins)"
                else -> "Kshaya/Asparsha Vyapti: Selected ${bestCandidate.evaluation.date} based on maximum daytime prevalence (${bestCandidate.evaluation.totalDaytimeOverlapMinutes.toInt()} mins)"
            }

            val selectedDay = SelectedShraddhaDay(
                date = bestCandidate.evaluation.date,
                kalaDetails = bestCandidate.evaluation.kalaDetails,
                evaluationReason = reason,
                evaluations = matchingCandidates.map { it.evaluation }
            )

            return VarshikaSearchResult(selectedDay, bestCandidate.panchanga, bestCandidate.sunrisePanchanga)
        }

        // Fallback to standard AparahnaVyaptiEngine
        val fallback = AparahnaVyaptiEngine.findShraddhaDate(
            targetTithiNumber = targetTithiNumber,
            approximateDate = approxDate,
            location = location,
            searchWindowDays = 25
        )
        val zdt = ZonedDateTime.of(fallback.date, fallback.kalaDetails.aparahnaStart, zoneId)
        val p = MasaCalculator.getFullPanchangaTithi(zdt)
        val sunriseZdt = ZonedDateTime.of(fallback.date, fallback.kalaDetails.sunrise, zoneId)
        val sunriseP = MasaCalculator.getFullPanchangaTithi(sunriseZdt)
        return VarshikaSearchResult(fallback, p, sunriseP)
    }
}

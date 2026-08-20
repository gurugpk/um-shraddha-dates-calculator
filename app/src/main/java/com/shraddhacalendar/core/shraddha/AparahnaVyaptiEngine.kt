package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.DayKalaDetails
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import java.time.Duration
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

data class AparahnaEvaluation(
    val date: LocalDate,
    val kalaDetails: DayKalaDetails,
    val aparahnaOverlapMinutes: Double,
    val touchesAparahna: Boolean,
    val touchesKutapa: Boolean,
    val totalDaytimeOverlapMinutes: Double
)

data class SelectedShraddhaDay(
    val date: LocalDate,
    val kalaDetails: DayKalaDetails,
    val evaluationReason: String,
    val evaluations: List<AparahnaEvaluation>
)

/**
 * Evaluates Aparahna Vyapti (afternoon prevalence) to determine the exact Shraddha date.
 * Strictly adheres to Dharma Shastra & Smriti Muktavali principles.
 */
object AparahnaVyaptiEngine {

    /**
     * Determines which candidate day is selected for the given target [tithiNumber] (1..30)
     * around [approximateDate] at [location].
     */
    fun findShraddhaDate(
        targetTithiNumber: Int,
        approximateDate: LocalDate,
        location: GeoLocation,
        searchWindowDays: Int = 3
    ): SelectedShraddhaDay {
        val zoneId = ZoneId.of(location.timezoneId)
        val startDate = approximateDate.minusDays(searchWindowDays.toLong())
        val endDate = approximateDate.plusDays(searchWindowDays.toLong())

        val evaluations = mutableListOf<AparahnaEvaluation>()

        var curr = startDate
        while (!curr.isAfter(endDate)) {
            val eval = evaluateDay(curr, targetTithiNumber, location, zoneId)
            evaluations.add(eval)
            curr = curr.plusDays(1)
        }

        // Filter days that have some relevance to the target tithi
        val relevantDays = evaluations.filter { it.aparahnaOverlapMinutes > 0 || it.totalDaytimeOverlapMinutes > 0 }

        if (relevantDays.isEmpty()) {
            // Fallback: day with closest tithi proximity
            val fallback = evaluations.maxByOrNull { it.totalDaytimeOverlapMinutes } ?: evaluations[evaluations.size / 2]
            return SelectedShraddhaDay(
                date = fallback.date,
                kalaDetails = fallback.kalaDetails,
                evaluationReason = "Selected based on nearest lunar tithi occurrence (No direct daytime overlap)",
                evaluations = evaluations
            )
        }

        val aparahnaDays = relevantDays.filter { it.aparahnaOverlapMinutes > 0 }

        return when {
            // Case 1: Exactly one day touches Aparahna Kala
            aparahnaDays.size == 1 -> {
                val day = aparahnaDays.first()
                SelectedShraddhaDay(
                    date = day.date,
                    kalaDetails = day.kalaDetails,
                    evaluationReason = "Eka Aparahna Vyapti: Tithi prevailed during Aparahna Kala (${day.aparahnaOverlapMinutes.toInt()} mins) solely on ${day.date}",
                    evaluations = evaluations
                )
            }

            // Case 2: Multiple days touch Aparahna Kala (Ubhaya Vyapti)
            aparahnaDays.size > 1 -> {
                val bestDay = aparahnaDays.maxByOrNull { it.aparahnaOverlapMinutes } ?: aparahnaDays.first()
                val otherDays = aparahnaDays.filter { it != bestDay }
                val otherDesc = otherDays.joinToString(", ") { "${it.date} (${it.aparahnaOverlapMinutes.toInt()}m)" }
                SelectedShraddhaDay(
                    date = bestDay.date,
                    kalaDetails = bestDay.kalaDetails,
                    evaluationReason = "Ubhaya Vyapti: Tithi spanned across multiple afternoons. Selected ${bestDay.date} due to maximum Aparahna overlap (${bestDay.aparahnaOverlapMinutes.toInt()}m vs $otherDesc)",
                    evaluations = evaluations
                )
            }

            // Case 3: Zero days touch Aparahna (Kshaya / Asparsha) -> Fallback to Kutapa Muhurta or Max Daytime
            else -> {
                val kutapaDay = relevantDays.firstOrNull { it.touchesKutapa }
                    ?: relevantDays.maxByOrNull { it.totalDaytimeOverlapMinutes }
                    ?: relevantDays.first()

                SelectedShraddhaDay(
                    date = kutapaDay.date,
                    kalaDetails = kutapaDay.kalaDetails,
                    evaluationReason = "Kshaya/Asparsha Vyapti: Tithi did not span full Aparahna. Selected ${kutapaDay.date} based on Kutapa Muhurta / maximum daytime prevalence (${kutapaDay.totalDaytimeOverlapMinutes.toInt()}m)",
                    evaluations = evaluations
                )
            }
        }
    }

    private fun evaluateDay(
        date: LocalDate,
        targetTithiNumber: Int,
        location: GeoLocation,
        zoneId: ZoneId
    ): AparahnaEvaluation {
        val kala = DinmanaCalculator.calculateDayKala(date, location)

        val aparahnaStartZdt = ZonedDateTime.of(date, kala.aparahnaStart, zoneId)
        val aparahnaEndZdt = ZonedDateTime.of(date, kala.aparahnaEnd, zoneId)
        val sunriseZdt = ZonedDateTime.of(date, kala.sunrise, zoneId)
        val sunsetZdt = ZonedDateTime.of(date, kala.sunset, zoneId)
        val kutapaStartZdt = ZonedDateTime.of(date, kala.kutapaStart, zoneId)
        val kutapaEndZdt = ZonedDateTime.of(date, kala.kutapaEnd, zoneId)

        // Sample every 5 minutes during Aparahna
        var aparahnaOverlapSec = 0.0
        var sample = aparahnaStartZdt
        val stepSeconds = 300L // 5 mins
        while (sample.isBefore(aparahnaEndZdt)) {
            val tithi = TithiCalculator.getTithiAt(sample)
            if (tithi.number == targetTithiNumber) {
                aparahnaOverlapSec += stepSeconds
            }
            sample = sample.plusSeconds(stepSeconds)
        }

        // Sample every 15 minutes during entire daylight (sunrise to sunset)
        var daytimeOverlapSec = 0.0
        sample = sunriseZdt
        val daytimeStepSeconds = 600L // 10 mins
        while (sample.isBefore(sunsetZdt)) {
            val tithi = TithiCalculator.getTithiAt(sample)
            if (tithi.number == targetTithiNumber) {
                daytimeOverlapSec += daytimeStepSeconds
            }
            sample = sample.plusSeconds(daytimeStepSeconds)
        }

        // Check Kutapa
        val kutapaMid = kutapaStartZdt.plusSeconds(Duration.between(kutapaStartZdt, kutapaEndZdt).seconds / 2)
        val touchesKutapa = TithiCalculator.getTithiAt(kutapaMid).number == targetTithiNumber

        val aparahnaMinutes = aparahnaOverlapSec / 60.0
        val daytimeMinutes = daytimeOverlapSec / 60.0

        return AparahnaEvaluation(
            date = date,
            kalaDetails = kala,
            aparahnaOverlapMinutes = aparahnaMinutes,
            touchesAparahna = aparahnaMinutes > 0,
            touchesKutapa = touchesKutapa,
            totalDaytimeOverlapMinutes = daytimeMinutes
        )
    }
}

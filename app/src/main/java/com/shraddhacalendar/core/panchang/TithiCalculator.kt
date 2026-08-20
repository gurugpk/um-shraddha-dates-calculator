package com.shraddhacalendar.core.panchang

import com.shraddhacalendar.core.astro.JulianDay
import com.shraddhacalendar.core.astro.MoonCoordinates
import com.shraddhacalendar.core.astro.SunCoordinates
import com.shraddhacalendar.core.models.TithiInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

data class TithiSpan(
    val tithi: TithiInfo,
    val startTime: ZonedDateTime,
    val endTime: ZonedDateTime
)

/**
 * Calculates the Vedic Tithi (Lunar Phase) at any exact instant and determines start/end intervals.
 */
object TithiCalculator {

    /**
     * Calculates the continuous lunar phase angle: Delta = (Moon_Long - Sun_Long) in [0, 360).
     */
    fun getLunarElongation(jd: Double): Double {
        val moonLong = MoonCoordinates.getTropicalLongitude(jd)
        val sunLong = SunCoordinates.getTropicalLongitude(jd)
        var diff = (moonLong - sunLong) % 360.0
        if (diff < 0.0) diff += 360.0
        return diff
    }

    /**
     * Returns the [TithiInfo] active at the exact given [zonedDateTime].
     */
    fun getTithiAt(zonedDateTime: ZonedDateTime): TithiInfo {
        val jd = JulianDay.fromZonedDateTime(zonedDateTime)
        val elongation = getLunarElongation(jd)
        val tithiNumber = (Math.floor(elongation / 12.0).toInt() % 30) + 1
        return TithiInfo.fromNumber(tithiNumber)
    }

    /**
     * Convenience method to get Tithi at [date] and [time] in [zoneId].
     */
    fun getTithiAt(date: LocalDate, time: LocalTime, zoneId: ZoneId): TithiInfo {
        val zdt = ZonedDateTime.of(date, time, zoneId)
        return getTithiAt(zdt)
    }

    /**
     * Finds the start and end [ZonedDateTime] for the tithi running at [zonedDateTime].
     */
    fun getTithiSpan(zonedDateTime: ZonedDateTime): TithiSpan {
        val currentTithi = getTithiAt(zonedDateTime)
        val targetStartAngle = (currentTithi.number - 1) * 12.0
        val targetEndAngle = (currentTithi.number % 30) * 12.0

        val currentJd = JulianDay.fromZonedDateTime(zonedDateTime)
        val startJd = findTithiTransitionJd(currentJd - 1.5, currentJd, targetStartAngle)
        val endJd = findTithiTransitionJd(currentJd, currentJd + 1.5, targetEndAngle)

        val zoneId = zonedDateTime.zone
        val startUtc = JulianDay.toLocalDateTime(startJd).atZone(ZoneOffset.UTC)
        val endUtc = JulianDay.toLocalDateTime(endJd).atZone(ZoneOffset.UTC)

        return TithiSpan(
            tithi = currentTithi,
            startTime = startUtc.withZoneSameInstant(zoneId),
            endTime = endUtc.withZoneSameInstant(zoneId)
        )
    }

    /**
     * Binary search / root finder to locate the exact Julian Day when elongation crosses [targetAngleDeg].
     */
    private fun findTithiTransitionJd(
        jdMin: Double,
        jdMax: Double,
        targetAngleDeg: Double
    ): Double {
        var low = jdMin
        var high = jdMax

        // 25 iterations gives sub-second precision (~0.05 seconds)
        for (i in 0 until 25) {
            val mid = (low + high) / 2.0
            val angle = getLunarElongation(mid)

            // Adjust for angular wrap around 360/0 degrees
            val diff = normalizeAngleDiff(angle - targetAngleDeg)
            if (diff >= 0) {
                high = mid
            } else {
                low = mid
            }
        }
        return (low + high) / 2.0
    }

    private fun normalizeAngleDiff(diff: Double): Double {
        var d = diff % 360.0
        if (d > 180.0) d -= 360.0
        if (d < -180.0) d += 360.0
        return d
    }
}

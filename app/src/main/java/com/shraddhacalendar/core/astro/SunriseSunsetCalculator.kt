package com.shraddhacalendar.core.astro

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.*

data class SolarTimes(
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val solarNoon: LocalTime,
    val dayLengthMinutes: Long
)

/**
 * High-precision Sunrise, Sunset, and Solar Noon calculator based on NOAA / Meeus algorithms.
 */
object SunriseSunsetCalculator {

    private const val ZENITH_SUNRISE_SUNSET = 90.8333 // 90° 50' accounting for refraction and solar disc

    /**
     * Calculates Sunrise, Sunset, and Solar Noon for a given [date], [latitude], [longitude], and [zoneId].
     */
    fun calculate(
        date: LocalDate,
        latitude: Double,
        longitude: Double,
        zoneId: ZoneId
    ): SolarTimes {
        val jd = JulianDay.fromLocalDate(date)

        // Timezone offset in hours
        val testZdt = date.atStartOfDay(zoneId)
        val tzOffsetHours = testZdt.offset.totalSeconds / 3600.0

        // Approximate calculation for noon to get accurate declination and equation of time
        val approxNoonJd = jd + 0.5 - (longitude / 360.0)
        val eqTime = SunCoordinates.getEquationOfTime(approxNoonJd)
        val solarDec = SunCoordinates.getDeclination(approxNoonJd)

        // Solar Noon in minutes from midnight UTC
        val solarNoonMinutesUtc = 720.0 - (4.0 * longitude) - eqTime
        val solarNoonMinutesLocal = solarNoonMinutesUtc + (tzOffsetHours * 60.0)

        // Hour angle calculation for sunrise/sunset
        val latRad = Math.toRadians(latitude)
        val zenithRad = Math.toRadians(ZENITH_SUNRISE_SUNSET)

        val cosHourAngle = (cos(zenithRad) - sin(latRad) * sin(solarDec)) / (cos(latRad) * cos(solarDec))

        val (sunriseMinutesLocal, sunsetMinutesLocal) = if (cosHourAngle > 1.0) {
            // Polar night (sun never rises)
            Pair(solarNoonMinutesLocal, solarNoonMinutesLocal)
        } else if (cosHourAngle < -1.0) {
            // Midnight sun (sun never sets)
            Pair(0.0, 1440.0)
        } else {
            val hourAngleDeg = Math.toDegrees(acos(cosHourAngle))
            val deltaMinutes = hourAngleDeg * 4.0 // 4 minutes per degree
            Pair(solarNoonMinutesLocal - deltaMinutes, solarNoonMinutesLocal + deltaMinutes)
        }

        val sunrise = minutesToLocalTime(sunriseMinutesLocal)
        val sunset = minutesToLocalTime(sunsetMinutesLocal)
        val solarNoon = minutesToLocalTime(solarNoonMinutesLocal)

        val dayLength = (sunsetMinutesLocal - sunriseMinutesLocal).coerceAtLeast(0.0).toLong()

        return SolarTimes(
            sunrise = sunrise,
            sunset = sunset,
            solarNoon = solarNoon,
            dayLengthMinutes = dayLength
        )
    }

    private fun minutesToLocalTime(totalMinutes: Double): LocalTime {
        var mins = totalMinutes % 1440.0
        if (mins < 0) mins += 1440.0

        val hours = (mins / 60).toInt().coerceIn(0, 23)
        val minutes = (mins % 60).toInt().coerceIn(0, 59)
        val seconds = ((mins * 60) % 60).toInt().coerceIn(0, 59)

        return LocalTime.of(hours, minutes, seconds)
    }
}

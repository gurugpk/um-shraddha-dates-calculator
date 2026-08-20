package com.shraddhacalendar.core.panchang

import com.shraddhacalendar.core.astro.SunriseSunsetCalculator
import com.shraddhacalendar.core.models.DayKalaDetails
import com.shraddhacalendar.core.models.GeoLocation
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

/**
 * Calculates Dinmana (Day duration from sunrise to sunset) and the 5 Vedic Kalas,
 * including Aparahna Kala (4th part) and Kutapa Muhurta (8th of 15 Muhurtas).
 */
object DinmanaCalculator {

    fun calculateDayKala(date: LocalDate, location: GeoLocation): DayKalaDetails {
        val zoneId = ZoneId.of(location.timezoneId)
        val solarTimes = SunriseSunsetCalculator.calculate(
            date = date,
            latitude = location.latitude,
            longitude = location.longitude,
            zoneId = zoneId
        )

        val sunrise = solarTimes.sunrise
        val sunset = solarTimes.sunset

        val sunriseSeconds = sunrise.toSecondOfDay().toDouble()
        val sunsetSeconds = sunset.toSecondOfDay().toDouble()
        val dinmanaSeconds = (sunsetSeconds - sunriseSeconds).coerceAtLeast(0.0)

        val kalaDuration = dinmanaSeconds / 5.0
        val muhurtaDuration = dinmanaSeconds / 15.0

        // Aparahna is the 4th of 5 Kalas: [3/5, 4/5]
        val aparahnaStartSec = sunriseSeconds + (3.0 * kalaDuration)
        val aparahnaEndSec = sunriseSeconds + (4.0 * kalaDuration)

        // Kutapa Muhurta is the 8th of 15 Muhurtas: [7/15, 8/15]
        val kutapaStartSec = sunriseSeconds + (7.0 * muhurtaDuration)
        val kutapaEndSec = sunriseSeconds + (8.0 * muhurtaDuration)

        val dinmanaMinutes = (dinmanaSeconds / 60.0).toLong()

        return DayKalaDetails(
            date = date,
            sunrise = sunrise,
            sunset = sunset,
            dinmanaMinutes = dinmanaMinutes,
            aparahnaStart = secondsToLocalTime(aparahnaStartSec),
            aparahnaEnd = secondsToLocalTime(aparahnaEndSec),
            kutapaStart = secondsToLocalTime(kutapaStartSec),
            kutapaEnd = secondsToLocalTime(kutapaEndSec)
        )
    }

    private fun secondsToLocalTime(totalSeconds: Double): LocalTime {
        val secs = ((totalSeconds % 86400.0) + 86400.0) % 86400.0
        val hours = (secs / 3600).toInt().coerceIn(0, 23)
        val minutes = ((secs % 3600) / 60).toInt().coerceIn(0, 59)
        val seconds = (secs % 60).toInt().coerceIn(0, 59)
        return LocalTime.of(hours, minutes, seconds)
    }
}

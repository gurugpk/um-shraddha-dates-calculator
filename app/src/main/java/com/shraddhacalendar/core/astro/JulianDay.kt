package com.shraddhacalendar.core.astro

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

/**
 * Astronomical time utilities for Julian Day (JD) calculations.
 * Reference: Jean Meeus, "Astronomical Algorithms", Chapter 7.
 */
object JulianDay {

    /**
     * Converts a UTC [ZonedDateTime] to Julian Day (UT).
     */
    fun fromZonedDateTime(zonedDateTime: ZonedDateTime): Double {
        val utc = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC)
        return fromLocalDateTime(utc.toLocalDateTime())
    }

    /**
     * Converts a UTC [LocalDateTime] to Julian Day.
     */
    fun fromLocalDateTime(dateTime: LocalDateTime): Double {
        val date = dateTime.toLocalDate()
        val time = dateTime.toLocalTime()
        val dayFraction = (time.hour + (time.minute + (time.second + time.nano / 1e9) / 60.0) / 60.0) / 24.0
        return fromLocalDate(date) + dayFraction
    }

    /**
     * Converts a Gregorian [LocalDate] at 00:00:00 UT to Julian Day number.
     */
    fun fromLocalDate(date: LocalDate): Double {
        var year = date.year
        var month = date.monthValue
        val day = date.dayOfMonth

        if (month <= 2) {
            year -= 1
            month += 12
        }

        val a = year / 100
        val b = 2 - a + (a / 4)

        return Math.floor(365.25 * (year + 4716)) +
                Math.floor(30.6001 * (month + 1)) +
                day + b - 1524.5
    }

    /**
     * Julian centuries from standard J2000.0 epoch (JD 2451545.0).
     */
    fun toJulianCenturies(jd: Double): Double {
        return (jd - 2451545.0) / 36525.0
    }

    /**
     * Converts Julian Day back to UTC [LocalDateTime].
     */
    fun toLocalDateTime(jd: Double): LocalDateTime {
        val z = Math.floor(jd + 0.5).toLong()
        val f = (jd + 0.5) - z

        val a = if (z < 2299161) {
            z
        } else {
            val alpha = Math.floor((z - 1867216.25) / 36524.25).toLong()
            z + 1 + alpha - (alpha / 4)
        }

        val b = a + 1524
        val c = Math.floor((b - 122.1) / 365.25).toLong()
        val d = Math.floor(365.25 * c).toLong()
        val e = Math.floor((b - d) / 30.6001).toLong()

        val day = b - d - Math.floor(30.6001 * e).toLong()
        val month = if (e < 14) (e - 1).toInt() else (e - 13).toInt()
        val year = if (month > 2) (c - 4716).toInt() else (c - 4715).toInt()

        val totalSeconds = (f * 86400.0 + 0.5).toLong()
        val hours = (totalSeconds / 3600).toInt()
        val minutes = ((totalSeconds % 3600) / 60).toInt()
        val seconds = (totalSeconds % 60).toInt()

        return LocalDateTime.of(year, month, day.toInt(), hours.coerceIn(0, 23), minutes.coerceIn(0, 59), seconds.coerceIn(0, 59))
    }
}

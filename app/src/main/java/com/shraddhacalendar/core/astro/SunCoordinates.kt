package com.shraddhacalendar.core.astro

import kotlin.math.*

/**
 * High-precision Solar coordinate and Ayanamsha calculations.
 * Reference: Jean Meeus, "Astronomical Algorithms", Chapter 25 & Lahiri Chitrapaksha Ayanamsha.
 */
object SunCoordinates {

    /**
     * Calculates the Chitrapaksha / Lahiri Ayanamsha (in degrees) for a given Julian Day.
     * Standard Lahiri value at J2000.0 is 23° 51' 25.532" = 23.8570922° with precession 50.29"/year.
     */
    fun getAyanamsha(jd: Double): Double {
        val t = JulianDay.toJulianCenturies(jd)
        // Standard IAU / Lahiri Ayanamsha polynomial
        return 23.8570922 + (5029.0966 * t + 1.112 * t * t) / 3600.0
    }

    /**
     * Calculates the Apparent Geocentric Solar Ecliptic Longitude (in degrees, [0, 360)) for Julian Day [jd].
     */
    fun getTropicalLongitude(jd: Double): Double {
        val t = JulianDay.toJulianCenturies(jd)

        // Geometric mean longitude of the Sun
        var l0 = 280.46646 + 36000.76983 * t + 0.0003032 * t * t
        l0 = normalizeDegrees(l0)

        // Mean anomaly of the Sun
        var m = 357.52911 + 35999.05029 * t - 0.0001537 * t * t
        m = normalizeDegrees(m)
        val mRad = Math.toRadians(m)

        // Sun's equation of center
        val c = (1.914602 - 0.004817 * t - 0.000014 * t * t) * sin(mRad) +
                (0.019993 - 0.000101 * t) * sin(2.0 * mRad) +
                0.000289 * sin(3.0 * mRad)

        // Sun's true longitude
        val trueLongitude = l0 + c

        // Apparent longitude correcting for nutation and aberration
        val omega = 125.04 - 1934.136 * t
        val apparentLongitude = trueLongitude - 0.00569 - 0.00478 * sin(Math.toRadians(omega))

        return normalizeDegrees(apparentLongitude)
    }

    /**
     * Calculates the Sidereal (Nirayana) Solar Longitude in degrees [0, 360) using Lahiri Ayanamsha.
     */
    fun getNirayanaLongitude(jd: Double): Double {
        val tropical = getTropicalLongitude(jd)
        val ayanamsha = getAyanamsha(jd)
        return normalizeDegrees(tropical - ayanamsha)
    }

    /**
     * Calculates Sun's Declination (in radians) for a given Julian Day.
     */
    fun getDeclination(jd: Double): Double {
        val t = JulianDay.toJulianCenturies(jd)
        val lambda = Math.toRadians(getTropicalLongitude(jd))

        // Mean obliquity of ecliptic
        val eps0 = 23.439291 - 0.0130042 * t - 0.00000016 * t * t + 0.000000504 * t * t * t
        val eps = Math.toRadians(eps0 + 0.00256 * cos(Math.toRadians(125.04 - 1934.136 * t)))

        return asin(sin(eps) * sin(lambda))
    }

    /**
     * Calculates Equation of Time (in minutes).
     */
    fun getEquationOfTime(jd: Double): Double {
        val t = JulianDay.toJulianCenturies(jd)
        var l0 = normalizeDegrees(280.46646 + 36000.76983 * t + 0.0003032 * t * t)
        var m = normalizeDegrees(357.52911 + 35999.05029 * t - 0.0001537 * t * t)
        val e = 0.016708634 - 0.000042037 * t - 0.0000001267 * t * t

        val eps0 = 23.439291 - 0.0130042 * t
        val y = tan(Math.toRadians(eps0) / 2.0).pow(2)

        val l0Rad = Math.toRadians(l0)
        val mRad = Math.toRadians(m)

        val eotRad = y * sin(2.0 * l0Rad) - 2.0 * e * sin(mRad) +
                4.0 * e * y * sin(mRad) * cos(2.0 * l0Rad) -
                0.5 * y * y * sin(4.0 * l0Rad) -
                1.25 * e * e * sin(2.0 * mRad)

        return Math.toDegrees(eotRad) * 4.0 // 4 minutes per degree
    }

    private fun normalizeDegrees(deg: Double): Double {
        val res = deg % 360.0
        return if (res < 0.0) res + 360.0 else res
    }
}

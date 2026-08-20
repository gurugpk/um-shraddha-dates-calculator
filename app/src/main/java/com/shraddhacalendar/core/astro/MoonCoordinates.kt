package com.shraddhacalendar.core.astro

import kotlin.math.*

/**
 * High-precision Lunar coordinate calculations.
 * Reference: Jean Meeus, "Astronomical Algorithms", Chapter 47 (Lunar Theory).
 */
object MoonCoordinates {

    /**
     * Calculates the Apparent Geocentric Tropical Ecliptic Longitude of the Moon (in degrees, [0, 360)) for Julian Day [jd].
     */
    fun getTropicalLongitude(jd: Double): Double {
        val t = JulianDay.toJulianCenturies(jd)
        val t2 = t * t
        val t3 = t2 * t
        val t4 = t3 * t

        // Moon's mean longitude L'
        val lp = normalizeDegrees(218.3164477 + 481267.88123421 * t - 0.0015786 * t2 + t3 / 538841.0 - t4 / 65194000.0)

        // Moon's mean elongation D
        val d = normalizeDegrees(297.8501921 + 445267.1114034 * t - 0.0018819 * t2 + t3 / 545868.0 - t4 / 113065000.0)

        // Sun's mean anomaly M
        val m = normalizeDegrees(357.5291092 + 35999.0502909 * t - 0.0001536 * t2 + t3 / 24490000.0)

        // Moon's mean anomaly M'
        val mp = normalizeDegrees(134.9633964 + 477198.8675055 * t + 0.0087414 * t2 + t3 / 69699.0 - t4 / 14712000.0)

        // Moon's argument of latitude F
        val f = normalizeDegrees(93.2720950 + 483202.0175233 * t - 0.0036539 * t2 - t3 / 3526000.0 + t4 / 863310000.0)

        val dRad = Math.toRadians(d)
        val mRad = Math.toRadians(m)
        val mpRad = Math.toRadians(mp)
        val fRad = Math.toRadians(f)

        // Periodic perturbation terms in Moon's longitude (coefficients in degrees * 1e-6)
        var sigmaL = 0.0

        // Major terms from Meeus Table 47.A
        sigmaL += 6288774.0 * sin(mpRad)
        sigmaL += 1274027.0 * sin(2.0 * dRad - mpRad)
        sigmaL += 658314.0 * sin(2.0 * dRad)
        sigmaL += 213618.0 * sin(2.0 * mpRad)
        sigmaL += -185116.0 * sin(mRad)
        sigmaL += -114332.0 * sin(2.0 * fRad)
        sigmaL += 58793.0 * sin(2.0 * dRad - 2.0 * mpRad)
        sigmaL += 57066.0 * sin(2.0 * dRad - mRad - mpRad)
        sigmaL += 53322.0 * sin(2.0 * dRad + mpRad)
        sigmaL += 45758.0 * sin(2.0 * dRad - mRad)
        sigmaL += -40923.0 * sin(mRad - mpRad)
        sigmaL += -34720.0 * sin(dRad)
        sigmaL += -30383.0 * sin(mRad + mpRad)
        sigmaL += 15327.0 * sin(2.0 * dRad - 2.0 * fRad)
        sigmaL += -12528.0 * sin(mpRad + 2.0 * fRad)
        sigmaL += 10980.0 * sin(mpRad - 2.0 * fRad)
        sigmaL += 10675.0 * sin(4.0 * dRad - mpRad)
        sigmaL += 10034.0 * sin(3.0 * mpRad)
        sigmaL += 8548.0 * sin(4.0 * dRad - 2.0 * mpRad)
        sigmaL += -7888.0 * sin(2.0 * dRad + mRad - mpRad)
        sigmaL += -6766.0 * sin(2.0 * dRad + mRad)
        sigmaL += -5163.0 * sin(dRad - mpRad)
        sigmaL += 4987.0 * sin(dRad + mRad)
        sigmaL += 4036.0 * sin(2.0 * dRad - mRad + mpRad)
        sigmaL += 3994.0 * sin(2.0 * dRad + 2.0 * mpRad)
        sigmaL += 3861.0 * sin(4.0 * dRad)
        sigmaL += 3665.0 * sin(2.0 * dRad - 3.0 * mpRad)
        sigmaL += -2689.0 * sin(mRad - 2.0 * fRad)
        sigmaL += -2602.0 * sin(2.0 * dRad - mpRad + 2.0 * fRad)
        sigmaL += 2390.0 * sin(2.0 * dRad - mRad - 2.0 * mpRad)
        sigmaL += -2348.0 * sin(dRad + mpRad)
        sigmaL += 2236.0 * sin(2.0 * dRad - 2.0 * mRad)
        sigmaL += -2120.0 * sin(mRad + 2.0 * mpRad)
        sigmaL += -2069.0 * sin(2.0 * mRad)
        sigmaL += 2048.0 * sin(2.0 * dRad - 2.0 * mRad - mpRad)
        sigmaL += -1773.0 * sin(2.0 * dRad + mpRad - 2.0 * fRad)
        sigmaL += -1595.0 * sin(2.0 * dRad + 2.0 * fRad)
        sigmaL += 1215.0 * sin(4.0 * dRad - mRad - mpRad)
        sigmaL += -1110.0 * sin(2.0 * mpRad + 2.0 * fRad)
        sigmaL += -892.0 * sin(3.0 * dRad - mpRad)
        sigmaL += -810.0 * sin(2.0 * dRad + mRad + mpRad)
        sigmaL += 759.0 * sin(4.0 * dRad - mRad - 2.0 * mpRad)
        sigmaL += -713.0 * sin(2.0 * mRad - mpRad)
        sigmaL += -700.0 * sin(2.0 * dRad + 2.0 * mRad - mpRad)
        sigmaL += 691.0 * sin(2.0 * dRad + mRad - 2.0 * mpRad)
        sigmaL += 596.0 * sin(2.0 * dRad - mRad - 2.0 * fRad)
        sigmaL += 549.0 * sin(4.0 * dRad + mpRad)
        sigmaL += 537.0 * sin(4.0 * mpRad)
        sigmaL += 520.0 * sin(4.0 * dRad - mRad)
        sigmaL += -487.0 * sin(dRad - 2.0 * mpRad)
        sigmaL += -399.0 * sin(2.0 * dRad + mpRad + 2.0 * fRad)
        sigmaL += -381.0 * sin(2.0 * mpRad - 2.0 * fRad)
        sigmaL += 351.0 * sin(dRad + mRad - mpRad)
        sigmaL += -340.0 * sin(3.0 * dRad)
        sigmaL += 330.0 * sin(4.0 * dRad - 3.0 * mpRad)
        sigmaL += 327.0 * sin(2.0 * dRad - mRad + 2.0 * mpRad)
        sigmaL += -323.0 * sin(2.0 * mRad + mpRad)
        sigmaL += 299.0 * sin(dRad + mRad + mpRad)
        sigmaL += 294.0 * sin(2.0 * dRad + 3.0 * mpRad)

        val lambda = lp + (sigmaL / 1e6)

        // Add Nutation correction
        val omega = 125.04452 - 1934.136261 * t
        val nutationInLongitude = -0.004778 * sin(Math.toRadians(omega))

        return normalizeDegrees(lambda + nutationInLongitude)
    }

    /**
     * Calculates the Sidereal (Nirayana) Lunar Longitude in degrees [0, 360) using Lahiri Ayanamsha.
     */
    fun getNirayanaLongitude(jd: Double): Double {
        val tropical = getTropicalLongitude(jd)
        val ayanamsha = SunCoordinates.getAyanamsha(jd)
        return normalizeDegrees(tropical - ayanamsha)
    }

    private fun normalizeDegrees(deg: Double): Double {
        val res = deg % 360.0
        return if (res < 0.0) res + 360.0 else res
    }
}

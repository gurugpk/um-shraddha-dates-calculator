package com.shraddhacalendar.core.panchang

import com.shraddhacalendar.core.astro.JulianDay
import com.shraddhacalendar.core.astro.SunCoordinates
import com.shraddhacalendar.core.models.LunarMonth
import com.shraddhacalendar.core.models.PanchangaTithi
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

data class LunarMonthInfo(
    val masa: LunarMonth,
    val isAdhika: Boolean,
    val newMoonPrevJd: Double,
    val newMoonNextJd: Double
)

/**
 * Calculates the Amanta (Amavasyanta) Lunar Month, Adhika Masa detection, and 60-year Samvatsara.
 */
object MasaCalculator {

    private val SAMVATSARAS = listOf(
        "Prabhava", "Vibhava", "Shukla", "Pramoduta", "Prajotpatti",
        "Angirasa", "Shrimukha", "Bhava", "Yuva", "Dhatri",
        "Ishvara", "Bahudhanya", "Pramathi", "Vikrama", "Vrisha",
        "Chitrabhanu", "Svabhanu", "Tarana", "Parthiva", "Vyaya",
        "Sarvajitu", "Sarvadhari", "Virodhi", "Vikrita", "Khara",
        "Nandana", "Vijaya", "Jaya", "Manmatha", "Durmukha",
        "Hevilambi", "Vilambi", "Vikari", "Sharvari", "Plava",
        "Shubhakritu", "Shobhakritu", "Krodhi", "Vishvavasu", "Parabhava",
        "Plavanga", "Kilaka", "Saumya", "Sadharana", "Virodhikritu",
        "Paridhavi", "Pramadicha", "Ananda", "Rakshasa", "Nala",
        "Pingala", "Kalayukti", "Siddharthi", "Raudra", "Durmati",
        "Dundubhi", "Rudhirodgari", "Raktakshi", "Krodhana", "Kshaya"
    )

    /**
     * Determines the Amanta Lunar Month and Adhika Masa status for the given [zonedDateTime].
     */
    fun getLunarMonthInfo(zonedDateTime: ZonedDateTime): LunarMonthInfo {
        val currentJd = JulianDay.fromZonedDateTime(zonedDateTime)

        // Find previous New Moon (elongation = 0°) strictly before/at currentJd
        val prevNmJd = findPreviousNewMoonJd(currentJd)
        // Find next New Moon strictly after currentJd
        val nextNmJd = findNextNewMoonJd(currentJd)

        // Determine Sun's sidereal Nirayana zodiac sign at previous and next New Moons
        val sunLongPrev = SunCoordinates.getNirayanaLongitude(prevNmJd)
        val sunLongNext = SunCoordinates.getNirayanaLongitude(nextNmJd)

        val rashiPrev = (Math.floor(sunLongPrev / 30.0).toInt()) % 12
        val rashiNext = (Math.floor(sunLongNext / 30.0).toInt()) % 12

        val isAdhika = (rashiPrev == rashiNext)

        // The month is determined by the rashi the sun enters (or current rashi + 1 in Adhika)
        val targetRashi = (rashiPrev + 1) % 12
        val masa = rashiToLunarMonth(targetRashi)

        return LunarMonthInfo(
            masa = masa,
            isAdhika = isAdhika,
            newMoonPrevJd = prevNmJd,
            newMoonNextJd = nextNmJd
        )
    }

    /**
     * Complete Panchanga Tithi details including Tithi, Masa, Adhika, and Samvatsara.
     */
    fun getFullPanchangaTithi(zonedDateTime: ZonedDateTime): PanchangaTithi {
        val tithi = TithiCalculator.getTithiAt(zonedDateTime)
        val monthInfo = getLunarMonthInfo(zonedDateTime)
        val samvatsara = getSamvatsara(zonedDateTime.toLocalDate(), monthInfo.masa)

        return PanchangaTithi(
            tithi = tithi,
            masa = monthInfo.masa,
            isAdhikaMasa = monthInfo.isAdhika,
            samvatsara = samvatsara
        )
    }

    /**
     * Calculates the 60-year Jovian Samvatsara name.
     * Reference epoch: Prabhava was 1987-1988 (starting Chaitra Shukla Prathama 1987).
     * 2024-2025: Krodhi, 2025-2026: Vishvavasu, 2026-2027: Parabhava.
     */
    fun getSamvatsara(date: LocalDate, masa: LunarMonth): String {
        var baseYear = date.year
        // In Amanta calendar, new Samvatsara starts in Chaitra (March/April)
        if (date.monthValue < 3 || (date.monthValue == 3 && masa == LunarMonth.PHALGUNA)) {
            baseYear -= 1
        }
        val cycleIndex = ((baseYear - 1987) % 60 + 60) % 60
        return SAMVATSARAS[cycleIndex]
    }

    /**
     * Map Nirayana solar sign (0..11) to Amanta Lunar Month:
     * 0 (Mesha) -> Chaitra
     * 1 (Vrishabha) -> Vaishakha
     * 2 (Mithuna) -> Jyeshtha
     * 3 (Karka) -> Ashadha
     * 4 (Simha) -> Shravana
     * 5 (Kanya) -> Bhadrapada
     * 6 (Tula) -> Ashvina
     * 7 (Vrischika) -> Kartika
     * 8 (Dhanus) -> Margashirsha
     * 9 (Makara) -> Pushya
     * 10 (Kumbha) -> Magha
     * 11 (Mina) -> Phalguna
     */
    private fun rashiToLunarMonth(rashi: Int): LunarMonth {
        return when (rashi) {
            0 -> LunarMonth.CHAITRA
            1 -> LunarMonth.VAISHAKHA
            2 -> LunarMonth.JYESHTHA
            3 -> LunarMonth.ASHADHA
            4 -> LunarMonth.SHRAVANA
            5 -> LunarMonth.BHADRAPADA
            6 -> LunarMonth.ASHVINA
            7 -> LunarMonth.KARTIKA
            8 -> LunarMonth.MARGASHIRSHA
            9 -> LunarMonth.PUSHYA
            10 -> LunarMonth.MAGHA
            11 -> LunarMonth.PHALGUNA
            else -> LunarMonth.CHAITRA
        }
    }

    /**
     * Finds the New Moon (elongation = 0°) immediately preceding [currentJd].
     */
    private fun findPreviousNewMoonJd(currentJd: Double): Double {
        var step = currentJd
        while (step >= currentJd - 35.0) {
            val el1 = TithiCalculator.getLunarElongation(step - 0.5)
            val el2 = TithiCalculator.getLunarElongation(step)
            if (el1 > 300.0 && el2 < 60.0) {
                return refineNewMoon(step - 0.5, step)
            }
            step -= 0.5
        }
        return currentJd - 29.53
    }

    /**
     * Finds the New Moon (elongation = 0°) immediately following [currentJd].
     */
    private fun findNextNewMoonJd(currentJd: Double): Double {
        var step = currentJd
        while (step <= currentJd + 35.0) {
            val el1 = TithiCalculator.getLunarElongation(step)
            val el2 = TithiCalculator.getLunarElongation(step + 0.5)
            if (el1 > 300.0 && el2 < 60.0) {
                return refineNewMoon(step, step + 0.5)
            }
            step += 0.5
        }
        return currentJd + 29.53
    }

    /**
     * Binary search to find exact New Moon JD within [low, high].
     */
    private fun refineNewMoon(low: Double, high: Double): Double {
        var lo = low
        var hi = high
        for (i in 0 until 35) {
            val mid = (lo + hi) / 2.0
            val angle = TithiCalculator.getLunarElongation(mid)
            if (angle > 180.0) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return (lo + hi) / 2.0
    }
}

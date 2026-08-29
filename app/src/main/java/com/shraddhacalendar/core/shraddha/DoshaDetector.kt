package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.astro.JulianDay
import com.shraddhacalendar.core.astro.MoonCoordinates
import com.shraddhacalendar.core.astro.SunCoordinates
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.TithiCalculator
import java.time.DayOfWeek
import java.time.ZonedDateTime
import kotlin.math.abs

/**
 * Comprehensive Shastric Dosha Detection Engine.
 * Evaluates classical scriptural conditions based on Dharma Sindhu, Nirnaya Sindhu,
 * Garuda Purana, and Smriti Muktavali.
 */
object DoshaDetector {

    private val NAKSHATRA_NAMES = listOf(
        "Ashwini", "Bharani", "Krittika", "Rohini", "Mrigashira", "Ardra",
        "Punarvasu", "Pushya", "Ashlesha", "Magha", "Purva Phalguni", "Uttara Phalguni",
        "Hasta", "Chitra", "Swati", "Vishakha", "Anuradha", "Jyeshtha",
        "Moola", "Purva Ashadha", "Uttara Ashadha", "Shravana", "Dhanishta",
        "Shatabhisha", "Purva Bhadrapada", "Uttara Bhadrapada", "Revati"
    )

    fun evaluate(
        zonedDateTime: ZonedDateTime,
        mrutaPanchanga: PanchangaTithi
    ): DoshaEvaluationResult {
        val jd = JulianDay.fromZonedDateTime(zonedDateTime)
        val moonNirayana = MoonCoordinates.getNirayanaLongitude(jd)
        val sunNirayana = SunCoordinates.getNirayanaLongitude(jd)
        val nakshatraNumber = (Math.floor(moonNirayana / (360.0 / 27.0)).toInt() % 27) + 1
        val nakshatraName = NAKSHATRA_NAMES[nakshatraNumber - 1]
        val dayOfWeek = zonedDateTime.dayOfWeek
        val tithiNumber = mrutaPanchanga.tithi.pakshaTithiNumber // 1..15

        val detectedDoshas = mutableListOf<DoshaRecord>()

        // 1. Dhanishta Panchaka (धनिष्ठापञ्चकम्)
        // Spans from 300° (Dhanishta 3rd pada) to 360° (End of Revati)
        // i.e. Dhanishta 2nd half, Shatabhisha, Purva Bhadrapada, Uttara Bhadrapada, Revati
        if (moonNirayana >= 300.0 || (nakshatraNumber == 23 && (moonNirayana % (360.0 / 27.0)) >= (360.0 / 54.0))) {
            val panchakaNakshatra = when (nakshatraNumber) {
                23 -> "Dhanishta (2nd half / Padas 3 & 4)"
                24 -> "Shatabhisha"
                25 -> "Purva Bhadrapada"
                26 -> "Uttara Bhadrapada"
                27 -> "Revati"
                else -> nakshatraName
            }
            detectedDoshas.add(
                DoshaRecord(
                    type = DoshaType.DHANISHTA_PANCHAKA,
                    title = "Dhanishta Panchaka Mrutyu (ಧನಿಷ್ಠಾ ಪಂಚಕ / धनिष्ठापञ्चकम्)",
                    conditionDescription = "Death occurred during $panchakaNakshatra in the sacred Panchaka zone ($moonNirayana° Nirayana).",
                    significance = "In Dharma Sindhu and Smriti Muktavali, demise during Dhanishta Panchaka is traditionally understood to cause severe spiritual affliction (Grihadaha Dosha and obstacle recurrence) unless sanctified through prescribed Shanti rites.",
                    prescribedRemedy = "Performance of Panchaka Shanti Homa, Putala Vidhana (offering 5 symbolic dough/kusha effigies alongside the cremation/funeral), and Kamsya Patra Dana (donation of a bronze vessel filled with pure ghee).",
                    scripturalSource = "Dharma Sindhu (Ashaucha Prakarana), Nirnaya Sindhu (Pariccheda 3), Smriti Muktavali (Pitrumedha)",
                    isSevere = true
                )
            )
        }

        // 2. Tri-Pushkara Yoga (त्रिपुಷ್ಕರ ಯೋಗ)
        // Combination:
        // - Days: Sunday, Tuesday, Saturday (Bhanu, Bhauma, Sthira)
        // - Tithis: Bhadra Tithis (Dvitiya, Saptami, Dvadashi -> 2, 7, 12 in either Shukla or Krishna)
        // - Nakshatras: Pushkara Nakshatras having 1/3 or 3/4 padas (Krittika, Punarvasu, Uttara Phalguni, Vishakha, Uttara Ashadha, Purva Bhadrapada) -> [3, 7, 12, 16, 21, 25]
        val isPushkaraDay = dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.TUESDAY || dayOfWeek == DayOfWeek.SATURDAY
        val isBhadraTithi = tithiNumber == 2 || tithiNumber == 7 || tithiNumber == 12
        val pushkaraNakshatras = setOf(3, 7, 12, 16, 21, 25)
        if (isPushkaraDay && isBhadraTithi && pushkaraNakshatras.contains(nakshatraNumber)) {
            detectedDoshas.add(
                DoshaRecord(
                    type = DoshaType.TRI_PUSHKARA_YOGA,
                    title = "Tri-Pushkara Yoga Mrutyu (ತ್ರಿಪುಷ್ಕರ ಯೋಗ / त्रिपुष्करयोगः)",
                    conditionDescription = "Death occurred on ${dayOfWeek.name} during Bhadra Tithi ($tithiNumber) with Pushkara Nakshatra ($nakshatraName).",
                    significance = "According to Muhurta Chintamani and Dharma Shastras, occurrences during Tri-Pushkara Yoga indicate a 3-fold spiritual echo. It is an exceptional astrological circumstance requiring propitiation.",
                    prescribedRemedy = "Performance of Tri-Pushkara Shanti Homa, Godana (cow donation or symbolic Sankalpa), and Dana of Gold/Sesame (Svarna/Tila Dana) to an austere Brahmana.",
                    scripturalSource = "Muhurta Chintamani, Smriti Muktavali, Dharma Sindhu",
                    isSevere = true
                )
            )
        }

        // 3. Dvi-Pushkara Yoga (ದ್ವಿಪುಷ್ಕರ ಯೋಗ)
        // Combination: Sunday/Tuesday/Saturday + Bhadra Tithis (2, 7, 12) + Nakshatras: Mrigashira (5), Chitra (14), Dhanishta (23)
        val dviPushkaraNakshatras = setOf(5, 14, 23)
        if (isPushkaraDay && isBhadraTithi && dviPushkaraNakshatras.contains(nakshatraNumber)) {
            detectedDoshas.add(
                DoshaRecord(
                    type = DoshaType.DVI_PUSHKARA_YOGA,
                    title = "Dvi-Pushkara Yoga Mrutyu (ದ್ವಿಪುಷ್ಕರ ಯೋಗ / द्विपुष्करयोगः)",
                    conditionDescription = "Death occurred on ${dayOfWeek.name} during Bhadra Tithi ($tithiNumber) with Dvi-Pushkara Nakshatra ($nakshatraName).",
                    significance = "Classical Jyotisha and Smriti texts indicate a two-fold recurrence tendency under Dvi-Pushkara combinations, calling for specific pacification.",
                    prescribedRemedy = "Performance of Dvi-Pushkara Shanti Homa and Vastra/Svarna Dana.",
                    scripturalSource = "Smriti Sangraha, Jyotir-Nirnaya",
                    isSevere = false
                )
            )
        }

        // 4. Nakshatra Gandanta (ಗಂಡಾಂತ ಮೃತ್ಯು)
        // Junction points of Jyeshtha-Moola (18-19), Ashlesha-Magha (9-10), Revati-Ashwini (27-1) within ~1° of the boundary
        val isGandantaNakshatra = when (nakshatraNumber) {
            9 -> (moonNirayana % (360.0 / 27.0)) >= (360.0 / 27.0 - 1.2) // End of Ashlesha
            10 -> (moonNirayana % (360.0 / 27.0)) <= 1.2                  // Beginning of Magha
            18 -> (moonNirayana % (360.0 / 27.0)) >= (360.0 / 27.0 - 1.2) // End of Jyeshtha
            19 -> (moonNirayana % (360.0 / 27.0)) <= 1.2                  // Beginning of Moola
            27 -> (moonNirayana % (360.0 / 27.0)) >= (360.0 / 27.0 - 1.2) // End of Revati
            1 -> (moonNirayana % (360.0 / 27.0)) <= 1.2                   // Beginning of Ashwini
            else -> false
        }
        if (isGandantaNakshatra) {
            detectedDoshas.add(
                DoshaRecord(
                    type = DoshaType.NAKSHATRA_GANDANTA,
                    title = "Nakshatra Gandanta Mrutyu (ಗಂಡಾಂತ ಮೃತ್ಯು / नक्षत्रगण्डान्तम्)",
                    conditionDescription = "Demise occurred during the critical transitional junction of $nakshatraName ($moonNirayana°).",
                    significance = "Gandanta (the junction between Water and Fire signs) is considered a critical Sandhi in Dharma Sindhu, requiring purification of the subtle body.",
                    prescribedRemedy = "Performance of Gandanta Shanti Homa, Mrutyunjaya Japa, and Panchagavya Snana Sankalpa.",
                    scripturalSource = "Dharma Sindhu (Gandanta Shanti Prakarana), Nirnaya Sindhu",
                    isSevere = true
                )
            )
        }

        // 5. Ravi Sankranti / Ayana Sandhi Mrutyu (ಸಂಕ್ರಾಂತಿ ಮೃತ್ಯು)
        // Check if Sun is within 0.5 degrees (1 Ghatika / ~24 mins) of a Rashi boundary (0°, 30°, 60°...)
        val rashiOffset = sunNirayana % 30.0
        val isSankrantiSandhi = rashiOffset <= 0.35 || rashiOffset >= 29.65
        if (isSankrantiSandhi) {
            detectedDoshas.add(
                DoshaRecord(
                    type = DoshaType.SANKRANTI_MRUTYU,
                    title = "Ravi Sankranti Sandhi Mrutyu (ಸಂಕ್ರಾಂತಿ ಸಂಧಿ ಮೃತ್ಯು / संक्रान्तिमृत्युः)",
                    conditionDescription = "Demise occurred right at the moment of Solar Ingress / Ravi Sankranti ($sunNirayana°).",
                    significance = "Transitions during the Sun's planetary ingress into a new zodiac sign represent a transitional time where standard rites require specific Sandhi Prayaschitta.",
                    prescribedRemedy = "Performance of Sankranti Prayaschitta Dana (Tiladana & Godana Sankalpa).",
                    scripturalSource = "Smriti Muktavali, Dharma Sindhu",
                    isSevere = false
                )
            )
        }

        // 6. Grahana Mrutyu (ಗ್ರಹಣ ಮೃತ್ಯು)
        // Check if Sun-Moon elongation is very close to 0° (Solar Eclipse / Amavasya) or 180° (Lunar Eclipse / Purnima)
        // AND Sun is close to Rahu/Ketu nodes (within 12 degrees)
        val elongation = TithiCalculator.getLunarElongation(jd)
        val isEclipseWindow = (elongation <= 4.0 || elongation >= 356.0 || abs(elongation - 180.0) <= 4.0)
        // For precision, check if tithi is exactly Amavasya or Purnima during Grahana
        if (isEclipseWindow && (mrutaPanchanga.tithi.number == 15 || mrutaPanchanga.tithi.number == 30)) {
            // Check node proximity approximation
            val t = JulianDay.toJulianCenturies(jd)
            val nodeLong = (125.04452 - 1934.136261 * t) % 360.0
            val normNode = if (nodeLong < 0) nodeLong + 360.0 else nodeLong
            val distToNode = abs((sunNirayana - normNode) % 180.0)
            if (distToNode <= 15.0 || distToNode >= 165.0) {
                detectedDoshas.add(
                    DoshaRecord(
                        type = DoshaType.GRAHANA_MRUTYU,
                        title = "Grahana Kala Mrutyu (ಗ್ರಹಣ ಕಾಲ ಮೃತ್ಯು / ग्रहणकालमृत्युः)",
                        conditionDescription = "Demise coincided with an eclipse window (Grahana Kala).",
                        significance = "During solar or lunar eclipses, celestial impurity prevails across the atmosphere. Traditional scriptures prescribe specific Rahu-Ketu pacification.",
                        prescribedRemedy = "Performance of Grahana Shanti Homa and Rahu-Ketu Preeti Dana.",
                        scripturalSource = "Garuda Purana (Preta Khanda), Dharma Sindhu",
                        isSevere = true
                    )
                )
            }
        }

        val hasDosha = detectedDoshas.isNotEmpty()
        val generalAdvice = if (hasDosha) {
            "Traditional considerations have been identified based on canonical Smriti texts. Please consult your family Acharya or Matha Vidwan to perform the prescribed Shanti rituals."
        } else {
            "No traditional dosha or exceptional condition detected for this demise time."
        }

        return DoshaEvaluationResult(
            hasDosha = hasDosha,
            doshas = detectedDoshas,
            generalAdvice = generalAdvice
        )
    }
}

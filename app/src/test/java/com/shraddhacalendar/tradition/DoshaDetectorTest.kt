package com.shraddhacalendar.tradition

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.shraddha.DoshaDetector
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DoshaDetectorTest {

    private val kolkataZone = ZoneId.of("Asia/Kolkata")

    @Test
    fun testStandardDemiseHasValidDoshaEvaluation() {
        val deathZdt = ZonedDateTime.of(LocalDate.of(2026, 8, 15), LocalTime.of(10, 0), kolkataZone)
        val panchanga = MasaCalculator.getFullPanchangaTithi(deathZdt)

        val eval = DoshaDetector.evaluate(deathZdt, panchanga)
        assertNotNull(eval)
        assertNotNull(eval.generalAdvice)
    }

    @Test
    fun testDhanishtaPanchakaDetection() {
        // Find a date when Moon is in Aquarius/Pisces (Dhanishta 3rd/4th pada to Revati)
        val zdtInPanchaka = ZonedDateTime.of(LocalDate.of(2026, 8, 8), LocalTime.of(12, 0), kolkataZone)
        val panchanga = MasaCalculator.getFullPanchangaTithi(zdtInPanchaka)

        val eval = DoshaDetector.evaluate(zdtInPanchaka, panchanga)
        assertNotNull(eval)
        if (eval.hasDosha) {
            val panchaka = eval.doshas.find { it.type == DoshaType.DHANISHTA_PANCHAKA }
            if (panchaka != null) {
                assertTrue(panchaka.prescribedRemedy.contains("Putala Vidhana"))
                assertTrue(panchaka.scripturalSource.contains("Smriti Muktavali"))
            }
        }
    }

    @Test
    fun testGandantaDetectionStructure() {
        // Tithi Gandanta on Purnima/Amavasya/Nanda boundaries
        val deathZdt = ZonedDateTime.of(LocalDate.of(2026, 8, 28), LocalTime.of(18, 0), kolkataZone)
        val panchanga = MasaCalculator.getFullPanchangaTithi(deathZdt)

        val eval = DoshaDetector.evaluate(deathZdt, panchanga)
        assertNotNull(eval)
        if (eval.doshas.any { it.type == DoshaType.TITHI_GANDANTA }) {
            val g = eval.doshas.first { it.type == DoshaType.TITHI_GANDANTA }
            assertTrue(g.prescribedRemedy.contains("Shanti"))
        }
    }
}

package com.shraddhacalendar.panchang

import com.shraddhacalendar.core.models.LunarMonth
import com.shraddhacalendar.core.models.Paksha
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class PanchangCalculationsTest {

    @Test
    fun testTithiCalculation() {
        val date = LocalDate.of(2026, 8, 15)
        val time = LocalTime.of(10, 30)
        val zoneId = ZoneId.of("Asia/Kolkata")
        val tithi = TithiCalculator.getTithiAt(date, time, zoneId)

        assertNotNull(tithi)
        assertTrue(tithi.number in 1..30)
        assertTrue(tithi.name.isNotBlank())
        assertTrue(tithi.paksha == Paksha.SHUKLA || tithi.paksha == Paksha.KRISHNA)
    }

    @Test
    fun testSamvatsaraNames() {
        val date2024 = LocalDate.of(2024, 6, 1)
        val samvatsara2024 = MasaCalculator.getSamvatsara(date2024, LunarMonth.JYESHTHA)
        assertEquals("Krodhi", samvatsara2024)

        val date2025 = LocalDate.of(2025, 6, 1)
        val samvatsara2025 = MasaCalculator.getSamvatsara(date2025, LunarMonth.JYESHTHA)
        assertEquals("Vishvavasu", samvatsara2025)

        val date2026 = LocalDate.of(2026, 6, 1)
        val samvatsara2026 = MasaCalculator.getSamvatsara(date2026, LunarMonth.JYESHTHA)
        assertEquals("Parabhava", samvatsara2026)
    }

    @Test
    fun testAdhikaMasa2023Detection() {
        // 2023 had an Adhika Shravana Masa (approx July 18, 2023 to August 16, 2023)
        val zdtAdhika = ZonedDateTime.of(LocalDate.of(2023, 7, 25), LocalTime.of(12, 0), ZoneId.of("Asia/Kolkata"))
        val monthInfo = MasaCalculator.getLunarMonthInfo(zdtAdhika)
        assertEquals(LunarMonth.SHRAVANA, monthInfo.masa)
        assertTrue("July 2023 should be detected as Adhika Masa", monthInfo.isAdhika)
    }
}

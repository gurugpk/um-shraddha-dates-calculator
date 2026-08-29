package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ShakuntalaVarshikaTest {

    @Test
    fun testShakuntalaVarshikaYears() {
        val deathDate = LocalDate.of(2020, 8, 17)
        val deathTime = LocalTime.of(12, 0)
        val location = GeoLocation(
            city = "Bengaluru",
            state = "Karnataka",
            country = "India",
            latitude = 12.9716,
            longitude = 77.5946,
            timezoneId = "Asia/Kolkata"
        )
        val record = PersonDeathRecord(
            name = "Shakuntala",
            relationship = FamilyRelationship.MOTHER,
            deathDate = deathDate,
            deathTime = deathTime,
            location = location,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)

        println("=== SHAKUNTALA ALL 10 YEARS ===")
        result.yearlyObservanceGroups.forEach { group ->
            val v = group.varshikaEvent
            println("Year ${group.yearIndex} (${group.yearTitle}): Date=${v.gregorianDate} | SunriseTithi=${v.sunrisePanchanga.tithi.name} | RitualTithi=${v.tithi.tithi.name} (#${v.tithi.tithi.number}) | Paksha=${v.tithi.tithi.paksha} | Masa=${v.tithi.masa}")
            assertEquals("Trayodashi", v.tithi.tithi.name)
            assertEquals(28, v.tithi.tithi.number)

            if (v.gregorianDate == LocalDate.of(2026, 9, 8)) {
                // Year 6: Sunrise is Dvadashi (#27), Aparahna overlap is Trayodashi (#28)
                assertEquals("Dvadashi", v.sunrisePanchanga.tithi.name)
                assertEquals(27, v.sunrisePanchanga.tithi.number)
                assertTrue(v.isSunriseDifferentFromRitual)
            }

            // Test scriptural basis mapping
            val info = EducationalContentRepository.findInfoForEvent(v)
            assertNotNull(info)
            if (group.yearIndex == 1) {
                assertEquals("prathama_varshika", info!!.ceremonyKey)
            } else {
                assertEquals("annual_varshika", info!!.ceremonyKey)
            }
        }
    }
}

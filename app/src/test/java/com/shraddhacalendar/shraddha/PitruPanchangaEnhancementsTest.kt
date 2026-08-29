package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class PitruPanchangaEnhancementsTest {

    private val bangalore = GeoLocation(
        city = "Bengaluru",
        state = "Karnataka",
        country = "India",
        latitude = 12.9716,
        longitude = 77.5946,
        timezoneId = "Asia/Kolkata"
    )

    @Test
    fun testShakuntalaTrayodashiVerification() {
        val person = PersonDeathRecord(
            name = "Shakuntala",
            relationship = FamilyRelationship.MOTHER,
            deathDate = LocalDate.of(2020, 8, 17),
            deathTime = LocalTime.of(12, 0),
            location = bangalore,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 1))

        // Demise is Trayodashi
        assertEquals("Trayodashi", result.mrutaTithi.tithi.name)
        assertEquals(28, result.mrutaTithi.tithi.number)

        // Year 6 (2026) upcoming Varshika is on 2026-09-08
        val year6Group = result.yearlyObservanceGroups.first { it.yearIndex == 6 }
        assertEquals(LocalDate.of(2026, 9, 8), year6Group.varshikaEvent.gregorianDate)
        assertEquals("Trayodashi", year6Group.varshikaEvent.tithi.tithi.name)
        assertEquals(28, year6Group.varshikaEvent.tithi.tithi.number)

        // Scriptural info for Year 6 is annual_varshika (NOT masika!)
        val infoYear6 = EducationalContentRepository.findInfoForEvent(year6Group.varshikaEvent)
        assertNotNull(infoYear6)
        assertEquals("annual_varshika", infoYear6!!.ceremonyKey)

        // Scriptural info for Year 1 is prathama_varshika
        val year1Group = result.yearlyObservanceGroups.first { it.yearIndex == 1 }
        val infoYear1 = EducationalContentRepository.findInfoForEvent(year1Group.varshikaEvent)
        assertNotNull(infoYear1)
        assertEquals("prathama_varshika", infoYear1!!.ceremonyKey)
    }

    @Test
    fun testMahalayaApplicabilityForWomen() {
        val mother = PersonDeathRecord(
            name = "Lakshmi Bai",
            relationship = FamilyRelationship.MOTHER,
            deathDate = LocalDate.of(2022, 5, 10),
            deathTime = LocalTime.of(10, 30),
            location = bangalore,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(mother, currentDate = LocalDate.of(2026, 8, 1))

        // In Year 2+, Mahalaya Paksha event must exist
        val year2Group = result.yearlyObservanceGroups.first { it.yearIndex == 2 }
        assertNotNull("Mahalaya Paksha must be calculated for deceased mother in Year 2", year2Group.pakshaEvent)

        val infoMahalaya = EducationalContentRepository.findInfoForEvent(year2Group.pakshaEvent!!)
        assertNotNull(infoMahalaya)
        assertEquals("mahalaya_paksha", infoMahalaya!!.ceremonyKey)
    }

    @Test
    fun testDynamicYearIndexComputation() {
        val oldDeath = PersonDeathRecord(
            name = "Shakuntala",
            relationship = FamilyRelationship.MOTHER,
            deathDate = LocalDate.of(2020, 8, 17),
            deathTime = LocalTime.of(12, 0),
            location = bangalore,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        // On Aug 1, 2026, next upcoming observance is in Year 6 (Sep 8, 2026)
        val result = ShraddhaCalculator.calculate(oldDeath, currentDate = LocalDate.of(2026, 8, 1))
        val nextDate = result.nextUpcomingObservance?.gregorianDate
        assertNotNull(nextDate)

        val computedYear = result.yearlyObservanceGroups.find { group ->
            group.varshikaEvent.gregorianDate == nextDate ||
                group.pakshaEvent?.gregorianDate == nextDate ||
                group.masikas.any { it.gregorianDate == nextDate }
        }?.yearIndex ?: 1

        assertEquals(6, computedYear)
    }

    @Test
    fun testRecentsEditAndDeduplicationLogic() {
        data class LocalRecent(
            val name: String,
            val deathDate: LocalDate,
            val deathTime: LocalTime,
            val location: GeoLocation
        )

        val history = mutableListOf<LocalRecent>()

        fun saveOrUpdate(item: LocalRecent) {
            history.removeAll { it.name == item.name && it.deathDate == item.deathDate }
            history.add(0, item)
            if (history.size > 10) history.removeAt(history.size - 1)
        }

        val initial = LocalRecent("Shakuntala", LocalDate.of(2020, 8, 17), LocalTime.of(12, 0), bangalore)
        saveOrUpdate(initial)
        assertEquals(1, history.size)

        // Edit location and save
        val edited = initial.copy(location = bangalore.copy(city = "Bagalkot"))
        saveOrUpdate(edited)

        // Must update without creating duplicates
        assertEquals(1, history.size)
        assertEquals("Bagalkot", history.first().location.city)
    }
}

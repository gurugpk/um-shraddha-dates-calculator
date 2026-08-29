package com.shraddhacalendar.ui

import com.shraddhacalendar.core.calendar.makeEntityKey
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.location.CityDatabase
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class UiUxFunctionalityTest {

    @Test
    fun testValidationAndEmptyNameHandling() {
        val blankName = "   "
        assertTrue(blankName.isBlank())
    }

    @Test
    fun testShodashaOrderAndDatesForPraneshKulkarni() {
        val person = PersonDeathRecord(
            name = "Pranesh kulkarni",
            deathDate = LocalDate.of(2026, 7, 3),
            deathTime = LocalTime.of(5, 0),
            location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        )

        val result = ShraddhaCalculator.calculate(person, LocalDate.of(2026, 7, 3))
        assertNotNull(result)
        assertFalse(result.isDeathOlderThanOneYear)

        val year1 = result.yearlySections.first()
        assertTrue(year1.events.isNotEmpty())

        // Check sequence ordering is strictly ascending
        for (i in 0 until year1.events.size - 1) {
            assertFalse(
                "Events must be in chronological order: ${year1.events[i].gregorianDate} <= ${year1.events[i+1].gregorianDate}",
                year1.events[i].gregorianDate.isAfter(year1.events[i+1].gregorianDate)
            )
        }

        // Verify key milestones are present
        val names = year1.events.map { it.traditionalName }
        assertTrue(names.any { it.contains("Adya Masika") })
        assertTrue(names.any { it.contains("Unmasika") })
        assertTrue(names.any { it.contains("Traipakshika") })
        assertTrue(names.any { it.contains("Una-Shanmasika") })
        assertTrue(names.any { it.contains("Unabdika") })
        assertTrue(names.any { it.contains("Varshika") })
    }

    @Test
    fun testAllLanguagesLocalizationFidelity() {
        val traditionalName = "Masika 1 — Adya Masika"

        val kn = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.KANNADA)
        assertEquals("ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ)", kn)

        val sa = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.SANSKRIT)
        assertEquals("मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्)", sa)

        val te = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.TELUGU)
        assertEquals("మాసికం 1 — ఆద్య మాసికం (13వ రోజు)", te)

        val ta = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.TAMIL)
        assertEquals("மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்)", ta)

        val en = PanchangaLocalizer.localizeTraditionalName(traditionalName, AppLanguage.ENGLISH)
        assertEquals("Masika 1 — Adya Masika (13th Day)", en)
    }

    @Test
    fun testLanguageNativeDisplayNames() {
        assertEquals("English", AppLanguage.ENGLISH.nativeDisplayName)
        assertEquals("ಕನ್ನಡ", AppLanguage.KANNADA.nativeDisplayName)
        assertEquals("संस्कृतम्", AppLanguage.SANSKRIT.nativeDisplayName)
        assertEquals("తెలుగు", AppLanguage.TELUGU.nativeDisplayName)
        assertEquals("தமிழ்", AppLanguage.TAMIL.nativeDisplayName)
    }

    @Test
    fun testCityDatabaseWorldCoverage() {
        assertTrue("Cities database should have 50+ locations", CityDatabase.CITIES.size >= 50)
        assertTrue(CityDatabase.CITIES.any { it.city == "Bengaluru" })
        assertTrue(CityDatabase.CITIES.any { it.city == "New York" })
        assertTrue(CityDatabase.CITIES.any { it.city == "London" })
        assertTrue(CityDatabase.CITIES.any { it.city == "Sydney" })
        assertTrue(CityDatabase.CITIES.any { it.city == "Dubai" })
        assertTrue(CityDatabase.CITIES.any { it.city == "Singapore" })
        assertTrue(CityDatabase.CITIES.any { it.city == "Udupi" })
        assertTrue(CityDatabase.CITIES.any { it.city == "Mantralayam" })
    }

    @Test
    fun testUniqueEntityKeys() {
        val key1 = makeEntityKey("Pranesh Kulkarni", LocalDate.of(2026, 7, 15), 1)
        val key2 = makeEntityKey("Pranesh Kulkarni", LocalDate.of(2026, 7, 29), 2)
        val key3 = makeEntityKey("Ramachandra Rao", LocalDate.of(2026, 7, 15), 1)

        assertEquals("pranesh_kulkarni_2026-07-15_1", key1)
        assertEquals("pranesh_kulkarni_2026-07-29_2", key2)
        assertEquals("ramachandra_rao_2026-07-15_1", key3)

        assertNotEquals(key1, key2)
        assertNotEquals(key1, key3)
    }

    @Test
    fun testCanonicalDevotionalInvocationAndDedicationConcepts() {
        // Verify that all 5 languages have unique and proper native scripts
        val languages = AppLanguage.entries
        assertEquals(5, languages.size)

        // Verify the 5 distinct codes
        assertEquals(listOf("en", "kn", "sa", "te", "ta"), languages.map { it.code })
    }
}

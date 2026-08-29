package com.shraddhacalendar.tradition

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.core.tradition.TraditionEngineFactory
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class TraditionEnginesTest {

    private val bengaluru = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
    private val udupi = GeoLocation("Udupi", "Karnataka", "India", 13.3409, 74.7421, "Asia/Kolkata")
    private val mantralayam = GeoLocation("Mantralayam", "Andhra Pradesh", "India", 15.9338, 77.4297, "Asia/Kolkata")

    @Test
    fun testAllThreeTraditionEnginesInstantiate() {
        MadhwaTradition.entries.forEach { trad ->
            val engine = TraditionEngineFactory.getEngine(trad)
            assertNotNull("Engine for $trad must not be null", engine)
            assertEquals(trad, engine.tradition)
        }
    }

    @Test
    fun testUttaradiMathaCalculationStructure() {
        val person = PersonDeathRecord(
            name = "Sri UM Devotee",
            deathDate = LocalDate.of(2025, 4, 10),
            deathTime = LocalTime.of(9, 30),
            location = bengaluru,
            relationship = FamilyRelationship.FATHER,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2025, 4, 15))

        assertEquals(MadhwaTradition.UTTARADI_MATHA, result.tradition)
        assertTrue(result.yearlyObservanceGroups.isNotEmpty())

        val year1 = result.yearlyObservanceGroups.first()
        assertEquals(1, year1.yearIndex)
        assertEquals(16, year1.masikas.size)
        assertNotNull(year1.varshikaEvent)
        assertNotNull("Paksha is applicable in Year 1 since Prathama Varshika (April 2026) precedes Mahalaya (Sep 2026)", year1.pakshaEvent)
        assertEquals(ObservanceCategory.MAHALAYA_PAKSHA, year1.pakshaEvent!!.observanceCategory)
    }

    @Test
    fun testYear1PakshaNotApplicableWhenMahalayaPrecedesVarshika() {
        // Demise in Kartika (November 2025)
        val person = PersonDeathRecord(
            name = "Late Smt Sumitra",
            deathDate = LocalDate.of(2025, 11, 20),
            deathTime = LocalTime.of(15, 0),
            location = bengaluru,
            relationship = FamilyRelationship.MOTHER,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2025, 11, 25))
        val year1 = result.yearlyObservanceGroups.first()

        assertEquals(1, year1.yearIndex)
        assertNotNull(year1.varshikaEvent)
        // 2026 Mahalaya (Sep 2026) happened BEFORE 1st Varshika (Nov 2026) -> Sapindikarana not done yet
        assertNull("Paksha must NOT be applicable in Year 1 prior to Sapindikarana", year1.pakshaEvent)
        assertNotNull(year1.pakshaNotApplicableReason)
        assertTrue(year1.pakshaNotApplicableReason!!.contains("Preta Avastha"))
    }

    @Test
    fun testMantralayaMuttCalculationStructure() {
        val person = PersonDeathRecord(
            name = "Sri Rayara Devotee",
            deathDate = LocalDate.of(2024, 8, 20),
            deathTime = LocalTime.of(14, 0),
            location = mantralayam,
            relationship = FamilyRelationship.MOTHER,
            tradition = MadhwaTradition.MANTRALAYA_MUTT
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))

        assertEquals(MadhwaTradition.MANTRALAYA_MUTT, result.tradition)
        assertTrue(result.yearlyObservanceGroups.isNotEmpty())
        assertNotNull(result.nextUpcomingObservance)

        if (result.yearlyObservanceGroups.size > 1) {
            val year2 = result.yearlyObservanceGroups[1]
            assertNotNull(year2.varshikaEvent)
            assertNotNull(year2.pakshaEvent)
            assertEquals(ObservanceCategory.MAHALAYA_PAKSHA, year2.pakshaEvent!!.observanceCategory)
        }
    }

    @Test
    fun testUdupiAshtaMathaCalculationStructure() {
        val person = PersonDeathRecord(
            name = "Sri Krishna Devotee",
            deathDate = LocalDate.of(2023, 10, 5),
            deathTime = LocalTime.of(11, 15),
            location = udupi,
            relationship = FamilyRelationship.GRANDFATHER,
            tradition = MadhwaTradition.UDUPI_ASHTA_MATHA
        )

        val result = ShraddhaCalculator.calculate(person, currentDate = LocalDate.of(2026, 8, 20))

        assertEquals(MadhwaTradition.UDUPI_ASHTA_MATHA, result.tradition)
        assertTrue(result.yearlyObservanceGroups.isNotEmpty())
    }

    @Test
    fun testEducationalContentRepositoryCompleteness() {
        val allEvents = EducationalContentRepository.getAllCeremonies()
        assertTrue("Educational content repository must have all 18+ ceremonies documented", allEvents.size >= 18)

        val adya = EducationalContentRepository.findInfoForEvent("Adya Masika")
        assertNotNull(adya)
        assertTrue(adya!!.scripturalCitation.contains("Garuda Purana"))
        assertTrue(adya.stationDescription.contains("86,000-Yojana"))

        val unmasika = EducationalContentRepository.findInfoForEvent("Unmasika")
        assertNotNull(unmasika)
        assertTrue(unmasika!!.stationDescription.contains("Yamya Pura"))

        val vaitarani = EducationalContentRepository.findInfoForEvent("Una-Shanmasika")
        assertNotNull(vaitarani)
        assertTrue(vaitarani!!.stationDescription.contains("Vaitarani"))

        val sapindikarana = EducationalContentRepository.findInfoForEvent("Prathama Varshika")
        assertNotNull(sapindikarana)
        assertTrue(sapindikarana!!.stationDescription.contains("Samyamani Puri"))

        val paksha = EducationalContentRepository.findInfoForEvent("Mahalaya Paksha")
        assertNotNull(paksha)
        assertTrue(paksha!!.scripturalCitation.contains("Smriti Muktavali"))
    }
}

package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.shraddha.EkadashiShraddhaRepository
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.core.shraddha.TimingExplanationGenerator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class EkadashiShraddhaShastraTest {

    @Test
    fun testEkadashiDemiseIdentificationAndDvadashiDate() {
        val deathDate = LocalDate.of(2026, 6, 25) // Shukla Ekadashi
        val deathTime = LocalTime.of(10, 0)
        val location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        val record = PersonDeathRecord(
            name = "Sri Ananda Rao",
            relationship = FamilyRelationship.FATHER,
            deathDate = deathDate,
            deathTime = deathTime,
            location = location,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)

        assertEquals("Ekadashi", result.mrutaTithi.tithi.name)
        assertEquals(Paksha.SHUKLA, result.mrutaTithi.tithi.paksha)
        assertEquals(11, result.mrutaTithi.tithi.pakshaTithiNumber)

        val y1 = result.yearlyObservanceGroups.first()
        val varshika1 = y1.varshikaEvent
        assertTrue("Varshika event should be recognized as Ekadashi observance", varshika1.isEkadashiObservance)
        assertTrue("Varshika event should be shifted from Ekadashi to Dvadashi", varshika1.isEkadashiShifted)
        assertNotNull(varshika1.ekadashiDate)
        assertEquals(varshika1.ekadashiDate!!.plusDays(1), varshika1.gregorianDate)

        // Masikas on Ekadashi should also be flagged and shifted to Dvadashi
        val ekadashiMasikas = y1.masikas.filter { it.isEkadashiShifted }
        assertTrue("Should have monthly Masikas shifted from Ekadashi", ekadashiMasikas.isNotEmpty())
        ekadashiMasikas.forEach { m ->
            assertTrue(m.isEkadashiObservance)
            assertTrue(m.isEkadashiShifted)
            assertNotNull(m.ekadashiDate)
            assertEquals(m.ekadashiDate!!.plusDays(1), m.gregorianDate)
        }
    }

    @Test
    fun testEkadashiRepositoryAcrossAllLanguages() {
        val languages = listOf(
            AppLanguage.ENGLISH,
            AppLanguage.KANNADA,
            AppLanguage.SANSKRIT,
            AppLanguage.TELUGU,
            AppLanguage.TAMIL
        )

        for (lang in languages) {
            val guide = EkadashiShraddhaRepository.getGuide(lang)
            assertNotNull(guide)
            assertTrue("Title should not be blank for $lang", guide.title.isNotBlank())
            assertTrue("Subtitle should not be blank for $lang", guide.subtitle.isNotBlank())
            assertTrue("Canonical shloka 1 should contain Padma Purana text for $lang", guide.canonicalShloka1.isNotBlank())
            assertTrue("Canonical shloka 2 should contain Katyayana text for $lang", guide.canonicalShloka2.isNotBlank())
            assertTrue("Nitya vs Naimittika description should not be blank for $lang", guide.nityaVsNaimittikaDesc.isNotBlank())
            assertTrue("Varshika rule description should not be blank for $lang", guide.varshikaRuleDesc.isNotBlank())
            assertTrue("Paksha rule description should not be blank for $lang", guide.pakshaRuleDesc.isNotBlank())
            assertTrue("Dvadashi Parane description should not be blank for $lang", guide.dvadashiParaneDesc.isNotBlank())
            assertTrue("Disclaimer should not be blank for $lang", guide.disclaimerDesc.isNotBlank())
        }
    }

    @Test
    fun testEkadashiLocalizationHelpers() {
        val dvadashiStr = "Sat, 26 Jun 2026"
        val knBadge = PanchangaLocalizer.localizeEkadashiBadge(dvadashiStr, AppLanguage.KANNADA)
        assertTrue(knBadge.contains("ಏಕಾದಶಿ ವ್ರತ"))
        assertTrue(knBadge.contains("ದ್ವಾದಶಿಯಂದು"))

        val saBadge = PanchangaLocalizer.localizeEkadashiBadge(dvadashiStr, AppLanguage.SANSKRIT)
        assertTrue(saBadge.contains("एकादशीव्रतम्"))
        assertTrue(saBadge.contains("द्वादश्याम्"))

        val teBadge = PanchangaLocalizer.localizeEkadashiBadge(dvadashiStr, AppLanguage.TELUGU)
        assertTrue(teBadge.contains("ఏకాదశి వ్రతం"))
        assertTrue(teBadge.contains("ద్వాదశి"))

        val taBadge = PanchangaLocalizer.localizeEkadashiBadge(dvadashiStr, AppLanguage.TAMIL)
        assertTrue(taBadge.contains("ஏகாதசி விரதம்"))
        assertTrue(taBadge.contains("துவாதசியில்"))

        val enBadge = PanchangaLocalizer.localizeEkadashiBadge(dvadashiStr, AppLanguage.ENGLISH)
        assertTrue(enBadge.contains("Ekadashi Vrata"))
        assertTrue(enBadge.contains("Dvadashi"))

        val knBtn = PanchangaLocalizer.localizeEkadashiButton(AppLanguage.KANNADA)
        assertEquals("ಏಕಾದಶಿ ಶಾಸ್ತ್ರ ಮಾರ್ಗದರ್ಶನ", knBtn)

        val enBtn = PanchangaLocalizer.localizeEkadashiButton(AppLanguage.ENGLISH)
        assertEquals("Ekadashi Guidance", enBtn)

        // Test Ekadashi Shift Note
        val enNote = PanchangaLocalizer.localizeEkadashiShiftNote(AppLanguage.ENGLISH)
        assertEquals("Ekadashi date detected; hence moving the ritual to Dvadashi.", enNote)

        val knNote = PanchangaLocalizer.localizeEkadashiShiftNote(AppLanguage.KANNADA)
        assertTrue(knNote.contains("ಏಕಾದಶಿ ದಿನ ಪತ್ತೆಯಾಗಿದೆ"))
        assertTrue(knNote.contains("ದ್ವಾದಶಿಗೆ"))

        val saNote = PanchangaLocalizer.localizeEkadashiShiftNote(AppLanguage.SANSKRIT)
        assertTrue(saNote.contains("एकादशीतिथिः"))
        assertTrue(saNote.contains("द्वादश्यां"))

        val teNote = PanchangaLocalizer.localizeEkadashiShiftNote(AppLanguage.TELUGU)
        assertTrue(teNote.contains("ఏకాదశి"))
        assertTrue(teNote.contains("ద్వాదశికి"))

        val taNote = PanchangaLocalizer.localizeEkadashiShiftNote(AppLanguage.TAMIL)
        assertTrue(taNote.contains("ஏகாதசி"))
        assertTrue(taNote.contains("துவாதசிக்கு"))
    }

    @Test
    fun testDeathAnniversaryWording() {
        val shashtha = "Yearly Shraddha — Shashtha Varshika Shraddha"
        val localized = PanchangaLocalizer.localizeTraditionalName(shashtha, AppLanguage.ENGLISH)
        assertEquals("Shashtha Varshika Shraddha (6th Death Anniversary)", localized)

        val prathama = "Yearly Shraddha — Prathama Varshika Shraddha"
        assertEquals("Prathama Varshika Shraddha (1st Death Anniversary)", PanchangaLocalizer.localizeTraditionalName(prathama, AppLanguage.ENGLISH))
    }

    @Test
    fun testTimingExplanationEkadashiGuidance() {
        val deathDate = LocalDate.of(2026, 6, 25)
        val location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        val record = PersonDeathRecord(
            name = "Sri Ananda Rao",
            relationship = FamilyRelationship.FATHER,
            deathDate = deathDate,
            deathTime = LocalTime.of(10, 0),
            location = location,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        val result = ShraddhaCalculator.calculate(record)
        val varshika = result.yearlyObservanceGroups.first().varshikaEvent

        val analysisKn = TimingExplanationGenerator.generateAnalysis(varshika, location, AppLanguage.KANNADA)
        assertTrue(analysisKn.whyThisDateExplanation.contains("ಏಕಾದಶಿ ವಿಶೇಷ ಸೂಚನೆ"))
        assertTrue(analysisKn.whyThisDateExplanation.contains("ದ್ವಾದಶಿಯಂದು"))

        val analysisEn = TimingExplanationGenerator.generateAnalysis(varshika, location, AppLanguage.ENGLISH)
        assertTrue(analysisEn.whyThisDateExplanation.contains("Ekadashi Shastric Note"))
        assertTrue(analysisEn.whyThisDateExplanation.contains("Dvadashi"))
    }
}

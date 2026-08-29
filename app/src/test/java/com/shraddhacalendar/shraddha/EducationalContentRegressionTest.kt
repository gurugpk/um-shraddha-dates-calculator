package com.shraddhacalendar.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.shraddha.EducationalContentLocalizer
import com.shraddhacalendar.core.shraddha.EducationalContentRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EducationalContentRegressionTest {

    @Test
    fun testLakshmiBaiAdhikaMasikaMatching() {
        // In Lakshmi Bai's calculation, Masika 13 is "Masika 13 — Adhika Masika (Adhika Jyeshtha)"
        val ceremonyName = "Masika 13 — Adhika Masika (Adhika Jyeshtha)"
        val info = EducationalContentRepository.findInfoForEvent(ceremonyName)

        assertNotNull("Info should not be null for Adhika Masika", info)
        assertEquals("adhika_masika", info?.ceremonyKey)
        assertTrue(info!!.titleEnglish.contains("Adhika Masika"))
        assertTrue(info.pretaConditionAndYatanaDeha.isNotEmpty())
        assertTrue(info.pindaSignificanceAndRelief.isNotEmpty())
        assertTrue(info.scripturalCitation.contains("Dharmasindhu") || info.scripturalCitation.contains("ಧರ್ಮ ಸಿಂಧು"))
        assertNotNull(info.classicalVerse)
        assertTrue(info.classicalVerse!!.contains("अधिकेऽपि"))
    }

    @Test
    fun testAll20CeremoniesHaveAuthenticShastricContentAndVerses() {
        val allKeys = listOf(
            "adya_masika", "unmasika", "dvitiya_masika", "traipakshika",
            "tritiya_masika", "chaturtha_masika", "panchama_masika", "shashtha_masika",
            "una_shanmasika", "saptama_masika", "ashtama_masika", "navama_masika",
            "dashama_masika", "ekadasha_masika", "dvadasha_masika", "adhika_masika",
            "trayodasha_masika", "unabdika", "prathama_varshika", "annual_varshika",
            "mahalaya_paksha"
        )

        for (key in allKeys) {
            val info = EducationalContentRepository.getInfo(key)
            assertNotNull("Content for key '$key' should exist", info)
            info?.let {
                assertFalse("Title English should not be empty for $key", it.titleEnglish.isBlank())
                assertFalse("Day timing should not be empty for $key", it.dayTiming.isBlank())
                assertFalse("Station should not be empty for $key", it.soulJourneyStation.isBlank())
                assertFalse("Station description should not be empty for $key", it.stationDescription.isBlank())
                assertFalse("Preta condition / Yatana Deha should not be empty for $key", it.pretaConditionAndYatanaDeha.isBlank())
                assertFalse("Pinda significance / relief should not be empty for $key", it.pindaSignificanceAndRelief.isBlank())
                assertFalse("Scriptural citation should not be empty for $key", it.scripturalCitation.isBlank())
                assertNotNull("Classical verse should be present for $key", it.classicalVerse)
                assertFalse("Classical verse should not be blank for $key", it.classicalVerse!!.isBlank())
            }
        }
    }

    @Test
    fun testMultiLanguageParityForAllCeremonies() {
        val allKeys = listOf(
            "adya_masika", "unmasika", "dvitiya_masika", "traipakshika",
            "tritiya_masika", "chaturtha_masika", "panchama_masika", "shashtha_masika",
            "una_shanmasika", "saptama_masika", "ashtama_masika", "navama_masika",
            "dashama_masika", "ekadasha_masika", "dvadasha_masika", "adhika_masika",
            "unabdika", "prathama_varshika", "annual_varshika", "mahalaya_paksha"
        )

        val languages = listOf(
            AppLanguage.ENGLISH,
            AppLanguage.KANNADA,
            AppLanguage.SANSKRIT,
            AppLanguage.TELUGU,
            AppLanguage.TAMIL
        )

        for (key in allKeys) {
            val baseInfo = EducationalContentRepository.getInfo(key)
            assertNotNull("Base info must exist for $key", baseInfo)

            for (lang in languages) {
                val localized = EducationalContentLocalizer.getLocalizedInfo(baseInfo!!, lang)
                assertNotNull("Localized info must not be null for $key in $lang", localized)
                assertFalse("Localized titleEnglish should not be blank for $key in $lang", localized.titleEnglish.isBlank())
                assertFalse("Localized soulJourneyStation should not be blank for $key in $lang", localized.soulJourneyStation.isBlank())
                assertFalse("Localized stationDescription should not be blank for $key in $lang", localized.stationDescription.isBlank())
                assertFalse("Localized scripturalCitation should not be blank for $key in $lang", localized.scripturalCitation.isBlank())
            }
        }
    }

    @Test
    fun testAdhikaMasikaLocalizationAcrossAll5Languages() {
        val baseInfo = EducationalContentRepository.getInfo("adhika_masika")!!

        val kn = EducationalContentLocalizer.getLocalizedInfo(baseInfo, AppLanguage.KANNADA)
        assertTrue("Kannada Adhika Masika should contain ಅಧಿಕ", kn.titleEnglish.contains("ಅಧಿಕ"))
        assertTrue("Kannada citation should reference ಧರ್ಮ ಸಿಂಧು", kn.scripturalCitation.contains("ಧರ್ಮ ಸಿಂಧು"))

        val sa = EducationalContentLocalizer.getLocalizedInfo(baseInfo, AppLanguage.SANSKRIT)
        assertTrue("Sanskrit Adhika Masika should contain अधिक", sa.titleEnglish.contains("अधिक"))
        assertTrue("Sanskrit citation should reference धर्मसिन्धुः", sa.scripturalCitation.contains("धर्मसिन्धुः"))

        val te = EducationalContentLocalizer.getLocalizedInfo(baseInfo, AppLanguage.TELUGU)
        assertTrue("Telugu Adhika Masika should contain అధిక", te.titleEnglish.contains("అధిక"))
        assertTrue("Telugu citation should reference ధర్మ సింధు", te.scripturalCitation.contains("ధర్మ సింధు"))

        val ta = EducationalContentLocalizer.getLocalizedInfo(baseInfo, AppLanguage.TAMIL)
        assertTrue("Tamil Adhika Masika should contain அதிக", ta.titleEnglish.contains("அதிக"))
        assertTrue("Tamil citation should reference தர்ம சிந்து", ta.scripturalCitation.contains("தர்ம சிந்து"))
    }
}

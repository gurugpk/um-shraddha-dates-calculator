package com.shraddhacalendar.localization

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.LunarMonth
import com.shraddhacalendar.core.models.Paksha
import com.shraddhacalendar.core.models.TithiInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class PanchangaLocalizationTest {

    @Test
    fun testDayOfWeekLocalization() {
        assertEquals("ಸೋಮವಾರ", PanchangaLocalizer.localizeDayOfWeek("MONDAY", AppLanguage.KANNADA))
        assertEquals("ಮಂಗಳವಾರ", PanchangaLocalizer.localizeDayOfWeek("TUESDAY", AppLanguage.KANNADA))
        assertEquals("ಬುಧವಾರ", PanchangaLocalizer.localizeDayOfWeek("WEDNESDAY", AppLanguage.KANNADA))
        assertEquals("ಗುರುವಾರ", PanchangaLocalizer.localizeDayOfWeek("THURSDAY", AppLanguage.KANNADA))
        assertEquals("ಶುಕ್ರವಾರ", PanchangaLocalizer.localizeDayOfWeek("FRIDAY", AppLanguage.KANNADA))
        assertEquals("ಶನಿವಾರ", PanchangaLocalizer.localizeDayOfWeek("SATURDAY", AppLanguage.KANNADA))
        assertEquals("ಭಾನುವಾರ", PanchangaLocalizer.localizeDayOfWeek("SUNDAY", AppLanguage.KANNADA))

        assertEquals("सोमवासरः", PanchangaLocalizer.localizeDayOfWeek("MONDAY", AppLanguage.SANSKRIT))
        assertEquals("సోమవారం", PanchangaLocalizer.localizeDayOfWeek("MONDAY", AppLanguage.TELUGU))
        assertEquals("திங்கட்கிழமை", PanchangaLocalizer.localizeDayOfWeek("MONDAY", AppLanguage.TAMIL))
        assertEquals("Monday", PanchangaLocalizer.localizeDayOfWeek("MONDAY", AppLanguage.ENGLISH))
    }

    @Test
    fun testYearTitleLocalization() {
        assertEquals("ವರ್ಷ 2 (2021 - 2022)", PanchangaLocalizer.localizeYearTitle(2, 2021, 2022, AppLanguage.KANNADA))
        assertEquals("2-वर्षम् (2021 - 2022)", PanchangaLocalizer.localizeYearTitle(2, 2021, 2022, AppLanguage.SANSKRIT))
        assertEquals("సంవత్సరం 2 (2021 - 2022)", PanchangaLocalizer.localizeYearTitle(2, 2021, 2022, AppLanguage.TELUGU))
        assertEquals("ஆண்டு 2 (2021 - 2022)", PanchangaLocalizer.localizeYearTitle(2, 2021, 2022, AppLanguage.TAMIL))
        assertEquals("Year 2 (2021 - 2022)", PanchangaLocalizer.localizeYearTitle(2, 2021, 2022, AppLanguage.ENGLISH))
    }

    @Test
    fun testRelationshipLocalization() {
        assertEquals("ತಂದೆ", PanchangaLocalizer.localizeRelationship(FamilyRelationship.FATHER, AppLanguage.KANNADA))
        assertEquals("ತಾಯಿ", PanchangaLocalizer.localizeRelationship(FamilyRelationship.MOTHER, AppLanguage.KANNADA))
        assertEquals("पिता", PanchangaLocalizer.localizeRelationship(FamilyRelationship.FATHER, AppLanguage.SANSKRIT))
        assertEquals("తండ్రి", PanchangaLocalizer.localizeRelationship(FamilyRelationship.FATHER, AppLanguage.TELUGU))
        assertEquals("தந்தை", PanchangaLocalizer.localizeRelationship(FamilyRelationship.FATHER, AppLanguage.TAMIL))
    }

    @Test
    fun testMasaLocalization() {
        assertEquals("ನಿಜ ಶ್ರಾವಣ", PanchangaLocalizer.localizeMasa(LunarMonth.SHRAVANA, false, AppLanguage.KANNADA))
        assertEquals("ಅಧಿಕ ಶ್ರಾವಣ", PanchangaLocalizer.localizeMasa(LunarMonth.SHRAVANA, true, AppLanguage.KANNADA))
        assertEquals("निज श्रावणः", PanchangaLocalizer.localizeMasa(LunarMonth.SHRAVANA, false, AppLanguage.SANSKRIT))
    }
}

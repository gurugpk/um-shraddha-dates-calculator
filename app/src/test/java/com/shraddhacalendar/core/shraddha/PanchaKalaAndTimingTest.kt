package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class PanchaKalaAndTimingTest {

    private val testLocation = GeoLocation(
        city = "Gadag",
        state = "Karnataka",
        country = "India",
        latitude = 15.43,
        longitude = 75.63,
        timezoneId = "Asia/Kolkata"
    )

    @Test
    fun testPanchaKalaWindows_All5KalasCalculated() {
        val date = LocalDate.of(2026, 9, 8)
        val windows = DinmanaCalculator.calculatePanchaKalaWindows(date, testLocation)

        assertEquals(5, windows.size)
        assertTrue(windows.containsKey(VedicKala.PRATAH))
        assertTrue(windows.containsKey(VedicKala.SANGAVA))
        assertTrue(windows.containsKey(VedicKala.MADHYAHNA))
        assertTrue(windows.containsKey(VedicKala.APARAHNA))
        assertTrue(windows.containsKey(VedicKala.SAYAHNA))

        val pratah = windows[VedicKala.PRATAH]!!
        val sangava = windows[VedicKala.SANGAVA]!!
        val madhyahna = windows[VedicKala.MADHYAHNA]!!
        val aparahna = windows[VedicKala.APARAHNA]!!
        val sayahna = windows[VedicKala.SAYAHNA]!!

        // Ensure chronological progression
        assertEquals(pratah.second, sangava.first)
        assertEquals(sangava.second, madhyahna.first)
        assertEquals(madhyahna.second, aparahna.first)
        assertEquals(aparahna.second, sayahna.first)
    }

    @Test
    fun testPanchaKalaRepository_AllLanguagesSupported() {
        val date = LocalDate.of(2026, 9, 8)

        AppLanguage.entries.forEach { lang ->
            val kalas = PanchaKalaRepository.getPanchaKalas(date, testLocation, lang)
            assertEquals(5, kalas.size)

            val aparahna = kalas.first { it.kala == VedicKala.APARAHNA }
            assertTrue(aparahna.isSacredAncestralWindow)
            assertTrue(aparahna.name.isNotBlank())
            assertTrue(aparahna.shlokaNativeScript.contains("पूर्वाह्णे") || aparahna.shlokaNativeScript.contains("ಪೂರ್ವಾಹ್ಣೇ") || aparahna.shlokaNativeScript.contains("పూర్వాహ్ణే") || aparahna.shlokaNativeScript.contains("பூர்வாஹ்ணே"))
            assertTrue(aparahna.meaning.isNotBlank())
            assertTrue(aparahna.prescribedDuties.isNotEmpty())
            assertTrue(aparahna.prohibitedDuties.isNotEmpty())
        }
    }

    @Test
    fun testKartruDevaPujaGuide_AllLanguagesAndOptions() {
        AppLanguage.entries.forEach { lang ->
            val guide = PanchaKalaRepository.getKartruDevaPujaGuide(lang)

            assertTrue(guide.canonicalShlokaNative.isNotBlank())
            assertTrue(guide.shlokaMeaning.isNotBlank())
            assertEquals(3, guide.rationales.size)
            assertEquals(3, guide.canonicalOptions.size)

            val opt1 = guide.canonicalOptions[0]
            val opt2 = guide.canonicalOptions[1]
            val opt3 = guide.canonicalOptions[2]

            assertEquals(1, opt1.optionNumber)
            assertEquals(2, opt2.optionNumber)
            assertEquals(3, opt3.optionNumber)

            assertTrue(opt1.title.isNotBlank())
            assertTrue(opt2.title.isNotBlank())
            assertTrue(opt3.title.isNotBlank())
        }
    }

    @Test
    fun testTimingExplanationGenerator_OverlapScenario() {
        // Tuesday 8 Sep 2026 for Trayodashi demise
        val date = LocalDate.of(2026, 9, 8)
        val dayKala = DinmanaCalculator.calculateDayKala(date, testLocation)

        val tithiInfo = TithiInfo.fromNumber(28) // Trayodashi (#28)
        val event = ShraddhaEvent(
            sequenceNumber = 6,
            type = ShraddhaType.VARSHIKA,
            traditionalName = "Varshika Shraddha",
            gregorianDate = date,
            dayOfWeek = "Tuesday",
            tithi = PanchangaTithi(
                tithi = tithiInfo,
                masa = LunarMonth.SHRAVANA,
                isAdhikaMasa = false,
                samvatsara = "Parabhava"
            ),
            kalaDetails = dayKala,
            explanation = "Eka Aparahna Vyapti"
        )

        val analysis = TimingExplanationGenerator.generateAnalysis(event, testLocation, AppLanguage.KANNADA)

        assertEquals(28, analysis.targetTithi.number)
        assertEquals("Trayodashi", analysis.targetTithi.name)
        assertTrue(analysis.targetOverlapMinutes > 0)
        assertTrue(analysis.whyThisDateExplanation.contains("ಅಪರಾಹ್ನ"))
        assertTrue(analysis.whyNotMorningExplanation.contains("ಪ್ರಾತಃಕಾಲದಲ್ಲಿ"))
        assertTrue(analysis.canonicalProhibitionShloka.contains("ಪೂರ್ವಾಹ್ಣೇ"))
    }
}

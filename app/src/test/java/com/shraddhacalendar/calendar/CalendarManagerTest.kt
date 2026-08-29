package com.shraddhacalendar.calendar

import com.shraddhacalendar.core.calendar.makeEntityKey
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class CalendarManagerTest {

    @Test
    fun testMakeEntityKeyWithPersonName() {
        val date = LocalDate.of(2026, 7, 15)
        val key = makeEntityKey("Pranesh Kulkarni", date, 1)
        assertEquals("pranesh_kulkarni_2026-07-15_1", key)

        val keySpecialChars = makeEntityKey("  Sri Ramachandra Rao (Bengaluru)  ", date, 16)
        assertEquals("sri_ramachandra_rao_bengaluru_2026-07-15_16", keySpecialChars)
    }

    @Test
    fun testEventTitleWithPersonName() {
        val personName = "Pranesh Kulkarni"
        val rawCeremonyName = "Masika 1 — Adya Masika"
        val localizedEnglish = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.ENGLISH)
        val titleEnglish = "$localizedEnglish — $personName"
        assertEquals("Masika 1 — Adya Masika (13th Day) — Pranesh Kulkarni", titleEnglish)

        val localizedKannada = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.KANNADA)
        val titleKannada = "$localizedKannada — $personName"
        assertEquals("ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ (13ನೇ ದಿನ) — Pranesh Kulkarni", titleKannada)

        val localizedSanskrit = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.SANSKRIT)
        assertEquals("मासिकम् 1 — आद्यमासिकम् (13 तमदिनम्) — Pranesh Kulkarni", "$localizedSanskrit — $personName")

        val localizedTelugu = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.TELUGU)
        assertEquals("మాసికం 1 — ఆద్య మాసికం (13వ రోజు) — Pranesh Kulkarni", "$localizedTelugu — $personName")

        val localizedTamil = PanchangaLocalizer.localizeTraditionalName(rawCeremonyName, AppLanguage.TAMIL)
        assertEquals("மாஸிகம் 1 — ஆத்ய மாஸிகம் (13ஆம் நாள்) — Pranesh Kulkarni", "$localizedTamil — $personName")

        val varshikaName = "Prathama Varshika Shraddha"
        val localizedVarshika = PanchangaLocalizer.localizeTraditionalName(varshikaName, AppLanguage.ENGLISH)
        val titleVarshika = "$localizedVarshika — $personName"
        assertEquals("Prathama Varshika Shraddha (1st Death Anniversary) — Pranesh Kulkarni", titleVarshika)
    }

    @Test
    fun testDescriptionContent() {
        val person = PersonDeathRecord(
            name = "Pranesh Kulkarni",
            deathDate = LocalDate.of(2026, 7, 3),
            deathTime = LocalTime.of(5, 0),
            location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        )

        val event = ShraddhaEvent(
            sequenceNumber = 1,
            type = ShraddhaType.MASIKA,
            traditionalName = "Masika 1 — Adya Masika",
            gregorianDate = LocalDate.of(2026, 7, 15),
            dayOfWeek = "Wednesday",
            tithi = PanchangaTithi(
                tithi = TithiInfo.fromNumber(2),
                masa = LunarMonth.JYESHTHA,
                isAdhikaMasa = false,
                samvatsara = "Parabhava"
            ),
            kalaDetails = DayKalaDetails(
                date = LocalDate.of(2026, 7, 15),
                sunrise = LocalTime.of(6, 0),
                sunset = LocalTime.of(18, 45),
                dinmanaMinutes = 765,
                aparahnaStart = LocalTime.of(13, 42),
                aparahnaEnd = LocalTime.of(16, 16),
                kutapaStart = LocalTime.of(11, 40),
                kutapaEnd = LocalTime.of(12, 30)
            ),
            explanation = "Adya Masika on 13th day"
        )

        val localizedCeremony = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, AppLanguage.ENGLISH)
        val title = "$localizedCeremony — ${person.name}"
        assertEquals("Masika 1 — Adya Masika (13th Day) — Pranesh Kulkarni", title)

        val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, AppLanguage.ENGLISH)
        val localizedPaksha = PanchangaLocalizer.localizePaksha(event.tithi.tithi.paksha, AppLanguage.ENGLISH)
        val localizedTithi = PanchangaLocalizer.localizeTithi(event.tithi.tithi, AppLanguage.ENGLISH)

        val description = """
            Person: ${person.name}
            Ceremony: $localizedCeremony
            Date: ${event.gregorianDate}
            Panchanga: ${event.tithi.samvatsara} Nama Samvatsara, $localizedMasa, $localizedPaksha, $localizedTithi
            Location: ${person.location.displayName}
            Aparahna Timing: ${event.kalaDetails.aparahnaStart} to ${event.kalaDetails.aparahnaEnd}
        """.trimIndent()

        assertTrue(description.contains("Person: Pranesh Kulkarni"))
        assertTrue(description.contains("Masika 1 — Adya Masika (13th Day)"))
        assertTrue(description.contains("Parabhava"))
        assertTrue(description.contains("Bengaluru, Karnataka, India"))
    }

    @Test
    fun testPatternMatchingForCalendarEventDelete() {
        val personName = "Pranesh Kulkarni"
        val ritual1 = "Masika 1 — Adya Masika"
        val cleanRitual1 = ritual1.substringAfter("—").trim()
        assertEquals("Adya Masika", cleanRitual1)

        val titlePattern1 = "%$cleanRitual1%${personName.trim()}%"
        assertEquals("%Adya Masika%Pranesh Kulkarni%", titlePattern1)

        val ritual2 = "Yearly Shraddha — Prathama Varshika Shraddha"
        val cleanRitual2 = ritual2.substringAfter("—").trim()
        assertEquals("Prathama Varshika Shraddha", cleanRitual2)

        val titlePattern2 = "%$cleanRitual2%${personName.trim()}%"
        assertEquals("%Prathama Varshika Shraddha%Pranesh Kulkarni%", titlePattern2)
    }
}

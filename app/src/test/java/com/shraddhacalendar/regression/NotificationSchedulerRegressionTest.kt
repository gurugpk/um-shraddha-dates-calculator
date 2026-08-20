package com.shraddhacalendar.regression

import com.shraddhacalendar.core.calendar.makeEntityKey
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.*
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Deterministic test suite for App Notification scheduling logic,
 * entity key generation, cancel logic, and multi-language message formatting.
 */
class NotificationSchedulerRegressionTest {

    @Test
    fun testEntityKeyIdempotencyAndIsolation() {
        val date1 = LocalDate.of(2026, 8, 21)
        val date2 = LocalDate.of(2026, 9, 19)

        val key1 = makeEntityKey("Pranesh Kulkarni", date1, 1)
        val key2 = makeEntityKey("Pranesh Kulkarni", date1, 1)
        val key3 = makeEntityKey("Pranesh Kulkarni", date2, 2)
        val key4 = makeEntityKey("Ramachandra Rao", date1, 1)

        assertEquals("Same inputs must produce identical entity key", key1, key2)
        assertNotEquals("Different dates must produce different entity keys", key1, key3)
        assertNotEquals("Different persons must produce different entity keys", key1, key4)
    }

    @Test
    fun test2DayAnd1DayTriggerCalculations() {
        val shraddhaDate = LocalDate.of(2026, 8, 21)

        val trigger2DaysBefore = shraddhaDate.minusDays(2)
        val trigger1DayBefore = shraddhaDate.minusDays(1)

        assertEquals(LocalDate.of(2026, 8, 19), trigger2DaysBefore)
        assertEquals(LocalDate.of(2026, 8, 20), trigger1DayBefore)
    }

    @Test
    fun testNotificationMessageFormattingInAll5Languages() {
        val personName = "Pranesh Kulkarni"
        val ritualRaw = "Masika 1 — Adya Masika"
        val dateFormatted = "21 August 2026"

        // Templates corresponding to strings.xml
        val en2Day = "%1\$s — %2\$s is in 2 days, on %3\$s."
        val en1Day = "%1\$s — %2\$s is tomorrow, %3\$s."

        val kn2Day = "%1\$s — %2\$s ಇನ್ನೂ ೨ ದಿನಗಳಲ್ಲಿದೆ, %3\$s ರಂದು."
        val kn1Day = "%1\$s — %2\$s ನಾಳೆ ಇದೆ, %3\$s ರಂದು."

        val sa2Day = "%1\$s — %2\$s दिनद्वयानन्तरम् अस्ति, %3\$s दिनाङ्के।"
        val sa1Day = "%1\$s — %2\$s श्वः अस्ति, %3\$s दिनाङ्के।"

        val te2Day = "%1\$s — %2\$s మరో 2 రోజుల్లో ఉంది, %3\$s తేదీన."
        val te1Day = "%1\$s — %2\$s రేపు ఉంది, %3\$s తేదీన."

        val ta2Day = "%1\$s — %2\$s இன்னும் 2 நாட்களில் உள்ளது, %3\$s அன்று."
        val ta1Day = "%1\$s — %2\$s நாளை உள்ளது, %3\$s அன்று."

        // Test English
        val ritualEn = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.ENGLISH)
        val msgEn2 = String.format(en2Day, personName, ritualEn, dateFormatted)
        val msgEn1 = String.format(en1Day, personName, ritualEn, dateFormatted)
        assertEquals("Pranesh Kulkarni — Masika 1 — Adya Masika is in 2 days, on 21 August 2026.", msgEn2)
        assertEquals("Pranesh Kulkarni — Masika 1 — Adya Masika is tomorrow, 21 August 2026.", msgEn1)

        // Test Kannada
        val ritualKn = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.KANNADA)
        val msgKn2 = String.format(kn2Day, personName, ritualKn, dateFormatted)
        val msgKn1 = String.format(kn1Day, personName, ritualKn, dateFormatted)
        assertEquals("Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ ಇನ್ನೂ ೨ ದಿನಗಳಲ್ಲಿದೆ, 21 August 2026 ರಂದು.", msgKn2)
        assertEquals("Pranesh Kulkarni — ಮಾಸಿಕ 1 — ಆದ್ಯ ಮಾಸಿಕ ನಾಳೆ ಇದೆ, 21 August 2026 ರಂದು.", msgKn1)

        // Test Sanskrit (Devanagari)
        val ritualSa = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.SANSKRIT)
        val msgSa2 = String.format(sa2Day, personName, ritualSa, dateFormatted)
        val msgSa1 = String.format(sa1Day, personName, ritualSa, dateFormatted)
        assertEquals("Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् दिनद्वयानन्तरम् अस्ति, 21 August 2026 दिनाङ्के।", msgSa2)
        assertEquals("Pranesh Kulkarni — मासिकम् 1 — आद्यमासिकम् श्वः अस्ति, 21 August 2026 दिनाङ्के।", msgSa1)

        // Test Telugu
        val ritualTe = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.TELUGU)
        val msgTe2 = String.format(te2Day, personName, ritualTe, dateFormatted)
        val msgTe1 = String.format(te1Day, personName, ritualTe, dateFormatted)
        assertEquals("Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం మరో 2 రోజుల్లో ఉంది, 21 August 2026 తేదీన.", msgTe2)
        assertEquals("Pranesh Kulkarni — మాసికం 1 — ఆద్య మాసికం రేపు ఉంది, 21 August 2026 తేదీన.", msgTe1)

        // Test Tamil
        val ritualTa = PanchangaLocalizer.localizeTraditionalName(ritualRaw, AppLanguage.TAMIL)
        val msgTa2 = String.format(ta2Day, personName, ritualTa, dateFormatted)
        val msgTa1 = String.format(ta1Day, personName, ritualTa, dateFormatted)
        assertEquals("Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் இன்னும் 2 நாட்களில் உள்ளது, 21 August 2026 அன்று.", msgTa2)
        assertEquals("Pranesh Kulkarni — மாஸிகம் 1 — ஆத்ய மாஸிகம் நாளை உள்ளது, 21 August 2026 அன்று.", msgTa1)
    }

    @Test
    fun testRequestCodeUniqueness() {
        val key1 = "pranesh_kulkarni_2026-08-21_1"
        val key2 = "pranesh_kulkarni_2026-08-21_2"

        val req1_2d = (key1.hashCode() * 31 + 2).let { if (it < 0) -it else it }
        val req1_1d = (key1.hashCode() * 31 + 1).let { if (it < 0) -it else it }
        val req2_2d = (key2.hashCode() * 31 + 2).let { if (it < 0) -it else it }

        assertNotEquals("2-day and 1-day request codes must differ", req1_2d, req1_1d)
        assertNotEquals("Different ceremonies must have distinct request codes", req1_2d, req2_2d)
    }
}

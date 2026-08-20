package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.panchang.TithiCalculator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * Calculates the complete Shodasha (16) Shraddha rites for Year 1 following Madhwa tradition
 * (Smriti Muktavali / Dvaita Smriti).
 *
 * Sequence:
 *  1. Adya Masika (13th Day after death, completing Ashaucha)
 *  2. Unmasika (27th Day after death)
 *  3. Dwitiya Masika (1st Lunar Death Tithi)
 *  4. Traipakshika (45th Day after death)
 *  5. Tritiya Masika (2nd Lunar Death Tithi)
 *  6. Chaturtha Masika (3rd Lunar Death Tithi)
 *  7. Panchama Masika (4th Lunar Death Tithi)
 *  8. Shashtha Masika / Shanmasika (5th Lunar Death Tithi)
 *  9. Una-Shanmasika with Godana (Observed in 6th month, ~Day 163-170)
 * 10. Saptama Masika (6th Lunar Death Tithi)
 * 11. Ashtama Masika (7th Lunar Death Tithi)
 * 12. Navama Masika (8th Lunar Death Tithi)
 * 13. Dashama Masika (9th Lunar Death Tithi)
 * 14. Ekadasha Masika (10th Lunar Death Tithi)
 * 15. Dvadasha Masika (11th Lunar Death Tithi)
 * 16. Unabdika (Una-Varshika, Day ~346-350)
 * 17. Yearly Shraddha — Prathama Varshika (12th Lunar Death Tithi / 1st Annual Shraddha)
 */
object MasikaShraddhaCalculator {

    fun calculateYear1Events(
        personRecord: PersonDeathRecord,
        mrutaPanchanga: PanchangaTithi
    ): List<ShraddhaEvent> {
        val deathDate = personRecord.deathDate
        val location = personRecord.location
        val zoneId = ZoneId.of(location.timezoneId)
        val targetTithiNumber = mrutaPanchanga.tithi.number

        val events = mutableListOf<ShraddhaEvent>()

        // 1. Adya Masika on 13th Day (inclusive counting: deathDate + 12 days)
        val adyaDate = deathDate.plusDays(12)
        val adyaKala = DinmanaCalculator.calculateDayKala(adyaDate, location)
        val adyaZdt = ZonedDateTime.of(adyaDate, adyaKala.aparahnaStart, zoneId)
        val adyaPanchanga = MasaCalculator.getFullPanchangaTithi(adyaZdt)

        val adyaEvent = ShraddhaEvent(
            sequenceNumber = 1,
            type = ShraddhaType.MASIKA,
            traditionalName = "Masika 1 — Adya Masika",
            gregorianDate = adyaDate,
            dayOfWeek = adyaDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            tithi = adyaPanchanga,
            kalaDetails = adyaKala,
            explanation = "Observed on 13th day following death (completion of Ashaucha rites)"
        )

        // 2. Unmasika on 27th Day (inclusive counting: deathDate + 26 days)
        val unmasikaDate = deathDate.plusDays(26)
        val unmasikaKala = DinmanaCalculator.calculateDayKala(unmasikaDate, location)
        val unmasikaZdt = ZonedDateTime.of(unmasikaDate, unmasikaKala.aparahnaStart, zoneId)
        val unmasikaPanchanga = MasaCalculator.getFullPanchangaTithi(unmasikaZdt)

        val unmasikaEvent = ShraddhaEvent(
            sequenceNumber = 2,
            type = ShraddhaType.UNA_RITE,
            traditionalName = "Masika 2 — Unmasika",
            gregorianDate = unmasikaDate,
            dayOfWeek = unmasikaDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            tithi = unmasikaPanchanga,
            kalaDetails = unmasikaKala,
            explanation = "Traditional interval rite observed on Day 27 following death"
        )

        // 3. Traipakshika on 45th Day (inclusive counting: deathDate + 44 days)
        val traipakshikaDate = deathDate.plusDays(44)
        val traipakshikaKala = DinmanaCalculator.calculateDayKala(traipakshikaDate, location)
        val traipakshikaZdt = ZonedDateTime.of(traipakshikaDate, traipakshikaKala.aparahnaStart, zoneId)
        val traipakshikaPanchanga = MasaCalculator.getFullPanchangaTithi(traipakshikaZdt)

        val traipakshikaEvent = ShraddhaEvent(
            sequenceNumber = 4,
            type = ShraddhaType.UNA_RITE,
            traditionalName = "Masika 4 — Traipakshika",
            gregorianDate = traipakshikaDate,
            dayOfWeek = traipakshikaDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            tithi = traipakshikaPanchanga,
            kalaDetails = traipakshikaKala,
            explanation = "Traditional interval rite observed on Day 45 (three half-months) following death"
        )

        // 4. Una-Shanmasika (with Godana) observed in the 6th month (~Day 163-170)
        val unaShanmasikaDate = deathDate.plusDays(163)
        val unaShanmasikaKala = DinmanaCalculator.calculateDayKala(unaShanmasikaDate, location)
        val unaShanmasikaZdt = ZonedDateTime.of(unaShanmasikaDate, unaShanmasikaKala.aparahnaStart, zoneId)
        val unaShanmasikaPanchanga = MasaCalculator.getFullPanchangaTithi(unaShanmasikaZdt)

        val unaShanmasikaEvent = ShraddhaEvent(
            sequenceNumber = 9,
            type = ShraddhaType.UNA_RITE,
            traditionalName = "Masika 9 — Una-Shanmasika (with Godana)",
            gregorianDate = unaShanmasikaDate,
            dayOfWeek = unaShanmasikaDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            tithi = unaShanmasikaPanchanga,
            kalaDetails = unaShanmasikaKala,
            explanation = "Traditional preliminary 6-month interval rite (Una-Shanmasika) with Godana observed before Saptama Masika"
        )

        // 5. Unabdika (Una-Varshika) on Day 350
        val unabdikaDate = deathDate.plusDays(350)
        val unabdikaKala = DinmanaCalculator.calculateDayKala(unabdikaDate, location)
        val unabdikaZdt = ZonedDateTime.of(unabdikaDate, unabdikaKala.aparahnaStart, zoneId)
        val unabdikaPanchanga = MasaCalculator.getFullPanchangaTithi(unabdikaZdt)

        val unabdikaEvent = ShraddhaEvent(
            sequenceNumber = 16,
            type = ShraddhaType.UNA_RITE,
            traditionalName = "Masika 16 — Unabdika (Una-Varshika)",
            gregorianDate = unabdikaDate,
            dayOfWeek = unabdikaDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
            tithi = unabdikaPanchanga,
            kalaDetails = unabdikaKala,
            explanation = "Traditional preliminary annual interval rite (Unabdika) observed on Day 350"
        )

        // 6. Calculate Lunar Monthly Masikas (Dwitiya Masika to Prathama Varshika)
        val lunarNames = listOf(
            "Dwitiya Masika",       // 1st monthly death tithi (M1)
            "Tritiya Masika",       // 2nd monthly death tithi (M2)
            "Chaturtha Masika",     // 3rd monthly death tithi (M3)
            "Panchama Masika",      // 4th monthly death tithi (M4)
            "Shashtha Masika (Shanmasika)", // 5th monthly death tithi (M5)
            "Saptama Masika",       // 6th monthly death tithi (M6)
            "Ashtama Masika",       // 7th monthly death tithi (M7)
            "Navama Masika",        // 8th monthly death tithi (M8)
            "Dashama Masika",       // 9th monthly death tithi (M9)
            "Ekadasha Masika",      // 10th monthly death tithi (M10)
            "Dvadasha Masika",      // 11th monthly death tithi (M11)
            "Prathama Varshika Shraddha" // 12th monthly death tithi (M12 / Annual)
        )

        val lunarEvents = mutableListOf<ShraddhaEvent>()
        var approxSearchDate = deathDate.plusDays(29)
        var monthIdx = 0
        var totalMonths = 0

        while (monthIdx < 12 && totalMonths < 14) {
            val selected = AparahnaVyaptiEngine.findShraddhaDate(
                targetTithiNumber = targetTithiNumber,
                approximateDate = approxSearchDate,
                location = location
            )

            val eventZdt = ZonedDateTime.of(selected.date, selected.kalaDetails.aparahnaStart, zoneId)
            val eventPanchanga = MasaCalculator.getFullPanchangaTithi(eventZdt)

            val isAdhika = eventPanchanga.isAdhikaMasa
            val isFinalVarshika = (monthIdx == 11 && !isAdhika)

            val name = if (isAdhika) {
                "Adhika Masika (${eventPanchanga.masaDisplayName})"
            } else {
                lunarNames[monthIdx]
            }

            val type = if (isFinalVarshika) ShraddhaType.VARSHIKA else ShraddhaType.MASIKA

            lunarEvents.add(
                ShraddhaEvent(
                    sequenceNumber = 0,
                    type = type,
                    traditionalName = name,
                    gregorianDate = selected.date,
                    dayOfWeek = selected.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                    tithi = eventPanchanga,
                    kalaDetails = selected.kalaDetails,
                    explanation = "${selected.evaluationReason} | Aparahna: ${selected.kalaDetails.aparahnaStart} to ${selected.kalaDetails.aparahnaEnd}"
                )
            )

            if (!isAdhika) {
                monthIdx++
            }
            totalMonths++
            approxSearchDate = selected.date.plusDays(29)
        }

        // 7. Combine all events, sort chronologically, and assign proper sequential numbering
        val rawAll = (listOf(adyaEvent, unmasikaEvent, traipakshikaEvent, unaShanmasikaEvent, unabdikaEvent) + lunarEvents)
            .sortedBy { it.gregorianDate }

        var seq = 1
        for (ev in rawAll) {
            val formattedName = when {
                ev.type == ShraddhaType.VARSHIKA -> "Yearly Shraddha — ${ev.traditionalName}"
                ev.traditionalName.startsWith("Masika") -> {
                    val clean = ev.traditionalName.substringAfter("—").trim()
                    "Masika $seq — $clean"
                }
                else -> "Masika $seq — ${ev.traditionalName}"
            }

            events.add(
                ev.copy(
                    sequenceNumber = seq,
                    traditionalName = formattedName
                )
            )
            seq++
        }

        return events
    }
}

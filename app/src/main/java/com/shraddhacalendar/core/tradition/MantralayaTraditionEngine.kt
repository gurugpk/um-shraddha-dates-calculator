package com.shraddhacalendar.core.tradition

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import com.shraddhacalendar.core.panchang.MasaCalculator
import com.shraddhacalendar.core.shraddha.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Tradition-specific calculation engine for Mantralaya (Sri Raghavendra Swamy Mutt).
 * Follows Sri Raghavendra Swamy Mutt Panchanga conventions and Smriti Muktavali guidelines.
 */
class MantralayaTraditionEngine : IShraddhaTraditionEngine {

    override val tradition: MadhwaTradition = MadhwaTradition.MANTRALAYA_MUTT

    override fun calculateMrutaTithi(
        deathDate: LocalDate,
        deathTime: LocalTime,
        location: GeoLocation
    ): PanchangaTithi {
        val zoneId = ZoneId.of(location.timezoneId)
        val deathZdt = ZonedDateTime.of(deathDate, deathTime, zoneId)
        return MasaCalculator.getFullPanchangaTithi(deathZdt)
    }

    override fun calculateShodashaMasikas(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi
    ): List<ShraddhaEvent> {
        return MasikaShraddhaCalculator.calculateYear1Events(record, mrutaTithi)
    }

    override fun calculateYearlyObservanceGroups(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi,
        currentDate: LocalDate
    ): List<YearlyObservanceGroup> {
        val location = record.location
        val zoneId = ZoneId.of(location.timezoneId)
        val targetTithiNumber = mrutaTithi.tithi.number
        val deathDate = record.deathDate

        val year1MasikaEvents = calculateShodashaMasikas(record, mrutaTithi)
        val year1VarshikaEvent = year1MasikaEvents.last()

        val year1PrathamaDate = year1VarshikaEvent.gregorianDate
        val startYear1 = deathDate.year
        val endYear1 = year1PrathamaDate.year

        // Determine Mahalaya Paksha for Year 1:
        // In Dharma Shastra, Mahalaya Paksha Shraddha is applicable once Sapindikarana (Prathama Varshika) is completed.
        // If Prathama Varshika happens BEFORE / ON the Mahalaya Paksha date in that anniversary year (e.g. death in Bhadrapada Shukla),
        // Sapindikarana is already over, so Mahalaya is applicable in Year 1!
        val year1Mahalaya = calculateMahalayaPaksha(
            year = year1PrathamaDate.year,
            mrutaTithi = mrutaTithi,
            location = location,
            zoneId = zoneId
        )

        val isYear1PakshaApplicable = year1Mahalaya != null && !year1Mahalaya.gregorianDate.isBefore(year1PrathamaDate)

        val year1Group = YearlyObservanceGroup(
            yearIndex = 1,
            yearTitle = "Year 1 ($startYear1 - $endYear1)",
            samvatsaraName = "${mrutaTithi.samvatsara} Nama Samvatsara",
            isExpandedByDefault = true,
            masikas = year1MasikaEvents.dropLast(1),
            varshikaEvent = year1VarshikaEvent,
            pakshaEvent = if (isYear1PakshaApplicable) year1Mahalaya else null,
            pakshaNotApplicableReason = if (isYear1PakshaApplicable) null else "Not Applicable in Year 1 (Departed soul remains in Preta Avastha prior to Sapindikarana)"
        )

        val groups = mutableListOf(year1Group)
        val deathYear = deathDate.year
        val currentYear = currentDate.year
        val maxYearIdx = ((currentYear - deathYear) + 2).coerceIn(2, 60)

        var prevVarshikaDate = year1PrathamaDate

        for (yearIdx in 2..maxYearIdx) {
            val result = VarshikaDateFinder.findVarshikaDate(
                prevVarshikaDate = prevVarshikaDate,
                targetTithiNumber = targetTithiNumber,
                targetMasa = mrutaTithi.masa,
                location = location
            )
            val selectedVarshika = result.selectedDay
            val eventPanchanga = result.panchanga
            val ordinal = getOrdinalName(yearIdx)

            val isEkadashi = eventPanchanga.tithi.pakshaTithiNumber == 11
            val ritualDate = if (isEkadashi) selectedVarshika.date.plusDays(1) else selectedVarshika.date
            val ritualKala = if (isEkadashi) DinmanaCalculator.calculateDayKala(ritualDate, location) else selectedVarshika.kalaDetails
            val ritualSunriseZdt = ZonedDateTime.of(ritualDate, ritualKala.sunrise, zoneId)
            val ritualSunrisePanchanga = if (isEkadashi) MasaCalculator.getFullPanchangaTithi(ritualSunriseZdt) else result.sunrisePanchanga
            val ritualAparahnaZdt = ZonedDateTime.of(ritualDate, ritualKala.aparahnaStart, zoneId)
            val ritualAparahnaPanchanga = if (isEkadashi) MasaCalculator.getFullPanchangaTithi(ritualAparahnaZdt) else eventPanchanga

            val varshikaEvent = ShraddhaEvent(
                sequenceNumber = 1,
                type = ShraddhaType.VARSHIKA,
                traditionalName = "Yearly Shraddha — $ordinal Varshika Shraddha",
                gregorianDate = ritualDate,
                dayOfWeek = ritualDate.dayOfWeek.name,
                tithi = ritualAparahnaPanchanga,
                kalaDetails = ritualKala,
                explanation = if (isEkadashi) {
                    "Ekadashi demise tithi detected on ${selectedVarshika.date}. Per Shastras (Padma Purana / Nirnaya Sindhu), Anna-Shraddha ritual is observed on Dvadashi ($ritualDate). ${selectedVarshika.evaluationReason}"
                } else {
                    "Annual death anniversary according to Sri Raghavendra Swamy Mutt Panchanga in ${eventPanchanga.masaDisplayName}. ${selectedVarshika.evaluationReason}"
                },
                observanceCategory = ObservanceCategory.VARSHIKA_SHRADDHA,
                sunrisePanchanga = ritualSunrisePanchanga,
                isEkadashiShifted = isEkadashi,
                ekadashiDate = if (isEkadashi) selectedVarshika.date else null
            )

            val pakshaEvent = calculateMahalayaPaksha(
                year = selectedVarshika.date.year,
                mrutaTithi = mrutaTithi,
                location = location,
                zoneId = zoneId
            )

            val startYear = if (yearIdx == 2) year1PrathamaDate.year else year1PrathamaDate.year + (yearIdx - 2)
            val endYear = selectedVarshika.date.year

            val isCurrentOrUpcoming = !selectedVarshika.date.isBefore(currentDate) ||
                    (pakshaEvent != null && !pakshaEvent.gregorianDate.isBefore(currentDate))

            groups.add(
                YearlyObservanceGroup(
                    yearIndex = yearIdx,
                    yearTitle = "Year $yearIdx ($startYear - $endYear)",
                    samvatsaraName = "${eventPanchanga.samvatsara} Nama Samvatsara",
                    isExpandedByDefault = isCurrentOrUpcoming,
                    masikas = emptyList(),
                    varshikaEvent = varshikaEvent,
                    pakshaEvent = pakshaEvent
                )
            )

            prevVarshikaDate = selectedVarshika.date

            if (selectedVarshika.date.isAfter(currentDate.plusMonths(8))) {
                break
            }
        }

        return groups
    }

    override fun evaluateDosha(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi
    ): DoshaEvaluationResult {
        val zoneId = ZoneId.of(record.location.timezoneId)
        val deathZdt = ZonedDateTime.of(record.deathDate, record.deathTime, zoneId)
        return DoshaDetector.evaluate(deathZdt, mrutaTithi)
    }

    private fun calculateMahalayaPaksha(
        year: Int,
        mrutaTithi: PanchangaTithi,
        location: GeoLocation,
        zoneId: ZoneId
    ): ShraddhaEvent? {
        return BhadrapadaFinder.calculateMahalayaPakshaEvent(year, mrutaTithi, location, zoneId)
    }

    private fun getOrdinalName(yearIdx: Int): String {
        val titles = listOf(
            "Prathama", "Dvitiya", "Tritiya", "Chaturtha", "Panchama",
            "Shashtha", "Saptama", "Ashtama", "Navama", "Dashama",
            "Ekadasha", "Dvadasha", "Trayodasha", "Chaturdasha", "Panchadasha"
        )
        return if (yearIdx <= titles.size) titles[yearIdx - 1] else "Year $yearIdx"
    }
}

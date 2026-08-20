package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Main coordinator for calculating Shraddha dates based on Uttaradimatha Panchanga.
 */
object ShraddhaCalculator {

    fun calculate(
        personRecord: PersonDeathRecord,
        currentDate: LocalDate = LocalDate.now()
    ): ShraddhaCalculationResult {
        val zoneId = ZoneId.of(personRecord.location.timezoneId)

        // 1. Calculate the exact astronomical Panchanga at the precise moment of death
        val deathZdt = ZonedDateTime.of(personRecord.deathDate, personRecord.deathTime, zoneId)
        val mrutaPanchanga = MasaCalculator.getFullPanchangaTithi(deathZdt)

        // 2. Calculate Year 1 events (16 Shodasha rites + Prathama Varshika)
        val year1Events = MasikaShraddhaCalculator.calculateYear1Events(personRecord, mrutaPanchanga)
        val prathamaVarshikaEvent = year1Events.last { it.type == ShraddhaType.VARSHIKA }
        val prathamaVarshikaDate = prathamaVarshikaEvent.gregorianDate

        // 3. Determine if first year is already completed based on actual Panchanga Shraddha date
        val isFirstYearCompleted = currentDate.isAfter(prathamaVarshikaDate)

        return if (isFirstYearCompleted) {
            // Death is older than 1 year -> Show only the next upcoming applicable Shraddha
            val nextUpcoming = VarshikaShraddhaCalculator.findNextUpcomingShraddha(
                personRecord = personRecord,
                mrutaPanchanga = mrutaPanchanga,
                currentDate = currentDate
            )

            ShraddhaCalculationResult(
                personRecord = personRecord,
                mrutaTithi = mrutaPanchanga,
                isDeathOlderThanOneYear = true,
                nextUpcomingShraddha = nextUpcoming,
                yearlySections = emptyList()
            )
        } else {
            // Recent death (within 1st year) -> Show 5-year drilldown
            val year1StartYear = personRecord.deathDate.year
            val year1EndYear = prathamaVarshikaDate.year

            val year1Section = ShraddhaYearSection(
                yearIndex = 1,
                yearTitle = "Year 1 ($year1StartYear - $year1EndYear)",
                isExpandedByDefault = true,
                events = year1Events
            )

            val futureYearSections = VarshikaShraddhaCalculator.calculateFutureYears(
                personRecord = personRecord,
                mrutaPanchanga = mrutaPanchanga,
                year1VarshikaDate = prathamaVarshikaDate
            )

            val allYearSections = listOf(year1Section) + futureYearSections

            ShraddhaCalculationResult(
                personRecord = personRecord,
                mrutaTithi = mrutaPanchanga,
                isDeathOlderThanOneYear = false,
                nextUpcomingShraddha = null,
                yearlySections = allYearSections
            )
        }
    }
}

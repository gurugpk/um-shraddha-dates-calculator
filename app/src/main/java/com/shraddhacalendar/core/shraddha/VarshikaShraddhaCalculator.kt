package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.panchang.MasaCalculator
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale

/**
 * Calculates Annual Varshika Shraddhas for Years 2 through 5 and finds the next upcoming Shraddha.
 */
object VarshikaShraddhaCalculator {

    private val ORDINAL_TITLES = listOf(
        "Prathama", "Dvitiya", "Tritiya", "Chaturtha", "Panchama",
        "Shashtha", "Saptama", "Ashtama", "Navama", "Dashama"
    )

    /**
     * Calculates Yearly Shraddhas for Years 2..5.
     */
    fun calculateFutureYears(
        personRecord: PersonDeathRecord,
        mrutaPanchanga: PanchangaTithi,
        year1VarshikaDate: LocalDate
    ): List<ShraddhaYearSection> {
        val location = personRecord.location
        val zoneId = ZoneId.of(location.timezoneId)
        val targetTithiNumber = mrutaPanchanga.tithi.number

        val yearSections = mutableListOf<ShraddhaYearSection>()
        var prevVarshikaDate = year1VarshikaDate

        for (yearIdx in 2..5) {
            // Approx 1 lunar year (354 days) from previous Varshika
            val approxDate = prevVarshikaDate.plusDays(354)

            val selected = AparahnaVyaptiEngine.findShraddhaDate(
                targetTithiNumber = targetTithiNumber,
                approximateDate = approxDate,
                location = location,
                searchWindowDays = 20
            )

            val eventZdt = ZonedDateTime.of(selected.date, selected.kalaDetails.aparahnaStart, zoneId)
            val eventPanchanga = MasaCalculator.getFullPanchangaTithi(eventZdt)

            val titleOrdinal = if (yearIdx <= ORDINAL_TITLES.size) ORDINAL_TITLES[yearIdx - 1] else "Year $yearIdx"
            val event = ShraddhaEvent(
                sequenceNumber = 1,
                type = ShraddhaType.VARSHIKA,
                traditionalName = "Yearly Shraddha — $titleOrdinal Varshika Shraddha",
                gregorianDate = selected.date,
                dayOfWeek = selected.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                tithi = eventPanchanga,
                kalaDetails = selected.kalaDetails,
                explanation = "Annual death anniversary calculated based on Aparahna Vyapti in Nija ${eventPanchanga.masaDisplayName}. ${selected.evaluationReason}"
            )

            val startYear = selected.date.year
            val endYear = startYear + 1
            yearSections.add(
                ShraddhaYearSection(
                    yearIndex = yearIdx,
                    yearTitle = "Year $yearIdx ($startYear - $endYear)",
                    isExpandedByDefault = false,
                    events = listOf(event)
                )
            )

            prevVarshikaDate = selected.date
        }

        return yearSections
    }

    /**
     * For deaths older than 1 year: finds the immediate next upcoming Varshika Shraddha.
     */
    fun findNextUpcomingShraddha(
        personRecord: PersonDeathRecord,
        mrutaPanchanga: PanchangaTithi,
        currentDate: LocalDate = LocalDate.now()
    ): ShraddhaEvent {
        val location = personRecord.location
        val zoneId = ZoneId.of(location.timezoneId)
        val targetTithiNumber = mrutaPanchanga.tithi.number

        // Search in the current year and next year for the next upcoming event
        var searchDate = currentDate.minusDays(15)
        var candidate: ShraddhaEvent? = null

        for (i in 0 until 14) {
            val selected = AparahnaVyaptiEngine.findShraddhaDate(
                targetTithiNumber = targetTithiNumber,
                approximateDate = searchDate,
                location = location,
                searchWindowDays = 5
            )

            val eventZdt = ZonedDateTime.of(selected.date, selected.kalaDetails.aparahnaStart, zoneId)
            val eventPanchanga = MasaCalculator.getFullPanchangaTithi(eventZdt)

            // Make sure it matches the target lunar month (Nija masa)
            if (eventPanchanga.masa == mrutaPanchanga.masa && !eventPanchanga.isAdhikaMasa) {
                if (!selected.date.isBefore(currentDate)) {
                    val yearsElapsed = selected.date.year - personRecord.deathDate.year
                    val ordinal = if (yearsElapsed in 1..ORDINAL_TITLES.size) ORDINAL_TITLES[yearsElapsed - 1] else "Year $yearsElapsed"

                    candidate = ShraddhaEvent(
                        sequenceNumber = 1,
                        type = ShraddhaType.VARSHIKA,
                        traditionalName = "Yearly Shraddha ($ordinal Varshika)",
                        gregorianDate = selected.date,
                        dayOfWeek = selected.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                        tithi = eventPanchanga,
                        kalaDetails = selected.kalaDetails,
                        explanation = "Upcoming annual death anniversary in Nija ${eventPanchanga.masaDisplayName}. ${selected.evaluationReason}"
                    )
                    break
                }
            }
            searchDate = searchDate.plusDays(30)
        }

        return candidate ?: run {
            // Fallback: 1 year from now
            val fallbackDate = currentDate.plusYears(1)
            val selected = AparahnaVyaptiEngine.findShraddhaDate(targetTithiNumber, fallbackDate, location)
            val eventZdt = ZonedDateTime.of(selected.date, selected.kalaDetails.aparahnaStart, zoneId)
            val eventPanchanga = MasaCalculator.getFullPanchangaTithi(eventZdt)
            ShraddhaEvent(
                sequenceNumber = 1,
                type = ShraddhaType.VARSHIKA,
                traditionalName = "Yearly Shraddha (Upcoming)",
                gregorianDate = selected.date,
                dayOfWeek = selected.date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                tithi = eventPanchanga,
                kalaDetails = selected.kalaDetails,
                explanation = "Calculated based on Aparahna Vyapti in Nija ${eventPanchanga.masaDisplayName}"
            )
        }
    }
}

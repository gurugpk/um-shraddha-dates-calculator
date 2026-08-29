package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.*
import com.shraddhacalendar.core.tradition.TraditionEngineFactory
import java.time.LocalDate

/**
 * Main coordinator for calculating Shraddha and Paksha dates across all Madhwa traditions.
 */
object ShraddhaCalculator {

    fun calculate(
        personRecord: PersonDeathRecord,
        currentDate: LocalDate = LocalDate.now()
    ): ShraddhaCalculationResult {
        val tradition = personRecord.tradition
        val engine = TraditionEngineFactory.getEngine(tradition)

        // If Person is Missing / Demise Unconfirmed, do NOT generate speculative Shraddha calendar
        if (personRecord.isMissingUnconfirmed) {
            val dummyTithiInfo = TithiInfo.fromNumber(1)
            val dummyPanchanga = PanchangaTithi(
                tithi = dummyTithiInfo,
                masa = LunarMonth.CHAITRA,
                isAdhikaMasa = false,
                samvatsara = "N/A"
            )
            val missingGuidance = MissingPersonGuidanceRepository.getGuidance(
                ageAtDisappearance = personRecord.ageAtDisappearance,
                lastSeenDate = personRecord.lastSeenDate,
                language = com.shraddhacalendar.core.localization.AppLanguage.ENGLISH,
                tradition = tradition
            )
            return ShraddhaCalculationResult(
                personRecord = personRecord,
                mrutaTithi = dummyPanchanga,
                isDeathOlderThanOneYear = false,
                nextUpcomingShraddha = null,
                yearlySections = emptyList(),
                yearlyObservanceGroups = emptyList(),
                nextUpcomingObservance = null,
                nextUpcomingCategory = null,
                doshaEvaluation = DoshaEvaluationResult(false, emptyList(), "Missing person: No dosha evaluation applicable."),
                tradition = tradition,
                circumstanceGuidance = null,
                missingPersonGuidance = missingGuidance
            )
        }

        // 1. Calculate astronomical Mruta Panchanga Tithi at moment of death
        val mrutaPanchanga = engine.calculateMrutaTithi(
            deathDate = personRecord.deathDate,
            deathTime = personRecord.deathTime,
            location = personRecord.location
        )

        // 2. Evaluate classical Shastric Doshas
        val doshaResult = engine.evaluateDosha(personRecord, mrutaPanchanga)

        // 3. Calculate full chronological groups (Year 1 Masikas, Varshikas, and Mahalaya Pakshas)
        val observanceGroups = engine.calculateYearlyObservanceGroups(
            record = personRecord,
            mrutaTithi = mrutaPanchanga,
            currentDate = currentDate
        )

        val year1Group = observanceGroups.first()
        val prathamaVarshikaDate = year1Group.varshikaEvent.gregorianDate
        val isDeathOlderThanOneYear = currentDate.isAfter(prathamaVarshikaDate)

        // 4. Find the immediate next upcoming observance (whichever is earliest on or after currentDate)
        val allEventsChronological = mutableListOf<ShraddhaEvent>()
        observanceGroups.forEach { group ->
            allEventsChronological.addAll(group.masikas)
            allEventsChronological.add(group.varshikaEvent)
            if (group.pakshaEvent != null) {
                allEventsChronological.add(group.pakshaEvent)
            }
        }
        allEventsChronological.sortBy { it.gregorianDate }

        val nextUpcomingObservance = allEventsChronological.firstOrNull { !it.gregorianDate.isBefore(currentDate) }
            ?: allEventsChronological.lastOrNull()

        // Also identify the next upcoming annual Varshika Shraddha specifically
        val nextUpcomingVarshika = allEventsChronological
            .filter { it.type == ShraddhaType.VARSHIKA && !it.gregorianDate.isBefore(currentDate) }
            .minByOrNull { it.gregorianDate } ?: year1Group.varshikaEvent

        // Build legacy yearlySections for backward compatibility
        val legacyYearSections = observanceGroups.map { grp ->
            val eventsList = if (grp.yearIndex == 1) {
                grp.masikas + listOf(grp.varshikaEvent)
            } else {
                listOfNotNull(grp.varshikaEvent, grp.pakshaEvent)
            }
            ShraddhaYearSection(
                yearIndex = grp.yearIndex,
                yearTitle = grp.yearTitle,
                isExpandedByDefault = grp.isExpandedByDefault,
                events = eventsList
            )
        }

        val circumstanceGuidance = if (personRecord.demiseCircumstance != DemiseCircumstance.NATURAL) {
            ShastricCircumstanceRepository.getGuidance(
                circumstance = personRecord.demiseCircumstance,
                language = com.shraddhacalendar.core.localization.AppLanguage.ENGLISH,
                tradition = tradition
            )
        } else null

        return ShraddhaCalculationResult(
            personRecord = personRecord,
            mrutaTithi = mrutaPanchanga,
            isDeathOlderThanOneYear = isDeathOlderThanOneYear,
            nextUpcomingShraddha = nextUpcomingVarshika,
            yearlySections = legacyYearSections,
            yearlyObservanceGroups = observanceGroups,
            nextUpcomingObservance = nextUpcomingObservance,
            nextUpcomingCategory = nextUpcomingObservance?.observanceCategory,
            doshaEvaluation = doshaResult,
            tradition = tradition,
            circumstanceGuidance = circumstanceGuidance,
            missingPersonGuidance = null
        )
    }
}

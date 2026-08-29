package com.shraddhacalendar.core.tradition

import com.shraddhacalendar.core.models.*
import java.time.LocalDate
import java.time.LocalTime

/**
 * Pluggable tradition-aware Shraddha and Paksha calculation engine interface.
 */
interface IShraddhaTraditionEngine {
    val tradition: MadhwaTradition

    fun calculateMrutaTithi(
        deathDate: LocalDate,
        deathTime: LocalTime,
        location: GeoLocation
    ): PanchangaTithi

    fun calculateShodashaMasikas(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi
    ): List<ShraddhaEvent>

    fun calculateYearlyObservanceGroups(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi,
        currentDate: LocalDate = LocalDate.now()
    ): List<YearlyObservanceGroup>

    fun evaluateDosha(
        record: PersonDeathRecord,
        mrutaTithi: PanchangaTithi
    ): DoshaEvaluationResult
}

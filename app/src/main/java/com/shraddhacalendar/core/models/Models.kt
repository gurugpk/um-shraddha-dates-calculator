package com.shraddhacalendar.core.models

import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

data class GeoLocation(
    val city: String,
    val state: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezoneId: String
) {
    val displayName: String
        get() = if (state.isNotBlank() && state != city) "$city, $state, $country" else "$city, $country"
}

data class PersonDeathRecord(
    val id: Long = 0,
    val name: String,
    val deathDate: LocalDate = LocalDate.now(),
    val deathTime: LocalTime = LocalTime.NOON, // Mandatory: exact time of death (for confirmed demise)
    val location: GeoLocation,
    val relationship: FamilyRelationship = FamilyRelationship.OTHER,
    val tradition: MadhwaTradition = MadhwaTradition.UTTARADI_MATHA,
    val notes: String = "",
    val demiseStatus: PersonDemiseStatus = PersonDemiseStatus.CONFIRMED_DEMISE,
    val demiseCircumstance: DemiseCircumstance = DemiseCircumstance.NATURAL,
    val lastSeenDate: LocalDate? = null,
    val ageAtDisappearance: Int? = null
) {
    val isMissingUnconfirmed: Boolean
        get() = demiseStatus == PersonDemiseStatus.MISSING_UNCONFIRMED
}

enum class Paksha(val displayName: String) {
    SHUKLA("Shukla Paksha"),
    KRISHNA("Krishna Paksha");

    override fun toString(): String = displayName
}

enum class LunarMonth(val id: Int, val traditionalName: String) {
    CHAITRA(1, "Chaitra"),
    VAISHAKHA(2, "Vaishakha"),
    JYESHTHA(3, "Jyeshtha"),
    ASHADHA(4, "Ashadha"),
    SHRAVANA(5, "Shravana"),
    BHADRAPADA(6, "Bhadrapada"),
    ASHVINA(7, "Ashvina"),
    KARTIKA(8, "Kartika"),
    MARGASHIRSHA(9, "Margashirsha"),
    PUSHYA(10, "Pushya"),
    MAGHA(11, "Magha"),
    PHALGUNA(12, "Phalguna");

    fun next(): LunarMonth {
        val nextId = if (id == 12) 1 else id + 1
        return entries.first { it.id == nextId }
    }

    override fun toString(): String = traditionalName
}

data class TithiInfo(
    val number: Int, // 1..30 (1..15 Shukla, 16..30 Krishna)
    val name: String,
    val paksha: Paksha,
    val pakshaTithiNumber: Int // 1..15
) {
    val displayName: String
        get() = "$name (${paksha.displayName})"

    companion object {
        private val TITHI_NAMES = listOf(
            "Prathama", "Dwitiya", "Tritiya", "Chaturthi", "Panchami",
            "Shashthi", "Saptami", "Ashtami", "Navami", "Dashami",
            "Ekadashi", "Dvadashi", "Trayodashi", "Chaturdashi", "Purnima"
        )

        fun fromNumber(tithiNumber: Int): TithiInfo {
            val normalized = ((tithiNumber - 1) % 30 + 30) % 30 + 1
            val isShukla = normalized <= 15
            val paksha = if (isShukla) Paksha.SHUKLA else Paksha.KRISHNA
            val pakshaIndex = if (isShukla) normalized else normalized - 15
            val name = if (normalized == 30) "Amavasya" else TITHI_NAMES[pakshaIndex - 1]
            return TithiInfo(
                number = normalized,
                name = name,
                paksha = paksha,
                pakshaTithiNumber = pakshaIndex
            )
        }
    }
}

data class PanchangaTithi(
    val tithi: TithiInfo,
    val masa: LunarMonth,
    val isAdhikaMasa: Boolean,
    val samvatsara: String
) {
    val masaDisplayName: String
        get() = if (isAdhikaMasa) "Adhika $masa" else "Nija $masa"

    val fullDescription: String
        get() = "$samvatsara Nama Samvatsara, $masaDisplayName, ${tithi.paksha.displayName}, ${tithi.name}"
}

data class DayKalaDetails(
    val date: LocalDate,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val dinmanaMinutes: Long,
    val aparahnaStart: LocalTime,
    val aparahnaEnd: LocalTime,
    val kutapaStart: LocalTime,
    val kutapaEnd: LocalTime
)

enum class ShraddhaType {
    MASIKA,
    UNA_RITE,
    VARSHIKA,
    MAHALAYA_PAKSHA
}

data class ShraddhaEvent(
    val sequenceNumber: Int,
    val type: ShraddhaType,
    val traditionalName: String, // e.g. "Masika 1 — Adya Masika", "Masika 2 — Unmasika"
    val gregorianDate: LocalDate,
    val dayOfWeek: String,
    val tithi: PanchangaTithi, // Aparahna / Ritual Panchanga
    val kalaDetails: DayKalaDetails,
    val explanation: String, // Trace explanation for transparency
    val observanceCategory: ObservanceCategory = when (type) {
        ShraddhaType.MASIKA, ShraddhaType.UNA_RITE -> ObservanceCategory.MASIKA
        ShraddhaType.VARSHIKA -> ObservanceCategory.VARSHIKA_SHRADDHA
        ShraddhaType.MAHALAYA_PAKSHA -> ObservanceCategory.MAHALAYA_PAKSHA
    },
    val sunrisePanchanga: PanchangaTithi = tithi, // Prevailing Panchanga at Sunrise of the civil date
    val isEkadashiShifted: Boolean = false, // True if ritual date was shifted from Ekadashi to Dvadashi
    val ekadashiDate: LocalDate? = null // Original astronomical Ekadashi date if shifted
) {
    val isEkadashiObservance: Boolean
        get() = isEkadashiShifted || tithi.tithi.pakshaTithiNumber == 11 || sunrisePanchanga.tithi.pakshaTithiNumber == 11

    val dvadashiDate: LocalDate
        get() = if (isEkadashiShifted) gregorianDate else gregorianDate.plusDays(1)

    val isSunriseDifferentFromRitual: Boolean
        get() = sunrisePanchanga.tithi.number != tithi.tithi.number
}


data class ShraddhaYearSection(
    val yearIndex: Int, // 1..5
    val yearTitle: String, // e.g. "Year 1 (2026 - 2027)"
    val isExpandedByDefault: Boolean,
    val events: List<ShraddhaEvent>
)

data class ShraddhaCalculationResult(
    val personRecord: PersonDeathRecord,
    val mrutaTithi: PanchangaTithi,
    val isDeathOlderThanOneYear: Boolean,
    val nextUpcomingShraddha: ShraddhaEvent?,
    val yearlySections: List<ShraddhaYearSection> = emptyList(),
    val yearlyObservanceGroups: List<YearlyObservanceGroup> = emptyList(),
    val nextUpcomingObservance: ShraddhaEvent? = nextUpcomingShraddha,
    val nextUpcomingCategory: ObservanceCategory? = nextUpcomingShraddha?.observanceCategory,
    val doshaEvaluation: DoshaEvaluationResult = DoshaEvaluationResult(false, emptyList(), "No exceptional dosha detected."),
    val tradition: MadhwaTradition = personRecord.tradition,
    val circumstanceGuidance: CircumstanceGuidance? = null,
    val missingPersonGuidance: MissingPersonGuidance? = null
)

package com.shraddhacalendar.core.models

import java.time.LocalDate

/**
 * Supported Madhwa traditions for tradition-aware calculation and invocations.
 */
enum class MadhwaTradition(
    val id: String,
    val displayNameEnglish: String,
    val invocationHeaderSanskrit: String,
    val guruParamparaName: String,
    val defaultCityName: String
) {
    UTTARADI_MATHA(
        id = "uttaradi_matha",
        displayNameEnglish = "Sri Uttaradi Matha",
        invocationHeaderSanskrit = "श्री सत्यप्रमोदतीर्थकरोत्सङ्गोत्थसद्व्रतम् । सत्यप्रियसदार्च्यं तं सत्यात्मयतिमाश्रये ॥\nश्री सत्यस्मृतिविहितं मध्वश्राद्धतिथिमणकम्",
        guruParamparaName = "Sri 108 Uttaradi Matha Parampara",
        defaultCityName = "Bengaluru"
    ),
    MANTRALAYA_MUTT(
        id = "mantralaya_mutt",
        displayNameEnglish = "Mantralaya (Sri Raghavendra Swamy Mutt)",
        invocationHeaderSanskrit = "पूज्याय राघवेंद्राय सत्यधर्मरताय च । भजतां कल्पवृक्षाय नमतां कामधेनवे ॥\nश्री राघवेन्द्रगुरुसार्वभौम अनुग्रहभावनम्",
        guruParamparaName = "Sri 108 Raghavendra Swamy Mutt Parampara (Mantralayam)",
        defaultCityName = "Mantralayam"
    ),
    UDUPI_ASHTA_MATHA(
        id = "udupi_ashta_matha",
        displayNameEnglish = "Udupi Ashta Mathas",
        invocationHeaderSanskrit = "नमो भगवते तस्मै विष्णवे प्रभविष्णवे । यस्य संस्मरणादेव सर्वसिद्धिरनुत्तमा ॥\nश्रीमदानन्दतीर्थभगवत्पाद परम्परा",
        guruParamparaName = "Sri Udupi Ashta Matha Parampara",
        defaultCityName = "Udupi"
    );

    companion object {
        fun fromId(id: String?): MadhwaTradition {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: UTTARADI_MATHA
        }
    }
}

/**
 * User's relationship with the departed person.
 */
enum class FamilyRelationship(val id: String, val displayNameEnglish: String) {
    FATHER("father", "Father"),
    MOTHER("mother", "Mother"),
    GRANDFATHER("grandfather", "Grandfather"),
    GRANDMOTHER("grandmother", "Grandmother"),
    HUSBAND("husband", "Husband"),
    WIFE("wife", "Wife"),
    BROTHER("brother", "Brother"),
    SISTER("sister", "Sister"),
    SON("son", "Son"),
    DAUGHTER("daughter", "Daughter"),
    UNCLE("uncle", "Uncle"),
    AUNT("aunt", "Aunt"),
    OTHER("other", "Other / Relative");

    companion object {
        fun fromId(id: String?): FamilyRelationship {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: OTHER
        }
    }
}

/**
 * Classification of observances.
 */
enum class ObservanceCategory {
    MASIKA,             // 16 Shodasha Masikas in Year 1
    VARSHIKA_SHRADDHA,  // Annual Death Anniversary
    MAHALAYA_PAKSHA     // Pitru Paksha Shraddha (Bhadrapada Krishna)
}

/**
 * Types of classical death-related doshas / special considerations.
 */
enum class DoshaType {
    NONE,
    DHANISHTA_PANCHAKA,
    TRI_PUSHKARA_YOGA,
    DVI_PUSHKARA_YOGA,
    NAKSHATRA_GANDANTA,
    TITHI_GANDANTA,
    SANKRANTI_MRUTYU,
    GRAHANA_MRUTYU
}

/**
 * Details of a detected dosha/traditional consideration.
 */
data class DoshaRecord(
    val type: DoshaType,
    val title: String,
    val conditionDescription: String,
    val significance: String,
    val prescribedRemedy: String,
    val scripturalSource: String,
    val isSevere: Boolean
)

/**
 * Complete dosha evaluation output.
 */
data class DoshaEvaluationResult(
    val hasDosha: Boolean,
    val doshas: List<DoshaRecord>,
    val generalAdvice: String
)

/**
 * Educational & scriptural information for a ceremony.
 */
data class EducationalCeremonyInfo(
    val ceremonyKey: String,
    val titleEnglish: String,
    val titleSanskrit: String,
    val dayTiming: String,
    val soulJourneyStation: String,
    val stationDescription: String,
    val pretaConditionAndYatanaDeha: String = "",
    val pindaSignificanceAndRelief: String = "",
    val spiritualSignificance: String,
    val whyNeeded: String,
    val scripturalCitation: String,
    val classicalVerse: String? = null
)

/**
 * Year-by-year grouped observances containing Shraddha and Mahalaya Paksha.
 */
data class YearlyObservanceGroup(
    val yearIndex: Int,          // 1, 2, 3...
    val yearTitle: String,         // e.g. "Year 1 (2020 - 2021)"
    val samvatsaraName: String,    // e.g. "Sharvari Nama Samvatsara"
    val isExpandedByDefault: Boolean,
    val masikas: List<ShraddhaEvent>, // All 16 Shodasha Masikas (Only present in Year 1)
    val varshikaEvent: ShraddhaEvent, // Annual Shraddha event
    val pakshaEvent: ShraddhaEvent?,  // Mahalaya Paksha event (null in Year 1 = Not Applicable)
    val pakshaNotApplicableReason: String? = null // e.g. "Not Applicable in Year 1 (Preta Avastha prior to Sapindikarana)"
)

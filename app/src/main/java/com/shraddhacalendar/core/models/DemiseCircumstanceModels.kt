package com.shraddhacalendar.core.models

import java.time.LocalDate

enum class PersonDemiseStatus(val id: String) {
    CONFIRMED_DEMISE("confirmed_demise"),
    MISSING_UNCONFIRMED("missing_unconfirmed")
}

enum class DemiseCircumstance(
    val id: String,
    val sanskritName: String,
    val isUnnatural: Boolean
) {
    NATURAL("natural", "प्राकृतमरणम्", false),
    SNAKEBITE("snakebite", "सर्पदष्टमरणम्", true),
    DROWNING("drowning", "जले मज्जनमरणम्", true),
    FIRE_BURNS("fire_burns", "अग्निदग्धमरणम्", true),
    LIGHTNING("lightning", "विद्युत् / वज्रपातमरणम्", true),
    TRAUMA_ACCIDENT("trauma_accident", "शस्त्र / आयुधहतमरणम्", true),
    POISONING("poisoning", "विषभक्षणमरणम्", true),
    FALL_HEIGHT("fall_height", "वृक्ष / गिरिपातनेन मरणम्", true),
    ANIMAL_ATTACK("animal_attack", "श्वपद / शृङ्गिदंष्ट्रिहतमरणम्", true),
    SELF_INFLICTED("self_inflicted", "आत्मघात / उद्वन्धनमरणम्", true),
    UNRECOVERED_BODY("unrecovered_body", "नष्टशरीर / अदेहदाहमरणम्", true),
    PREGNANCY_CHILDBIRTH("pregnancy_childbirth", "गर्भिणी / प्रसूतिकामरणम्", true),
    OTHER_DURMARANA("other_durmarana", "अन्यथा दुर्मरणम्", true);

    companion object {
        fun fromId(id: String?): DemiseCircumstance {
            return entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: NATURAL
        }
    }
}

data class ScripturalSourceReference(
    val primaryText: String,          // e.g. "Dharmasindhu", "Garuda Purana", "Nirnayasindhu"
    val sectionOrChapter: String,     // e.g. "Tritiya Parichheda, Durmarana Prayaschitta Prakarana"
    val citationVerse: String,        // e.g. "Garuda Purana Preta Khanda Ch. 40, v. 4-12"
    val sanskritText: String,         // Classical Sanskrit shloka / sutra excerpt
    val translation: String           // Explanatory translation
)

data class CircumstanceGuidance(
    val circumstance: DemiseCircumstance,
    val localizedName: String,
    val localizedMeaning: String,
    val sanskritTermLocalScript: String,
    val remedyName: String,
    val remedySanskritLocalScript: String,
    val timingGuidance: String,
    val purposeExplanation: String,
    val scripturalSource: ScripturalSourceReference,
    val traditionNotes: String,
    val isMandatory: Boolean
)

data class MissingPersonWaitingPeriodInfo(
    val ageAtDisappearance: Int?,
    val prescribedWaitingYears: Int,
    val authorityRule: String,
    val elapsedYears: Int?,
    val isPeriodElapsed: Boolean,
    val remainingYears: Int?
)

data class MissingPersonGuidance(
    val title: String,
    val statusSummary: String,
    val whyShraddhaProhibited: String,
    val waitingPeriodInfo: MissingPersonWaitingPeriodInfo,
    val recommendedPrayers: List<String>,
    val postWaitingPeriodProtocol: List<String>,
    val laterConfirmedWorkflow: String,
    val returnAliveRestorationProtocol: String,
    val scripturalSources: List<ScripturalSourceReference>,
    val acharyaConsultationNote: String
)

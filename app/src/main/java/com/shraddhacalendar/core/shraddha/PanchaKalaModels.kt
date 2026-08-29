package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.models.TithiInfo
import java.time.LocalTime

enum class VedicKala(val order: Int) {
    PRATAH(1),
    SANGAVA(2),
    MADHYAHNA(3),
    APARAHNA(4),
    SAYAHNA(5)
}

data class PanchaKalaItem(
    val kala: VedicKala,
    val name: String,
    val divisionLabel: String,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val shlokaNativeScript: String,
    val shlokaTransliteration: String,
    val meaning: String,
    val prescribedDuties: List<String>,
    val prohibitedDuties: List<String>,
    val isSacredAncestralWindow: Boolean = (kala == VedicKala.APARAHNA)
)

data class PujaRationaleItem(
    val key: String,
    val title: String,
    val subtitle: String,
    val description: String
)

data class PujaOptionItem(
    val optionNumber: Int,
    val title: String,
    val ruleSubtitle: String,
    val practicalPractice: String
)

data class KartruDevaPujaGuide(
    val canonicalShlokaNative: String,
    val canonicalShlokaTransliteration: String,
    val shlokaMeaning: String,
    val philosophicalRationaleIntro: String,
    val rationales: List<PujaRationaleItem>,
    val canonicalOptions: List<PujaOptionItem>
)

data class TithiTimingAnalysis(
    val targetTithi: TithiInfo,
    val sunriseTithi: TithiInfo,
    val aparahnaTithi: TithiInfo,
    val sunriseTime: LocalTime,
    val sunsetTime: LocalTime,
    val aparahnaStart: LocalTime,
    val aparahnaEnd: LocalTime,
    val kutapaStart: LocalTime,
    val kutapaEnd: LocalTime,
    val targetOverlapMinutes: Int,
    val isSunriseDifferentFromRitual: Boolean,
    val canonicalProhibitionShloka: String,
    val canonicalProhibitionShlokaTranslit: String,
    val canonicalProhibitionMeaning: String,
    val whyThisDateExplanation: String,
    val whyNotMorningExplanation: String
)

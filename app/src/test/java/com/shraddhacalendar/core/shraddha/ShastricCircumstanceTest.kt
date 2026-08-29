package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.DemiseCircumstance
import com.shraddhacalendar.core.models.MadhwaTradition
import org.junit.Assert.*
import org.junit.Test

class ShastricCircumstanceTest {

    @Test
    fun testAll13CircumstancesAcrossAll5Languages() {
        val languages = AppLanguage.entries

        for (circumstance in DemiseCircumstance.entries) {
            for (lang in languages) {
                val guidance = ShastricCircumstanceRepository.getGuidance(
                    circumstance = circumstance,
                    language = lang,
                    tradition = MadhwaTradition.UTTARADI_MATHA
                )

                assertNotNull("Guidance should not be null for $circumstance in $lang", guidance)
                assertTrue("Localized name should not be blank for $circumstance in $lang", guidance.localizedName.isNotBlank())
                assertTrue("Localized meaning should not be blank for $circumstance in $lang", guidance.localizedMeaning.isNotBlank())
                assertTrue("Sanskrit term should not be blank for $circumstance in $lang", guidance.sanskritTermLocalScript.isNotBlank())
                assertTrue("Remedy name should not be blank for $circumstance in $lang", guidance.remedyName.isNotBlank())
                assertTrue("Remedy Sanskrit should not be blank for $circumstance in $lang", guidance.remedySanskritLocalScript.isNotBlank())
                assertTrue("Timing guidance should not be blank for $circumstance in $lang", guidance.timingGuidance.isNotBlank())
                assertTrue("Purpose explanation should not be blank for $circumstance in $lang", guidance.purposeExplanation.isNotBlank())
                assertTrue("Primary text should not be blank for $circumstance in $lang", guidance.scripturalSource.primaryText.isNotBlank())
                assertTrue("Sanskrit text citation should not be blank for $circumstance in $lang", guidance.scripturalSource.sanskritText.isNotBlank())
            }
        }
    }

    @Test
    fun testSnakebiteSpecificShastricMapping() {
        val guidance = ShastricCircumstanceRepository.getGuidance(
            circumstance = DemiseCircumstance.SNAKEBITE,
            language = AppLanguage.ENGLISH,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        assertTrue(guidance.remedyName.contains("Narayana Bali", ignoreCase = true))
        assertTrue(guidance.remedyName.contains("Nagabali", ignoreCase = true))
        assertTrue(guidance.scripturalSource.primaryText.contains("Garuda Purana", ignoreCase = true))
        assertTrue(guidance.scripturalSource.sanskritText.contains("सर्पदष्टस्य", ignoreCase = true))
    }

    @Test
    fun testUnrecoveredBodyPalashaVidhiMapping() {
        val guidance = ShastricCircumstanceRepository.getGuidance(
            circumstance = DemiseCircumstance.UNRECOVERED_BODY,
            language = AppLanguage.ENGLISH,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        assertTrue(guidance.remedyName.contains("Parṇa-nara-dāha", ignoreCase = true) || guidance.remedyName.contains("Palāśa-vidhi", ignoreCase = true))
        assertTrue(guidance.scripturalSource.primaryText.contains("Baudhayana", ignoreCase = true) || guidance.scripturalSource.primaryText.contains("Dharmasindhu", ignoreCase = true))
        assertTrue(guidance.scripturalSource.sanskritText.contains("३६०", ignoreCase = true) || guidance.scripturalSource.sanskritText.contains("पलाश", ignoreCase = true))
    }

    @Test
    fun testSelfInflictedCompassionateGuidance() {
        val guidanceKn = ShastricCircumstanceRepository.getGuidance(
            circumstance = DemiseCircumstance.SELF_INFLICTED,
            language = AppLanguage.KANNADA,
            tradition = MadhwaTradition.UTTARADI_MATHA
        )

        assertTrue(guidanceKn.remedyName.contains("ನಾರಾಯಣ ಬಲಿ"))
        assertTrue(guidanceKn.scripturalSource.primaryText.contains("ಗರುಡ ಪುರಾಣ") || guidanceKn.scripturalSource.primaryText.contains("ಮನು ಸ್ಮೃತಿ"))
    }
}

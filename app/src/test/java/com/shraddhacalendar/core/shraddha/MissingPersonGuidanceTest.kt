package com.shraddhacalendar.core.shraddha

import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.models.PersonDemiseStatus
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class MissingPersonGuidanceTest {

    @Test
    fun testAgeTieredWaitingPeriods() {
        // Youth < 20 years -> 20 years
        val waitingYouth = MissingPersonGuidanceRepository.calculateWaitingPeriod(
            ageAtDisappearance = 17,
            lastSeenDate = LocalDate.now().minusYears(5)
        )
        assertEquals(20, waitingYouth.prescribedWaitingYears)
        assertEquals(5, waitingYouth.elapsedYears)
        assertEquals(15, waitingYouth.remainingYears)
        assertFalse(waitingYouth.isPeriodElapsed)

        // Adult 20..50 years -> 12 years (Dvadasa-varsha niyamah)
        val waitingAdult = MissingPersonGuidanceRepository.calculateWaitingPeriod(
            ageAtDisappearance = 35,
            lastSeenDate = LocalDate.now().minusYears(14)
        )
        assertEquals(12, waitingAdult.prescribedWaitingYears)
        assertEquals(14, waitingAdult.elapsedYears)
        assertEquals(0, waitingAdult.remainingYears)
        assertTrue(waitingAdult.isPeriodElapsed)

        // Elder > 50 years -> 6 years
        val waitingElder = MissingPersonGuidanceRepository.calculateWaitingPeriod(
            ageAtDisappearance = 68,
            lastSeenDate = LocalDate.now().minusYears(3)
        )
        assertEquals(6, waitingElder.prescribedWaitingYears)
        assertEquals(3, waitingElder.elapsedYears)
        assertEquals(3, waitingElder.remainingYears)
        assertFalse(waitingElder.isPeriodElapsed)

        // Null age -> defaults to 12 years
        val waitingDefault = MissingPersonGuidanceRepository.calculateWaitingPeriod(
            ageAtDisappearance = null,
            lastSeenDate = null
        )
        assertEquals(12, waitingDefault.prescribedWaitingYears)
        assertNull(waitingDefault.elapsedYears)
    }

    @Test
    fun testMissingPersonCalculatorIntegration() {
        val missingRecord = PersonDeathRecord(
            name = "Missing Person Test",
            deathDate = LocalDate.now(),
            deathTime = LocalTime.NOON,
            location = GeoLocation("Bangalore", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata"),
            relationship = FamilyRelationship.FATHER,
            tradition = MadhwaTradition.UTTARADI_MATHA,
            demiseStatus = PersonDemiseStatus.MISSING_UNCONFIRMED,
            lastSeenDate = LocalDate.now().minusYears(4),
            ageAtDisappearance = 42
        )

        val result = ShraddhaCalculator.calculate(missingRecord)

        assertTrue(missingRecord.isMissingUnconfirmed)
        assertNull("Missing person must have NO next upcoming shraddha event", result.nextUpcomingShraddha)
        assertNull("Missing person must have NO next upcoming observance event", result.nextUpcomingObservance)
        assertTrue("Missing person must have empty yearly sections", result.yearlySections.isEmpty())
        assertTrue("Missing person must have empty yearly groups", result.yearlyObservanceGroups.isEmpty())
        assertNotNull("Missing person guidance must be attached", result.missingPersonGuidance)
        assertEquals(12, result.missingPersonGuidance?.waitingPeriodInfo?.prescribedWaitingYears)
    }

    @Test
    fun testMultiLanguageMissingPersonGuidance() {
        for (lang in AppLanguage.entries) {
            val guidance = MissingPersonGuidanceRepository.getGuidance(
                ageAtDisappearance = 30,
                lastSeenDate = LocalDate.now().minusYears(2),
                language = lang,
                tradition = MadhwaTradition.UTTARADI_MATHA
            )

            assertNotNull("Guidance must not be null for $lang", guidance)
            assertTrue("Title must not be blank for $lang", guidance.title.isNotBlank())
            assertTrue("Status summary must not be blank for $lang", guidance.statusSummary.isNotBlank())
            assertTrue("Prohibition reasoning must not be blank for $lang", guidance.whyShraddhaProhibited.isNotBlank())
            assertTrue("Recommended prayers must not be empty for $lang", guidance.recommendedPrayers.isNotEmpty())
            assertTrue("Post-waiting protocol must not be empty for $lang", guidance.postWaitingPeriodProtocol.isNotEmpty())
            assertTrue("Return alive protocol must not be blank for $lang", guidance.returnAliveRestorationProtocol.isNotBlank())
            assertTrue("Acharya consultation note must not be blank for $lang", guidance.acharyaConsultationNote.isNotBlank())
            assertTrue("Scriptural sources must not be empty for $lang", guidance.scripturalSources.isNotEmpty())
        }
    }
}

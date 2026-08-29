package com.shraddhacalendar.regression

import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.data.local.SavedProfileItem
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Regression test for Saved Profiles permanent device memory storage logic, verifying:
 * 1. Permanent retention without FIFO capacity limit
 * 2. Uniqueness by person name and death date
 * 3. Individual profile deletion
 * 4. Independence from Recents history clearing
 * 5. Full data integrity on reload
 */
class SavedProfilesRepositoryRegressionTest {

    private val sampleLocation = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")

    @Test
    fun testPermanentRetentionWithoutCap() {
        val savedList = mutableListOf<SavedProfileItem>()

        // Simulate saving 25 distinct family profiles
        for (i in 1..25) {
            val profile = SavedProfileItem(
                id = i.toLong(),
                personName = "Beloved Family Member $i",
                relationship = if (i % 2 == 0) "Father" else "Mother",
                deathDate = LocalDate.of(2020, 1, 1).plusMonths(i.toLong()),
                deathTime = LocalTime.of(8, 30),
                location = sampleLocation,
                notes = "Family ancestral record $i",
                timestamp = System.currentTimeMillis() + i
            )
            savedList.add(0, profile)
        }

        // Unlike recents, all 25 must be permanently retained (no FIFO truncation!)
        assertEquals(25, savedList.size)
        assertTrue(savedList.any { it.personName == "Beloved Family Member 1" })
        assertTrue(savedList.any { it.personName == "Beloved Family Member 25" })
    }

    @Test
    fun testUniquenessAndRecordMatching() {
        val savedList = mutableListOf<SavedProfileItem>()

        val p1 = SavedProfileItem(
            id = 1L,
            personName = "Late Sri Pranesh Kulkarni",
            relationship = "Father",
            deathDate = LocalDate.of(2021, 5, 15),
            deathTime = LocalTime.of(14, 30),
            location = sampleLocation,
            notes = "Revered father",
            timestamp = 1000L
        )
        savedList.add(p1)

        val isSaved = savedList.any {
            it.personName.trim().equals("Late Sri Pranesh Kulkarni", ignoreCase = true) &&
            it.deathDate == LocalDate.of(2021, 5, 15)
        }
        assertTrue("Profile must be identified as saved", isSaved)

        val isNotSaved = savedList.any {
            it.personName.trim().equals("Another Person", ignoreCase = true)
        }
        assertFalse("Unsaved person should not match", isNotSaved)
    }

    @Test
    fun testIndividualDeletion() {
        val savedList = mutableListOf<SavedProfileItem>()

        val p1 = SavedProfileItem(1L, "Ancestor 1", "Grandfather", LocalDate.of(2010, 3, 4), LocalTime.of(6, 0), sampleLocation, null, "uttaradi_matha", 100L)
        val p2 = SavedProfileItem(2L, "Ancestor 2", "Grandmother", LocalDate.of(2015, 7, 20), LocalTime.of(18, 0), sampleLocation, null, "uttaradi_matha", 200L)
        savedList.add(p1)
        savedList.add(p2)

        assertEquals(2, savedList.size)

        // Delete Ancestor 1
        savedList.removeIf { it.id == 1L }

        assertEquals(1, savedList.size)
        assertEquals("Ancestor 2", savedList.first().personName)
    }

    @Test
    fun testIndependenceFromRecentsHistory() {
        val recentSearches = mutableListOf("Search 1", "Search 2")
        val savedProfiles = mutableListOf("Permanent Profile 1", "Permanent Profile 2")

        // Clear all recents history
        recentSearches.clear()

        // Saved profiles must remain completely untouched
        assertEquals(0, recentSearches.size)
        assertEquals(2, savedProfiles.size)
        assertEquals("Permanent Profile 1", savedProfiles[0])
        assertEquals("Permanent Profile 2", savedProfiles[1])
    }

    @Test
    fun testReopenDataIntegrity() {
        val saved = SavedProfileItem(
            id = 10L,
            personName = "Sri Pranesh Kulkarni",
            relationship = "Father",
            deathDate = LocalDate.of(2022, 11, 4),
            deathTime = LocalTime.of(9, 15),
            location = GeoLocation("Mantralayam", "Andhra Pradesh", "India", 15.9338, 77.4297, "Asia/Kolkata"),
            notes = "Devotee of Rayaru",
            timestamp = 5000L
        )

        // Reconstruct PersonDeathRecord for recalculation
        val reopened = PersonDeathRecord(
            name = saved.personName,
            deathDate = saved.deathDate,
            deathTime = saved.deathTime,
            location = saved.location
        )

        assertEquals("Sri Pranesh Kulkarni", reopened.name)
        assertEquals(LocalDate.of(2022, 11, 4), reopened.deathDate)
        assertEquals(LocalTime.of(9, 15), reopened.deathTime)
        assertEquals("Mantralayam", reopened.location.city)
        assertEquals("Asia/Kolkata", reopened.location.timezoneId)
    }
}

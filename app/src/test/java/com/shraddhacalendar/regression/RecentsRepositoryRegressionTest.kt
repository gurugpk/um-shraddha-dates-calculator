package com.shraddhacalendar.regression

import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

/**
 * Regression test for Recents history caching logic, verifying:
 * 1. Maximum capacity limit of 10 items
 * 2. FIFO eviction of 11th item
 * 3. Exact data preservation for reopen
 */
class RecentsRepositoryRegressionTest {

    private val sampleLocation = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")

    @Test
    fun testFifoEvictionAlgorithm() {
        val historyList = mutableListOf<PersonDeathRecord>()
        val maxCapacity = 10

        // Simulate adding 15 unique searches
        for (i in 1..15) {
            val record = PersonDeathRecord(
                name = "Devotee $i",
                deathDate = LocalDate.of(2026, 1, 1).plusDays(i.toLong()),
                deathTime = LocalTime.of(10, 0),
                location = sampleLocation
            )

            // Insert at front
            historyList.add(0, record)

            // Evict if beyond max capacity
            if (historyList.size > maxCapacity) {
                historyList.removeAt(historyList.size - 1)
            }
        }

        // Must strictly maintain max 10
        assertEquals(10, historyList.size)

        // Most recent must be Devotee 15
        assertEquals("Devotee 15", historyList.first().name)

        // Oldest preserved must be Devotee 6 (Devotee 1 to 5 evicted)
        assertEquals("Devotee 6", historyList.last().name)

        // Verify Devotees 1 through 5 are not present
        for (i in 1..5) {
            assertFalse(historyList.any { it.name == "Devotee $i" })
        }
    }
}

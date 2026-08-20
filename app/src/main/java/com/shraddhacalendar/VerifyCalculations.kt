package com.shraddhacalendar

import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.models.ShraddhaType
import com.shraddhacalendar.core.shraddha.ShraddhaCalculator
import com.shraddhacalendar.data.location.CityDatabase
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun main() {
    println("==========================================================================")
    println("        UTTARADIMATHA PANCHANGA SHRADDHA DATE CALCULATOR VERIFICATION     ")
    println("==========================================================================")

    val blr = CityDatabase.CITIES.first { it.city == "Bengaluru" }
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)")

    // Test Case 1: Recent Death (August 15, 2026 at 14:30 in Bengaluru)
    println("\n>>> TEST CASE 1: Recent Death (< 1 Year) - Sri Ramachandra Rao")
    val deathDate1 = LocalDate.of(2026, 8, 15)
    val deathTime1 = LocalTime.of(14, 30)
    val person1 = PersonDeathRecord(name = "Sri Ramachandra Rao", deathDate = deathDate1, deathTime = deathTime1, location = blr)

    val result1 = ShraddhaCalculator.calculate(person1, currentDate = LocalDate.of(2026, 8, 20))
    println("Calculated Death Panchanga: ${result1.mrutaTithi.fullDescription}")
    println("Location: ${blr.displayName}")
    println("\n--- 5-YEAR DRILLDOWN SCHEDULE ---")

    for (section in result1.yearlySections) {
        println("\n▼ ${section.yearTitle} (${section.events.size} Ceremonies):")
        for (event in section.events) {
            val typeTag = if (event.type == ShraddhaType.VARSHIKA) "[ANNUAL SHRADDHA]" else "[MASIKA]"
            println("   • ${event.traditionalName} $typeTag")
            println("     Date: ${event.gregorianDate.format(formatter)}")
            println("     Panchanga: ${event.tithi.masaDisplayName} | ${event.tithi.tithi.displayName}")
            println("     Aparahna: ${event.kalaDetails.aparahnaStart} - ${event.kalaDetails.aparahnaEnd} | Sunrise: ${event.kalaDetails.sunrise}, Sunset: ${event.kalaDetails.sunset}")
            println("     Trace: ${event.explanation}\n")
        }
    }

    // Test Case 2: Past Death (> 1 Year) - Sri Madhwacharya Devotee (Death: 10 Jan 2024 at 09:15)
    println("==========================================================================")
    println(">>> TEST CASE 2: Death Older Than 1 Year (> 1 Year) - Sri Madhwacharya Devotee")
    val deathDate2 = LocalDate.of(2024, 1, 10)
    val deathTime2 = LocalTime.of(9, 15)
    val person2 = PersonDeathRecord(name = "Sri Madhwacharya Devotee", deathDate = deathDate2, deathTime = deathTime2, location = blr)

    val result2 = ShraddhaCalculator.calculate(person2, currentDate = LocalDate.of(2026, 8, 20))
    println("Calculated Death Panchanga: ${result2.mrutaTithi.fullDescription}")
    println("Death Older Than 1 Year: ${result2.isDeathOlderThanOneYear}")

    val next = result2.nextUpcomingShraddha!!
    println("\n>>> NEXT UPCOMING SHRADDHA <<<")
    println("Type: ${next.traditionalName}")
    println("Date: ${next.gregorianDate.format(formatter)}")
    println("Tithi: ${next.tithi.tithi.displayName}")
    println("Masa: ${next.tithi.masaDisplayName}")
    println("Samvatsara: ${next.tithi.samvatsara} Nama Samvatsara")
    println("Location: ${blr.displayName}")
    println("Aparahna Kala: ${next.kalaDetails.aparahnaStart} - ${next.kalaDetails.aparahnaEnd}")
    println("Explanation: ${next.explanation}")
    println("==========================================================================")
}

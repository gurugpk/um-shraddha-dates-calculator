package com.shraddhacalendar.astro

import com.shraddhacalendar.core.astro.*
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.panchang.DinmanaCalculator
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class AstroCalculationsTest {

    @Test
    fun testJulianDay() {
        // J2000.0 epoch: 2000-01-01 12:00:00 UTC = 2451545.0
        val date = LocalDate.of(2000, 1, 1)
        val time = LocalTime.of(12, 0, 0)
        val zdt = ZonedDateTime.of(date, time, ZoneId.of("UTC"))
        val jd = JulianDay.fromZonedDateTime(zdt)
        assertEquals(2451545.0, jd, 0.0001)

        val dt = JulianDay.toLocalDateTime(jd)
        assertEquals(2000, dt.year)
        assertEquals(1, dt.monthValue)
        assertEquals(1, dt.dayOfMonth)
        assertEquals(12, dt.hour)
    }

    @Test
    fun testSunCoordinatesAndAyanamsha() {
        val jd2026 = JulianDay.fromLocalDate(LocalDate.of(2026, 8, 20))
        val ayanamsha = SunCoordinates.getAyanamsha(jd2026)
        // Lahiri Ayanamsha in 2026 is around 24.23 degrees
        assertTrue("Ayanamsha should be around 24.2 deg, was $ayanamsha", ayanamsha in 24.0..24.5)

        val tropicalSun = SunCoordinates.getTropicalLongitude(jd2026)
        val nirayanaSun = SunCoordinates.getNirayanaLongitude(jd2026)
        assertTrue(tropicalSun in 0.0..360.0)
        assertTrue(nirayanaSun in 0.0..360.0)
    }

    @Test
    fun testSunriseSunsetBengaluru() {
        val date = LocalDate.of(2026, 8, 15)
        val location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        val solarTimes = SunriseSunsetCalculator.calculate(
            date = date,
            latitude = location.latitude,
            longitude = location.longitude,
            zoneId = ZoneId.of(location.timezoneId)
        )

        // Bengaluru sunrise in mid-August is around 06:05 to 06:10 AM IST, sunset around 18:40 to 18:50 PM IST
        assertEquals(6, solarTimes.sunrise.hour)
        assertTrue(solarTimes.sunrise.minute in 0..20)
        assertEquals(18, solarTimes.sunset.hour)
        assertTrue(solarTimes.sunset.minute in 30..55)
        assertTrue(solarTimes.dayLengthMinutes in 720..770)
    }

    @Test
    fun testDinmanaAndAparahnaKala() {
        val date = LocalDate.of(2026, 8, 15)
        val location = GeoLocation("Bengaluru", "Karnataka", "India", 12.9716, 77.5946, "Asia/Kolkata")
        val kala = DinmanaCalculator.calculateDayKala(date, location)

        assertTrue(kala.aparahnaStart.isAfter(kala.sunrise))
        assertTrue(kala.aparahnaEnd.isAfter(kala.aparahnaStart))
        assertTrue(kala.sunset.isAfter(kala.aparahnaEnd))

        // Aparahna should be around 13:30 to 16:15 IST
        assertTrue(kala.aparahnaStart.hour in 13..14)
        assertTrue(kala.aparahnaEnd.hour in 15..16)

        // Kutapa should fall in Madhyahna / around 11:45 to 12:45 IST
        assertTrue(kala.kutapaStart.hour in 11..12)
    }
}

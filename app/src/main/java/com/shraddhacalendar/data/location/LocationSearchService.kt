package com.shraddhacalendar.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.shraddhacalendar.core.models.GeoLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale
import java.util.TimeZone

class LocationSearchService(private val context: Context) {

    suspend fun searchLocations(query: String): List<GeoLocation> = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            return@withContext CityDatabase.CITIES.take(20)
        }

        // 1. Search offline pre-bundled cities first
        val localMatches = CityDatabase.search(trimmed)
        if (localMatches.size >= 5) {
            return@withContext localMatches
        }

        // 2. Fallback to Android Geocoder for global world cities
        val onlineMatches = mutableListOf<GeoLocation>()
        if (Geocoder.isPresent()) {
            try {
                val geocoder = Geocoder(context, Locale.getDefault())
                @Suppress("DEPRECATION")
                val addresses: List<Address>? = geocoder.getFromLocationName(trimmed, 10)

                addresses?.forEach { addr ->
                    val city = addr.locality ?: addr.subAdminArea ?: addr.featureName ?: trimmed
                    val state = addr.adminArea ?: ""
                    val country = addr.countryName ?: ""
                    val lat = addr.latitude
                    val lon = addr.longitude

                    // Infer timezone ID
                    val tz = findTimezoneForCoords(lat, lon, addr.countryCode)

                    val geo = GeoLocation(
                        city = city,
                        state = state,
                        country = country,
                        latitude = lat,
                        longitude = lon,
                        timezoneId = tz
                    )

                    if (!localMatches.any { it.city.equals(city, ignoreCase = true) && it.country.equals(country, ignoreCase = true) }) {
                        onlineMatches.add(geo)
                    }
                }
            } catch (_: Exception) {
                // If offline / network error
            }
        }

        (localMatches + onlineMatches).distinctBy { "${it.city}_${it.country}_${it.latitude}" }
    }

    private fun findTimezoneForCoords(lat: Double, lon: Double, countryCode: String?): String {
        if (countryCode != null) {
            when (countryCode.uppercase()) {
                "IN" -> return "Asia/Kolkata"
                "GB", "UK" -> return "Europe/London"
                "DE" -> return "Europe/Berlin"
                "JP" -> return "Asia/Tokyo"
                "SG" -> return "Asia/Singapore"
                "AE" -> return "Asia/Dubai"
                "NZ" -> return "Pacific/Auckland"
            }
        }

        // Approximate US/Australia or default
        if (lon < -50 && lon > -130 && lat > 20 && lat < 60) {
            return when {
                lon < -115 -> "America/Los_Angeles"
                lon < -100 -> "America/Denver"
                lon < -85 -> "America/Chicago"
                else -> "America/New_York"
            }
        }

        if (lat < -10 && lon > 110 && lon < 160) {
            return when {
                lon > 140 -> "Australia/Sydney"
                lon > 130 -> "Australia/Adelaide"
                else -> "Australia/Perth"
            }
        }

        return TimeZone.getDefault().id ?: "UTC"
    }
}

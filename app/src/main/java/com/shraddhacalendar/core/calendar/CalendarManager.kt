package com.shraddhacalendar.core.calendar

import android.Manifest
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.util.Log
import androidx.core.content.ContextCompat
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.PersonDeathRecord
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.data.local.CalendarMappingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun makeEntityKey(personName: String, date: LocalDate, sequenceNumber: Int): String {
    val cleanName = personName.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_").trim('_')
    return "${cleanName}_${date}_$sequenceNumber"
}

data class DiscoveredCalendar(
    val id: Long,
    val accountName: String,
    val accountType: String,
    val displayName: String,
    val isPrimary: Boolean,
    val accessLevel: Int
)

class CalendarManager(private val context: Context) {
    private val mappingRepo = CalendarMappingRepository(context)

    companion object {
        private const val TAG = "CalendarManager"
    }

    fun hasCalendarPermission(): Boolean {
        return try {
            val writePerm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
            val readPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
            writePerm && readPerm
        } catch (e: Exception) {
            Log.e(TAG, "Error checking calendar permissions", e)
            false
        }
    }

    /**
     * Queries only writable calendars (ACCESS_LEVEL >= CAL_ACCESS_CONTRIBUTOR) and prioritizes
     * primary Google Calendar accounts (ACCOUNT_TYPE = "com.google" or @gmail.com).
     */
    fun getTargetCalendarId(): Long? {
        if (!hasCalendarPermission()) {
            Log.w(TAG, "Cannot query calendars: permissions not granted")
            return null
        }
        return try {
            val projection = arrayOf(
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.IS_PRIMARY,
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL
            )

            val cursor = context.contentResolver.query(
                CalendarContract.Calendars.CONTENT_URI,
                projection,
                null,
                null,
                null
            )

            val calendars = mutableListOf<DiscoveredCalendar>()
            cursor?.use { c ->
                val idCol = c.getColumnIndex(CalendarContract.Calendars._ID)
                val nameCol = c.getColumnIndex(CalendarContract.Calendars.ACCOUNT_NAME)
                val typeCol = c.getColumnIndex(CalendarContract.Calendars.ACCOUNT_TYPE)
                val dispCol = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                val primaryCol = c.getColumnIndex(CalendarContract.Calendars.IS_PRIMARY)
                val accessCol = c.getColumnIndex(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL)

                while (c.moveToNext()) {
                    val id = c.getLong(idCol)
                    val accountName = if (nameCol >= 0) c.getString(nameCol) ?: "" else ""
                    val accountType = if (typeCol >= 0) c.getString(typeCol) ?: "" else ""
                    val displayName = if (dispCol >= 0) c.getString(dispCol) ?: "" else ""
                    val isPrimary = if (primaryCol >= 0) c.getInt(primaryCol) == 1 else false
                    val accessLevel = if (accessCol >= 0) c.getInt(accessCol) else 700

                    Log.d(TAG, "Discovered calendar: id=$id, name=$accountName, type=$accountType, display=$displayName, primary=$isPrimary, access=$accessLevel")

                    // ONLY consider writable calendars (ACCESS_LEVEL >= 500: contributor, editor, owner, root)
                    if (accessLevel >= CalendarContract.Calendars.CAL_ACCESS_CONTRIBUTOR) {
                        calendars.add(
                            DiscoveredCalendar(
                                id = id,
                                accountName = accountName,
                                accountType = accountType,
                                displayName = displayName,
                                isPrimary = isPrimary,
                                accessLevel = accessLevel
                            )
                        )
                    }
                }
            }

            // Priority 1: Primary Google account with Owner access (700)
            val p1 = calendars.firstOrNull { 
                (it.accountType.equals("com.google", ignoreCase = true) || it.accountName.contains("@gmail.com", ignoreCase = true)) && 
                it.isPrimary && it.accessLevel >= CalendarContract.Calendars.CAL_ACCESS_OWNER 
            }
            if (p1 != null) {
                Log.i(TAG, "Selected Priority 1 Primary Google Calendar: id=${p1.id}, name=${p1.accountName}")
                return p1.id
            }

            // Priority 2: Any Google account with Owner access (700)
            val p2 = calendars.firstOrNull { 
                (it.accountType.equals("com.google", ignoreCase = true) || it.accountName.contains("@gmail.com", ignoreCase = true)) && 
                it.accessLevel >= CalendarContract.Calendars.CAL_ACCESS_OWNER 
            }
            if (p2 != null) {
                Log.i(TAG, "Selected Priority 2 Google Owner Calendar: id=${p2.id}, name=${p2.accountName}")
                return p2.id
            }

            // Priority 3: Any Google account with writable access (>= 500)
            val p3 = calendars.firstOrNull { 
                it.accountType.equals("com.google", ignoreCase = true) || it.accountName.contains("@gmail.com", ignoreCase = true)
            }
            if (p3 != null) {
                Log.i(TAG, "Selected Priority 3 Google Writable Calendar: id=${p3.id}, name=${p3.accountName}")
                return p3.id
            }

            // Priority 4: Any primary writable calendar
            val p4 = calendars.firstOrNull { it.isPrimary }
            if (p4 != null) {
                Log.i(TAG, "Selected Priority 4 Primary Writable Calendar: id=${p4.id}")
                return p4.id
            }

            // Priority 5: Any writable calendar
            val p5 = calendars.firstOrNull()
            Log.i(TAG, "Selected Priority 5 First Writable Calendar: id=${p5?.id}")
            p5?.id
        } catch (e: Exception) {
            Log.e(TAG, "Failed to query calendars", e)
            null
        }
    }

    /**
     * Creates a meeting/event in Google Calendar with person-specific unique title and 2-day & 1-day advance notifications.
     */
    suspend fun addShraddhaToCalendar(
        person: PersonDeathRecord,
        event: ShraddhaEvent,
        entityKey: String,
        language: AppLanguage
    ): Boolean = withContext(Dispatchers.IO) {
        if (!hasCalendarPermission()) {
            Log.w(TAG, "addShraddhaToCalendar aborted: missing calendar permission")
            return@withContext false
        }

        try {
            val calendarId = getTargetCalendarId() ?: run {
                Log.e(TAG, "addShraddhaToCalendar aborted: no writable calendar found on device")
                return@withContext false
            }

            // Pre-clean any old instances to avoid duplicates
            try {
                removeShraddhaFromCalendar(entityKey, person.name, event)
            } catch (e: Exception) {
                Log.w(TAG, "Pre-clean warning: ${e.message}")
            }

            val tzId = try {
                ZoneId.of(person.location.timezoneId).id
            } catch (_: Exception) {
                ZoneId.systemDefault().id
            }
            val zoneId = ZoneId.of(tzId)

            val startZdt = ZonedDateTime.of(event.gregorianDate, event.kalaDetails.aparahnaStart, zoneId)
            val endZdt = ZonedDateTime.of(event.gregorianDate, event.kalaDetails.aparahnaEnd, zoneId)
            val startMillis = startZdt.toInstant().toEpochMilli()
            val endMillis = if (endZdt.isAfter(startZdt)) endZdt.toInstant().toEpochMilli() else startMillis + (3 * 60 * 60 * 1000)

            val localizedCeremony = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
            val title = "$localizedCeremony — ${person.name}"

            val localizedMasa = PanchangaLocalizer.localizeMasa(event.tithi.masa, event.tithi.isAdhikaMasa, language)
            val localizedPaksha = PanchangaLocalizer.localizePaksha(event.tithi.tithi.paksha, language)
            val localizedTithi = PanchangaLocalizer.localizeTithi(event.tithi.tithi, language)

            val description = """
                Person: ${person.name}
                Ceremony: $localizedCeremony
                Date: ${event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy (EEEE)"))}
                Panchanga: ${event.tithi.samvatsara} Nama Samvatsara, $localizedMasa, $localizedPaksha, $localizedTithi
                Location: ${person.location.displayName}
                Aparahna Timing: ${event.kalaDetails.aparahnaStart} to ${event.kalaDetails.aparahnaEnd}
                
                Calculated strictly according to Sri Uttaradi Math Panchanga & Smriti Muktavali.
                [Created by UM Shraddha Dates Calculator]
            """.trimIndent()

            val eventValues = ContentValues().apply {
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.TITLE, title)
                put(CalendarContract.Events.DESCRIPTION, description)
                put(CalendarContract.Events.EVENT_LOCATION, person.location.displayName)
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.EVENT_TIMEZONE, tzId)
                put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED)
                put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                put(CalendarContract.Events.HAS_ALARM, 1)
            }

            val eventUri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues)
            val eventId = eventUri?.lastPathSegment?.toLongOrNull() ?: run {
                Log.e(TAG, "Failed to insert event into Calendar Provider: uri=$eventUri")
                return@withContext false
            }

            Log.i(TAG, "Successfully inserted event into Google Calendar: eventId=$eventId, title=$title, key=$entityKey")

            // Insert Reminders (2880 = 2 days, 1440 = 1 day, 30 mins)
            val reminderMinutes = listOf(2880, 1440, 30)
            reminderMinutes.forEach { mins ->
                try {
                    val remValues = ContentValues().apply {
                        put(CalendarContract.Reminders.EVENT_ID, eventId)
                        put(CalendarContract.Reminders.MINUTES, mins)
                        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                    }
                    context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, remValues)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to insert reminder $mins mins for eventId=$eventId: ${e.message}")
                }
            }

            // Save Mapping locally
            mappingRepo.saveMapping(entityKey, eventId, calendarId, title)

            // Trigger immediate real-time refresh on Google Calendar app
            triggerImmediateCalendarRefresh()

            true
        } catch (e: Exception) {
            Log.e(TAG, "Exception during addShraddhaToCalendar", e)
            false
        }
    }

    /**
     * Removes the application-created calendar event using multi-pass ID, date-range, and title pattern deletion.
     */
    suspend fun removeShraddhaFromCalendar(
        entityKey: String,
        personName: String? = null,
        event: ShraddhaEvent? = null
    ): Boolean = withContext(Dispatchers.IO) {
        if (!hasCalendarPermission()) {
            Log.w(TAG, "removeShraddhaFromCalendar aborted: missing calendar permission")
            return@withContext false
        }

        try {
            var totalDeleted = 0
            val eventId = mappingRepo.getEventId(entityKey)

            // Pass 1: Direct Delete by mapped eventId
            if (eventId != null && eventId > 0) {
                totalDeleted += executeComprehensiveEventDelete(eventId)
            }

            // Pass 2: Date-Range Delete across event date window (00:00:00 to 23:59:59)
            if (event != null) {
                try {
                    val zoneId = ZoneId.systemDefault()
                    val dayStartMillis = event.gregorianDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
                    val dayEndMillis = event.gregorianDate.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

                    val cursor = context.contentResolver.query(
                        CalendarContract.Events.CONTENT_URI,
                        arrayOf(CalendarContract.Events._ID, CalendarContract.Events.TITLE),
                        "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} < ?",
                        arrayOf(dayStartMillis.toString(), dayEndMillis.toString()),
                        null
                    )

                    cursor?.use { c ->
                        val idCol = c.getColumnIndex(CalendarContract.Events._ID)
                        val titleCol = c.getColumnIndex(CalendarContract.Events.TITLE)
                        while (c.moveToNext()) {
                            val id = c.getLong(idCol)
                            val t = if (titleCol >= 0) c.getString(titleCol) ?: "" else ""

                            val matchesPerson = personName.isNullOrBlank() || t.contains(personName.trim(), ignoreCase = true)
                            val cleanRitual = event.traditionalName.substringAfter("—").trim()
                            val matchesRitual = t.contains(cleanRitual, ignoreCase = true) || t.contains(event.traditionalName, ignoreCase = true)

                            if (matchesPerson || matchesRitual) {
                                totalDeleted += executeComprehensiveEventDelete(id)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Date-range event delete failed", e)
                }
            }

            // Pass 3: Title Search across database
            if (!personName.isNullOrBlank()) {
                try {
                    val cursor = context.contentResolver.query(
                        CalendarContract.Events.CONTENT_URI,
                        arrayOf(CalendarContract.Events._ID, CalendarContract.Events.TITLE),
                        "${CalendarContract.Events.TITLE} LIKE ?",
                        arrayOf("%${personName.trim()}%"),
                        null
                    )
                    cursor?.use { c ->
                        val idCol = c.getColumnIndex(CalendarContract.Events._ID)
                        val titleCol = c.getColumnIndex(CalendarContract.Events.TITLE)
                        while (c.moveToNext()) {
                            val id = c.getLong(idCol)
                            val t = if (titleCol >= 0) c.getString(titleCol) ?: "" else ""
                            if (event != null) {
                                val cleanRitual = event.traditionalName.substringAfter("—").trim()
                                if (t.contains(cleanRitual, ignoreCase = true)) {
                                    totalDeleted += executeComprehensiveEventDelete(id)
                                }
                            } else {
                                totalDeleted += executeComprehensiveEventDelete(id)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.w(TAG, "Title search event delete failed", e)
                }
            }

            // Pass 4: Purge local SQLite mapping
            mappingRepo.deleteMapping(entityKey)

            // Trigger immediate real-time refresh on Google Calendar app
            triggerImmediateCalendarRefresh()

            Log.i(TAG, "removeShraddhaFromCalendar completed: key=$entityKey, totalDeleted=$totalDeleted")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Exception removing event from calendar", e)
            false
        }
    }

    /**
     * Forces immediate Android OS ContentObserver notification and expedited Google account sync.
     */
    private fun triggerImmediateCalendarRefresh() {
        try {
            // 1. Instantly notify ContentObservers for foreground calendar apps (Google Calendar, Samsung Calendar)
            context.contentResolver.notifyChange(CalendarContract.Events.CONTENT_URI, null)
            context.contentResolver.notifyChange(CalendarContract.Instances.CONTENT_URI, null)
            context.contentResolver.notifyChange(CalendarContract.Reminders.CONTENT_URI, null)
            context.contentResolver.notifyChange(CalendarContract.Calendars.CONTENT_URI, null)

            // 2. Request expedited sync on all Google accounts
            val accountManager = android.accounts.AccountManager.get(context)
            val googleAccounts = accountManager.getAccountsByType("com.google")
            googleAccounts.forEach { acc ->
                val bundle = android.os.Bundle().apply {
                    putBoolean(android.content.ContentResolver.SYNC_EXTRAS_MANUAL, true)
                    putBoolean(android.content.ContentResolver.SYNC_EXTRAS_EXPEDITED, true)
                }
                android.content.ContentResolver.requestSync(acc, CalendarContract.AUTHORITY, bundle)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Expedited calendar sync notice: ${e.message}")
        }
    }

    private fun executeComprehensiveEventDelete(id: Long): Int {
        var count = 0
        try {
            val eventUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id)
            val rows = context.contentResolver.delete(eventUri, null, null)
            count += rows
        } catch (e: Exception) {
            Log.w(TAG, "Delete via URI failed for id=$id: ${e.message}")
        }

        try {
            val rows = context.contentResolver.delete(
                CalendarContract.Events.CONTENT_URI,
                "${CalendarContract.Events._ID} = ?",
                arrayOf(id.toString())
            )
            count += rows
        } catch (e: Exception) {
            Log.w(TAG, "Delete via selection failed for id=$id: ${e.message}")
        }

        return count
    }

    /**
     * Checks if a calendar event is currently active for this entityKey in Google Calendar.
     */
    suspend fun isEventActive(
        entityKey: String,
        personName: String? = null,
        event: ShraddhaEvent? = null
    ): Boolean = withContext(Dispatchers.IO) {
        if (!hasCalendarPermission()) {
            return@withContext mappingRepo.getEventId(entityKey) != null
        }

        try {
            val eventId = mappingRepo.getEventId(entityKey)
            if (eventId != null && eventId > 0) {
                val cursor = context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(CalendarContract.Events._ID, CalendarContract.Events.DELETED),
                    "${CalendarContract.Events._ID} = ?",
                    arrayOf(eventId.toString()),
                    null
                )
                val found = cursor?.use { c ->
                    if (c.moveToFirst()) {
                        val deletedCol = c.getColumnIndex(CalendarContract.Events.DELETED)
                        val isDeleted = if (deletedCol >= 0) c.getInt(deletedCol) == 1 else false
                        !isDeleted
                    } else false
                } ?: false
                if (found) return@withContext true
            }

            // Check by date range and title if event is provided
            if (event != null && !personName.isNullOrBlank()) {
                val zoneId = ZoneId.systemDefault()
                val dayStartMillis = event.gregorianDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
                val dayEndMillis = event.gregorianDate.plusDays(1).atStartOfDay(zoneId).toInstant().toEpochMilli()

                val cursor = context.contentResolver.query(
                    CalendarContract.Events.CONTENT_URI,
                    arrayOf(CalendarContract.Events._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DELETED),
                    "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTSTART} < ?",
                    arrayOf(dayStartMillis.toString(), dayEndMillis.toString()),
                    null
                )
                val found = cursor?.use { c ->
                    val titleCol = c.getColumnIndex(CalendarContract.Events.TITLE)
                    val deletedCol = c.getColumnIndex(CalendarContract.Events.DELETED)
                    val idCol = c.getColumnIndex(CalendarContract.Events._ID)
                    var matched = false
                    while (c.moveToNext()) {
                        val t = if (titleCol >= 0) c.getString(titleCol) ?: "" else ""
                        val isDeleted = if (deletedCol >= 0) c.getInt(deletedCol) == 1 else false
                        val cleanRitual = event.traditionalName.substringAfter("—").trim()

                        if (!isDeleted && t.contains(personName.trim(), ignoreCase = true) && t.contains(cleanRitual, ignoreCase = true)) {
                            val matchedId = c.getLong(idCol)
                            mappingRepo.saveMapping(entityKey, matchedId, 1L, t)
                            matched = true
                            break
                        }
                    }
                    matched
                } ?: false
                if (found) return@withContext true
            }

            false
        } catch (e: Exception) {
            Log.e(TAG, "Exception checking isEventActive", e)
            false
        }
    }
}

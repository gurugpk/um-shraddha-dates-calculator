package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import java.time.LocalDate
import java.time.LocalTime

data class RecentSearchItem(
    val id: Long,
    val personName: String,
    val deathDate: LocalDate,
    val deathTime: LocalTime,
    val location: GeoLocation,
    val timestamp: Long
)

data class SavedProfileItem(
    val id: Long,
    val personName: String,
    val relationship: String?,
    val deathDate: LocalDate,
    val deathTime: LocalTime,
    val location: GeoLocation,
    val notes: String?,
    val timestamp: Long
)

class ShraddhaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "um_shraddha.db"
        const val DATABASE_VERSION = 3

        // Table 1: Recent Searches (Max 10 FIFO)
        const val TABLE_RECENTS = "recent_searches"
        const val COL_ID = "id"
        const val COL_PERSON_NAME = "person_name"
        const val COL_DEATH_DATE_EPOCH = "death_date_epoch"
        const val COL_DEATH_TIME_SEC = "death_time_sec"
        const val COL_CITY = "city"
        const val COL_STATE = "state"
        const val COL_COUNTRY = "country"
        const val COL_LATITUDE = "latitude"
        const val COL_LONGITUDE = "longitude"
        const val COL_TIMEZONE = "timezone"
        const val COL_TIMESTAMP = "timestamp"

        // Table 2: Calendar Event Mappings (EntityKey -> CalendarEventId)
        const val TABLE_CALENDAR_MAPPINGS = "calendar_mappings"
        const val COL_ENTITY_KEY = "entity_key"
        const val COL_CALENDAR_EVENT_ID = "calendar_event_id"
        const val COL_CALENDAR_ID = "calendar_id"
        const val COL_EVENT_TITLE = "event_title"

        // Table 3: Notification Schedules (EntityKey -> Alarm details)
        const val TABLE_NOTIFICATIONS = "notification_schedules"
        const val COL_CEREMONY_NAME = "ceremony_name"
        const val COL_GREGORIAN_DATE_EPOCH = "gregorian_date_epoch"
        const val COL_TIMEZONE_ID = "timezone_id"
        const val COL_LANGUAGE_CODE = "language_code"

        // Table 4: Permanently Saved Profiles (User-saved records that never get deleted by FIFO)
        const val TABLE_SAVED_PROFILES = "saved_profiles"
        const val COL_RELATIONSHIP = "relationship"
        const val COL_NOTES = "notes"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createRecents = """
            CREATE TABLE IF NOT EXISTS $TABLE_RECENTS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PERSON_NAME TEXT NOT NULL,
                $COL_DEATH_DATE_EPOCH INTEGER NOT NULL,
                $COL_DEATH_TIME_SEC INTEGER NOT NULL,
                $COL_CITY TEXT NOT NULL,
                $COL_STATE TEXT NOT NULL,
                $COL_COUNTRY TEXT NOT NULL,
                $COL_LATITUDE REAL NOT NULL,
                $COL_LONGITUDE REAL NOT NULL,
                $COL_TIMEZONE TEXT NOT NULL,
                $COL_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()

        val createMappings = """
            CREATE TABLE IF NOT EXISTS $TABLE_CALENDAR_MAPPINGS (
                $COL_ENTITY_KEY TEXT PRIMARY KEY,
                $COL_CALENDAR_EVENT_ID INTEGER NOT NULL,
                $COL_CALENDAR_ID INTEGER NOT NULL,
                $COL_EVENT_TITLE TEXT NOT NULL
            )
        """.trimIndent()

        val createNotifications = """
            CREATE TABLE IF NOT EXISTS $TABLE_NOTIFICATIONS (
                $COL_ENTITY_KEY TEXT PRIMARY KEY,
                $COL_PERSON_NAME TEXT NOT NULL,
                $COL_CEREMONY_NAME TEXT NOT NULL,
                $COL_GREGORIAN_DATE_EPOCH INTEGER NOT NULL,
                $COL_TIMEZONE_ID TEXT NOT NULL,
                $COL_LANGUAGE_CODE TEXT NOT NULL
            )
        """.trimIndent()

        val createSavedProfiles = """
            CREATE TABLE IF NOT EXISTS $TABLE_SAVED_PROFILES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PERSON_NAME TEXT NOT NULL,
                $COL_RELATIONSHIP TEXT,
                $COL_DEATH_DATE_EPOCH INTEGER NOT NULL,
                $COL_DEATH_TIME_SEC INTEGER NOT NULL,
                $COL_CITY TEXT NOT NULL,
                $COL_STATE TEXT NOT NULL,
                $COL_COUNTRY TEXT NOT NULL,
                $COL_LATITUDE REAL NOT NULL,
                $COL_LONGITUDE REAL NOT NULL,
                $COL_TIMEZONE TEXT NOT NULL,
                $COL_NOTES TEXT,
                $COL_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(createRecents)
        db.execSQL(createMappings)
        db.execSQL(createNotifications)
        db.execSQL(createSavedProfiles)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            val createNotifications = """
                CREATE TABLE IF NOT EXISTS $TABLE_NOTIFICATIONS (
                    $COL_ENTITY_KEY TEXT PRIMARY KEY,
                    $COL_PERSON_NAME TEXT NOT NULL,
                    $COL_CEREMONY_NAME TEXT NOT NULL,
                    $COL_GREGORIAN_DATE_EPOCH INTEGER NOT NULL,
                    $COL_TIMEZONE_ID TEXT NOT NULL,
                    $COL_LANGUAGE_CODE TEXT NOT NULL
                )
            """.trimIndent()
            db.execSQL(createNotifications)
        }
        if (oldVersion < 3) {
            val createSavedProfiles = """
                CREATE TABLE IF NOT EXISTS $TABLE_SAVED_PROFILES (
                    $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    $COL_PERSON_NAME TEXT NOT NULL,
                    $COL_RELATIONSHIP TEXT,
                    $COL_DEATH_DATE_EPOCH INTEGER NOT NULL,
                    $COL_DEATH_TIME_SEC INTEGER NOT NULL,
                    $COL_CITY TEXT NOT NULL,
                    $COL_STATE TEXT NOT NULL,
                    $COL_COUNTRY TEXT NOT NULL,
                    $COL_LATITUDE REAL NOT NULL,
                    $COL_LONGITUDE REAL NOT NULL,
                    $COL_TIMEZONE TEXT NOT NULL,
                    $COL_NOTES TEXT,
                    $COL_TIMESTAMP INTEGER NOT NULL
                )
            """.trimIndent()
            db.execSQL(createSavedProfiles)
        }
    }
}

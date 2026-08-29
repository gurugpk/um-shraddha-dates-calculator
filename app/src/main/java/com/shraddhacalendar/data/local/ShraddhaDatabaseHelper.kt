package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import java.time.LocalDate
import java.time.LocalTime

import com.shraddhacalendar.core.models.DemiseCircumstance
import com.shraddhacalendar.core.models.PersonDemiseStatus

data class RecentSearchItem(
    val id: Long,
    val personName: String,
    val deathDate: LocalDate,
    val deathTime: LocalTime,
    val location: GeoLocation,
    val timestamp: Long,
    val demiseStatus: PersonDemiseStatus = PersonDemiseStatus.CONFIRMED_DEMISE,
    val demiseCircumstance: DemiseCircumstance = DemiseCircumstance.NATURAL,
    val lastSeenDate: LocalDate? = null,
    val ageAtDisappearance: Int? = null
)

class ShraddhaDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "madwa_shraddha.db"
        const val DATABASE_VERSION = 5

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
        const val COL_DEMISE_STATUS = "demise_status"
        const val COL_DEMISE_CIRCUMSTANCE = "demise_circumstance"
        const val COL_LAST_SEEN_DATE_EPOCH = "last_seen_date_epoch"
        const val COL_AGE_AT_DISAPPEARANCE = "age_at_disappearance"

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
        const val COL_TRADITION_ID = "tradition_id"
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
                $COL_DEMISE_STATUS TEXT NOT NULL DEFAULT 'confirmed_demise',
                $COL_DEMISE_CIRCUMSTANCE TEXT NOT NULL DEFAULT 'natural',
                $COL_LAST_SEEN_DATE_EPOCH INTEGER,
                $COL_AGE_AT_DISAPPEARANCE INTEGER,
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
                $COL_TRADITION_ID TEXT NOT NULL DEFAULT 'uttaradi_matha',
                $COL_DEMISE_STATUS TEXT NOT NULL DEFAULT 'confirmed_demise',
                $COL_DEMISE_CIRCUMSTANCE TEXT NOT NULL DEFAULT 'natural',
                $COL_LAST_SEEN_DATE_EPOCH INTEGER,
                $COL_AGE_AT_DISAPPEARANCE INTEGER,
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
        if (oldVersion < 4) {
            try {
                db.execSQL("ALTER TABLE $TABLE_SAVED_PROFILES ADD COLUMN $COL_TRADITION_ID TEXT NOT NULL DEFAULT 'uttaradi_matha'")
            } catch (e: Exception) {
                // Column might already exist
            }
        }
        if (oldVersion < 5) {
            try {
                db.execSQL("ALTER TABLE $TABLE_SAVED_PROFILES ADD COLUMN $COL_DEMISE_STATUS TEXT NOT NULL DEFAULT 'confirmed_demise'")
                db.execSQL("ALTER TABLE $TABLE_SAVED_PROFILES ADD COLUMN $COL_DEMISE_CIRCUMSTANCE TEXT NOT NULL DEFAULT 'natural'")
                db.execSQL("ALTER TABLE $TABLE_SAVED_PROFILES ADD COLUMN $COL_LAST_SEEN_DATE_EPOCH INTEGER")
                db.execSQL("ALTER TABLE $TABLE_SAVED_PROFILES ADD COLUMN $COL_AGE_AT_DISAPPEARANCE INTEGER")

                db.execSQL("ALTER TABLE $TABLE_RECENTS ADD COLUMN $COL_DEMISE_STATUS TEXT NOT NULL DEFAULT 'confirmed_demise'")
                db.execSQL("ALTER TABLE $TABLE_RECENTS ADD COLUMN $COL_DEMISE_CIRCUMSTANCE TEXT NOT NULL DEFAULT 'natural'")
                db.execSQL("ALTER TABLE $TABLE_RECENTS ADD COLUMN $COL_LAST_SEEN_DATE_EPOCH INTEGER")
                db.execSQL("ALTER TABLE $TABLE_RECENTS ADD COLUMN $COL_AGE_AT_DISAPPEARANCE INTEGER")
            } catch (e: Exception) {
                // Column might already exist
            }
        }
    }
}

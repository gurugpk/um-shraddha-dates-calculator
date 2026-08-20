package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import java.time.LocalDate
import java.time.LocalTime

/**
 * Repository managing permanently saved Shraddha profiles in device memory.
 * Unlike recent searches, saved profiles are permanent and never deleted by FIFO eviction.
 */
class SavedProfilesRepository(context: Context) {

    private val dbHelper = ShraddhaDatabaseHelper(context)

    /**
     * Saves a person's death record permanently.
     * Returns the inserted or existing row ID.
     */
    fun saveProfile(
        record: PersonDeathRecord,
        relationship: String? = null,
        notes: String? = null
    ): Long {
        val db = dbHelper.writableDatabase

        // If already exists with same name and death date, update timestamp and optional fields
        val existing = getProfileByRecord(record.name, record.deathDate)
        if (existing != null) {
            val values = ContentValues().apply {
                put(ShraddhaDatabaseHelper.COL_RELATIONSHIP, relationship ?: existing.relationship)
                put(ShraddhaDatabaseHelper.COL_NOTES, notes ?: existing.notes)
                put(ShraddhaDatabaseHelper.COL_TIMESTAMP, System.currentTimeMillis())
            }
            db.update(
                ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
                values,
                "${ShraddhaDatabaseHelper.COL_ID} = ?",
                arrayOf(existing.id.toString())
            )
            return existing.id
        }

        val values = ContentValues().apply {
            put(ShraddhaDatabaseHelper.COL_PERSON_NAME, record.name.trim())
            put(ShraddhaDatabaseHelper.COL_RELATIONSHIP, relationship?.trim())
            put(ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH, record.deathDate.toEpochDay())
            put(ShraddhaDatabaseHelper.COL_DEATH_TIME_SEC, record.deathTime.toSecondOfDay())
            put(ShraddhaDatabaseHelper.COL_CITY, record.location.city)
            put(ShraddhaDatabaseHelper.COL_STATE, record.location.state)
            put(ShraddhaDatabaseHelper.COL_COUNTRY, record.location.country)
            put(ShraddhaDatabaseHelper.COL_LATITUDE, record.location.latitude)
            put(ShraddhaDatabaseHelper.COL_LONGITUDE, record.location.longitude)
            put(ShraddhaDatabaseHelper.COL_TIMEZONE, record.location.timezoneId)
            put(ShraddhaDatabaseHelper.COL_NOTES, notes?.trim())
            put(ShraddhaDatabaseHelper.COL_TIMESTAMP, System.currentTimeMillis())
        }

        return db.insert(ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES, null, values)
    }

    /**
     * Retrieves all saved profiles, sorted by newest saved first.
     */
    fun getAllSaved(): List<SavedProfileItem> {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<SavedProfileItem>()

        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            null,
            null,
            null,
            null,
            null,
            "${ShraddhaDatabaseHelper.COL_TIMESTAMP} DESC"
        )

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_ID))
                val personName = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_PERSON_NAME))
                val relationship = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_RELATIONSHIP))
                val deathDateEpoch = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH))
                val deathTimeSec = it.getInt(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_TIME_SEC))
                val city = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_CITY))
                val state = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_STATE))
                val country = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_COUNTRY))
                val lat = it.getDouble(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LATITUDE))
                val lon = it.getDouble(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LONGITUDE))
                val tz = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMEZONE))
                val notes = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_NOTES))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                list.add(
                    SavedProfileItem(
                        id = id,
                        personName = personName,
                        relationship = relationship,
                        deathDate = LocalDate.ofEpochDay(deathDateEpoch),
                        deathTime = LocalTime.ofSecondOfDay(deathTimeSec.toLong()),
                        location = GeoLocation(city, state, country, lat, lon, tz),
                        notes = notes,
                        timestamp = timestamp
                    )
                )
            }
        }

        return list
    }

    /**
     * Checks if a profile is already saved.
     */
    fun isProfileSaved(personName: String, deathDate: LocalDate): Boolean {
        return getProfileByRecord(personName, deathDate) != null
    }

    private fun getProfileByRecord(personName: String, deathDate: LocalDate): SavedProfileItem? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            null,
            "LOWER(TRIM(${ShraddhaDatabaseHelper.COL_PERSON_NAME})) = LOWER(TRIM(?)) AND ${ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH} = ?",
            arrayOf(personName.trim(), deathDate.toEpochDay().toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_ID))
                val name = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_PERSON_NAME))
                val relationship = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_RELATIONSHIP))
                val deathDateEpoch = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH))
                val deathTimeSec = it.getInt(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_TIME_SEC))
                val city = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_CITY))
                val state = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_STATE))
                val country = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_COUNTRY))
                val lat = it.getDouble(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LATITUDE))
                val lon = it.getDouble(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LONGITUDE))
                val tz = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMEZONE))
                val notes = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_NOTES))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                return SavedProfileItem(
                    id = id,
                    personName = name,
                    relationship = relationship,
                    deathDate = LocalDate.ofEpochDay(deathDateEpoch),
                    deathTime = LocalTime.ofSecondOfDay(deathTimeSec.toLong()),
                    location = GeoLocation(city, state, country, lat, lon, tz),
                    notes = notes,
                    timestamp = timestamp
                )
            }
        }
        return null
    }

    /**
     * Deletes a saved profile by its ID.
     */
    fun deleteSavedProfile(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            "${ShraddhaDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    /**
     * Deletes a saved profile matching person name and death date.
     */
    fun deleteSavedProfileByRecord(personName: String, deathDate: LocalDate) {
        val db = dbHelper.writableDatabase
        db.delete(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            "LOWER(TRIM(${ShraddhaDatabaseHelper.COL_PERSON_NAME})) = LOWER(TRIM(?)) AND ${ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH} = ?",
            arrayOf(personName.trim(), deathDate.toEpochDay().toString())
        )
    }

    /**
     * Clears all saved profiles.
     */
    fun clearAllSavedProfiles() {
        val db = dbHelper.writableDatabase
        db.delete(ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES, null, null)
    }
}

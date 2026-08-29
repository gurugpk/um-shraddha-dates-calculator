package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import com.shraddhacalendar.core.models.FamilyRelationship
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.MadhwaTradition
import com.shraddhacalendar.core.models.PersonDeathRecord
import java.time.LocalDate
import java.time.LocalTime

import com.shraddhacalendar.core.models.DemiseCircumstance
import com.shraddhacalendar.core.models.PersonDemiseStatus

data class SavedProfileItem(
    val id: Long,
    val personName: String,
    val relationship: String?,
    val deathDate: LocalDate,
    val deathTime: LocalTime,
    val location: GeoLocation,
    val notes: String?,
    val traditionId: String = "uttaradi_matha",
    val timestamp: Long,
    val demiseStatus: PersonDemiseStatus = PersonDemiseStatus.CONFIRMED_DEMISE,
    val demiseCircumstance: DemiseCircumstance = DemiseCircumstance.NATURAL,
    val lastSeenDate: LocalDate? = null,
    val ageAtDisappearance: Int? = null
) {
    fun toPersonDeathRecord(): PersonDeathRecord {
        return PersonDeathRecord(
            id = id,
            name = personName,
            deathDate = deathDate,
            deathTime = deathTime,
            location = location,
            relationship = FamilyRelationship.fromId(relationship),
            tradition = MadhwaTradition.fromId(traditionId),
            notes = notes ?: "",
            demiseStatus = demiseStatus,
            demiseCircumstance = demiseCircumstance,
            lastSeenDate = lastSeenDate,
            ageAtDisappearance = ageAtDisappearance
        )
    }
}

/**
 * Repository managing permanently saved Shraddha profiles in device memory.
 * Supports insert, in-place edit/update, and full deletion.
 */
class SavedProfilesRepository(context: Context) {

    private val dbHelper = ShraddhaDatabaseHelper(context)

    /**
     * Saves a person's death record permanently.
     * Returns the inserted or existing row ID.
     */
    fun saveProfile(
        record: PersonDeathRecord,
        relationship: String? = record.relationship.id,
        notes: String? = record.notes,
        traditionId: String = record.tradition.id
    ): Long {
        val db = dbHelper.writableDatabase

        // If already exists with same name and death date, update in-place
        val existing = getProfileByRecord(record.name, record.deathDate)
        if (existing != null) {
            val values = ContentValues().apply {
                put(ShraddhaDatabaseHelper.COL_RELATIONSHIP, relationship ?: existing.relationship)
                put(ShraddhaDatabaseHelper.COL_TRADITION_ID, traditionId)
                put(ShraddhaDatabaseHelper.COL_DEMISE_STATUS, record.demiseStatus.id)
                put(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE, record.demiseCircumstance.id)
                put(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH, record.lastSeenDate?.toEpochDay())
                put(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE, record.ageAtDisappearance)
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
            put(ShraddhaDatabaseHelper.COL_TRADITION_ID, traditionId)
            put(ShraddhaDatabaseHelper.COL_DEMISE_STATUS, record.demiseStatus.id)
            put(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE, record.demiseCircumstance.id)
            put(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH, record.lastSeenDate?.toEpochDay())
            put(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE, record.ageAtDisappearance)
            put(ShraddhaDatabaseHelper.COL_NOTES, notes?.trim())
            put(ShraddhaDatabaseHelper.COL_TIMESTAMP, System.currentTimeMillis())
        }

        return db.insert(ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES, null, values)
    }

    /**
     * Updates an existing profile in-place by ID without creating a duplicate.
     */
    fun updateProfile(
        id: Long,
        record: PersonDeathRecord,
        relationship: String? = record.relationship.id,
        notes: String? = record.notes,
        traditionId: String = record.tradition.id
    ): Boolean {
        val db = dbHelper.writableDatabase
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
            put(ShraddhaDatabaseHelper.COL_TRADITION_ID, traditionId)
            put(ShraddhaDatabaseHelper.COL_DEMISE_STATUS, record.demiseStatus.id)
            put(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE, record.demiseCircumstance.id)
            put(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH, record.lastSeenDate?.toEpochDay())
            put(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE, record.ageAtDisappearance)
            put(ShraddhaDatabaseHelper.COL_NOTES, notes?.trim())
            put(ShraddhaDatabaseHelper.COL_TIMESTAMP, System.currentTimeMillis())
        }

        val rowsAffected = db.update(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            values,
            "${ShraddhaDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
        return rowsAffected > 0
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
                val tradIndex = it.getColumnIndex(ShraddhaDatabaseHelper.COL_TRADITION_ID)
                val traditionId = if (tradIndex >= 0) it.getString(tradIndex) ?: "uttaradi_matha" else "uttaradi_matha"
                val notes = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_NOTES))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                val statusIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_STATUS)
                val statusStr = if (statusIdx >= 0) it.getString(statusIdx) else null
                val demiseStatus = if (statusStr == "missing_unconfirmed") {
                    PersonDemiseStatus.MISSING_UNCONFIRMED
                } else {
                    PersonDemiseStatus.CONFIRMED_DEMISE
                }

                val circIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE)
                val circStr = if (circIdx >= 0) it.getString(circIdx) else null
                val demiseCircumstance = DemiseCircumstance.fromId(circStr)

                val lastSeenIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH)
                val lastSeenEpoch = if (lastSeenIdx >= 0 && !it.isNull(lastSeenIdx)) it.getLong(lastSeenIdx) else null
                val lastSeenDate = lastSeenEpoch?.let { ep -> LocalDate.ofEpochDay(ep) }

                val ageIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE)
                val age = if (ageIdx >= 0 && !it.isNull(ageIdx)) it.getInt(ageIdx) else null

                list.add(
                    SavedProfileItem(
                        id = id,
                        personName = personName,
                        relationship = relationship,
                        deathDate = LocalDate.ofEpochDay(deathDateEpoch),
                        deathTime = LocalTime.ofSecondOfDay(deathTimeSec.toLong()),
                        location = GeoLocation(city, state, country, lat, lon, tz),
                        notes = notes,
                        traditionId = traditionId,
                        timestamp = timestamp,
                        demiseStatus = demiseStatus,
                        demiseCircumstance = demiseCircumstance,
                        lastSeenDate = lastSeenDate,
                        ageAtDisappearance = age
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

    fun getProfileById(id: Long): SavedProfileItem? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_SAVED_PROFILES,
            null,
            "${ShraddhaDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val pId = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_ID))
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
                val tradIndex = it.getColumnIndex(ShraddhaDatabaseHelper.COL_TRADITION_ID)
                val traditionId = if (tradIndex >= 0) it.getString(tradIndex) ?: "uttaradi_matha" else "uttaradi_matha"
                val notes = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_NOTES))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                val statusIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_STATUS)
                val statusStr = if (statusIdx >= 0) it.getString(statusIdx) else null
                val demiseStatus = if (statusStr == "missing_unconfirmed") {
                    PersonDemiseStatus.MISSING_UNCONFIRMED
                } else {
                    PersonDemiseStatus.CONFIRMED_DEMISE
                }

                val circIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE)
                val circStr = if (circIdx >= 0) it.getString(circIdx) else null
                val demiseCircumstance = DemiseCircumstance.fromId(circStr)

                val lastSeenIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH)
                val lastSeenEpoch = if (lastSeenIdx >= 0 && !it.isNull(lastSeenIdx)) it.getLong(lastSeenIdx) else null
                val lastSeenDate = lastSeenEpoch?.let { ep -> LocalDate.ofEpochDay(ep) }

                val ageIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE)
                val age = if (ageIdx >= 0 && !it.isNull(ageIdx)) it.getInt(ageIdx) else null

                return SavedProfileItem(
                    id = pId,
                    personName = name,
                    relationship = relationship,
                    deathDate = LocalDate.ofEpochDay(deathDateEpoch),
                    deathTime = LocalTime.ofSecondOfDay(deathTimeSec.toLong()),
                    location = GeoLocation(city, state, country, lat, lon, tz),
                    notes = notes,
                    traditionId = traditionId,
                    timestamp = timestamp,
                    demiseStatus = demiseStatus,
                    demiseCircumstance = demiseCircumstance,
                    lastSeenDate = lastSeenDate,
                    ageAtDisappearance = age
                )
            }
        }
        return null
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
                val tradIndex = it.getColumnIndex(ShraddhaDatabaseHelper.COL_TRADITION_ID)
                val traditionId = if (tradIndex >= 0) it.getString(tradIndex) ?: "uttaradi_matha" else "uttaradi_matha"
                val notes = it.getString(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_NOTES))
                val timestamp = it.getLong(it.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                val statusIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_STATUS)
                val statusStr = if (statusIdx >= 0) it.getString(statusIdx) else null
                val demiseStatus = if (statusStr == "missing_unconfirmed") {
                    PersonDemiseStatus.MISSING_UNCONFIRMED
                } else {
                    PersonDemiseStatus.CONFIRMED_DEMISE
                }

                val circIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE)
                val circStr = if (circIdx >= 0) it.getString(circIdx) else null
                val demiseCircumstance = DemiseCircumstance.fromId(circStr)

                val lastSeenIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH)
                val lastSeenEpoch = if (lastSeenIdx >= 0 && !it.isNull(lastSeenIdx)) it.getLong(lastSeenIdx) else null
                val lastSeenDate = lastSeenEpoch?.let { ep -> LocalDate.ofEpochDay(ep) }

                val ageIdx = it.getColumnIndex(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE)
                val age = if (ageIdx >= 0 && !it.isNull(ageIdx)) it.getInt(ageIdx) else null

                return SavedProfileItem(
                    id = id,
                    personName = name,
                    relationship = relationship,
                    deathDate = LocalDate.ofEpochDay(deathDateEpoch),
                    deathTime = LocalTime.ofSecondOfDay(deathTimeSec.toLong()),
                    location = GeoLocation(city, state, country, lat, lon, tz),
                    notes = notes,
                    traditionId = traditionId,
                    timestamp = timestamp,
                    demiseStatus = demiseStatus,
                    demiseCircumstance = demiseCircumstance,
                    lastSeenDate = lastSeenDate,
                    ageAtDisappearance = age
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

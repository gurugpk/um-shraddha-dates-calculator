package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import com.shraddhacalendar.core.models.GeoLocation
import com.shraddhacalendar.core.models.PersonDeathRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class RecentSearchRepository(context: Context) {
    private val dbHelper = ShraddhaDatabaseHelper(context)

    suspend fun saveRecentSearch(person: PersonDeathRecord): Long = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            // Check if exact same search already exists, remove it first so it moves to top
            db.delete(
                ShraddhaDatabaseHelper.TABLE_RECENTS,
                "${ShraddhaDatabaseHelper.COL_PERSON_NAME} = ? AND ${ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH} = ?",
                arrayOf(person.name, person.deathDate.toEpochDay().toString())
            )

            val values = ContentValues().apply {
                put(ShraddhaDatabaseHelper.COL_PERSON_NAME, person.name)
                put(ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH, person.deathDate.toEpochDay())
                put(ShraddhaDatabaseHelper.COL_DEATH_TIME_SEC, person.deathTime.toSecondOfDay())
                put(ShraddhaDatabaseHelper.COL_CITY, person.location.city)
                put(ShraddhaDatabaseHelper.COL_STATE, person.location.state)
                put(ShraddhaDatabaseHelper.COL_COUNTRY, person.location.country)
                put(ShraddhaDatabaseHelper.COL_LATITUDE, person.location.latitude)
                put(ShraddhaDatabaseHelper.COL_LONGITUDE, person.location.longitude)
                put(ShraddhaDatabaseHelper.COL_TIMEZONE, person.location.timezoneId)
                put(ShraddhaDatabaseHelper.COL_DEMISE_STATUS, person.demiseStatus.id)
                put(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE, person.demiseCircumstance.id)
                put(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH, person.lastSeenDate?.toEpochDay())
                put(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE, person.ageAtDisappearance)
                put(ShraddhaDatabaseHelper.COL_TIMESTAMP, System.currentTimeMillis())
            }
            val insertedId = db.insert(ShraddhaDatabaseHelper.TABLE_RECENTS, null, values)

            // Prune to Max 10 searches (FIFO: delete oldest beyond 10)
            val pruneQuery = """
                DELETE FROM ${ShraddhaDatabaseHelper.TABLE_RECENTS}
                WHERE ${ShraddhaDatabaseHelper.COL_ID} NOT IN (
                    SELECT ${ShraddhaDatabaseHelper.COL_ID}
                    FROM ${ShraddhaDatabaseHelper.TABLE_RECENTS}
                    ORDER BY ${ShraddhaDatabaseHelper.COL_TIMESTAMP} DESC
                    LIMIT 10
                )
            """.trimIndent()
            db.execSQL(pruneQuery)

            db.setTransactionSuccessful()
            insertedId
        } finally {
            db.endTransaction()
        }
    }

    suspend fun getRecentSearches(): List<RecentSearchItem> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<RecentSearchItem>()
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_RECENTS,
            null,
            null,
            null,
            null,
            null,
            "${ShraddhaDatabaseHelper.COL_TIMESTAMP} DESC",
            "10"
        )
        cursor.use { c ->
            while (c.moveToNext()) {
                val id = c.getLong(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_ID))
                val name = c.getString(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_PERSON_NAME))
                val dateEpoch = c.getLong(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_DATE_EPOCH))
                val timeSec = c.getInt(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_DEATH_TIME_SEC))
                val city = c.getString(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_CITY))
                val state = c.getString(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_STATE))
                val country = c.getString(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_COUNTRY))
                val lat = c.getDouble(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LATITUDE))
                val lon = c.getDouble(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_LONGITUDE))
                val tz = c.getString(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMEZONE))
                val ts = c.getLong(c.getColumnIndexOrThrow(ShraddhaDatabaseHelper.COL_TIMESTAMP))

                val statusIdx = c.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_STATUS)
                val statusStr = if (statusIdx >= 0) c.getString(statusIdx) else null
                val demiseStatus = if (statusStr == "missing_unconfirmed") {
                    com.shraddhacalendar.core.models.PersonDemiseStatus.MISSING_UNCONFIRMED
                } else {
                    com.shraddhacalendar.core.models.PersonDemiseStatus.CONFIRMED_DEMISE
                }

                val circIdx = c.getColumnIndex(ShraddhaDatabaseHelper.COL_DEMISE_CIRCUMSTANCE)
                val circStr = if (circIdx >= 0) c.getString(circIdx) else null
                val demiseCircumstance = com.shraddhacalendar.core.models.DemiseCircumstance.fromId(circStr)

                val lastSeenIdx = c.getColumnIndex(ShraddhaDatabaseHelper.COL_LAST_SEEN_DATE_EPOCH)
                val lastSeenEpoch = if (lastSeenIdx >= 0 && !c.isNull(lastSeenIdx)) c.getLong(lastSeenIdx) else null
                val lastSeenDate = lastSeenEpoch?.let { LocalDate.ofEpochDay(it) }

                val ageIdx = c.getColumnIndex(ShraddhaDatabaseHelper.COL_AGE_AT_DISAPPEARANCE)
                val age = if (ageIdx >= 0 && !c.isNull(ageIdx)) c.getInt(ageIdx) else null

                list.add(
                    RecentSearchItem(
                        id = id,
                        personName = name,
                        deathDate = LocalDate.ofEpochDay(dateEpoch),
                        deathTime = LocalTime.ofSecondOfDay(timeSec.toLong()),
                        location = GeoLocation(
                            city = city,
                            state = state,
                            country = country,
                            latitude = lat,
                            longitude = lon,
                            timezoneId = tz
                        ),
                        timestamp = ts,
                        demiseStatus = demiseStatus,
                        demiseCircumstance = demiseCircumstance,
                        lastSeenDate = lastSeenDate,
                        ageAtDisappearance = age
                    )
                )
            }
        }
        list
    }

    suspend fun deleteRecentSearch(id: Long): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val count = db.delete(
            ShraddhaDatabaseHelper.TABLE_RECENTS,
            "${ShraddhaDatabaseHelper.COL_ID} = ?",
            arrayOf(id.toString())
        )
        count > 0
    }

    suspend fun clearAllHistory(): Boolean = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(ShraddhaDatabaseHelper.TABLE_RECENTS, null, null)
        true
    }
}

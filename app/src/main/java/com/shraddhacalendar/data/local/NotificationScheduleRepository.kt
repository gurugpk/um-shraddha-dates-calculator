package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

data class ScheduledNotificationItem(
    val entityKey: String,
    val personName: String,
    val ceremonyName: String,
    val gregorianDate: LocalDate,
    val timezoneId: String,
    val languageCode: String
)

class NotificationScheduleRepository(context: Context) {
    private val dbHelper = ShraddhaDatabaseHelper(context)

    suspend fun saveSchedule(
        entityKey: String,
        personName: String,
        ceremonyName: String,
        gregorianDate: LocalDate,
        timezoneId: String,
        languageCode: String
    ) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ShraddhaDatabaseHelper.COL_ENTITY_KEY, entityKey)
            put(ShraddhaDatabaseHelper.COL_PERSON_NAME, personName)
            put(ShraddhaDatabaseHelper.COL_CEREMONY_NAME, ceremonyName)
            put(ShraddhaDatabaseHelper.COL_GREGORIAN_DATE_EPOCH, gregorianDate.toEpochDay())
            put(ShraddhaDatabaseHelper.COL_TIMEZONE_ID, timezoneId)
            put(ShraddhaDatabaseHelper.COL_LANGUAGE_CODE, languageCode)
        }
        db.insertWithOnConflict(
            ShraddhaDatabaseHelper.TABLE_NOTIFICATIONS,
            null,
            values,
            android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    suspend fun deleteSchedule(entityKey: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            ShraddhaDatabaseHelper.TABLE_NOTIFICATIONS,
            "${ShraddhaDatabaseHelper.COL_ENTITY_KEY} = ?",
            arrayOf(entityKey)
        )
    }

    suspend fun getAllSchedules(): List<ScheduledNotificationItem> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<ScheduledNotificationItem>()
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_NOTIFICATIONS,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.use { c ->
            val keyCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_ENTITY_KEY)
            val nameCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_PERSON_NAME)
            val ceremonyCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_CEREMONY_NAME)
            val dateCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_GREGORIAN_DATE_EPOCH)
            val tzCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_TIMEZONE_ID)
            val langCol = c.getColumnIndex(ShraddhaDatabaseHelper.COL_LANGUAGE_CODE)

            while (c.moveToNext()) {
                val entityKey = c.getString(keyCol)
                val personName = c.getString(nameCol)
                val ceremonyName = c.getString(ceremonyCol)
                val dateEpoch = c.getLong(dateCol)
                val timezoneId = c.getString(tzCol)
                val languageCode = c.getString(langCol)

                list.add(
                    ScheduledNotificationItem(
                        entityKey = entityKey,
                        personName = personName,
                        ceremonyName = ceremonyName,
                        gregorianDate = LocalDate.ofEpochDay(dateEpoch),
                        timezoneId = timezoneId,
                        languageCode = languageCode
                    )
                )
            }
        }
        list
    }

    suspend fun clearAll() = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(ShraddhaDatabaseHelper.TABLE_NOTIFICATIONS, null, null)
    }
}

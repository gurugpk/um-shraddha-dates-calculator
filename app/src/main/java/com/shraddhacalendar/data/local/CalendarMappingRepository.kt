package com.shraddhacalendar.data.local

import android.content.ContentValues
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class CalendarMappingItem(
    val entityKey: String,
    val calendarEventId: Long,
    val calendarId: Long,
    val eventTitle: String
)

class CalendarMappingRepository(context: Context) {
    private val dbHelper = ShraddhaDatabaseHelper(context)

    suspend fun saveMapping(entityKey: String, eventId: Long, calendarId: Long, title: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ShraddhaDatabaseHelper.COL_ENTITY_KEY, entityKey)
            put(ShraddhaDatabaseHelper.COL_CALENDAR_EVENT_ID, eventId)
            put(ShraddhaDatabaseHelper.COL_CALENDAR_ID, calendarId)
            put(ShraddhaDatabaseHelper.COL_EVENT_TITLE, title)
        }
        db.insertWithOnConflict(
            ShraddhaDatabaseHelper.TABLE_CALENDAR_MAPPINGS,
            null,
            values,
            android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    suspend fun getEventId(entityKey: String): Long? = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_CALENDAR_MAPPINGS,
            arrayOf(ShraddhaDatabaseHelper.COL_CALENDAR_EVENT_ID),
            "${ShraddhaDatabaseHelper.COL_ENTITY_KEY} = ?",
            arrayOf(entityKey),
            null,
            null,
            null
        )
        cursor.use { c ->
            if (c.moveToFirst()) {
                c.getLong(0)
            } else {
                null
            }
        }
    }

    suspend fun getAllActiveMappings(): Map<String, Long> = withContext(Dispatchers.IO) {
        val db = dbHelper.readableDatabase
        val map = mutableMapOf<String, Long>()
        val cursor = db.query(
            ShraddhaDatabaseHelper.TABLE_CALENDAR_MAPPINGS,
            arrayOf(ShraddhaDatabaseHelper.COL_ENTITY_KEY, ShraddhaDatabaseHelper.COL_CALENDAR_EVENT_ID),
            null,
            null,
            null,
            null,
            null
        )
        cursor.use { c ->
            while (c.moveToNext()) {
                map[c.getString(0)] = c.getLong(1)
            }
        }
        map
    }

    suspend fun deleteMapping(entityKey: String) = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(
            ShraddhaDatabaseHelper.TABLE_CALENDAR_MAPPINGS,
            "${ShraddhaDatabaseHelper.COL_ENTITY_KEY} = ?",
            arrayOf(entityKey)
        )
    }

    suspend fun clearAllMappings() = withContext(Dispatchers.IO) {
        val db = dbHelper.writableDatabase
        db.delete(ShraddhaDatabaseHelper.TABLE_CALENDAR_MAPPINGS, null, null)
    }
}

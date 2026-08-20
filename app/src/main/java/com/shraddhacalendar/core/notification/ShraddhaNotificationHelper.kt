package com.shraddhacalendar.core.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shraddhacalendar.MainActivity
import com.shraddhacalendar.R
import com.shraddhacalendar.core.localization.AppLanguage
import com.shraddhacalendar.core.localization.PanchangaLocalizer
import com.shraddhacalendar.core.models.ShraddhaEvent
import com.shraddhacalendar.data.local.NotificationScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object ShraddhaNotificationHelper {

    const val CHANNEL_ID = "shraddha_reminders_channel"
    private const val TAG = "ShraddhaNotifHelper"

    const val EXTRA_ENTITY_KEY = "extra_entity_key"
    const val EXTRA_PERSON_NAME = "extra_person_name"
    const val EXTRA_CEREMONY_NAME = "extra_ceremony_name"
    const val EXTRA_DATE_STR = "extra_date_str"
    const val EXTRA_DAYS_BEFORE = "extra_days_before"
    const val EXTRA_LANGUAGE_CODE = "extra_language_code"
    const val EXTRA_FROM_NOTIFICATION = "extra_from_notification"

    /**
     * Initializes the standard Android Notification Channel with system default notification sound.
     */
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = context.getString(R.string.notif_channel_name)
            val channelDesc = context.getString(R.string.notif_channel_desc)
            val importance = NotificationManager.IMPORTANCE_HIGH

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel(CHANNEL_ID, channelName, importance).apply {
                description = channelDesc
                enableLights(true)
                enableVibration(true)
                setSound(defaultSoundUri, null)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            Log.d(TAG, "Notification channel created: $CHANNEL_ID")
        }
    }

    /**
     * Schedules 2-day-before and 1-day-before Android application notifications using AlarmManager.
     */
    fun scheduleNotificationsForEvent(
        context: Context,
        personName: String,
        event: ShraddhaEvent,
        entityKey: String,
        language: AppLanguage,
        locationTimezoneId: String = "Asia/Kolkata"
    ) {
        createNotificationChannel(context)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val zoneId = try { ZoneId.of(locationTimezoneId) } catch (_: Exception) { ZoneId.systemDefault() }

        val localizedCeremony = PanchangaLocalizer.localizeTraditionalName(event.traditionalName, language)
        val formattedDate = event.gregorianDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

        val reminderDays = listOf(2, 1)

        reminderDays.forEach { daysBefore ->
            val reminderDate = event.gregorianDate.minusDays(daysBefore.toLong())
            val reminderTime = LocalTime.of(8, 0) // 08:00 AM
            val triggerZdt = ZonedDateTime.of(reminderDate, reminderTime, zoneId)
            val triggerMillis = triggerZdt.toInstant().toEpochMilli()

            val nowMillis = System.currentTimeMillis()

            if (triggerMillis > nowMillis) {
                val intent = Intent(context, ShraddhaAlarmReceiver::class.java).apply {
                    putExtra(EXTRA_ENTITY_KEY, entityKey)
                    putExtra(EXTRA_PERSON_NAME, personName)
                    putExtra(EXTRA_CEREMONY_NAME, localizedCeremony)
                    putExtra(EXTRA_DATE_STR, formattedDate)
                    putExtra(EXTRA_DAYS_BEFORE, daysBefore)
                    putExtra(EXTRA_LANGUAGE_CODE, language.code)
                }

                val requestCode = generateRequestCode(entityKey, daysBefore)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    requestCode,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                    }
                    Log.i(TAG, "Scheduled $daysBefore-day alarm for $personName ($localizedCeremony) at $triggerZdt")
                } catch (e: SecurityException) {
                    Log.w(TAG, "Exact alarm permission restricted, falling back to setAndAllowWhileIdle: ${e.message}")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                    }
                }
            } else {
                Log.d(TAG, "Skipping past trigger date: $triggerZdt (daysBefore=$daysBefore)")
            }
        }

        // Persist schedule in SQLite for reboot restoration
        CoroutineScope(Dispatchers.IO).launch {
            val repo = NotificationScheduleRepository(context)
            repo.saveSchedule(
                entityKey = entityKey,
                personName = personName,
                ceremonyName = event.traditionalName,
                gregorianDate = event.gregorianDate,
                timezoneId = locationTimezoneId,
                languageCode = language.code
            )
        }
    }

    /**
     * Cancels any pending 2-day and 1-day alarms for this entityKey.
     */
    fun cancelNotificationsForEvent(context: Context, entityKey: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val reminderDays = listOf(2, 1)

        reminderDays.forEach { daysBefore ->
            val requestCode = generateRequestCode(entityKey, daysBefore)
            val intent = Intent(context, ShraddhaAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d(TAG, "Cancelled $daysBefore-day alarm for key: $entityKey")
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val repo = NotificationScheduleRepository(context)
            repo.deleteSchedule(entityKey)
        }
    }

    /**
     * Displays the notification in the Android system notification drawer.
     */
    fun showNotification(
        context: Context,
        personName: String,
        ceremonyName: String,
        dateStr: String,
        daysBefore: Int,
        languageCode: String
    ) {
        createNotificationChannel(context)

        // Localized strings
        val titleRes = if (daysBefore == 2) R.string.notif_upcoming_title else R.string.notif_reminder_title
        val bodyTemplateRes = if (daysBefore == 2) R.string.notif_2days_body else R.string.notif_1day_body

        val title = context.getString(titleRes)
        val body = context.getString(bodyTemplateRes, personName, ceremonyName, dateStr)

        // Deep-link PendingIntent to MainActivity
        val tapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_FROM_NOTIFICATION, true)
            putExtra(EXTRA_PERSON_NAME, personName)
            putExtra(EXTRA_CEREMONY_NAME, ceremonyName)
            putExtra(EXTRA_DATE_STR, dateStr)
        }

        val tapPendingIntent = PendingIntent.getActivity(
            context,
            (personName.hashCode() * 31 + daysBefore),
            tapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_agenda)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setContentIntent(tapPendingIntent)
            .build()

        val notificationId = (personName.hashCode() * 31 + daysBefore).let { if (it < 0) -it else it }
        try {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
            Log.i(TAG, "Notification delivered: id=$notificationId, title=$title, body=$body")
        } catch (e: SecurityException) {
            Log.w(TAG, "Failed to deliver notification (missing POST_NOTIFICATIONS permission): ${e.message}")
        }
    }

    /**
     * Called on device boot to re-register all future alarms.
     */
    fun rescheduleAllActiveAlarms(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val repo = NotificationScheduleRepository(context)
            val schedules = repo.getAllSchedules()
            val now = LocalDate.now()

            schedules.forEach { item ->
                if (item.gregorianDate.isAfter(now) || item.gregorianDate.isEqual(now)) {
                    val lang = AppLanguage.entries.firstOrNull { it.code == item.languageCode } ?: AppLanguage.ENGLISH
                    val dummyEvent = ShraddhaEvent(
                        sequenceNumber = 1,
                        type = com.shraddhacalendar.core.models.ShraddhaType.MASIKA,
                        traditionalName = item.ceremonyName,
                        gregorianDate = item.gregorianDate,
                        dayOfWeek = item.gregorianDate.dayOfWeek.name,
                        tithi = com.shraddhacalendar.core.models.PanchangaTithi(
                            tithi = com.shraddhacalendar.core.models.TithiInfo(1, "Prathama", com.shraddhacalendar.core.models.Paksha.SHUKLA, 1),
                            masa = com.shraddhacalendar.core.models.LunarMonth.CHAITRA,
                            isAdhikaMasa = false,
                            samvatsara = "Krodhi"
                        ),
                        kalaDetails = com.shraddhacalendar.core.models.DayKalaDetails(
                            date = item.gregorianDate,
                            sunrise = LocalTime.of(6, 0),
                            sunset = LocalTime.of(18, 0),
                            dinmanaMinutes = 720,
                            aparahnaStart = LocalTime.of(13, 15),
                            aparahnaEnd = LocalTime.of(15, 45),
                            kutapaStart = LocalTime.of(11, 45),
                            kutapaEnd = LocalTime.of(12, 35)
                        ),
                        explanation = "Restored from schedule repository"
                    )

                    scheduleNotificationsForEvent(
                        context = context,
                        personName = item.personName,
                        event = dummyEvent,
                        entityKey = item.entityKey,
                        language = lang,
                        locationTimezoneId = item.timezoneId
                    )
                }
            }
        }
    }

    private fun generateRequestCode(entityKey: String, daysBefore: Int): Int {
        val code = entityKey.hashCode() * 31 + daysBefore
        return if (code < 0) -code else code
    }
}

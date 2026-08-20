package com.shraddhacalendar.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ShraddhaAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val personName = intent.getStringExtra(ShraddhaNotificationHelper.EXTRA_PERSON_NAME) ?: return
        val ceremonyName = intent.getStringExtra(ShraddhaNotificationHelper.EXTRA_CEREMONY_NAME) ?: "Shraddha"
        val dateStr = intent.getStringExtra(ShraddhaNotificationHelper.EXTRA_DATE_STR) ?: ""
        val daysBefore = intent.getIntExtra(ShraddhaNotificationHelper.EXTRA_DAYS_BEFORE, 2)
        val languageCode = intent.getStringExtra(ShraddhaNotificationHelper.EXTRA_LANGUAGE_CODE) ?: "en"

        Log.i("ShraddhaAlarmReceiver", "Received alarm for $personName, ceremony=$ceremonyName, daysBefore=$daysBefore")

        ShraddhaNotificationHelper.showNotification(
            context = context,
            personName = personName,
            ceremonyName = ceremonyName,
            dateStr = dateStr,
            daysBefore = daysBefore,
            languageCode = languageCode
        )
    }
}

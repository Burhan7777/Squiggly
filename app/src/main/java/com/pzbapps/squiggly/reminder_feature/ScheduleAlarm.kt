package com.pzbapps.squiggly.reminder_feature

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar
import java.util.UUID

fun scheduleReminder(context: Context, reminderTime: Calendar, noteId: Int, noteTitle: String) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("noteId", noteId)
        putExtra("noteTitle", noteTitle)
    }


    val pendingIntent = PendingIntent.getBroadcast(
        context,
        noteId,  // Use noteId to make each PendingIntent unique
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or  PendingIntent.FLAG_IMMUTABLE
    )

    val triggerTimeMillis = reminderTime.timeInMillis
    println(triggerTimeMillis / 60)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
        }
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
    }
}

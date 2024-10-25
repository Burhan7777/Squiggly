package com.pzbapps.squiggly.reminder_feature

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

fun scheduleReminder(
    context: Context,
    reminderTime: Calendar,
    noteId: Int,
    noteTitle: String,
    note: MutableState<Note>,
    viewModel: MainActivityViewModel,
    time: MutableLongState,
    systemTime: MutableLongState
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("noteId", noteId)
        putExtra("noteTitle", noteTitle)
    }


    val pendingIntent = PendingIntent.getBroadcast(
        context,
        noteId,  // Use noteId to make each PendingIntent unique
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val triggerTimeMillis = reminderTime.timeInMillis

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (alarmManager.canScheduleExactAlarms()) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerTimeMillis,
                pendingIntent
            )
            var note = note.value.copy(reminder = triggerTimeMillis)
            viewModel.updateNote(note)
            viewModel.getNoteById(noteId)
            var note1 = viewModel.getNoteById.value
            time.longValue = triggerTimeMillis
            viewModel.timeInString.value = formatDateTimeFromMillis(triggerTimeMillis)
            systemTime.longValue = System.currentTimeMillis()
        }
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )
        var note = note.value.copy(reminder = triggerTimeMillis)
        viewModel.updateNote(note)
        viewModel.getNoteById(noteId)
        var note1 = viewModel.getNoteById.value
        time.longValue = triggerTimeMillis
        viewModel.timeInString.value = formatDateTimeFromMillis(triggerTimeMillis)
        systemTime.longValue = System.currentTimeMillis()
    }
}

fun cancelReminder(context: Context, noteId: Int) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // Create an Intent matching the one used in the scheduleReminder function
    val intent = Intent(context, ReminderReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        noteId, // Use the same unique noteId to identify the PendingIntent
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    // Cancel the alarm
    alarmManager.cancel(pendingIntent)
    // Also cancel the pendingIntent
    pendingIntent.cancel()
}

fun formatDateTimeFromMillis(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())
    val date = Date(millis)
    return dateFormat.format(date)
}

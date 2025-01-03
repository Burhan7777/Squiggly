package com.pzbapps.squiggly.reminder_feature

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var reminderRepository: ReminderRepository

    override fun onReceive(context: Context, intent: Intent?) {
        println("alarm received")
        val noteId = intent?.getIntExtra("noteId", 0)
        val noteTitle = intent?.getStringExtra("noteTitle")

        GlobalScope.launch(Dispatchers.IO) {
            reminderRepository.resetReminder(noteId!!)
        }

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "MyApp::AlarmWakeLockTag"
        )
        wakeLock.acquire(10 * 60 * 1000L /* 10 minutes */)
        try {
            notification(context, noteId!!)

        } finally {
            wakeLock.release()
        }
    }

    fun notification(context: Context, noteId: Int) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val openNoteIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("noteId", noteId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            noteId!!,
            openNoteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Reminder:Hello there !!!")
            .setContentText("You have a reminder for this note.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(noteId, notification)
    }
}
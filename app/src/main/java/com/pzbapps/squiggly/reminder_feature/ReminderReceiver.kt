package com.pzbapps.squiggly.reminder_feature

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint


class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        println("alarm received")
        val noteId = intent?.getIntExtra("noteId", 0)
        val noteTitle = intent?.getStringExtra("noteTitle")

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            noteId!!,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Reminder: $noteTitle")
            .setContentText("You have a reminder for this note.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(noteId, notification)
    }
}
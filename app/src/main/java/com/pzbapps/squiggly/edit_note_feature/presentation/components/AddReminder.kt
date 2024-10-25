package com.pzbapps.squiggly.edit_note_feature.presentation.components

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.edit_note_feature.domain.utils.permissionHandlerNotification
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.reminder_feature.scheduleReminder
import io.grpc.Context
import java.util.Calendar

fun addReminder(
    activity: MainActivity,
    note: MutableState<Note>,
    title: String,
    showMenu: MutableState<Boolean>,
    notificationLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: MainActivityViewModel,
    time: MutableLongState,
    systemTime: MutableLongState
) {


    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        addReminderForAndroid13AndAbove(
            activity,
            note,
            title,
            showMenu,
            notificationLauncher,
            viewModel,
            time,
            systemTime
        )
    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.S) {
        addReminderForAndroid12(
            activity,
            note,
            title,
            showMenu,
            viewModel,
            time,
            systemTime
        )
    } else {
        addReminderForAndroidBelow12(
            activity,
            note,
            title,
            showMenu,
            viewModel,
            time,
            systemTime
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun addReminderForAndroid13AndAbove(
    activity: MainActivity,
    note: MutableState<Note>,
    title: String,
    showMenu: MutableState<Boolean>,
    notificationLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: MainActivityViewModel,
    time: MutableLongState,
    systemTime: MutableLongState

) {

    var resultNotification = ""
    val alarmManager =
        activity.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager
    if (!alarmManager.canScheduleExactAlarms()) {
        // You need to guide the user to the system settings to grant the permission
        // Here's how you can send the user to the system settings for your app:
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        activity.startActivity(intent)
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            resultNotification = permissionHandlerNotification(activity)
        }

        if (resultNotification == "true") {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                activity,
                { _, year, month, dayOfMonth ->
                    // After the date is selected, show the time picker dialog
                    val timePickerDialog = TimePickerDialog(
                        activity,
                        { _, hourOfDay, minute ->
                            // Set the selected date and time in Calendar for API 21+
                            calendar.set(Calendar.YEAR, year)
                            calendar.set(Calendar.MONTH, month)
                            calendar.set(
                                Calendar.DAY_OF_MONTH,
                                dayOfMonth
                            )
                            calendar.set(
                                Calendar.HOUR_OF_DAY,
                                hourOfDay
                            )
                            calendar.set(Calendar.MINUTE, minute)
                            calendar.set(
                                Calendar.SECOND,
                                0
                            ) // Reset seconds to 0

                            // Pass the selected date and time back to the caller
                            //  onDateTimeSelected(calendar)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePickerDialog.show() // Show the time picker dialog
                    timePickerDialog.setOnDismissListener {
                        scheduleReminder(
                            activity,
                            calendar,
                            note.value.id,
                            title,
                            note,
                            viewModel,
                            time,
                            systemTime
                        )
                        Toast.makeText(
                            activity,
                            "Reminder has been set",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            datePickerDialog.show()

            showMenu.value = false
        } else if (resultNotification == "Rationale shown") {
            Toast.makeText(
                activity,
                "We need this permission to show the notification of reminder",
                Toast.LENGTH_SHORT
            ).show()
        } else if (resultNotification == "false") {
            notificationLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun addReminderForAndroid12(
    activity: MainActivity,
    note: MutableState<Note>,
    title: String,
    showMenu: MutableState<Boolean>,
    viewModel: MainActivityViewModel,
    time: MutableLongState,
    systemTime: MutableLongState
) {

    //  resultAlarm = permissionHandlerAlarm(activity)
    val alarmManager =
        activity.getSystemService(android.content.Context.ALARM_SERVICE) as AlarmManager
    if (!alarmManager.canScheduleExactAlarms()) {
        // You need to guide the user to the system settings to grant the permission
        // Here's how you can send the user to the system settings for your app:
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        activity.startActivity(intent)
    } else {
        // Permission is granted, you can schedule the alarm
        //scheduleReminder(context, reminderTime, noteId, noteTitle)
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            activity,
            { _, year, month, dayOfMonth ->
                // After the date is selected, show the time picker dialog
                val timePickerDialog = TimePickerDialog(
                    activity,
                    { _, hourOfDay, minute ->
                        // Set the selected date and time in Calendar for API 21+
                        calendar.set(Calendar.YEAR, year)
                        calendar.set(Calendar.MONTH, month)
                        calendar.set(
                            Calendar.DAY_OF_MONTH,
                            dayOfMonth
                        )
                        calendar.set(
                            Calendar.HOUR_OF_DAY,
                            hourOfDay
                        )
                        calendar.set(Calendar.MINUTE, minute)
                        calendar.set(
                            Calendar.SECOND,
                            0
                        ) // Reset seconds to 0

                        // Pass the selected date and time back to the caller
                        //  onDateTimeSelected(calendar)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                )
                timePickerDialog.show() // Show the time picker dialog
                timePickerDialog.setOnDismissListener {
                    scheduleReminder(
                        activity,
                        calendar,
                        note.value.id,
                        title,
                        note,
                        viewModel,
                        time,
                        systemTime
                    )
                    Toast.makeText(
                        activity,
                        "Reminder has been set",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()

        showMenu.value = false
    }


}

fun addReminderForAndroidBelow12(
    activity: MainActivity,
    note: MutableState<Note>,
    title: String,
    showMenu: MutableState<Boolean>,
    viewModel: MainActivityViewModel,
    time: MutableLongState,
    systemTime: MutableLongState
) {
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        activity,
        { _, year, month, dayOfMonth ->
            // After the date is selected, show the time picker dialog
            val timePickerDialog = TimePickerDialog(
                activity,
                { _, hourOfDay, minute ->
                    // Set the selected date and time in Calendar for API 21+
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(
                        Calendar.DAY_OF_MONTH,
                        dayOfMonth
                    )
                    calendar.set(
                        Calendar.HOUR_OF_DAY,
                        hourOfDay
                    )
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(
                        Calendar.SECOND,
                        0
                    ) // Reset seconds to 0

                    // Pass the selected date and time back to the caller
                    //  onDateTimeSelected(calendar)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.show() // Show the time picker dialog
            timePickerDialog.setOnDismissListener {
                scheduleReminder(
                    activity,
                    calendar,
                    note.value.id,
                    title,
                    note,
                    viewModel,
                    time,
                    systemTime
                )
                Toast.makeText(
                    activity,
                    "Reminder has been set",
                    Toast.LENGTH_SHORT
                ).show()
            }

        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    datePickerDialog.show()
    showMenu.value = false
}

fun cancelAlarm(){

}
package com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.edit_note_feature.presentation.components.addReminder
import com.pzbapps.squiggly.edit_note_feature.presentation.components.updateReminderInDB
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.reminder_feature.cancelReminder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertBoxShowReminder(
    time: MutableLongState,
    systemTime: MutableLongState,
    activity: MainActivity,
    note: MutableState<Note>,
    title: String,
    showMenu: MutableState<Boolean>,
    notificationLauncher: ManagedActivityResultLauncher<String, Boolean>,
    viewModel: MainActivityViewModel,
    timeInString: MutableState<String>,
    formattedTime: MutableState<String>,
    showReminderDialogBox: MutableState<Boolean>
) {
    var scope = rememberCoroutineScope()
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            showReminderDialogBox.value = false
        },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = MaterialTheme.colors.primaryVariant,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Column() {
                Text(
                    text = "Active Reminder",
                    fontFamily = FontFamily.fontFamilyBold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            addReminder(
                                activity,
                                note,
                                title,
                                showMenu,
                                notificationLauncher,
                                viewModel,
                                time,
                                systemTime,
                                mutableStateOf(false),
                                timeInString
                            )
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = "Alarm",
                            tint = MaterialTheme.colors.onPrimary
                        )
                        Spacer(modifier = Modifier.width(3.dp))

                        androidx.compose.material3.Text(
                            if (timeInString.value == "") formattedTime.value else timeInString.value,
                            fontSize = 15.sp, modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        IconButton(onClick = {
                            cancelReminder(activity, note.value.id)
                            updateReminderInDB(viewModel, note)
                            time.longValue = 0
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Cancel the alarm",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }

        },
        confirmButton = {
            TextButton(onClick = { showReminderDialogBox.value = false }) {
                Text(text = "OK", color = MaterialTheme.colors.onPrimary)
            }
        },
        dismissButton = {}
    )
}
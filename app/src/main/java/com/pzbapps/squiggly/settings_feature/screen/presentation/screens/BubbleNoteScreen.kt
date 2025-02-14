package com.pzbapps.squiggly.settings_feature.screen.presentation.screens

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.pzbapps.squiggly.bubble_note_feature.presentation.BubbleService
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity


@Composable
fun BubbleNoteScreen(activity: MainActivity) {
    var context = LocalContext.current
    var isServiceOn = remember { mutableStateOf(false) }
    isServiceOn.value = isForegroundServiceRunning(activity, 10)

    var intent = Intent(activity, BubbleService::class.java).apply {
        action = "STOP_SERVICE"
    }

    val overlayPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val serviceIntent = Intent(context, BubbleService::class.java)

        if (Settings.canDrawOverlays(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent)
            } else {
                activity.startService(serviceIntent)
            }
        } else {
            Toast.makeText(context, "Overlay Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        var checked = remember { mutableStateOf(isServiceOn.value) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                    MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {

                },
            shape = androidx.compose.material3.MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                disabledContainerColor = MaterialTheme.colors.primary,
                disabledContentColor = MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Auto Save",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Column() {
                    Text(
                        text = "Start Bubble Note",
                        modifier = Modifier.padding(
                            top = 12.dp,
                            start = 10.dp
                        ),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Press the notification to close it",
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = FontStyle.Italic
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Checkbox(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        checked = checked.value,
                        onCheckedChange = {
                            checked.value = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colors.onSecondary,
                            uncheckedColor = MaterialTheme.colors.onPrimary,
                            checkedColor = MaterialTheme.colors.onPrimary
                        ),
                    )
                }
            }
        }
        if (checked.value) {
            if (!Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                )
                overlayPermissionLauncher.launch(intent)
            } else {
                val serviceIntent = Intent(context, BubbleService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    activity.startForegroundService(serviceIntent)
                } else {
                    activity.startService(serviceIntent)
                }
            }
        } else {
            activity.stopService(intent)
        }

    }
}

fun isForegroundServiceRunning(context: Context, notificationId: Int): Boolean {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.activeNotifications.any { it.id == notificationId }
}
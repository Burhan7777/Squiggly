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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.bubble_note_feature.presentation.BubbleService
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity

enum class BubbleSize {
    SMALL, MEDIUM, LARGE
}

@Composable
fun BubbleNoteScreen(activity: MainActivity) {
    var context = LocalContext.current
    var isServiceOn = remember { mutableStateOf(false) }
    isServiceOn.value = isForegroundServiceRunning(activity, 10)

    var intent = Intent(activity, BubbleService::class.java).apply {
        action = "STOP_SERVICE"
    }

    // Preferences
    val prefs = context.getSharedPreferences("bubble_prefs", Context.MODE_PRIVATE)
    var bubbleSize by remember { mutableStateOf(BubbleSize.valueOf(prefs.getString("size", BubbleSize.MEDIUM.name)!!)) }
    var selectedIcon by remember { mutableStateOf(prefs.getInt("icon", 0)) }

    // Beautiful light colors
    val availableColors = listOf(
        Color(0xFF94B9F4),  // Soft Sky Blue
        Color(0xFFF4B9C8),  // Light Rose Pink
        Color(0xFFB9F4D6),  // Mint Green
        Color(0xFFE2B9F4),  // Soft Lavender
        Color(0xFFF4DAB9),  // Peach
        Color(0xFFB9E6F4)   // Light Aqua
    )

    // Store and retrieve color as a single integer
    var bubbleColor by remember {
        val colorInt = prefs.getInt("bubble_color", availableColors[0].toArgb())
        mutableStateOf(Color(colorInt))
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

        // Service Toggle Card
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
                ),
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
                Column {
                    Text(
                        text = "Start Bubble Note",
                        modifier = Modifier.padding(top = 12.dp, start = 10.dp),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Bubble Size Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Bubble Size",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BubbleSize.values().forEach { size ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            RadioButton(
                                selected = bubbleSize == size,
                                onClick = {
                                    bubbleSize = size
                                    prefs.edit().putString("size", size.name).apply()
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.onPrimary
                                )
                            )
                            Text(
                                size.name,
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }

        // Color Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Bubble Color",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    availableColors.forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .background(color, CircleShape)
                                .border(
                                    width = if (bubbleColor.toArgb() == color.toArgb()) 2.dp else 0.dp,
                                    color = MaterialTheme.colors.onPrimary,
                                    shape = CircleShape
                                )
                                .clickable {
                                    bubbleColor = color
                                    prefs.edit()
                                        .putInt("bubble_color", color.toArgb())
                                        .apply()
                                }
                        )
                    }
                }
            }
        }

        // Icon Selection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.primary)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Bubble Icon",
                    color = MaterialTheme.colors.onPrimary,
                    style = MaterialTheme.typography.h6
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val icons = listOf(
                        Icons.Default.Note to "Note",
                        Icons.Default.Edit to "Edit",
                        Icons.Default.Create to "Create",
                        Icons.Default.AddCircle to "Add"
                    )

                    icons.forEachIndexed { index, (icon, description) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            IconButton(
                                onClick = {
                                    selectedIcon = index
                                    prefs.edit().putInt("icon", index).apply()
                                }
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = description,
                                    tint = if (selectedIcon == index)
                                        MaterialTheme.colors.onPrimary
                                    else
                                        MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Text(
                                description,
                                color = MaterialTheme.colors.onPrimary,
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }

        // Handle service state
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
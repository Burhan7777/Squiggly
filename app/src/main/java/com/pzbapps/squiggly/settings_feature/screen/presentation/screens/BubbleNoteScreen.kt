package com.pzbapps.squiggly.settings_feature.screen.presentation.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.startActivityForResult
import com.pzbapps.squiggly.bubble_note_feature.presentation.BubbleService
import com.pzbapps.squiggly.common.presentation.MainActivity


@Composable
fun BubbleNoteScreen(activity: MainActivity) {
    var context = LocalContext.current

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
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        var checked = remember { mutableStateOf(false) }
        Checkbox(
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
        }
    }
}
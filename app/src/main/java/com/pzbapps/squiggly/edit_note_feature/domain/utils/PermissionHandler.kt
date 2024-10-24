package com.pzbapps.squiggly.edit_note_feature.domain.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.pzbapps.squiggly.common.presentation.MainActivity


fun permissionHandlerNotification(activity: MainActivity): String {
    return if (ContextCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        "true"
    } else if (shouldShowRequestPermissionRationale(
            activity,
            android.Manifest.permission.POST_NOTIFICATIONS
        )
    ) {

        "Rationale shown"
    } else {
        "false"
    }
}
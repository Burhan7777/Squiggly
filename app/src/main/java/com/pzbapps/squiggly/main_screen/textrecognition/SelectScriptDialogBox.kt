package com.pzbapps.squiggly.main_screen.textrecognition

import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.pzbapps.squiggly.common.presentation.MainActivity


@Composable
fun SelectScriptDialogBox(
    scanner: GmsDocumentScanner,
    result: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    activity: MainActivity,
    selectedScript: MutableState<String>,
    onDismiss: () -> Unit
) {

    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = androidx.compose.material.MaterialTheme.colors.primary,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Select the language of document",
                    fontSize = 12.sp,
                    color = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
                ScriptCardView(
                    "Latin script (English, Spanish, German etc)",
                    selectedScript,
                    0,
                    scanner,
                    result,
                    activity,
                    onDismiss
                )
                ScriptCardView(
                    "Devanagari script (Hindi, Sanskrit  etc)",
                    selectedScript,
                    1,
                    scanner,
                    result,
                    activity,
                    onDismiss
                )
                ScriptCardView(
                    "Chinese script ",
                    selectedScript,
                    2,
                    scanner,
                    result,
                    activity,
                    onDismiss
                )
                ScriptCardView(
                    "Japanese script",
                    selectedScript,
                    3,
                    scanner,
                    result,
                    activity,
                    onDismiss
                )
                ScriptCardView(
                    "Korean script",
                    selectedScript,
                    4,
                    scanner,
                    result,
                    activity,
                    onDismiss
                )
            }
        },
        confirmButton = {

        },
        dismissButton = {

        }
    )
}

@Composable
fun ScriptCardView(
    text: String, selectedScript: MutableState<String>, index: Int, scanner: GmsDocumentScanner,
    result: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    activity: MainActivity, onDismiss: () -> Unit
) {
    val script = arrayOf("English", "Hindi", "Chinese", "Japanese", "Korean")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                selectedScript.value = script[index]
                scanner
                    .getStartScanIntent(activity)
                    .addOnSuccessListener {
                        try {
                            result.launch(
                                IntentSenderRequest
                                    .Builder(it)
                                    .build()
                            )
                        } catch (exception: RuntimeException) {
                            Toast
                                .makeText(
                                    activity,
                                    "Failed to open the activity. Please try again",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }

                    }
                onDismiss()
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
        )
    ) {
        Text(
            text = text,
            color = androidx.compose.material.MaterialTheme.colors.onPrimary,
            fontSize = 15.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}
package com.pzbapps.squiggly.settings_feature.screen.presentation.components

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.storage.StorageReference
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.di.AppModule
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun DisplayBackupNamesAlertBox(
    listOfBackUpFiles: List<StorageReference>,
    loadingDialog: MutableState<Boolean>,
    activity: MainActivity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
        title = {
            Column(modifier = Modifier.height(300.dp)) {
                Text(
                    text = "Select the backup file to restore",
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    color = MaterialTheme.colors.onPrimary
                )
                LazyColumn() {
                    items(listOfBackUpFiles.toMutableStateList()) { item ->
                        Text(
                            text = item.name,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.fontFamilyRegular,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colors.primary,
                                    shape = MaterialTheme.shapes.medium.copy(
                                        topStart = CornerSize(20.dp),
                                        topEnd = CornerSize(20.dp),
                                        bottomStart = CornerSize(20.dp),
                                        bottomEnd = CornerSize(20.dp),
                                    )
                                )
                                .fillMaxWidth()
                                .padding(15.dp)
                                .clickable {
                                    loadingDialog.value = true
                                    var db1 = AppModule().createDataBase(context)
                                    db1.close()
                                    val currentDBPath =
                                        context.getDatabasePath("notes").absolutePath
                                    val dst = File(currentDBPath)
                                    val localFile = File.createTempFile("notes", ".db")
                                    item
                                        .getFile(localFile)
                                        .addOnSuccessListener {
                                            try {
                                                if (localFile.exists()) {
                                                    var fileInputStream = FileInputStream(localFile)
                                                    var fileOutputStream = FileOutputStream(dst)
                                                    val inputChannel = fileInputStream.channel
                                                    val outputChannel = fileOutputStream.channel
                                                    outputChannel.transferFrom(
                                                        inputChannel,
                                                        0,
                                                        inputChannel.size()
                                                    )
                                                    fileInputStream.close()
                                                    fileOutputStream.close()
                                                    loadingDialog.value = false
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "Successfully restored notes",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                    val intent =
                                                        Intent(context, MainActivity::class.java)
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                    activity.finish()
                                                    context.startActivity(intent)
                                                } else {
                                                    Toast
                                                        .makeText(
                                                            context,
                                                            "Failed to restore the notes",
                                                            Toast.LENGTH_SHORT
                                                        )
                                                        .show()
                                                }
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Failed to restore the backup. Please try again",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                            loadingDialog.value = false
                                        }
                                    onDismiss()
                                }

                        )
                        Spacer(modifier = Modifier.height(10.dp))

                    }
                }
            }
        }, confirmButton = {
        }, dismissButton = {
            androidx.compose.material.OutlinedButton(
                onClick = { onDismiss() },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                ),
            ) {
                androidx.compose.material.Text(
                    text = "Cancel",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        })
}

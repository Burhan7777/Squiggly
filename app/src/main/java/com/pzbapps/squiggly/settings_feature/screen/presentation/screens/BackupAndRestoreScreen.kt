package com.pzbapps.squiggly.settings_feature.screen.presentation.screens

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.DatabaseBackupNameAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.DisplayBackupNamesAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.LoadingDialogBox


@Composable
fun BackupAndRestoreScreen(navHostController: NavHostController, activity: MainActivity) {
    val context = LocalContext.current
    var backUpFileName = remember { mutableStateOf("") }
    var showBackUpFIleNameAlertBox = remember { mutableStateOf(false) }
    val listOfBackUpFiles = remember { mutableStateOf<List<StorageReference>>(emptyList()) }
    var showListOfBackupFilesDialogBox = remember { mutableStateOf(false) }
    var loadingDialogWhenBackingUp = remember { mutableStateOf(false) }
    var loadingDialogWhenRestoring = remember { mutableStateOf(false) }

    val prefs = context.getSharedPreferences(Constant.AUTO_SAVE_PREF, MODE_PRIVATE)
    val autoSave = prefs.getBoolean(Constant.AUTO_SAVE_KEY, true)
    var autoSaveCheckedBox = remember { mutableStateOf(autoSave) }
    Column {
        Row() {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = "go Back",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            Text(
                text = "Cloud Backup and Restore",
                fontFamily = FontFamily.fontFamilyBold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(10.dp)
            )
        }

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
                    showBackUpFIleNameAlertBox.value = true
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
                    imageVector = Icons.Filled.Backup,
                    contentDescription = "Backup notes icon",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Column() {
                    Text(
                        text = "Backup Notes",
                        modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "This uploads notes backup to  cloud.",
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = FontStyle.Italic
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
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
                    var firebaseUserId = Firebase.auth.currentUser?.uid
                    var storageRef = Firebase.storage
                    val fileMetadataList = mutableListOf<Pair<StorageReference, Long>>()
                    var childStorage =
                        storageRef.reference.child("Notebook Database/$firebaseUserId/")
                    childStorage
                        .listAll()
                        .addOnSuccessListener { result ->
                            if (result.items.isNotEmpty()) {
                                result.items.forEach { fileRef ->
                                    fileRef
                                        .getMetadata()
                                        .addOnSuccessListener { metadata ->
                                            val lastModified = metadata.updatedTimeMillis ?: 0L
                                            fileMetadataList.add(Pair(fileRef, lastModified))

                                            // Check if we've retrieved metadata for all files
                                            if (fileMetadataList.size == result.items.size) {
                                                // Sort files by last modified time in descending order
                                                val sortedFiles =
                                                    fileMetadataList.sortedByDescending { it.second }
                                                listOfBackUpFiles.value =
                                                    sortedFiles.map { it.first } // Update your state
                                                showListOfBackupFilesDialogBox.value = true
                                            }
                                        }
                                        .addOnFailureListener {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "Failed to restore the backups",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                }
                            }
                        }
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
                    imageVector = Icons.Filled.Restore,
                    contentDescription = "Backup notes icon",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Column() {
                    Text(
                        text = "Restore Notes",
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
                        text = "This restores the notes saved on cloud.",
                        modifier = Modifier.padding(start = 10.dp),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = FontStyle.Italic
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
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
                        text = "Auto Save notes",
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
                        text = "This auto saves notes every 72 hours",
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
                        checked = autoSaveCheckedBox.value,
                        onCheckedChange = { autoSaveCheckedBox.value = it },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colors.onSecondary,
                            uncheckedColor = MaterialTheme.colors.onPrimary,
                            checkedColor = MaterialTheme.colors.onPrimary
                        ),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Please restart the app after restoring the data to see the full effect.",
            fontFamily = FontFamily.fontFamilyBold,
            fontSize = 20.sp,
            color = MaterialTheme.colors.onPrimary,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .background(
                    MaterialTheme.colors.primaryVariant,
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(20.dp),
                        topEnd = CornerSize(20.dp),
                        bottomStart = CornerSize(20.dp),
                        bottomEnd = CornerSize(20.dp),
                    )
                )
                .padding(20.dp)
        )
        if (showBackUpFIleNameAlertBox.value) {
            DatabaseBackupNameAlertBox(
                backUpFileName = backUpFileName,
                loadingDialogWhenBackingUp
            ) {
                showBackUpFIleNameAlertBox.value = false
            }
        }
        if (showListOfBackupFilesDialogBox.value) {
            DisplayBackupNamesAlertBox(
                listOfBackUpFiles = listOfBackUpFiles,
                loadingDialogWhenRestoring,
                activity = activity
            ) {
                showListOfBackupFilesDialogBox.value = false
            }
        }
        if (loadingDialogWhenBackingUp.value) {
            LoadingDialogBox(text = mutableStateOf("Uploading to cloud"))
        }
        if (loadingDialogWhenRestoring.value) {
            LoadingDialogBox(text = mutableStateOf("Restoring backup"))
        }
    }
    if (autoSaveCheckedBox.value) {
        val editor: SharedPreferences.Editor =
            context
                .getSharedPreferences(
                    Constant.AUTO_SAVE_PREF,
                    MODE_PRIVATE
                )
                .edit()
        editor.putBoolean(Constant.AUTO_SAVE_KEY, true)
        editor.apply()
    } else {
        val editor: SharedPreferences.Editor =
            context
                .getSharedPreferences(
                    Constant.AUTO_SAVE_PREF,
                    MODE_PRIVATE
                )
                .edit()
        editor.putBoolean(Constant.AUTO_SAVE_KEY, false)
        editor.apply()
    }
}


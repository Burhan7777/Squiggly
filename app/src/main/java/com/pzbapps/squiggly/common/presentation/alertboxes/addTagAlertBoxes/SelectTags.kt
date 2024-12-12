package com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.di.AppModule
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun SelectTags(
    lisOfTags: SnapshotStateList<Tag>,
    listOfSelectedTags: MutableState<ArrayList<String>>,
    viewModel: MainActivityViewModel,
    showAddTagAlertBox: MutableState<Boolean>,
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
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            showAddTagAlertBox.value = true
                        }
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add tag",
                        tint = MaterialTheme.colors.onPrimary
                    )
                    Text(
                        "Add Tag",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
                Divider(modifier = Modifier.height(5.dp))
                if (lisOfTags.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.height(150.dp)) {
                        items(lisOfTags) { item ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                var checkbox = remember { mutableStateOf(false) }
                                Checkbox(
                                    checked = checkbox.value,
                                    onCheckedChange = { checkbox.value = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.onPrimary,
                                        uncheckedColor = MaterialTheme.colors.onPrimary
                                    )
                                )
                                Text(
                                    text = item.name,
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    color = MaterialTheme.colors.onPrimary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                        .clickable {
                                            checkbox.value = !checkbox.value
                                            if (checkbox.value) {
                                                listOfSelectedTags.value.add(item.name)
                                            } else {
                                                listOfSelectedTags.value.remove(item.name)
                                            }
                                        }

                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }

                    }
                } else {
                    Text(
                        "No tags saved",
                        style = TextStyle.Default.copy(
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.fontFamilyRegular,
                            color = MaterialTheme.colors.onPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }
            }
        }, confirmButton = {
            androidx.compose.material.OutlinedButton(
                onClick = { onDismiss() },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onSecondary
                ),
                enabled = if (lisOfTags.isNotEmpty()) true else false
            ) {
                androidx.compose.material.Text(
                    text = "Save",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
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
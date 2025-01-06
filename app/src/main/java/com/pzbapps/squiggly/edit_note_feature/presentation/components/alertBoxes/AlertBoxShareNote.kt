package com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.edit_note_feature.presentation.components.convertMutableStateIntoString
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.bulletpoints.exportBulletPointsToDocx
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.bulletpoints.shareBulletPointsAsText
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.checkboxes.exportToDocxCheckBoxes
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.checkboxes.exportToTextCheckBoxes
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.notes.exportAndShareDocx
import com.pzbapps.squiggly.edit_note_feature.presentation.components.shareFiles.notes.sharePlainText
import exportAndShareBulletPointsPdf
import exportAndSharePDF
import exportAndSharePdfCheckBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertBoxShareNote(
    title: String,
    content: String,
    checkboxes: MutableState<ArrayList<Boolean>>,
    checkboxesText: SnapshotStateList<MutableState<String>>,
    converted: ArrayList<String>,
    listOfBulletPoints: SnapshotStateList<MutableState<String>>,
    convertedBulletPoints: ArrayList<String>,
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
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
            Column {
                Text(
                    text = "Share file as...",
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    color = MaterialTheme.colors.onPrimary
                )
                LazyColumn() {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .height(50.dp)
                                .clickable {
                                    if (viewModel.ifUserIsPremium.value) {
                                        if (checkboxes.value.size == 0 && listOfBulletPoints.size == 0) {
                                            exportAndSharePDF(context, title, content)
                                        } else if (listOfBulletPoints.size == 0) {
                                            convertMutableStateIntoString(checkboxesText, converted)
                                            exportAndSharePdfCheckBox(
                                                context,
                                                title,
                                                checkboxes.value,
                                                converted
                                            )
                                        } else {
                                            convertMutableStateIntoString(
                                                listOfBulletPoints,
                                                convertedBulletPoints
                                            )
                                            exportAndShareBulletPointsPdf(
                                                context,
                                                title,
                                                convertedBulletPoints
                                            )
                                        }
                                    } else {
                                        navHostController.navigate(Screens.PremiumPlanScreen.route)
                                    }
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            ),

                            ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Share as Pdf",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 10.dp
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    painter = painterResource(R.drawable.ic_premium),
                                    contentDescription = "Premium feature",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .height(50.dp)
                                .clickable {
                                    if (viewModel.ifUserIsPremium.value) {
                                        if (checkboxes.value.size == 0 && listOfBulletPoints.size == 0) {
                                            exportAndShareDocx(context, title, content)
                                        } else if (listOfBulletPoints.size == 0) {
                                            convertMutableStateIntoString(checkboxesText, converted)
                                            exportToDocxCheckBoxes(
                                                context,
                                                title,
                                                checkboxes.value,
                                                converted
                                            )
                                        } else {
                                            convertMutableStateIntoString(
                                                listOfBulletPoints,
                                                convertedBulletPoints
                                            )
                                            exportBulletPointsToDocx(
                                                context,
                                                title,
                                                convertedBulletPoints
                                            )
                                        }
                                    } else {
                                        navHostController.navigate(Screens.PremiumPlanScreen.route)
                                    }
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Share as .docx",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(
                                        horizontal = 10.dp,
                                        vertical = 10.dp
                                    )
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(
                                    painter = painterResource(R.drawable.ic_premium),
                                    contentDescription = "Premium feature",
                                    tint = Color.Unspecified,
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                                .height(50.dp)
                                .clickable {
                                    if (checkboxes.value.size == 0 && listOfBulletPoints.size == 0) {
                                        sharePlainText(context, title, content)
                                    } else if (listOfBulletPoints.size == 0) {
                                        convertMutableStateIntoString(checkboxesText, converted)
                                        exportToTextCheckBoxes(
                                            context,
                                            title,
                                            checkboxes.value,
                                            converted
                                        )
                                    } else {
                                        convertMutableStateIntoString(
                                            listOfBulletPoints,
                                            convertedBulletPoints
                                        )
                                        shareBulletPointsAsText(
                                            context,
                                            title,
                                            convertedBulletPoints
                                        )
                                    }
                                },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            )
                        ) {
                            Text(
                                "Share as text",
                                color = MaterialTheme.colors.onPrimary,
                                fontFamily = FontFamily.fontFamilyRegular,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
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
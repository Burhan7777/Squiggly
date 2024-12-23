package com.pzbapps.squiggly.notebook_main_screen.presentation.components.alertboxes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

@Composable
fun DeleteAllNotesToo(
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    name: String,
    showDeleteNotebookDialogBox: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
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
        containerColor = MaterialTheme.colors.primaryVariant,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Text(
                text = "What about notes...",
                fontFamily = FontFamily.fontFamilyBold,
                fontSize = 15.sp,
                color = MaterialTheme.colors.onPrimary
            )
        },
        text = {
            Text(
                text = "If yes then this will move notes to trash ",
                fontFamily = FontFamily.fontFamilyRegular,
                color = MaterialTheme.colors.onPrimary
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.getAllNotesByNotebook(name)
                    var listOfNotebooks = viewModel.listOfNotesByNotebook
                    for (i in listOfNotebooks) {
                        val notebook = i.copy(
                            deletedNote = true,
                            timePutInTrash = System.currentTimeMillis()
                        )
                        viewModel.updateNote(notebook)
                    }
                    onDismiss()
                    showDeleteNotebookDialogBox.value = true


                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                Text(text = "Delete notes", fontFamily = FontFamily.fontFamilyRegular)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    viewModel.getAllNotesByNotebook(name)
                    var listOfNotebooks = viewModel.listOfNotesByNotebook
                    for (i in listOfNotebooks) {
                        val notebook = i.copy(
                            notebook = Constant.NOT_CATEGORIZED
                        )
                        viewModel.updateNote(notebook)
                    }
                    onDismiss()
                    showDeleteNotebookDialogBox.value = true
                },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
            ) {
                Text(text = "Save notes", fontFamily = FontFamily.fontFamilyRegular)
            }
        }
    )
}
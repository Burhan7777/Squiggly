package com.pzbapps.squiggly.main_screen.textrecognition

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note

@Composable
fun ShowRecognizedText(
    text: MutableState<String>,
    showRecognizedText: MutableState<StringBuilder>,
    viewModel: MainActivityViewModel,
    onDismiss: () -> Unit
) {

    val title = remember { mutableStateOf("") }
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
            Column {
                androidx.compose.material3.OutlinedTextField(
                    value = title.value,
                    onValueChange = { title.value = it },
                    label = {
                        Text(
                            "Title",
                            fontSize = 15.sp,
                            fontFamily = FontFamily.fontFamilyRegular,
                            color = androidx.compose.material.MaterialTheme.colors.onPrimary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        unfocusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        focusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        unfocusedBorderColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        cursorColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 15.sp
                    ),
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(15.dp),
                        topEnd = CornerSize(15.dp),
                        bottomEnd = CornerSize(15.dp),
                        bottomStart = CornerSize(15.dp),
                    )
                )
                androidx.compose.material3.OutlinedTextField(
                    value = text.value,
                    onValueChange = { text.value = it },
                    label = {

                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        unfocusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        focusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        unfocusedBorderColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        cursorColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 15.sp
                    ),
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(15.dp),
                        topEnd = CornerSize(15.dp),
                        bottomEnd = CornerSize(15.dp),
                        bottomStart = CornerSize(15.dp),
                    ),
                    modifier = Modifier.height(300.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val note = Note(
                        title = title.value,
                        content = showRecognizedText.value.toString(),
                        timeModified = System.currentTimeMillis(),
                        notebook = Constant.NOT_CATEGORIZED,
                        timeStamp = System.currentTimeMillis()
                    )
                    viewModel.insertNote(note)
                    onDismiss()

                },
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                Text(
                    text = "Save note",
                    color = androidx.compose.material.MaterialTheme.colors.onSecondary
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    onDismiss()
                },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(1.dp,  androidx.compose.material.MaterialTheme.colors.onPrimary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = androidx.compose.material.MaterialTheme.colors.onSecondary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    text = "Cancel",
                    color = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
            }
        }
    )
}
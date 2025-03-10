package com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.edit_note_feature.domain.usecase.getPasswordFromFirebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AlertDialogBoxEnterPasswordToUnlock(
    viewModel: MainActivityViewModel,
    id: Int,
    activity: MainActivity,
    navHostController: NavHostController,
    title: String,
    content: String,
    listOfCheckedNotes: ArrayList<String>,
    listOfCheckBoxes: ArrayList<Boolean>,
    listOfBulletPoints: ArrayList<String>,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val enteredPassword = remember { mutableStateOf("") }
    androidx.compose.material3.AlertDialog(onDismissRequest = {
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
        text = {
            Column {
                Text(
                    text = "Enter password to unlock the note. ",
                    fontFamily = FontFamily.fontFamilyRegular,
                    color = MaterialTheme.colors.onPrimary
                )
                OutlinedTextField(
                    value = enteredPassword.value,
                    onValueChange = { enteredPassword.value = it },
                    label = {
                        androidx.compose.material3.Text(
                            text = "Confirm Password",
                            color = MaterialTheme.colors.onPrimary,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.fontFamilyRegular,

                            )
                    },

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colors.onPrimary,
                        unfocusedTextColor = MaterialTheme.colors.onPrimary,
                        focusedTextColor = MaterialTheme.colors.onPrimary,
                        unfocusedBorderColor = MaterialTheme.colors.onPrimary,
                        cursorColor = MaterialTheme.colors.onPrimary
                    ),
                    textStyle = TextStyle.Default.copy(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.fontFamilyRegular
                    ),
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(15.dp),
                        topEnd = CornerSize(15.dp),
                        bottomEnd = CornerSize(15.dp),
                        bottomStart = CornerSize(15.dp),
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val result = getPasswordFromFirebase()
                    result.observe(activity) {
                        if (it == enteredPassword.value.trim()) {
                            unlockTheNote(viewModel, id, scope, navHostController, context)
                        } else if (it == Constant.FAILURE) {
                            Toast.makeText(
                                activity,
                                "Failed to get the password from server. Please try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (it != enteredPassword.value) {
                            Toast.makeText(activity, "Wrong Password", Toast.LENGTH_SHORT).show()
                        }
                    }
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
                Text(text = "Unlock Note", fontFamily = FontFamily.fontFamilyRegular)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = { onDismiss() },
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
                Text(text = "Cancel", fontFamily = FontFamily.fontFamilyRegular)
            }
        }
    )
}

fun unlockTheNote(
    viewModel: MainActivityViewModel,
    id: Int,
    scope: CoroutineScope,
    navHostController: NavHostController,
    context: Context
) {
    scope.launch {
        viewModel.getNoteById(id)
        var note = viewModel.getNoteById.value
        var note1 = note.copy(
            locked = false
        )
        viewModel.updateNote(note1)
        delay(200)
        viewModel.getNoteById(id)
        var note2 = viewModel.getNoteById.value
        if (note2.locked) {
            println("UNLOCKED TRIGGERED")
            unlockTheNote(viewModel, id, scope, navHostController, context)
        } else {

            delay(300)
            navHostController.navigateUp()
            delay(200)
            Toast.makeText(context, "Note has been unlocked", Toast.LENGTH_SHORT).show()
        }
    }
}
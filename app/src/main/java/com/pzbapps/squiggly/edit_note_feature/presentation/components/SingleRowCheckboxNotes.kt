package com.pzbapps.squiggly.edit_note_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.ShowShortToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleRowCheckBoxNotes(
    note: MutableState<String>,
    index: Int,
    listOfCheckboxes: MutableState<ArrayList<Boolean>>,
    listOfCheckboxText: SnapshotStateList<MutableState<String>>,
    count: MutableState<Int>,
    focusRequester: FocusRequester,
    focusRequesters: SnapshotStateList<FocusRequester>,
    isNewCheckboxCreated: MutableState<Boolean>,
    backgroundColor: MutableState<Int>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>,
    onDelete: () -> Unit

) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusRequester = remember { FocusRequester() }
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    LaunchedEffect(key1 = listOfCheckboxText.size) {
        listOfCheckboxes.value.add(false)

    }
    var coroutine = rememberCoroutineScope()

    // println("TEXT:$listOfCheckboxes")

    var checkBox = rememberSaveable { mutableStateOf(false) }
    try {
        for (i in listOfCheckboxes.value) {
            checkBox.value = listOfCheckboxes.value[index]
        }
    } catch (exception: IndexOutOfBoundsException) {
        ShowShortToast("Some error with the note")
    }
//    for(i in listOfCheckboxes)
//    LaunchedEffect(key1 = i) {
//        listOfCheckboxes[index] = i
//    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkBox.value,
            onCheckedChange = {
                checkBox.value = it
                listOfCheckboxes.value[index] = it
//                coroutine.launch {
//                    delay(300)
//                    count.value++
//                }
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.onPrimary,
                checkmarkColor = MaterialTheme.colors.onSecondary,
                uncheckedColor = MaterialTheme.colors.onPrimary
            ),
            modifier = Modifier.clickable {


            }
        )
        OutlinedTextField(
            value = note.value, onValueChange = {
                note.value = it

                // Extract the string values from the SnapshotStateList
                // val stringList = listOfCheckboxText.map { value }

                // Check if the string value is already present
                listOfCheckboxText[index].value = it
                ///  println("INDEX:$index")
                //  println("TEXT2:${listOfCheckboxText.toCollection(ArrayList())}")
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    count.value++
                    listOfCheckboxText.add(mutableStateOf(""))
                    coroutine.launch {
                        // Delay for one frame to ensure the new item is created before requesting focus
                        kotlinx.coroutines.delay(100)
                        isNewCheckboxCreated.value = true
                        if (index < focusRequesters.size - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                }
            ),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = Color(backgroundColor.value),
                unfocusedContainerColor = Color(backgroundColor.value),
                focusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedIndicatorColor = Color(backgroundColor.value),
                focusedIndicatorColor = Color(backgroundColor.value),
                cursorColor = MaterialTheme.colors.onPrimary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = Color.Gray
                )
            ),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = fontFamily.value
            ),
            modifier = Modifier
                .clickable {

                }
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            trailingIcon = {
                IconButton(onClick = {
                    onDelete()
                    listOfCheckboxText.removeAt(index)
                    listOfCheckboxes.value.removeAt(index)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }

            })
    }
}
package com.pzbapps.squiggly.locked_notes_feature.presentation.components.ChecboxLockedNotesComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleRowCheckBoxLockedNotes(
    text: MutableState<String>,
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListOfCheckBoxes: ArrayList<Boolean>,
    index: Int,
    count: MutableState<Int>,
    backgroundColor: MutableState<Color>
) {
    var checkBox = rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current


    LaunchedEffect(key1 = checkBox.value) {
        mutableListOfCheckBoxes[index] = checkBox.value
    }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checkBox.value,
            onCheckedChange = {
                checkBox.value = it
            },
            colors = androidx.compose.material3.CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.onPrimary,
                checkmarkColor = MaterialTheme.colors.onSecondary,
                uncheckedColor = MaterialTheme.colors.onPrimary
            )
        )
        OutlinedTextField(
            value = text.value, onValueChange = { text.value = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    count.value++
                    mutableList.add(mutableStateOf(""))
                }
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor.value,
                unfocusedContainerColor = backgroundColor.value,
                focusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedIndicatorColor = backgroundColor.value,
                focusedIndicatorColor = backgroundColor.value,
                cursorColor = MaterialTheme.colors.onPrimary,
                selectionColors = TextSelectionColors(
                    handleColor = MaterialTheme.colors.onPrimary,
                    backgroundColor = Color.Gray
                )
            ),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.fontFamilyRegular
            ),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            trailingIcon = {
                IconButton(onClick = { mutableList.removeAt(index) }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        )

    }
}

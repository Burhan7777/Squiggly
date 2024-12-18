package com.pzbapps.squiggly.notebook_main_screen.presentation.components.bulletpointsScreenComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.presentation.FontFamily

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleRowBulletPointNotebook(
    text: MutableState<String>,
    mutableListOfBulletPointsNotes: SnapshotStateList<MutableState<String>>,
    index: Int,
    count: MutableState<Int>,
    focusRequester: FocusRequester,
    backgroundColor: MutableState<Color>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>,
    onDelete: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current


//    val focusRequester = remember { FocusRequester() }
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(id = R.drawable.bullet_point),
            contentDescription = "Bullet Point",
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
        )
        OutlinedTextField(
            value = text.value, onValueChange = { text.value = it },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    count.value++
                    mutableListOfBulletPointsNotes.add(mutableStateOf(""))
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
                fontFamily = fontFamily.value
            ),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    if (it.isFocused) {
                        keyboardController?.show()
                    }
                },
            trailingIcon = {
                IconButton(onClick = {
                    onDelete()
                    mutableListOfBulletPointsNotes.removeAt(index)
                }) {
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

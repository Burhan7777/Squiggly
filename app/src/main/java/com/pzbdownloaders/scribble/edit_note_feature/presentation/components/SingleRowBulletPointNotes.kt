package com.pzbdownloaders.scribble.edit_note_feature.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import com.pzbdownloaders.scribble.R
import com.pzbdownloaders.scribble.common.presentation.FontFamily

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleRoaBulletPointNotes(
    note: MutableState<String>,
    index: Int,
    listOfBulletPoints: SnapshotStateList<MutableState<String>>

) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


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
            value = note.value, onValueChange = {
                note.value = it
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { listOfBulletPoints.add(mutableStateOf("")) }
            ),
            colors = androidx.compose.material3.TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colors.primary,
                unfocusedContainerColor = MaterialTheme.colors.primary,
                focusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary
            ),
            textStyle = LocalTextStyle.current.copy(
                fontFamily = FontFamily.fontFamilyRegular
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
                IconButton(onClick = { listOfBulletPoints.removeAt(index) }) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }

            })
    }
}
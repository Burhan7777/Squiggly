package com.pzbapps.squiggly.edit_note_feature.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SingleRoaBulletPointNotes(
    note: MutableState<String>,
    index: Int,
    listOfBulletPoints: SnapshotStateList<MutableState<String>>,
    countBullet: MutableState<Int>,
    focusRequester: FocusRequester,
    focusRequesters: SnapshotStateList<FocusRequester>,
    backgroundColor: MutableState<Int>,
    //isNewCheckBoxAdded: MutableState<Boolean>,
    onDelete: () -> Unit

) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    val focusRequester = remember { FocusRequester() }
//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    var coroutine = rememberCoroutineScope()

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
                onNext = {
                    countBullet.value++
                    listOfBulletPoints.add(mutableStateOf(""))
                    coroutine.launch {
                        // Delay for one frame to ensure the new item is created before requesting focus
                        kotlinx.coroutines.delay(100)
                        //   isNewCheckBoxAdded.value = true
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
                IconButton(onClick = {
                    onDelete()
                    listOfBulletPoints.removeAt(index)
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
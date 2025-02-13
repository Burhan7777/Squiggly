package com.pzbapps.squiggly.bubble_note_feature.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.presentation.FontFamily

@Composable
fun BulletPointScreen() {
    var title = remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    val focusRequesters = remember { mutableStateListOf(FocusRequester()) }
    var mutableListConverted = rememberSaveable {
        ArrayList<String>()
    }
    var mutableListOfBulletPointsNotes =
        RememberSaveableSnapshotStateList()
    var count = remember { mutableStateOf(0) }
    LaunchedEffect(key1 = true) {
        if (mutableListOfBulletPointsNotes.isEmpty()) {
            mutableListOfBulletPointsNotes.add(mutableStateOf(""))
        }
    }
    Column {
        androidx.compose.material.TextField(
            value = title.value,
            onValueChange = { title.value = it },
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary,
                textColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyRegular, fontSize = 25.sp)
        )

        LazyColumn(state = lazyListState) {
            itemsIndexed(mutableListOfBulletPointsNotes) { indexed, item ->
                if (indexed >= focusRequesters.size) {
                    focusRequesters.add(FocusRequester())
                }
                val focusRequester = focusRequesters[indexed]
                SingleRowBulletPoint(
                    text = item,
                    mutableListOfBulletPointsNotes,
                    indexed,
                    count,
                    focusRequester,
                    mutableStateOf(MaterialTheme.colors.primary),
                    mutableStateOf(FontFamily.fontFamilyRegular),
                    onDelete = {
                        try {
                            focusRequesters.removeAt(indexed)
                            mutableListConverted.removeAt(indexed)
                        } catch (exception: IndexOutOfBoundsException) {

                        }
                    }
                )
            }
        }
    }
}

fun convertMutableStateIntoString(
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>
) {
    for (i in mutableList) {
        if (!mutableListConverted.contains(i.value)) {
            mutableListConverted.add(i.value)
        }
    }
}
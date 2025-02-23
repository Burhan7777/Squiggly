package com.pzbapps.squiggly.bubble_note_feature.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.presentation.BubbleNoteViewModel
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.main_screen.domain.model.Note

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckBoxScreen(
    title: MutableState<String>,
    listOfCheckboxTexts: SnapshotStateList<MutableState<String>>,
    mutableListOfCheckBoxes: ArrayList<Boolean>,
    viewModel: BubbleNoteViewModel,
    onClose: () -> Unit
) {
    val focusRequesters = remember { mutableStateListOf(FocusRequester()) }
    var count = remember { mutableStateOf(0) }

    val lazyListState = rememberLazyListState()

    var mutableListConverted = rememberSaveable {
        ArrayList<String>()
    }



    LaunchedEffect(key1 = true) {
        if (listOfCheckboxTexts.isEmpty()) {
            listOfCheckboxTexts.add(mutableStateOf(""))
        }

    }

    LaunchedEffect(key1 = listOfCheckboxTexts.size) {
        mutableListOfCheckBoxes.add(false)
    }
    Column() {
        androidx.compose.material.TextField(
            value = title.value,
            onValueChange = { title.value = it },
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary,
                textColor = MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(
                fontFamily = FontFamily.fontFamilyRegular,
                fontSize = 20.sp
            )
        )

        val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)

        LazyColumn(
            state = lazyListState,
            modifier = Modifier.imePadding()
        ) {
            itemsIndexed(listOfCheckboxTexts) { index, item ->
                if (index >= focusRequesters.size) {
                    focusRequesters.add(FocusRequester())
                }
                val focusRequester = focusRequesters[index]
                SingleRowCheckBox(
                    text = item,
                    mutableList = listOfCheckboxTexts,
                    mutableListOfCheckBoxes = mutableListOfCheckBoxes,
                    index = index,
                    count = count,
                    focusRequester = focusRequester,
                    backgroundColor = mutableStateOf(MaterialTheme.colors.primary),
                    fontFamily = remember { mutableStateOf(FontFamily.fontFamilyRegular) },
                ) {
                    try {
                        focusRequesters.removeAt(index)
                        listOfCheckboxTexts.removeAt(index)
                        //mutableListConverted.removeAt(indexed)
                    } catch (exception: IndexOutOfBoundsException) {

                    }
                }
            }
            items(1) {
                Spacer(modifier = Modifier.height(imeHeight.dp))
            }
            item {
                Spacer(modifier = Modifier.height(5.dp))
                Button(onClick = {
                    saveCheckbox(
                        title.value,
                        listOfCheckboxTexts,
                        mutableListOfCheckBoxes,
                        mutableListConverted,
                        viewModel
                    )
                    onClose()
                }) {
                    Text("Save")
                }
            }
        }
    }
    LaunchedEffect(count.value) {
        if (listOfCheckboxTexts.size > 1) {
            lazyListState.animateScrollToItem(listOfCheckboxTexts.lastIndex)
            focusRequesters.lastOrNull()
                ?.requestFocus()  // Move focus to the last added checkbox
        }
   }
    LaunchedEffect(focusRequesters, listOfCheckboxTexts) {
//        if (listOfCheckboxTexts.isNotEmpty()) {
//            // Delay focus request to ensure the UI is composed
//            focusRequesters.firstOrNull()?.let { firstFocusRequester ->
//                // Add a small delay to ensure everything is composed
//                kotlinx.coroutines.delay(100)
//                firstFocusRequester.requestFocus()
//            }
//        }
    }

}

fun saveCheckbox(
    title: String,
    listOfCheckboxTexts: SnapshotStateList<MutableState<String>>,
    mutableListOfCheckBoxes: ArrayList<Boolean>,
    mutableListConverted: ArrayList<String>,
    viewModel: BubbleNoteViewModel
) {
    var convertedList = convertMutableStateIntoString(
        listOfCheckboxTexts,
        mutableListConverted
    )
    var note = Note(
        title = title,
        listOfCheckedNotes = convertedList,
        listOfCheckedBoxes = mutableListOfCheckBoxes,
        timeStamp = System.currentTimeMillis(),
    )
    viewModel.insertNote(note)
}

@Composable
fun RememberSaveableSnapshotStateList(): SnapshotStateList<MutableState<String>> {
    // Create a custom saver for SnapshotStateList<Mutable<String>>
    val listSaver = Saver<SnapshotStateList<MutableState<String>>, List<List<String>>>(
        save = { snapshotStateList ->
            // Convert SnapshotStateList<Mutable<String>> to List<List<String>> for saving
            snapshotStateList.map { state -> listOf(state.value) }
        },
        restore = { savedList ->
            // Convert List<List<String>> back to SnapshotStateList<Mutable<String>> on restore
            val restoredList = savedList.map { mutableStateOf(it.first()) }
            SnapshotStateList<MutableState<String>>().apply {
                addAll(restoredList)
            }
        })
    return rememberSaveable(saver = listSaver) {
        mutableStateListOf<MutableState<String>>() // Initial state
    }
}

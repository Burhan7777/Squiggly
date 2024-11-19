package com.pzbapps.squiggly.locked_notes_feature.presentation.components.BulletPointsLockedNotesComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BulletPointLockedNotes(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    mutableListOfBulletPointsNotes: SnapshotStateList<MutableState<String>>,
    count: MutableState<Int>,
    mutableListConverted: ArrayList<String>,
    backgroundColor: MutableState<androidx.compose.ui.graphics.Color>
) {

    val context = LocalContext.current
    var dialogOpen = remember {
        mutableStateOf(false)
    }

    val notebookText = remember {
        mutableStateOf("")
    }

    viewModel.getNoteBook()

    val focusRequesters = remember { mutableStateListOf(FocusRequester()) }

    val imeVisible = WindowInsets.isImeVisible

    val lazyListState = rememberLazyListState()


    viewModel.getNoteBook()
    val notebooks: ArrayList<String> =
        arrayListOf("Add Notebook")

//    for (i in listOfNoteBooks?.indices ?: arrayListOf<GetNoteBook>().indices) {
//        notebooks.add(listOfNoteBooks!![i]?.notebook ?: GetNoteBook().notebook)
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value)
    ) {

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
                backgroundColor = backgroundColor.value,
                focusedIndicatorColor = backgroundColor.value,
                unfocusedIndicatorColor = backgroundColor.value,
                cursorColor = MaterialTheme.colors.onPrimary,
                textColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyBold, fontSize = 25.sp)
        )
        var firstCheckBoxCheck = remember {
            mutableStateOf(true)
        }
        if (firstCheckBoxCheck.value) {
            //CreateCheckBox(firstCheckBoxCheck)
        }

        Column(
            modifier = Modifier
                .padding(bottom = if (imeVisible) WindowInsets.ime.getBottom((LocalDensity.current)).dp else 0.dp)
        ) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(mutableListOfBulletPointsNotes) { indexed, item ->
                    if (indexed >= focusRequesters.size) {
                        focusRequesters.add(FocusRequester())
                    }
                    val focusRequester = focusRequesters[indexed]
                    SingleRowBulletPointLockedNotes(
                        text = item,
                        mutableListOfBulletPointsNotes,
                        indexed,
                        count,
                        focusRequester,
                        backgroundColor,
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
        LaunchedEffect(count.value) {
            if (mutableListOfBulletPointsNotes.size > 1) {
                lazyListState.animateScrollToItem(mutableListOfBulletPointsNotes.size - 1)
                focusRequesters.lastOrNull()
                    ?.requestFocus()  // Move focus to the last added checkbox
            }
        }
        LaunchedEffect(focusRequesters, mutableListOfBulletPointsNotes) {
            if (mutableListOfBulletPointsNotes.isNotEmpty()) {
                // Delay focus request to ensure the UI is composed
                focusRequesters.firstOrNull()?.let { firstFocusRequester ->
                    // Add a small delay to ensure everything is composed
                    kotlinx.coroutines.delay(100)
                    firstFocusRequester.requestFocus()
                }
            }
        }
    }
}

package com.pzbapps.squiggly.notebook_main_screen.presentation.components.bulletpointsScreenComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.AddTag
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.SelectTags

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterialApi::class)
@Composable
fun BulletPointNotebook(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    mutableListOfBulletPointsNotes: SnapshotStateList<MutableState<String>>,
    count: MutableState<Int>,
    mutableListConverted: ArrayList<String>,
    backgroundColor: MutableState<Color>,
    listOfSelectedTags: SnapshotStateList<String>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>
) {

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

    val showSelectTagAlertBox = remember { mutableStateOf(false) }
    val showAddTagAlertBox = remember { mutableStateOf(false) }

    val showAddingTagDialogBox = remember { mutableStateOf(false) } // THIS DIALOG BOX IS SHOWN
    // WHEN WE PRESS SAVE BUTTON IN ADD TAG DIALOG BOX


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

        if (showSelectTagAlertBox.value) {
            SelectTags(viewModel.tags, listOfSelectedTags, viewModel, showAddTagAlertBox) {
                showSelectTagAlertBox.value = false
            }
        }

        if (showAddTagAlertBox.value) {
            AddTag(viewModel,showAddingTagDialogBox,listOfSelectedTags,showSelectTagAlertBox) {
                showAddTagAlertBox.value = false
            }
        }

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
            textStyle = TextStyle(fontFamily = fontFamily.value, fontSize = 25.sp)
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
                    SingleRowBulletPointNotebook(
                        text = item,
                        mutableListOfBulletPointsNotes,
                        indexed,
                        count,
                        focusRequester,
                        backgroundColor,
                        fontFamily,
                        onDelete = {
                            try {
                                focusRequesters.removeAt(indexed)
                                mutableListConverted.removeAt(indexed)
                            } catch (exception: IndexOutOfBoundsException) {
                            }
                        }
                    )

                }
                item {
                    Text(
                        "Tags",
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    LazyRow(
                    ) {
                        items(listOfSelectedTags) { item ->
                            androidx.compose.material.Chip(onClick = {},
                                modifier = Modifier.padding(5.dp),
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = MaterialTheme.colors.onPrimary,
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Remove from list",
                                        tint = MaterialTheme.colors.onSecondary,
                                        modifier = Modifier.clickable {
                                            listOfSelectedTags.remove(item)
                                        }
                                    )
                                }
                            ) {
                                Text(
                                    item,
                                    color = MaterialTheme.colors.onSecondary,
                                    fontFamily = FontFamily.fontFamilyRegular
                                )
                            }
                        }
                        item {
                            androidx.compose.material.Chip(
                                modifier = Modifier.padding(5.dp),
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = MaterialTheme.colors.primaryVariant,
                                    contentColor = MaterialTheme.colors.onPrimary
                                ),
                                onClick = {
                                    showSelectTagAlertBox.value = true

                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Add Tag",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }) {
                                Text(
                                    text = "Add Tag",
                                    color = MaterialTheme.colors.onPrimary,
                                    fontFamily = FontFamily.fontFamilyRegular
                                )
                            }
                        }
                    }
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

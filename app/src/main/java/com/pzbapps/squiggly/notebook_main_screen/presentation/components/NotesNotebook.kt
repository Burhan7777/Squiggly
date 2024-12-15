package com.pzbapps.squiggly.notebook_main_screen.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.main_screen.presentation.components.SingleItemNoteList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotesNotebook(
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    title: String,
    showUnlockDialogBox: MutableState<Boolean>,
    showEditTagsAlertBox: MutableState<Boolean>,
) {

    val fontFamilyExtraLight = Font(R.font.lufgaextralight).toFontFamily()

    // viewModel.getNotebookNote(title)
    // val listOfNotes = viewModel.getNotebookNotes.observeAsState().value


    viewModel.getAllNotesByNotebook(title)
    //var listOfNotes = viewModel.listOfNotesByNotebook
    var listOfNotesBooks = remember { SnapshotStateList<Note>() }
    var listOfPinnedNotes = ArrayList<Note>()

    viewModel.listOfNotesByNotebookLiveData.observe(activity) {

        listOfPinnedNotes.clear()
        listOfNotesBooks = it.toMutableStateList()
        for (i in it) {
            if (i.notePinned) {
                listOfPinnedNotes.add(i)
            }
        }
        if (viewModel.listOfLockedNotebooksNote.isEmpty()) {
            // viewModel.listlockedNotes.clear()
            for (i in it) {
                if (i.notebook == title && i.locked) {
                    viewModel.listOfLockedNotebooksNote.add(i)
                }
            }
        }
    }


    if (viewModel.showGridOrLinearNotes.value) {
        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Adaptive(160.dp)) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Column {
                    Text(
                        text = "My",
                        fontFamily = fontFamilyExtraLight,
                        fontSize = 65.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Text(
                        text = title,
                        fontFamily = fontFamilyExtraLight, fontSize = 45.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Tags",
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit tags",
                            tint = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    showEditTagsAlertBox.value = true
                                }
                        )
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(viewModel.tags) { index, item ->
                            androidx.compose.material.Chip(onClick = {
                                if (!viewModel.selectedTags.contains(index)) {
                                    viewModel.selectedTags.add(index)
                                } else {
                                    viewModel.selectedTags.remove(index)
                                }


                                if (viewModel.selectedTags.contains(index)) {
                                    viewModel.filteredNotesByTag.addAll(listOfNotesBooks.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                } else {
                                    viewModel.filteredNotesByTag.removeAll(listOfNotesBooks.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                }

                                if (viewModel.selectedTags.isEmpty()) {
                                    viewModel.filteredNotesByTag.clear()
                                }

                            },
                                modifier = Modifier.padding(5.dp),
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = if (viewModel.selectedTags.contains(index)) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant,
                                ),
                                leadingIcon = {
                                    if (viewModel.selectedTags.contains(index)) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Filter notes by tag",
                                            tint = MaterialTheme.colors.onSecondary,
                                            modifier = Modifier.clickable {
                                                if (!viewModel.selectedTags.contains(index)) {
                                                    viewModel.selectedTags.add(index)
                                                } else {
                                                    viewModel.selectedTags.remove(index)
                                                }


                                                if (viewModel.selectedTags.contains(index)) {
                                                    viewModel.filteredNotesByTag.addAll(
                                                        listOfNotesBooks.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                } else {
                                                    viewModel.filteredNotesByTag.removeAll(
                                                        listOfNotesBooks.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                }

                                                if (viewModel.selectedTags.isEmpty()) {
                                                    viewModel.filteredNotesByTag.clear()
                                                }

                                            }
                                        )
                                    }
                                }
                            ) {
                                Text(
                                    item.name,
                                    color = if (viewModel.selectedTags.contains(index)) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                                    fontFamily = FontFamily.fontFamilyRegular
                                )
                            }
                        }
                    }
                }
            }

            if (listOfPinnedNotes.size > 0) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = "PINNED",
                        fontFamily = FontFamily.fontFamilyBold, fontSize = 15.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(listOfPinnedNotes) { note ->
                SingleItemNotebookList(
                    note = note,
                    navHostController = navHostController,
                    title,
                    viewModel.showLockedNotes
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "ALL NOTES",
                    fontFamily = FontFamily.fontFamilyBold, fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
            }
            if (viewModel.filteredNotesByTag.isEmpty() && viewModel.selectedTags.isNotEmpty()) {
                item {
                    Text(
                        "This tag has no notes.",
                        fontFamily = FontFamily.fontFamilyRegular,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            } else if (viewModel.filteredNotesByTag.isEmpty()) {
                items(
                    listOfNotesBooks ?: emptyList()
                ) { note ->
                    SingleItemNotebookList(
                        note = note,
                        navHostController,
                        title,
                        viewModel.showLockedNotes
                    )
                }
            } else {
                items(
                    viewModel.filteredNotesByTag ?: emptyList()
                ) { note ->
                    SingleItemNotebookList(
                        note = note,
                        navHostController,
                        title,
                        viewModel.showLockedNotes
                    )
                }
            }
            if (!viewModel.showLockedNotes.value && viewModel.listOfLockedNotebooksNote.isNotEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),

                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center

                        ) {
                            Text(
                                if (viewModel.listOfLockedNotebooksNote.size == 1) "1 locked note" else "${viewModel.listOfLockedNotebooksNote.size} locked notes",
                                fontFamily = FontFamily.fontFamilyRegular,
                                fontSize = 15.sp,
                                color = MaterialTheme.colors.onPrimary
                            )
                            TextButton(onClick = {
                                showUnlockDialogBox.value = true
                            }) {
                                Text(
                                    "Show",
                                    fontFamily = FontFamily.fontFamilyBold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colors.onPrimary,
                                    textDecoration = TextDecoration.Underline
                                )
                            }
                        }
                    }
                }
            } else {
                items(viewModel.listOfLockedNotebooksNote) { note ->
                    SingleItemUnlockNotebookList(
                        note,
                        navHostController,
                        title,
                        viewModel.showLockedNotes
                    )
                }
            }
        }
    } else {
        LazyColumn() {
            item {
                Column {
                    Text(
                        text = "My",
                        fontFamily = fontFamilyExtraLight,
                        fontSize = 65.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Text(
                        text = title,
                        fontFamily = fontFamilyExtraLight, fontSize = 45.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Tags",
                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(start = 10.dp),
                        )
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit tags",
                            tint = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    showEditTagsAlertBox.value = true
                                }
                        )
                    }
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(viewModel.tags) { index, item ->
                            androidx.compose.material.Chip(onClick = {
                                if (!viewModel.selectedTags.contains(index)) {
                                    viewModel.selectedTags.add(index)
                                } else {
                                    viewModel.selectedTags.remove(index)
                                }


                                if (viewModel.selectedTags.contains(index)) {
                                    viewModel.filteredNotesByTag.addAll(listOfNotesBooks.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                } else {
                                    viewModel.filteredNotesByTag.removeAll(listOfNotesBooks.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                }

                                if (viewModel.selectedTags.isEmpty()) {
                                    viewModel.filteredNotesByTag.clear()
                                }

                            },
                                modifier = Modifier.padding(5.dp),
                                colors = ChipDefaults.chipColors(
                                    backgroundColor = if (viewModel.selectedTags.contains(index)) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant,
                                ),
                                leadingIcon = {
                                    if (viewModel.selectedTags.contains(index)) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = "Filter notes by tag",
                                            tint = MaterialTheme.colors.onSecondary,
                                            modifier = Modifier.clickable {
                                                if (!viewModel.selectedTags.contains(index)) {
                                                    viewModel.selectedTags.add(index)
                                                } else {
                                                    viewModel.selectedTags.remove(index)
                                                }


                                                if (viewModel.selectedTags.contains(index)) {
                                                    viewModel.filteredNotesByTag.addAll(
                                                        listOfNotesBooks.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                } else {
                                                    viewModel.filteredNotesByTag.removeAll(
                                                        listOfNotesBooks.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                }

                                                if (viewModel.selectedTags.isEmpty()) {
                                                    viewModel.filteredNotesByTag.clear()
                                                }

                                            }
                                        )
                                    }
                                }
                            ) {
                                Text(
                                    item.name,
                                    color = if (viewModel.selectedTags.contains(index)) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                                    fontFamily = FontFamily.fontFamilyRegular
                                )
                            }
                        }
                    }
                }
            }
            if (listOfPinnedNotes.size > 0) {
                item {
                    Text(
                        text = "PINNED",
                        fontFamily = FontFamily.fontFamilyBold, fontSize = 15.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            items(listOfPinnedNotes) { note ->
                SingleItemNotebookList(
                    note = note,
                    navHostController = navHostController,
                    title,
                    viewModel.showLockedNotes
                )
            }
            item {
                Text(
                    text = "ALL NOTES",
                    fontFamily = FontFamily.fontFamilyBold, fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
            }
            if (viewModel.filteredNotesByTag.isEmpty() && viewModel.selectedTags.isNotEmpty()) {
                item {
                    Text(
                        "This tag has no notes.",
                        fontFamily = FontFamily.fontFamilyRegular,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            } else if (viewModel.filteredNotesByTag.isEmpty()) {
                items(
                    listOfNotesBooks ?: emptyList()
                ) { note ->
                    SingleItemNotebookList(
                        note = note,
                        navHostController,
                        title,
                        viewModel.showLockedNotes
                    )
                }
            } else {
                items(
                    viewModel.filteredNotesByTag ?: emptyList()
                ) { note ->
                    SingleItemNotebookList(
                        note = note,
                        navHostController,
                        title,
                        viewModel.showLockedNotes
                    )
                }
                if (!viewModel.showLockedNotes.value && viewModel.listOfLockedNotebooksNote.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),

                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center

                            ) {
                                Text(
                                    if (viewModel.listOfLockedNotebooksNote.size == 1) "1 locked note" else "${viewModel.listOfLockedNotebooksNote.size} locked notes",
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colors.onPrimary
                                )
                                TextButton(onClick = {
                                    showUnlockDialogBox.value = true
                                }) {
                                    Text(
                                        "Show",
                                        fontFamily = FontFamily.fontFamilyBold,
                                        fontStyle = FontStyle.Italic,
                                        fontSize = 15.sp,
                                        color = MaterialTheme.colors.onPrimary,
                                        textDecoration = TextDecoration.Underline
                                    )
                                }
                            }
                        }
                    }
                } else {
                    items(viewModel.listOfLockedNotebooksNote) { note ->
                        SingleItemUnlockNotebookList(
                            note,
                            navHostController,
                            title,
                            viewModel.showLockedNotes
                        )
                    }
                }
            }
        }
    }
}
///}




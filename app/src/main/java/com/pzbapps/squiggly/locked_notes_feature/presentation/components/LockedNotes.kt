package com.pzbapps.squiggly.locked_notes_feature.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.ChipDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
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
fun LockedNotes(
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    showEditTagsAlertBox: MutableState<Boolean>,
) {

    val fontFamilyExtraLight = Font(R.font.lufgaextralight).toFontFamily()

//    viewModel.getArchivedNotes()
//    val listOfNotes: SnapshotStateList<AddNote>? =
//        viewModel.getArchivedNotes.observeAsState().value

    var listOfPinnedNotes = SnapshotStateList<Note>()
    var listOfNotes = SnapshotStateList<Note>()
    viewModel.getAllNotes()
    viewModel.listOfNotesLiveData.observe(activity) {
        listOfPinnedNotes.clear()
        listOfNotes = it.toMutableStateList()
        for (i in listOfNotes) {
            if (i.notePinned && i.locked) {
                listOfPinnedNotes.add(i)
            }
        }
    }


    if (listOfNotes == null) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(8.dp)
                    .width(30.dp)
                    .height(30.dp),
                color = MaterialTheme.colors.onPrimary
            )
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
                        text = "Locked Notes",
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
                                    viewModel.filteredNotesByTag.addAll(listOfNotes.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                } else {
                                    viewModel.filteredNotesByTag.removeAll(listOfNotes.filter {
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
                                                        listOfNotes.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                } else {
                                                    viewModel.filteredNotesByTag.removeAll(
                                                        listOfNotes.filter {
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
                SingleItemLockedNoteList(note = note, navHostController = navHostController)
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
                    listOfNotes ?: emptyList()
                ) { note ->
                    SingleItemLockedNoteList(note = note, navHostController)
                }
            } else {
                items(
                    viewModel.filteredNotesByTag ?: emptyList()
                ) { note ->
                    SingleItemLockedNoteList(note = note, navHostController)
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
                        text = "Locked Notes",
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
                                    viewModel.filteredNotesByTag.addAll(listOfNotes.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                } else {
                                    viewModel.filteredNotesByTag.removeAll(listOfNotes.filter {
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
                                                        listOfNotes.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                } else {
                                                    viewModel.filteredNotesByTag.removeAll(
                                                        listOfNotes.filter {
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
                SingleItemLockedNoteList(note = note, navHostController = navHostController)
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
                    listOfNotes ?: emptyList()
                ) { note ->
                    SingleItemLockedNoteList(note = note, navHostController)
                }
            } else {
                items(
                    viewModel.filteredNotesByTag ?: emptyList()
                ) { note ->
                    SingleItemLockedNoteList(note = note, navHostController)
                }
            }
        }
    }
}
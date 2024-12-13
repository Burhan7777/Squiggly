package com.pzbapps.squiggly.main_screen.presentation.components

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
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
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Notes(
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    showGridOrLinearNotes: MutableState<Boolean>
) {

    val fontFamilyExtraLight = Font(R.font.lufgaextralight).toFontFamily()


//    viewModel.getNotesToShow()
//    val listOfNotes: SnapshotStateList<AddNote>? =
//        viewModel.getListOfNotesToShow.observeAsState().value


    var scope = rememberCoroutineScope()
    var listOfNotesFromDB = remember { mutableStateListOf<Note>() }
    var listOfPinnedNotes = SnapshotStateList<Note>()

    viewModel.getAllTags()
    var listOfTags = viewModel.tags
    val prefs: SharedPreferences = activity.getSharedPreferences(Constant.SORT_ORDER, MODE_PRIVATE)
    val name = prefs.getString(Constant.SORT_ORDER_KEY, Constant.SORT_ORDER_VALUE_1)
    if (name == Constant.SORT_ORDER_VALUE_1) {
        viewModel.getAllNotes()
        viewModel.listOfNotesLiveData.observe(activity) {
            listOfPinnedNotes.clear()
            listOfNotesFromDB = it.toMutableStateList()
            for (i in it) {
                if (i.notePinned) {
                    listOfPinnedNotes.add(i)
                }
            }
        }
    } else if (name == Constant.SORT_ORDER_VALUE_2) {
        viewModel.getAllNotesByDateModified()
        viewModel.listOfNotesByDataModified.observe(activity) {
            listOfPinnedNotes.clear()
            listOfNotesFromDB = it.toMutableStateList()
            for (i in it) {
                if (i.notePinned) {
                    listOfPinnedNotes.add(i)
                }
            }
        }
    }


//    viewModel.listOfNotesLiveData.observe(activity) {
//        for (note in listOfNotesFromDB) {
//            if (note.title.isEmpty() && note.content.isEmpty()) {
//                viewModel.deleteNoteById(note.id)
//            }
//        }
//    }


    if (showGridOrLinearNotes.value) {
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
                        text = "Notes",
                        fontFamily = fontFamilyExtraLight, fontSize = 65.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    Text(
                        "Tags",
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(viewModel.tags) { item ->
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

                                        }
                                    )
                                }
                            ) {
                                Text(
                                    item.name,
                                    color = MaterialTheme.colors.onSecondary,
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
                SingleItemNoteList(note = note, navHostController = navHostController, scope)
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = "ALL NOTES",
                    fontFamily = FontFamily.fontFamilyBold, fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
                if (listOfNotesFromDB.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                text = "WOW... so empty !!!",
                                fontSize = 25.sp,
                                fontFamily = FontFamily.fontFamilyExtraLight,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Create your first note by clicking the add button in the bottom right",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.fontFamilyRegular,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            items(
                items = listOfNotesFromDB ?: emptyList()
            ) { note ->
                SingleItemNoteList(note = note, navHostController, scope)
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
                        text = "Notes",
                        fontFamily = fontFamilyExtraLight, fontSize = 65.sp,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                    Text(
                        "Tags",
                        color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(start = 10.dp)
                    )
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
                                    viewModel.filteredNotesByTag.addAll(listOfNotesFromDB.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                } else {
                                    viewModel.filteredNotesByTag.removeAll(listOfNotesFromDB.filter {
                                        it.tags.contains(
                                            item.name
                                        )
                                    })
                                }

                                if (viewModel.selectedTags.isEmpty()) {
                                    viewModel.filteredNotesByTag.clear()
                                }

                                println(viewModel.selectedTags)
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
                                                        listOfNotesFromDB.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                } else {
                                                    viewModel.filteredNotesByTag.removeAll(
                                                        listOfNotesFromDB.filter {
                                                            it.tags.contains(
                                                                item.name
                                                            )
                                                        })
                                                }

                                                if (viewModel.selectedTags.isEmpty()) {
                                                    viewModel.filteredNotesByTag.clear()
                                                }

                                                println(viewModel.selectedTags)
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
//                    if (listOfNotesFromDB.isEmpty()) {
//                        val composiion by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.empty))
//                        var isPlaying = remember { mutableStateOf(true) }
//                        val progress by animateLottieCompositionAsState(composition = composiion, isPlaying = true)
//                        LottieAnimation(modifier = Modifier.size(100.dp),composition = composiion, progress = { progress })
//                    }
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
                SingleItemNoteList(note = note, navHostController = navHostController, scope)
            }
            item {
                Text(
                    text = "ALL NOTES",
                    fontFamily = FontFamily.fontFamilyBold, fontSize = 20.sp,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                )
                if (listOfNotesFromDB.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 50.dp)
                    ) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Text(
                                text = "WOW... so empty !!!",
                                fontSize = 25.sp,
                                fontFamily = FontFamily.fontFamilyExtraLight,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Create your first note by clicking the add button in the bottom right",
                                fontSize = 15.sp,
                                fontFamily = FontFamily.fontFamilyRegular,
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
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
                    listOfNotesFromDB ?: emptyList()
                ) { note ->
                    SingleItemNoteList(note = note, navHostController, scope)
                }
            } else {
                items(
                    viewModel.filteredNotesByTag ?: emptyList()
                ) { note ->
                    SingleItemNoteList(note = note, navHostController, scope)
                }
            }


        }
    }
}
//}
///}




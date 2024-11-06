package com.pzbapps.squiggly.main_screen.presentation.components

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note


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
            items(listOfPinnedNotes,  key = { it.id }) { note ->
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
                items = listOfNotesFromDB ?: emptyList(),  key = { it.id }
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
            items(listOfPinnedNotes, key = { it.id }) { note ->
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
            items(
                listOfNotesFromDB ?: emptyList(),  key = { it.id }
            ) { note ->
                SingleItemNoteList(note = note, navHostController, scope)
            }


        }
    }
}
//}
///}




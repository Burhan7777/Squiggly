package com.pzbapps.squiggly.search_main_screen_feature.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note

@Composable
fun SearchedNotes(
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navHostController: NavHostController,
    screen: String,
    queryText: String
) {
    //val searchResult = viewModel.getSearchResult.observeAsState().value
    //val searchArchiveResult = viewModel.getArchiveSearchResult.observeAsState().value
    val filteredNotes = ArrayList<Note>()
    //viewModel.getAllNotes()
    viewModel.listOfNotesLiveData.observe(activity) { notes ->
        for (i in notes) {
            if (i.title.lowercase().contains(queryText) || i.content.lowercase()
                    .contains(queryText)
            ) {
                filteredNotes.add(i)
            }
        }
        for (i in notes.indices) {
            for (j in notes[i].listOfCheckedNotes) {
                if (j.lowercase().contains(queryText)) {
                    filteredNotes.add(notes[i])
                }
            }
        }
        for (i in notes.indices) {
            for (j in notes[i].listOfBulletPointNotes) {
                if (j.lowercase().contains(queryText)) {
                    filteredNotes.add(notes[i])
                }
            }
        }
    }



    LazyColumn() {
        item {
            Text(
                text = "Search results",
                fontFamily = FontFamily.fontFamilyBold,
                fontSize = 25.sp,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }

        items(
            filteredNotes ?: emptyList()
        ) { note ->
            SingleItemSearchNoteList(note = note, navHostController, queryText)
        }
//        items(
//            searchArchiveResult ?: emptyList()
//        ) { note ->
//            //  SingleItemNoteList(note = note, navHostController)
//        }
    }
}


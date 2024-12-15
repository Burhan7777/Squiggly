package com.pzbapps.squiggly.edit_note_feature.presentation.screens


import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.edit_note_feature.presentation.components.MainStructureEditNote


@Composable
fun EditNoteScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    id: Int,
    screen: String,
    notesId: MutableIntState,
    query: String
) {

    // viewModel.getNoteById(id)
    // val note = viewModel.getNote


    // Log.i("note", note.toString())
    var title = remember {
        mutableStateOf("")
    }

    var content = remember {
        mutableStateOf("")
    }

    viewModel.getAllTags()

    notesId.intValue = -1

//      Log.i("mutable", note.value.title)
//  Log.i("mutable", note.value.content)

//Log.i("mutableString", title.value)
//  Log.i("mutableString", content.value)

//   Log.i("getnote", note.toString())

    MainStructureEditNote(
        navController = navHostController,
        viewModel = viewModel,
        id = id,
        activity,
        screen,
        notesId,
        query
    )
}



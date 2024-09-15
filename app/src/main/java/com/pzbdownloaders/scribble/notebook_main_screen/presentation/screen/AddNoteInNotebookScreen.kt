package com.pzbdownloaders.scribble.notebook_main_screen.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbdownloaders.scribble.add_note_feature.presentation.components.MainStructureAddNote
import com.pzbdownloaders.scribble.common.presentation.MainActivity
import com.pzbdownloaders.scribble.common.presentation.MainActivityViewModel
import com.pzbdownloaders.scribble.notebook_main_screen.presentation.components.MainStructureAddNoteInNotebook

@Composable
fun AddNoteInNotebookScreen(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    activity: MainActivity,
    notebookName:String
) {


    var title = remember {
        mutableStateOf("")
    }

    var content = remember {
        mutableStateOf("")
    }

    var notebookState = remember {
        mutableStateOf("")
    }

    val richStateText = mutableStateOf(rememberRichTextState())

    viewModel.getAllNotebooks()

    //  var note = Note(0, title.value, content.value, getTimeInMilliSeconds(), 123456)

    MainStructureAddNoteInNotebook(
        navHostController,
        title = title,
        content = content,
        viewModel= viewModel,
        //note,
        notebookState = notebookState,
        activity = activity,
        richTextState = richStateText,
        notebookName = notebookName
//        notebook,
//        notebookFromDB
    )

}
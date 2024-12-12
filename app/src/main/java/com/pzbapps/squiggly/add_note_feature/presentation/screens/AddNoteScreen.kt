package com.pzbapps.squiggly.add_note_feature.presentation.screens

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbapps.squiggly.add_note_feature.presentation.components.MainStructureAddNote
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import java.util.Calendar

@Composable
fun AddNoteScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity
) {

    var title = rememberSaveable {
        mutableStateOf("")
    }

    var content = remember {
        mutableStateOf("")
    }

    var notebookState = rememberSaveable {
        mutableStateOf("")
    }

    val richStateText = mutableStateOf(rememberRichTextState())

    viewModel.getAllNotebooks()
    viewModel.getAllTags()

    //  var note = Note(0, title.value, content.value, getTimeInMilliSeconds(), 123456)


    MainStructureAddNote(
        navHostController,
        title = title,
        content = content,
        viewModel = viewModel,
        //note,
        notebookState = notebookState,
        activity = activity,
        richTextState = richStateText
//        notebook,
//        notebookFromDB
    )


}

fun getTimeInMilliSeconds(): Long {
    var calendar = Calendar.getInstance()
    return calendar.timeInMillis
}
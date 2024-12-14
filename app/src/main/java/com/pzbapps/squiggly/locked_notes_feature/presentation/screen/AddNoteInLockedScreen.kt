package com.pzbapps.squiggly.locked_notes_feature.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.locked_notes_feature.presentation.components.MainStructureAddNoteLockedScreen

@Composable
fun AddNoteInLockedScreen(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    activity: MainActivity
) {


    var title = rememberSaveable {
        mutableStateOf("")
    }

    var content = rememberSaveable {
        mutableStateOf("")
    }

    var notebookState = rememberSaveable {
        mutableStateOf("")
    }

    val richStateText = mutableStateOf(rememberRichTextState())

    viewModel.getAllNotebooks()
    viewModel.getAllTags()

    //  var note = Note(0, title.value, content.value, getTimeInMilliSeconds(), 123456)

    MainStructureAddNoteLockedScreen(
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
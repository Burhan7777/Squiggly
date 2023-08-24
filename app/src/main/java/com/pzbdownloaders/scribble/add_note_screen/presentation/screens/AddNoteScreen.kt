package com.pzbdownloaders.scribble.add_note_screen.presentation.screens

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.pzbdownloaders.scribble.add_note_screen.domain.AddNote
import com.pzbdownloaders.scribble.add_note_screen.presentation.components.MainStructureAddNote
import com.pzbdownloaders.scribble.add_note_screen.presentation.components.NoteContent
import com.pzbdownloaders.scribble.common.presentation.Constant
import com.pzbdownloaders.scribble.common.presentation.MainActivityViewModel
import com.pzbdownloaders.scribble.main_screen.domain.model.Note
import java.util.Calendar

@Composable
fun AddNoteScreen(navHostController: NavHostController, viewModel: MainActivityViewModel) {

    var title = remember {
        mutableStateOf("")
    }

    var content = remember {
        mutableStateOf("")
    }

    //  var note = Note(0, title.value, content.value, getTimeInMilliSeconds(), 123456)
    var note = AddNote(
        "",
        title.value,
        content.value,
        FirebaseAuth.getInstance().currentUser!!.uid,
        System.currentTimeMillis(),
        123,
        false,
        "random"
    )
    MainStructureAddNote(navHostController, title, content, viewModel, note)


}

fun getTimeInMilliSeconds(): Long {
    var calendar = Calendar.getInstance()
    return calendar.timeInMillis
}
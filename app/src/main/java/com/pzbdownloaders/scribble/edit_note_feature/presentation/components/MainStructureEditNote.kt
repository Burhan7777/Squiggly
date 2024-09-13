package com.pzbdownloaders.scribble.edit_note_feature.presentation.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbdownloaders.scribble.add_note_feature.domain.model.AddNote
import com.pzbdownloaders.scribble.add_note_feature.presentation.components.BottomTextFormattingBar
import com.pzbdownloaders.scribble.common.domain.utils.Constant
import com.pzbdownloaders.scribble.common.domain.utils.GetResult
import com.pzbdownloaders.scribble.common.presentation.*
import com.pzbdownloaders.scribble.common.presentation.components.AlertDialogBoxTrialEnded
import com.pzbdownloaders.scribble.edit_note_feature.domain.usecase.checkIfUserHasCreatedPassword
import com.pzbdownloaders.scribble.main_screen.domain.model.Note
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureEditNote(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    id: Int,
    activity: MainActivity,
    screen: String
) {
    var context = LocalContext.current

    var dialogOpen = remember {
        mutableStateOf(false)
    }

    var richStateText = mutableStateOf(rememberRichTextState())

    var isBoldActivated = remember { mutableStateOf(false) }
    var isUnderlineActivated = remember { mutableStateOf(false) }
    var isItalicActivated = remember { mutableStateOf(false) }
    var isOrderedListActivated = remember { mutableStateOf(false) }
    var isUnOrderedListActivated = remember { mutableStateOf(false) }
    var showFontSize = remember { mutableStateOf(false) }
    var fontSize = remember { mutableStateOf("20") }

    if (richStateText.value.annotatedString.text == "") fontSize.value = "20"

//    var note: AddNote? by remember {
//        mutableStateOf(AddNote())
//    }

    //viewModel.getNoteToEdit(id)
    // note = viewModel.getNoteDetailsToEdit.observeAsState().value

    viewModel.getNoteById(id)
    var note = viewModel.getNoteById

    var title by remember {
        mutableStateOf("")
    }


    var content by remember {
        mutableStateOf("")
    }

    var mutableListOfCheckboxTexts = remember {
        mutableStateListOf<MutableState<String>>()
    }

    var converted = remember { ArrayList<String>() }
// This is the list of checkbox notes which we saved in checkboxes
    var mutableListOfCheckBoxes = remember { ArrayList<Boolean>() }// This is the llst of checkboxes

    var convertedBulletPoints = remember { ArrayList<String>() }

    var notebook by remember {
        mutableStateOf("")
    }

    var isExpanded = remember {
        mutableStateOf(false)
    }

    var selectedNotebook = remember {
        mutableStateOf("")
    }

    var passwordNotSetUpDialogBox = remember {
        mutableStateOf(false)
    }

    var mutableListOfBulletPoints = remember { mutableStateListOf<MutableState<String>>() }

    var enterPasswordToLockDialogBox = remember { mutableStateOf(false) }

    var enterPasswordToUnLockDialogBox = remember { mutableStateOf(false) }

    var showTrialEndedDialogBox = remember { mutableStateOf(false) }



    LaunchedEffect(key1 = true) {
       // WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    }

    LaunchedEffect(key1 = Unit) {
        for (i in note.value.listOfCheckedNotes) {
            mutableListOfCheckboxTexts.add(mutableStateOf(i))
        }

    }

    LaunchedEffect(key1 = Unit) {
        for (i in note.value.listOfBulletPointNotes) {
            mutableListOfBulletPoints.add(mutableStateOf(i))
        }
    }

    if (note.value != null) {
        mutableListOfCheckBoxes = note.value.listOfCheckedBoxes
    }

    LaunchedEffect(key1 = true) {
        title = note.value.title ?: ""
        content = note.value.content ?: "Failed to get the contents.Please try again"
        notebook = note.value.notebook

    }


    if (showTrialEndedDialogBox.value) {
        AlertDialogBoxTrialEnded {
            showTrialEndedDialogBox.value = false
        }
    }


    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Undo")
                    }
                },
                title = { Text(text = "") },
                actions = {
                    IconButton(onClick = {
                        //    var updatedNote = Note(id, title, content, getTimeInMilliSeconds(), 123)
                        //   Log.i("title", title)
                        //  viewModel.updateNote(updatedNote)
//                        val map = HashMap<String, Any>()
//                        map["title"] = title
//                        map["content"] = content
//                        map["timeStamp"] = System.currentTimeMillis()
//                        if (selectedNotebook.value.isNotEmpty()) map["label"] =
//                            selectedNotebook.value
//                      //  viewModel.updateNote(note!!.noteId, map)
//                        viewModel.getResultFromUpdateNote.observe(activity) {
//                            when (it) {
//                                is GetResult.Success -> {
//                                    Toast.makeText(
//                                        context,
//                                        "Updated Successfully",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                    navController.popBackStack()
//                                }
//                                is GetResult.Failure -> {
//                                    Toast.makeText(
//                                        context,
//                                        "Update failed. Try again",
//                                        Toast.LENGTH_SHORT
//                                    )
//                                        .show()
//                                }
//                            }
//                        }

                        convertMutableStateIntoString(mutableListOfCheckboxTexts, converted)
                        convertMutableStateIntoString(
                            mutableListOfBulletPoints,
                            convertedBulletPoints
                        )
                        viewModel.getNoteById(id)
                        var noteFromDb = viewModel.getNoteById
                        var archived = noteFromDb.value.archive
                        var lockedOrNote = noteFromDb.value.locked
                        var note = Note(
                            id,
                            title,
                            richStateText.value.toHtml(),
                            archived,
                            locked = lockedOrNote,
                            listOfCheckedNotes = converted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                            listOfBulletPointNotes = convertedBulletPoints,
                            timeStamp = System.currentTimeMillis()
                        )
                        viewModel.updateNote(note)
                        navController.popBackStack()
                        Toast.makeText(context, "Note has been updated", Toast.LENGTH_SHORT)
                            .show()


                    }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Undo")
                    }
                    IconButton(onClick = {
                        if (screen == Constant.HOME || screen == Constant.ARCHIVE) {
                            val result = checkIfUserHasCreatedPassword()
                            result.observe(activity) {
                                if (it == false) {
                                    passwordNotSetUpDialogBox.value = true
                                } else {
                                    convertMutableStateIntoString(
                                        mutableListOfCheckboxTexts,
                                        converted
                                    )
                                    convertMutableStateIntoString(
                                        mutableListOfBulletPoints,
                                        convertedBulletPoints
                                    )
                                    enterPasswordToLockDialogBox.value = true

                                }
                            }


                        } else if (screen == Constant.LOCKED_NOTE) {
                            convertMutableStateIntoString(
                                mutableListOfCheckboxTexts,
                                converted
                            )
                            convertMutableStateIntoString(
                                mutableListOfBulletPoints,
                                convertedBulletPoints
                            )
                            enterPasswordToUnLockDialogBox.value = true
                        }
                    }) {
                        Icon(
                            imageVector = if (screen ==
                                Constant.HOME
                            ) Icons.Filled.Lock else if (screen == Constant.ARCHIVE) Icons.Filled.Lock else Icons.Filled.LockOpen,
                            contentDescription = "Lock Note"
                        )
                    }
                    IconButton(onClick = {
                        convertMutableStateIntoString(mutableListOfCheckboxTexts, converted)
                        convertMutableStateIntoString(
                            mutableListOfBulletPoints,
                            convertedBulletPoints
                        )
                        if (screen == Constant.HOME || screen == Constant.LOCKED_NOTE) {
                            var note = (Note(
                                id,
                                title,
                                content,
                                listOfCheckedNotes = converted,
                                listOfCheckedBoxes = mutableListOfCheckBoxes,
                                listOfBulletPointNotes = convertedBulletPoints,
                                archive = true,
                                timeStamp = System.currentTimeMillis()
                            ))
                            viewModel.updateNote(note)
                            Toast.makeText(
                                activity,
                                "Note has been archived",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.popBackStack()
//                            var hashmap = HashMap<String, Any>()
//                            hashmap["archived"] = true
                            //   viewModel.archiveNotes(id, hashmap)
//                            viewModel.getResultFromArchivedNotes.observe(activity) {
//                                when (it) {
//                                    is GetResult.Success -> {
//                                        Toast.makeText(
//                                            context,
//                                            "Note has be archived",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        navController.popBackStack()
//                                    }
//
//                                    is GetResult.Failure -> {
//                                        Toast.makeText(
//                                            context,
//                                            "Note failed to be archived",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            }
                        } else if (screen == Constant.ARCHIVE) {
                            var note = (Note(
                                id,
                                title,
                                content,
                                archive = false,
                                timeStamp = System.currentTimeMillis(),
                                listOfCheckedNotes = converted,
                                listOfCheckedBoxes = mutableListOfCheckBoxes,
                                listOfBulletPointNotes = convertedBulletPoints,
                            ))
                            viewModel.updateNote(note)
                            Toast.makeText(
                                activity,
                                "Note has been unarchived",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            navController.popBackStack()
//                            val hashmap = HashMap<String, Any>()
//                            hashmap["archived"] = false
                            // viewModel.unArchiveNotes(id, hashmap)
//                            viewModel.getResultFromUnArchiveNotes.observe(activity) {
//                                when (it) {
//                                    is GetResult.Success -> {
//                                        Toast.makeText(
//                                            context,
//                                            "Note has been unarchived",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                        navController.popBackStack()
//                                    }
//
//                                    is GetResult.Failure -> {
//                                        Toast.makeText(
//                                            context,
//                                            "Note failed to be unarchived",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            }
                        }

                    }) {
                        Icon(
                            imageVector = if (screen == Constant.HOME) Icons.Filled.Archive else if (screen == Constant.LOCKED_NOTE) Icons.Filled.Archive else Icons.Filled.Unarchive,
                            contentDescription = "Archive"
                        )
                    }
                    IconButton(onClick = {
                        // viewModel.deleteNoteById(id)
                        dialogOpen.value = true

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                }
            )


        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                NoteContent(
                    selectedNotebook,
                    isExpanded,
                    viewModel,
                    title,
                    content,
                    notebook,
                    mutableListOfCheckboxTexts,
                    mutableListOfCheckBoxes,
                    mutableListOfBulletPoints,
                    activity,
                    richStateText.value,
                    { title = it },
                    { content = it }
                )
            }
            Box(
                modifier =
                Modifier
                    .padding(WindowInsets.ime.asPaddingValues())
                    .padding(15.dp)
                    .background(MaterialTheme.colors.primaryVariant)
                    .fillMaxWidth()
                    .height(if (showFontSize.value) 100.dp else 50.dp)
                    .align(
                        Alignment.BottomCenter
                    )
            ) {
                BottomTextFormattingBar(
                    showFontSize = showFontSize,
                    fontSize = fontSize,
                    richTextState = richStateText,
                    isBoldActivated = isBoldActivated,
                    isUnderlineActivated = isUnderlineActivated,
                    isItalicActivated = isItalicActivated,
                    isOrderedListActivated = isOrderedListActivated,
                    isUnOrderedListActivated = isUnOrderedListActivated
                )
            }
        }
    }
    if (dialogOpen.value) {
        viewModel.getNoteById(id)
        var noteFromDb = viewModel.getNoteById
        var archived = noteFromDb.value.archive
        var lockedOrNote = noteFromDb.value.locked
        var note = Note(
            id,
            title,
            richStateText.value.toHtml(),
            archived,
            locked = lockedOrNote,
            listOfCheckedNotes = converted,
            listOfCheckedBoxes = mutableListOfCheckBoxes,
            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
            listOfBulletPointNotes = convertedBulletPoints,
            timeStamp = System.currentTimeMillis()
        )
        AlertDialogBoxDelete(
            viewModel = viewModel,
            id = id,
            activity = activity,
            navHostController = navController,
            note = note
        ) {
            dialogOpen.value = false
        }
    }
    if (passwordNotSetUpDialogBox.value) {
        AlertDialogBoxPassword(
            viewModel = viewModel,
            activity = activity,
            navHostController = navController
        ) {
            passwordNotSetUpDialogBox.value = false
        }
    }
    if (enterPasswordToLockDialogBox.value) {
        AlertDialogBoxEnterPassword(
            viewModel = viewModel,
            id = id,
            activity = activity,
            navHostController = navController,
            title = title,
            convertedMutableList = converted,
            listOfCheckboxes = mutableListOfCheckBoxes,
            listOfBulletPoints = convertedBulletPoints,
            content = content
        ) {
            enterPasswordToLockDialogBox.value = false
        }
    }
    if (enterPasswordToUnLockDialogBox.value) {
        AlertDialogBoxEnterPasswordToUnlock(
            viewModel = viewModel,
            id = id,
            activity = activity,
            navHostController = navController,
            title = title,
            listOfCheckedNotes = converted,
            listOfCheckBoxes = mutableListOfCheckBoxes,
            listOfBulletPoints = convertedBulletPoints,
            content = content
        ) {

        }
    }
}

fun convertMutableStateIntoString(
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>
) {
    for (i in mutableList) {
        mutableListConverted.add(i.value)
    }
}





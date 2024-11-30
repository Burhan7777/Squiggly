package com.pzbapps.squiggly.notebook_main_screen.presentation.components.addNoteInNotebookComponents

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.AddNoteBottomSheet
import com.pzbapps.squiggly.add_note_feature.presentation.components.DiscardNoteAlertBox
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.components.AlertDialogBoxTrialEnded
import com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.TextColorBottomSheet
import com.pzbapps.squiggly.main_screen.domain.model.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Stack

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun MainStructureAddNoteInNotebook(
    navController: NavHostController,
    title: MutableState<String>,
    content: MutableState<String>,
    viewModel: MainActivityViewModel,
    //note: Note,
    notebookState: MutableState<String>,
    activity: MainActivity,
    richTextState: MutableState<RichTextState>,
    notebookName: String
//    notebook: MutableState<ArrayList<String>>,
//    notebookFromDB: MutableState<ArrayList<NoteBook>>
) {

    var showCircularProgress = remember { mutableStateOf(true) }
    var context = LocalContext.current
    var showTrialEndedDialogBox = remember { mutableStateOf(false) }
    var boldText = remember { mutableStateOf(false) }
    var underlineText = remember { mutableStateOf(false) }
    var italicText = remember { mutableStateOf(false) }
    var generatedNoteId = rememberSaveable { mutableStateOf<Long>(0) }

    var annotatedString = remember { mutableStateOf(AnnotatedString("")) }

    var showDiscardNoteAlertBox = remember { mutableStateOf(false) }

    var textFieldValue =
        remember { mutableStateOf<TextFieldValue>(TextFieldValue(annotatedString.value)) }

    val keyboardController = LocalSoftwareKeyboardController.current

    var hideFormattingTextBarWhenTitleIsInFocus = remember { mutableStateOf(true) }


    var boldSelection = remember { mutableStateOf(false) }

    var isBoldActivated = remember { mutableStateOf(false) }
    var isUnderlineActivated = remember { mutableStateOf(false) }
    var isItalicActivated = remember { mutableStateOf(false) }
    var isOrderedListActivated = remember { mutableStateOf(false) }
    var isUnOrderedListActivated = remember { mutableStateOf(false) }
    var showFontSize = remember { mutableStateOf(false) }
    var fontSize = remember { mutableStateOf("20") }

    val showBottomSheet = remember { mutableStateOf(false) }
    val showTextColorBottomSheet = remember { mutableStateOf(false) }
    val backgroundColor1 = MaterialTheme.colors.primary
    val backgroundColor = remember { mutableStateOf(backgroundColor1) }

    if (richTextState.value.annotatedString.text == "") fontSize.value = "20"

    val undoStack = remember { Stack<String>() }
    val redoStack = remember { Stack<String>() }

    // Track the current content as a snapshot
    var currentContent = remember { mutableStateOf("") }

    LaunchedEffect(richTextState.value) {
        snapshotFlow { richTextState.value.annotatedString }
            .debounce(200)
            .map { richTextState.value.toHtml() }// To avoid frequent updates, only take new state every 300ms
            .filter { it != currentContent.value } // Only proceed if content has changed
            .collect { newContent ->
                // Store the current content before updating
                if (currentContent.value.isNotEmpty()) {
                    undoStack.push(currentContent.value)
                    redoStack.clear() // Clear redo stack on new change
                }
                // Update the tracked content
                currentContent.value = newContent
            }
    }


//    DisposableEffect(Unit) {
//        var note = Note(
//            0,
//            title = title.value,
//            content = richTextState.value.toHtml(),
//            timeModified = System.currentTimeMillis(),
//            notebook = notebookName,
//            timeStamp = System.currentTimeMillis()
////                listOfBulletPointNotes = convertedBulletPoints,
////                listOfCheckedNotes = converted,
////                listOfCheckedBoxes = mutableListOfCheckBoxes
//
//        )
//        viewModel.insertNote(note)
//        viewModel.generatedNoteId.observe(activity) {
//            generatedNoteId.value = it
//        }
//        val timer = Timer()
//        // Schedule a task to run every 10 seconds
//        timer.schedule(delay = 3000L, period = 1000L) {
//            viewModel.getNoteById(generatedNoteId.value.toInt())
//            var noteFromDb = viewModel.getNoteById
//            var note1 = noteFromDb.value.copy(
//                title = title.value,
//                content = richTextState.value.toHtml(),
//                timeModified = System.currentTimeMillis(),
//                notebook = notebookName,
////                listOfBulletPointNotes = convertedBulletPoints,
////                listOfCheckedNotes = converted,
////                listOfCheckedBoxes = mutableListOfCheckBoxes
//
//            )
//            viewModel.updateNote(note1)
//        }
//
//        // Clean up the timer when the composable leaves the composition
//        onDispose {
//            timer.cancel() // Stop the timer
//        }
//    }

    // WindowCompat.setDecorFitsSystemWindows(activity.window, false)

    var coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        if (generatedNoteId.value.toInt() == 0) {
            val note = Note(
                title = title.value,
                content = richTextState.value.toHtml(),
                timeModified = System.currentTimeMillis(),
                notebook = notebookName,
                timeStamp = System.currentTimeMillis(),
                color = backgroundColor.value.toArgb()
            )
            viewModel.insertNote(note)
        }
        viewModel.generatedNoteId.observe(activity) {
            generatedNoteId.value = it
        }

        val job = coroutineScope.launch {
            // Delay the autosave for 3 seconds, then run it every 10 seconds
            delay(3000L)
            while (isActive) {
                // Get the note by ID and update it
                // viewModel.getNoteById(generatedNoteId.value.toInt())
                // val noteFromDb = viewModel.getNoteById.value
                val updatedNote = Note(
                    id = generatedNoteId.value.toInt(),
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = notebookName,
                    timeStamp = System.currentTimeMillis(),
                    color = backgroundColor.value.toArgb()
                )
                viewModel.updateNote(updatedNote)
                delay(5000L)
                // Save every 10 seconds
            }
        }

        onDispose {
            job.cancel()  // Cancel the coroutine when the component is disposed
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    // Observe the lifecycle to detect when the app goes into the background
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                // Trigger autosave when app goes to background (onStop)
                val updatedNote = Note(
                    id = generatedNoteId.value.toInt(),
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = notebookName,
                    timeStamp = System.currentTimeMillis(),
                    color = backgroundColor.value.toArgb()
                )
                viewModel.updateNote(updatedNote)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cleanup the observer when the Composable is disposed
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler {
        if (title.value.isNotEmpty() || richTextState.value.annotatedString.text.isNotEmpty()) {
            val updatedNote = Note(
                id = generatedNoteId.value.toInt(),
                title = title.value,
                content = richTextState.value.toHtml(),
                timeModified = System.currentTimeMillis(),
                notebook = notebookName,
                timeStamp = System.currentTimeMillis(),
                color = backgroundColor.value.toArgb()
            )
            viewModel.updateNote(updatedNote)
            navController.navigateUp()
        } else {
            viewModel.deleteNoteById(generatedNoteId.value.toInt())
            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor.value
                ),
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (title.value.isNotEmpty() || richTextState.value.annotatedString.text.isNotEmpty()) {
                            var note2 = Note(
                                id = generatedNoteId.value.toInt(),
                                title = title.value,
                                content = richTextState.value.toHtml(),
                                timeModified = System.currentTimeMillis(),
                                notebook = notebookName,
                                timeStamp = System.currentTimeMillis(),
                                color = backgroundColor.value.toArgb()
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                            )
                            viewModel.updateNote(note2)
                            Toast.makeText(context, "Note has been added", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "go back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showDiscardNoteAlertBox.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Discard Note",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        if (title.value.isNotEmpty() || richTextState.value.annotatedString.text.isNotEmpty()) {
                            var note2 = Note(
                                id = generatedNoteId.value.toInt(),
                                title = title.value,
                                content = richTextState.value.toHtml(),
                                timeModified = System.currentTimeMillis(),
                                notebook = notebookName,
                                timeStamp = System.currentTimeMillis(),
                                color = backgroundColor.value.toArgb()
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                            )
                            viewModel.updateNote(note2)
                            Toast.makeText(context, "Note has been added", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Save")
                    }
                    if (showTrialEndedDialogBox.value) {
                        AlertDialogBoxTrialEnded {
                            showTrialEndedDialogBox.value = false
                        }
                    }
                }
            )
        },
//        bottomBar = {
//            BottomAppBar(
//                modifier = if (WindowInsets.isImeVisible) Modifier.padding(keyboardHeight) else Modifier.padding(
//                    10.dp).imePadding()
//            ) {
//
//            }
//        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor.value)
        ) {
            Column(modifier = Modifier.padding(it)) {
                if (showDiscardNoteAlertBox.value) {
                    DiscardNoteAlertBox(
                        viewModel = viewModel,
                        navHostController = navController,
                        activity = activity,
                        id = generatedNoteId.value.toInt()
                    ) {
                        showDiscardNoteAlertBox.value = false
                    }
                }
                if (showBottomSheet.value) {
                    AddNoteBottomSheet(showBottomSheet, backgroundColor)
                }
                if(showTextColorBottomSheet.value){
                    TextColorBottomSheet(showTextColorBottomSheet,richTextState)
                }
                NoteContentNoteInNotebook(
                    title,
                    content,
                    viewModel,
                    notebookState,
                    showCircularProgress,
                    textFieldValue,
                    boldText,
                    richTextState.value,
                    hideFormattingTextBarWhenTitleIsInFocus,
                    backgroundColor
//                notebook,
//                notebookFromDB)
                )
            }
            if (!hideFormattingTextBarWhenTitleIsInFocus.value) {
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
                    BottomTextFormattingBarNoteInNotebook(
                        showFontSize = showFontSize,
                        fontSize = fontSize,
                        richTextState = richTextState,
                        isBoldActivated = isBoldActivated,
                        isUnderlineActivated = isUnderlineActivated,
                        isItalicActivated = isItalicActivated,
                        isOrderedListActivated = isOrderedListActivated,
                        isUnOrderedListActivated = isUnOrderedListActivated,
                        undoStack = undoStack,
                        redoStack = redoStack,
                        currentContent = currentContent,
                        showBottomSheet = showBottomSheet,
                        showTextColorBottomSheet = showTextColorBottomSheet
                    )
                }
            }
        }
    }

}


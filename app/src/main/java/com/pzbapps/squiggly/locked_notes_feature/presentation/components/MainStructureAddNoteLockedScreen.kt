package com.pzbapps.squiggly.locked_notes_feature.presentation.components

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
import androidx.compose.runtime.mutableStateListOf
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
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.fontsbottomsheet.FontBottomSheet
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
fun MainStructureAddNoteLockedScreen(
    navController: NavHostController,
    title: MutableState<String>,
    content: MutableState<String>,
    viewModel: MainActivityViewModel,
    //note: Note,
    notebookState: MutableState<String>,
    activity: MainActivity,
    richTextState: MutableState<RichTextState>
//    notebook: MutableState<ArrayList<String>>,
//    notebookFromDB: MutableState<ArrayList<NoteBook>>
) {

    var showCircularProgress = remember { mutableStateOf(true) }
    var context = LocalContext.current
    var showTrialEndedDialogBox = remember { mutableStateOf(false) }
    var boldText = remember { mutableStateOf(false) }
    var underlineText = remember { mutableStateOf(false) }
    var italicText = remember { mutableStateOf(false) }

    var annotatedString = remember { mutableStateOf(AnnotatedString("")) }

    var showDiscardNoteAlertBox = remember { mutableStateOf(false) }

    var textFieldValue =
        remember { mutableStateOf<TextFieldValue>(TextFieldValue(annotatedString.value)) }

    val keyboardController = LocalSoftwareKeyboardController.current

    val listOfSelectedTags =
        remember { mutableStateListOf<String>() } // THESE ARE THE TAGS SELECTED BY THE USER
    // IN ADD NOTE FEATURE AND WILL BE ADDED TO THE "LIST_OF_TAGS" IN THE NOTE TABLE


    var boldSelection = remember { mutableStateOf(false) }

    var isBoldActivated = remember { mutableStateOf(false) }
    var isUnderlineActivated = remember { mutableStateOf(false) }
    var isItalicActivated = remember { mutableStateOf(false) }
    var isOrderedListActivated = remember { mutableStateOf(false) }
    var isUnOrderedListActivated = remember { mutableStateOf(false) }
    var showFontSize = remember { mutableStateOf(false) }
    var fontSize = remember { mutableStateOf("20") }
    var generatedNoteId = rememberSaveable { mutableStateOf<Long>(0) }

    if (richTextState.value.annotatedString.text == "") fontSize.value = "20"

    var hideFormattingTextBarWhileTitleIsInFocus = remember { mutableStateOf(true) }

    val showBottomSheet = remember { mutableStateOf(false) }
    val showTextColorBottomSheet = remember { mutableStateOf(false) }

    val backgroundColor1 = MaterialTheme.colors.primary
    val backgroundColor = remember { mutableStateOf(backgroundColor1) }

    val undoStack = remember { Stack<String>() }
    val redoStack = remember { Stack<String>() }

    // Track the current content as a snapshot
    var currentContent = remember { mutableStateOf("") }

    var showFontBottomSheet = remember { mutableStateOf(false) }

    var fontFamily = remember { mutableStateOf(FontFamily.fontFamilyRegular) }

    var fontFamilyString = remember { mutableStateOf("") }

    when (fontFamily.value) {
        FontFamily.fontFamilyRegular -> fontFamilyString.value = FontFamily.lufgaRegular
        FontFamily.fontFamilyBold -> fontFamilyString.value = FontFamily.lufgaBold
        FontFamily.fontFamilyExtraLight -> fontFamilyString.value = FontFamily.lufgaextraLight
        FontFamily.pacificoRegular -> fontFamilyString.value = FontFamily.pacificoString
        FontFamily.parkinsons -> fontFamilyString.value = FontFamily.parkinsonsString
        FontFamily.jaro -> fontFamilyString.value = FontFamily.jaroString
        FontFamily.dancingScript -> fontFamilyString.value = FontFamily.dancingScriptString
        FontFamily.doto -> fontFamilyString.value = FontFamily.dotoString
        FontFamily.edu -> fontFamilyString.value = FontFamily.eduString
        FontFamily.lobster -> fontFamilyString.value = FontFamily.lobsterString
        FontFamily.playfair -> fontFamilyString.value = FontFamily.playfairString
        FontFamily.poppins -> fontFamilyString.value = FontFamily.poppinsString
        else -> FontFamily.fontFamilyRegular
    }

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
//            notebook = notebookState.value,
//            locked = true,
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
//                notebook = notebookState.value,
//                locked = true
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

    var coroutineScope = rememberCoroutineScope()

    DisposableEffect(Unit) {
        if (generatedNoteId.value.toInt() == 0) {
            if (backgroundColor.value != backgroundColor1) {
                val note = Note(
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = Constant.NOT_CATEGORIZED,
                    timeStamp = System.currentTimeMillis(),
                    locked = true,
                    color = backgroundColor.value.toArgb(),
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.insertNote(note)
            } else {
                val note = Note(
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = Constant.NOT_CATEGORIZED,
                    timeStamp = System.currentTimeMillis(),
                    locked = true,
                    color = 0,
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.insertNote(note)
            }
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
                if (backgroundColor.value != backgroundColor1) {
                    val updatedNote = Note(
                        id = generatedNoteId.value.toInt(),
                        title = title.value,
                        content = richTextState.value.toHtml(),
                        timeModified = System.currentTimeMillis(),
                        notebook = Constant.NOT_CATEGORIZED,
                        timeStamp = System.currentTimeMillis(),
                        locked = true,
                        color = backgroundColor.value.toArgb(),
                        font = fontFamilyString.value,
                        tags = listOfSelectedTags.toCollection(ArrayList())
                    )
                    viewModel.updateNote(updatedNote)
                } else {
                    val updatedNote = Note(
                        id = generatedNoteId.value.toInt(),
                        title = title.value,
                        content = richTextState.value.toHtml(),
                        timeModified = System.currentTimeMillis(),
                        notebook = Constant.NOT_CATEGORIZED,
                        timeStamp = System.currentTimeMillis(),
                        locked = true,
                        color = 0,
                        font = fontFamilyString.value,
                        tags = listOfSelectedTags.toCollection(ArrayList())
                    )
                    viewModel.updateNote(updatedNote)
                }
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
                if (backgroundColor.value != backgroundColor1) {
                    val updatedNote = Note(
                        id = generatedNoteId.value.toInt(),
                        title = title.value,
                        content = richTextState.value.toHtml(),
                        timeModified = System.currentTimeMillis(),
                        notebook = Constant.NOT_CATEGORIZED,
                        timeStamp = System.currentTimeMillis(),
                        locked = true,
                        color = backgroundColor.value.toArgb(),
                        font = fontFamilyString.value,
                        tags = listOfSelectedTags.toCollection(ArrayList())
                    )
                    viewModel.updateNote(updatedNote)
                } else {
                    val updatedNote = Note(
                        id = generatedNoteId.value.toInt(),
                        title = title.value,
                        content = richTextState.value.toHtml(),
                        timeModified = System.currentTimeMillis(),
                        notebook = Constant.NOT_CATEGORIZED,
                        timeStamp = System.currentTimeMillis(),
                        locked = true,
                        color = 0,
                        font = fontFamilyString.value,
                        tags = listOfSelectedTags.toCollection(ArrayList())
                    )
                    viewModel.updateNote(updatedNote)
                }
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
            if (backgroundColor.value != backgroundColor1) {
                val updatedNote = Note(
                    id = generatedNoteId.value.toInt(),
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = Constant.NOT_CATEGORIZED,
                    timeStamp = System.currentTimeMillis(),
                    locked = true,
                    color = backgroundColor.value.toArgb(),
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.updateNote(updatedNote)
            } else {
                val updatedNote = Note(
                    id = generatedNoteId.value.toInt(),
                    title = title.value,
                    content = richTextState.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = Constant.NOT_CATEGORIZED,
                    timeStamp = System.currentTimeMillis(),
                    locked = true,
                    color = 0,
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.updateNote(updatedNote)
            }
            navController.navigateUp()
        } else {
            viewModel.deleteNoteById(generatedNoteId.value.toInt())
            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }
    // WindowCompat.setDecorFitsSystemWindows(activity.window, false)


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
                            if (backgroundColor.value != backgroundColor1) {
                                var note1 = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    content = richTextState.value.toHtml(),
                                    timeModified = System.currentTimeMillis(),
                                    notebook = notebookState.value,
                                    locked = true,
                                    color = backgroundColor.value.toArgb(),
                                    font = fontFamilyString.value,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                                )
                                viewModel.updateNote(note1)
                            } else {
                                var note1 = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    content = richTextState.value.toHtml(),
                                    timeModified = System.currentTimeMillis(),
                                    notebook = notebookState.value,
                                    locked = true,
                                    color = 0,
                                    font = fontFamilyString.value,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                                )
                                viewModel.updateNote(note1)
                            }
                            Toast.makeText(context, "Note has been added", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Undo")
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
                            if (backgroundColor.value != backgroundColor1) {
                                var note1 = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    content = richTextState.value.toHtml(),
                                    timeModified = System.currentTimeMillis(),
                                    notebook = notebookState.value,
                                    locked = true,
                                    color = backgroundColor.value.toArgb(),
                                    font = fontFamilyString.value,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                                )
                                viewModel.updateNote(note1)
                            } else {
                                var note1 = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    content = richTextState.value.toHtml(),
                                    timeModified = System.currentTimeMillis(),
                                    notebook = notebookState.value,
                                    locked = true,
                                    color = 0,
                                    font = fontFamilyString.value,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                                )
                                viewModel.updateNote(note1)
                            }
                            Toast.makeText(context, "Note has been added", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Check, contentDescription = "Save")
                    }
                }
            )
        }

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
                    AddNoteBottomSheet(
                        showBottomSheet,
                        backgroundColor = backgroundColor,
                        activity = activity
                    )
                }
                if (showTextColorBottomSheet.value) {
                    TextColorBottomSheet(showTextColorBottomSheet, richTextState)
                }
                if (showFontBottomSheet.value) {
                    FontBottomSheet(showFontBottomSheet, richTextState, fontFamily)
                }
                NoteContentNoteInLockedScreen(
                    title,
                    content,
                    viewModel,
                    notebookState,
                    showCircularProgress,
                    textFieldValue,
                    boldText,
                    richTextState.value,
                    hideFormattingTextBarWhileTitleIsInFocus,
                    backgroundColor,
                    fontFamily,
                    listOfSelectedTags
//                notebook,
//                notebookFromDB)
                )
            }
            if (!hideFormattingTextBarWhileTitleIsInFocus.value) {
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
                    BottomTextFormattingBarLockedNote(
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
                        showTextColorBottomSheet = showTextColorBottomSheet,
                        showFontBottomSheet = showFontBottomSheet
                    )
                }
            }
        }
    }

}


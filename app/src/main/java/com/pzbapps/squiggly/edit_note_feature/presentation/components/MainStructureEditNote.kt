package com.pzbapps.squiggly.edit_note_feature.presentation.components

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.auth
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.AddNoteBottomSheet
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomTextFormattingBar
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.*
import com.pzbapps.squiggly.common.presentation.alertboxes.AlertDialogBoxTrialEnded
import com.pzbapps.squiggly.common.presentation.fontsbottomsheet.FontBottomSheet
import com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.TextColorBottomSheet
import com.pzbapps.squiggly.edit_note_feature.domain.usecase.checkIfUserHasCreatedPassword
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertBoxShareNote
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertBoxShowRationale
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertDialogBoxDelete
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertDialogBoxEnterPassword
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertDialogBoxEnterPasswordToUnlock
import com.pzbapps.squiggly.edit_note_feature.presentation.components.alertBoxes.AlertDialogBoxPassword
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.LoadingDialogBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.YouNeedToLoginFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.Stack
import kotlin.collections.ArrayList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureEditNote(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    id: Int,
    activity: MainActivity,
    screen: String,
    notesid: MutableIntState,
    query: String
) {
    var context = LocalContext.current

    var dialogOpen = remember {
        mutableStateOf(false)
    }

    var coroutineScope = rememberCoroutineScope()

    var richStateText = mutableStateOf(rememberRichTextState())

    var isBoldActivated = remember { mutableStateOf(false) }
    var isUnderlineActivated = remember { mutableStateOf(false) }
    var isItalicActivated = remember { mutableStateOf(false) }
    var isOrderedListActivated = remember { mutableStateOf(false) }
    var isUnOrderedListActivated = remember { mutableStateOf(false) }
    var showFontSize = remember { mutableStateOf(false) }
    var fontSize = remember { mutableStateOf("20") }
    var iconSize = remember { mutableStateOf(IntSize.Zero) }
    var iconPosition = remember { mutableStateOf(Offset.Zero) }

    val showBottomSheet = remember { mutableStateOf(false) }
    val showTextColorBottomSheet = remember { mutableStateOf(false) }

    if (richStateText.value.annotatedString.text == "") fontSize.value = "20"

    var note = remember { mutableStateOf(Note()) }

    var scope = rememberCoroutineScope()

    viewModel.getNoteById(id)
    //LaunchedEffect(true) {


    scope.launch {
        note.value = viewModel.getNoteByIdFlow.first { it != null }!!
    }
//        viewModel.getNoteByIdLivData2.observe(activity){
//            note.value = it
//        }
    //  }

    var title by rememberSaveable {
        mutableStateOf("")
    }


    var content = rememberSaveable {
        mutableStateOf("")
    }

    var mutableListOfCheckboxTexts = RememberSaveableSnapshotStateList()

    var converted = rememberSaveable { ArrayList<String>() }

// This is the list of checkbox notes which we saved in checkboxes
    var mutableListOfCheckBoxes =
        rememberSaveable { mutableStateOf<ArrayList<Boolean>>(arrayListOf()) }// This is the llst of checkboxes

    var convertedBulletPoints = rememberSaveable { ArrayList<String>() }

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

    var mutableListOfBulletPoints = RememberSaveableSnapshotStateList()

    var enterPasswordToLockDialogBox = remember { mutableStateOf(false) }

    var enterPasswordToUnLockDialogBox = remember { mutableStateOf(false) }

    var showTrialEndedDialogBox = remember { mutableStateOf(false) }

    var count = rememberSaveable { mutableStateOf(0) }

    var countBullet = rememberSaveable { mutableStateOf(0) }

    var hideFormattingTextBarWhenTitleIsInFocus = remember { mutableStateOf(true) }

    var showMovingToArchiveLoadingBox = remember { mutableStateOf(false) }


    var showMovingFromArchiveLoadingBox = remember { mutableStateOf(false) }

    var showDeletingNoteDialogBox = remember { mutableStateOf(false) }

    var showYouNeedToLoginFIrst = rememberSaveable { mutableStateOf(false) }

    var showShareDialogBox = remember { mutableStateOf(false) }

    var showMenu = remember { mutableStateOf(false) }

    var pinnedOrNot = remember { mutableStateOf(false) }
    var menuPosition = remember { mutableStateOf(DpOffset.Zero) }
    val density = LocalDensity.current

    var time = remember { mutableLongStateOf(0) }

    var systemTime = remember { mutableLongStateOf(0) }

    var showRationaleDialogBox = remember { mutableStateOf(false) }

    var timeInString = remember { mutableStateOf("") }


    val backgroundColor = remember { mutableStateOf<Int>(0) }

    var fontFamily = remember { mutableStateOf(FontFamily.fontFamilyRegular) }

    var fontFamilyString = remember { mutableStateOf("Default") }

    var showFontBottomSheet = remember { mutableStateOf(false) }

    var listOfSelectedTags = remember { mutableStateListOf<String>() }

    val showTags = remember { mutableStateOf(false) }

    val showActiveReminders = remember { mutableStateOf(false) }




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


    var notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

        } else {
            Toast.makeText(
                activity,
                "This permission is needed to show the notification of reminder",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        listOfSelectedTags.addAll(note.value.tags)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getNoteById(id)
        var noteFromDb = viewModel.getNoteById
        pinnedOrNot.value = noteFromDb.value.notePinned
        backgroundColor.value = noteFromDb.value.color
        fontFamilyString.value = noteFromDb.value.font
        //  println(listOfSelectedTags.size)
        when (fontFamilyString.value) {
            FontFamily.lufgaRegular -> fontFamily.value = FontFamily.fontFamilyRegular
            FontFamily.lufgaBold -> fontFamily.value = FontFamily.fontFamilyBold
            FontFamily.lufgaextraLight -> fontFamily.value = FontFamily.fontFamilyExtraLight
            FontFamily.pacificoString -> fontFamily.value = FontFamily.pacificoRegular
            FontFamily.parkinsonsString -> fontFamily.value = FontFamily.parkinsons
            FontFamily.jaroString -> fontFamily.value = FontFamily.jaro
            FontFamily.dancingScriptString -> fontFamily.value = FontFamily.dancingScript
            FontFamily.dotoString -> fontFamily.value = FontFamily.doto
            FontFamily.eduString -> fontFamily.value = FontFamily.edu
            FontFamily.lobsterString -> fontFamily.value = FontFamily.lobster
            FontFamily.playfairString -> fontFamily.value = FontFamily.playfair
            FontFamily.poppinsString -> fontFamily.value = FontFamily.poppins
            else -> FontFamily.fontFamilyRegular
        }
    }


    LaunchedEffect(key1 = true) {
        // WindowCompat.setDecorFitsSystemWindows(activity.window, false)
    }

    LaunchedEffect(key1 = Unit) {
        for (i in note.value.listOfCheckedNotes) {
            val value = mutableStateOf(i).value

            // Extract the string values from the SnapshotStateList
            val stringList = mutableListOfCheckboxTexts.map { it.value }

            // Check if the string value is already present
            if (!stringList.contains(value)) {
                mutableListOfCheckboxTexts.add(mutableStateOf(i))
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        for (i in note.value.listOfBulletPointNotes) {
            val value = mutableStateOf(i).value

            // Extract the string values from the SnapshotStateList
            val stringList = mutableListOfBulletPoints.map { it.value }

            // Check if the string value is already present
            if (!stringList.contains(value)) {
                mutableListOfBulletPoints.add(mutableStateOf(i))
            }
        }
    }


    LaunchedEffect(Unit) {
        if (note.value != null) {
            mutableListOfCheckBoxes.value = note.value.listOfCheckedBoxes
        }
    }


    LaunchedEffect(key1 = true) {
        if (title == "" && content.value == "") {
            title = note.value.title ?: ""
            content.value = note.value.content ?: "Failed to get the contents.Please try again"
        }
        notebook = note.value.notebook

    }

    val undoStack = remember { Stack<String>() }
    val redoStack = remember { Stack<String>() }

    // Track the current content as a snapshot
    var currentContent = remember { mutableStateOf("") }

    LaunchedEffect(richStateText.value) {
        snapshotFlow { richStateText.value.annotatedString }
            .debounce(200)
            .map { richStateText.value.toHtml() }// To avoid frequent updates, only take new state every 300ms
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

//    if (note.value != null) {
//        if (note.value.listOfCheckedNotes.isEmpty() && note.value.listOfBulletPointNotes.isEmpty()) {
//            DisposableEffect(Unit) {
//                val job = coroutineScope.launch(Dispatchers.IO) {
//                    // Delay the autosave for 3 seconds, then run it every 10 seconds
//                    delay(3000L)
//                    while (isActive) {
//                        // Get the note by ID and update it
//                        viewModel.getNoteById(note.value.id)
//                        activity.lifecycleScope.launch(Dispatchers.IO) {
//                            val noteFromDb = viewModel.getNoteById.value
//                            var note = noteFromDb.copy(
//                                title = title,
//                                content = richStateText.value.toHtml(),
//                                timeModified = System.currentTimeMillis(),
//                                notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
////                listOfBulletPointNotes = convertedBulletPoints,
////                listOfCheckedNotes = converted,
////                listOfCheckedBoxes = mutableListOfCheckBoxes
//
//                            )
//                        viewModel.updateNote(note)
//                            delay(5000L)
//                        }
//                        // Save every 10 seconds
//                    }
//                }
//                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
//
//                onDispose {
//                    job.cancel()  // Cancel the coroutine when the component is disposed
//                }
//            }
//        }
//    }

    val lifecycleOwner = LocalLifecycleOwner.current

// Observe the lifecycle to detect when the app goes into the background
    if (content.value.isNotEmpty()) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    var formattedText =
                        removeHighlights(richStateText.value.toHtml())
                    richStateText.value.setHtml(formattedText)
                    // Trigger autosave when app goes to background (onStop)
                    viewModel.getNoteById(note.value.id)
                    activity.lifecycleScope.launch {
                        val noteFromDb = viewModel.getNoteByIdFlow.first() { true }
                        var note = noteFromDb?.copy(
                            title = title,
                            content = richStateText.value.toHtml(),
                            timeModified = System.currentTimeMillis(),
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                            color = backgroundColor.value,
                            font = fontFamilyString.value,
                            tags = listOfSelectedTags.toCollection(ArrayList())

                        )
                        viewModel.updateNote(note!!)
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            // Cleanup the observer when the Composable is disposed
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

// Observe the lifecycle to detect when the app goes into the background
    if (mutableListOfCheckboxTexts.size > 0) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    convertMutableStateIntoString(
                        mutableListOfCheckboxTexts,
                        converted
                    )
                    // Trigger autosave when app goes to background (onStop)
                    viewModel.getNoteById(note.value.id)
                    activity.lifecycleScope.launch {
                        val noteFromDb = viewModel.getNoteById.value
                        var note = noteFromDb?.copy(
                            title = title,
                            // content = richStateText.value.toHtml(),
                            timeModified = System.currentTimeMillis(),
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
//                listOfBulletPointNotes = convertedBulletPoints,
                            listOfCheckedNotes = converted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes.value,
                            color = backgroundColor.value,
                            font = fontFamilyString.value,
                            tags = listOfSelectedTags.toCollection(ArrayList())

                        )
                        viewModel.updateNote(note!!)
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            // Cleanup the observer when the Composable is disposed
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    if (mutableListOfBulletPoints.size > 0) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_STOP) {
                    var formattedText =
                        removeHighlights(richStateText.value.toHtml())
                    richStateText.value.setHtml(formattedText)
                    convertMutableStateIntoString(
                        mutableListOfBulletPoints,
                        convertedBulletPoints
                    )
                    // Trigger autosave when app goes to background (onStop)
                    viewModel.getNoteById(note.value.id)
                    activity.lifecycleScope.launch {
                        val noteFromDb = viewModel.getNoteById.value
                        var note = noteFromDb?.copy(
                            title = title,
                            // content = richStateText.value.toHtml(),
                            timeModified = System.currentTimeMillis(),
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                            listOfBulletPointNotes = convertedBulletPoints,
                            color = backgroundColor.value,
                            tags = listOfSelectedTags.toCollection(ArrayList()),
                            font = fontFamilyString.value,


                            )
                        viewModel.updateNote(note!!)
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            // Cleanup the observer when the Composable is disposed
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    if (mutableListOfCheckboxTexts.size > 0) {
        LaunchedEffect(key1 = count.value) {
            delay(500)

            viewModel.getNoteById(id)
            var noteFromDb = viewModel.getNoteById

            var listOfNotes = noteFromDb.value.listOfCheckedNotes

            for (i in listOfNotes) {
                if (!converted.contains(i)) {
                    converted.add(i)
                }
            }

            convertMutableStateIntoString(
                mutableListOfCheckboxTexts,
                converted
            )

            converted.removeAll { it == "" }
            var note1 = noteFromDb.value.copy(
                title = title,
                timeModified = System.currentTimeMillis(),
                //timeStamp = System.currentTimeMillis(),
                notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                listOfCheckedNotes = converted,
                listOfCheckedBoxes = mutableListOfCheckBoxes.value,
                color = backgroundColor.value,
                tags = listOfSelectedTags.toCollection(ArrayList()),
                font = fontFamilyString.value,
            )
            viewModel.updateNote(note1)
        }
    }

    if (mutableListOfBulletPoints.size > 0) {
        LaunchedEffect(key1 = countBullet.value) {
            delay(500)

            viewModel.getNoteById(id)
            var noteFromDb = viewModel.getNoteById

            convertMutableStateIntoString(
                mutableListOfBulletPoints,
                convertedBulletPoints
            )

            convertedBulletPoints.removeAll { it == "" }
            var note1 = noteFromDb.value.copy(
                title = title,
                timeModified = System.currentTimeMillis(),
                //timeStamp = System.currentTimeMillis(),
                notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                listOfBulletPointNotes = convertedBulletPoints,
                color = backgroundColor.value,
                tags = listOfSelectedTags.toCollection(ArrayList()),
                font = fontFamilyString.value,
            )
            viewModel.updateNote(note1)
        }
    }

    var remember = rememberCoroutineScope()
    BackHandler {
        var formattedText =
            removeHighlights(richStateText.value.toHtml())
        richStateText.value.setHtml(formattedText)
        var analytics = com.google.firebase.ktx.Firebase.analytics
        var bundle = Bundle()
        bundle.putString("back_handler_triggered_edit_notes", "back_handler_triggered_edit_notes")
        analytics.logEvent("back_handler_triggered_edit_notes", bundle)
        // keyboardController?.hide()
        remember.launch(Dispatchers.Main) {
            if (note.value.listOfCheckedNotes.size > 0 || note.value.listOfBulletPointNotes.size > 0) {
                count.value++
                countBullet.value++
            } else {
                viewModel.getNoteById(note.value.id)
                val noteFromDb = viewModel.getNoteById.value
                var reminder = noteFromDb.reminder
                var note = noteFromDb.copy(
                    title = title,
                    content = richStateText.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                    reminder = reminder,
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                )
                viewModel.updateNote(note)
            }
            navController.navigateUp()
        }
    }


    if (showTrialEndedDialogBox.value) {
        AlertDialogBoxTrialEnded {
            showTrialEndedDialogBox.value = false
        }
    }

//    var debounceJob: Job? = null
//
//    DisposableEffect(Unit) {
//        scope.launch {
//            // Listen for changes in the title or content
//            snapshotFlow { title to richStateText.value.toHtml() }
//                .debounce(2000L)  // Wait for 2 seconds of inactivity before saving
//                .collect { (newTitle, newContent) ->  // Destructure the Pair here
//                    // When the user stops typing, save the note
//                    viewModel.getNoteById(note.value.id)
//                    val note = viewModel.getNoteById.value
//                    val updatedNote = note.copy(
//                        title = newTitle,
//                        content = newContent,
//                        timeModified = System.currentTimeMillis(),
//                        notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value
//                    )
//                    viewModel.updateNote(updatedNote)
//                }
//        }
//        Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
//
//        onDispose {
//            debounceJob?.cancel()
//        }
//    }

    var lastContent =
        remember { mutableStateOf(richStateText.value.annotatedString.text) } // Track last saved content
    var isTyping by remember { mutableStateOf(false) }

    LaunchedEffect(isTyping) {
        if (!isTyping) {
            delay(2000) // Wait for 2 seconds after typing stops
            var formattedText =
                removeHighlights(richStateText.value.toHtml())
            richStateText.value.setHtml(formattedText)
            if (richStateText.value.annotatedString.text != lastContent.value) {
                viewModel.getNoteById(note.value.id)
                val noteFromDb = viewModel.getNoteById.value
                var reminder = noteFromDb.reminder
                var note = noteFromDb.copy(
                    title = title,
                    content = richStateText.value.toHtml(),
                    timeModified = System.currentTimeMillis(),
                    notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                    reminder = reminder,
                    color = backgroundColor.value,
                    font = fontFamilyString.value,
                    tags = listOfSelectedTags.toCollection(ArrayList())
//                listOfBulletPointNotes = convertedBulletPoints,
//                listOfCheckedNotes = converted,
//                listOfCheckedBoxes = mutableListOfCheckBoxes

                )
                viewModel.updateNote(note)
                lastContent.value =
                    richStateText.value.annotatedString.text // Update the last saved content
            }
        }
    }

    LaunchedEffect(richStateText.value.annotatedString.text) {
        while (true) {
            delay(500) // Poll every 500ms to check for changes
            if (richStateText.value.annotatedString.text != lastContent.value) {
                isTyping = true
                coroutineScope.launch {
                    delay(2000)
                    isTyping = false
                }
            }
        }
    }




    Scaffold(
        topBar = {
            androidx.compose.material3.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(backgroundColor.value)
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        var formattedText =
                            removeHighlights(richStateText.value.toHtml())
                        richStateText.value.setHtml(formattedText)
                        notesid.intValue = -1
                        var analytics = com.google.firebase.ktx.Firebase.analytics
                        var bundle = Bundle()
                        bundle.putString(
                            "back_button_pressed_edit_notes",
                            "back_button_pressed_edit_notes"
                        )
                        analytics.logEvent("back_button_pressed_edit_notes", bundle)
                        println("MUTABLESTATE:$mutableListOfCheckBoxes")
                        convertMutableStateIntoString(mutableListOfCheckboxTexts, converted)
                        convertMutableStateIntoString(
                            mutableListOfBulletPoints,
                            convertedBulletPoints
                        )
                        viewModel.getNoteById(id)
                        var noteFromDb = viewModel.getNoteById
                        var archived = noteFromDb.value.archive
                        var lockedOrNote = noteFromDb.value.locked
                        var timeCreated = noteFromDb.value.timeStamp
                        var pinned = noteFromDb.value.notePinned
                        var reminder = noteFromDb.value.reminder
                        var note = Note(
                            id,
                            title,
                            richStateText.value.toHtml(),
                            archived,
                            locked = lockedOrNote,
                            listOfCheckedNotes = converted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes.value,
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                            listOfBulletPointNotes = convertedBulletPoints,
                            timeStamp = timeCreated,
                            timeModified = System.currentTimeMillis(),
                            notePinned = pinned,
                            reminder = reminder,
                            color = backgroundColor.value,
                            font = fontFamilyString.value,
                            tags = listOfSelectedTags.toCollection(ArrayList())
                        )
                        viewModel.updateNote(note)
                        Toast.makeText(context, "Note has been updated", Toast.LENGTH_SHORT)
                            .show()
                        scope.launch {
                            delay(200)
                            navController.navigateUp()
                        }

                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Undo")
                    }
                },
                title = { Text(text = "") },
                actions = {

                    if (screen == Constant.HOME || screen == Constant.LOCKED_NOTE) {
                        IconButton(onClick = {
                            var analytics = com.google.firebase.ktx.Firebase.analytics
                            var bundle = Bundle()
                            bundle.putString(
                                "pinned_button_pressed_edit_notes",
                                "pinned_button_pressed_edit_notes"
                            )
                            analytics.logEvent(
                                "pinned_button_pressed_edit_notes",
                                bundle
                            )
                            pinOrUnpinNote(viewModel, id, scope, navController)

                        }) {
                            Icon(
                                imageVector = if (pinnedOrNot.value) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                                contentDescription = "pin note",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                    if (screen == Constant.HOME || screen == Constant.LOCKED_NOTE) {
                        IconButton(onClick = {
                            if (screen == Constant.HOME) {
                                var analytics =
                                    com.google.firebase.ktx.Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString(
                                    "lock_button_pressed_edit_notes",
                                    "lock_button_pressed_edit_notes"
                                )
                                analytics.logEvent(
                                    "lock_button_pressed_edit_notes",
                                    bundle
                                )
                                val user = Firebase.auth.currentUser
                                if (user != null) {
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
                                } else {
                                    showYouNeedToLoginFIrst.value = true
                                }
                            } else if (screen == Constant.LOCKED_NOTE) {
                                var analytics =
                                    com.google.firebase.ktx.Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString(
                                    "unlock_button_pressed_edit_notes",
                                    "unlock_button_pressed_edit_notes"
                                )
                                analytics.logEvent(
                                    "unlock_button_pressed_edit_notes",
                                    bundle
                                )
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
                    }
                    if (screen == Constant.HOME || screen == Constant.ARCHIVE) {
                        IconButton(onClick = {

                            if (screen == Constant.HOME) {
                                var analytics =
                                    com.google.firebase.ktx.Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString(
                                    "archive_button_pressed_edit_notes",
                                    "archive_button_pressed_edit_notes"
                                )
                                analytics.logEvent(
                                    "archive_button_pressed_edit_notes",
                                    bundle
                                )

                                showMovingToArchiveLoadingBox.value = true

                                moveToArchive(
                                    id = id,
                                    navController = navController,
                                    viewModel = viewModel,
                                    scope = scope,
                                    activity = activity,
                                    showMovingToArchiveLoadingBox
                                )


                            } else if (screen == Constant.ARCHIVE) {
                                var analytics =
                                    com.google.firebase.ktx.Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString(
                                    "unarchive_button_pressed_edit_notes",
                                    "unarchive_button_pressed_edit_notes"
                                )
                                analytics.logEvent(
                                    "unarchive_button_pressed_edit_notes",
                                    bundle
                                )

                                showMovingFromArchiveLoadingBox.value = true

                                unArchiveNote(
                                    id,
                                    viewModel,
                                    activity,
                                    navController,
                                    scope,
                                    showMovingFromArchiveLoadingBox
                                )
                            }

                        }) {
                            Icon(
                                imageVector = if (screen == Constant.HOME) Icons.Filled.Archive else if (screen == Constant.LOCKED_NOTE) Icons.Filled.Archive else Icons.Filled.Unarchive,
                                contentDescription = "Archive"
                            )
                        }
                    }
                    IconButton(onClick = {
                        // viewModel.deleteNoteById(id)
                        var analytics = com.google.firebase.ktx.Firebase.analytics
                        var bundle = Bundle()
                        bundle.putString(
                            "delete_button_pressed_edit_notes",
                            "delete_button_pressed_edit_notes"
                        )
                        analytics.logEvent("delete_button_pressed_edit_notes", bundle)
                        dialogOpen.value = true

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }
                    IconButton(onClick = {
                        var formattedText =
                            removeHighlights(richStateText.value.toHtml())
                        richStateText.value.setHtml(formattedText)
                        var analytics = com.google.firebase.ktx.Firebase.analytics
                        var bundle = Bundle()
                        bundle.putString(
                            "save_button_pressed_edit_notes",
                            "save_button_pressed_edit_notes"
                        )
                        analytics.logEvent("save_button_pressed_edit_notes", bundle)

                        convertMutableStateIntoString(
                            mutableListOfCheckboxTexts,
                            converted
                        )
                        convertMutableStateIntoString(
                            mutableListOfBulletPoints,
                            convertedBulletPoints
                        )
                        viewModel.getNoteById(id)
                        var noteFromDb = viewModel.getNoteById
                        var archived = noteFromDb.value.archive
                        var lockedOrNote = noteFromDb.value.locked
                        var timeCreated = noteFromDb.value.timeStamp
                        var pinned = noteFromDb.value.notePinned
                        var reminder = noteFromDb.value.reminder
                        var note = Note(
                            id,
                            title,
                            richStateText.value.toHtml(),
                            archived,
                            locked = lockedOrNote,
                            listOfCheckedNotes = converted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes.value,
                            notebook = if (selectedNotebook.value == "") notebook else selectedNotebook.value,
                            listOfBulletPointNotes = convertedBulletPoints,
                            timeStamp = timeCreated,
                            timeModified = System.currentTimeMillis(),
                            notePinned = pinned,
                            reminder = reminder,
                            color = backgroundColor.value,
                            font = fontFamilyString.value,
                            tags = listOfSelectedTags.toCollection(ArrayList())
                        )
                        viewModel.updateNote(note)
                        scope.launch {
                            delay(200)
                            navController.navigateUp()
                        }

                        Toast.makeText(
                            context,
                            "Note has been updated",
                            Toast.LENGTH_SHORT
                        )
                            .show()


                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save"
                        )
                    }
                    IconButton(
                        onClick = { showMenu.value = true },
                        modifier = Modifier.onGloballyPositioned { coordinates ->
                            // Capture icon size and position on the screen
                            iconSize.value = coordinates.size
                            iconPosition.value = coordinates.localToWindow(Offset.Zero)
                        }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Reminder",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    DropdownMenu(
                        modifier = Modifier.wrapContentWidth(),
                        expanded = showMenu.value,
                        onDismissRequest = { showMenu.value = false },
                        offset = with(density) {
                            // Calculate the DpOffset for the DropdownMenu based on icon position and size
                            DpOffset(
                                x = iconPosition.value.x.toDp(),
                                y = (iconPosition.value.y.toDp() + iconSize.value.height.toDp() - 90.dp)
                            )
                        }
                    ) {
                        DropdownMenuItem(onClick = {
                            // Handle option 1 click
                            showShareDialogBox.value = true
                            showMenu.value = false
                        }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Share,
                                    contentDescription = "Share the note",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    "Share the note",
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 17.sp
                                )
                            }
                        }
                        DropdownMenuItem(
                            onClick = {
                                // Handle option 2 click
                                if (systemTime.longValue > time.longValue) {
                                    addReminder(
                                        activity,
                                        note,
                                        title,
                                        showMenu,
                                        notificationLauncher,
                                        viewModel,
                                        time,
                                        systemTime,
                                        showRationaleDialogBox,
                                        timeInString,
                                    )
                                } else {
                                    showActiveReminders.value = true
                                }

                            }) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.AlarmAdd,
                                    contentDescription = "Reminder",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    "Reminder",
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 17.sp
                                )
                            }
                        }
                        if (mutableListOfCheckboxTexts.size > 0 || mutableListOfBulletPoints.size > 0) {
                            DropdownMenuItem(
                                onClick = {
                                    // Handle option 2 click
                                    showFontBottomSheet.value = true

                                }) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.FontDownload,
                                        contentDescription = "Font",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        "Fonts",
                                        fontFamily = FontFamily.fontFamilyRegular,
                                        fontSize = 17.sp
                                    )
                                }
                            }
                            DropdownMenuItem(onClick = {
                                var analytics = com.google.firebase.ktx.Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString(
                                    "color_button_pressed_add_note_screen",
                                    "color_button_pressed_add_note_screen"
                                )
                                analytics.logEvent("color_button_pressed_add_note_screen", bundle)
                                showBottomSheet.value = true
                                showMenu.value = false
                            }) {

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Filled.Palette,
                                        contentDescription = "Background color",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        "Background",
                                        fontFamily = FontFamily.fontFamilyRegular,
                                        fontSize = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            )


        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(backgroundColor.value))
        ) {
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
                    richStateText,
                    count,
                    converted,
                    countBullet,
                    convertedBulletPoints,
                    hideFormattingTextBarWhenTitleIsInFocus,
                    { title = it },
                    { content.value = it },
                    screen,
                    note,
                    showMenu,
                    notificationLauncher,
                    time,
                    systemTime,
                    timeInString,
                    backgroundColor,
                    fontFamily,
                    listOfSelectedTags,
                    showTags,
                    query,
                    showActiveReminders
                )
            }
            if (mutableListOfCheckboxTexts.size == 0 && mutableListOfBulletPoints.size == 0 && !hideFormattingTextBarWhenTitleIsInFocus.value) {
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
                        isUnOrderedListActivated = isUnOrderedListActivated,
                        mutableStateOf(false),
                        undoStack = undoStack,
                        redoStack = redoStack,
                        currentContent = currentContent,
                        showBottomSheet = showBottomSheet,
                        showTextColorBottomSheet = showTextColorBottomSheet,
                        showFontBottomSheet = showFontBottomSheet,
                        showTags = showTags
                    )
                }
            }
        }
    }
    if (dialogOpen.value) {
        AlertDialogBoxDelete(
            viewModel = viewModel,
            id = id,
            activity = activity,
            navHostController = navController,
            showDeletingNoteDialogBox
            // note = note
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
            listOfCheckboxes = mutableListOfCheckBoxes.value,
            listOfBulletPoints = convertedBulletPoints,
            content = content.value
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
            listOfCheckBoxes = mutableListOfCheckBoxes.value,
            listOfBulletPoints = convertedBulletPoints,
            content = content.value
        ) {
            enterPasswordToUnLockDialogBox.value = false

        }
    }
    if (showMovingToArchiveLoadingBox.value) {
        LoadingDialogBox(text = mutableStateOf("Moving to Archive"))
    }
    if (showMovingFromArchiveLoadingBox.value) {
        LoadingDialogBox(text = mutableStateOf("Moving from Archive"))
    }
    if (showDeletingNoteDialogBox.value) {
        LoadingDialogBox(text = mutableStateOf("Moving to trash"))
    }
    if (showYouNeedToLoginFIrst.value) {
        YouNeedToLoginFirst(navController) {
            showYouNeedToLoginFIrst.value = false
        }
    }
    if (showShareDialogBox.value) {
        AlertBoxShareNote(
            title,
            richStateText.value.annotatedString.text,
            mutableListOfCheckBoxes,
            mutableListOfCheckboxTexts,
            converted,
            mutableListOfBulletPoints,
            convertedBulletPoints
        ) {
            showShareDialogBox.value = false
        }
    }
    if (showRationaleDialogBox.value) {
        AlertBoxShowRationale {
            showRationaleDialogBox.value = false
        }
    }
    if (showBottomSheet.value) {
        AddNoteBottomSheet(
            showBottomSheet,
            backgroundColorInInt = backgroundColor,
            activity = activity
        )
    }
    if (showTextColorBottomSheet.value) {
        TextColorBottomSheet(showTextColorBottomSheet, richStateText)
    }
    if (showFontBottomSheet.value) {
        FontBottomSheet(showFontBottomSheet, fontFamily)
    }


}

fun convertMutableStateIntoString(
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>
) {
    mutableListConverted.clear()
    for (i in mutableList) {
        if (!mutableListConverted.contains(i.value)) {
            mutableListConverted.add(i.value)
        }
    }
}

@Composable
fun RememberSaveableSnapshotStateList(): SnapshotStateList<MutableState<String>> {
    // Create a custom saver for SnapshotStateList<Mutable<String>>
    val listSaver =
        Saver<SnapshotStateList<MutableState<String>>, List<List<String>>>(
            save = { snapshotStateList ->
                // Convert SnapshotStateList<Mutable<String>> to List<List<String>> for saving
                snapshotStateList.map { state -> listOf(state.value) }
            },
            restore = { savedList ->
                // Convert List<List<String>> back to SnapshotStateList<Mutable<String>> on restore
                val restoredList = savedList.map { mutableStateOf(it.first()) }
                SnapshotStateList<MutableState<String>>().apply {
                    addAll(restoredList)
                }
            })
    return rememberSaveable(saver = listSaver) {
        mutableStateListOf<MutableState<String>>() // Initial state
    }
}


fun moveToArchive(
    id: Int,
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    scope: CoroutineScope, activity: MainActivity,
    showMovingDialog: MutableState<Boolean>
) {
    scope.launch {
        viewModel.getNoteById(id)
        var it = viewModel.getNoteById.value
        // println("NOTE:$it")
        var note = it.copy(archive = true)

        viewModel.updateNote(note)

        viewModel.getNoteById(id)
        delay(200)
        var note1 = viewModel.getNoteById.value
        if (!note1.archive) {
            println("NESTED TRIGGERED")
            moveToArchive(
                id = id,
                navController = navController,
                viewModel = viewModel,
                scope = scope,
                activity = activity,
                showMovingDialog = showMovingDialog
            )
        } else {

            println("NOTE1:$note")


            delay(200)
            showMovingDialog.value = false
            navController.navigateUp()
            delay(200)
            Toast.makeText(
                activity,
                "Note has been archived",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}

fun unArchiveNote(
    id: Int,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    navController: NavHostController,
    scope: CoroutineScope,
    showMoveFromArchiveDialog: MutableState<Boolean>
) {
    scope.launch {
        viewModel.getNoteById(id)
        var it = viewModel.getNoteById.value
        //println("NOTE:$it")
        var note = it.copy(archive = false)

        viewModel.updateNote(note)
        delay(200)
        viewModel.getNoteById(id)
        var note1 = viewModel.getNoteById.value
        if (note1.archive) {
            println("NESTED TRIGGERED")
            unArchiveNote(
                id,
                viewModel,
                activity,
                navController,
                scope,
                showMoveFromArchiveDialog
            )
        } else {
            // println("NOTE1:$note")


            delay(200)
            showMoveFromArchiveDialog.value = false
            navController.navigateUp()
            delay(200)
            Toast.makeText(
                activity,
                "Note has been unarchived",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
}

fun pinOrUnpinNote(
    viewModel: MainActivityViewModel,
    id: Int,
    scope: CoroutineScope,
    navController: NavHostController
) {
    scope.launch {
        viewModel.getNoteById(id)
        var note = viewModel.getNoteById.value
        var pinnedStatus = note.notePinned
        var note1 = note.copy(notePinned = !pinnedStatus)
        viewModel.updateNote(note1)
        delay(200)
        viewModel.getNoteById(id)
        var note2 = viewModel.getNoteById.value
        if (note2.notePinned == pinnedStatus) {
            println("PINNED TRIGGERED")
            pinOrUnpinNote(viewModel, id, scope, navController)
        } else {
            delay(200)
            navController.navigateUp()
        }
    }
}


fun removeHighlights(text: String): String {
    // Regex to remove spans with background style (rgba)
    return text.replace(
        Regex(
            """<span[^>]*background\s*:\s*rgba\(\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d(\.\d)?)\s*\);?[^>]*>(.*?)</span>""",
            RegexOption.DOT_MATCHES_ALL
        )
    ) {
        it.groupValues[6] // Correctly retrieve the inner content of the span
    }
}

fun decodeHtml(text: String): String {
    return text.replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
}

fun encodeHtml(text: String): String {
    return text.replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("&", "&amp;")
}

fun removeHighlightsWithEscaping(text: String): String {
    val decodedText = decodeHtml(text) // Decode HTML
    val cleanedText = removeHighlights(decodedText) // Remove highlights
    return encodeHtml(cleanedText) // Re-encode HTML
}







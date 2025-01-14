package com.pzbapps.squiggly.notebook_main_screen.presentation.components.checkboxScreenComponents

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.AddNoteBottomSheet
import com.pzbapps.squiggly.add_note_feature.presentation.components.DiscardNoteAlertBox
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.alertboxes.AlertDialogBoxTrialEnded
import com.pzbapps.squiggly.common.presentation.fontsbottomsheet.FontBottomSheet
import com.pzbapps.squiggly.main_screen.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureCheckBoxNotebook(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    activity: MainActivity,
    notebook: String
) {
    var context = LocalContext.current

    var mutableListOfCheckboxTexts = RememberSaveableSnapshotStateList()

    var mutableListConverted = rememberSaveable {
        ArrayList<String>()
    }
    var mutableListOfCheckBoxes = rememberSaveable { ArrayList<Boolean>() }

    var generatedNoteId = rememberSaveable {
        mutableStateOf<Long>(0)
    }

    var count = rememberSaveable {
        mutableStateOf(0)
    }

    var showTrialEndedDialogBox = remember {
        mutableStateOf(
            false
        )
    }

    val listOfSelectedTags =
        remember { mutableStateListOf<String>() } // THESE ARE THE TAGS SELECTED BY THE USER IN ADD NOTE CHECKBOX
    // FEATURE AND WILL BE ADDED TO THE "LIST_OF_TAGS" IN THE NOTE TABLE

    var showDiscardNoteAlertBox = remember { mutableStateOf(false) }

    val backgroundColor1 = MaterialTheme.colors.primary
    val backgroundColor = remember { mutableStateOf(backgroundColor1) }

    val showBottomSheet = remember { mutableStateOf(false) }

    var fontFamily = remember { mutableStateOf(FontFamily.fontFamilyRegular) }

    var fontFamilyString = remember { mutableStateOf("") }

    var showFontBottomSheet = remember { mutableStateOf(false) }

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


    LaunchedEffect(key1 = true) {
        if (mutableListOfCheckboxTexts.isEmpty()) {
            mutableListOfCheckboxTexts.add(mutableStateOf(""))
        }

    }

    LaunchedEffect(key1 = mutableListOfCheckboxTexts.size) {
        mutableListOfCheckBoxes.add(false)

    }

    if (showTrialEndedDialogBox.value) {
        AlertDialogBoxTrialEnded {
            showTrialEndedDialogBox.value = false
        }
    }


    if (generatedNoteId.value.toInt() == 0) {
        LaunchedEffect(key1 = true) {
            if (backgroundColor.value != backgroundColor1) {
                var note = Note(
                    0,
                    title = title.value,
                    timeModified = System.currentTimeMillis(),
                    notebook = notebook,
                    timeStamp = System.currentTimeMillis(),
                    color = backgroundColor.value.toArgb(),
                    tags = listOfSelectedTags.toCollection(ArrayList()),
                    font = fontFamilyString.value
//            listOfCheckedNotes = mutableListConverted,
//            listOfCheckedBoxes = mutableListOfCheckBoxes,

                )
                viewModel.insertNote(note)
            } else {
                var note = Note(
                    0,
                    title = title.value,
                    timeModified = System.currentTimeMillis(),
                    notebook = notebook,
                    timeStamp = System.currentTimeMillis(),
                    color = 0,
                    tags = listOfSelectedTags.toCollection(ArrayList()),
                    font = fontFamilyString.value
//            listOfCheckedNotes = mutableListConverted,
//            listOfCheckedBoxes = mutableListOfCheckBoxes,

                )
                viewModel.insertNote(note)
            }

        }
    }
    viewModel.generatedNoteId.observe(activity) {
        generatedNoteId.value = it
        // Schedule a task to run every 10 seconds
    }

    //   println("LIST:${listOfCheckedNotes.size}")
    LaunchedEffect(key1 = count.value) {
        convertMutableStateIntoString(
            mutableListOfCheckboxTexts,
            mutableListConverted
        )
        delay(500)

//        for (i in listOfCheckedNotes) {
//            println("TEXT1:${i.value}")
//        }
//
//        for (i in mutableListConverted) {
//            println("TEXT2:${i}")
//        }

        mutableListConverted.removeAll { it == "" }
        if (backgroundColor.value != backgroundColor1) {
            var note1 = Note(
                id = generatedNoteId.value.toInt(),
                title = title.value,
                timeModified = System.currentTimeMillis(),
                timeStamp = System.currentTimeMillis(),
                notebook = notebook,
                listOfCheckedNotes = mutableListConverted,
                listOfCheckedBoxes = mutableListOfCheckBoxes,
                color = backgroundColor.value.toArgb(),
                tags = listOfSelectedTags.toCollection(ArrayList()),
                font = fontFamilyString.value
            )
            viewModel.updateNote(note1)
        } else {
            var note1 = Note(
                id = generatedNoteId.value.toInt(),
                title = title.value,
                timeModified = System.currentTimeMillis(),
                timeStamp = System.currentTimeMillis(),
                notebook = notebook,
                listOfCheckedNotes = mutableListConverted,
                listOfCheckedBoxes = mutableListOfCheckBoxes,
                color = 0,
                tags = listOfSelectedTags.toCollection(ArrayList()),
                font = fontFamilyString.value
            )
            viewModel.updateNote(note1)
        }
    }

//    LaunchedEffect(key1 = mutableListOfCheckboxTexts.size > 0) {
//        mutableListOfCheckBoxes.add(false)
//    }

    var remember = rememberCoroutineScope()
    BackHandler {
        // keyboardController?.hide()
        remember.launch(Dispatchers.Main) {
            count.value++
            navController.navigateUp()
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                convertMutableStateIntoString(
                    mutableListOfCheckboxTexts,
                    mutableListConverted
                )
                // Trigger autosave when app goes to background (onStop)
                activity.lifecycleScope.launch {
                    mutableListConverted.removeAll { it == "" }
                    if (backgroundColor.value != backgroundColor1) {
                        var note1 = Note(
                            id = generatedNoteId.value.toInt(),
                            title = title.value,
                            timeModified = System.currentTimeMillis(),
                            timeStamp = System.currentTimeMillis(),
                            notebook = notebook,
                            listOfCheckedNotes = mutableListConverted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            color = backgroundColor.value.toArgb(),
                            tags = listOfSelectedTags.toCollection(ArrayList()),
                            font = fontFamilyString.value
                        )
                        viewModel.updateNote(note1)
                    } else {
                        var note1 = Note(
                            id = generatedNoteId.value.toInt(),
                            title = title.value,
                            timeModified = System.currentTimeMillis(),
                            timeStamp = System.currentTimeMillis(),
                            notebook = notebook,
                            listOfCheckedNotes = mutableListConverted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            color = 0,
                            tags = listOfSelectedTags.toCollection(ArrayList()),
                            font = fontFamilyString.value
                        )
                        viewModel.updateNote(note1)
                    }
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        // Cleanup the observer when the Composable is disposed
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

//    LaunchedEffect(key1 = mutableListOfCheckboxTexts.size > 0) {
//        mutableListOfCheckBoxes.add(false)
//    }

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
                        convertMutableStateIntoString(
                            mutableListOfCheckboxTexts,
                            mutableListConverted
                        )
                        if (title.value.isNotEmpty() || (mutableListConverted.size != 1 || mutableListConverted[0].isNotEmpty())) {
                            if (backgroundColor.value != backgroundColor1) {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebook,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = backgroundColor.value.toArgb(),
                                    tags = listOfSelectedTags.toCollection(ArrayList()),
                                    font = fontFamilyString.value
                                )
                                viewModel.updateNote(note)
                            } else {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebook,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = 0,
                                    tags = listOfSelectedTags.toCollection(ArrayList()),
                                    font = fontFamilyString.value
                                )
                                viewModel.updateNote(note)
                            }
                            Toast.makeText(activity, "Note has been saved", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Undo",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        showFontBottomSheet.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.FontDownload,
                            contentDescription = "Fonts",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        var analytics = Firebase.analytics
                        var bundle = Bundle()
                        bundle.putString(
                            "color_button_pressed_add_note_screen",
                            "color_button_pressed_add_note_screen"
                        )
                        analytics.logEvent("color_button_pressed_add_note_screen", bundle)
                        showBottomSheet.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "background Color",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
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
                        convertMutableStateIntoString(
                            mutableListOfCheckboxTexts,
                            mutableListConverted
                        )
                        if (title.value.isNotEmpty() || (mutableListConverted.size != 1 || mutableListConverted[0].isNotEmpty())) {
                            if (backgroundColor.value != backgroundColor1) {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebook,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = backgroundColor.value.toArgb(),
                                    tags = listOfSelectedTags.toCollection(ArrayList()),
                                    font = fontFamilyString.value
                                )
                                viewModel.updateNote(note)
                            } else {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebook,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = 0,
                                    tags = listOfSelectedTags.toCollection(ArrayList()),
                                    font = fontFamilyString.value
                                )
                                viewModel.updateNote(note)
                            }
                            Toast.makeText(activity, "Note has been saved", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        } else {
                            viewModel.deleteNoteById(generatedNoteId.value.toInt())
                            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigateUp()
                        }

                    }) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            )
        },

        // ADD AGAIN WHEN FEATURES WOULD BE ADDED TO BottomAppBar

        /*    bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {

                }
            }*/
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(backgroundColor.value)
        ) {
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
                AddNoteBottomSheet(showBottomSheet, backgroundColor, activity = activity)
            }
            if (showFontBottomSheet.value) {
                FontBottomSheet(showFontBottomSheet, fontFamily, viewModel, navController)
            }
            CheckboxNotebook(
                viewModel,
                navController,
                notebookState,
                title,
                mutableListOfCheckboxTexts,
                mutableListOfCheckBoxes,
                count,
                mutableListConverted,
                backgroundColor,
                listOfSelectedTags,
                fontFamily
            )
        }
    }
}

fun convertMutableStateIntoString(
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>
) {
    for (i in mutableList) {
        if (!mutableListConverted.contains(i.value)) {
            mutableListConverted.add(i.value)
        }
    }
}

@Composable
fun RememberSaveableSnapshotStateList(): SnapshotStateList<MutableState<String>> {
    // Create a custom saver for SnapshotStateList<Mutable<String>>
    val listSaver = Saver<SnapshotStateList<MutableState<String>>, List<List<String>>>(
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
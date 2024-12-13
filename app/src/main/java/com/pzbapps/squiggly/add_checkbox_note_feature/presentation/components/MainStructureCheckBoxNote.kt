package com.pzbapps.squiggly.add_checkbox_note_feature.presentation.components

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
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
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.alertboxes.AlertDialogBoxTrialEnded
import com.pzbapps.squiggly.main_screen.domain.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureCheckBoxNote(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    activity: MainActivity,
    listOfCheckedNotes: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>,
    mutableListOfCheckBoxes: ArrayList<Boolean>
) {
    var context = LocalContext.current
    var generatedNoteId = rememberSaveable { mutableStateOf<Long>(0) }

    val showBottomSheet = remember { mutableStateOf(false) }

    val listOfSelectedTags =
        remember { mutableStateListOf<String>() } // THESE ARE THE TAGS SELECTED BY THE USER IN ADD NOTE CHECKBOX
    // FEATURE AND WILL BE ADDED TO THE "LIST_OF_TAGS" IN THE NOTE TABLE

//    var mutableListOfCheckboxTexts = remember {
//        mutableStateListOf<MutableState<String>>()
//    }


//    if(equate.value) {
//        mutableListOfCheckboxTexts = RememberSaveableSnapshotStateList()
//       equate.value = false
    // }

//    LaunchedEffect(key1 = Unit) {
//        mutableListOfCheckBoxes.add(false)
//        mutableListOfCheckBoxes.removeLast()
//    }


    var count = remember {
        mutableStateOf(0)

    }

    val backgroundColor1 = MaterialTheme.colors.primary
    val backgroundColor = remember { mutableStateOf(backgroundColor1) }

    var showTrialEndedDialogBox = remember {
        mutableStateOf(
            false
        )
    }
    var showDiscardNoteAlertBox = remember { mutableStateOf(false) }


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
                    notebook = notebookState.value,
                    timeStamp = System.currentTimeMillis(),
                    color = backgroundColor.value.toArgb(),
                    tags = listOfSelectedTags.toCollection(ArrayList())
//            listOfCheckedNotes = mutableListConverted,
//            listOfCheckedBoxes = mutableListOfCheckBoxes,

                )
                viewModel.insertNote(note)
            } else {
                var note = Note(
                    0,
                    title = title.value,
                    timeModified = System.currentTimeMillis(),
                    notebook = notebookState.value,
                    timeStamp = System.currentTimeMillis(),
                    color = 0,
                    tags = listOfSelectedTags.toCollection(ArrayList())
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

    if (title.value.isNotEmpty() || (mutableListConverted.size != 1 || mutableListConverted[0].isNotEmpty())) {
        LaunchedEffect(key1 = count.value) {
            convertMutableStateIntoString(
                listOfCheckedNotes,
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
                    notebook = notebookState.value,
                    listOfCheckedNotes = mutableListConverted,
                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                    color = backgroundColor.value.toArgb(),
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.updateNote(note1)
            } else {
                var note1 = Note(
                    id = generatedNoteId.value.toInt(),
                    title = title.value,
                    timeModified = System.currentTimeMillis(),
                    timeStamp = System.currentTimeMillis(),
                    notebook = notebookState.value,
                    listOfCheckedNotes = mutableListConverted,
                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                    color = 0,
                    tags = listOfSelectedTags.toCollection(ArrayList())
                )
                viewModel.updateNote(note1)
            }
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
                    listOfCheckedNotes,
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
                            notebook = notebookState.value,
                            listOfCheckedNotes = mutableListConverted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            color = backgroundColor.value.toArgb(),
                            tags = listOfSelectedTags.toCollection(ArrayList())
                        )
                        viewModel.updateNote(note1)
                    } else {
                        var note1 = Note(
                            id = generatedNoteId.value.toInt(),
                            title = title.value,
                            timeModified = System.currentTimeMillis(),
                            timeStamp = System.currentTimeMillis(),
                            notebook = notebookState.value,
                            listOfCheckedNotes = mutableListConverted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            color = 0,
                            tags = listOfSelectedTags.toCollection(ArrayList())
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
                            listOfCheckedNotes,
                            mutableListConverted
                        )
                        if (title.value.isNotEmpty() || (mutableListConverted.size != 1 || mutableListConverted[0].isNotEmpty())) {
                            if (backgroundColor.value != backgroundColor1) {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebookState.value,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = backgroundColor.value.toArgb(),
                                    tags = listOfSelectedTags.toCollection(ArrayList())
                                )
                                viewModel.updateNote(note)
                            } else {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebookState.value,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = 0,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
                                )
                                viewModel.updateNote(note)
                            }
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
                            imageVector = Icons.Filled.Palette,
                            contentDescription = "Background Color",
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
                            listOfCheckedNotes,
                            mutableListConverted
                        )

                        if (title.value.isNotEmpty() || (mutableListConverted.size != 1 || mutableListConverted[0].isNotEmpty())) {
                            if (backgroundColor.value != backgroundColor1) {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebookState.value,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = backgroundColor.value.toArgb(),
                                    tags = listOfSelectedTags.toCollection(ArrayList())
                                )
                                viewModel.updateNote(note)
                            } else {
                                val note = Note(
                                    id = generatedNoteId.value.toInt(),
                                    title = title.value,
                                    notebook = notebookState.value,
                                    listOfCheckedNotes = mutableListConverted,
                                    listOfCheckedBoxes = mutableListOfCheckBoxes,
                                    timeStamp = System.currentTimeMillis(),
                                    timeModified = System.currentTimeMillis(),
                                    color = 0,
                                    tags = listOfSelectedTags.toCollection(ArrayList())
                                )
                                viewModel.updateNote(note)
                            }
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
                AddNoteBottomSheet(showBottomSheet, backgroundColor, activity = activity)
            }
            CheckboxNote(
                viewModel,
                navController,
                notebookState,
                title,
                listOfCheckedNotes,
                mutableListOfCheckBoxes,
                count,
                mutableListConverted,
                backgroundColor,
                listOfSelectedTags
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


package com.pzbdownloaders.scribble.locked_notes_feature.presentation.components.ChecboxLockedNotesComponents

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.pzbdownloaders.scribble.common.domain.utils.Constant
import com.pzbdownloaders.scribble.common.presentation.MainActivity
import com.pzbdownloaders.scribble.common.presentation.MainActivityViewModel
import com.pzbdownloaders.scribble.common.presentation.components.AlertDialogBoxTrialEnded
import com.pzbdownloaders.scribble.main_screen.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureCheckBoxLockedNotes(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    activity: MainActivity
) {
    var context = LocalContext.current

    var mutableListOfCheckboxTexts = remember {
        mutableStateListOf<MutableState<String>>()
    }

    var mutableListConverted = remember {
        ArrayList<String>()
    }
    var mutableListOfCheckBoxes = remember { ArrayList<Boolean>() }

    var showTrialEndedDialogBox = remember {
        mutableStateOf(
            false
        )
    }

    LaunchedEffect(key1 = true) {
        mutableListOfCheckboxTexts.add(mutableStateOf(""))

    }

    LaunchedEffect(key1 = mutableListOfCheckboxTexts.size) {
        mutableListOfCheckBoxes.add(false)

    }

    if (showTrialEndedDialogBox.value) {
        AlertDialogBoxTrialEnded {
            showTrialEndedDialogBox.value = false
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
                    containerColor = MaterialTheme.colors.primary
                ),
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Undo",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        convertMutableStateIntoString(
                            mutableListOfCheckboxTexts,
                            mutableListConverted
                        )
                        val note = Note(
                            id = 0,
                            title = title.value,
                            listOfCheckedNotes = mutableListConverted,
                            listOfCheckedBoxes = mutableListOfCheckBoxes,
                            timeStamp = System.currentTimeMillis(),
                            locked = true
                        )
                        viewModel.insertNote(note)
                        Toast.makeText(activity, "Note has been saved", Toast.LENGTH_SHORT)
                            .show()
                        navController.popBackStack()

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
            CheckboxLockedNotes(
                viewModel,
                navController,
                notebookState,
                title,
                mutableListOfCheckboxTexts,
                mutableListOfCheckBoxes
            )
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
package com.pzbapps.squiggly.locked_notes_feature.presentation.components.ChecboxLockedNotesComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

@Composable
fun CheckboxLockedNotes(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    mutableListOfCheckBoxTexts: SnapshotStateList<MutableState<String>>,
    mutableListOfCheckBoxes: ArrayList<Boolean>,
    count: MutableState<Int>,
    backgroundColor: MutableState<Color>
) {

    var dialogOpen = remember {
        mutableStateOf(false)
    }

    val notebookText = remember {
        mutableStateOf("")
    }

    viewModel.getNoteBook()


    viewModel.getNoteBook()
    val notebooks: ArrayList<String> =
        arrayListOf("Add Notebook")

//    for (i in listOfNoteBooks?.indices ?: arrayListOf<GetNoteBook>().indices) {
//        notebooks.add(listOfNoteBooks!![i]?.notebook ?: GetNoteBook().notebook)
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor.value)
    ) {

        androidx.compose.material.TextField(
            value = title.value,
            onValueChange = { title.value = it },
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = backgroundColor.value,
                focusedIndicatorColor = backgroundColor.value,
                unfocusedIndicatorColor = backgroundColor.value,
                cursorColor = MaterialTheme.colors.onPrimary,
                textColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyBold, fontSize = 25.sp)
        )
        var firstCheckBoxCheck = remember {
            mutableStateOf(true)
        }
        if (firstCheckBoxCheck.value) {
            //CreateCheckBox(firstCheckBoxCheck)
        }

        LazyColumn {
            itemsIndexed(mutableListOfCheckBoxTexts) { indexed, item ->
                SingleRowCheckBoxLockedNotes(
                    text = item,
                    mutableListOfCheckBoxTexts,
                    mutableListOfCheckBoxes,
                    indexed,
                    count,
                    backgroundColor

                    )
            }
        }
    }
}

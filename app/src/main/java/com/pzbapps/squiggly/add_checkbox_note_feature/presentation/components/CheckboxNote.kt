package com.pzbapps.squiggly.add_checkbox_note_feature.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.add_note_feature.presentation.components.AlertDialogBox
import com.pzbapps.squiggly.add_note_feature.presentation.components.MainMenu
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CheckboxNote(
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    notebookState: MutableState<String>,
    title: MutableState<String>,
    mutableListOfCheckBoxTexts: SnapshotStateList<MutableState<String>>,
    mutableListOfCheckBoxes: ArrayList<Boolean>,
    count: MutableState<Int>,
    mutableListConverted: ArrayList<String>,
    backgroundColor: MutableState<Color>
) {

    var dialogOpen = remember {
        mutableStateOf(false)
    }

    val notebookText = remember {
        mutableStateOf("")
    }

    viewModel.getNoteBook()

    var notebook by remember {
        mutableStateOf("")
    }

    var isExpanded = remember {
        mutableStateOf(false)
    }

    var selectedNotebook = remember {
        mutableStateOf("")
    }

    val focusRequesters = remember { mutableStateListOf(FocusRequester()) }

    val imeVisible = WindowInsets.isImeVisible

    val lazyListState = rememberLazyListState()


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

        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(backgroundColor.value)
        ) {
            if (notebook.isNotEmpty()) {
                Text(
                    text = if (notebookState.value.isEmpty()) "Notebook:$notebook" else "Notebook selected: ${notebookState.value}",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyRegular,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            } else {
                Text(
                    text = if (notebookState.value.isEmpty()) "Add to Notebook" else "Notebook selected: ${notebookState.value}",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyRegular,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            androidx.compose.material.Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Arrow DropDown",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.clickable {
                    isExpanded.value = true
                })
            if (isExpanded.value) {
                MainMenu(
                    isExpanded = isExpanded,
                    notebookState,
                    viewModel,
                    dialogOpen,
                    notebookText
                )
            }
            //     CreateDropDownMenu("Color", notebookText, notebooks, viewModel, noteBookState)
        }

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

        Column(
            modifier = Modifier
                .background(backgroundColor.value)
                .padding(bottom = if (imeVisible) WindowInsets.ime.getBottom((LocalDensity.current)).dp else 0.dp)
        ) {

            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(mutableListOfCheckBoxTexts) { indexed, item ->
                    if (indexed >= focusRequesters.size) {
                        focusRequesters.add(FocusRequester())
                    }
                    val focusRequester = focusRequesters[indexed]
                    SingleRowCheckBox(
                        text = item,
                        mutableListOfCheckBoxTexts,
                        mutableListOfCheckBoxes,
                        indexed,
                        count,
                        focusRequester,
                        backgroundColor,
                        onDelete = {
                            try {
                                focusRequesters.removeAt(indexed)
                                mutableListOfCheckBoxTexts.removeAt(indexed)
                                mutableListConverted.removeAt(indexed)
                            } catch (exception: IndexOutOfBoundsException) {

                            }
                        }
                    )
                }
            }
        }
    }
    LaunchedEffect(count.value) {
        if (mutableListOfCheckBoxTexts.size > 1) {
            lazyListState.animateScrollToItem(mutableListOfCheckBoxTexts.size - 1)
            focusRequesters.lastOrNull()?.requestFocus()  // Move focus to the last added checkbox
        }
    }
    LaunchedEffect(focusRequesters, mutableListOfCheckBoxTexts) {
        if (mutableListOfCheckBoxTexts.isNotEmpty()) {
            // Delay focus request to ensure the UI is composed
            focusRequesters.firstOrNull()?.let { firstFocusRequester ->
                // Add a small delay to ensure everything is composed
                kotlinx.coroutines.delay(100)
                firstFocusRequester.requestFocus()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDropDownMenuCheckbox(
    label: String,
    notebookText: MutableState<String>,
    notebooks: ArrayList<String>,
    viewModel: MainActivityViewModel,
    noteBookState: MutableState<String>
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }

    val dialogOpen = remember {
        mutableStateOf(false)
    }

    viewModel.getNoteBook()

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = noteBookState.value,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            label = {
                Text(
                    text = label,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colors.onPrimary,
                unfocusedBorderColor = MaterialTheme.colors.onPrimary,
                focusedTextColor = MaterialTheme.colors.onPrimary,
                focusedContainerColor = MaterialTheme.colors.primary,
                unfocusedContainerColor = MaterialTheme.colors.primary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary
            ),
            shape = RoundedCornerShape(15.dp),
            maxLines = 1,

            )

        DropdownMenu(
            modifier = Modifier
                .background(MaterialTheme.colors.primaryVariant)
                .fillMaxWidth(),
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            viewModel.notebooks.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            color = MaterialTheme.colors.onPrimary
                        )
                    },
                    onClick = {
                        if (item == "Add Notebook") {
                            dialogOpen.value = true
                        }
                        noteBookState.value = item
                        isExpanded = !isExpanded
                    },
                    leadingIcon = {
                        if (item == "Add Notebook") {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                )
            }
        }
    }

    if (dialogOpen.value) {
        AlertDialogBox(
            notebookText = notebookText,
            viewModel = viewModel,
//            notebooksFromDB = notebooksFromDB,
//            notebooks = notebooks,
            onSaveNotebook = {
                //   notebooks.value.add(notebookText.value)
            },
            onDismiss = {
                dialogOpen.value = false
            }
        )

    }
}
//
//@Composable
//fun CreateCheckBox(previousCreateBox: MutableState<Boolean>) {
//    var checked by remember {
//        mutableStateOf(false)
//    }
//
//    var text by remember {
//        mutableStateOf("")
//    }
//    var createBox = remember {
//        mutableStateOf(false)
//    }
//
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        androidx.compose.material.Checkbox(
//            checked = checked,
//            onCheckedChange = { checked = it })
//        TextField(
//            value = text,
//            onValueChange = { text = it },
//            colors = androidx.compose.material3.TextFieldDefaults.colors(
//                focusedContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
//                unfocusedContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
//                focusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
//                unfocusedTextColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
//                unfocusedIndicatorColor = androidx.compose.material.MaterialTheme.colors.primary,
//                focusedIndicatorColor = androidx.compose.material.MaterialTheme.colors.primary,
//                cursorColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
//            ),
//            textStyle = LocalTextStyle.current.copy(
//                fontFamily = FontFamily.fontFamilyLight,
//                fontSize = 16.sp
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { createBox.value = true }
//            ),
//            keyboardOptions = KeyboardOptions(
//                imeAction = ImeAction.Done
//            ),
//            trailingIcon = {
//                IconButton(onClick = { previousCreateBox.value = false }) {
//                    Icon(
//                        imageVector = Icons.Default.Clear,
//                        contentDescription = "Cancel",
//                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary
//                    )
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(end = 10.dp)
//        )
//    }
//    if (createBox.value)
//        CreateCheckBox(createBox)
//}

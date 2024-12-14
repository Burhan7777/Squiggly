package com.pzbapps.squiggly.add_note_feature.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.data.Model.Tag
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.AddTag
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.SelectTags
import kotlinx.coroutines.delay


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun NoteContent(
    title: MutableState<String>,
    content: MutableState<String>,
    viewModel: MainActivityViewModel,
    noteBookState: MutableState<String>,
    showCircularProgress: MutableState<Boolean>,
    textFieldValue: MutableState<TextFieldValue>,
    boldText: MutableState<Boolean>,
    richStateText: RichTextState,
    hideFormattingTextBar: MutableState<Boolean>,
    showSavedText: MutableState<Boolean>,
    backgroundColor: MutableState<Color>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>,
    listOFSelectedTags: SnapshotStateList<String>,
    showTags: MutableState<Boolean>
//    notebook: MutableState<ArrayList<String>>,
//    notebookFromDB: MutableState<ArrayList<NoteBook>>
) {

    var dialogOpen = remember {
        mutableStateOf(false)
    }

//    var notebookText = remember {
//        mutableStateOf("")
//    }
//

    val showSelectTagAlertBox = remember { mutableStateOf(false) }
    val showAddTagAlertBox = remember { mutableStateOf(false) }

    var filteredList = remember { mutableStateListOf<Tag>() }

    var notebook by remember {
        mutableStateOf("")
    }

    var isExpanded = remember {
        mutableStateOf(false)
    }

    var selectedNotebook = remember {
        mutableStateOf("")
    }

    var notebookText = remember {
        mutableStateOf("")
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible

    var focusRequester = remember { FocusRequester() }
    //val titleFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(key1 = showSavedText.value) {
        delay(1000)
        showSavedText.value = false
    }


//    val listOfNoteBooks = viewModel.getNoteBooks.observeAsState().value
//    Log.i("notebooks", listOfNoteBooks?.size.toString())

    Box(modifier = Modifier.fillMaxWidth()) {
        if (showSelectTagAlertBox.value) {
            SelectTags(viewModel.tags, listOFSelectedTags, viewModel, showAddTagAlertBox) {
                showSelectTagAlertBox.value = false
            }
        }

        if (showAddTagAlertBox.value) {
            AddTag(viewModel) {
                showAddTagAlertBox.value = false
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            if (notebook.isNotEmpty()) {
                Text(
                    text = if (noteBookState.value.isEmpty()) "Notebook:$notebook" else "Notebook selected: ${noteBookState.value}",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyRegular,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            } else {
                Text(
                    text = if (noteBookState.value.isEmpty()) "Add to Notebook" else "Notebook selected: ${noteBookState.value}",
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
                modifier = Modifier.clickable {
                    isExpanded.value = true
                })
            if (isExpanded.value) {
                MainMenu(
                    isExpanded = isExpanded,
                    noteBookState,
                    viewModel,
                    dialogOpen,
                    notebookText
                )
            }
            //     CreateDropDownMenu("Color", notebookText, notebooks, viewModel, noteBookState)
        }
//        if (showSavedText.value) {
//            Text(
//                text = "Saved",
//                color = MaterialTheme.colors.onPrimary,
//                fontFamily = FontFamily.fontFamilyRegular,
//                fontStyle = FontStyle.Italic,
//                modifier = Modifier
//                    .align(Alignment.CenterEnd)
//                    .padding(end = 10.dp)
//            )
//        }
    }


    TextField(
        value = title.value,
        onValueChange = { title.value = it },
        placeholder = {
            Text(
                text = "Title",
                fontSize = 20.sp,
                fontFamily = FontFamily.fontFamilyBold,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.alpha(0.5f)
            )
        },
        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
            backgroundColor = backgroundColor.value,
            focusedIndicatorColor = backgroundColor.value,
            cursorColor = MaterialTheme.colors.onPrimary
        ),
        textStyle = TextStyle(fontFamily = fontFamily.value, fontSize = 20.sp),
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                hideFormattingTextBar.value = it.isFocused
            }
    )

    val scrollState = rememberScrollState()
    val imePadding =
        if (imeVisible) WindowInsets.ime.getBottom(LocalDensity.current).dp + 50.dp else 0.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = imePadding)
    ) {
        // RichTextEditor and Tags Section with dynamic spacing
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            var richEditorMaxHeight: Dp = 0.dp
            if (!showTags.value) {
                richEditorMaxHeight = maxHeight - 80.dp // Leave space for Tags
            } else {
                richEditorMaxHeight = maxHeight - 20.dp
            }
            // Scrollable RichTextEditor
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .heightIn(max = richEditorMaxHeight) // Ensure space for Tags initially
            ) {
                RichTextEditor(
                    state = richStateText,
                    colors = RichTextEditorDefaults.richTextEditorColors(
                        containerColor = backgroundColor.value,
                        cursorColor = MaterialTheme.colors.onPrimary,
                        textColor = MaterialTheme.colors.onPrimary,
                        unfocusedIndicatorColor = backgroundColor.value,
                        focusedIndicatorColor = backgroundColor.value,
                        selectionColors = TextSelectionColors(
                            handleColor = MaterialTheme.colors.onPrimary,
                            backgroundColor = Color.Gray
                        )
                    ),
                    placeholder = {
                        Text(
                            text = "Note",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.fontFamilyBold,
                            color = MaterialTheme.colors.onPrimary,
                            modifier = Modifier.alpha(0.5f)
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = fontFamily.value,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Automatic scrolling when text changes
                LaunchedEffect(richStateText.annotatedString.text) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
            }
        }

        // Spacer to dynamically adjust the layout
//        Spacer(
//            modifier = Modifier
//                .weight(if (scrollState.maxValue > 0) 0f else 1f) // Take up remaining space if not scrollable
//        )

        // Tags Section - Initially below RichTextEditor, moves to bottom when RichTextEditor scrolls
        if (!showTags.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    "Tags",
                    color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(start = 10.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOFSelectedTags) { item ->
                        androidx.compose.material.Chip(
                            onClick = {},
                            modifier = Modifier.padding(5.dp),
                            colors = ChipDefaults.chipColors(
                                backgroundColor = MaterialTheme.colors.onPrimary,
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Remove from list",
                                    tint = MaterialTheme.colors.onSecondary,
                                    modifier = Modifier.clickable {
                                        listOFSelectedTags.remove(item)
                                    }
                                )
                            }
                        ) {
                            Text(
                                item,
                                color = MaterialTheme.colors.onSecondary,
                                fontFamily = FontFamily.fontFamilyRegular
                            )
                        }
                    }
                    item {
                        androidx.compose.material.Chip(
                            modifier = Modifier.padding(5.dp),
                            colors = ChipDefaults.chipColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                contentColor = MaterialTheme.colors.onPrimary
                            ),
                            onClick = {
                                showSelectTagAlertBox.value = true
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Tag",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        ) {
                            Text(
                                text = "Add Tag",
                                color = MaterialTheme.colors.onPrimary,
                                fontFamily = FontFamily.fontFamilyRegular
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainMenu(
    isExpanded: MutableState<Boolean>,
    selectedNotebook: MutableState<String>,
    viewModel: MainActivityViewModel,
    dialogOpen: MutableState<Boolean>,
    notebookText: MutableState<String>
) {
    DropdownMenu(
        offset = DpOffset.Zero,
        modifier = Modifier
            .width(200.dp)
            .background(MaterialTheme.colors.primaryVariant),
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false }
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
                    } else {
                        selectedNotebook.value = item
                        isExpanded.value = false
                    }
                },
            )
        }
    }

    if (dialogOpen.value) {
        com.pzbapps.squiggly.edit_note_feature.presentation.components.AlertDialogBox(
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

@Composable
fun AlertDialogBox(
    notebookText: MutableState<String>,
    onSaveNotebook: () -> Unit,
    viewModel: MainActivityViewModel,
    onDismiss: () -> Unit,
//    notebooksFromDB: MutableState<ArrayList<NoteBook>>,
//    notebooks: MutableState<ArrayList<String>>,


) {

    val context = LocalContext.current
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = MaterialTheme.colors.primaryVariant,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            OutlinedTextField(
                value = notebookText.value,
                onValueChange = { notebookText.value = it },
                label = {
                    Text(
                        text = "Add notebook",
                        color = MaterialTheme.colors.onPrimary,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.fontFamilyRegular
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colors.onPrimary,
                    unfocusedTextColor = MaterialTheme.colors.onPrimary,
                    focusedTextColor = MaterialTheme.colors.onPrimary,
                    unfocusedBorderColor = MaterialTheme.colors.onPrimary,
                    cursorColor = MaterialTheme.colors.onPrimary
                ),
                textStyle = TextStyle.Default.copy(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.fontFamilyRegular
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                )
            )
        },
        confirmButton = {
            androidx.compose.material.Button(
                onClick = {
                    val noteBook = NoteBook(0, notebookText.value)
                    viewModel.addNoteBook(noteBook)
                    viewModel.notebooks.add(notebookText.value)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                androidx.compose.material.Text(
                    text = "Add",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        },
        dismissButton = {
            androidx.compose.material.OutlinedButton(
                onClick = { onDismiss() },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
            ) {
                androidx.compose.material.Text(
                    text = "Cancel",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        },
    )
}





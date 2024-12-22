package com.pzbapps.squiggly.notebook_main_screen.presentation.components.addNoteInNotebookComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.AddTag
import com.pzbapps.squiggly.common.presentation.alertboxes.addTagAlertBoxes.SelectTags


@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun NoteContentNoteInNotebook(
    title: MutableState<String>,
    content: MutableState<String>,
    viewModel: MainActivityViewModel,
    noteBookState: MutableState<String>,
    showCircularProgress: MutableState<Boolean>,
    textFieldValue: MutableState<TextFieldValue>,
    boldText: MutableState<Boolean>,
    richStateText: RichTextState,
    hideFormattingTextBar: MutableState<Boolean>,
    backgroundColor: MutableState<Color>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>,
    listOFSelectedTags: SnapshotStateList<String>,
    showTags: MutableState<Boolean>,
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

    val focusRequester = remember { FocusRequester() }

    val showSelectTagAlertBox = remember { mutableStateOf(false) }
    val showAddTagAlertBox = remember { mutableStateOf(false) }

    val showAddingTagDialogBox = remember { mutableStateOf(false) } // THIS DIALOG BOX IS SHOWN
    // WHEN WE PRESS SAVE BUTTON IN ADD TAG DIALOG BOX
    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

//    val listOfNoteBooks = viewModel.getNoteBooks.observeAsState().value
//    Log.i("notebooks", listOfNoteBooks?.size.toString())


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
            unfocusedIndicatorColor = backgroundColor.value,
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

        if (showSelectTagAlertBox.value) {
            SelectTags(viewModel.tags, listOFSelectedTags, viewModel, showAddTagAlertBox) {
                showSelectTagAlertBox.value = false
            }
        }

        if (showAddTagAlertBox.value) {
            AddTag(viewModel,showAddingTagDialogBox,listOFSelectedTags,showSelectTagAlertBox) {
                showAddTagAlertBox.value = false
            }
        }
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





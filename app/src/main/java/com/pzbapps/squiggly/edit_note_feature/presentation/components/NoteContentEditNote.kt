package com.pzbapps.squiggly.edit_note_feature.presentation.components

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.pzbapps.squiggly.common.data.Model.NoteBook
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.reminder_feature.cancelReminder
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
fun NoteContent(
    selectedNotebook: MutableState<String>,
    isExpanded: MutableState<Boolean>,
    viewModel: MainActivityViewModel,
    title: String,
    content: MutableState<String>,
    noteBook: String,
    listOfNotes: SnapshotStateList<MutableState<String>>,
    listOfCheckboxes: MutableState<ArrayList<Boolean>>,
    listOfBulletPointNotes: SnapshotStateList<MutableState<String>>,
    activity: MainActivity,
    richStateText: MutableState<RichTextState>,
    count: MutableState<Int>,
    converted: ArrayList<String>,
    countBullet: MutableState<Int>,
    convertedBulletPoints: ArrayList<String>,
    hideFormattingTextBar: MutableState<Boolean>,
    onChangeTitle: (String) -> Unit,
    onChangeContent: (String) -> Unit,
    screen: String,
    note: MutableState<Note>,
    showMenu: MutableState<Boolean>,
    notificationLauncher: ManagedActivityResultLauncher<String, Boolean>,
    time: MutableLongState,
    systemTime: MutableLongState
) {

    ///viewModel.getNoteBook()
    //val listOfNoteBooks = viewModel.getNoteBooks.observeAsState().value

    var dialogOpen = remember {
        mutableStateOf(false)
    }

    var notebookText = remember {
        mutableStateOf("")
    }
    var formattedTime = remember { mutableStateOf("") }
    LaunchedEffect(true) {
        systemTime.longValue = System.currentTimeMillis()
        time.longValue = note.value.reminder
        formattedTime.value = formatDateTimeFromMillis(time.longValue)
    }

//    LaunchedEffect(key1 = content) {
//        richStateText.value.setHtml(content)
//        println("RICHTEXT:${richStateText.value.annotatedString.text}")
//    }

    LaunchedEffect(key1 = content) {
        // Set the content only once when the screen opens, or if content changes externally
        if (richStateText.value.annotatedString.text != content.value) {
            richStateText.value.setHtml(content.value)
        }
    }

// Update 'content' as user types in the editor
    LaunchedEffect(key1 = richStateText.value.annotatedString) {
        // Update content dynamically with the current text
        content.value = richStateText.value.annotatedString.text
        println("RICHTEXT: ${richStateText.value.annotatedString.text}")
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val imeVisible = WindowInsets.isImeVisible

    var focusRequester = remember { FocusRequester() }


    // richTextState1.setText(richTextState1.annotatedString.text)
    // println(richTextState1.annotatedString.text)


//    var notebooks: ArrayList<String> = ArrayList()
//
//    for (i in listOfNoteBooks?.indices ?: arrayListOf<GetNoteBook>().indices) {
//        notebooks.add(listOfNoteBooks!![i]?.notebook ?: GetNoteBook().notebook)
//    }


    if (screen != Constant.LOCKED_NOTE && screen != Constant.ARCHIVE) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (noteBook.isNotEmpty()) {
                Text(
                    text = if (selectedNotebook.value.isEmpty()) "Notebook:$noteBook" else "New Notebook selected: ${selectedNotebook.value}",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyRegular,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            } else {
                Text(
                    text = if (selectedNotebook.value.isEmpty()) "Add to Notebook" else "New Notebook selected: ${selectedNotebook.value}",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyRegular,
                    fontStyle = FontStyle.Italic,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 15.dp)
                )
            }

            /*     TextField(
                modifier = Modifier.wrapContentWidth(),
                 value = "Notebook:$noteBook",
                 onValueChange = { },
                 placeholder = {
                     Text(
                         text = "Notebook",
                         fontSize = 15.sp,
                         fontFamily = FontFamily.fontFamilyRegular,
                         color = MaterialTheme.colors.onPrimary,
                         modifier = Modifier.alpha(0.5f)
                     )
                 },
                 colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
                     backgroundColor = MaterialTheme.colors.primary,
                     focusedIndicatorColor = MaterialTheme.colors.primary,
                     unfocusedIndicatorColor = MaterialTheme.colors.primary,
                     cursorColor = MaterialTheme.colors.onPrimary
                 ),
                 textStyle = TextStyle(
                     fontFamily = FontFamily.fontFamilyRegular,
                     fontSize = 15.sp,
                     fontStyle = FontStyle.Italic
                 ),
                 readOnly = true
             )*/
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Arrow DropDown",
                modifier = Modifier.clickable {
                    isExpanded.value = true
                })
            if (isExpanded.value) {
                Menu(isExpanded = isExpanded, selectedNotebook, viewModel, dialogOpen, notebookText)
            }
        }
    }
    if (listOfNotes.size == 0 && listOfBulletPointNotes.size == 0) {
        TextField(
            value = title,
            onValueChange = { onChangeTitle(it) },
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
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyBold, fontSize = 20.sp),
            modifier = Modifier
                .focusRequester(focusRequester)
                .onFocusChanged {
                    hideFormattingTextBar.value = it.isFocused
                }
        )

        Column(
            modifier = Modifier
                .padding(bottom = if (imeVisible) WindowInsets.ime.getBottom((LocalDensity.current)).dp + 100.dp else 0.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
            }

            RichTextEditor(
                state = richStateText.value,
                colors = RichTextEditorDefaults.richTextEditorColors(
                    containerColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.onPrimary,
                    textColor = MaterialTheme.colors.onPrimary,
                    focusedIndicatorColor = MaterialTheme.colors.primary,
                    unfocusedIndicatorColor = MaterialTheme.colors.primary,
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
                textStyle = TextStyle(fontFamily = FontFamily.fontFamilyRegular, fontSize = 18.sp)
            )
            if (systemTime.longValue < time.longValue) {
                Card(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .padding(30.dp)
                        .clickable {
                            addReminder(
                                activity,
                                note,
                                title,
                                showMenu,
                                notificationLauncher,
                                viewModel,
                                time,
                                systemTime
                            )
                        },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colors.primaryVariant,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Alarm,
                            contentDescription = "Alarm",
                            tint = MaterialTheme.colors.onPrimary
                        )
                        Spacer(modifier = Modifier.width(3.dp))

                        Text(if (viewModel.timeInString.value == "") formattedTime.value else viewModel.timeInString.value)
                        Spacer(modifier = Modifier.width(3.dp))
                        IconButton(onClick = {
                            cancelReminder(activity, note.value.id)
                            val noteUpdate = note.value.copy(reminder = 0)
                            viewModel.updateNote(noteUpdate)
                            time.value = 0
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Cancel the alarm",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            }
        }
    } else if (listOfNotes.size > 0 && listOfBulletPointNotes.size == 0) {
        androidx.compose.material3.TextField(
            value = title,
            onValueChange = { onChangeTitle(it) },
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary,
                focusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyBold, fontSize = 25.sp)
        )
        // println(listOfNotes.size)
        //println(listOfCheckboxes.size)
        val focusRequesters = remember { mutableStateListOf(FocusRequester()) }

        val imeVisible = WindowInsets.isImeVisible

        var isNewCheckboxAdded = remember { mutableStateOf(false) } //

        val lazyListState = rememberLazyListState()
        Column(
            modifier = Modifier
                .padding(bottom = if (imeVisible) WindowInsets.ime.getBottom((LocalDensity.current)).dp else 0.dp)
        ) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listOfNotes) { index, note ->
                    if (index >= focusRequesters.size) {
                        focusRequesters.add(FocusRequester())
                    }
                    val focusRequester = focusRequesters[index]
                    SingleRowCheckBoxNotes(note = note,
                        index,
                        listOfCheckboxes,
                        listOfNotes,
                        count,
                        focusRequester,
                        focusRequesters,
                        isNewCheckboxAdded,
                        onDelete = {
                            try {
                                focusRequesters.removeAt(index)
                                convertedBulletPoints.removeAt(index)
                            } catch (exception: IndexOutOfBoundsException) {

                            }
                        })

                }
            }
        }
        LaunchedEffect(count.value) {
            if (listOfNotes.size > 1 && listOfNotes.last().value.isEmpty()) {
                lazyListState.animateScrollToItem(listOfNotes.size - 1)
                focusRequesters.lastOrNull()
                    ?.requestFocus()
                isNewCheckboxAdded.value = false// Move focus to the last added checkbox
            }
        }
//        LaunchedEffect(focusRequesters, listOfNotes) {
//            if (listOfNotes.isNotEmpty()) {
//                // Delay focus request to ensure the UI is composed
//                focusRequesters.firstOrNull()?.let { firstFocusRequester ->
//                    // Add a small delay to ensure everything is composed
//                   // kotlinx.coroutines.delay(100)
//                    firstFocusRequester.requestFocus()
//                }
//            }
//        }
    } else if (listOfBulletPointNotes.size > 0) {
        val focusRequesters = remember { mutableStateListOf(FocusRequester()) }

        val imeVisible = WindowInsets.isImeVisible

        val lazyListState = rememberLazyListState()
        androidx.compose.material3.TextField(
            value = title,
            onValueChange = { onChangeTitle(it) },
            placeholder = {
                Text(
                    text = "Title",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary,
                    modifier = Modifier.alpha(0.5f)
                )
            },
            colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.primary,
                unfocusedIndicatorColor = MaterialTheme.colors.primary,
                cursorColor = MaterialTheme.colors.onPrimary,
                focusedTextColor = MaterialTheme.colors.onPrimary,
                unfocusedTextColor = MaterialTheme.colors.onPrimary
            ),
            textStyle = TextStyle(fontFamily = FontFamily.fontFamilyBold, fontSize = 25.sp)
        )

        Column(
            modifier = Modifier
                .padding(bottom = if (imeVisible) WindowInsets.ime.getBottom((LocalDensity.current)).dp else 0.dp)
        ) {
            LazyColumn(state = lazyListState, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(listOfBulletPointNotes) { index, note ->
                    if (index >= focusRequesters.size) {
                        focusRequesters.add(FocusRequester())
                    }
                    val focusRequester = focusRequesters[index]
                    SingleRoaBulletPointNotes(
                        note = note,
                        index,
                        listOfBulletPointNotes,
                        countBullet,
                        focusRequester,
                        focusRequesters,
                        onDelete = {
                            try {
                                focusRequesters.removeAt(index)
                                converted.removeAt(index)
                            } catch (exception: IndexOutOfBoundsException) {

                            }
                        }
                    )

                }
            }
        }
        LaunchedEffect(countBullet.value) {
            if (listOfBulletPointNotes.size > 1 && listOfBulletPointNotes.last().value.isEmpty()) {
                lazyListState.animateScrollToItem(listOfBulletPointNotes.size - 1)
                focusRequesters.lastOrNull()
                    ?.requestFocus()  // Move focus to the last added checkbox
            }
        }
//        LaunchedEffect(focusRequesters, listOfBulletPointNotes) {
//            if (listOfBulletPointNotes.isNotEmpty()) {
//                // Delay focus request to ensure the UI is composed
//                focusRequesters.firstOrNull()?.let { firstFocusRequester ->
//                    // Add a small delay to ensure everything is composed
//                    kotlinx.coroutines.delay(100)
//                    firstFocusRequester.requestFocus()
//                }
//            }
//        }
    }

}

@Composable
fun Menu(
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

fun formatDateTimeFromMillis(millis: Long): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy, HH:mm", Locale.getDefault())
    val date = Date(millis)
    return dateFormat.format(date)
}


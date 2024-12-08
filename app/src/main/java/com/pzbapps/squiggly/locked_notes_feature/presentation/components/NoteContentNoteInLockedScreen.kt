package com.pzbapps.squiggly.locked_notes_feature.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun NoteContentNoteInLockedScreen(
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
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>
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
    var focusRequester = remember { FocusRequester() }

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
            )
        )
    }
}





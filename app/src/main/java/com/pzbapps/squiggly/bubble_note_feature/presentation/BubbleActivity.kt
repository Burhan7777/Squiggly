package com.pzbapps.squiggly.bubble_note_feature.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pzbapps.squiggly.R
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.appwidget.CheckBox
import androidx.lifecycle.ViewModelProvider
import com.pzbapps.squiggly.bubble_note_feature.screens.BulletPointScreen
import com.pzbapps.squiggly.bubble_note_feature.screens.CheckBoxScreen
import com.pzbapps.squiggly.bubble_note_feature.screens.SingleRowCheckBox
import com.pzbapps.squiggly.common.presentation.BubbleNoteViewModel
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.ui.theme.BubbleTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

enum class selectTypeOfNote {
    NOTES,
    CHECKBOX,
    BULLETPOINT
}


@AndroidEntryPoint
class BubbleActivity : AppCompatActivity() {
    private lateinit var bubbleViewModel: BubbleNoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bubbleViewModel = ViewModelProvider(this)[BubbleNoteViewModel::class.java]

        val window = window
        window.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(), // 85% of screen width
            (resources.displayMetrics.heightPixels * 0.6).toInt(), // 60% of screen height
        )
        window.setBackgroundDrawableResource(android.R.color.transparent) // Make background transparent


        setContent {
            BubbleTheme {
                QuickNoteScreen(bubbleViewModel) { finish() }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun QuickNoteScreen(viewModel: BubbleNoteViewModel, onClose: () -> Unit) {
        var title = remember { mutableStateOf("") }
        var content = remember { mutableStateOf("") }
        var typeOfNote = remember { mutableStateOf(selectTypeOfNote.NOTES) }
        var listOfCheckboxTexts = RememberSaveableSnapshotStateList()
        var mutableListOfCheckBoxes = rememberSaveable { ArrayList<Boolean>() }




        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable { onClose() }, contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = typeOfNote.value == selectTypeOfNote.NOTES,
                        onClick = { typeOfNote.value = selectTypeOfNote.NOTES })
                    Icon(
                        imageVector = Icons.Default.Note,
                        contentDescription = "Select checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )
                    RadioButton(
                        selected = typeOfNote.value == selectTypeOfNote.CHECKBOX,
                        onClick = { typeOfNote.value = selectTypeOfNote.CHECKBOX })
                    Icon(
                        imageVector = Icons.Default.CheckBox,
                        contentDescription = "Select checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )
                    RadioButton(
                        selected = typeOfNote.value == selectTypeOfNote.BULLETPOINT,
                        onClick = { typeOfNote.value = selectTypeOfNote.BULLETPOINT })
                    Icon(
                        imageVector = Icons.Default.FormatListBulleted,
                        contentDescription = "Select checkbox",
                        tint = MaterialTheme.colors.onPrimary
                    )

                    Spacer(modifier = Modifier.weight(0.5f))
                    IconButton(
                        modifier = Modifier
                            .padding(end = 15.dp)
                            .size(25.dp)
                            .background(
                                color = MaterialTheme.colors.onPrimary,
                                shape = CircleShape
                            ),
                        onClick = {
                            onClose()
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Close the activity",
                            tint = MaterialTheme.colors.onSecondary,

                            )
                    }
                }
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colors.primary,
                        contentColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        when (typeOfNote.value) {
                            selectTypeOfNote.NOTES -> {
                                NotesScreen(title, content, viewModel, onClose)
                            }

                            selectTypeOfNote.CHECKBOX -> {
                                CheckBoxScreen(
                                    title,
                                    listOfCheckboxTexts,
                                    mutableListOfCheckBoxes,
                                    viewModel,
                                    onClose
                                )
                            }

                            selectTypeOfNote.BULLETPOINT -> {
                                    BulletPointScreen()
                            }
                        }
                    }
                }
            }
        }
    }

}

private fun saveNote(title: String, content: String, viewModel: BubbleNoteViewModel) {
    val note = Note(title = title, content = content, timeStamp = System.currentTimeMillis())
    viewModel.insertNote(note)
}

fun convertMutableStateIntoString(
    mutableList: SnapshotStateList<MutableState<String>>,
    mutableListConverted: ArrayList<String>
): ArrayList<String> {
    for (i in mutableList) {
        if (!mutableListConverted.contains(i.value)) {
            mutableListConverted.add(i.value)
        }
    }
    return mutableListConverted
}



@Composable
fun NotesScreen(
    title: MutableState<String>,
    content: MutableState<String>,
    viewModel: BubbleNoteViewModel,
    onClose: () -> Unit
) {

    androidx.compose.material.TextField(
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
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        textStyle = TextStyle(
            fontFamily = FontFamily.fontFamilyRegular,
            fontSize = 20.sp
        ),
//                        modifier = Modifier
//                            .focusRequester(focusRequester)
//                            .onFocusChanged {
//                                hideFormattingTextBar.value = it.isFocused
//                            }
    )
    Spacer(modifier = Modifier.height(8.dp))
    androidx.compose.material.TextField(
        value = content.value,
        onValueChange = { content.value = it },
        placeholder = {
            Text(
                text = "Note",
                fontSize = 20.sp,
                fontFamily = FontFamily.fontFamilyBold,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier.alpha(0.5f)
            )
        },
        colors = androidx.compose.material.TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.primary,
            cursorColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
        textStyle = TextStyle(
            fontFamily = FontFamily.fontFamilyRegular,
            fontSize = 20.sp
        ),
        modifier = Modifier.fillMaxHeight(0.7f)
//                        modifier = Modifier
//                            .focusRequester(focusRequester)
//                            .onFocusChanged {
//                                hideFormattingTextBar.value = it.isFocused
//                            }
    )
    Button(onClick = {
        saveNote(title.value, content.value, viewModel)
        onClose()
    }) {
        Text("Save")
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






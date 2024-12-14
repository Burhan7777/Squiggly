package com.pzbapps.squiggly.locked_notes_feature.presentation.components

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatAlignCenter
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatAlignLeft
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Undo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mohamedrejeb.richeditor.model.RichTextState
import java.util.Stack

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomTextFormattingBarLockedNote(
    showFontSize: MutableState<Boolean>,
    fontSize: MutableState<String>,
    richTextState: MutableState<RichTextState>,
    isBoldActivated: MutableState<Boolean>,
    isUnderlineActivated: MutableState<Boolean>,
    isItalicActivated: MutableState<Boolean>,
    isOrderedListActivated: MutableState<Boolean>,
    isUnOrderedListActivated: MutableState<Boolean>,
    undoStack: Stack<String>,
    redoStack: Stack<String>,
    currentContent: MutableState<String>,
    showBottomSheet: MutableState<Boolean>,
    showTextColorBottomSheet: MutableState<Boolean>,
    showFontBottomSheet: MutableState<Boolean>
) {
    Column(modifier = Modifier.imePadding()) {
        if (showFontSize.value) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.width(200.dp)
            ) {
                androidx.compose.material3.IconButton(onClick = {
                    if (fontSize.value > "0") {
                        fontSize.value = (fontSize.value.toInt() - 1).toString()
                        richTextState.value.toggleSpanStyle(
                            SpanStyle(fontSize = fontSize.value.toInt().sp)
                        )
                    }
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlusOne,
                        contentDescription = "Decrease font size",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                TextField(
                    value = fontSize.value,
                    onValueChange = { fontSize.value = it },
                    modifier = Modifier
                        .width(50.dp)
                        .background(MaterialTheme.colors.primaryVariant),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                        unfocusedIndicatorColor = MaterialTheme.colors.primaryVariant,
                        textColor = MaterialTheme.colors.onPrimary
                    ),
                    enabled = false
                )
                androidx.compose.material3.IconButton(onClick = {
                    fontSize.value = (fontSize.value.toInt() + 1).toString()
                    richTextState.value.toggleSpanStyle(
                        SpanStyle(fontSize = fontSize.value.toInt().sp)
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.PlusOne,
                        contentDescription = "increase  font size",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
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
                if (undoStack.isNotEmpty()) {
                    redoStack.push(currentContent.value) // Save the current state to redo stack
                    currentContent.value = undoStack.pop() // Get the last content from undo stack
                    richTextState.value.setHtml(currentContent.value) // Set editor content
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Undo,
                    contentDescription = "Undo",
                    tint = MaterialTheme.colors.onPrimary
                )
            }

            IconButton(onClick = {
                if (redoStack.isNotEmpty()) {
                    undoStack.push(currentContent.value) // Save the current state to undo stack
                    currentContent.value = redoStack.pop() // Get the last content from redo stack
                    richTextState.value.setHtml(currentContent.value) // Set editor content
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Redo,
                    contentDescription = "Undo",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(
                        CircleShape
                    )
                    .background(MaterialTheme.colors.onSecondary)
                    .border(
                        width = 10.dp, // Border width
                        color = MaterialTheme.colors.secondary, // Border color
                        shape = CircleShape // Ensure the border follows a circular shape
                    )
                    .clickable {
                        showTextColorBottomSheet.value = true
                    }

            )

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(
                onClick = {
                    richTextState.value.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                    isBoldActivated.value = !isBoldActivated.value
                },
                modifier = Modifier
                    .clip(
                        if (isBoldActivated.value) CircleShape else MaterialTheme.shapes.medium.copy(
                            all = CornerSize(0.dp)
                        )
                    )
                    .background(if (isBoldActivated.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            )
            {
                Icon(
                    imageVector = Icons.Filled.FormatBold,
                    contentDescription = "Make text bold",
                    tint = if (isBoldActivated.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(
                onClick = {
                    richTextState.value.toggleSpanStyle(
                        SpanStyle(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    isUnderlineActivated.value = !isUnderlineActivated.value
                },
                modifier = Modifier
                    .clip(
                        if (isUnderlineActivated.value) CircleShape else MaterialTheme.shapes.medium.copy(
                            all = CornerSize(0.dp)
                        )
                    )
                    .background(if (isUnderlineActivated.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 0.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatUnderlined,
                    contentDescription = "Make text underline",
                    tint = if (isUnderlineActivated.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(
                onClick = {
                    richTextState.value.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                    isItalicActivated.value = !isItalicActivated.value
                },
                modifier = Modifier
                    .clip(
                        if (isItalicActivated.value) CircleShape else MaterialTheme.shapes.medium.copy(
                            all = CornerSize(0.dp)
                        )
                    )
                    .background(if (isItalicActivated.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatItalic,
                    contentDescription = "Make text Italic",
                    tint = if (isItalicActivated.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(
                onClick = {

                    showFontBottomSheet.value = true
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.FontDownload,
                    contentDescription = "Change Font",
                )
            }
            IconButton(onClick = {
                showFontSize.value = !showFontSize.value

            }) {
                Icon(
                    imageVector = Icons.Filled.FormatSize,
                    contentDescription = "Change text format",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(
                onClick = {
                    richTextState.value.toggleOrderedList()
                    isOrderedListActivated.value = !isOrderedListActivated.value
                    isUnOrderedListActivated.value = false
                },
                modifier = Modifier
                    .clip(
                        if (isOrderedListActivated.value) CircleShape else MaterialTheme.shapes.medium.copy(
                            all = CornerSize(0.dp)
                        )
                    )
                    .background(if (isOrderedListActivated.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 0.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatListNumbered,
                    contentDescription = "Change text format",
                    tint = if (isOrderedListActivated.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(
                onClick = {
                    richTextState.value.toggleUnorderedList()
                    isUnOrderedListActivated.value = !isUnOrderedListActivated.value
                    isOrderedListActivated.value = false
                },
                modifier = Modifier
                    .clip(
                        if (isUnOrderedListActivated.value) CircleShape else MaterialTheme.shapes.medium.copy(
                            all = CornerSize(0.dp)
                        )
                    )
                    .background(if (isUnOrderedListActivated.value) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primaryVariant)
                    .border(
                        width = 0.dp,
                        color = MaterialTheme.colors.primaryVariant,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Filled.FormatListBulleted,
                    contentDescription = "Change text format",
                    tint = if (isUnOrderedListActivated.value) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(onClick = {
                richTextState.value.toggleParagraphStyle(
                    ParagraphStyle(
                        textAlign = TextAlign.Left
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.FormatAlignLeft,
                    contentDescription = "Change text format",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(onClick = {
                richTextState.value.toggleParagraphStyle(
                    ParagraphStyle(
                        textAlign = TextAlign.End
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.FormatAlignRight,
                    contentDescription = "Change text format",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(onClick = {
                richTextState.value.toggleParagraphStyle(
                    ParagraphStyle(
                        textAlign = TextAlign.Center
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.FormatAlignCenter,
                    contentDescription = "Change text format",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(onClick = {
                richTextState.value.toggleParagraphStyle(
                    ParagraphStyle(
                        textAlign = TextAlign.Justify
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.FormatAlignJustify,
                    contentDescription = "Change text format",
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

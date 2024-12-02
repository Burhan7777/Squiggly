package com.pzbapps.squiggly.notebook_main_screen.presentation.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.domain.model.Note

@Composable
fun SingleItemNotebookList(
    note: Note,
    navHostController: NavHostController,
    title: String,
    showLockedNotes: MutableState<Boolean>
) {

    var richTextState = rememberRichTextState()
    var contentText = remember { mutableStateOf("") }
    val color = if (note.color == 0) MaterialTheme.colors.primary.toArgb() else note.color
    //  LaunchedEffect(note.content) {
    //    scope.launch(Dispatchers.Default) {
    // Truncate the HTML content to a reasonable preview length
    val truncatedHtml = if (note.content.length > 300) {
        note.content.take(300) + "..."
    } else {
        note.content
    }

    // Set the truncated HTML to richTextState for a preview
    val previewText = richTextState.setHtml(truncatedHtml).annotatedString.text

    // Update the contentText on the main thread with the preview text
    //  withContext(Dispatchers.Main) {
    contentText.value = previewText


    if (note.listOfCheckedNotes.size == 0 && note.listOfBulletPointNotes.size == 0 && !note.deletedNote && !note.archive && !note.locked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                    MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(
                        Screens.EditNoteScreen.editNoteWithId(
                            note.id,
                            Constant.HOME
                        )
                    )
                },
            shape = androidx.compose.material3.MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //   elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(color),
                contentColor = MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(color),
                disabledContentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(
                text = note.title,
                modifier = Modifier.padding(10.dp),
                fontSize = 25.sp,
                fontFamily = FontFamily.fontFamilyBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = contentText.value,
                modifier = Modifier.padding(10.dp),
                fontSize = 15.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = FontFamily.fontFamilyLight
            )
        }
    } else if (note.listOfCheckedNotes.size > 0 && note.listOfBulletPointNotes.size == 0 && !note.deletedNote && !note.archive && !note.locked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(
                        Screens.EditNoteScreen.editNoteWithId(
                            note.id,
                            Constant.HOME
                        )
                    )
                    Log.i("title", note.title)
                },
            shape = androidx.compose.material3.MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //   elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(color),
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(color),
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = note.title,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 25.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = ((-22).dp),
                        alignment = Alignment.CenterVertically
                    )
                ) {
                    var listSize = note.listOfCheckedNotes.size
                    if (listSize >= 3) {
                        for (i in 0..2) {

                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-5).dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = note.listOfCheckedBoxes[i],
                                    {
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.onPrimary,
                                        checkmarkColor = MaterialTheme.colors.onSecondary,
                                        uncheckedColor = MaterialTheme.colors.onPrimary,
                                        disabledCheckedColor = MaterialTheme.colors.onPrimary,
                                        disabledUncheckedColor = MaterialTheme.colors.onPrimary,
                                    ),
                                    enabled = false,
                                )
                                Text(
                                    text = note.listOfCheckedNotes[i],
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                            }
                        }


                    } else {
                        for (i in note.listOfCheckedNotes.indices) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-5).dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = note.listOfCheckedBoxes[i],
                                    {
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colors.onPrimary,
                                        checkmarkColor = MaterialTheme.colors.onSecondary,
                                        uncheckedColor = MaterialTheme.colors.onPrimary,
                                        disabledCheckedColor = MaterialTheme.colors.onPrimary,
                                        disabledUncheckedColor = MaterialTheme.colors.onPrimary,
                                    ),
                                    enabled = false,
                                )
                                Text(
                                    text = note.listOfCheckedNotes[i],
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    } else if (note.listOfBulletPointNotes.size > 0 && note.listOfCheckedNotes.size == 0 && !note.deletedNote && !note.archive && !note.locked) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(
                        Screens.EditNoteScreen.editNoteWithId(
                            note.id,
                            Constant.HOME
                        )
                    )
                    Log.i("title", note.title)
                },
            shape = androidx.compose.material3.MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //    elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(color),
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(color),
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = note.title,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 25.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        space = ((-22).dp),
                        alignment = Alignment.CenterVertically
                    )
                ) {

                    var listSize = note.listOfBulletPointNotes.size
                    if (listSize >= 2) {
                        for (i in 0..1) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-5).dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bullet_point),
                                    contentDescription = "Bullet Point",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                                )
                                Text(
                                    text = note.listOfBulletPointNotes[i],
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                            }
                        }
                    } else {
                        for (i in note.listOfBulletPointNotes.indices) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy((-5).dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painterResource(id = R.drawable.bullet_point),
                                    contentDescription = "Bullet Point",
                                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                                )
                                Text(
                                    text = note.listOfBulletPointNotes[i],
                                    fontFamily = FontFamily.fontFamilyRegular,
                                    fontSize = 15.sp,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
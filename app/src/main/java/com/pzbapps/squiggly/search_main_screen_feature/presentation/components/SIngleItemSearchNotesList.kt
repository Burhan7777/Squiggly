package com.pzbapps.squiggly.search_main_screen_feature.presentation.components

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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.domain.model.Note

@Composable
fun SingleItemSearchNoteList(note: Note, navHostController: NavHostController, query: String) {

    var richTextState = rememberRichTextState()
    var contentText = richTextState.setHtml(note.content).annotatedString.text

    var fontFamily = remember { mutableStateOf(FontFamily.fontFamilyRegular) }

    var fontFamilyString = note.font

    when (fontFamilyString) {
        FontFamily.lufgaRegular -> fontFamily.value = FontFamily.fontFamilyRegular
        FontFamily.lufgaBold -> fontFamily.value = FontFamily.fontFamilyBold
        FontFamily.lufgaextraLight -> fontFamily.value = FontFamily.fontFamilyExtraLight
        FontFamily.pacificoString -> fontFamily.value = FontFamily.pacificoRegular
        FontFamily.parkinsonsString -> fontFamily.value = FontFamily.parkinsons
        FontFamily.jaroString -> fontFamily.value = FontFamily.jaro
        FontFamily.dancingScriptString -> fontFamily.value = FontFamily.dancingScript
        FontFamily.dotoString -> fontFamily.value = FontFamily.doto
        FontFamily.eduString -> fontFamily.value = FontFamily.edu
        FontFamily.lobsterString -> fontFamily.value = FontFamily.lobster
        FontFamily.playfairString -> fontFamily.value = FontFamily.playfair
        FontFamily.poppinsString -> fontFamily.value = FontFamily.poppins
        else -> FontFamily.fontFamilyRegular
    }

    if (!note.locked && note.listOfCheckedNotes.size == 0 && note.listOfBulletPointNotes.size == 0) {
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
                        Screens.EditNoteScreen.editNoteWithSearchQuery(
                            note.id,
                            Constant.HOME,
                            query
                        )
                    )
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //   elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(note.color),
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(note.color),
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {
            Text(
                text = note.title,
                modifier = Modifier.padding(10.dp),
                fontSize = 25.sp,
                fontFamily = fontFamily.value,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = contentText,
                modifier = Modifier.padding(10.dp),
                fontSize = 15.sp,
                overflow = TextOverflow.Ellipsis,
                fontFamily = fontFamily.value
            )
        }
    } else if (note.listOfCheckedNotes.size > 0 && !note.locked && note.listOfBulletPointNotes.size == 0) {
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
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //  elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(note.color),
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(note.color),
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = note.title,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 25.sp,
                    fontFamily =  fontFamily.value,
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
                                        checkedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        checkmarkColor = androidx.compose.material.MaterialTheme.colors.onSecondary,
                                        uncheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        disabledCheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        disabledUncheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                                    ),
                                    enabled = false,
                                )
                                Text(
                                    text = note.listOfCheckedNotes[i],
                                    fontFamily =  fontFamily.value,
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
                                        checkedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        checkmarkColor = androidx.compose.material.MaterialTheme.colors.onSecondary,
                                        uncheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        disabledCheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                                        disabledUncheckedColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                                    ),
                                    enabled = false,
                                )
                                Text(
                                    text = note.listOfCheckedNotes[i],
                                    fontFamily =  fontFamily.value,
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
    } else if (note.listOfBulletPointNotes.size > 0 && note.listOfCheckedNotes.size == 0 && !note.locked) {
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
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            //    elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(note.color),
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = Color(note.color),
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = note.title,
                    modifier = Modifier.padding(10.dp),
                    fontSize = 25.sp,
                    fontFamily =  fontFamily.value,
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
                                    painterResource(id = com.pzbapps.squiggly.R.drawable.bullet_point),
                                    contentDescription = "Bullet Point",
                                    colorFilter = ColorFilter.tint(androidx.compose.material.MaterialTheme.colors.onPrimary)
                                )
                                Text(
                                    text = note.listOfBulletPointNotes[i],
                                    fontFamily =  fontFamily.value,
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
                                    painterResource(id = com.pzbapps.squiggly.R.drawable.bullet_point),
                                    contentDescription = "Bullet Point",
                                    colorFilter = ColorFilter.tint(androidx.compose.material.MaterialTheme.colors.onPrimary)
                                )
                                Text(
                                    text = note.listOfBulletPointNotes[i],
                                    fontFamily =  fontFamily.value,
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

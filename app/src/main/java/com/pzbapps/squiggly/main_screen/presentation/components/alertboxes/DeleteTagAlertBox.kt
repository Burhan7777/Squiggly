package com.pzbapps.squiggly.main_screen.presentation.components.alertboxes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel

@Composable
fun DeleteTagAlertBox(
    viewModel: MainActivityViewModel,
    tag: String,
    showProgressBarOfDeletingTag: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
        title = {
            Column {
                Text(
                    "Delete Tag",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyBold,
                    color = MaterialTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    "Are you sure you want to delete tag? This will NOT delete notes associated with it",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }, confirmButton = {
            androidx.compose.material.OutlinedButton(
                onClick = {
                    showProgressBarOfDeletingTag.value = true
                    viewModel.getAllNotes()
                    var listOfNotes = viewModel.listOfNotes
                    for (note in listOfNotes) {
                        var tags = note.tags
                        if (tags.contains(tag)) {
                            tags.remove(tag)
                        }
                        var note = note.copy(tags = tags)
                        viewModel.deleteTag(tag)
                        viewModel.updateNote(note)
                    }
                    showProgressBarOfDeletingTag.value = false
                    viewModel.getAllTags()
                    viewModel.tags.removeAll { it.name == tag }
                    onDismiss()
                },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onSecondary
                ),
            ) {
                androidx.compose.material.Text(
                    text = "Yes",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        }, dismissButton = {
            androidx.compose.material.OutlinedButton(
                onClick = {

                    onDismiss()
                },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                ),
            ) {
                androidx.compose.material.Text(
                    text = "Cancel",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        })
}
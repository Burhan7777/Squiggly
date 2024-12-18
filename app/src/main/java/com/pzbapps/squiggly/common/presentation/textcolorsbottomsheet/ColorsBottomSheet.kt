package com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.bottomsheetcomponents.ColorsColorSelection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextColorBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    richTextState: MutableState<RichTextState>,
) {
    if (showBottomSheet.value) {

        //  val sheetState = rememberModalBottomSheetState(initialValue = SheetValue.Hidden)
        val scope = rememberCoroutineScope()

        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf("Text colors")

        ModalBottomSheet(
            containerColor = MaterialTheme.colors.primaryVariant,
            onDismissRequest = { scope.launch { showBottomSheet.value = false } },
            // sheetState = sheetState,
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(selected = selectedTabIndex == index,
                            selectedContentColor = MaterialTheme.colors.primary,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, color = Color.Black) })
                    }
                }
            }

            when (selectedTabIndex) {
                0 -> {
                    ColorsColorSelection(richTextState,showBottomSheet)
                }

            }
            // Button to show the bottom shee
        }

    }
}
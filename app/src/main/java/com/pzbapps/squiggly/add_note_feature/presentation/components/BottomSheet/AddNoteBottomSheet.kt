package com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.bottomsheetcomponents.ColorSelection
import com.pzbapps.squiggly.ui.theme.primaryColorDark
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    backgroundColor: MutableState<Color> = mutableStateOf(Color.Black),
    backgroundColorInInt: MutableState<Int> = mutableIntStateOf(0)
) {
    if (showBottomSheet.value) {

        //  val sheetState = rememberModalBottomSheetState(initialValue = SheetValue.Hidden)
        val scope = rememberCoroutineScope()

        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabTitles = listOf("Colors")

        ModalBottomSheet(
            modifier = Modifier
                .height(500.dp),
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
                    ColorSelection(backgroundColor, backgroundColorInInt)
                }

            }
            // Button to show the bottom shee
        }

    }
}
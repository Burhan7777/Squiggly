package com.pzbapps.squiggly.common.presentation.fontsbottomsheet

import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.bottomsheetcomponents.ColorsColorSelection
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    richTextState: MutableState<RichTextState>,
    fontFamily: MutableState<androidx.compose.ui.text.font.FontFamily>
) {
    if (showBottomSheet.value) {

        //  val sheetState = rememberModalBottomSheetState(initialValue = SheetValue.Hidden)
        val scope = rememberCoroutineScope()


        ModalBottomSheet(
            modifier = Modifier
                .height(500.dp),
            containerColor = MaterialTheme.colors.primaryVariant,
            onDismissRequest = { scope.launch { showBottomSheet.value = false } },
            // sheetState = sheetState,
        ) {
            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(FontFamily.listOfFonts()) { font ->
                    SingleRowFontsBottomSheet(font, richTextState, fontFamily, showBottomSheet)
                }
            }

            // Button to show the bottom shee
        }

    }
}
package com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.bottomsheetcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.add_note_feature.domain.usecase.listOfColors
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.SingleRowColorList
import com.pzbapps.squiggly.common.presentation.textcolorsbottomsheet.SingleRowTextColorList

@Composable
fun ColorsColorSelection(
    richTextState: MutableState<RichTextState>,
    showBottomSheet: MutableState<Boolean>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(count = 4)) {
        items(listOfColors) { colorList ->
            SingleRowTextColorList(colorList, richTextState, showBottomSheet)
        }
    }
}
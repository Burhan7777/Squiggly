package com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.bottomsheetcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pzbapps.squiggly.add_note_feature.domain.usecase.listOfColors
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.SingleRowColorList

@Composable
fun ColorSelection() {
    LazyVerticalGrid(columns = GridCells.Fixed(count = 4)) {
        items(listOfColors) { colorList ->
            SingleRowColorList(colorList)
        }
    }
}
package com.pzbapps.squiggly.add_note_feature.domain.usecase

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.pzbapps.squiggly.ui.theme.green1
import com.pzbapps.squiggly.ui.theme.green2
import com.pzbapps.squiggly.ui.theme.green3
import com.pzbapps.squiggly.ui.theme.green4

data class ColorList(
    var name: String,
    var rgb: Int
)

var listOfColors =
    listOf<ColorList>(
        ColorList("green1", green1.toArgb()),
        ColorList("green2", green2.toArgb()),
        ColorList("green3", green3.toArgb()),
        ColorList("green4", green4.toArgb()),
    )
package com.pzbapps.squiggly.add_note_feature.domain.usecase

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.pzbapps.squiggly.ui.theme.black1
import com.pzbapps.squiggly.ui.theme.black2
import com.pzbapps.squiggly.ui.theme.black3
import com.pzbapps.squiggly.ui.theme.green1
import com.pzbapps.squiggly.ui.theme.green2
import com.pzbapps.squiggly.ui.theme.green3
import com.pzbapps.squiggly.ui.theme.green4
import com.pzbapps.squiggly.ui.theme.primaryColorDark
import com.pzbapps.squiggly.ui.theme.primaryColorLight
import com.pzbapps.squiggly.ui.theme.red1
import com.pzbapps.squiggly.ui.theme.red2
import com.pzbapps.squiggly.ui.theme.red3
import com.pzbapps.squiggly.ui.theme.red4

data class ColorList(
    var name: String,
    var rgb: Int
)

var listOfColors =
    listOf<ColorList>(
        ColorList("Default", primaryColorDark.toArgb()),
        ColorList("black2", black1.toArgb()),
        ColorList("black3", black2.toArgb()),
        ColorList("black4", black3.toArgb()),

        ColorList("green1", green1.toArgb()),
        ColorList("green2", green2.toArgb()),
        ColorList("green3", green3.toArgb()),
        ColorList("green4", green4.toArgb()),

        ColorList("red1", red1.toArgb()),
        ColorList("red2", red2.toArgb()),
        ColorList("red3", red3.toArgb()),
        ColorList("red4", red4.toArgb()),
    )

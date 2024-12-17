package com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.bottomsheetcomponents

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pzbapps.squiggly.add_note_feature.domain.usecase.ColorList
import com.pzbapps.squiggly.add_note_feature.domain.usecase.listOfColors
import com.pzbapps.squiggly.add_note_feature.presentation.components.BottomSheet.SingleRowColorList
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.MainActivity

@Composable
fun ColorSelection(
    backgroundColor: MutableState<Color>,
    backgroundColorInInt: MutableState<Int>,
    activity: MainActivity
) {
    val prefs: SharedPreferences =
        activity.getSharedPreferences(Constant.CHANGE_THEME, MODE_PRIVATE)
    var result = prefs.getString(Constant.THEME_KEY, Constant.SYSTEM_DEFAULT)
    var listOfColorsFinal = ArrayList<ColorList>()
    if (result == Constant.DARK_THEME) {
        listOfColorsFinal =
            listOfColors.filter { !it.name.contains("white") }.toCollection(ArrayList())
    } else if (result == Constant.LIGHT_THEME) {
        listOfColorsFinal =
            listOfColors.filter { !it.name.contains("black") }.toCollection(ArrayList())
    } else if (result == Constant.SYSTEM_DEFAULT) {
        if (isSystemInDarkTheme()) {
            listOfColorsFinal =
                listOfColors.filter { !it.name.contains("white") }.toCollection(ArrayList())
        } else {
            listOfColorsFinal =
                listOfColors.filter { !it.name.contains("black") }.toCollection(ArrayList())
        }
    }
    LazyVerticalGrid(columns = GridCells.Fixed(count = 4)) {
        items(listOfColorsFinal) { colorList ->
            SingleRowColorList(colorList, backgroundColor, backgroundColorInInt)
        }
    }

}
package com.pzbapps.squiggly.common.presentation

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.toFontFamily
import com.pzbapps.squiggly.R

object FontFamily {

    val fontFamilyExtraLight = Font(R.font.lufgaextralight).toFontFamily()
    val fontFamilyRegular = Font(R.font.lufgaregular).toFontFamily()
    val fontFamilyLight = Font(R.font.lufgalight).toFontFamily()
    val fontFamilyBold = Font(R.font.lufgablack).toFontFamily()
    val pacificoRegular = Font(R.font.pacificoregular).toFontFamily()
    val parkinsons = Font(R.font.parkinsons).toFontFamily()

    var lufgaRegular = "Default"
    var lufgaBold = "lufgaBold"
    var lufgaextraLight = "lufgaextraLight"
    var pacificoString = "pacificoRegular"
    var parkinsonsString = "parkinsons"

    val listOfFonts = ArrayList<FontFamily>()
    fun listOfFonts(): ArrayList<FontFamily> {
        if (!listOfFonts.contains(fontFamilyRegular))
            listOfFonts.add(fontFamilyRegular)

        if (!listOfFonts.contains(fontFamilyBold))
            listOfFonts.add(fontFamilyBold)

        if (!listOfFonts.contains(fontFamilyExtraLight))
            listOfFonts.add(fontFamilyExtraLight)

        if (!listOfFonts.contains(pacificoRegular))
            listOfFonts.add(pacificoRegular)

        if (!listOfFonts.contains(parkinsons))
            listOfFonts.add(parkinsons)
        return listOfFonts
    }
}
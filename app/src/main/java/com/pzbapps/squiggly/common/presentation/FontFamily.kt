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
    val jaro = Font(R.font.jaro).toFontFamily()
    val dancingScript = Font(R.font.dancingscript).toFontFamily()
    val doto = Font(R.font.doto).toFontFamily()
    val edu = Font(R.font.edu).toFontFamily()
    val lobster = Font(R.font.lobster).toFontFamily()
    val playfair = Font(R.font.playfair).toFontFamily()
    val poppins = Font(R.font.poppins).toFontFamily()
    val playWriteAustralia = Font(R.font.playwriteaustrailia).toFontFamily()

    var lufgaRegular = "Default"
    var lufgaBold = "lufgaBold"
    var lufgaextraLight = "lufgaextraLight"
    var pacificoString = "pacificoRegular"
    var parkinsonsString = "parkinsons"
    val jaroString = "jaro"
    val dancingScriptString = "dancingscript"
    val dotoString = "doto"
    val eduString = "edu"
    val lobsterString = "lobster"
    val playfairString = "playfair"
    val poppinsString = "poppins"
    val playWriteAustraliaString = "playwriteaustralia"

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

        if (!listOfFonts.contains(jaro))
            listOfFonts.add(jaro)

        if (!listOfFonts.contains(dancingScript))
            listOfFonts.add(dancingScript)

        if (!listOfFonts.contains(doto))
            listOfFonts.add(doto)

        if (!listOfFonts.contains(edu))
            listOfFonts.add(edu)

        if (!listOfFonts.contains(lobster))
            listOfFonts.add(lobster)

        if (!listOfFonts.contains(playfair))
            listOfFonts.add(playfair)

        if (!listOfFonts.contains(poppins))
            listOfFonts.add(poppins)

        return listOfFonts
    }

    var listOfPremiumFonts = ArrayList<FontFamily>()
    fun listOfPremiumFonts(): ArrayList<FontFamily> {
        if (!listOfPremiumFonts.contains(playWriteAustralia)) {
            listOfPremiumFonts.add(playWriteAustralia)
        }
        return listOfPremiumFonts
    }
}
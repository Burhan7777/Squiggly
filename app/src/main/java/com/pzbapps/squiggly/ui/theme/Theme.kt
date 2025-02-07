package com.pzbapps.squiggly.ui.theme

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.pzbapps.squiggly.common.domain.utils.Constant


private val DarkColorPalette = darkColors(
    primary = primaryColorDark,
    primaryVariant = secondaryColorDark,
    secondary = gradient1,
    onPrimary = white,
    onSecondary = black,
)

val LightColorPalette = lightColors(
    primary = primaryColorLight,
    primaryVariant = secondaryColorLight,
    secondary = gradient1,
    onPrimary = black,
    onSecondary = white

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ScribbleTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(Constant.CHANGE_THEME, MODE_PRIVATE)

    // Create a mutable state to hold the current theme
    var theme by remember { mutableStateOf(prefs.getString(Constant.THEME_KEY, Constant.SYSTEM_DEFAULT)) }

    // Listen for changes to SharedPreferences
    DisposableEffect(prefs) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Constant.THEME_KEY) {
                // Update the theme when the preference changes
                theme = prefs.getString(Constant.THEME_KEY, Constant.SYSTEM_DEFAULT)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // Set the color palette based on the current theme
    val colors = when (theme) {
        Constant.DARK_THEME -> DarkColorPalette
        Constant.LIGHT_THEME -> LightColorPalette
        else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun BubbleTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val prefs: SharedPreferences = context.getSharedPreferences(Constant.CHANGE_THEME, MODE_PRIVATE)

    // Create a mutable state to hold the current theme
    var theme by remember { mutableStateOf(prefs.getString(Constant.THEME_KEY, Constant.SYSTEM_DEFAULT)) }

    // Listen for changes to SharedPreferences
    DisposableEffect(prefs) {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Constant.THEME_KEY) {
                // Update the theme when the preference changes
                theme = prefs.getString(Constant.THEME_KEY, Constant.SYSTEM_DEFAULT)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    // Set the color palette based on the current theme
    val colors = when (theme) {
        Constant.DARK_THEME -> DarkColorPalette
        Constant.LIGHT_THEME -> LightColorPalette
        else -> if (isSystemInDarkTheme()) DarkColorPalette else LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
package com.pzbapps.squiggly.widgetfeature.presentation

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize

class MyAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            mainScreen()
        }
    }
}

@Composable
fun mainScreen() {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        Button(text = "Hello", onClick = {})
    }
}
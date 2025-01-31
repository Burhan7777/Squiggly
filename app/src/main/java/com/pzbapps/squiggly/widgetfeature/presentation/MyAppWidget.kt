package com.pzbapps.squiggly.widgetfeature.presentation

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import com.pzbapps.squiggly.common.presentation.MainActivity

class MyAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MainScreen(context)
        }
    }
}

@Composable
fun MainScreen(context: Context) {
    Column(modifier = GlanceModifier.fillMaxSize()) {
        Button(
            text = "Hello", onClick =
            actionStartActivity<MainActivity>(
                actionParametersOf(destinationKey to "addNote")
            )
        )
    }
}

private val destinationKey = ActionParameters.Key<String>(
    "squiggly://addNote"
)
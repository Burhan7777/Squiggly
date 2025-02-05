package com.pzbapps.squiggly.bubble_note_feature.presentation

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import android.content.pm.ServiceInfo
import androidx.compose.ui.draw.clip
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.bubble_note_feature.presentation.BubbleActivity

class BubbleService : Service(), LifecycleOwner, SavedStateRegistryOwner {
    private lateinit var windowManager: WindowManager
    private lateinit var frameLayout: FrameLayout
    private lateinit var params: WindowManager.LayoutParams

    // Lifecycle and SavedStateRegistry
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        startForeground()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        savedStateRegistryController.performRestore(null)
        addFloatingBubble()
    }

    private fun startForeground() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, "bubble_note")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Bubble note")
            .setContentText("Press to close the bubble note.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(10, notification)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceCompat.startForeground(
                this,
                10,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            ServiceCompat.startForeground(
                this,
                10,
                notification,
                0
            )
        }
    }

    private fun addFloatingBubble() {
        // Set up layout parameters for the floating bubble
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 100
            y = 200
        }

        // Create a FrameLayout to hold the ComposeView
        frameLayout = FrameLayout(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
        }

        // Create a ComposeView for the floating bubble UI
        val composeView = ComposeView(this).apply {
            // Set the LifecycleOwner and SavedStateRegistryOwner
            setViewTreeLifecycleOwner(this@BubbleService)
            setViewTreeSavedStateRegistryOwner(this@BubbleService)
            setContent {
                FloatingBubbleUI { openQuickNote() }
            }
        }

        // Add the ComposeView to the FrameLayout
        frameLayout.addView(composeView)

        // Add the FrameLayout to the WindowManager
        windowManager.addView(frameLayout, params)

        // Set up touch listener for dragging
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f

        composeView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(frameLayout, params)
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (Math.abs(event.rawX - initialTouchX) < 10 && Math.abs(event.rawY - initialTouchY) < 10) {
                        openQuickNote()
                    }
                    true
                }

                else -> false
            }
        }
    }

    @Composable
    fun FloatingBubbleUI(onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Blue)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_premium),
                contentDescription = "Floating Note",
                tint = Color.White
            )
        }
    }

    private fun openQuickNote() {
        val intent = Intent(this, BubbleActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::frameLayout.isInitialized) {
            windowManager.removeView(frameLayout)
        }
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // LifecycleOwner implementation
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    // SavedStateRegistryOwner implementation
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry
}
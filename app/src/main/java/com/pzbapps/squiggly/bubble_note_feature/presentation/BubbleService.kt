package com.pzbapps.squiggly.bubble_note_feature.presentation

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Note
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.bubble_note_feature.presentation.BubbleActivity
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.BubbleSize

class BubbleService : Service(), LifecycleOwner, SavedStateRegistryOwner {
    private lateinit var windowManager: WindowManager
    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    override fun onCreate() {
        super.onCreate()
        startForeground()
        Toast.makeText(this, "Service created", Toast.LENGTH_SHORT).show()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.currentState = Lifecycle.State.STARTED // Set lifecycle state

        addFloatingBubble()
    }

    fun closeService(): PendingIntent {
        var intent = Intent(this, BubbleService::class.java).apply {
            action = "STOP_SERVICE"
        }
        var pending = PendingIntent.getService(
            this,
            10,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return pending
    }


    private fun startForeground() {
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(this, "bubble_note")
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("Bubble note")
            .setContentText("Press to close the bubble note.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(closeService())
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "STOP_SERVICE") {
            stopForeground(true) // Remove the notification
            stopSelf() // Stop the service
            return START_NOT_STICKY
        }
        return START_STICKY
    }


    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private fun addFloatingBubble() {
        val params = WindowManager.LayoutParams(
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

        val composeView = ComposeView(this).apply {
            setViewTreeSavedStateRegistryOwner(this@BubbleService)
            setViewTreeLifecycleOwner(this@BubbleService)
            setContent {
                FloatingBubbleUI(
                    onClick = { /* Don't put click handler here */ }
                )
            }
        }

        // Move touch listener setup outside of ComposeView.apply
        var initialX = 0
        var initialY = 0
        var initialTouchX = 0f
        var initialTouchY = 0f
        var isMoved = false

        composeView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    isMoved = false
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = (event.rawX - initialTouchX).toInt()
                    val dy = (event.rawY - initialTouchY).toInt()

                    if (dx != 0 || dy != 0) {
                        isMoved = true
                        params.x = initialX + dx
                        params.y = initialY + dy
                        windowManager.updateViewLayout(view, params)
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    if (!isMoved) {
                        openQuickNote()
                    }
                    true
                }

                else -> false
            }
        }

        windowManager.addView(composeView, params)
    }


    @Composable
    fun FloatingBubbleUI(onClick: () -> Unit) {
        val context = LocalContext.current
        val prefs = context.getSharedPreferences("bubble_prefs", Context.MODE_PRIVATE)

        // Get saved preferences
        val size = BubbleSize.valueOf(prefs.getString("size", BubbleSize.MEDIUM.name)!!)
        val selectedIcon = prefs.getInt("icon", 0)

        // Get color as a single integer and convert to Color
        val colorInt = prefs.getInt("bubble_color", Color.Blue.toArgb())
        val color = Color(colorInt)

        // Convert size enum to dp
        val bubbleSize = when (size) {
            BubbleSize.SMALL -> 40.dp
            BubbleSize.MEDIUM -> 60.dp
            BubbleSize.LARGE -> 80.dp
        }

        // List of available icons
        val icons = listOf(
            Icons.Default.Note,
            Icons.Default.Edit,
            Icons.Default.Create,
            Icons.Default.AddCircle
        )

        Box(
            modifier = Modifier
                .size(bubbleSize)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icons[selectedIcon],
                contentDescription = "Floating Note",
                tint = Color.White,
                modifier = Modifier.size(bubbleSize * 0.6f)
            )
        }
    }
    //   @Composable
//    fun FloatingBubbleUI(onClick: () -> Unit) {
//        Box(
//            modifier = Modifier
//                .size(60.dp)
//                .clip(CircleShape)
//                .background(Color.Blue)
//            // Remove the clickable modifier here
//            ,
//            contentAlignment = Alignment.Center
//        ) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_premium),
//                contentDescription = "Floating Note",
//                tint = Color.White
//            )
//        }
//    }

    private fun openQuickNote() {
        val intent = Intent(this, BubbleActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED // Clean up lifecycle
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
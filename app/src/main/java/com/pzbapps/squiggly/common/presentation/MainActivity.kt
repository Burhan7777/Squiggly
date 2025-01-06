package com.pzbapps.squiggly.common.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.android.gms.ads.MobileAds
import com.pzbapps.squiggly.auto_save_firebase_backup_feature.BackupWorker
import com.pzbapps.squiggly.common.domain.usecase.ShowFIrebaseMaintenanceAlertBox
import com.pzbapps.squiggly.common.domain.usecase.ShowFirebaseOtherPurposesAlertBox
import com.pzbapps.squiggly.common.domain.usecase.ShowFirebaseUpdateAlertBox
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.alertboxes.ShowFirebaseDialogBox
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.ui.theme.ScribbleTheme
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.QonversionConfig
import com.qonversion.android.sdk.dto.QEnvironment
import com.qonversion.android.sdk.dto.QLaunchMode
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.dto.entitlements.QEntitlementRenewState
import com.qonversion.android.sdk.dto.entitlements.QEntitlementsCacheLifetime
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var viewModel: MainActivityViewModel
    lateinit var result: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val qonversionConfig = QonversionConfig.Builder(
            this,
            "xPbOCckgIIHehGB6hBQk1a1WHgfyJE-0",
            QLaunchMode.SubscriptionManagement
        ).setEnvironment(QEnvironment.Sandbox)
            .setEntitlementsCacheLifetime(QEntitlementsCacheLifetime.Month)
            .build()
        Qonversion.initialize(qonversionConfig)

        Qonversion.shared.checkEntitlements(object : QonversionEntitlementsCallback {
            override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                val premiumEntitlement = entitlements["monhtlypremium"]
                if (premiumEntitlement != null && premiumEntitlement.isActive) {
                    viewModel.ifUserIsPremium = true
                    when (premiumEntitlement.renewState) {
                        QEntitlementRenewState.NonRenewable -> {
                            // NonRenewable is the state of a consumable or non-consumable in-app purchase
                        }

                        QEntitlementRenewState.WillRenew -> {
                            // WillRenew is the state of an auto-renewable subscription
                        }

                        QEntitlementRenewState.BillingIssue -> {
                            // Prompt the user to update the payment method.
                        }

                        QEntitlementRenewState.Canceled -> {
                            // The user has turned off auto-renewal for the subscription, but the subscription has not expired yet.
                            // Prompt the user to resubscribe with a special offer.
                        }

                        else -> {

                        }
                    }
                }
            }

            override fun onError(error: QonversionError) {
                // handle error here
                Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
            }
        })

        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@MainActivity) {}
        }

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        viewModel.getAllNotebooks() // WE LOAD THE NOTEBOOKS IN THE START ONLY SO THAT TO SHOW THEM EVERYWHERE NEEDED.
        val sharedPreferences = getSharedPreferences("rememberUser", Context.MODE_PRIVATE)
        result = sharedPreferences.getString("LoggedInUser", "nothing")!!


        var titleUpdate = mutableStateOf("")
        var bodyUpdate = mutableStateOf("")
        var showUpdate = mutableStateOf(false)
        var versionCodeUpdate = mutableStateOf("")
        var buttonNameUpdate = mutableStateOf("")

        var titleMaintenance = mutableStateOf("")
        var bodyMaintenance = mutableStateOf("")
        var showMaintenance = mutableStateOf(false)
        var versionCodeMaintenance = mutableStateOf("")
        var buttonNameMaintenance = mutableStateOf("")

        var titleOtherPurposes = mutableStateOf("")
        var bodyOtherPurposes = mutableStateOf("")
        var showOtherPurposes = mutableStateOf(false)
        var versionCodeOtherPurposes = mutableStateOf("")
        var buttonNameOtherPurposes = mutableStateOf("")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminder Notifications"
            val descriptionText = "Channel for Reminder notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val conMgr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.activeNetworkInfo
        Log.i("network", netInfo.toString())
        deleteTrashNotes(viewModel, this)
        val prefs = getSharedPreferences(Constant.AUTO_SAVE_PREF, MODE_PRIVATE)
        val autoSave = prefs.getBoolean(Constant.AUTO_SAVE_KEY, false)

        var code = Int.MAX_VALUE
        try {
            var versionCode =
                packageManager.getPackageInfo(this.packageName, 0)
            code = versionCode.versionCode
        } catch (exception: PackageManager.NameNotFoundException) {

        }

        if (autoSave) {
            firebaseBackUp(this)
        }

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        setContent {
            ScribbleTheme {

                var selectedIItem = remember {
                    mutableStateOf(0)
                }
                var selectedNote = remember {
                    mutableStateOf(0)
                }
                var showUpdateDialogBox = remember { mutableStateOf(false) }

                var showMaintenanceDialogBox = remember { mutableStateOf(false) }

                var showOtherPurposesDialogBox = remember { mutableStateOf(false) }


                ShowFirebaseUpdateAlertBox(
                    showUpdateDialogBox = showUpdateDialogBox,
                    show = showUpdate,
                    title = titleUpdate,
                    body = bodyUpdate,
                    versionCode = versionCodeUpdate,
                    buttonName = buttonNameUpdate,
                    code = code
                )

                ShowFIrebaseMaintenanceAlertBox(
                    showUpdateDialogBox = showMaintenanceDialogBox,
                    show = showMaintenance,
                    title = titleMaintenance,
                    body = bodyMaintenance,
                    versionCode = versionCodeMaintenance,
                    buttonName = buttonNameMaintenance,
                    code = code
                )

                ShowFirebaseOtherPurposesAlertBox(
                    showUpdateDialogBox = showOtherPurposesDialogBox,
                    show = showOtherPurposes,
                    title = titleOtherPurposes,
                    body = bodyOtherPurposes,
                    versionCode = versionCodeOtherPurposes,
                    buttonName = buttonNameOtherPurposes,
                    code = code
                )



                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.primary)
                ) {
                    if (showUpdateDialogBox.value) {
                        ShowFirebaseDialogBox(
                            title = titleUpdate.value,
                            body = bodyUpdate.value,
                            confirmButtonText = buttonNameUpdate.value,
                            onClick = {
                                var intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.pzbdownloaders.scribble")
                                )
                                startActivity(intent)
                            }
                        ) {
                            showUpdateDialogBox.value = false
                        }
                    }
                    if (showMaintenanceDialogBox.value) {
                        ShowFirebaseDialogBox(
                            title = titleMaintenance.value,
                            body = bodyMaintenance.value,
                            confirmButtonText = buttonNameMaintenance.value,
                            onClick = {}
                        ) {
                            showMaintenanceDialogBox.value = false
                        }
                    }
                    if (showOtherPurposesDialogBox.value) {
                        ShowFirebaseDialogBox(
                            title = titleOtherPurposes.value,
                            body = bodyOtherPurposes.value,
                            confirmButtonText = buttonNameOtherPurposes.value,
                            onClick = {}
                        ) {
                            showOtherPurposesDialogBox.value = false
                        }
                    }
                    val navController = rememberNavController()
                    val noteId = remember { mutableIntStateOf(0) }
                    noteId.value = intent.getIntExtra("noteId", -1)
                    NavHost(
                        navController = navController,
                        viewModel,
                        this@MainActivity,
                        result,
                        selectedIItem,
                        selectedNote,
                        noteId
                    )


                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ScribbleTheme {
    }
}


fun deleteTrashNotes(viewModel: MainActivityViewModel, activity: MainActivity) {
    viewModel.getAllNotes()
    viewModel.listOfNotesLiveData.observe(activity) {
        var notesInTrash = mutableStateOf(SnapshotStateList<Note>())
        // println(it.size)
        for (i in it) {
            if (i.deletedNote) {
                notesInTrash.value.add(i)
            }
        }

        println(notesInTrash.value.size)
        for (i in notesInTrash.value) {
            if ((System.currentTimeMillis() - i.timePutInTrash) > 1209600000) {
                viewModel.deleteNoteById(i.id)
                //  Toast.makeText(activity, "Trash cleared", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun firebaseBackUp(context: Context) {
    val backupRequest = PeriodicWorkRequestBuilder<BackupWorker>(72, TimeUnit.HOURS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) // Only run if connected to the internet
                .build()
        )
        .build()

    WorkManager.getInstance(context)
        .enqueueUniquePeriodicWork(
            "firebaseAutoSave",
            ExistingPeriodicWorkPolicy.KEEP,
            backupRequest
        )

}


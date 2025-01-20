package com.pzbapps.squiggly.settings_feature.screen.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Looks
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.pzbapps.squiggly.common.domain.utils.CheckInternet
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.edit_note_feature.domain.usecase.checkIfUserHasCreatedPassword
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.ChangePasswordAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.ChangeThemeDialogBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.EmailWillBeSendAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.LoadingDialogBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.SetPasswordAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.ShowChangePasswordThroughCodeAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.ShowNotebooksAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.VerificationCodeAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.YouNeedToLoginFirst
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    activity: MainActivity,
    viewModel: MainActivityViewModel
) {
    val context = LocalContext.current
    var showPasswordDialog = remember { mutableStateOf(false) }
    var password = remember { mutableStateOf("") }
    var confirmPassword = remember { mutableStateOf("") }
    val showChangePasswordDialog = remember { mutableStateOf(false) }
    var showNotebooksDialogBox = remember { mutableStateOf(false) }
    var showChangeThemeDialogBox = remember { mutableStateOf(false) }
    var showEmailWillbeSendAlertBox = rememberSaveable { mutableStateOf(false) }
    var showVerificationCodeAlertBox = rememberSaveable { mutableStateOf(false) }
    var showLoadingDialogOfEmailWIllbeSend = rememberSaveable { mutableStateOf(false) }
    var showUpdatePasswordDialogBox = rememberSaveable { mutableStateOf(false) }
    var showYouNeedToLoginFirstDialogBox = rememberSaveable { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navHostController.navigateUp() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBackIos,
                    contentDescription = "Back to main screen",
                    tint = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
            }
            Text(
                text = "Settings",
                fontFamily = FontFamily.fontFamilyBold,
                fontSize = 35.sp,
                modifier = Modifier.padding(start = 5.dp),
                color = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    if (CheckInternet.isInternetAvailable(context)) {
                        var user = Firebase.auth.currentUser
                        if (user != null) {
                            val result =
                                checkIfUserHasCreatedPassword() // This  file can be found in editNote -> domain
                            result.observe(activity) {
                                if (it == true) {
                                    Toast
                                        .makeText(
                                            context,
                                            "Password has been already created",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                } else {
                                    showPasswordDialog.value = true
                                }
                            }
                        } else {
                            showYouNeedToLoginFirstDialogBox.value = true
                        }
                    } else {
                        Toast
                            .makeText(context, "This needs internet", Toast.LENGTH_SHORT)
                            .show()
                    }


                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Lock icon",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Password Protect Notes",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(80.dp)
//                .padding(10.dp)
//                .border(
//                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
//                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
//                        topStart = CornerSize(10.dp),
//                        topEnd = CornerSize(10.dp),
//                        bottomStart = CornerSize(10.dp),
//                        bottomEnd = CornerSize(10.dp),
//                    )
//                )
//                .clickable {
//                    showNotebooksDialogBox.value = true
//                },
//            shape = MaterialTheme.shapes.medium.copy(
//                topStart = CornerSize(10.dp),
//                topEnd = CornerSize(10.dp),
//                bottomStart = CornerSize(10.dp),
//                bottomEnd = CornerSize(10.dp),
//            ),
//            elevation = CardDefaults.cardElevation(15.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
//                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
//                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
//                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
//            )
//        ) {
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    imageVector = Icons.Filled.Lock,
//                    contentDescription = "Lock icon",
//                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
//                )
//                Text(
//                    text = "Lock Notebook",
//                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
//                    fontSize = 18.sp,
//                    fontFamily = FontFamily.fontFamilyRegular,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Box(modifier = Modifier.fillMaxWidth()) {
//                    Icon(
//                        imageVector = Icons.Filled.ArrowForwardIos,
//                        contentDescription = "Arrow Forward",
//                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
//                        modifier = Modifier
//                            .padding(top = 10.dp, end = 10.dp)
//                            .align(Alignment.CenterEnd)
//                    )
//                }
//            }
//        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(
                        1.dp,
                        androidx.compose.material.MaterialTheme.colors.onPrimary
                    ),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    if (CheckInternet.isInternetAvailable(context)) {
                        var user = Firebase.auth.currentUser
                        if (user != null) {
                            val result =
                                checkIfUserHasCreatedPassword() // This  file can be found in editNote -> domain
                            result.observe(activity) {
                                if (it == true) {
                                    showChangePasswordDialog.value = true
                                } else {
                                    Toast
                                        .makeText(
                                            activity,
                                            "Please setup the password first.",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                        } else {
                            showYouNeedToLoginFirstDialogBox.value = true
                        }
                    } else {
                        Toast
                            .makeText(context, "This needs internet", Toast.LENGTH_SHORT)
                            .show()
                    }

                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Password,
                    contentDescription = "Change Password",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Change Password",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    if (CheckInternet.isInternetAvailable(context)) {
                        var user = Firebase.auth.currentUser
                        if (user != null) {
                            showEmailWillbeSendAlertBox.value = true
                        } else {
                            showYouNeedToLoginFirstDialogBox.value = true
                        }
                    } else {
                        Toast
                            .makeText(context, "This needs internet", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Password,
                    contentDescription = "Forgot Password",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Forgot Password",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    if (CheckInternet.isInternetAvailable(context)) {
                        var user = Firebase.auth.currentUser
                        if (user != null) {
                            navHostController.navigate(Screens.BackupAndRestoreScreen.route)
                        } else {
                            showYouNeedToLoginFirstDialogBox.value = true
                        }
                    } else {
                        Toast
                            .makeText(context, "This needs internet", Toast.LENGTH_SHORT)
                            .show()
                    }

                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Sync,
                    contentDescription = "Backup notes icon",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Backup and Restore",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        var user = Firebase.auth.currentUser
        if (user != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(10.dp)
                    .border(
                        BorderStroke(
                            1.dp,
                            androidx.compose.material.MaterialTheme.colors.onPrimary
                        ),
                        androidx.compose.material.MaterialTheme.shapes.medium.copy(
                            topStart = CornerSize(10.dp),
                            topEnd = CornerSize(10.dp),
                            bottomStart = CornerSize(10.dp),
                            bottomEnd = CornerSize(10.dp),
                        )
                    )
                    .clickable {
                        if (CheckInternet.isInternetAvailable(context)) {
                            Firebase.auth.signOut()
                            Toast
                                .makeText(context, "Successfully signed out", Toast.LENGTH_SHORT)
                                .show()
                            navHostController.navigate(Screens.LoginScreen.route)
                        } else {
                            Toast
                                .makeText(context, "This needs internet", Toast.LENGTH_SHORT)
                                .show()
                        }

                    },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(10.dp),
                    topEnd = CornerSize(10.dp),
                    bottomStart = CornerSize(10.dp),
                    bottomEnd = CornerSize(10.dp),
                ),
                elevation = CardDefaults.cardElevation(15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                    disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                )
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Login,
                        contentDescription = "Backup notes icon",
                        modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                    )
                    Text(
                        text = "Sign in With Other Account",
                        modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.fontFamilyRegular,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForwardIos,
                            contentDescription = "Arrow Forward",
                            tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                            modifier = Modifier
                                .padding(top = 10.dp, end = 10.dp)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    showChangeThemeDialogBox.value = true
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Looks,
                    contentDescription = "Change theme",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Change Theme",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(Screens.PremiumPlanScreen.route)
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Subscriptions,
                    contentDescription = "Premium",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Premium",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(Screens.FeedbackScreen.route)
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Feedback,
                    contentDescription = "Feedback",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Feedback",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(Screens.ReportBugScreen.route)
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.BugReport,
                    contentDescription = "Bug report",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Report a Bug",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    Qonversion.shared.restore(object : QonversionEntitlementsCallback {
                        override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                            val premiumEntitlement = entitlements["monhtlypremium"]
                            if (premiumEntitlement != null && premiumEntitlement.isActive) {
                                Toast
                                    .makeText(
                                        activity,
                                        "Subscription restored successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                                viewModel.ifUserIsPremium.value = true
                            } else {
                                Toast
                                    .makeText(
                                        activity,
                                        "No are not subscribed. Please subscribe.",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }
                        }

                        override fun onError(error: QonversionError) {
                            // handle error here
                            Toast
                                .makeText(
                                    activity,
                                    "Please try again",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    })
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Subscriptions,
                    contentDescription = "Restore Subscription",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Restore Subscription",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp)
                .border(
                    BorderStroke(1.dp, androidx.compose.material.MaterialTheme.colors.onPrimary),
                    androidx.compose.material.MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(10.dp),
                        topEnd = CornerSize(10.dp),
                        bottomStart = CornerSize(10.dp),
                        bottomEnd = CornerSize(10.dp),
                    )
                )
                .clickable {
                    navHostController.navigate(Screens.PrivacyPolicy.route)
                },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(10.dp),
                topEnd = CornerSize(10.dp),
                bottomStart = CornerSize(10.dp),
                bottomEnd = CornerSize(10.dp),
            ),
            elevation = CardDefaults.cardElevation(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = androidx.compose.material.MaterialTheme.colors.primary,
                contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                disabledContainerColor = androidx.compose.material.MaterialTheme.colors.primary,
                disabledContentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.PrivacyTip,
                    contentDescription = "Privacy policy",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp)
                )
                Text(
                    text = "Privacy Policy",
                    modifier = Modifier.padding(top = 12.dp, start = 10.dp),
                    fontSize = 18.sp,
                    fontFamily = FontFamily.fontFamilyRegular,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForwardIos,
                        contentDescription = "Arrow Forward",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(top = 10.dp, end = 10.dp)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        }

        if (showPasswordDialog.value) {
            SetPasswordAlertBox(
                password = password,
                confirmPassword = confirmPassword,
                showPasswordDialog = showPasswordDialog,
                activity
            )
        }
        if (showChangePasswordDialog.value) {
            ChangePasswordAlertBox(
                changePasswordDialog = showChangePasswordDialog,
                activity = activity
            )
        }
        if (showNotebooksDialogBox.value) {
            ShowNotebooksAlertBox(viewModel = viewModel, activity = activity) {
                showNotebooksDialogBox.value = false
            }
        }
        if (showChangeThemeDialogBox.value) {
            ChangeThemeDialogBox(showChangeThemeDialogBox) {
                showChangeThemeDialogBox.value = false
            }
        }
        if (showEmailWillbeSendAlertBox.value) {
            EmailWillBeSendAlertBox(
                showVerificationCodeAlertBox,
                showEmailWillbeSendAlertBox,
                showLoadingDialogOfEmailWIllbeSend
            ) {
                showEmailWillbeSendAlertBox.value = false
            }
        }
        if (showVerificationCodeAlertBox.value) {
            VerificationCodeAlertBox(showUpdatePasswordDialogBox) {
                showVerificationCodeAlertBox.value = false
            }
        }
        if (showLoadingDialogOfEmailWIllbeSend.value) {
            LoadingDialogBox(text = mutableStateOf("Sending verification code."))
        }
        if (showUpdatePasswordDialogBox.value) {
            ShowChangePasswordThroughCodeAlertBox(activity = activity) {
                showUpdatePasswordDialogBox.value = false
            }
        }
        if (showYouNeedToLoginFirstDialogBox.value) {
            YouNeedToLoginFirst(navHostController) {
                showYouNeedToLoginFirstDialogBox.value = false
            }
        }
    }
}



package com.pzbapps.squiggly.main_screen.presentation.components

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pzbapps.squiggly.common.domain.utils.CheckInternet
import com.pzbapps.squiggly.common.domain.utils.NavigationItems
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.edit_note_feature.domain.usecase.checkIfUserHasCreatedPassword
import com.pzbapps.squiggly.main_screen.domain.usecase.cameraPermissionHandle
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.YouNeedToLoginFirst
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureMainScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    // notebookNavigation: ArrayList<String>,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>,
) {

    var drawerState = androidx.compose.material3.rememberDrawerState(
        initialValue =
        DrawerValue.Closed
    )
    var coroutineScope = rememberCoroutineScope()

    var showDialogToAccessLockedNotes = remember { mutableStateOf(false) }

    var showOrderDialogBox = remember { mutableStateOf(false) }

    var startCamera = remember { mutableStateOf(false) }

    var showYouNeedToLoginFirst = remember { mutableStateOf(false) }


    var launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera.value = true
        } else {
            Toast.makeText(
                activity,
                "This permission is needed to scan the document",
                Toast.LENGTH_SHORT
            ).show()
        }
    }



    if (selectedItem.value == 0) selectedNote.value = 100000


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colors.primaryVariant,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    androidx.compose.material.Text(
                        text = "SQUIGGLY",
                        color = MaterialTheme.colors.onPrimary,
                        fontFamily = FontFamily.fontFamilyBold,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 20.sp
                    )
//                    androidx.compose.material.OutlinedButton(
//                        onClick = {
//                            FirebaseAuth.getInstance().signOut()
//                            val sharedPreferences =
//                                activity.getSharedPreferences(
//                                    Constant.SHARED_PREP_NAME,
//                                    Context.MODE_PRIVATE
//                                )
//                            sharedPreferences.edit().apply {
//                                putString(Constant.USER_KEY, "LoggedOut")
//                            }.apply()
//
//                            navHostController.popBackStack()
//                            navHostController.navigate(Screens.LoginScreen.route)
//                        },
//                        border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
//                        shape = RoundedCornerShape(10.dp)
//                    ) {
//                        androidx.compose.material.Text(
//                            text = "Log out",
//                            color = MaterialTheme.colors.onPrimary,
//                            fontFamily = FontFamily.fontFamilyLight,
//                            fontSize = 10.sp,
//                        )
//                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                NavigationItems.navigationItems.forEachIndexed { indexed, item ->
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colors.primary,
                            unselectedContainerColor = MaterialTheme.colors.primaryVariant
                        ),
                        label = {
                            androidx.compose.material.Text(
                                text = item.label,
                                color = MaterialTheme.colors.onPrimary,
                                fontFamily = FontFamily.fontFamilyRegular
                            )
                        },
                        selected = selectedItem.value == indexed,
                        onClick = {
                            selectedItem.value = indexed
                            selectedNote.value = 100000

                            coroutineScope.launch {
                                drawerState.close()
                            }
                            if (selectedItem.value == 0) {
                                navHostController.navigate(Screens.HomeScreen.route)
                            } else if (selectedItem.value == 1) {
                                navHostController.navigate(Screens.ArchiveScreen.route)
                            } else if (selectedItem.value == 2) {
                                if (CheckInternet.isInternetAvailable(activity)) {
                                    val user = Firebase.auth.currentUser
                                    if (user != null) {
                                        var result = checkIfUserHasCreatedPassword()
                                        result.observe(activity) {
                                            if (it == true) {
                                                showDialogToAccessLockedNotes.value = true
                                            } else {
                                                Toast.makeText(
                                                    activity,
                                                    "Please setup password in settings first",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    } else {
                                        showYouNeedToLoginFirst.value = true
                                    }
                                } else {
                                    Toast.makeText(
                                        activity,
                                        "This needs internet",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } else if (selectedItem.value == 3) {
                                navHostController.navigate(Screens.TrashBinScreen.route)
                            } else if (selectedItem.value == 4) {
                                navHostController.navigate(Screens.SettingsScreen.route)
                            } else if (selectedItem.value == 5) {
                                var analytics = Firebase.analytics
                                var bundle = Bundle()
                                bundle.putString("rate_app_pressed", "rate_app_pressed")
                                analytics.logEvent("rate_app_pressed", bundle)
                                var intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=com.pzbapps.squiggly")
                                )
                                activity.startActivity(intent)
                            } else if (selectedItem.value == 6) {
                                navHostController.navigate(Screens.AboutUsScreen.route)
                            }
                        },
                        icon = {
                            if (selectedItem.value == indexed) Icon(
                                imageVector = item.selectedIcon,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = "Notes"
                            ) else Icon(
                                imageVector = item.unSelectedIcon,
                                contentDescription = "Notes",
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                androidx.compose.material.Text(
                    text = "NOTEBOOKS",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyBold,
                    modifier = Modifier.padding(20.dp),
                    fontSize = 20.sp
                )

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    viewModel.notebooks.forEachIndexed { indexed, items ->

                        if (indexed != 0) {
                            NavigationDrawerItem(
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = MaterialTheme.colors.primary,
                                    unselectedContainerColor = MaterialTheme.colors.primaryVariant
                                ),
                                selected = selectedNote.value == indexed,
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                                label = {
                                    androidx.compose.material.Text(
                                        text = items,
                                        color = MaterialTheme.colors.onPrimary,
                                        fontFamily = FontFamily.fontFamilyRegular
                                    )
                                },
                                onClick = {
                                    selectedNote.value = indexed
                                    selectedItem.value = 100000
                                    navHostController.navigate(
                                        Screens.NotebookMainScreen.notebookWithTitle(
                                            items
                                        )
                                    )


                                },
                                icon = {
                                    if (selectedNote.value == indexed) {
                                        Icon(
                                            imageVector = Icons.Filled.Folder,
                                            contentDescription = "Folder",
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Folder,
                                            contentDescription = "Folder",
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = Modifier.background(MaterialTheme.colors.primary),
//            topBar = {
//                androidx.compose.material.TopAppBar(actions = {
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            imageVector = Icons.Filled.GridOn,
//                            contentDescription = "Grid On",
//                            tint = MaterialTheme.colors.onPrimary
//                        )
//                    }
//                },
//                    title = {
//                        androidx.compose.material3.Text(
//                            text = "SQIUGGLY",
//                            fontSize = 20.sp,
//                            fontFamily = FontFamily.fontFamilyBold,
//                            color = MaterialTheme.colors.onPrimary
//                        )
//                    })
//            },
            bottomBar = {
                BottomAppBar {
                    BottomAppBar() {
                        IconButton(onClick = {
                            var analytics = Firebase.analytics
                            var bundle = Bundle()
                            bundle.putString(
                                "add_checkbox_button_pressed",
                                "add_checkbox_button_pressed"
                            )
                            analytics.logEvent("add_checkbox_button_pressed", bundle)
                            navHostController.navigate(Screens.CheckboxMainScreen.route)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.CheckBox,
                                contentDescription = "CheckBox",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                        IconButton(onClick = {
                            var analytics = Firebase.analytics
                            var bundle = Bundle()
                            bundle.putString(
                                "add_bullet_point_button_pressed",
                                "add_bullet_point_button_pressed"
                            )
                            analytics.logEvent("add_bullet_point_button_pressed", bundle)
                            navHostController.navigate(Screens.BulletPointMainScreen.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.FormatListBulleted,
                                contentDescription = "Bullet point list",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
//                        IconButton(
//                            onClick = {
//                                var result = cameraPermissionHandle(activity)
//                                if (result == "true") {
//                                    startCamera.value = true
//                                } else if (result == "Rationale shown") {
//                                    Toast.makeText(
//                                        activity,
//                                        "We need this permission to scan the document",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                } else if (result == "false") {
//                                    launcher.launch(android.Manifest.permission.CAMERA)
//                                }
//                            }
//
//                        ) {
//                            Icon(
//                                imageVector = Icons.Filled.Scanner,
//                                contentDescription = "Scanner",
//                                tint = MaterialTheme.colors.onPrimary
//                            )
//                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    onClick = {
                        var analytics = Firebase.analytics
                        var bundle = Bundle()
                        bundle.putString("add_note_button_pressed", "add_note_button_pressed")
                        analytics.logEvent("add_note_button_pressed", bundle)
                        navHostController.navigate(Screens.AddNoteScreen.route)
                    },
                    shape = MaterialTheme.shapes.medium.copy(
                        topStart = CornerSize(15.dp),
                        topEnd = CornerSize(15.dp),
                        bottomStart = CornerSize(15.dp),
                        bottomEnd = CornerSize(15.dp),
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = MaterialTheme.colors.onPrimary,
                        contentDescription = "Add Note"
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                TopSearchBar(
                    navHostController,
                    drawerState,
                    viewModel,
                    viewModel.showGridOrLinearNotes,
                    activity,
                    showOrderDialogBox
                )
                // ShowPremiumBar(activity)
                if (showDialogToAccessLockedNotes.value) {
                    AlertDialogBoxEnterPasswordToOpenLockedNotes(
                        viewModel = viewModel,
                        activity = activity,
                        navHostController = navHostController,
                    ) {
                        showDialogToAccessLockedNotes.value = false
                    }
                }
                if (showOrderDialogBox.value) {
                    SortNotesAlertBox(viewModel = viewModel, activity = activity) {
                        showOrderDialogBox.value = false
                    }
                }
                if (startCamera.value) {
                    CameraScreen(navHostController)
                }
                if (showYouNeedToLoginFirst.value) {
                    YouNeedToLoginFirst(navHostController) {
                        showYouNeedToLoginFirst.value = false
                    }
                }
                Notes(viewModel, activity, navHostController, viewModel.showGridOrLinearNotes)
            }
        }
    }
}


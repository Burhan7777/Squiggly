package com.pzbapps.squiggly.archive_notes_feature.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.AlertDialogBoxEnterPasswordToOpenLockedNotes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureArchiveScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    notebookNavigation: ArrayList<String>,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>
) {


    var drawerState = androidx.compose.material3.rememberDrawerState(
        initialValue =
        DrawerValue.Closed
    )
    var coroutineScope = rememberCoroutineScope()

    var showDialogToAccessLockedNotes = remember { mutableStateOf(false) }

//    if (selectedItem.value == 0) selectedNote.value = 100000
//
//
//    ModalNavigationDrawer(
//        drawerContent = {
//            ModalDrawerSheet(
//                drawerContainerColor = MaterialTheme.colors.primaryVariant,
//            ) {
//                androidx.compose.material.Text(
//                    text = "SCRIBBLE",
//                    color = MaterialTheme.colors.onPrimary,
//                    fontFamily = FontFamily.fontFamilyBold,
//                    modifier = Modifier.padding(20.dp),
//                    fontSize = 20.sp
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                NavigationItems.navigationItems.forEachIndexed { indexed, item ->
//                    NavigationDrawerItem(
//                        colors = NavigationDrawerItemDefaults.colors(
//                            selectedContainerColor = MaterialTheme.colors.primary,
//                            unselectedContainerColor = MaterialTheme.colors.primaryVariant
//                        ),
//                        label = {
//                            androidx.compose.material.Text(
//                                text = item.label,
//                                color = MaterialTheme.colors.onPrimary,
//                                fontFamily = FontFamily.fontFamilyRegular
//                            )
//                        },
//                        selected = selectedItem.value == indexed,
//                        onClick = {
//                            selectedItem.value = indexed
//                            coroutineScope.launch {
//                                drawerState.close()
//                            }
//                            if (selectedItem.value == 0) {
//                                navHostController.navigate(Screens.HomeScreen.route)
//                            } else if (selectedItem.value == 1) {
//                                navHostController.navigate(Screens.ArchiveScreen.route)
//                            } else if (selectedItem.value == 2) {
//                                var result = checkIfUserHasCreatedPassword()
//                                result.observe(activity) {
//                                    if (it == true) {
//                                        showDialogToAccessLockedNotes.value = true
//                                    } else {
//                                        Toast.makeText(
//                                            activity,
//                                            "Please setup password in settings first",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            } else if (selectedItem.value == 3) {
//                                navHostController.navigate(Screens.TrashBinScreen.route)
//                            } else if (selectedItem.value == 4) {
//                                navHostController.navigate(Screens.SettingsScreen.route)
//                            } else if (selectedItem.value == 5) {
//                                navHostController.navigate(Screens.AboutUsScreen.route)
//                            }
//                        },
//                        icon = {
//                            if (selectedItem.value == indexed) Icon(
//                                imageVector = item.selectedIcon,
//                                tint = MaterialTheme.colors.onPrimary,
//                                contentDescription = "Notes"
//                            ) else Icon(
//                                imageVector = item.unSelectedIcon,
//                                contentDescription = "Notes",
//                                tint = MaterialTheme.colors.onPrimary,
//                            )
//                        },
//                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
//                    )
//                }
//                androidx.compose.material.Text(
//                    text = "NOTEBOOKS",
//                    color = MaterialTheme.colors.onPrimary,
//                    fontFamily = FontFamily.fontFamilyBold,
//                    modifier = Modifier.padding(20.dp),
//                    fontSize = 20.sp
//                )
//
//                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
//                    viewModel.notebooks.forEachIndexed { indexed, items ->
//                        if (indexed != 0) {
//                            NavigationDrawerItem(
//                                colors = NavigationDrawerItemDefaults.colors(
//                                    selectedContainerColor = MaterialTheme.colors.primary,
//                                    unselectedContainerColor = MaterialTheme.colors.primaryVariant
//                                ),
//                                selected = selectedNote.value == indexed,
//                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
//                                label = {
//                                    androidx.compose.material.Text(
//                                        text = items,
//                                        color = MaterialTheme.colors.onPrimary,
//                                        fontFamily = FontFamily.fontFamilyRegular
//                                    )
//                                },
//                                onClick = {
//                                    selectedNote.value = indexed
//                                    selectedItem.value = 100000
//                                    navHostController.navigate(
//                                        Screens.NotebookMainScreen.notebookWithTitle(
//                                            items
//                                        )
//                                    )
//                                },
//                                icon = {
//                                    if (selectedNote.value == indexed) {
//                                        Icon(
//                                            imageVector = Icons.Filled.Folder,
//                                            contentDescription = "Folder",
//                                            tint = MaterialTheme.colors.onPrimary
//                                        )
//                                    } else {
//                                        Icon(
//                                            imageVector = Icons.Default.Folder,
//                                            contentDescription = "Folder",
//                                            tint = MaterialTheme.colors.onPrimary
//                                        )
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        },
//        drawerState = drawerState
//    ) {
    Scaffold(
        containerColor = MaterialTheme.colors.primary,
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material.Text(
                        text = "Archive",
                        fontFamily = FontFamily.fontFamilyRegular,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go Back to main screen",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colors.primary
                ),
            )

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            //  TopSearchBarArchive(navHostController, drawerState, viewModel)
            if (showDialogToAccessLockedNotes.value) {
                AlertDialogBoxEnterPasswordToOpenLockedNotes( // FILE IN MAIN SCREEN -> PRESENTATION-> COMPONENTS
                    viewModel = viewModel,
                    activity = activity,
                    navHostController = navHostController
                ) {
                    showDialogToAccessLockedNotes.value = false
                }
            }
            Notes(viewModel, activity, navHostController)
        }
    }
}

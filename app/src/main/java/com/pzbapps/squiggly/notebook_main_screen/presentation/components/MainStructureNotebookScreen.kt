package com.pzbapps.squiggly.notebook_main_screen.presentation.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.domain.model.Note
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.AlertDialogBoxEnterPasswordToOpenLockedNotes
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.DeleteTagAlertBox
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.EditTagsAlertBox
import com.pzbapps.squiggly.notebook_main_screen.presentation.components.alertboxes.DeleteAllNotesToo
import com.pzbapps.squiggly.notebook_main_screen.presentation.components.alertboxes.DeleteNotebookAlertBox
import com.pzbapps.squiggly.notebook_main_screen.presentation.components.alertboxes.UnlockNotes
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.LoadingDialogBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureNotebookScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    notebookNavigation: ArrayList<String>,
    title: String,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>
) {

    var drawerState = androidx.compose.material3.rememberDrawerState(
        initialValue =
        DrawerValue.Closed
    )
    var coroutineScope = rememberCoroutineScope()

    var showDialogToAccessLockedNotes = remember { mutableStateOf(false) }

    var showDeleteNotebookDialogBox = remember { mutableStateOf(false) }

    var showDeleteNotesTooDialogBox = remember { mutableStateOf(false) }

    // THIS SHOWS LOCKED NOTES IN NOTEBOOKS WHEN UNLOCKED BY UNLOCKED BUTTON

    var showUnlockDialogBox = rememberSaveable { mutableStateOf(false) }

//    var listOfLockedNotes =
//        remember { SnapshotStateList<Note>() } // CONSISTS OF LOCKED NOTES OF A PARTICULAR NOTEBOOK

    val scope = rememberCoroutineScope()

    val showEditTagsAlertBox = remember { mutableStateOf(false) }
    val showDeleteTagAlertBox = remember { mutableStateOf(false) }
    val showProgressOfRemovingTag = remember { mutableStateOf(false) }
    viewModel.getAllTags() // WE GET THE TAGS TO DISPLAY THEM IN EDIT TAG ALERT BOX

    val tag =
        remember { mutableStateOf("") } // WE GET THE SELECTED TAG TO REMOVE IT. ITS PASSED TO NOTE
    // AND SET THEIR IN THE ONCLICK OF THE DELETE BUTTON IN EDIT TAG ALERT BOX



//    if (selectedItem.value == 0) selectedNote.value = 100000
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
//
//                            coroutineScope.launch {
//                                drawerState.close()
//                            }
//                            if (selectedItem.value == 0) {
//                                navHostController.popBackStack()
//                                navHostController.popBackStack()
//                                navHostController.navigate(Screens.HomeScreen.route)
//                            } else if (selectedItem.value == 1) {
//                                navHostController.popBackStack()
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
//                viewModel.notebooks.forEachIndexed { indexed, items ->
//                    if (indexed != 0) {
//                        NavigationDrawerItem(
//                            colors = NavigationDrawerItemDefaults.colors(
//                                selectedContainerColor = MaterialTheme.colors.primary,
//                                unselectedContainerColor = MaterialTheme.colors.primaryVariant
//                            ),
//                            selected = selectedNote.value == indexed,
//                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
//                            label = {
//                                androidx.compose.material.Text(
//                                    text = items,
//                                    color = MaterialTheme.colors.onPrimary,
//                                    fontFamily = FontFamily.fontFamilyRegular
//                                )
//                            },
//                            onClick = {
//                                selectedNote.value = indexed
//                                selectedItem.value = 100000
//                                navHostController.navigate(
//                                    Screens.NotebookMainScreen.notebookWithTitle(
//                                        items
//                                    )
//                                )
//                            },
//                            icon = {
//                                if (selectedNote.value == indexed) {
//                                    Icon(
//                                        imageVector = Icons.Filled.Folder,
//                                        contentDescription = "Folder",
//                                        tint = MaterialTheme.colors.onPrimary
//                                    )
//                                } else {
//                                    Icon(
//                                        imageVector = Icons.Default.Folder,
//                                        contentDescription = "Folder",
//                                        tint = MaterialTheme.colors.onPrimary
//                                    )
//                                }
//                            }
//                        )
//                    }
//                }
//            }
//        },
//        drawerState = drawerState
//    ) {
    Scaffold(
        modifier = Modifier.background(MaterialTheme.colors.primary),
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material.Text(
                        text = title,
                        fontFamily = FontFamily.fontFamilyRegular,
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.showLockedNotes.value = false
                        viewModel.listOfLockedNotebooksNote.clear()
                        navHostController.navigateUp()
                    }) {
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
                actions = {
                    IconButton(onClick = {
                        showDeleteNotesTooDialogBox.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Notebook",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            )

        },
        bottomBar = {
            BottomAppBar {
                BottomAppBar() {
                    IconButton(onClick = {
                        navHostController.navigate(
                            Screens.CheckboxNotebookMainScreen.checkboxNotebookMainScreenWithNotebook(
                                title
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.CheckBox,
                            contentDescription = "CheckBox",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = {
                        navHostController.navigate(
                            Screens.BulletPointsNotebook.bulletPointsWithNotebook(
                                title
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListBulleted,
                            contentDescription = "Bullet point list",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    navHostController.navigate(
                        Screens.AddNoteInNotebookScreen.addNoteBookWIthName(
                            title
                        )
                    )
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
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            BackHandler {
                viewModel.listOfLockedNotebooksNote.clear()
                viewModel.showLockedNotes.value = false
                navHostController.navigateUp()
            }
            //   TopSearchBarNotebook(navHostController, drawerState, viewModel)
            if (showDialogToAccessLockedNotes.value) {
                AlertDialogBoxEnterPasswordToOpenLockedNotes(  // FILE IN MAIN SCREEN -> PRESENTATION-> COMPONENTS
                    viewModel = viewModel,
                    activity = activity,
                    navHostController = navHostController
                ) {
                    showDialogToAccessLockedNotes.value = false
                }
            }
            if (showDeleteNotebookDialogBox.value) {
                DeleteNotebookAlertBox(
                    viewModel = viewModel,
                    title,
                    navHostController,
                    activity
                ) {
                    showDeleteNotebookDialogBox.value = false
                }
            }
            if (showDeleteNotesTooDialogBox.value) {
                DeleteAllNotesToo(
                    viewModel = viewModel,
                    activity = activity,
                    navHostController = navHostController,
                    name = title,
                    showDeleteNotebookDialogBox = showDeleteNotebookDialogBox
                ) {
                    showDeleteNotesTooDialogBox.value = false
                }
            }
            if (showUnlockDialogBox.value) {
                UnlockNotes(
                    activity,
                    viewModel
                ) {
                    showUnlockDialogBox.value = false
                }
            }
            if (showEditTagsAlertBox.value) {
                EditTagsAlertBox(viewModel.tags, showDeleteTagAlertBox, tag) {
                    showEditTagsAlertBox.value = false
                }

            }
            if (showDeleteTagAlertBox.value) {
                DeleteTagAlertBox(viewModel, tag.value, showEditTagsAlertBox,showProgressOfRemovingTag) {
                    showDeleteTagAlertBox.value = false
                }
            }
            if (showProgressOfRemovingTag.value) {
                LoadingDialogBox(mutableStateOf("Removing tag"))
            }
            NotesNotebook(
                viewModel,
                activity,
                navHostController,
                title,
                showUnlockDialogBox,
                showEditTagsAlertBox,
            )
        }
    }
}

fun lockTheUnlockedNotesOfNotebook(
    listOfLockedNotes: SnapshotStateList<Note>,
    viewModel: MainActivityViewModel,
    navHostController: NavHostController,
    scope: CoroutineScope,
) {
    scope.launch {
        for (i in listOfLockedNotes) {
            viewModel.getNoteById(i.id)
            var note = viewModel.getNoteById.value
            var note1 = note.copy(
                locked = true
            )
            viewModel.updateNote(note1)
            delay(200)
            viewModel.getNoteById(i.id)
            var note2 = viewModel.getNoteById.value
            if (!note2.locked) {
                println("LOCKED TRIGGERED")
                lockTheUnlockedNotesOfNotebook(
                    listOfLockedNotes,
                    viewModel,
                    navHostController,
                    scope
                )
            } else {
                delay(300)
                navHostController.navigateUp()
            }
        }
    }
}
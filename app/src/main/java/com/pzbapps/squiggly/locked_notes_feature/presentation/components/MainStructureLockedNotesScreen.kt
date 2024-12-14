package com.pzbapps.squiggly.locked_notes_feature.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.DeleteTagAlertBox
import com.pzbapps.squiggly.main_screen.presentation.components.alertboxes.EditTagsAlertBox
import com.pzbapps.squiggly.settings_feature.screen.presentation.components.LoadingDialogBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainStructureLockedNotesScreen(
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

    val showEditTagsAlertBox = remember { mutableStateOf(false) }
    val showDeleteTagAlertBox = remember { mutableStateOf(false) }
    val showProgressOfRemovingTag = remember { mutableStateOf(false) }
    val tag =
        remember { mutableStateOf("") } // WE GET THE SELECTED TAG TO REMOVE IT. ITS PASSED TO NOTE
    // AND SET THEIR IN THE ONCLICK OF THE DELETE BUTTON IN EDIT TAG ALERT BOX

    viewModel.getAllTags()

//    if (selectedItem.value == 0) selectedNote.value = 100000
//
//
//    ModalNavigationDrawer(
//        drawerContent = {
//            ModalDrawerSheet(
//                drawerContainerColor = MaterialTheme.colors.primaryVariant,
//            ) {
//                Text(
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
//                            Text(
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
//                                navHostController.popBackStack()
//                                navHostController.popBackStack()
//                                navHostController.navigate(Screens.HomeScreen.route)
//                            } else if (selectedItem.value == 1) {
//                                navHostController.popBackStack()
//                                navHostController.navigate(Screens.ArchiveScreen.route)
//                            } else if (selectedItem.value == 2) {
//                                navHostController.navigate(Screens.LockedNotesScreen.route)
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
//                Text(
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
//                                    Text(
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
                    Text(
                        text = "Locked Notes",
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

        },
        bottomBar = {
            androidx.compose.material.BottomAppBar {
                androidx.compose.material.BottomAppBar() {
                    IconButton(onClick = { navHostController.navigate(Screens.CheckBoxLockedNotesMainScreen.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.CheckBox,
                            contentDescription = "CheckBox",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                    IconButton(onClick = { navHostController.navigate(Screens.BulletPointsLockedNotesMainScreen.route) }) {
                        Icon(
                            imageVector = Icons.Filled.FormatListBulleted,
                            contentDescription = "Bullet point list",
                            tint = MaterialTheme.colors.onPrimary
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    navHostController.navigate(Screens.AddNoteInLockedScreen.route)
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
            //TopSearchBarArchive(navHostController, drawerState, viewModel)
            if (showEditTagsAlertBox.value) {
                EditTagsAlertBox(viewModel.tags, showDeleteTagAlertBox, tag) {
                    showEditTagsAlertBox.value = false
                }

            }
            if (showDeleteTagAlertBox.value) {
                DeleteTagAlertBox(viewModel, tag.value, showProgressOfRemovingTag) {
                    showDeleteTagAlertBox.value = false
                }
            }
            if (showProgressOfRemovingTag.value) {
                LoadingDialogBox(mutableStateOf("Removing tag"))
            }
            LockedNotes(viewModel, activity, navHostController,showEditTagsAlertBox,)
        }
    }
}

package com.pzbapps.squiggly.main_screen.presentation.screens


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.presentation.components.MainStructureMainScreen

@Composable
fun NotesScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>,
    notesId: MutableIntState
) {

    WindowCompat.setDecorFitsSystemWindows(activity.window, true)

    // WE GET THE NOTEBOOKS HERE BECAUSE WE HAVE TO DISPLAY THEM IN THE NAVIGATION  VIEW

//    viewModel.getNoteBook()
//    val notebooks = viewModel.getNoteBooks.observeAsState().value
//    val notebookNavigation: ArrayList<String> = ArrayList()
//    for (i in notebooks?.indices ?: emptyList<String>().indices) {
//        notebookNavigation.add(notebooks!![i]?.notebook ?: "")
//    }
    MainStructureMainScreen(
        navHostController,
        viewModel,
        activity,
        // notebookNavigation,
        selectedItem,
        selectedNote,
    )
    LaunchedEffect(true) {
        if (notesId.intValue != -1) {
            navHostController.navigate(
                Screens.EditNoteScreen.editNoteWithId(
                    notesId.intValue,
                    Constant.HOME
                )
            )
        }
    }


}


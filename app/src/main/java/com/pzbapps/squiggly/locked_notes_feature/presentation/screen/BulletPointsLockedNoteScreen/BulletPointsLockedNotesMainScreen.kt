package com.pzbapps.squiggly.locked_notes_feature.presentation.screen.BulletPointsLockedNoteScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.locked_notes_feature.presentation.components.BulletPointsLockedNotesComponents.MainStructureBulletPointsLockedNotes

@Composable
fun BulletPointsLockedNotesMainScreen(
    navHostController: NavHostController,
    mainActivityViewModel: MainActivityViewModel,
    activity: MainActivity
) {

    var notebookState = rememberSaveable {
        mutableStateOf("")
    }

    var title = rememberSaveable {
        mutableStateOf("")
    }

    MainStructureBulletPointsLockedNotes(
        navController = navHostController,
        viewModel = mainActivityViewModel,
        notebookState,
        title,
        activity = activity
    )
}
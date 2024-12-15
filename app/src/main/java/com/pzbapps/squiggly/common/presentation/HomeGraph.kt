package com.pzbapps.squiggly.common.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.pzbapps.squiggly.add_bullet_points_note_feature.presentation.screens.BulletPointsNoteMainScreen
import com.pzbapps.squiggly.about_us.presentation.screens.AboutUsScreen
import com.pzbapps.squiggly.add_checkbox_note_feature.presentation.screens.CheckboxNoteMainScreen
import com.pzbapps.squiggly.add_note_feature.presentation.screens.AddNoteScreen
import com.pzbapps.squiggly.archive_notes_feature.presentation.screen.ArchiveNotesScreen
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.edit_note_feature.presentation.screens.EditNoteScreen
import com.pzbapps.squiggly.locked_notes_feature.presentation.screen.AddNoteInLockedScreen
import com.pzbapps.squiggly.locked_notes_feature.presentation.screen.BulletPointsLockedNoteScreen.BulletPointsLockedNotesMainScreen
import com.pzbapps.squiggly.locked_notes_feature.presentation.screen.CheckBoxLockedNotesScreen.CheckboxLockedNotesMainScreen
import com.pzbapps.squiggly.locked_notes_feature.presentation.screen.LockedNotesScreen
import com.pzbapps.squiggly.main_screen.presentation.screens.NotesScreen
import com.pzbapps.squiggly.notebook_main_screen.presentation.screen.AddNoteInNotebookScreen
import com.pzbapps.squiggly.notebook_main_screen.presentation.screen.NotebookMainScreen
import com.pzbapps.squiggly.notebook_main_screen.presentation.screen.bulletpointsscreen.BulletPointsNotebookMainScreen
import com.pzbapps.squiggly.notebook_main_screen.presentation.screen.checkboxscreen.CheckboxNoteBookMainScreen
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.BackupAndRestoreScreen
import com.pzbapps.squiggly.search_main_screen_feature.presentation.screens.SearchScreen
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.FeedbackScreen
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.PrivacyPolicy
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.ReportBugScreen
import com.pzbapps.squiggly.settings_feature.screen.presentation.screens.SettingsScreen
import com.pzbapps.squiggly.trash_bin_feature.presentation.screens.DeleteTrashScreen
import com.pzbapps.squiggly.trash_bin_feature.presentation.screens.TrashBinScreen


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    result: String,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>,
    noteId: MutableIntState
) {
    navigation(
        startDestination = Screens.HomeScreen.route,
        route = HOME_GRAPH
    ) {
        if (result == Constant.USER_VALUE) {
            composable(route = Screens.SplashScreen.route) {
                SplashScreen(result, navController)
            }
        }
        composable(Screens.HomeScreen.route) {
            NotesScreen(navController, viewModel, activity, selectedItem, selectedNote, noteId)
        }
        composable(
            Screens.AddNoteScreen.route
        ) {
            AddNoteScreen(navController, viewModel, activity)

        }
        composable(
            Screens.EditNoteScreen.route,
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            }, navArgument("screen") {
                type = NavType.StringType
            }, navArgument("query") {
                type = NavType.StringType
                defaultValue = ""
            }
            )) {
            EditNoteScreen(
                navHostController = navController,
                viewModel = viewModel,
                activity = activity,
                id = it.arguments!!.getInt("id")!!,
                screen = it.arguments!!.getString("screen")!!,
                noteId,
                query = it.arguments!!.getString("query")!!
            )
        }
        composable(route = Screens.SettingsScreen.route) {
            SettingsScreen(navController, activity, viewModel)
        }
        composable(Screens.ArchiveScreen.route) {
            ArchiveNotesScreen(
                navHostController = navController,
                viewModel = viewModel,
                activity = activity,
                selectedItem,
                selectedNote
            )
        }

        composable(
            Screens.SearchScreen.route,
            listOf(navArgument("screen") {
                type = NavType.StringType
            }, navArgument("queryText") {
                type = NavType.StringType
            })
        ) {
            SearchScreen(
                navHostController = navController,
                viewModel = viewModel,
                activity = activity,
                screen = it.arguments?.getString("screen")!!,
                queryText = it.arguments?.getString("queryText")!!
            )
        }

        composable(Screens.AboutUsScreen.route) {
            AboutUsScreen(activity)
        }

        composable(
            Screens.NotebookMainScreen.route,
            listOf(navArgument("title") {
                type = NavType.StringType
            })
        ) {
            NotebookMainScreen(
                navController,
                viewModel,
                activity,
                it.arguments?.getString("title")!!,
                selectedItem,
                selectedNote
            )
        }
        composable(Screens.CheckboxMainScreen.route) {
            CheckboxNoteMainScreen(navController, viewModel, activity)
        }
        composable(Screens.LockedNotesScreen.route) {
            LockedNotesScreen(
                navHostController = navController,
                viewModel = viewModel,
                activity = activity,
                selectedItem = selectedItem,
                selectedNote = selectedNote
            )
        }
        composable(Screens.BulletPointMainScreen.route) {
            BulletPointsNoteMainScreen(navController, viewModel, activity)
        }
        composable(Screens.TrashBinScreen.route) {
            TrashBinScreen(navController, viewModel, activity, selectedItem, selectedNote)
        }
        composable(Screens.DeleteTrashScreen.route, listOf(navArgument("id") {
            type = NavType.IntType
            defaultValue = 0
        })) {
            DeleteTrashScreen(navController, viewModel, activity, it.arguments?.getInt("id") ?: 0)
        }

        composable(Screens.AddNoteInNotebookScreen.route, listOf(navArgument("notebookName") {
            type = NavType.StringType
        })) {
            AddNoteInNotebookScreen(
                viewModel,
                navController,
                activity,
                it.arguments?.getString("notebookName")!!
            )
        }
        composable(Screens.AddNoteInLockedScreen.route) {
            AddNoteInLockedScreen(
                viewModel = viewModel,
                navHostController = navController,
                activity = activity
            )
        }

        composable(Screens.BackupAndRestoreScreen.route) {
            BackupAndRestoreScreen(navController, activity)
        }
        composable(Screens.CheckboxNotebookMainScreen.route, listOf(navArgument("notebook") {
            type = NavType.StringType
        })) {
            CheckboxNoteBookMainScreen(
                navHostController = navController,
                mainActivityViewModel = viewModel,
                activity = activity,
                it.arguments?.getString("notebook")!!
            )
        }
        composable(Screens.BulletPointsNotebook.route, listOf(navArgument("notebook") {
            type = NavType.StringType
        })) {
            BulletPointsNotebookMainScreen(
                navHostController = navController,
                mainActivityViewModel = viewModel,
                activity = activity,
                it.arguments?.getString("notebook")!!
            )
        }
        composable(Screens.CheckBoxLockedNotesMainScreen.route) {
            CheckboxLockedNotesMainScreen(
                navHostController = navController,
                mainActivityViewModel = viewModel,
                activity = activity
            )
        }
        composable(Screens.BulletPointsLockedNotesMainScreen.route) {
            BulletPointsLockedNotesMainScreen(
                navHostController = navController,
                mainActivityViewModel = viewModel,
                activity = activity
            )
        }
        composable(Screens.PrivacyPolicy.route) {
            PrivacyPolicy()
        }
        composable(Screens.FeedbackScreen.route) {
            FeedbackScreen()
        }
        composable(Screens.ReportBugScreen.route) {
            ReportBugScreen()
        }

    }
}
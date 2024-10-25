package com.pzbapps.squiggly.common.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.login_and_signup_feature.authGraph

@Composable
fun NavHost(
    navController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    result: String,
    selectedItem: MutableState<Int>,
    selectedNote: MutableState<Int>,
    noteId: MutableIntState
) {
    androidx.navigation.compose.NavHost(
        navController = navController,
        startDestination = HOME_GRAPH

    ) {
        authGraph(navHostController = navController, viewModel, activity, result)
        homeGraph(
            navController = navController,
            viewModel = viewModel,
            activity = activity,
            result,
            selectedItem,
            selectedNote,
            noteId
        )
    }
}
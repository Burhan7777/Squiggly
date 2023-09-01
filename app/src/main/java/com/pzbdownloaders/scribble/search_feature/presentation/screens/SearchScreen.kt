package com.pzbdownloaders.scribble.search_feature.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.pzbdownloaders.scribble.common.presentation.MainActivity
import com.pzbdownloaders.scribble.common.presentation.MainActivityViewModel
import com.pzbdownloaders.scribble.search_feature.presentation.components.MainStructureSearchScreen

@Composable
fun SearchScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity,
    screen: String
) {
    MainStructureSearchScreen(
        navHostController = navHostController,
        viewModel = viewModel,
        activity = activity,
        screen = screen
    )
}
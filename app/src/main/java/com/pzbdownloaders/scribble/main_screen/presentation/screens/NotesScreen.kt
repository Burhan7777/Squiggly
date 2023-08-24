package com.pzbdownloaders.scribble.main_screen.presentation.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.pzbdownloaders.scribble.common.presentation.MainActivity
import com.pzbdownloaders.scribble.common.presentation.MainActivityViewModel
import com.pzbdownloaders.scribble.main_screen.presentation.components.MainStructureMainScreen

@Composable
fun NotesScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel,
    activity: MainActivity
) {


    MainStructureMainScreen(navHostController, viewModel, activity)
}


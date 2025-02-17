package com.pzbapps.squiggly.search_main_screen_feature.presentation.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBarSearchScreen(
    navHostController: NavHostController,
    viewModel: MainActivityViewModel
) {
    var text by remember { mutableStateOf("") }
    var active by remember {
        mutableStateOf(false)
    }


    var scope = rememberCoroutineScope()
    val context = LocalContext.current
    SearchBar(
        query = text,
        onQueryChange = { text = it },
        onSearch = {
            active = !active
            if (text.isNotEmpty()) {
                navHostController.popBackStack()
                navHostController.navigate(
                    Screens.SearchScreen.searchNoteWIthScreen(
                        text,
                        Constant.HOME
                    )
                )
            } else {
                Toast.makeText(context, "Nothing to search", Toast.LENGTH_SHORT).show()
            }
        },
        active = active,
        onActiveChange = { active = !active },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        enabled = true,
        colors = SearchBarDefaults.colors(
            containerColor = MaterialTheme.colors.primaryVariant,
            dividerColor = MaterialTheme.colors.primary,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colors.onPrimary
            )
        ),
        tonalElevation = SearchBarDefaults.Elevation.plus(10.dp),
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    navHostController.popBackStack()
                },
                tint = MaterialTheme.colors.onPrimary
            )
        },
        placeholder = {
            Text(
                text = "Search notes",
                modifier = Modifier.alpha(.5f),
                color = MaterialTheme.colors.onPrimary
            )
        },

        ) {
    }
}
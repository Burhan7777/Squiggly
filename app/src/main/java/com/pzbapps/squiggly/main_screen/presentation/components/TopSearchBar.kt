package com.pzbapps.squiggly.main_screen.presentation.components

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSearchBar(
    navHostController: NavHostController,
    drawerState: DrawerState,
    viewModel: MainActivityViewModel,
    showGridOrLinearNotes: MutableState<Boolean>,
    activity: MainActivity,
    showOrderDialogBox: MutableState<Boolean>
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
                imageVector = Icons.Filled.Menu,
                contentDescription = "Menu",
                modifier = Modifier.clickable {
                    scope.launch {
                        drawerState.open()
                    }
                },
            )
        },
        trailingIcon = {
            Row() {
                IconButton(onClick = {
                    showGridOrLinearNotes.value = !showGridOrLinearNotes.value
                    val editor: SharedPreferences.Editor =
                        activity.getSharedPreferences(Constant.LIST_PREFERENCE, MODE_PRIVATE).edit()
                    editor.putBoolean(Constant.LIST_OR_GRID, showGridOrLinearNotes.value)
                    editor.apply()
                }) {
                    Icon(
                        imageVector = if (showGridOrLinearNotes.value) Icons.Filled.List else Icons.Filled.GridOn,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
                IconButton(onClick = { showOrderDialogBox.value = true }) {
                    Icon(
                        imageVector = Icons.Filled.Sort,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        },
        placeholder = {
            Text(
                text = "Search notes",
                modifier = Modifier.alpha(.5f),
            )
        },
    ) {
    }
}
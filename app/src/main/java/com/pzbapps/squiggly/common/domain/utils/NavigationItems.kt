package com.pzbapps.squiggly.common.domain.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Note
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItems(
    var label: String,
    var selectedIcon: ImageVector,
    var unSelectedIcon: ImageVector
) {
    companion object {
        var navigationItems = listOf(
            NavigationItems(
                label = "Notes",
                selectedIcon = Icons.Filled.Note,
                unSelectedIcon = Icons.Outlined.Note
            ),
            NavigationItems(
                label = "Archive",
                selectedIcon = Icons.Filled.Archive,
                unSelectedIcon = Icons.Outlined.Archive
            ),
            NavigationItems(
                label = "Locked",
                selectedIcon = Icons.Filled.Lock,
                unSelectedIcon = Icons.Outlined.Lock
            ),
            NavigationItems(
                label = "Trash Bin",
                selectedIcon = Icons.Filled.Delete,
                unSelectedIcon = Icons.Outlined.Delete
            ),
            NavigationItems(
                label = "Settings",
                selectedIcon = Icons.Filled.Settings,
                unSelectedIcon = Icons.Outlined.Settings
            ),
            NavigationItems(
                label = "Rate the app",
                selectedIcon = Icons.Filled.Star,
                unSelectedIcon = Icons.Outlined.Star
            ),
            NavigationItems(
                label = "About",
                selectedIcon = Icons.Filled.Info,
                unSelectedIcon = Icons.Outlined.Info
            )

        )
    }
}
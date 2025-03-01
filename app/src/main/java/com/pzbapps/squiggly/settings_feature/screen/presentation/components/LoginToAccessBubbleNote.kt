package com.pzbapps.squiggly.settings_feature.screen.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.Screens

@Composable
fun LoginToAccessBubbleNote(navHostController: NavHostController, onDismiss: () -> Unit) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = { onDismiss() },
        shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
        title = {
            Text(
                text ="Couldn't find the ID required to start the trial version. You need to login to start the trial version",
                fontSize = 15.sp,
                fontFamily = FontFamily.fontFamilyRegular,
                color = MaterialTheme.colors.onPrimary
            )
        }, confirmButton = {
            androidx.compose.material.Button(
                onClick = {
                    onDismiss()
                    navHostController.navigate(Screens.LoginScreen.route)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.onPrimary,
                    contentColor = androidx.compose.material.MaterialTheme.colors.primary
                ),
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                androidx.compose.material.Text(
                    text = "Log In",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        },
        dismissButton = {
            androidx.compose.material.OutlinedButton(
                onClick = {
                    onDismiss()
                },
                shape = androidx.compose.material.MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colors.onPrimary
                ),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material.MaterialTheme.colors.primaryVariant,
                    contentColor = androidx.compose.material.MaterialTheme.colors.onPrimary
                ),
            ) {
                androidx.compose.material.Text(
                    text = "Cancel",
                    fontFamily = FontFamily.fontFamilyRegular
                )
            }
        })
}
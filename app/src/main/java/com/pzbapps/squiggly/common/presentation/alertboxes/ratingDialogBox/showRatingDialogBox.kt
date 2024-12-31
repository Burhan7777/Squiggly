package com.pzbapps.squiggly.common.presentation.alertboxes.ratingDialogBox

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity

@Composable
fun ShowRatingDialogBox(activity: MainActivity, onDismiss: () -> Unit) {
    var scope = rememberCoroutineScope()
    androidx.compose.material3.AlertDialog(
        onDismissRequest = {

        },
        shape = MaterialTheme.shapes.medium.copy(
            topStart = CornerSize(15.dp),
            topEnd = CornerSize(15.dp),
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp),
        ),
        containerColor = MaterialTheme.colors.primaryVariant,
        /*      icon = {
                     Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
              }*/

        title = {
            Text(
                text = "Rate app 🙏",
                fontFamily = FontFamily.fontFamilyBold,
                fontSize = 20.sp,
                color = MaterialTheme.colors.onPrimary
            )
        },
        text = {
            Text(
                text = "Your support means everything to me!  " +
                        "By giving me a quick 5-star rating, you're " +
                        "helping me reach more users, " +
                        "which inspires me to keep improving and " +
                        "adding amazing features.It only takes 5 seconds." +
                        " Thank you so much 😊. ",

                fontFamily = FontFamily.fontFamilyRegular,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 14.sp
            )
        },
        confirmButton = {
            Button(
                onClick = {

                    val createSharedPreferences =
                        activity.getSharedPreferences(Constant.HIDE_RATING_DIALOG_BOX, Context.MODE_PRIVATE)
                            .edit()
                    createSharedPreferences.putBoolean(Constant.HIDE_RATING_DIALOG_BOX_KEY, true)
                    createSharedPreferences.apply()

                    try {
                        var intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.pzbapps.squiggly")
                        )
                        activity.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            activity,
                            "No app found to open this link",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary,
                    contentColor = MaterialTheme.colors.primary
                ),
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                )
            ) {
                Text(text = "Rate app", fontFamily = FontFamily.fontFamilyRegular)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = {
                    val sharedPreference = activity.getSharedPreferences(
                        Constant.SHOW_RATING_DIALOG_BOX,
                        Context.MODE_PRIVATE
                    )

                    var value = sharedPreference.getInt(Constant.SHOW_RATING_DIALOG_BOX_KEY, 0)
                    var editSF = sharedPreference.edit()
                    editSF.putInt(Constant.SHOW_RATING_DIALOG_BOX_KEY, ++value)
                    editSF.apply()

                    onDismiss()
                },
                shape = MaterialTheme.shapes.medium.copy(
                    topStart = CornerSize(15.dp),
                    topEnd = CornerSize(15.dp),
                    bottomStart = CornerSize(15.dp),
                    bottomEnd = CornerSize(15.dp),
                ),
                border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.primaryVariant,
                    contentColor = MaterialTheme.colors.onPrimary
                ),
            ) {

                Text(text = "Remind me later", fontFamily = FontFamily.fontFamilyRegular)
            }
        }
    )
}
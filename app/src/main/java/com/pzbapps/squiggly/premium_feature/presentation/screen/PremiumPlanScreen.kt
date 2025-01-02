package com.pzbapps.squiggly.premium_feature.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.ui.theme.premiumColor

@Composable
fun PremiumPlan(navHostController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            var string = buildAnnotatedString {
                withStyle(
                    MaterialTheme.typography.h6.toSpanStyle().copy(
                        color = MaterialTheme.colors.onPrimary,
                        fontFamily = FontFamily.fontFamilyRegular,
                        fontSize = 35.sp
                    )
                ) {
                    append("Go ")
                }
                withStyle(
                    MaterialTheme.typography.h6.toSpanStyle()
                        .copy(
                            color = premiumColor,
                            fontFamily = FontFamily.fontFamilyRegular,
                            fontSize = 35.sp
                        )
                ) {
                    append("Premium")
                }
            }
            Text(
                string,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { navHostController.navigateUp() },
                modifier = Modifier.background(
                    color = MaterialTheme.colors.onPrimary,
                    shape = CircleShape
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Close",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }


        Spacer(modifier = Modifier.height(20.dp))

        Feature("Ad free experience", painterResource(R.drawable.ad_free))
        Feature("Scan documents to notes", painterResource(R.drawable.ic_scanner))
        Feature("Share notes as PDF", painterResource(R.drawable.ic_share))
        Feature("Share notes as DOCX", painterResource(R.drawable.ic_share))
        Feature("Cloud Backup every 24 hours", painterResource(R.drawable.ic_cloud_backup))
        Feature("Voice to text feature", painterResource(R.drawable.ic_voice))
        Feature("Exclusive premium batch", painterResource(R.drawable.ic_batch))
        Feature("Support my 6 months of development", painterResource(R.drawable.ic_support))

        Spacer(modifier = Modifier.height(10.dp))

        Card(
            onClick = {

            },
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(15.dp),
                topEnd = CornerSize(15.dp),
                bottomEnd = CornerSize(15.dp),
                bottomStart = CornerSize(15.dp),
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colors.primary
            ),
            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Monthly billed",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyBold,
                    fontSize = 25.sp
                )
                Text(
                    "₹ 19.00",
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyLight,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),

            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(15.dp),
                topEnd = CornerSize(15.dp),
                bottomStart = CornerSize(15.dp),
                bottomEnd = CornerSize(15.dp),
            ),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onPrimary
            ),
        ) {
            androidx.compose.material3.Text(
                text = "Subscribe",
                fontSize = 15.sp,
                color = MaterialTheme.colors.onSecondary,
                fontFamily = FontFamily.fontFamilyRegular
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            "You will be charged ₹ 19.00 every month ",
            color = MaterialTheme.colors.onPrimary,
            fontFamily = FontFamily.fontFamilyRegular,
            textAlign = TextAlign.Center
        )

        TextButton(onClick = {

        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Cancel Subscription",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = FontFamily.fontFamilyBold,
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}


@Composable
fun Feature(feature: String, icon: Painter) {
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = "Premium features",
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                feature,
                color = MaterialTheme.colors.onPrimary,
                fontFamily = FontFamily.fontFamilyRegular
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}
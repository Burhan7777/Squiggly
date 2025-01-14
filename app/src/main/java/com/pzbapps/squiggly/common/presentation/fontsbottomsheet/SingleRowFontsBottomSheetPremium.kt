package com.pzbapps.squiggly.common.presentation.fontsbottomsheet

import androidx.compose.material.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.pzbapps.squiggly.R

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.mohamedrejeb.richeditor.model.RichTextState
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens

@Composable
fun SingleRowFontsBottomSheetPremium(
    font: FontFamily,
    fontFamily: MutableState<FontFamily>,
    showBottomSheet: MutableState<Boolean>,
    viewModel: MainActivityViewModel,
    navHostController: NavHostController
) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(100.dp)
            .height(50.dp)
            .clickable {
                if (viewModel.ifUserIsPremium.value) {
                    fontFamily.value = font
                    showBottomSheet.value = false
                }else{
                    navHostController.navigate(Screens.PremiumPlanScreen.route)
                }
            }, shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        )
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                "Squiggly",
                fontFamily = font,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(R.drawable.ic_premium),
                contentDescription = "premium font",
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .padding(horizontal = 5.dp, vertical = 5.dp)
            )
        }
    }
}
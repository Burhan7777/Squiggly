package com.pzbapps.squiggly.main_screen.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.common.presentation.Screens
import com.pzbapps.squiggly.main_screen.domain.usecase.getRemainingDaysFromFirebase
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.products.QProduct
import com.qonversion.android.sdk.listeners.QonversionProductsCallback
import kotlin.math.cos


@Composable
fun ShowPremiumBar(activity: MainActivity, navHostController: NavHostController) {
    var cost = remember { mutableStateOf("") }
    Qonversion.shared.products(callback = object : QonversionProductsCallback {
        override fun onError(error: QonversionError) {
            TODO("Not yet implemented")
        }

        override fun onSuccess(products: Map<String, QProduct>) {
            // handle available products here
            var product = products["1_month_premium"]
            cost.value = product?.prettyPrice.toString()
        }
    })

    Box(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colors.onPrimary,
                        MaterialTheme.colors.onSecondary
                    )
                )
            )
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                text = "Buy premium and \nunlock all features",
                fontFamily = FontFamily.fontFamilyRegular,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 10.dp),
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = "${cost.value} / month",
                fontFamily = FontFamily.fontFamilyRegular,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 10.dp),
                color = MaterialTheme.colors.onSecondary
            )
        }
        OutlinedButton(
            onClick = { navHostController.navigate(Screens.PremiumPlanScreen.route) },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(15.dp),
            border = BorderStroke(1.dp, MaterialTheme.colors.onPrimary),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Buy Premium",
                fontFamily = FontFamily.fontFamilyBold,
                color = Color.Black
            )
        }
    }
}
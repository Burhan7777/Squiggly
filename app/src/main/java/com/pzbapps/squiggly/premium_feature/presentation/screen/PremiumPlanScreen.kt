package com.pzbapps.squiggly.premium_feature.presentation.screen

import android.os.Bundle
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pzbapps.squiggly.R
import com.pzbapps.squiggly.common.domain.utils.Constant
import com.pzbapps.squiggly.common.presentation.FontFamily
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.pzbapps.squiggly.ui.theme.premiumColor
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.QonversionErrorCode
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.dto.offerings.QOfferings
import com.qonversion.android.sdk.dto.products.QProduct
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import com.qonversion.android.sdk.listeners.QonversionOfferingsCallback
import com.qonversion.android.sdk.listeners.QonversionProductsCallback

@Composable
fun PremiumPlan(
    activity: MainActivity,
    navHostController: NavHostController,
    viewModel: MainActivityViewModel
) {

    var analytics = Firebase.analytics
    var bundle = Bundle()
    bundle.putString("premium_screen_started", "premium_screen_started")
    analytics.logEvent("premium_screen_started", bundle)


    var productPrice = remember { mutableStateOf("") }
    Qonversion.shared.products(callback = object : QonversionProductsCallback {
        override fun onSuccess(products: Map<String, QProduct>) {
            // handle available products here
            var product = products["1_month_premium"]
            productPrice.value = product?.prettyPrice ?: ""
        }

        override fun onError(error: QonversionError) {
            TODO("Not yet implemented")
        }
    })


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
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
        Feature("Premium fonts", painterResource(R.drawable.ic_font))
        Feature("Exclusive animated premium batch", painterResource(R.drawable.ic_batch))
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
                    productPrice.value,
                    color = MaterialTheme.colors.onPrimary,
                    fontFamily = FontFamily.fontFamilyLight,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                var analytics = Firebase.analytics
                var bundle = Bundle()
                bundle.putString("subscription_button_pressed", "subscription_button_pressed")
                analytics.logEvent("subscription_button_pressed", bundle)
                Qonversion.shared.products(callback = object : QonversionProductsCallback {
                    override fun onSuccess(products: Map<String, QProduct>) {
                        // handle available products here
                        var product = products["1_month_premium"]
                        if (product != null) {
                            Qonversion.shared.purchase(activity, product, callback = object :
                                QonversionEntitlementsCallback {
                                override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                                    val premiumEntitlement = entitlements["monhtlypremium"]
                                    if (premiumEntitlement != null && premiumEntitlement.isActive) {
                                        // Handle active entitlement here
                                        Toast.makeText(
                                            activity,
                                            "Subscription successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.ifUserIsPremium.value = true

                                    }
                                }

                                override fun onError(error: QonversionError) {
                                    // Handle error here
                                    if (error.code === QonversionErrorCode.PurchaseCanceled) {
                                        // Purchase canceled by the user
                                        Toast.makeText(
                                            activity,
                                            "Purchase canceled",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            })
                        }
                    }

                    override fun onError(error: QonversionError) {
                        // handle error here
                    }
                })

                Qonversion.shared.offerings(object : QonversionOfferingsCallback {
                    override fun onSuccess(offerings: QOfferings) {
                        val mainOffering = offerings.main
                        println(mainOffering?.products)
                        if (mainOffering != null && mainOffering.products.isNotEmpty()) {
                            // Display products for sale
                            val product = mainOffering.products[0]
                            println(product.trialPeriod)
                            println(product.prettyPrice)

                        }
                    }

                    override fun onError(error: QonversionError) {
                        // handle error here
                        Toast.makeText(activity, "error", Toast.LENGTH_SHORT).show()
                    }
                })

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
            "You will be charged ${productPrice.value} every month ",
            color = MaterialTheme.colors.onPrimary,
            fontFamily = FontFamily.fontFamilyRegular,
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(onClick = {

        }, modifier = Modifier.fillMaxWidth()) {
            Text(
                "Cancel Subscription",
                color = MaterialTheme.colors.onPrimary,
                fontFamily = FontFamily.fontFamilyExtraLight,
                textDecoration = TextDecoration.Underline,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }


    }
}


@Composable
fun Feature(
    feature: String, icon: Painter
) {
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
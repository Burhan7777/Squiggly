package com.pzbapps.squiggly.common.domain.utils

import android.app.Activity.MODE_PRIVATE
import android.widget.Toast
import com.pzbapps.squiggly.common.presentation.MainActivity
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.dto.entitlements.QEntitlementRenewState
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback

fun checkIfUserIsPremium(viewModel: MainActivityViewModel, activity: MainActivity) {
    Qonversion.shared.checkEntitlements(object : QonversionEntitlementsCallback {
        override fun onSuccess(entitlements: Map<String, QEntitlement>) {
            val premiumEntitlement = entitlements["monhtlypremium"]
            if (premiumEntitlement != null && premiumEntitlement.isActive) {
                viewModel.ifUserIsPremium.value = true

                when (premiumEntitlement.renewState) {
                    QEntitlementRenewState.NonRenewable -> {
                        // NonRenewable is the state of a consumable or non-consumable in-app purchase
                    }

                    QEntitlementRenewState.WillRenew -> {
                        // WillRenew is the state of an auto-renewable subscription
                    }

                    QEntitlementRenewState.BillingIssue -> {
                        // Prompt the user to update the payment method.
                    }

                    QEntitlementRenewState.Canceled -> {
                        // The user has turned off auto-renewal for the subscription, but the subscription has not expired yet.
                        // Prompt the user to resubscribe with a special offer.
                    }

                    else -> {

                    }
                }
            } else {

                val prefsEvery24Hours =
                    activity.getSharedPreferences(
                        Constant.AUTO_SAVE_PREF_EVERY_24_HOURS,
                        MODE_PRIVATE
                    ).edit()
                prefsEvery24Hours.putBoolean(Constant.AUTO_SAVE_KEY_EVERY_24_HOURS, false)
                prefsEvery24Hours.apply()

//                val prefsEvery72Hours =
//                    activity.getSharedPreferences(
//                        Constant.AUTO_SAVE_PREF,
//                        MODE_PRIVATE
//                    ).edit()
//                i
//                prefsEvery72Hours.putBoolean(Constant.AUTO_SAVE_KEY, true)
//                prefsEvery72Hours.apply()
            }
        }

        override fun onError(error: QonversionError) {
            // handle error here
        }
    })
}
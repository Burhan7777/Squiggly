package com.pzbapps.squiggly.common.domain.utils

import android.widget.Toast
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.dto.entitlements.QEntitlement
import com.qonversion.android.sdk.dto.entitlements.QEntitlementRenewState
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback

fun checkIfUserIsPremium(viewModel: MainActivityViewModel) {
    Qonversion.shared.checkEntitlements(object : QonversionEntitlementsCallback {
        override fun onSuccess(entitlements: Map<String, QEntitlement>) {
            val premiumEntitlement = entitlements["monhtlypremium"]
            if (premiumEntitlement != null && premiumEntitlement.isActive) {
                viewModel.ifUserIsPremium.value = true
                println(viewModel.ifUserIsPremium.value)
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
            }
        }

        override fun onError(error: QonversionError) {
            // handle error here
        }
    })
}
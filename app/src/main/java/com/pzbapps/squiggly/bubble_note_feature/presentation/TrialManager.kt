package com.pzbapps.squiggly.bubble_note_feature.presentation

import android.content.Context
import android.provider.Settings
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import com.pzbapps.squiggly.common.presentation.MainActivityViewModel


/**
 * Flow of the checkTrialStatus:
 * First we check if the user has the android ID, if not we send back the trial status with requiresLogin = true.
 * If android ID exists then we check if the trial has been started or not by checking if the sharedPrefs exists or not.
 * If it does not exists we create the sharedPrefs by encrypting the start date and android ID. Then we return the trial status with isActive = true.
 * If the sharedPrefs already exists we we  decrypt the data and check how much time we have remaining in the trial then we return the TrialsStatus based on that
 * If we face some exception while decrypting we start the trial again with new sharedPrefs.
 */


class SimpleTrialManager(private val context: Context, private val viewModel: MainActivityViewModel) {
    private val prefs = context.getSharedPreferences("trial_prefs", Context.MODE_PRIVATE)
    private val TRIAL_DATA_KEY = "trial_data"
    private val TRIAL_DURATION_DAYS = 30L

    data class TrialStatus(
        val isActive: Boolean,
        val startDate: Date?,
        val daysRemaining: Int,
        val requiresLogin: Boolean
    )

    private fun getAndroidId(): String? {
        return try {
            val androidId = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
            // Check if ID is valid (not null or known problematic value)
            if (!androidId.isNullOrEmpty() && androidId != "9774d56d682e549c") {
                androidId
            } else null
        } catch (e: Exception) {
            null
        }
    }

    private fun encryptData(data: String, deviceId: String): String {
        val key = SecretKeySpec(deviceId.take(16).padEnd(16, '0').toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return Base64.encodeToString(cipher.doFinal(data.toByteArray()), Base64.DEFAULT)
    }

    private fun decryptData(encryptedData: String, deviceId: String): String {
        val key = SecretKeySpec(deviceId.take(16).padEnd(16, '0').toByteArray(), "AES")
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return String(cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT)))
    }

    fun checkTrialStatus(): TrialStatus {
        val androidId = getAndroidId()

        // If no valid Android ID, require login
        if (androidId == null) {
            return TrialStatus(
                isActive = false,
                startDate = null,
                daysRemaining = 0,
                requiresLogin = true
            )
        }

        val encryptedData = prefs.getString(TRIAL_DATA_KEY, null)

        if (encryptedData == null) {
            // First time user - start trial
            val startDate = Date()
            startTrial(startDate, androidId)
            return TrialStatus(
                isActive = true,
                startDate = startDate,
                daysRemaining = TRIAL_DURATION_DAYS.toInt(),
                requiresLogin = false
            )
        }

        try {
            val decryptedData = decryptData(encryptedData, androidId)
            val startDateMillis = decryptedData.toLong()
            val startDate = Date(startDateMillis)

            val currentDate = Date()
            val diffInMillis = currentDate.time - startDate.time
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)
            val daysRemaining = (TRIAL_DURATION_DAYS - diffInDays).toInt()

            return TrialStatus(
                isActive = diffInDays < TRIAL_DURATION_DAYS,
                startDate = startDate,
                daysRemaining = daysRemaining.coerceAtLeast(0),
                requiresLogin = false
            )
        } catch (e: Exception) {
            // If decryption fails, start fresh trial
            val startDate = Date()
            startTrial(startDate, androidId)
            return TrialStatus(
                isActive = true,
                startDate = startDate,
                daysRemaining = TRIAL_DURATION_DAYS.toInt(),
                requiresLogin = false
            )
        }
    }

    private fun startTrial(startDate: Date, deviceId: String) {
        val encryptedData = encryptData(startDate.time.toString(), deviceId)
        prefs.edit()
            .putString(TRIAL_DATA_KEY, encryptedData)
            .apply()
    }

    fun isPremiumOrTrialActive(): Boolean {
        // First check if user has premium via Qonversion
        val isPremium = viewModel.ifUserIsPremium.value
        if (isPremium) return true

        // If not premium, check trial status
        val trialStatus = checkTrialStatus()
        return trialStatus.isActive
    }
}


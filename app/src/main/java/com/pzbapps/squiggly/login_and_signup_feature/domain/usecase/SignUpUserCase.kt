package com.pzbapps.squiggly.login_and_signup_feature.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.pzbapps.squiggly.common.domain.utils.Constant

class SignUpUserCase {

    fun signUpUser(email: String, password: String): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.value = Constant.SUCCESS
                } else {
                    result.value = task.exception?.localizedMessage
                }
            }
            .addOnFailureListener {
                result.value = it.localizedMessage
            }
        return result

    }
}
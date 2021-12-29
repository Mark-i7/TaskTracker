package com.example.trelloclone.ui.registration

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.trelloclone.utils.AppLevelFunctions.Companion.showToast

class SignUpViewModel(): ViewModel() {

    fun validateForm(name: String, email: String, password: String, context: Context): Boolean{
        return when {
            TextUtils.isEmpty(name) -> {
                showToast("Please enter a name", context)
                false
            }

            !isEmailValid(email) -> {
                showToast("The email is not valid!", context)
                false
            }

            TextUtils.isEmpty(email) -> {
                showToast("Please enter an email", context)
                false
            }


            !isPasswordValid(password) -> {
                showToast("The password is not valid!", context)
                false
            }

            TextUtils.isEmpty(password) -> {
                showToast("Please enter a password", context)
                false
            }
            else -> {
                true
            }
        }
    }


    private fun isEmailValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}
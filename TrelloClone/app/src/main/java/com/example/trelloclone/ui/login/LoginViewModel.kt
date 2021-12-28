package com.example.trelloclone.ui.login

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.ViewModel
import android.util.Patterns
import android.widget.Toast

class LoginViewModel() : ViewModel() {

    fun validateForm(email: String, password: String, context: Context): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(context, "Please enter an email", Toast.LENGTH_SHORT)
                false
            }

            TextUtils.isEmpty(password) -> {
                Toast.makeText(context, "Please enter a password", Toast.LENGTH_SHORT)
                false
            }

            else -> {
                true
            }
        }
    }
}
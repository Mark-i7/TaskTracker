package com.example.trelloclone.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.trelloclone.R
import com.google.firebase.auth.FirebaseAuth

class AppLevelFunctions {
    companion object {
        fun showToast(message: String, context: Context) {
            val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast.view?.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.snackbar_error_color
                )
            )
            toast.show()
        }

        fun getCurrentUserID(): String {
            return FirebaseAuth.getInstance().currentUser!!.uid
        }
    }
}
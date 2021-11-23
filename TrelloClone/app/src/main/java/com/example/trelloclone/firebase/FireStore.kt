package com.example.trelloclone.firebase

import android.util.Log
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.registration.SignUpFragment
import com.example.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class Firestore {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: SignUpFragment, userInfo: User) {
        mFireStore
            .collection(Constants.USERS)
            .document(getCurrentUserId())
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                fragment.userRegisteredSuccess()
            }.addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error writing documents")
            }
    }

    private fun getCurrentUserId(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
}

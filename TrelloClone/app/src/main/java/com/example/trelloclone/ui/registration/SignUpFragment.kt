package com.example.trelloclone.ui.registration

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentHomeBinding
import com.example.trelloclone.databinding.FragmentSignupBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.User
import com.example.trelloclone.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment: Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var mProgressDialog: Dialog
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun getCurrentUserID(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnSignUp.setOnClickListener {
            registerUser()
        }

        return  root
    }

    private fun registerUser(){
        name = binding.etName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if(validateForm(name,email,password)){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if(task.isSuccessful){
                        val firebaseUser = task.result!!.user!!
                        val registeredEmail =  firebaseUser.email!!
                        val user = User(firebaseUser.uid, name, email)
                        Firestore().registerUser(this, user)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString())
                    }
                }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean{
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }

            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                false
            }

            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> {
                true
            }
        }
    }

    fun userRegisteredSuccess() {
        Toast.makeText(requireContext(), "You have successfully registered",Toast.LENGTH_SHORT).show()
        hideProgressDialog()
        //finish()
    }

    fun showErrorSnackBar(message: String){
        //val snackBar = Snackbar.make(R.id.content, message, Snackbar.LENGTH_LONG)
        //val snackBarView = snackBar.view
        //snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackbar_error_color))
        //snackBar.show()
    }
}

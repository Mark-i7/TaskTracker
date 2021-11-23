package com.example.trelloclone.ui.registration

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentSignupBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.AppLevelFunctions.Companion.showToast
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment: Fragment() {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!
    private lateinit var signUpViewModel: SignUpViewModel

    private lateinit var mProgressDialog: Dialog
    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    fun showProgressDialog() {
        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnSignup.setOnClickListener {
            registerUser()
        }

        return  root
    }

    private fun registerUser(){
        name = binding.etName.text.toString().trim()
        email = binding.etEmail.text.toString().trim()
        password = binding.etPassword.text.toString().trim()

        if(signUpViewModel.validateForm(name, email, password, requireContext())){
            showProgressDialog()
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
                        showToast(task.exception!!.message.toString(), requireContext())
                    }
                }
        }
    }

    fun userRegisteredSuccess() {
        Toast.makeText(requireContext(), "You have successfully registered",Toast.LENGTH_SHORT).show()
        hideProgressDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

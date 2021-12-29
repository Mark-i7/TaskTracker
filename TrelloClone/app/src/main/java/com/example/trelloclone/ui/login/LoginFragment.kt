package com.example.trelloclone.ui.login

import android.app.Dialog
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentLoginBinding
import com.example.trelloclone.ui.progressdialog.ProgressDialog
import com.example.trelloclone.utils.AppLevelFunctions.Companion.showToast
import com.example.trelloclone.utils.Constants
import com.google.firebase.auth.FirebaseAuth


class LoginFragment() : Fragment(), ProgressDialog {
    override lateinit var mProgressDialog: Dialog
    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var loginBtn: Button

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()

        email = binding.email
        password = binding.password
        loginBtn = binding.btnLogin

        loginBtn.setOnClickListener {
            signInUser(email.text.toString().trim(), password.text.toString().trim())
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        return root
    }

    private fun signInUser(email: String, password: String) {
        if (loginViewModel.validateForm(email, password, requireContext())) {
            showProgressDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()
                    if (task.isSuccessful) {
                        showToast("Authentication success!", requireContext())
                        findNavController().navigate(R.id.action_loginFragment_to_nav_home)
                    } else {
                        showToast(task.exception!!.message.toString(), requireContext())
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showProgressDialog() {
        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
    }

    override fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}
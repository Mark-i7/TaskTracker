package com.example.trelloclone.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.trelloclone.MainActivity
import com.example.trelloclone.R
import com.example.trelloclone.databinding.FragmentCreateBoardBinding
import com.example.trelloclone.databinding.FragmentProfileBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.AppLevelFunctions
import com.google.common.io.Files
import com.google.firebase.storage.FirebaseStorage

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    private var mUserName: String = ""
    private var mUserEmail: String = ""
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var photoImageView: ImageView
    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        view?.apply{
//            nameTextView = findViewById(R.id.et_name)
//            emailTextView = findViewById(R.id.et_email)
//            photoImageView = findViewById(R.id.profilePic)
//        }

        photoImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getImage.launch("image/*")
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        initializeElements()
        //setListeners()
        return root
        //return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    private fun initializeElements() {
        nameTextView = binding.etName
        emailTextView = binding.etEmail
        photoImageView=binding.profilePic

    }
    fun updateNavigationUserDetails(user: User?) {
        if(user != null){
            if(user.name != null && user.name != ""){
                mUserName = user.name
                mUserEmail = user.email.toString()
                nameTextView.text = mUserName
                emailTextView.text = mUserEmail
            }
            if(mProfileImageURL.isNotEmpty() && mProfileImageURL != user.image){
                user.image = mProfileImageURL
            }
            Glide
                .with(this)
                .load(user.image)
                .circleCrop()
                .placeholder(R.drawable.ic_circle_profile)
                .into(photoImageView)
        }
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        Glide
            .with(this)
            .load(it)
            .circleCrop()
            .placeholder(R.drawable.ic_circle_profile)
            .into(photoImageView)
        mSelectedImageFileUri = it
        uploadUserImages()
        Firestore().updateUserPhoto(mSelectedImageFileUri.toString())
        Firestore().loadUserData(this)
    }

    private fun uploadUserImages() {
        if (mSelectedImageFileUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + Files.getFileExtension(
                    mSelectedImageFileUri.toString()
                )
            )

            storageRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener {
                        taskSnapshot ->
                    Log.i("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                    taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                            uri ->
                        Log.i("Downloadable Image File", uri.toString())
                        mProfileImageURL = uri.toString()

                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Upload error", exception.message.toString())
                    AppLevelFunctions.showToast(exception.message.toString(), requireContext())
                }
        }
    }

    companion object {
        private const val PICK_IMAGE = 1000
        private const val PERMISSION_CODE = 1001
    }

}
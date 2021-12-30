package com.example.trelloclone

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.trelloclone.databinding.ActivityMainBinding
import com.example.trelloclone.firebase.Firestore
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.AppLevelFunctions
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.common.io.Files.getFileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mUserName: String = ""
    private var mUserEmail: String = ""
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var photoImageView: ImageView
    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val header: View = navView.getHeaderView(0)
        nameTextView = header.findViewById(R.id.tv_user_name)
        emailTextView = header.findViewById(R.id.tv_user_mail)
        photoImageView = header.findViewById(R.id.nav_user_image)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        photoImageView.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                   READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getImage.launch("image/*")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_EXTERNAL_STORAGE),
                    1
                )
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, SplashActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
                .centerCrop()
                .placeholder(R.drawable.ic_circle_profile)
                .into(photoImageView)
        }
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {
        photoImageView.setImageURI(it)
        mSelectedImageFileUri = it
        uploadUserImages()
        Firestore().updateUserPhoto(mSelectedImageFileUri.toString())
        Firestore().loadUserData(this)
    }

    private fun uploadUserImages() {
        if (mSelectedImageFileUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis() + "." + getFileExtension(
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
                    AppLevelFunctions.showToast(exception.message.toString(), this)
                }
        }
    }
}
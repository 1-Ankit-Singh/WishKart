package com.androidproject.wishkart.auth

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.databinding.ActivitySignUpBinding
import com.androidproject.wishkart.model.IndividualUser
import com.androidproject.wishkart.model.NGOUser
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

const val USER_PHONE_NUMBER = "phoneNumber"
const val USER_TYPE_SIGNUP = "userType"

class SignUpActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var signUpActivity: ActivitySignUpBinding
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private lateinit var userType: String
    private lateinit var phoneNumber: String
    private var userCertificateUrl: String = ""
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivity = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpActivity.root)

        userType = intent.getStringExtra(USER_TYPE_SIGNUP).toString()
        phoneNumber = intent.getStringExtra(USER_PHONE_NUMBER).toString()
        signUpActivity.userPhoneNumber.text = phoneNumber

        if (userType == "NGO") {
            signUpActivity.userCertificate.visibility = View.VISIBLE
            signUpActivity.userCertificateImage.visibility = View.VISIBLE
            signUpActivity.userCertificateText.visibility = View.VISIBLE
        }

        signUpActivity.userCertificateImage.setOnClickListener {
            checkPermissionForImage()
        }
        signUpActivity.userCertificateText.setOnClickListener {
            checkPermissionForImage()
        }

        signUpActivity.next.setOnClickListener {
            signUpActivity.next.isEnabled = false
            if (userType == "Individual") {
                uploadIndividualUserData()
            }
            if (userType == "NGO") {
                uploadNGOData()
            }
        }
    }

    private fun uploadNGOData() {
        val userName = signUpActivity.userName.text.toString()
        val userStreetAddress = signUpActivity.userStreetAddress.text.toString()
        val userCity = signUpActivity.userCity.text.toString()
        val userPincode = signUpActivity.userPinCode.text.toString()
        val userCountry = signUpActivity.userCountry.text.toString()
        val userCertificateNumber = signUpActivity.userCertificate.text.toString()

        when {
            userName.isEmpty() -> {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
            }
            userStreetAddress.isEmpty() -> {
                Toast.makeText(this, "Please enter your street address.", Toast.LENGTH_SHORT).show()
            }
            userCity.isEmpty() -> {
                Toast.makeText(this, "Please enter your city name.", Toast.LENGTH_SHORT).show()
            }
            userPincode.isEmpty() -> {
                Toast.makeText(this, "Please enter your PinCode.", Toast.LENGTH_SHORT).show()
            }
            userCountry.isEmpty() -> {
                Toast.makeText(this, "Please enter your country name.", Toast.LENGTH_SHORT).show()
            }
            userCertificateNumber.isEmpty() -> {
                Toast.makeText(this, "Please enter NGO Certificate Number.", Toast.LENGTH_SHORT)
                    .show()
            }
            (userCertificateUrl.isEmpty()) -> {
                Toast.makeText(this, "Please upload NGO Certificate Picture.", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                val ngoUser = NGOUser(
                    auth.uid!!,
                    userType,
                    userName,
                    phoneNumber,
                    userStreetAddress,
                    userCity,
                    userPincode,
                    userCountry,
                    userCertificateUrl,
                    userCertificateNumber
                )
                database.collection("users").document(auth.uid.toString()).set(ngoUser)
                    .addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Something went wrong, Please try again !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun uploadIndividualUserData() {
        val userName = signUpActivity.userName.text.toString()
        val userStreetAddress = signUpActivity.userStreetAddress.text.toString()
        val userCity = signUpActivity.userCity.text.toString()
        val userPincode = signUpActivity.userPinCode.text.toString()
        val userCountry = signUpActivity.userCountry.text.toString()

        when {
            userName.isEmpty() -> {
                Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show()
            }
            userStreetAddress.isEmpty() -> {
                Toast.makeText(this, "Please enter your street address.", Toast.LENGTH_SHORT).show()
            }
            userCity.isEmpty() -> {
                Toast.makeText(this, "Please enter your city name.", Toast.LENGTH_SHORT).show()
            }
            userPincode.isEmpty() -> {
                Toast.makeText(this, "Please enter your PinCode.", Toast.LENGTH_SHORT).show()
            }
            userCountry.isEmpty() -> {
                Toast.makeText(this, "Please enter your country name.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val individualUser = IndividualUser(
                    auth.uid!!,
                    userType,
                    userName,
                    phoneNumber,
                    userStreetAddress,
                    userCity,
                    userPincode,
                    userCountry,
                )
                database.collection("users").document(auth.uid.toString()).set(individualUser)
                    .addOnSuccessListener {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Something went wrong, Please try again !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun checkPermissionForImage() {
        if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(
                permission,
                1001
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            requestPermissions(
                permissionWrite,
                1002
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            Toast.makeText(
                this,
                "Please click on the certificate icon again to upload the certificate.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            1000
        ) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            data?.data?.let {
                signUpActivity.userCertificateImage.setImageURI(it)
                startUpload(it)
            }
        }
    }

    private fun startUpload(filePath: Uri) {
        progressDialog = createProgressDialog()
        progressDialog.show()
        val ref = storage.reference.child("NGOCertificates/" + auth.uid.toString())
        val uploadTask = ref.putFile(filePath)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userCertificateUrl = task.result.toString()
                progressDialog.dismiss()
            } else {
                // Handle failures
                Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onBackPressed() {}

    private fun createProgressDialog(): ProgressDialog {
        return ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage("Uploading Image...")
        }
    }
}
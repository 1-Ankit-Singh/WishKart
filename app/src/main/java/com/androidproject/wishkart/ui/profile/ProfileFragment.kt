package com.androidproject.wishkart.ui.profile

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.R
import com.androidproject.wishkart.auth.LoginActivity
import com.androidproject.wishkart.databinding.FragmentProfileBinding
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    // Initializing Variables
    private lateinit var profileBinding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var ref: DocumentReference = database.collection("users").document(auth.uid!!)
    private var editable: Boolean = false
    private lateinit var userType: String
    private lateinit var userName: String
    private lateinit var phoneNumber: String
    private lateinit var userStreetAddress: String
    private lateinit var userCity: String
    private lateinit var userPinCode: String
    private lateinit var userCountry: String
    private lateinit var userCertificateUrl: String
    private lateinit var userCertificateNumber: String
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater)

        checkUserType()

        profileBinding.edit.setOnClickListener {
            editable = true

            profileBinding.userStreetAddress.isClickable = true
            profileBinding.userStreetAddress.isFocusableInTouchMode = true
            profileBinding.userStreetAddress.isFocusable = true

            profileBinding.userCity.isClickable = true
            profileBinding.userCity.isFocusableInTouchMode = true
            profileBinding.userCity.isFocusable = true

            profileBinding.userPinCode.isClickable = true
            profileBinding.userPinCode.isFocusableInTouchMode = true
            profileBinding.userPinCode.isFocusable = true

            profileBinding.userCountry.isClickable = true
            profileBinding.userCountry.isFocusableInTouchMode = true
            profileBinding.userCountry.isFocusable = true

            profileBinding.userCertificate.isClickable = true
            profileBinding.userCertificate.isFocusableInTouchMode = true
            profileBinding.userCertificate.isFocusable = true

            profileBinding.cancelBtn.visibility = View.VISIBLE
            profileBinding.submitBtn.visibility = View.VISIBLE
        }

        profileBinding.cancelBtn.setOnClickListener {
            checkUserType()
            editable = false

            profileBinding.userStreetAddress.isClickable = false
            profileBinding.userStreetAddress.isFocusableInTouchMode = false
            profileBinding.userStreetAddress.isFocusable = false

            profileBinding.userCity.isClickable = false
            profileBinding.userCity.isFocusableInTouchMode = false
            profileBinding.userCity.isFocusable = false

            profileBinding.userPinCode.isClickable = false
            profileBinding.userPinCode.isFocusableInTouchMode = false
            profileBinding.userPinCode.isFocusable = false

            profileBinding.userCountry.isClickable = false
            profileBinding.userCountry.isFocusableInTouchMode = false
            profileBinding.userCountry.isFocusable = false

            profileBinding.userCertificate.isClickable = false
            profileBinding.userCertificate.isFocusableInTouchMode = false
            profileBinding.userCertificate.isFocusable = false

            profileBinding.cancelBtn.visibility = View.GONE
            profileBinding.submitBtn.visibility = View.GONE
        }

        profileBinding.logout.setOnClickListener {
            signOutAlertDialogBox(it)
        }

        profileBinding.submitBtn.setOnClickListener {
            if (userType == "Individual") {
                updateIndividualUserData()
            }
            if (userType == "NGO") {
                updateNGOData()
            }
        }

        profileBinding.deleteAccount.setOnClickListener {
            deleteAccount()
        }

        profileBinding.userCertificateImage.setOnClickListener {
            if (editable) {
                checkPermissionForImage()
            }
        }
        profileBinding.userCertificateText.setOnClickListener {
            if (editable) {
                checkPermissionForImage()
            }
        }

        return profileBinding.root
    }

    private fun deleteAccount() {
        /*ref.delete()
            .addOnSuccessListener {
                FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {
                    Toast.makeText(
                        requireContext(),
                        "Account Deleted Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.startActivity(Intent(requireContext(), LoginActivity::class.java))
                    activity?.finish()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
    }

    private fun signOutAlertDialogBox(view: View) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(view.context)
        // set title
        builder.setTitle("WishKart")
        //set content area
        builder.setMessage("Are you sure, you want to logout?")
        //set negative button
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            auth.signOut()
            activity?.startActivity(Intent(requireContext(), LoginActivity::class.java))
            activity?.finish()
        }
        //set positive button
        builder.setNegativeButton(
            "No"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun updateNGOData() {
        val userName = profileBinding.userName.text.toString()
        val userStreetAddress = profileBinding.userStreetAddress.text.toString()
        val userCity = profileBinding.userCity.text.toString()
        val userPincode = profileBinding.userPinCode.text.toString()
        val userCountry = profileBinding.userCountry.text.toString()
        val userCertificateNumber = profileBinding.userCertificate.text.toString()

        when {
            userName.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your name.", Toast.LENGTH_SHORT)
                    .show()
            }
            userStreetAddress.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your street address.", Toast.LENGTH_SHORT)
                    .show()
            }
            userCity.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your city name.", Toast.LENGTH_SHORT)
                    .show()
            }
            userPincode.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your PinCode.", Toast.LENGTH_SHORT)
                    .show()
            }
            userCountry.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your country name.", Toast.LENGTH_SHORT)
                    .show()
            }
            userCertificateNumber.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter NGO Certificate Number.", Toast.LENGTH_SHORT)
                    .show()
            }
            (userCertificateUrl.isEmpty()) -> {
                Toast.makeText(requireContext(), "Please upload NGO Certificate Picture.", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                database.collection("users").document(auth.uid.toString()).update(
                    "userType", userType,
                    "userType", userName,
                    "userPhoneNumber", phoneNumber,
                    "userStreetAddress", userStreetAddress,
                    "userCity", userCity,
                    "userPinCode", userPincode,
                    "userCountry", userCountry,
                    "userCertificateUrl", userCertificateUrl,
                    "userCertificateNumber", userCertificateNumber
                ).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile Updated Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun updateIndividualUserData() {
        val userName = profileBinding.userName.text.toString()
        val userStreetAddress = profileBinding.userStreetAddress.text.toString()
        val userCity = profileBinding.userCity.text.toString()
        val userPincode = profileBinding.userPinCode.text.toString()
        val userCountry = profileBinding.userCountry.text.toString()

        when {
            userName.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your name.", Toast.LENGTH_SHORT)
                    .show()
            }
            userStreetAddress.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your street address.", Toast.LENGTH_SHORT)
                    .show()
            }
            userCity.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your city name.", Toast.LENGTH_SHORT)
                    .show()
            }
            userPincode.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your PinCode.", Toast.LENGTH_SHORT)
                    .show()
            }
            userCountry.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your country name.", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                database.collection("users").document(auth.uid.toString()).update(
                    "userType", userType,
                    "userType", userName,
                    "userPhoneNumber", phoneNumber,
                    "userStreetAddress", userStreetAddress,
                    "userCity", userCity,
                    "userPinCode", userPincode,
                    "userCountry", userCountry
                ).addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Profile Updated Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    activity?.startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        requireContext(),
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun checkUserType() {
        progressDialog = createProgressDialog("Loading Data")
        progressDialog.show()
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                if (auth.uid == it.get("uid")) {
                    userType = it.getString("userType").toString()
                    if (userType == "Individual") {
                        fetchIndividualUserData()
                    }
                    if (userType == "NGO") {
                        fetchNGOData()
                        profileBinding.userCertificate.visibility = View.VISIBLE
                        profileBinding.userCertificateImage.visibility = View.VISIBLE
                        profileBinding.userCertificateText.visibility = View.VISIBLE
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchNGOData() {
        ref.get().addOnSuccessListener {
            userType = it.getString("userType").toString()
            userName = it.getString("userName").toString()
            phoneNumber = it.getString("userPhoneNumber").toString()
            userStreetAddress = it.getString("userStreetAddress").toString()
            userCity = it.getString("userCity").toString()
            userPinCode = it.getString("userPinCode").toString()
            userCountry = it.getString("userCountry").toString()
            userCertificateUrl = it.getString("userCertificateUrl").toString()
            userCertificateNumber = it.getString("userCertificateNumber").toString()

            profileBinding.userName.setText(userName)
            profileBinding.userPhoneNumber.text = phoneNumber
            profileBinding.userStreetAddress.setText(userStreetAddress)
            profileBinding.userCity.setText(userCity)
            profileBinding.userPinCode.setText(userPinCode)
            profileBinding.userCountry.setText(userCountry)
            Picasso.get()
                .load(userCertificateUrl)
                .placeholder(R.drawable.user_ngo_certificate)
                .error(R.drawable.user_ngo_certificate)
                .into(profileBinding.userCertificateImage)
            profileBinding.userCertificateText.text = getString(R.string.your_ngo_certificate)
            profileBinding.userCertificate.setText(userCertificateNumber)
            progressDialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchIndividualUserData() {
        ref.get().addOnSuccessListener {
            userType = it.getString("userType").toString()
            userName = it.getString("userName").toString()
            phoneNumber = it.getString("userPhoneNumber").toString()
            userStreetAddress = it.getString("userStreetAddress").toString()
            userCity = it.getString("userCity").toString()
            userPinCode = it.getString("userPinCode").toString()
            userCountry = it.getString("userCountry").toString()

            profileBinding.userName.setText(userName)
            profileBinding.userPhoneNumber.text = phoneNumber
            profileBinding.userStreetAddress.setText(userStreetAddress)
            profileBinding.userCity.setText(userCity)
            profileBinding.userPinCode.setText(userPinCode)
            profileBinding.userCountry.setText(userCountry)
            progressDialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun checkPermissionForImage() {
        if ((ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED)
            && (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED)
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
                requireContext(),
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
                profileBinding.userCertificateImage.setImageURI(it)
                startUpload(it)
            }
        }
    }

    private fun startUpload(filePath: Uri) {
        progressDialog = createProgressDialog("Uploading Image...")
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
                Toast.makeText(
                    requireContext(),
                    "Something went wrong. Please try again!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong. Please try again!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createProgressDialog(message: String): ProgressDialog {
        return ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage(message)
        }
    }
}
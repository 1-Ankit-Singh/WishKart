package com.androidproject.wishkart.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.R
import com.androidproject.wishkart.auth.UserTypeActivity
import com.androidproject.wishkart.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {
    // Initializing Variables
    private lateinit var profileBinding: FragmentProfileBinding
    private val auth = FirebaseAuth.getInstance()
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

            profileBinding.userAddress.visibility = View.GONE
            profileBinding.userStreetAddress.visibility = View.VISIBLE
            profileBinding.userCity.visibility = View.VISIBLE
            profileBinding.userPinCode.visibility = View.VISIBLE
            profileBinding.userCountry.visibility = View.VISIBLE

            profileBinding.cancelBtn.visibility = View.VISIBLE
            profileBinding.submitBtn.visibility = View.VISIBLE
        }

        profileBinding.cancelBtn.setOnClickListener {
            checkUserType()
            editable = false

            profileBinding.userAddress.visibility = View.VISIBLE
            profileBinding.userStreetAddress.visibility = View.GONE
            profileBinding.userCity.visibility = View.GONE
            profileBinding.userPinCode.visibility = View.GONE
            profileBinding.userCountry.visibility = View.GONE

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
            updateProductsList()
            updateDonateList()
        }

        profileBinding.deleteAccount.setOnClickListener {
            deleteAccount()
        }

        return profileBinding.root
    }

    private fun updateDonateList() {
        val userCity = profileBinding.userCity.text.toString()
        val userPincode = profileBinding.userPinCode.text.toString()
        val userCountry = profileBinding.userCountry.text.toString()
        database.collection("donate").document("${auth.uid}").update(
            "productOwnerCity", userCity,
            "productOwnerPinCode", userPincode,
            "productOwnerCountry", userCountry
        ).addOnSuccessListener {
            // Do Nothing
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again !!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateProductsList() {
        val userCity = profileBinding.userCity.text.toString()
        val userPincode = profileBinding.userPinCode.text.toString()
        val userCountry = profileBinding.userCountry.text.toString()
        database.collection("donate").document("${auth.uid}").update(
            "productOwnerCity", userCity,
            "productOwnerPinCode", userPincode,
            "productOwnerCountry", userCountry
        ).addOnSuccessListener {
            // Do Nothing
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again !!",
                Toast.LENGTH_SHORT
            ).show()
        }
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
            activity?.startActivity(Intent(requireContext(), UserTypeActivity::class.java))
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
                database.collection("users").document("${auth.uid}").update(
                    "userType", userType,
                    "userName", userName,
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
                Toast.makeText(requireContext(), "Please enter your name.", Toast.LENGTH_SHORT).show()
            }
            userStreetAddress.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your street address.", Toast.LENGTH_SHORT).show()
            }
            userCity.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your city name.", Toast.LENGTH_SHORT).show()
            }
            userPincode.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your PinCode.", Toast.LENGTH_SHORT).show()
            }
            userCountry.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter your country name.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                database.collection("users").document("${auth.uid}").update(
                    "userType", userType,
                    "userName", userName,
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
        progressDialog = createProgressDialog()
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
                        profileBinding.userCertificateImageText.visibility = View.VISIBLE
                        profileBinding.userCertificateImage.visibility = View.VISIBLE
                        profileBinding.userCertificateText.visibility = View.VISIBLE
                        profileBinding.userCertificate.visibility = View.VISIBLE
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

            profileBinding.userName.text = userName
            profileBinding.userPhoneNumber.text = phoneNumber
            profileBinding.userStreetAddress.setText(userStreetAddress)
            profileBinding.userCity.setText(userCity)
            profileBinding.userPinCode.setText(userPinCode)
            profileBinding.userCountry.setText(userCountry)
            profileBinding.userAddress.text = getString(R.string.address, userStreetAddress, userCity, userPinCode, userCountry)
            Picasso.get()
                .load(userCertificateUrl)
                .placeholder(R.drawable.user_ngo_certificate)
                .error(R.drawable.user_ngo_certificate)
                .into(profileBinding.userCertificateImage)
            profileBinding.userCertificate.text = userCertificateNumber
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

            profileBinding.userName.text = userName
            profileBinding.userPhoneNumber.text = phoneNumber
            profileBinding.userStreetAddress.setText(userStreetAddress)
            profileBinding.userCity.setText(userCity)
            profileBinding.userPinCode.setText(userPinCode)
            profileBinding.userCountry.setText(userCountry)
            profileBinding.userAddress.text = getString(R.string.address, userStreetAddress, userCity, userPinCode, userCountry)
            progressDialog.dismiss()
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun createProgressDialog(): ProgressDialog {
        return ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage("Loading Data")
        }
    }
}
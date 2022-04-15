package com.androidproject.wishkart.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ActivityUpdatePhoneNumberBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class UpdatePhoneNumberActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var updatePhoneNumberBinding: ActivityUpdatePhoneNumberBinding
    private val database = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var countryCode: String
    private var phoneNumber: String? = null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var verificationId: String? = null
    private var reSendToken: PhoneAuthProvider.ForceResendingToken? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updatePhoneNumberBinding = ActivityUpdatePhoneNumberBinding.inflate(layoutInflater)
        setContentView(updatePhoneNumberBinding.root)
        setSupportActionBar(updatePhoneNumberBinding.toolbarUpdatePhoneNumber)
        auth = FirebaseAuth.getInstance()

        updatePhoneNumberBinding.next.setOnClickListener {
            countryCode = updatePhoneNumberBinding.ccp.selectedCountryCodeWithPlus
            phoneNumber = countryCode + updatePhoneNumberBinding.phoneNumber.text.toString().trim()
            if (phoneNumber!!.isNotEmpty() && phoneNumber!!.length == 13) {
                updatePhoneNumberBinding.linearLayoutNumber.visibility = View.GONE
                updatePhoneNumberBinding.linearLayoutOTP.visibility = View.VISIBLE
                initView()
                startVerify()
            } else {
                Toast.makeText(this, "Please enter a valid number to continue!", Toast.LENGTH_SHORT).show()
            }
        }

        updatePhoneNumberBinding.verification.setOnClickListener {
            verification()
        }
    }

    private fun verification() {
        val code = updatePhoneNumberBinding.sentCode.text.toString()
        if (code.isNotEmpty() && !verificationId.isNullOrEmpty()) {
            progressDialog = createProgressDialog("Please wait...")
            progressDialog.show()
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            updatePhoneNumber(credential)
        }
    }

    private fun startVerify() {
        startPhoneNumberVerification(phoneNumber!!)
        progressDialog = createProgressDialog("Sending a verification code")
        progressDialog.show()
    }

    private fun initView() {
        updatePhoneNumberBinding.verify.text = getString(R.string.verify_number, phoneNumber)

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                val smsMessageSent = credential.smsCode
                if (!smsMessageSent.isNullOrBlank()) {
                    updatePhoneNumberBinding.sentCode.setText(smsMessageSent)
                }
                updatePhoneNumber(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (::progressDialog.isInitialized) {
                    progressDialog.dismiss()
                }
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        // Invalid request
                        Log.e("Exception:", "FirebaseAuthInvalidCredentialsException", e)
                        Log.e("=========:", "FirebaseAuthInvalidCredentialsException" + e.message)
                        Toast.makeText(
                            this@UpdatePhoneNumberActivity,
                            "Invalid request!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is FirebaseTooManyRequestsException -> {
                        // The SMS quota for the project has been exceeded
                        Log.e("Exception:", "FirebaseTooManyRequestsException", e)
                        Toast.makeText(
                            this@UpdatePhoneNumberActivity,
                            "SMS quota limit exceeded!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else ->  {
                        Toast.makeText(
                            this@UpdatePhoneNumberActivity,
                            "Something went wrong, please try again!!!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                updatePhoneNumberBinding.linearLayoutNumber.visibility = View.VISIBLE
                updatePhoneNumberBinding.linearLayoutOTP.visibility = View.GONE
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                progressDialog.dismiss()
                // Save verification ID and resending token so we can use them later
                // Log.e("onCodeSent==", "onCodeSent:$verificationId")
                this@UpdatePhoneNumberActivity.verificationId = verificationId
                reSendToken = token
            }
        }
    }

    private fun updatePhoneNumber(phoneAuthCredential: PhoneAuthCredential) {
        FirebaseAuth.getInstance()
            .currentUser?.updatePhoneNumber(phoneAuthCredential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserProfile()
                } else {
                    if (::progressDialog.isInitialized) {
                        progressDialog.dismiss()
                    }
                    Toast.makeText(this, "Phone Number not updated", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUserProfile() {
        database.collection("users").document("${auth.uid}").update(
            "userPhoneNumber", phoneNumber
        ).addOnSuccessListener {
            if (::progressDialog.isInitialized) {
                progressDialog.dismiss()
            }
            Toast.makeText(this, "Phone Number updated", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            if (::progressDialog.isInitialized) {
                progressDialog.dismiss()
            }
            Toast.makeText(this, "Phone Number not updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startPhoneNumberVerification(phoneNumber: String) {
        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun createProgressDialog(message: String): ProgressDialog {
        return ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage(message)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        confirmation()
    }

    private fun confirmation() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("WishKart")
        builder.setMessage("Are you sure, you do not want to update your phone number?")
        builder.setPositiveButton(
            "Yes"
        ) { _, _ ->
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        builder.setNegativeButton(
            "No"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}
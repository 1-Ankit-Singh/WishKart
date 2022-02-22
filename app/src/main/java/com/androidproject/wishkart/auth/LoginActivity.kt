package com.androidproject.wishkart.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.androidproject.wishkart.databinding.ActivityLoginBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var loginActivity: ActivityLoginBinding
    private lateinit var phoneNumber: String
    private lateinit var countryCode: String
    private lateinit var alertDialogBuilder: MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivity = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginActivity.root)


        loginActivity.phoneNumber.addTextChangedListener {
            loginActivity.next.isEnabled = !(it.isNullOrEmpty() || it.length < 10)
        }

        // Hint Request for Phone Number
        loginActivity.next.setOnClickListener {
            checkNumber()
        }

    }

    private fun checkNumber() {
        countryCode = loginActivity.ccp.selectedCountryCodeWithPlus
        phoneNumber = countryCode + loginActivity.phoneNumber.text.toString()

        if (validatePhoneNumber(loginActivity.phoneNumber.text.toString())) {
            notifyUserBeforeVerify(
                "We will be verifying the phone number:$phoneNumber\n" +
                        "Is this OK, or would you like to edit the number?"
            )
        } else {
            Toast.makeText(this, "Please enter a valid number to continue!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        if (phone.isEmpty()) {
            return false
        }
        return true
    }

    private fun notifyUserBeforeVerify(message: String) {
        alertDialogBuilder = MaterialAlertDialogBuilder(this).apply {
            setMessage(message)
            setPositiveButton("Ok") { _, _ ->
                showLoginActivity()
            }

            setNegativeButton("Edit") { dialog, _ ->
                dialog.dismiss()
            }

            setCancelable(false)
            create()
            show()
        }
    }

    private fun showLoginActivity() {
        startActivity(Intent(this, OtpActivity::class.java).putExtra(PHONE_NUMBER, phoneNumber))
        finish()
    }
}
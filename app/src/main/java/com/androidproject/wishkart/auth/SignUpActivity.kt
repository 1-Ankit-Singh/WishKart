package com.androidproject.wishkart.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpActivity = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(signUpActivity.root)

        userType = intent.getStringExtra(USER_TYPE_SIGNUP).toString()
        phoneNumber = intent.getStringExtra(USER_PHONE_NUMBER).toString()
    }
}
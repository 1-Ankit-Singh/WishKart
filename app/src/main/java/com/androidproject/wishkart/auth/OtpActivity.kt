package com.androidproject.wishkart.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidproject.wishkart.R

const val PHONE_NUMBER = "phoneNumber"

class OtpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
    }
}
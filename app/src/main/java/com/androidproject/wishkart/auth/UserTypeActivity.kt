package com.androidproject.wishkart.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.androidproject.wishkart.databinding.ActivityUserTypeBinding

class UserTypeActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var userTypeActivity: ActivityUserTypeBinding
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userTypeActivity = ActivityUserTypeBinding.inflate(layoutInflater)
        setContentView(userTypeActivity.root)

        userTypeActivity.individual.setOnClickListener {
            userType = "Individual"
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .putExtra(USER_TYPE_LOGIN, userType)
            )
            finish()
        }

        userTypeActivity.ngo.setOnClickListener {
            userType = "NGO"
            startActivity(
                Intent(this, LoginActivity::class.java)
                    .putExtra(USER_TYPE_LOGIN, userType)
            )
            finish()
        }
    }
}
package com.androidproject.wishkart.donations

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityInterestDetailsBinding

class InterestDetailsActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var interestDetailsBinding: ActivityInterestDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        interestDetailsBinding = ActivityInterestDetailsBinding.inflate(layoutInflater)
        setContentView(interestDetailsBinding.root)
        setSupportActionBar(interestDetailsBinding.toolbarInterestDetails)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DonationsActivity::class.java))
        finish()
    }
}
package com.androidproject.wishkart.ui.donate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityDonatedProductDetailBinding

class DonatedProductDetailActivity : AppCompatActivity() {
    // Initializing Variable
    private lateinit var donatedProductDetailActivity: ActivityDonatedProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        donatedProductDetailActivity = ActivityDonatedProductDetailBinding.inflate(layoutInflater)
        setContentView(donatedProductDetailActivity.root)
    }
}
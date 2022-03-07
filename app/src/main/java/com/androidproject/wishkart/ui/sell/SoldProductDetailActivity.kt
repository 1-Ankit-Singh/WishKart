package com.androidproject.wishkart.ui.sell

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivitySoldProductDetailBinding

class SoldProductDetailActivity : AppCompatActivity() {
    // Initializing Variable
    private lateinit var soldProductDetailActivity: ActivitySoldProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soldProductDetailActivity = ActivitySoldProductDetailBinding.inflate(layoutInflater)
        setContentView(soldProductDetailActivity.root)
    }
}
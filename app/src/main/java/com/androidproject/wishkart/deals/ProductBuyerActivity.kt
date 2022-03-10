package com.androidproject.wishkart.deals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityProductBuyerBinding

class ProductBuyerActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productBuyerBinding: ActivityProductBuyerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productBuyerBinding = ActivityProductBuyerBinding.inflate(layoutInflater)
        setContentView(productBuyerBinding.root)
        setSupportActionBar(productBuyerBinding.toolbarProductBuyer)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DealsActivity::class.java))
        finish()
    }
}
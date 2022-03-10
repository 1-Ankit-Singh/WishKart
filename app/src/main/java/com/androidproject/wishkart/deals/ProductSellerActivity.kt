package com.androidproject.wishkart.deals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityProductSellerBinding

class ProductSellerActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productSellerBinding: ActivityProductSellerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productSellerBinding = ActivityProductSellerBinding.inflate(layoutInflater)
        setContentView(productSellerBinding.root)
        setSupportActionBar(productSellerBinding.toolbarProductSeller)
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
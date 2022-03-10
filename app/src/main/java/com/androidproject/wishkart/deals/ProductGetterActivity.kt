package com.androidproject.wishkart.deals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityProductGetterBinding

class ProductGetterActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productGetterBinding: ActivityProductGetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productGetterBinding = ActivityProductGetterBinding.inflate(layoutInflater)
        setContentView(productGetterBinding.root)
        setSupportActionBar(productGetterBinding.toolbarProductGetter)
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
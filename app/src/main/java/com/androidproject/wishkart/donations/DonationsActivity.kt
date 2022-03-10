package com.androidproject.wishkart.donations

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.adapter.DonationsScreenSlidePagerAdapter
import com.androidproject.wishkart.databinding.ActivityDonationsBinding
import com.google.android.material.tabs.TabLayoutMediator

class DonationsActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var donationsBinding: ActivityDonationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        donationsBinding = ActivityDonationsBinding.inflate(layoutInflater)
        setContentView(donationsBinding.root)
        setSupportActionBar(donationsBinding.toolbarDonations)
        donationsBinding.viewPager.adapter = DonationsScreenSlidePagerAdapter(this)
        TabLayoutMediator(donationsBinding.tabs, donationsBinding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Donations"
                    }
                    1 -> {
                        tab.text = "Interest"
                    }
                }
            }).attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
package com.androidproject.wishkart.deals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.adapter.ScreenSlidePagerAdapter
import com.androidproject.wishkart.databinding.ActivityDealsBinding
import com.google.android.material.tabs.TabLayoutMediator

class DealsActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var dealsBinding: ActivityDealsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dealsBinding = ActivityDealsBinding.inflate(layoutInflater)
        setContentView(dealsBinding.root)
        dealsBinding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        TabLayoutMediator(dealsBinding.tabs, dealsBinding.viewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Your Deals"
                    }
                    1 -> {
                        tab.text = "Other's Deal"
                    }
                    2 -> {
                        tab.text = "Donatable Deals"
                    }
                }
            }).attach()
    }
}
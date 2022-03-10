package com.androidproject.wishkart.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidproject.wishkart.donations.DonationsDetailsFragment
import com.androidproject.wishkart.donations.InterestDetailsFragment

class DonationsScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val numPages: Int = 2

    override fun getItemCount(): Int = numPages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> DonationsDetailsFragment()
        else -> InterestDetailsFragment()
    }
}
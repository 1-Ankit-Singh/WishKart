package com.androidproject.wishkart.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidproject.wishkart.deals.DonatableDealsFragment
import com.androidproject.wishkart.deals.OthersDealWithYouFragment
import com.androidproject.wishkart.deals.YourDealsWithOtherFragment

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val numPages: Int = 3

    override fun getItemCount(): Int = numPages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> YourDealsWithOtherFragment()
        1 -> OthersDealWithYouFragment()
        else -> DonatableDealsFragment()
    }
}
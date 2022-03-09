package com.androidproject.wishkart.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.androidproject.wishkart.deals.ProductBuyerFragment
import com.androidproject.wishkart.deals.ProductGetterFragment
import com.androidproject.wishkart.deals.ProductSellerFragment

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val numPages: Int = 3

    override fun getItemCount(): Int = numPages

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> ProductBuyerFragment()
        1 -> ProductSellerFragment()
        else -> ProductGetterFragment()
    }
}
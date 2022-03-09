package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentProductSellerBinding

class ProductSellerFragment : Fragment() {
    // Initializing Variables
    private lateinit var productSellerFragment: FragmentProductSellerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productSellerFragment = FragmentProductSellerBinding.inflate(inflater)
        return productSellerFragment.root
    }
}
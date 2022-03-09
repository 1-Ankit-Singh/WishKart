package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentProductBuyerBinding

class ProductBuyerFragment : Fragment() {
    // Initializing Variables
    private lateinit var productBuyerFragment: FragmentProductBuyerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productBuyerFragment = FragmentProductBuyerBinding.inflate(inflater)
        return productBuyerFragment.root
    }
}
package com.androidproject.wishkart.ui.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentBuyBinding

class BuyFragment : Fragment() {
    // Initializing Variables
    private lateinit var buyBinding: FragmentBuyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        buyBinding = FragmentBuyBinding.inflate(inflater)
        return buyBinding.root
    }
}
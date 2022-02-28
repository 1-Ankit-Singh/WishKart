package com.androidproject.wishkart.ui.sell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentSellBinding

class SellFragment : Fragment() {
    // Initializing Variables
    private lateinit var sellBinding: FragmentSellBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sellBinding = FragmentSellBinding.inflate(inflater)
        return sellBinding.root
    }
}
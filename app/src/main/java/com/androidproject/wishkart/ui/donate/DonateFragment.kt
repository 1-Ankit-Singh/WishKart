package com.androidproject.wishkart.ui.donate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.FragmentDonateBinding

class DonateFragment : Fragment() {
    // Initializing Variables
    private lateinit var donateBinding: FragmentDonateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        donateBinding = FragmentDonateBinding.inflate(inflater)
        return donateBinding.root
    }
}
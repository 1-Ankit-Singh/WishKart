package com.androidproject.wishkart.ui.donate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        donateBinding.donateNewProduct.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), DonateActivity::class.java))
            activity?.finish()
        }
        return donateBinding.root
    }
}
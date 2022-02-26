package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentOthersDealWithYouBinding

class OthersDealWithYouFragment : Fragment() {
    // Initializing Variables
    private lateinit var othersDealWithYouBinding: FragmentOthersDealWithYouBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        othersDealWithYouBinding = FragmentOthersDealWithYouBinding.inflate(inflater)
        return othersDealWithYouBinding.root
    }
}
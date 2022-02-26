package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentYourDealsWithOtherBinding

class YourDealsWithOtherFragment : Fragment() {
    // Initializing Variables
    private lateinit var yourDealsWithOtherBinding: FragmentYourDealsWithOtherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        yourDealsWithOtherBinding = FragmentYourDealsWithOtherBinding.inflate(inflater)
        return yourDealsWithOtherBinding.root
    }
}
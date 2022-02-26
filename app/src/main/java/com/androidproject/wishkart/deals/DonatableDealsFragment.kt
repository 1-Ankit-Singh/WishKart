package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentDonatableDealsBinding

class DonatableDealsFragment : Fragment() {
    // Initializing Variables
    private lateinit var donatableDealsBinding: FragmentDonatableDealsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        donatableDealsBinding = FragmentDonatableDealsBinding.inflate(inflater)
        return donatableDealsBinding.root
    }
}
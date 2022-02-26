package com.androidproject.wishkart.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    // Initializing Variables
    private lateinit var profileBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileBinding = FragmentProfileBinding.inflate(inflater)
        return profileBinding.root
    }
}
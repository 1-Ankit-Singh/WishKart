package com.androidproject.wishkart.ui.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.FragmentHelpBinding

class HelpFragment : Fragment() {
    // Initializing Variables
    private lateinit var helpBinding: FragmentHelpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        helpBinding = FragmentHelpBinding.inflate(inflater)
        return helpBinding.root
    }
}
package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentProductGetterBinding

class ProductGetterFragment : Fragment() {
    // Initializing Variables
    private lateinit var productGetterFragment: FragmentProductGetterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productGetterFragment = FragmentProductGetterBinding.inflate(inflater)
        return productGetterFragment.root
    }
}
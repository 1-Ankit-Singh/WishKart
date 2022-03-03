package com.androidproject.wishkart.ui.sell

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentSellBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class SellFragment : Fragment() {
    // Initializing Variables
    private lateinit var sellBinding: FragmentSellBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sellBinding = FragmentSellBinding.inflate(inflater)
        sellBinding.uploadNewProduct.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), SellActivity::class.java))
            activity?.finish()
        }
        return sellBinding.root
    }
}
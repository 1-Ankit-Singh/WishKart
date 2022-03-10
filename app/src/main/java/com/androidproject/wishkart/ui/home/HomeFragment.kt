package com.androidproject.wishkart.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentHomeBinding
import com.androidproject.wishkart.donations.DonationsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    // Initializing Variables
    private lateinit var homeBinding: FragmentHomeBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var ref: DocumentReference = database.collection("users").document(auth.uid!!)
    private lateinit var userType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater)
        checkUserType()
        return homeBinding.root
    }

    private fun checkUserType() {
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                if (auth.uid == it.get("uid")) {
                    userType = it.getString("userType").toString()
                    if (userType == "Individual") {
                        homeBinding.donations.visibility = View.GONE
                    }
                    if (userType == "NGO") {
                        homeBinding.donations.visibility = View.VISIBLE
                        homeBinding.donations.setOnClickListener {
                            activity?.startActivity(Intent(context, DonationsActivity::class.java))
                            activity?.finish()
                        }
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
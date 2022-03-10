package com.androidproject.wishkart.donations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductDonateAdapter
import com.androidproject.wishkart.databinding.FragmentDonationsDetailsBinding
import com.androidproject.wishkart.model.ProductDonate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DonationsDetailsFragment : Fragment() {
    // Initializing Variables
    private lateinit var donationsDetailsFragment: FragmentDonationsDetailsBinding
    private var productDonateArrayList = arrayListOf<ProductDonate>()
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        donationsDetailsFragment = FragmentDonationsDetailsBinding.inflate(inflater)
        fetchData()
        return donationsDetailsFragment.root
    }

    private fun fetchData() {
        database.collection("donate")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    donationsDetailsFragment.donationsRv.visibility = View.GONE
                    donationsDetailsFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    donationsDetailsFragment.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
                    if (auth.uid!! != product.getString("uid").toString()) {
                        val productDonate = ProductDonate(
                            product.getString("uid").toString(),
                            product.getString("productOwnerCity").toString(),
                            product.getString("productOwnerPinCode").toString(),
                            product.getString("productOwnerCountry").toString(),
                            product.getString("productName").toString(),
                            product.getString("productCategory").toString(),
                            product.getString("productDescription").toString(),
                            product.getString("productUrl1").toString(),
                            product.getString("productUrl2").toString(),
                            product.getString("productUrl3").toString(),
                            product.getString("productUrl4").toString(),
                            product.getString("productStatus").toString(),
                        )
                        productDonateArrayList.add(productDonate)
                    }
                }
                donationsDetailsFragment.donationsRv.layoutManager = LinearLayoutManager(requireContext())
                donationsDetailsFragment.donationsRv.adapter = ProductDonateAdapter(productDonateArrayList, requireContext(), "DonationsDetailsActivity")
                donationsDetailsFragment.progressBarDonations.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong, Please try again!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
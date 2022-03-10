package com.androidproject.wishkart.donations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductDonateAdapter
import com.androidproject.wishkart.databinding.FragmentInterestDetailsBinding
import com.androidproject.wishkart.model.ProductDonate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class InterestDetailsFragment : Fragment() {
    // Initializing Variables
    private lateinit var interestDetailsFragment: FragmentInterestDetailsBinding
    private var productDonateArrayList = arrayListOf<ProductDonate>()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        interestDetailsFragment = FragmentInterestDetailsBinding.inflate(inflater)
        fetchData()
        return interestDetailsFragment.root
    }

    private fun fetchData() {
        database.collection("users/${auth.uid.toString()}/donationInterest")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    interestDetailsFragment.InterestRv.visibility = View.GONE
                    interestDetailsFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    interestDetailsFragment.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
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
                interestDetailsFragment.InterestRv.layoutManager = LinearLayoutManager(context)
                interestDetailsFragment.InterestRv.adapter = ProductDonateAdapter(productDonateArrayList, requireContext(), "InterestDetailsActivity")
                interestDetailsFragment.progressBarInterest.visibility = View.GONE
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
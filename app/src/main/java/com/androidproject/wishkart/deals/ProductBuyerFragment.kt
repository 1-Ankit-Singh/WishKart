package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductBuyerAdapter
import com.androidproject.wishkart.databinding.FragmentProductBuyerBinding
import com.androidproject.wishkart.model.ProductBuyer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProductBuyerFragment : Fragment() {
    // Initializing Variables
    private lateinit var productBuyerFragment: FragmentProductBuyerBinding
    private var productBuyerArrayList = arrayListOf<ProductBuyer>()
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productBuyerFragment = FragmentProductBuyerBinding.inflate(inflater)
        fetchData()
        return productBuyerFragment.root
    }

    private fun fetchData() {
        database.collection("users/${auth.uid}/productBuyer")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    productBuyerFragment.sellDealRv.visibility = View.GONE
                    productBuyerFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    productBuyerFragment.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
                    if (auth.uid!! != product.getString("uid").toString()) {
                        val productBuyer = ProductBuyer(
                            product.getString("productBuyerUid").toString(),
                            product.getString("productBuyerType").toString(),
                            product.getString("productBuyerName").toString(),
                            product.getString("productBuyerPhoneNumber").toString(),
                            product.getString("productBuyerStreetAddress").toString(),
                            product.getString("productBuyerCity").toString(),
                            product.getString("productBuyerPinCode").toString(),
                            product.getString("productBuyerCountry").toString(),
                            product.getString("productName").toString(),
                            product.getString("productCategory").toString(),
                            product.getString("productMinPrice").toString(),
                            product.getString("productMaxPrice").toString(),
                            product.getString("productDescription").toString(),
                            product.getString("productUrl1").toString(),
                            product.getString("productUrl2").toString(),
                            product.getString("productUrl3").toString(),
                            product.getString("productUrl4").toString(),
                            product.getString("productStatus").toString(),
                        )
                        productBuyerArrayList.add(productBuyer)
                    }
                }
                productBuyerFragment.sellDealRv.layoutManager = LinearLayoutManager(context)
                productBuyerFragment.sellDealRv.adapter = ProductBuyerAdapter(productBuyerArrayList, requireContext(), "ProductBuyerActivity")
                productBuyerFragment.progressBarBuyer.visibility = View.GONE
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
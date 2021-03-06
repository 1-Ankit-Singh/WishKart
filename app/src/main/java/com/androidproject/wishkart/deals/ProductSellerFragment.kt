package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductBuyAdapter
import com.androidproject.wishkart.databinding.FragmentProductSellerBinding
import com.androidproject.wishkart.model.ProductBuy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProductSellerFragment : Fragment() {
    // Initializing Variables
    private lateinit var productSellerFragment: FragmentProductSellerBinding
    private var productSellerArrayList = arrayListOf<ProductBuy>()
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productSellerFragment = FragmentProductSellerBinding.inflate(inflater)
        fetchData()
        return productSellerFragment.root
    }
    private fun fetchData() {
        database.collection("users/${auth.uid.toString()}/buy")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if(list.isEmpty()) {
                    productSellerFragment.buyDealRv.visibility = View.GONE
                    productSellerFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    productSellerFragment.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
                    val productBuy = ProductBuy(
                        product.getString("uid").toString(),
                        product.getString("productOwnerCity").toString(),
                        product.getString("productOwnerPinCode").toString(),
                        product.getString("productOwnerCountry").toString(),
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
                    productSellerArrayList.add(productBuy)
                }
                productSellerFragment.buyDealRv.layoutManager = LinearLayoutManager(context)
                productSellerFragment.buyDealRv.adapter = ProductBuyAdapter(productSellerArrayList, requireContext(), "ProductSellerActivity")
                productSellerFragment.progressBarSeller.visibility = View.GONE
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
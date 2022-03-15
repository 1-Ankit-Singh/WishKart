package com.androidproject.wishkart.ui.sell

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductSellAdapter
import com.androidproject.wishkart.databinding.FragmentSellBinding
import com.androidproject.wishkart.model.ProductSell
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class SellFragment : Fragment() {
    // Initializing Variables
    private lateinit var sellBinding: FragmentSellBinding
    private var productSellArrayList = arrayListOf<ProductSell>()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sellBinding = FragmentSellBinding.inflate(inflater)
        fetchData()
        sellBinding.uploadNewProduct.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), SellActivity::class.java))
            activity?.finish()
        }
        return sellBinding.root
    }

    private fun fetchData() {
        database.collection("users/${auth.uid.toString()}/products")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    sellBinding.sellRv.visibility = View.GONE
                    sellBinding.nothingToShowHereImage.visibility = View.VISIBLE
                    sellBinding.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
                    val productSell = ProductSell(
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
                    productSellArrayList.add(productSell)
                }
                sellBinding.sellRv.layoutManager = LinearLayoutManager(context)
                sellBinding.sellRv.adapter = ProductSellAdapter(productSellArrayList, requireContext(), "SoldProductDetailActivity")
                sellBinding.progressBarProductSell.visibility = View.GONE
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
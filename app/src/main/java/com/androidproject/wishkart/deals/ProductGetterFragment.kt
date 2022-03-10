package com.androidproject.wishkart.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductGetterAdapter
import com.androidproject.wishkart.databinding.FragmentProductGetterBinding
import com.androidproject.wishkart.model.ProductGetter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class ProductGetterFragment : Fragment() {
    // Initializing Variables
    private lateinit var productGetterFragment: FragmentProductGetterBinding
    private var productGetterArrayList = arrayListOf<ProductGetter>()
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        productGetterFragment = FragmentProductGetterBinding.inflate(inflater)
        fetchData()
        return productGetterFragment.root
    }

    private fun fetchData() {
        database.collection("users/${auth.uid}/productGetter")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    productGetterFragment.donateDealRv.visibility = View.GONE
                    productGetterFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    productGetterFragment.nothingToShowHereText.visibility = View.VISIBLE
                }
                for (product in list) {
                    if (auth.uid!! != product.getString("uid").toString()) {
                        val productGetter = ProductGetter(
                            product.getString("productGetterUid").toString(),
                            product.getString("productGetterType").toString(),
                            product.getString("productGetterName").toString(),
                            product.getString("productGetterPhoneNumber").toString(),
                            product.getString("productGetterStreetAddress").toString(),
                            product.getString("productGetterCity").toString(),
                            product.getString("productGetterPinCode").toString(),
                            product.getString("productGetterCountry").toString(),
                            product.getString("productGetterCertificateUrl").toString(),
                            product.getString("productGetterCertificateNumber").toString(),
                            product.getString("productName").toString(),
                            product.getString("productCategory").toString(),
                            product.getString("productDescription").toString(),
                            product.getString("productUrl1").toString(),
                            product.getString("productUrl2").toString(),
                            product.getString("productUrl3").toString(),
                            product.getString("productUrl4").toString(),
                            product.getString("productStatus").toString(),
                        )
                        productGetterArrayList.add(productGetter)
                    }
                }
                productGetterFragment.donateDealRv.layoutManager = LinearLayoutManager(context)
                productGetterFragment.donateDealRv.adapter = ProductGetterAdapter(productGetterArrayList, requireContext(), "ProductGetterActivity")
                productGetterFragment.progressBarDonationGetter.visibility = View.GONE
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
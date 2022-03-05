package com.androidproject.wishkart.ui.buy

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductBuyAdapter
import com.androidproject.wishkart.databinding.FragmentBuyBinding
import com.androidproject.wishkart.model.ProductBuy
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class BuyFragment : Fragment() {
    // Initializing Variables
    private lateinit var buyBinding: FragmentBuyBinding
    private var productBuyArrayList = arrayListOf<ProductBuy>()
    private val database = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        buyBinding = FragmentBuyBinding.inflate(inflater)
        fetchData()
        return buyBinding.root
    }

    private fun fetchData() {
        progressDialog = createProgressDialog()
        progressDialog.show()
        database.collection("product")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if (list.isEmpty()) {
                    buyBinding.buyRv.visibility = View.GONE
                    buyBinding.nothingToShowHereImage.visibility = View.VISIBLE
                    buyBinding.nothingToShowHereText.visibility = View.VISIBLE
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
                    productBuyArrayList.add(productBuy)
                }
                buyBinding.buyRv.layoutManager = LinearLayoutManager(context)
                buyBinding.buyRv.adapter = ProductBuyAdapter(productBuyArrayList, requireContext())
                progressDialog.dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong, Please try again!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun createProgressDialog(): ProgressDialog {
        return ProgressDialog(requireContext()).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage("Loading Data...")
        }
    }
}
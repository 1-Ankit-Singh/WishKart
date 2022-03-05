package com.androidproject.wishkart.ui.donate

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductDonateAdapter
import com.androidproject.wishkart.databinding.FragmentDonateBinding
import com.androidproject.wishkart.model.ProductDonate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class DonateFragment : Fragment() {
    // Initializing Variables
    private lateinit var donateBinding: FragmentDonateBinding
    private var productDonateArrayList = arrayListOf<ProductDonate>()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        donateBinding = FragmentDonateBinding.inflate(inflater)
        fetchData()
        donateBinding.donateNewProduct.setOnClickListener {
            activity?.startActivity(Intent(requireContext(), DonateActivity::class.java))
            activity?.finish()
        }
        return donateBinding.root
    }

    private fun fetchData() {
        progressDialog = createProgressDialog()
        progressDialog.show()
        database.collection("users/${auth.uid.toString()}/donate")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                if(list.isEmpty()) {
                    donateBinding.donateRv.visibility = View.GONE
                    donateBinding.nothingToShowHereImage.visibility = View.VISIBLE
                    donateBinding.nothingToShowHereText.visibility = View.VISIBLE
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
                donateBinding.donateRv.layoutManager = LinearLayoutManager(context)
                donateBinding.donateRv.adapter = ProductDonateAdapter(productDonateArrayList, requireContext())
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
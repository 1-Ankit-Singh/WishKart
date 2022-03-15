package com.androidproject.wishkart.ui.buy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidproject.wishkart.adapter.ProductBuyAdapter
import com.androidproject.wishkart.databinding.FragmentBuyBinding
import com.androidproject.wishkart.model.ProductBuy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hbb20.R

class BuyFragment : Fragment() {
    // Initializing Variables
    private lateinit var buyBinding: FragmentBuyBinding
    private var productBuyArrayList = arrayListOf<ProductBuy>()
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val searchWithOptions = arrayListOf<Any>("Search with", "Name", "Category")
    private lateinit var searchWithQuery: String
    private lateinit var searchWithSelection: String
    private var categoryOptions = arrayListOf<Any>(
        "Select Category",
        "Apparel or Clothes",
        "Fashion",
        "Electronic Products or Gadgets",
        "Business Supply",
        "Mobile Phones",
        "Bags",
        "Cosmetics",
        "Spices and Edible Items",
        "Art & Craft",
        "Education",
        "Jewellery",
        "Books",
        "Shoes",
        "Furniture",
        "Bedding Items",
        "Utensils",
        "Kitchen Appliances",
        "Homemade Perfumes",
        "Greeting Cards",
        "Lights and Bulbs",
        "Handmade Toys",
        "Toys",
        "Watches",
        "Fitness Products",
        "Desktop or Laptop",
        "Paper Products",
        "Toddler Items"
    )
    private lateinit var searchCity: String
    private var searchable: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        buyBinding = FragmentBuyBinding.inflate(inflater)

        fetchData("", "", "", false)

        buyBinding.searchTap.setOnClickListener {
            if (!searchable){
                buyBinding.buyLinearLayout.visibility = View.VISIBLE
                searchable = true
                buyBinding.searchTap.visibility = View.GONE
            }
        }

        val categoryAdapter =
            context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, categoryOptions) } as SpinnerAdapter
        buyBinding.searchSpinner.adapter = categoryAdapter
        buyBinding.searchSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    searchWithQuery = categoryOptions[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ToDo
                }

            }

        val searchWithAdapter =
            context?.let {ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, searchWithOptions)} as SpinnerAdapter
        buyBinding.searchWith.adapter = searchWithAdapter
        buyBinding.searchWith.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when {
                        searchWithOptions[position].toString() == "Name" -> {
                            buyBinding.searchEditText.visibility = View.VISIBLE
                            buyBinding.searchSpinner.visibility = View.GONE
                        }
                        searchWithOptions[position].toString() == "Category" -> {
                            buyBinding.searchEditText.visibility = View.GONE
                            buyBinding.searchSpinner.visibility = View.VISIBLE
                        }
                    }
                    searchWithSelection = searchWithOptions[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ToDo
                }

            }

        buyBinding.searchBtn.setOnClickListener {
            if (buyBinding.searchCity.text.isEmpty()){
                buyBinding.searchCity.error = "Please enter city name!!"
            } else {
                searchCity = buyBinding.searchCity.text.toString()
                when (searchWithSelection) {
                    "Search with" -> {
                        Toast.makeText(requireContext(), "Please select an option!!!", Toast.LENGTH_SHORT).show()
                    }
                    "Name" -> {
                        if (buyBinding.searchEditText.text.isEmpty()) {
                            buyBinding.searchEditText.error = "Please enter product name!!"
                        } else {
                            searchWithQuery = buyBinding.searchEditText.text.toString()
                            fetchData(searchWithSelection, searchWithQuery, searchCity, true)
                        }
                    }
                    "Category" -> {
                        if (searchWithQuery == "Select Category"){
                            Toast.makeText(requireContext(), "Please select an category!!!", Toast.LENGTH_SHORT).show()
                        } else {
                            fetchData(searchWithSelection, searchWithQuery, searchCity, true)
                        }
                    }
                }
            }
        }

        return buyBinding.root
    }

    private fun fetchData(
        searchWithSelection: String,
        searchWithQuery: String,
        searchCity: String,
        searchable: Boolean
    ) {
        buyBinding.buyRv.visibility = View.VISIBLE
        buyBinding.nothingToShowHereImage.visibility = View.GONE
        buyBinding.nothingToShowHereText.visibility = View.GONE
        productBuyArrayList.clear()
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
                    if (auth.uid!! != product.getString("uid").toString()) {
                        if (searchable) {
                            if ((product.getString("product$searchWithSelection").toString().contains(searchWithQuery, true))
                                and (product.getString("productOwnerCity").toString().contains(searchCity, true))) {
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
                        } else {
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
                    }
                }
                if (productBuyArrayList.isEmpty()) {
                    buyBinding.buyRv.visibility = View.GONE
                    buyBinding.nothingToShowHereImage.visibility = View.VISIBLE
                    buyBinding.nothingToShowHereText.visibility = View.VISIBLE
                }
                buyBinding.buyRv.layoutManager = LinearLayoutManager(context)
                buyBinding.buyRv.adapter = ProductBuyAdapter(productBuyArrayList, requireContext(), "ProductDetailActivity")
                buyBinding.progressBarProductBuy.visibility = View.GONE
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
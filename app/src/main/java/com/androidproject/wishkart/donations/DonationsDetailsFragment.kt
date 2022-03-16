package com.androidproject.wishkart.donations

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
import com.androidproject.wishkart.R
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
        donationsDetailsFragment = FragmentDonationsDetailsBinding.inflate(inflater)

        fetchData("", "", "", false)

        donationsDetailsFragment.searchTap.setOnClickListener {
            if (!searchable){
                donationsDetailsFragment.donateLinearLayout1.visibility = View.VISIBLE
                donationsDetailsFragment.donateLinearLayout2.visibility = View.VISIBLE
                searchable = true
                donationsDetailsFragment.searchTap.text = requireContext().getString(R.string.search_tap, "hide")
            } else {
                donationsDetailsFragment.donateLinearLayout1.visibility = View.GONE
                donationsDetailsFragment.donateLinearLayout2.visibility = View.GONE
                searchable = false
                donationsDetailsFragment.searchTap.text = requireContext().getString(R.string.search_tap, "show")
            }
        }

        val categoryAdapter =
            context?.let { ArrayAdapter(it, com.hbb20.R.layout.support_simple_spinner_dropdown_item, categoryOptions) } as SpinnerAdapter
        donationsDetailsFragment.searchSpinner.adapter = categoryAdapter
        donationsDetailsFragment.searchSpinner.onItemSelectedListener =
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
            context?.let { ArrayAdapter(it, com.hbb20.R.layout.support_simple_spinner_dropdown_item, searchWithOptions) } as SpinnerAdapter
        donationsDetailsFragment.searchWith.adapter = searchWithAdapter
        donationsDetailsFragment.searchWith.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when {
                        searchWithOptions[position].toString() == "Name" -> {
                            donationsDetailsFragment.searchEditText.visibility = View.VISIBLE
                            donationsDetailsFragment.searchSpinner.visibility = View.GONE
                        }
                        searchWithOptions[position].toString() == "Category" -> {
                            donationsDetailsFragment.searchEditText.visibility = View.GONE
                            donationsDetailsFragment.searchSpinner.visibility = View.VISIBLE
                        }
                    }
                    searchWithSelection = searchWithOptions[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ToDo
                }

            }

        donationsDetailsFragment.searchBtn.setOnClickListener {
            if (donationsDetailsFragment.searchCity.text.isEmpty()){
                donationsDetailsFragment.searchCity.error = "Please enter city name!!"
            } else {
                searchCity = donationsDetailsFragment.searchCity.text.toString()
                when (searchWithSelection) {
                    "Search with" -> {
                        Toast.makeText(requireContext(), "Please select an option!!!", Toast.LENGTH_SHORT).show()
                    }
                    "Name" -> {
                        if (donationsDetailsFragment.searchEditText.text.isEmpty()) {
                            donationsDetailsFragment.searchEditText.error = "Please enter product name!!"
                        } else {
                            searchWithQuery = donationsDetailsFragment.searchEditText.text.toString()
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

        return donationsDetailsFragment.root
    }

    private fun fetchData(
        searchWithSelection: String,
        searchWithQuery: String,
        searchCity: String,
        searchable: Boolean
    ) {
        donationsDetailsFragment.donationsRv.visibility = View.VISIBLE
        donationsDetailsFragment.nothingToShowHereImage.visibility = View.GONE
        donationsDetailsFragment.nothingToShowHereText.visibility = View.GONE
        productDonateArrayList.clear()
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
                        if (searchable) {
                            if ((product.getString("product$searchWithSelection").toString().contains(searchWithQuery, true))
                                and (product.getString("productOwnerCity").toString().contains(searchCity, true))) {
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
                        } else {
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
                }
                if (productDonateArrayList.isEmpty()) {
                    donationsDetailsFragment.donationsRv.visibility = View.GONE
                    donationsDetailsFragment.nothingToShowHereImage.visibility = View.VISIBLE
                    donationsDetailsFragment.nothingToShowHereText.visibility = View.VISIBLE
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
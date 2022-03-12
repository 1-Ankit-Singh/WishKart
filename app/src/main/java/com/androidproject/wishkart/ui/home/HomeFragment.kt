package com.androidproject.wishkart.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductAdapter
import com.androidproject.wishkart.adapter.ProductCategoryAdapter
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.FragmentHomeBinding
import com.androidproject.wishkart.donations.DonationsActivity
import com.androidproject.wishkart.model.Category
import com.androidproject.wishkart.model.ProductBuy
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    // Initializing Variables
    private lateinit var homeBinding: FragmentHomeBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var ref: DocumentReference = database.collection("users").document(auth.uid!!)
    private lateinit var userType: String
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null
    private var categoryArrayList = arrayListOf<Category>()
    private var productBuyArrayList = arrayListOf<ProductBuy>()
    private var productImagesArrayList = ArrayList<ProductImages>()
    private var productCategoryTextArrayList = arrayOf(
        "All",
        "Apparel",
        "Fashion",
        "Clothes",
        "Electronic Products",
        "Business Supply",
        "Mobile Phones",
        "Bags",
        "Cosmetics",
        "Food Item",
        "Art & Craft",
        "Jewelry",
        "Books",
        "Shoes",
        "Furniture",
        "Bedding Items",
        "Utensils",
        "Kitchen Appliances",
        "Homemade Perfumes",
        "Greeting Cards",
        "Lights and Bulbs",
        "Spices and Edible Items",
        "Handmade Toys",
        "Toys",
        "Pet Care Products",
        "Fitness Products",
        "Watches",
        "Desktops/Laptop",
        "Paper and Forest",
        "Baby products"
    )
    private var productCategoryImageArrayList = arrayOf(
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg",
        "https://cdn.britannica.com/77/170477-050-1C747EE3/Laptop-computer.jpg"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater)
        setImageSlider()
        setCategories()
        setProduct("All")
        checkUserType()
        return homeBinding.root
    }

    private fun setCategories() {
        for (i in productCategoryTextArrayList.indices) {
            categoryArrayList.add(
                Category(
                    productCategoryTextArrayList[i],
                    "https://noobpreneur.com/wp-content/uploads/2016/07/selling-guitar.jpg"
                )
            )
        }
        homeBinding.productCategories.adapter = ProductCategoryAdapter(
            categoryArrayList,
            requireContext(),
            object : ProductCategoryAdapter.CategoryClickInterface {
                override fun onCategoryClick(position: Int) {
                    val category = categoryArrayList[position].category
                    setProduct(category)
                }
            })
    }

    private fun setProduct(category: String) {
        productBuyArrayList.clear()
        database.collection("product")
            .get()
            .addOnSuccessListener {
                val list: List<DocumentSnapshot> = it.documents
                for (product in list) {
                    if (auth.uid!! != product.getString("uid").toString()) {
                        if ((category == "All") or (category == product.getString("productCategory")
                                .toString())
                        ) {
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
                // Creating and attaching Grid View
                homeBinding.productList.layoutManager = GridLayoutManager(context, 2)
                homeBinding.productList.adapter =
                    ProductAdapter(productBuyArrayList, requireContext())
            }
            .addOnFailureListener {
                Toast.makeText(
                    requireContext(),
                    "Something went wrong, Please try again!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setImageSlider() {
        val productImages = ProductImages(
            "https://noobpreneur.com/wp-content/uploads/2016/07/selling-guitar.jpg",
            "https://assets.entrepreneur.com/content/3x2/2000/20151211134026-garage-sale-second-hand-souvenirs-flea-market-selling.jpeg",
            "https://assets.entrepreneur.com/content/3x2/2000/1596216896-GettyImages-608166811.jpg",
            "https://thumbs.dreamstime.com/b/front-view-happy-girl-looking-camera-keeping-hand-mother-running-forward-big-toy-store-sming-woman-following-161261804.jpg"
        )
        productImagesArrayList.add(productImages)
        val productViewPagerAdapter =
            ProductViewPagerAdapter(productImagesArrayList, requireContext())
        homeBinding.productViewPager.adapter = productViewPagerAdapter
        dotsCount = productViewPagerAdapter.count
        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(requireContext())
            dots!![i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireActivity().applicationContext,
                    R.drawable.dots_default
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            homeBinding.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity().applicationContext,
                R.drawable.dots_active
            )
        )
        homeBinding.productViewPager.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                for (i in 0 until dotsCount) {
                    dots!![i]!!.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireActivity().applicationContext,
                            R.drawable.dots_default
                        )
                    )
                }
                dots!![position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity().applicationContext,
                        R.drawable.dots_active
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun checkUserType() {
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                if (auth.uid == it.get("uid")) {
                    userType = it.getString("userType").toString()
                    if (userType == "Individual") {
                        homeBinding.donations.visibility = View.GONE
                    }
                    if (userType == "NGO") {
                        homeBinding.donations.visibility = View.VISIBLE
                        homeBinding.donations.setOnClickListener {
                            activity?.startActivity(Intent(context, DonationsActivity::class.java))
                            activity?.finish()
                        }
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
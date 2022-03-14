package com.androidproject.wishkart.ui.buy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.ActivityProductDetailBinding
import com.androidproject.wishkart.model.ProductBuy
import com.androidproject.wishkart.model.ProductBuyer
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class ProductDetailActivity : AppCompatActivity() {
    // Initializing Variable
    private lateinit var productDetailActivity: ActivityProductDetailBinding
    private lateinit var productBuyerType: String
    private lateinit var productBuyerName: String
    private lateinit var productBuyerPhoneNumber: String
    private lateinit var productBuyerStreetAddress: String
    private lateinit var productBuyerCity: String
    private lateinit var productBuyerPinCode: String
    private lateinit var productBuyerCountry: String
    private lateinit var uid: String
    private lateinit var productOwnerCity: String
    private lateinit var productOwnerPinCode: String
    private lateinit var productOwnerCountry: String
    private lateinit var productName: String
    private lateinit var productCategory: String
    private lateinit var productMinPrice: String
    private lateinit var productMaxPrice: String
    private lateinit var productDescription: String
    private lateinit var productUrl1: String
    private lateinit var productUrl2: String
    private lateinit var productUrl3: String
    private lateinit var productUrl4: String
    private lateinit var productStatus: String
    private var productImagesArrayList = ArrayList<ProductImages>()
    private var dotsCount: Int = 0
    private var dots: Array<ImageView?>? = null
    private val database = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var ref: DocumentReference = database.collection("users").document(auth.uid!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailActivity = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(productDetailActivity.root)
        setSupportActionBar(productDetailActivity.toolbarProductDetails)

        getUserData()
        setDataInVariables()
        setData()
        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this, true)
        productDetailActivity.productViewPager.adapter = productViewPagerAdapter

        dotsCount = productViewPagerAdapter.count
        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots!![i] = ImageView(this)
            dots!![i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.dots_default
                )
            )
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            productDetailActivity.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        productDetailActivity.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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
                            applicationContext,
                            R.drawable.dots_default
                        )
                    )
                }
                dots!![position]!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.dots_active
                    )
                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        productDetailActivity.interested.setOnClickListener {
            val productBuy = ProductBuy(
                uid,
                productOwnerCity,
                productOwnerPinCode,
                productOwnerCountry,
                productName,
                productCategory,
                productMinPrice,
                productMaxPrice,
                productDescription,
                productUrl1,
                productUrl2,
                productUrl3,
                productUrl4,
                productStatus
            )
            database.collection("users/${auth.uid.toString()}/buy")
                .document("$productCategory$productName").set(productBuy)
                .addOnSuccessListener {
                    addToProductOwnerProductBuyerList()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun getUserData() {
        ref.get().addOnSuccessListener {
            productBuyerType = it.getString("userType").toString()
            productBuyerName = it.getString("userName").toString()
            productBuyerPhoneNumber = it.getString("userPhoneNumber").toString()
            productBuyerStreetAddress = it.getString("userStreetAddress").toString()
            productBuyerCity = it.getString("userCity").toString()
            productBuyerPinCode = it.getString("userPinCode").toString()
            productBuyerCountry = it.getString("userCountry").toString()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addToProductOwnerProductBuyerList() {
        val productBuyer = ProductBuyer(
            auth.uid!!,
            productBuyerType,
            productBuyerName,
            productBuyerPhoneNumber,
            productBuyerStreetAddress,
            productBuyerCity,
            productBuyerPinCode,
            productBuyerCountry,
            productName,
            productCategory,
            productMinPrice,
            productMaxPrice,
            productDescription,
            productUrl1,
            productUrl2,
            productUrl3,
            productUrl4,
            productStatus
        )
        database.collection("users/$uid/productBuyer")
            .document("$productCategory$productName").set(productBuyer)
            .addOnSuccessListener {
                upDateProductStatusInOwnerProductsList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInOwnerProductsList() {
        database.collection("users/$uid/products")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                upDateProductStatusInProductList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInProductList() {
        database.collection("product")
            .document(uid).update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Product Added to Interest List",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setData() {
        when (productStatus) {
            "Available" -> {
                productDetailActivity.availability.text =
                    getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                productDetailActivity.availability.text =
                    getString(R.string.availability, "Available")
            }
            else -> {
                productDetailActivity.availability.text =
                    getString(R.string.availability, "Donated")
            }
        }
        productDetailActivity.productName.text = productName
        productDetailActivity.productCategory.text = productCategory
        productDetailActivity.productPrice.text =
            getString(R.string.price, productMinPrice, productMaxPrice)
        productDetailActivity.productDescription.text = productDescription
        productDetailActivity.productLocation.text =
            getString(R.string.location, productOwnerCity, productOwnerCountry)
    }

    private fun setDataInVariables() {
        productName = intent.getStringExtra("productName").toString()
        productCategory = intent.getStringExtra("productCategory").toString()
        productMinPrice = intent.getStringExtra("productMinPrice").toString()
        productMaxPrice = intent.getStringExtra("productMaxPrice").toString()
        productDescription = intent.getStringExtra("productDescription").toString()
        productUrl1 = intent.getStringExtra("productUrl1").toString()
        productUrl2 = intent.getStringExtra("productUrl2").toString()
        productUrl3 = intent.getStringExtra("productUrl3").toString()
        productUrl4 = intent.getStringExtra("productUrl4").toString()
        productStatus = "Dealing"
        productOwnerCity = intent.getStringExtra("productOwnerCity").toString()
        productOwnerPinCode = intent.getStringExtra("productOwnerPinCode").toString()
        productOwnerCountry = intent.getStringExtra("productOwnerCountry").toString()
        uid = intent.getStringExtra("uid").toString()
        val productImages = ProductImages(productUrl1, productUrl2, productUrl3, productUrl4)
        productImagesArrayList.add(productImages)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
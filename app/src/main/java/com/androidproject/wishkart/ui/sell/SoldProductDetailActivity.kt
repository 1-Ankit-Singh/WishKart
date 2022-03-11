package com.androidproject.wishkart.ui.sell

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
import com.androidproject.wishkart.databinding.ActivitySoldProductDetailBinding
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SoldProductDetailActivity : AppCompatActivity() {
    // Initializing Variable
    private lateinit var soldProductDetailActivity: ActivitySoldProductDetailBinding
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soldProductDetailActivity = ActivitySoldProductDetailBinding.inflate(layoutInflater)
        setContentView(soldProductDetailActivity.root)
        setSupportActionBar(soldProductDetailActivity.toolbarSoldProductDetails)

        setDataInVariables()
        setData()
        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this)
        soldProductDetailActivity.productViewPager.adapter = productViewPagerAdapter

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
            soldProductDetailActivity.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        soldProductDetailActivity.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        soldProductDetailActivity.removeSoldProduct.setOnClickListener {
            database.collection("users/${auth.uid.toString()}/products")
                .document("$productCategory$productName").delete()
                .addOnSuccessListener {
                    FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {
                        deleteProductFromProductList()
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun deleteProductFromProductList() {
        database.collection("product").document(auth.uid!!).delete()
            .addOnSuccessListener {
                FirebaseAuth.getInstance().currentUser!!.delete().addOnCompleteListener {
                    Toast.makeText(
                        this,
                        "Product Removed Successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
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
                soldProductDetailActivity.availability.text =
                    getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                soldProductDetailActivity.availability.text =
                    getString(R.string.availability, "Available")
            }
            else -> {
                soldProductDetailActivity.availability.text =
                    getString(R.string.availability, "Donated")
            }
        }
        soldProductDetailActivity.productName.text = productName
        soldProductDetailActivity.productCategory.text = productCategory
        soldProductDetailActivity.productPrice.text =
            getString(R.string.price, productMinPrice, productMaxPrice)
        soldProductDetailActivity.productDescription.text = productDescription
        soldProductDetailActivity.productLocation.text =
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
        productStatus = intent.getStringExtra("productStatus").toString()
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
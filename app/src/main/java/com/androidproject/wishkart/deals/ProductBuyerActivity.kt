package com.androidproject.wishkart.deals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.ActivityProductBuyerBinding
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductBuyerActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productBuyerBinding: ActivityProductBuyerBinding
    private lateinit var productOwnerCity: String
    private lateinit var productOwnerPinCode: String
    private lateinit var productOwnerCountry: String
    private lateinit var productBuyerUid: String
    private lateinit var productBuyerType: String
    private lateinit var productBuyerName: String
    private lateinit var productBuyerPhoneNumber: String
    private lateinit var productBuyerStreetAddress: String
    private lateinit var productBuyerCity: String
    private lateinit var productBuyerPinCode: String
    private lateinit var productBuyerCountry: String
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
        productBuyerBinding = ActivityProductBuyerBinding.inflate(layoutInflater)
        setContentView(productBuyerBinding.root)
        setSupportActionBar(productBuyerBinding.toolbarProductBuyer)

        setDataInVariables()
        getData()

        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this)
        productBuyerBinding.productViewPager.adapter = productViewPagerAdapter

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
            productBuyerBinding.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        productBuyerBinding.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        productBuyerBinding.sold.setOnClickListener {
            productStatus = productBuyerUid
            productBuyerBinding.notInterested.visibility = View.GONE
            database.collection("product").document(auth.uid!!).delete()
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

        productBuyerBinding.notInterested.setOnClickListener {
            database.collection("users/${auth.uid}/productBuyer")
                .document("$productCategory$productName").delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Product successfully removed",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this, DealsActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun upDateProductStatusInOwnerProductsList() {
        database.collection("users/${auth.uid}/products")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                upDateProductStatusInBuyerBuyList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInBuyerBuyList() {
        database.collection("users/$productBuyerUid/buy")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                upDateProductStatusInOwnerProductBuyerList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInOwnerProductBuyerList() {
        database.collection("users/${auth.uid}/productBuyer")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Product marked as sold",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, DealsActivity::class.java))
                finish()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getData() {
        database.collection("users").document(auth.uid!!).get().addOnSuccessListener {
            productOwnerCity = it.getString("userCity").toString()
            productOwnerPinCode = it.getString("userPinCode").toString()
            productOwnerCountry = it.getString("userCountry").toString()
            setData()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setData() {
        when (productStatus) {
            "Available" -> {
                productBuyerBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                productBuyerBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            else -> {
                productBuyerBinding.availability.text =
                    getString(R.string.availability, "Donated")
            }
        }
        productBuyerBinding.productName.text = productName
        productBuyerBinding.productCategory.text = productCategory
        productBuyerBinding.productPrice.text = getString(R.string.price, productMinPrice, productMaxPrice)
        productBuyerBinding.productDescription.text = productDescription
        productBuyerBinding.productLocation.text = getString(R.string.location, productOwnerCity, productOwnerCountry)
        productBuyerBinding.productBuyerName.text = productBuyerName
        productBuyerBinding.productBuyerPhoneNumber.text = productBuyerPhoneNumber
        productBuyerBinding.productBuyerAddress.text =
            getString(R.string.address,
                productBuyerStreetAddress,
                productBuyerCity,
                productBuyerPinCode,
                productBuyerCountry
            )
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
        productBuyerUid = intent.getStringExtra("productBuyerUid").toString()
        productBuyerType = intent.getStringExtra("productBuyerType").toString()
        productBuyerName = intent.getStringExtra("productBuyerName").toString()
        productBuyerPhoneNumber = intent.getStringExtra("productBuyerPhoneNumber").toString()
        productBuyerStreetAddress = intent.getStringExtra("productBuyerStreetAddress").toString()
        productBuyerCity = intent.getStringExtra("productBuyerCity").toString()
        productBuyerPinCode = intent.getStringExtra("productBuyerPinCode").toString()
        productBuyerCountry = intent.getStringExtra("productBuyerCountry").toString()
        val productImages = ProductImages(productUrl1, productUrl2, productUrl3, productUrl4)
        productImagesArrayList.add(productImages)
        if ((productStatus != "Available") and (productStatus != "Dealing")) {
            productBuyerBinding.notInterested.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, DealsActivity::class.java))
        finish()
    }
}
package com.androidproject.wishkart.donations

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.ActivityDonationsDetailsBinding
import com.androidproject.wishkart.model.ProductDonate
import com.androidproject.wishkart.model.ProductGetter
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class DonationsDetailsActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var donationsDetailsBinding: ActivityDonationsDetailsBinding
    private lateinit var productGetterType: String
    private lateinit var productGetterName: String
    private lateinit var productGetterPhoneNumber: String
    private lateinit var productGetterStreetAddress: String
    private lateinit var productGetterCity: String
    private lateinit var productGetterPinCode: String
    private lateinit var productGetterCountry: String
    private lateinit var productGetterCertificateUrl: String
    private lateinit var productGetterCertificateNumber: String
    private lateinit var uid: String
    private lateinit var productOwnerCity: String
    private lateinit var productOwnerPinCode: String
    private lateinit var productOwnerCountry: String
    private lateinit var productName: String
    private lateinit var productCategory: String
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
        donationsDetailsBinding = ActivityDonationsDetailsBinding.inflate(layoutInflater)
        setContentView(donationsDetailsBinding.root)
        setSupportActionBar(donationsDetailsBinding.toolbarDonationsDetails)

        getUserData()
        setDataInVariables()
        setData()
        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this)
        donationsDetailsBinding.productViewPager.adapter = productViewPagerAdapter

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
            donationsDetailsBinding.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        donationsDetailsBinding.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        donationsDetailsBinding.interested.setOnClickListener {
            val productDonate = ProductDonate(
                uid,
                productOwnerCity,
                productOwnerPinCode,
                productOwnerCountry,
                productName,
                productCategory,
                productDescription,
                productUrl1,
                productUrl2,
                productUrl3,
                productUrl4,
                productStatus
            )
            database.collection("users/${auth.uid.toString()}/donationInterest")
                .document("$productCategory$productName").set(productDonate)
                .addOnSuccessListener {
                    addToOwnerProductGetterList()
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
            productGetterType = it.getString("userType").toString()
            productGetterName = it.getString("userName").toString()
            productGetterPhoneNumber = it.getString("userPhoneNumber").toString()
            productGetterStreetAddress = it.getString("userStreetAddress").toString()
            productGetterCity = it.getString("userCity").toString()
            productGetterPinCode = it.getString("userPinCode").toString()
            productGetterCountry = it.getString("userCountry").toString()
            productGetterCertificateUrl = it.getString("userCertificateUrl").toString()
            productGetterCertificateNumber = it.getString("userCertificateNumber").toString()
        }.addOnFailureListener {
            Toast.makeText(
                this,
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addToOwnerProductGetterList() {
        val productGetter = ProductGetter(
            auth.uid!!,
            productGetterType,
            productGetterName,
            productGetterPhoneNumber,
            productGetterStreetAddress,
            productGetterCity,
            productGetterPinCode,
            productGetterCountry,
            productGetterCertificateUrl,
            productGetterCertificateNumber,
            productName,
            productCategory,
            productDescription,
            productUrl1,
            productUrl2,
            productUrl3,
            productUrl4,
            productStatus
        )
        database.collection("users/$uid/productGetter")
            .document("$productCategory$productName").set(productGetter)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Product Added to Buy List",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this, DonationsActivity::class.java))
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
        donationsDetailsBinding.productName.text = productName
        donationsDetailsBinding.productCategory.text = productCategory
        donationsDetailsBinding.productDescription.text = productDescription
        donationsDetailsBinding.productLocation.text =
            getString(R.string.location, productOwnerCity, productOwnerCountry)
    }

    private fun setDataInVariables() {
        productName = intent.getStringExtra("productName").toString()
        productCategory = intent.getStringExtra("productCategory").toString()
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
        startActivity(Intent(this, DonationsActivity::class.java))
        finish()
    }
}
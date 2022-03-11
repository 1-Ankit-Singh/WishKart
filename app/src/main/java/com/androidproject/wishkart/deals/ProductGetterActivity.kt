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
import com.androidproject.wishkart.databinding.ActivityProductGetterBinding
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ProductGetterActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productGetterBinding: ActivityProductGetterBinding
    private lateinit var productOwnerCity: String
    private lateinit var productOwnerPinCode: String
    private lateinit var productOwnerCountry: String
    private lateinit var productGetterUid: String
    private lateinit var productGetterType: String
    private lateinit var productGetterName: String
    private lateinit var productGetterPhoneNumber: String
    private lateinit var productGetterStreetAddress: String
    private lateinit var productGetterCity: String
    private lateinit var productGetterPinCode: String
    private lateinit var productGetterCountry: String
    private lateinit var productGetterCertificateUrl: String
    private lateinit var productGetterCertificateNumber: String
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productGetterBinding = ActivityProductGetterBinding.inflate(layoutInflater)
        setContentView(productGetterBinding.root)
        setSupportActionBar(productGetterBinding.toolbarProductGetter)

        setDataInVariables()
        getData()

        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this)
        productGetterBinding.productViewPager.adapter = productViewPagerAdapter

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
            productGetterBinding.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        productGetterBinding.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        productGetterBinding.donated.setOnClickListener {
            productStatus = productGetterUid
            productGetterBinding.notInterested.visibility = View.GONE
            database.collection("donate").document(auth.uid!!).delete()
                .addOnSuccessListener {
                    upDateProductStatusInOwnerDonateList()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        productGetterBinding.notInterested.setOnClickListener {
            database.collection("users/${auth.uid}/productGetter")
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

    private fun upDateProductStatusInOwnerDonateList() {
        database.collection("users/${auth.uid}/donate")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                upDateProductStatusInGetterDonationInterestList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInGetterDonationInterestList() {
        database.collection("users/$productGetterUid/donationInterest")
            .document("$productCategory$productName").update(
                "productStatus", productStatus
            ).addOnSuccessListener {
                upDateProductStatusInOwnerProductGetterList()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun upDateProductStatusInOwnerProductGetterList() {
        database.collection("users/${auth.uid}/productGetter")
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
                productGetterBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                productGetterBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            else -> {
                productGetterBinding.availability.text =
                    getString(R.string.availability, "Donated")
            }
        }
        productGetterBinding.productName.text = productName
        productGetterBinding.productCategory.text = productCategory
        productGetterBinding.productDescription.text = productDescription
        productGetterBinding.productLocation.text = getString(R.string.location, productOwnerCity, productOwnerCountry)
        productGetterBinding.ngoName.text = productGetterName
        productGetterBinding.ngoPhoneNumber.text = productGetterPhoneNumber
        productGetterBinding.ngoAddress.text =
            getString(R.string.address,
                productGetterStreetAddress,
                productGetterCity,
                productGetterPinCode,
                productGetterCountry
            )
        Picasso.get().load(productGetterCertificateUrl).into(productGetterBinding.ngoCertificateImage)
        productGetterBinding.ngoCertificateNumber.text = productGetterCertificateNumber
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
        productGetterUid = intent.getStringExtra("productGetterUid").toString()
        productGetterType = intent.getStringExtra("productGetterType").toString()
        productGetterName = intent.getStringExtra("productGetterName").toString()
        productGetterPhoneNumber = intent.getStringExtra("productGetterPhoneNumber").toString()
        productGetterStreetAddress = intent.getStringExtra("productGetterStreetAddress").toString()
        productGetterCity = intent.getStringExtra("productGetterCity").toString()
        productGetterPinCode = intent.getStringExtra("productGetterPinCode").toString()
        productGetterCountry = intent.getStringExtra("productGetterCountry").toString()
        productGetterCertificateUrl = intent.getStringExtra("productGetterCertificateUrl").toString()
        productGetterCertificateNumber = intent.getStringExtra("productGetterCertificateNumber").toString()
        val productImages = ProductImages(productUrl1, productUrl2, productUrl3, productUrl4)
        productImagesArrayList.add(productImages)
        if ((productStatus != "Available") or (productStatus != "Dealing")) {
            productGetterBinding.notInterested.visibility = View.GONE
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
package com.androidproject.wishkart.deals

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.ActivityProductSellerBinding
import com.androidproject.wishkart.model.ProductImages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProductSellerActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var productSellerBinding: ActivityProductSellerBinding
    private lateinit var uid: String
    private lateinit var productOwnerName: String
    private lateinit var productOwnerPhoneNumber: String
    private lateinit var productOwnerAddress: String
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
        productSellerBinding = ActivityProductSellerBinding.inflate(layoutInflater)
        setContentView(productSellerBinding.root)
        setSupportActionBar(productSellerBinding.toolbarProductSeller)

        setDataInVariables()
        getData()

        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList, this)
        productSellerBinding.productViewPager.adapter = productViewPagerAdapter

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
            productSellerBinding.sliderDots.addView(dots!![i], params)
        }
        dots!![0]!!.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.dots_active))
        productSellerBinding.productViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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

        productSellerBinding.notInterested.setOnClickListener {
            database.collection("users/${auth.uid.toString()}/buy")
                .document("$productCategory$productName").delete()
                .addOnSuccessListener {
                    deleteToProductOwnerProductBuyerList()
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Something went wrong, Please try again !!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun checkSellerInterest() {
        database.collection("users/$uid/productBuyer")
            .document("$productCategory$productName").get().addOnSuccessListener {
                if (!(it.exists())) {
                    showAlertDialogBox("Oops", "It looks Seller is not interested to sell their product to you. " +
                            "Do you want to keep this product in your list or want to remove it?")
                }
                if (it.exists()) {
                   if (productStatus == auth.uid) {
                        showAlertDialogBox("Congratulations", "Donor donated the product to you. " +
                                "Do you want to keep this product in your list or want to remove the record?")
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun showAlertDialogBox(title:String, message: String) {
        //Instantiate builder variable
        val builder = AlertDialog.Builder(this)
        // set title
        builder.setTitle(title)
        //set content area
        builder.setMessage(message)
        //set negative button
        builder.setPositiveButton(
            "Remove"
        ) { _, _ ->
            database.collection("users/${auth.uid.toString()}/buy")
                .document("$productCategory$productName").delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Product successfully removed from your list",
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
        //set positive button
        builder.setNegativeButton(
            "Keep"
        ) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun deleteToProductOwnerProductBuyerList() {
        database.collection("users/$uid/productBuyer")
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

    private fun getData() {
        database.collection("users").document(uid).get().addOnSuccessListener {
            productOwnerName = it.getString("userName").toString()
            productOwnerPhoneNumber = it.getString("userPhoneNumber").toString()
            productOwnerAddress =
                getString(R.string.address,
                    it.getString("userStreetAddress").toString(),
                    it.getString("userCity").toString(),
                    it.getString("userPinCode").toString(),
                    it.getString("userCountry").toString()
                )
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
                productSellerBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                productSellerBinding.availability.text =
                    getString(R.string.availability, "Available")
            }
            else -> {
                productSellerBinding.availability.text =
                    getString(R.string.availability, "Donated")
                productSellerBinding.notInterested.visibility = View.GONE
            }
        }
        productSellerBinding.productName.text = productName
        productSellerBinding.productCategory.text = productCategory
        productSellerBinding.productPrice.text = getString(R.string.price, productMinPrice, productMaxPrice)
        productSellerBinding.productDescription.text = productDescription
        productSellerBinding.productLocation.text = getString(R.string.location, productOwnerCity, productOwnerCountry)
        productSellerBinding.productOwnerName.text = productOwnerName
        productSellerBinding.productOwnerPhoneNumber.text = productOwnerPhoneNumber
        productSellerBinding.productOwnerAddress.text = productOwnerAddress
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
        checkSellerInterest()
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
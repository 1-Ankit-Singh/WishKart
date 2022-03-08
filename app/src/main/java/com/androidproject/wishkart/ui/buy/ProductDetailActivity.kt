package com.androidproject.wishkart.ui.buy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.R
import com.androidproject.wishkart.adapter.ProductViewPagerAdapter
import com.androidproject.wishkart.databinding.ActivityProductDetailBinding
import com.androidproject.wishkart.model.ProductImages

class ProductDetailActivity : AppCompatActivity() {
    // Initializing Variable
    private lateinit var productDetailActivity: ActivityProductDetailBinding
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
    private var productImagesArrayList = arrayListOf<ProductImages>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productDetailActivity = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(productDetailActivity.root)
        setSupportActionBar(productDetailActivity.toolbarProductDetails)

        setDataInVariables()
        setData()
        addImages()

        productDetailActivity.interested.setOnClickListener {

        }
    }

    private fun addImages() {
        val productViewPagerAdapter = ProductViewPagerAdapter(productImagesArrayList)
        productDetailActivity.productViewPager.adapter = productViewPagerAdapter
        /*TabLayoutMediator(
            productDetailActivity.tabDots,
            productDetailActivity.productViewPager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->

            }).attach()*/
        /*productDetailActivity.productViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Toast.makeText(this@ProductDetailActivity, position, Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    private fun setData() {
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
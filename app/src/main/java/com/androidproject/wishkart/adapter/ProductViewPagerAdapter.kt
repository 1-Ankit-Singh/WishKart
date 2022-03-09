package com.androidproject.wishkart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.androidproject.wishkart.databinding.ListProductImagesBinding
import com.androidproject.wishkart.model.ProductImages
import com.squareup.picasso.Picasso

class ProductViewPagerAdapter(
    productImages: ArrayList<ProductImages>,
    val context: Context
) : PagerAdapter() {

    private val productImageSlider = arrayOf(
        productImages[0].productUrl1,
        productImages[0].productUrl2,
        productImages[0].productUrl3,
        productImages[0].productUrl4
    )

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val slider = ListProductImagesBinding.inflate(LayoutInflater.from(container.context))
        Picasso.get().load(productImageSlider[position]).into(slider.productImages)
        container.addView(slider.root)
        return slider.root
    }

    override fun getCount(): Int {
        return productImageSlider.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
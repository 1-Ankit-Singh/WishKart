package com.androidproject.wishkart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.androidproject.wishkart.databinding.ListProductImagesBinding
import com.androidproject.wishkart.model.ProductImages
import com.squareup.picasso.Picasso

class ProductViewPagerAdapter(
    private val productImages: ArrayList<ProductImages>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val productImageSlider = arrayOf(
            productImages[position].productUrl1,
            productImages[position].productUrl2,
            productImages[position].productUrl3,
            productImages[position].productUrl4
        )
        val slider = ListProductImagesBinding.inflate(LayoutInflater.from(container.context))
        Picasso.get().load(productImageSlider[position]).into(slider.productSlidingImage)
        container.addView(slider.root)
        return slider.root
    }

    override fun getCount(): Int {
        return productImages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
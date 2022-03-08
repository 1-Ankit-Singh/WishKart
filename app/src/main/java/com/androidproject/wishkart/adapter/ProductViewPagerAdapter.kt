package com.androidproject.wishkart.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.androidproject.wishkart.databinding.ListProductImagesBinding
import com.androidproject.wishkart.model.ProductImages
import com.squareup.picasso.Picasso

/*class ProductViewPagerAdapter(
    val context: Context,
    private val productImagesArrayList: ArrayList<ProductImages>
) : RecyclerView.Adapter<ProductViewPagerAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productViewPagerAdapter = ListProductImagesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewPagerAdapter.ViewHolder {
        /*val adapter = ListProductImagesBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)*/
        return  ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_product_images, parent, false))
    }

    override fun onBindViewHolder(holder: ProductViewPagerAdapter.ViewHolder, position: Int) {
        val productImageSlider = arrayOf(
            productImagesArrayList[position].productUrl1,
            productImagesArrayList[position].productUrl2,
            productImagesArrayList[position].productUrl3,
            productImagesArrayList[position].productUrl4
        )
        Picasso.get().load(productImageSlider[position]).into(holder.productViewPagerAdapter.productSlidingImage)
    }

    override fun getItemCount(): Int {
        return productImagesArrayList.size
    }

}*/

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
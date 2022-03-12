package com.androidproject.wishkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.databinding.ListProductsImagesBinding
import com.androidproject.wishkart.model.ProductBuy
import com.androidproject.wishkart.ui.buy.ProductDetailActivity
import com.bumptech.glide.Glide

class ProductAdapter(
    private val productsArrayList: ArrayList<ProductBuy>,
    val context: Context,
) : RecyclerView.Adapter<ProductAdapter.Data>() {

    private lateinit var intent: Intent

    class Data(v: View) : RecyclerView.ViewHolder(v) {
        var customProductAdapter = ListProductsImagesBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Data {
        val adapter = ListProductsImagesBinding.inflate(LayoutInflater.from(parent.context))
        return Data(adapter.root)
    }

    override fun onBindViewHolder(holder: Data, position: Int) {
        Glide.with(holder.customProductAdapter.root).load(productsArrayList[position].productUrl1)
            .into(holder.customProductAdapter.productImage)
        holder.itemView.setOnClickListener {
            intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("productName", productsArrayList[position].productName)
            intent.putExtra("productCategory", productsArrayList[position].productCategory)
            intent.putExtra("productMinPrice", productsArrayList[position].productMinPrice)
            intent.putExtra("productMaxPrice", productsArrayList[position].productMaxPrice)
            intent.putExtra("productDescription", productsArrayList[position].productDescription)
            intent.putExtra("productUrl1", productsArrayList[position].productUrl1)
            intent.putExtra("productUrl2", productsArrayList[position].productUrl2)
            intent.putExtra("productUrl3", productsArrayList[position].productUrl3)
            intent.putExtra("productUrl4", productsArrayList[position].productUrl4)
            intent.putExtra("productStatus", productsArrayList[position].productStatus)
            intent.putExtra("productOwnerCity", productsArrayList[position].productOwnerCity)
            intent.putExtra("productOwnerPinCode", productsArrayList[position].productOwnerPinCode)
            intent.putExtra("productOwnerCountry", productsArrayList[position].productOwnerCountry)
            intent.putExtra("uid", productsArrayList[position].uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productsArrayList.size
    }
}
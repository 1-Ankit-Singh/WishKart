package com.androidproject.wishkart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductItemBinding
import com.androidproject.wishkart.model.ProductSell
import com.squareup.picasso.Picasso

class ProductSellAdapter(
    private val productsArrayList: ArrayList<ProductSell>,
    val context: Context
) : RecyclerView.Adapter<ProductSellAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productSellAdapter = ListProductItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapter = ListProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productSellAdapter.productName.text = productsArrayList[position].productName
        holder.productSellAdapter.productCategory.text = productsArrayList[position].productCategory
        holder.productSellAdapter.productPrice.text =
            context.getString(R.string.price, productsArrayList[position].productMinPrice, productsArrayList[position].productMaxPrice)
        holder.productSellAdapter.location.text =
            context.getString(R.string.location, productsArrayList[position].productOwnerCity, productsArrayList[position].productOwnerCountry)
        Picasso.get()
            .load(productsArrayList[position].productUrl1)
            .into(holder.productSellAdapter.product)
        holder.itemView.setOnClickListener {
            //val intent = Intent(context, NewsDetailsActivity::class.java)
            //intent.putExtra("title", productsArrayList[position].productName)
            //context.startActivity(intent)
            Toast.makeText(context, productsArrayList[position].productName, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun getItemCount(): Int {
        return productsArrayList.size
    }

}
package com.androidproject.wishkart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductItemBinding
import com.androidproject.wishkart.model.ProductBuy
import com.squareup.picasso.Picasso

class ProductBuyAdapter(
    private val productsArrayList: ArrayList<ProductBuy>,
    val context: Context
) : RecyclerView.Adapter<ProductBuyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productBuyAdapter = ListProductItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapter = ListProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productBuyAdapter.productName.text = productsArrayList[position].productName
        holder.productBuyAdapter.productCategory.text = productsArrayList[position].productCategory
        holder.productBuyAdapter.productPrice.text =
            context.getString(
                R.string.price,
                productsArrayList[position].productMinPrice,
                productsArrayList[position].productMaxPrice
            )
        holder.productBuyAdapter.location.text =
            context.getString(
                R.string.location,
                productsArrayList[position].productOwnerCity,
                productsArrayList[position].productOwnerCountry
            )
        Picasso.get()
            .load(productsArrayList[position].productUrl1)
            .into(holder.productBuyAdapter.product)
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
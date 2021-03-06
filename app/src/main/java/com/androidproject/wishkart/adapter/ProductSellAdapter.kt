package com.androidproject.wishkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductItemBinding
import com.androidproject.wishkart.model.ProductSell
import com.androidproject.wishkart.ui.sell.SoldProductDetailActivity
import com.squareup.picasso.Picasso

class ProductSellAdapter(
    private val productsArrayList: ArrayList<ProductSell>,
    val context: Context,
    private val clazz: String
) : RecyclerView.Adapter<ProductSellAdapter.ViewHolder>() {

    private lateinit var intent: Intent

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
            context.getString(
                R.string.price,
                productsArrayList[position].productMinPrice,
                productsArrayList[position].productMaxPrice
            )
        when (productsArrayList[position].productStatus) {
            "Available" -> {
                holder.productSellAdapter.productAvailability.text =
                    context.getString(R.string.availability, "Available")
            }
            "Dealing" -> {
                holder.productSellAdapter.productAvailability.text =
                    context.getString(R.string.availability, "Available")
            }
            else -> {
                holder.productSellAdapter.productAvailability.text =
                    context.getString(R.string.availability, "Sold")
            }
        }
        holder.productSellAdapter.location.text =
            context.getString(
                R.string.location,
                productsArrayList[position].productOwnerCity,
                productsArrayList[position].productOwnerCountry
            )
        Picasso.get()
            .load(productsArrayList[position].productUrl1)
            .into(holder.productSellAdapter.product)
        holder.itemView.setOnClickListener {
            if (clazz == "SoldProductDetailActivity") {
                intent = Intent(context, SoldProductDetailActivity::class.java)
            }
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
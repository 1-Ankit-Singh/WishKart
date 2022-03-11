package com.androidproject.wishkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductNotificationBinding
import com.androidproject.wishkart.deals.ProductBuyerActivity
import com.androidproject.wishkart.model.ProductBuyer
import java.util.*

class ProductBuyerAdapter(
    private val productBuyerArrayList: ArrayList<ProductBuyer>,
    val context: Context,
    private val clazz: String
) : RecyclerView.Adapter<ProductBuyerAdapter.ViewHolder>() {

    private lateinit var intent: Intent

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productBuyerAdapter = ListProductNotificationBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapter = ListProductNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val colors = context.resources.getIntArray(R.array.random_color)
        val randomColor = colors[Random().nextInt(colors.size)]
        holder.productBuyerAdapter.viewColorTag.setBackgroundColor(randomColor)
        holder.productBuyerAdapter.productNotification.text = context.getString(
            R.string.someone_showed_interest,
            productBuyerArrayList[position].productBuyerName,
            productBuyerArrayList[position].productName
        )
        holder.itemView.setOnClickListener {
            if (clazz == "ProductBuyerActivity") {
                intent = Intent(context, ProductBuyerActivity::class.java)
            }
            intent.putExtra("productName", productBuyerArrayList[position].productName)
            intent.putExtra("productCategory", productBuyerArrayList[position].productCategory)
            intent.putExtra("productMinPrice", productBuyerArrayList[position].productMinPrice)
            intent.putExtra("productMaxPrice", productBuyerArrayList[position].productMaxPrice)
            intent.putExtra("productDescription", productBuyerArrayList[position].productDescription)
            intent.putExtra("productUrl1", productBuyerArrayList[position].productUrl1)
            intent.putExtra("productUrl2", productBuyerArrayList[position].productUrl2)
            intent.putExtra("productUrl3", productBuyerArrayList[position].productUrl3)
            intent.putExtra("productUrl4", productBuyerArrayList[position].productUrl4)
            intent.putExtra("productStatus", productBuyerArrayList[position].productStatus)
            intent.putExtra("productBuyerUid", productBuyerArrayList[position].productBuyerUid)
            intent.putExtra("productBuyerType", productBuyerArrayList[position].productBuyerType)
            intent.putExtra("productBuyerName", productBuyerArrayList[position].productBuyerName)
            intent.putExtra("productBuyerPhoneNumber", productBuyerArrayList[position].productBuyerPhoneNumber)
            intent.putExtra("productBuyerStreetAddress", productBuyerArrayList[position].productBuyerStreetAddress)
            intent.putExtra("productBuyerCity", productBuyerArrayList[position].productBuyerCity)
            intent.putExtra("productBuyerPinCode", productBuyerArrayList[position].productBuyerPinCode)
            intent.putExtra("productBuyerCountry", productBuyerArrayList[position].productBuyerCountry)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productBuyerArrayList.size
    }
}
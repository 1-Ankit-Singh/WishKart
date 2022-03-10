package com.androidproject.wishkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductNotificationBinding
import com.androidproject.wishkart.deals.ProductGetterActivity
import com.androidproject.wishkart.model.ProductGetter

class ProductGetterAdapter(
    private val productGetterArrayList: ArrayList<ProductGetter>,
    val context: Context,
    private val clazz: String
) : RecyclerView.Adapter<ProductGetterAdapter.ViewHolder>() {

    private lateinit var intent: Intent

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productGetterAdapter = ListProductNotificationBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapter = ListProductNotificationBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productGetterAdapter.productNotification.text = context.getString(
            R.string.someone_showed_interest,
            productGetterArrayList[position].productGetterName,
            productGetterArrayList[position].productName
        )
        holder.itemView.setOnClickListener {
            if (clazz == "ProductGetterActivity") {
                intent = Intent(context, ProductGetterActivity::class.java)
            }
            intent.putExtra("productName", productGetterArrayList[position].productName)
            intent.putExtra("productCategory", productGetterArrayList[position].productCategory)
            intent.putExtra("productDescription", productGetterArrayList[position].productDescription)
            intent.putExtra("productUrl1", productGetterArrayList[position].productUrl1)
            intent.putExtra("productUrl2", productGetterArrayList[position].productUrl2)
            intent.putExtra("productUrl3", productGetterArrayList[position].productUrl3)
            intent.putExtra("productUrl4", productGetterArrayList[position].productUrl4)
            intent.putExtra("productStatus", productGetterArrayList[position].productStatus)
            intent.putExtra("productGetterUid", productGetterArrayList[position].productGetterUid)
            intent.putExtra("productGetterType", productGetterArrayList[position].productGetterType)
            intent.putExtra("productGetterName", productGetterArrayList[position].productGetterName)
            intent.putExtra("productGetterPhoneNumber", productGetterArrayList[position].productGetterPhoneNumber)
            intent.putExtra("productGetterStreetAddress", productGetterArrayList[position].productGetterStreetAddress)
            intent.putExtra("productGetterCity", productGetterArrayList[position].productGetterCity)
            intent.putExtra("productGetterPinCode", productGetterArrayList[position].productGetterPinCode)
            intent.putExtra("productGetterCountry", productGetterArrayList[position].productGetterCountry)
            intent.putExtra("productGetterCertificateUrl", productGetterArrayList[position].productGetterCertificateUrl)
            intent.putExtra("productGetterCertificateNumber", productGetterArrayList[position].productGetterCertificateNumber)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return productGetterArrayList.size
    }
}
package com.androidproject.wishkart.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductItemBinding
import com.androidproject.wishkart.donations.DonationsDetailsActivity
import com.androidproject.wishkart.donations.InterestDetailsActivity
import com.androidproject.wishkart.model.ProductDonate
import com.androidproject.wishkart.ui.donate.DonatedProductDetailActivity
import com.squareup.picasso.Picasso

class ProductDonateAdapter(
    private val productsArrayList: ArrayList<ProductDonate>,
    val context: Context,
    private val clazz: String
) : RecyclerView.Adapter<ProductDonateAdapter.ViewHolder>() {

    private lateinit var intent: Intent

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productDonateAdapter = ListProductItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val adapter = ListProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productDonateAdapter.productPrice.visibility = View.GONE
        holder.productDonateAdapter.productName.text = productsArrayList[position].productName
        holder.productDonateAdapter.productCategory.text = productsArrayList[position].productCategory
        holder.productDonateAdapter.location.text =
            context.getString(
                R.string.location,
                productsArrayList[position].productOwnerCity,
                productsArrayList[position].productOwnerCountry
            )
        Picasso.get()
            .load(productsArrayList[position].productUrl1)
            .into(holder.productDonateAdapter.product)
        holder.itemView.setOnClickListener {
            when (clazz) {
                "DonatedProductDetailActivity" -> {
                    intent = Intent(context, DonatedProductDetailActivity::class.java)
                }
                "DonationsDetailsActivity" -> {
                    intent = Intent(context, DonationsDetailsActivity::class.java)
                }
                "InterestDetailsActivity" -> {
                    intent = Intent(context, InterestDetailsActivity::class.java)
                }
            }
            intent.putExtra("productName", productsArrayList[position].productName)
            intent.putExtra("productCategory", productsArrayList[position].productCategory)
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
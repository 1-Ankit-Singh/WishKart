package com.androidproject.wishkart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.ListProductItemBinding
import com.androidproject.wishkart.model.ProductDonate
import com.squareup.picasso.Picasso

class ProductDonateAdapter(
    private val productsArrayList: ArrayList<ProductDonate>,
    val context: Context
) : RecyclerView.Adapter<ProductDonateAdapter.ViewHolder>() {

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
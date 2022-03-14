package com.androidproject.wishkart.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidproject.wishkart.databinding.ListProductCategoriesBinding
import com.androidproject.wishkart.model.Category
import com.squareup.picasso.Picasso

class ProductCategoryAdapter(
    private val categoryAdapterArrayList: ArrayList<Category>,
    val context: Context,
    private val categoryClickInterface: CategoryClickInterface
) : RecyclerView.Adapter<ProductCategoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryAdapter = ListProductCategoriesBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapter = ListProductCategoriesBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(adapter.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryAdapter.categoryTitle.text = categoryAdapterArrayList[position].category
        Picasso.get()
            .load(categoryAdapterArrayList[position].categoryImageID)
            .into(holder.categoryAdapter.categoryImage)
        holder.itemView.setOnClickListener {
            categoryClickInterface.onCategoryClick(position)
        }
    }

    override fun getItemCount(): Int {
        return categoryAdapterArrayList.size
    }

    interface CategoryClickInterface {
        fun onCategoryClick(position: Int)
    }
}
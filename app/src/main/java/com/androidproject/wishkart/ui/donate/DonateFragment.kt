package com.androidproject.wishkart.ui.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.databinding.FragmentDonateBinding
import com.hbb20.R

class DonateFragment : Fragment() {
    // Initializing Variables
    private lateinit var donateBinding: FragmentDonateBinding
    private var categoryOptions = arrayListOf<Any>("A-", "A+", "B-", "B+", "AB-", "AB+", "O-", "O+")
    var category: String = "None"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        donateBinding = FragmentDonateBinding.inflate(inflater)

        val bloodGroupAdapter =
            context?.let { ArrayAdapter(it, R.layout.support_simple_spinner_dropdown_item, categoryOptions) } as SpinnerAdapter
        donateBinding.category.adapter = bloodGroupAdapter
        donateBinding.category.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = categoryOptions[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ToDo
                }

            }

        return donateBinding.root
    }
}
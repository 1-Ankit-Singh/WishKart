package com.androidproject.wishkart.ui.help

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.R
import com.androidproject.wishkart.databinding.FragmentHelpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class HelpFragment : Fragment() {
    // Initializing Variables
    private lateinit var helpBinding: FragmentHelpBinding
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var ref: DocumentReference = database.collection("users").document(auth.uid!!)
    private lateinit var userType: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        helpBinding = FragmentHelpBinding.inflate(inflater)
        checkUserType()
        helpBinding.loginAndProfile.setOnClickListener {
            val loginAndProfile =
                "1. You must specify yourself as Individual or NGO." +
                        "\n2. You must register using their phone number." +
                        "\n3. You must create their profile as per below instructions:" +
                        "\n     I. As Individual user, yoy must provide your name, street address, city, pincode and country." +
                        "\n     II. As NGO, you must provide your name, street address, city, pincode, country, certificate " +
                        "picture and certificate number." +
                        "\n4. You can edit only their address in the profile section, so be careful while creating the profile."
            helpBinding.loginAndProfileText.text =
                getString(R.string.login_and_profile, loginAndProfile)
            helpBinding.loginAndProfileText.visibility = View.VISIBLE
            helpBinding.loginAndProfile.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.sell.setOnClickListener {
            val sell =
                "1. You can add your own product which you want to sell. To do so, you need to provide product name, " +
                        "(if possible try to keep the product name unique) category, min-max price, description(optional), 4 product images." +
                        "\n2. You can see all the product you added for selling in the sell section." +
                        "\n3. In case you want to delete the added product, you can do so by clicking on the particular product item and remove " +
                        "product button will be shown at the bottom of the product details screen." +
                        "\n4. Whenever any user will show interest in your product then, that will be listed in sell section of WishKart area." +
                        "\n5. you can mark the product as sold or not interested as per your choice, after discussing personally by " +
                        "contacting each other with the given contact details."
            helpBinding.sellText.text = getString(R.string.sell_product, sell)
            helpBinding.sellText.visibility = View.VISIBLE
            helpBinding.sell.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.buy.setOnClickListener {
            val buy =
                "1. You can see the list of available products in the buy section, and can see their details by clicking over them. " +
                        "You can also search for the product you want by using search option." +
                        "\n2. You can show interest to buy products by clicking on interested button in the product detail screen." +
                        "\n3. In whichever product you have shown your interest that will be listed in buy section of the WishKart area." +
                        "\n4. You can mark the product as not interested whenever you change your mind to not to buy that product." +
                        "\n5. You can contact seller and can buy the product." +
                        "\n6. You will be notified when you come back to that product details page, if the product is sold to other user, " +
                        "seller is not interested to sell that product to you or if sold to you."
            helpBinding.buyText.text = getString(R.string.buy_product, buy)
            helpBinding.buyText.visibility = View.VISIBLE
            helpBinding.buy.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.donate.setOnClickListener {
            val donate =
                "1. You can add your own product which you want to donate. To do so, you need to provide product name, " +
                        "(if possible try to keep the product name unique) category, description(optional), 4 product images." +
                        "\n2. You can see all the product you added for donating in the donate section." +
                        "\n3. In case you want to delete the added product, you can do so by clicking on the particular product item and remove " +
                        "product button will be shown at the bottom of the product details screen." +
                        "\n4. Whenever any any NGO will show interest in your product then, that will be listed in donate section of WishKart area." +
                        "\n5. You can mark the product as donated or not interested as per your choice, after discussing personally by " +
                        "contacting each other with the given contact details."
            helpBinding.donateText.text = getString(R.string.donate_product, donate)
            helpBinding.donateText.visibility = View.VISIBLE
            helpBinding.donate.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.donee.setOnClickListener {
            val donee =
                "1. You can see the list of available products in the donations section in the donations area, and can see their " +
                        "details by clicking over them. You can also search for the product you want by using search option." +
                        "\n2. You can show interest in products by clicking on interested button in the product detail screen." +
                        "\n3. In whichever product you have shown your interest that will be listed in interest section of the donations area." +
                        "\n4. You can mark the product as not interested whenever you change your mind to not to take that product." +
                        "\n5. You can contact donor and can get the product." +
                        "\n6. You will be notified when you come back to that product details page, if the product is donated to other NGO, " +
                        "donor is not interested to donate that product to you or if donated to you."
            helpBinding.doneeText.text = getString(R.string.donee, donee)
            helpBinding.doneeText.visibility = View.VISIBLE
            helpBinding.donee.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.note.setOnClickListener {
            val note =
                "The WishKart doest not provide any payment option to buy, sell products. You are solely responsible for selling, " +
                        "buying and donating products. You can contact seller, buyer and NGO by the given contact number and verify the NGO " +
                        "via their Certificate number. WishKart just provide you a platform where you can show the products to others that you " +
                        "want to sell and donate products which your are not using but someone else can use, as this will help need-ies a lot. " +
                        "and buy second hand or refurbished products. So, WishKart not responsible for anything, you need to take care of " +
                        "payments and other security or fraud related issues."
            helpBinding.noteText.text = note
            helpBinding.noteText.visibility = View.VISIBLE
            helpBinding.note.setBackgroundResource(R.drawable.background_above_doc)
        }
        helpBinding.contactUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:theankitsingh2001@gmail.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "")
            intent.putExtra(Intent.EXTRA_TEXT, "")
            try {
                startActivity(Intent.createChooser(intent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    requireContext(),
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return helpBinding.root
    }

    private fun checkUserType() {
        ref.get().addOnSuccessListener {
            if (it.exists()) {
                if (auth.uid == it.get("uid")) {
                    userType = it.getString("userType").toString()
                    if (userType == "Individual") {
                        helpBinding.donee.visibility = View.GONE
                    }
                    if (userType == "NGO") {
                        helpBinding.donee.visibility = View.VISIBLE
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Something went wrong, Please try again!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
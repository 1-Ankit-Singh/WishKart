package com.androidproject.wishkart.ui.donate

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.MainActivity
import com.androidproject.wishkart.databinding.ActivityDonateBinding
import com.androidproject.wishkart.model.ProductDonate
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hbb20.R

class DonateActivity : AppCompatActivity() {
    // Initializing Variables
    private lateinit var donateBinding: ActivityDonateBinding
    private var categoryOptions = arrayListOf<Any>(
        "Select Category",
        "Apparel or Clothes",
        "Fashion",
        "Electronic Products or Gadgets",
        "Business Supply",
        "Mobile Phones",
        "Bags",
        "Cosmetics",
        "Spices and Edible Items",
        "Art & Craft",
        "Education",
        "Jewellery",
        "Books",
        "Shoes",
        "Furniture",
        "Bedding Items",
        "Utensils",
        "Kitchen Appliances",
        "Homemade Perfumes",
        "Greeting Cards",
        "Lights and Bulbs",
        "Handmade Toys",
        "Toys",
        "Watches",
        "Fitness Products",
        "Desktop or Laptop",
        "Paper Products",
        "Toddler Items"
    )
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseFirestore.getInstance()
    private var flag: String = "0"
    private var productOwnerCity: String = ""
    private var productOwnerPinCode: String = ""
    private var productOwnerCountry: String = ""
    private var productName: String = ""
    var productCategory: String = "None"
    private var productDescription: String = "Description is not provided"
    private var productUrl1: String = ""
    private var productUrl2: String = ""
    private var productUrl3: String = ""
    private var productUrl4: String = ""
    private var productStatus: String = "Available"
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        donateBinding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(donateBinding.root)
        setSupportActionBar(donateBinding.toolbarDonate)

        getProductOwnerDetails()
        val categoryAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categoryOptions)
        donateBinding.category.adapter = categoryAdapter
        donateBinding.category.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    productCategory = categoryOptions[position].toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //ToDo
                }

            }

        donateBinding.uploadImage1.setOnClickListener {
            flag = "1"
            checkPermissionForImage()
        }
        donateBinding.uploadImage2.setOnClickListener {
            flag = "2"
            checkPermissionForImage()
        }
        donateBinding.uploadImage3.setOnClickListener {
            flag = "3"
            checkPermissionForImage()
        }
        donateBinding.uploadImage4.setOnClickListener {
            flag = "4"
            checkPermissionForImage()
        }

        donateBinding.submit.setOnClickListener {
            uploadProduct()
        }
    }

    private fun uploadProduct() {
        productName = donateBinding.productName.text.toString()
        productDescription = donateBinding.productDescription.text.toString()
        when {
            productName.isEmpty() -> {
                Toast.makeText(this, "Please enter product name.", Toast.LENGTH_SHORT).show()
            }
            productCategory.isEmpty() -> {
                Toast.makeText(this, "Please select product category.", Toast.LENGTH_SHORT).show()
            }
            (productUrl1.isEmpty() or productUrl2.isEmpty() or productUrl3.isEmpty() or productUrl4.isEmpty()) -> {
                Toast.makeText(this, "Please upload 4 product pictures.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val productDonate = ProductDonate(
                    auth.uid!!,
                    productOwnerCity,
                    productOwnerPinCode,
                    productOwnerCountry,
                    productName,
                    productCategory,
                    productDescription,
                    productUrl1,
                    productUrl2,
                    productUrl3,
                    productUrl4,
                    productStatus
                )
                database.collection("users/${auth.uid.toString()}/donate")
                    .document("$productCategory$productName").set(productDonate)
                    .addOnSuccessListener {
                        addProductToDonateList(productDonate)
                    }.addOnFailureListener {
                        Toast.makeText(
                            this,
                            "Something went wrong, Please try again !!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

    private fun getProductOwnerDetails() {
        database.collection("users").document(auth.uid!!).get()
            .addOnSuccessListener {
                productOwnerCity = it.getString("userCity").toString()
                productOwnerPinCode = it.getString("userPinCode").toString()
                productOwnerCountry = it.getString("userCountry").toString()
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun addProductToDonateList(productDonate: ProductDonate) {
        database.collection("donate")
            .document(auth.uid.toString()).set(productDonate)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                Toast.makeText(
                    this,
                    "Product Successfully added",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Something went wrong, Please try again !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun checkPermissionForImage() {
        if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        ) {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            val permissionWrite = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(
                permission,
                1001
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001
            requestPermissions(
                permissionWrite,
                1002
            ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_WRITE LIKE 1002
            Toast.makeText(
                this,
                "Please click on the certificate icon again to upload the certificate.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            1000
        ) // GIVE AN INTEGER VALUE FOR IMAGE_PICK_CODE LIKE 1000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1000) {
            data?.data?.let {
                when (flag) {
                    "1" -> {
                        donateBinding.uploadImage1.setImageURI(it)
                    }
                    "2" -> {
                        donateBinding.uploadImage2.setImageURI(it)
                    }
                    "3" -> {
                        donateBinding.uploadImage3.setImageURI(it)
                    }
                    "4" -> {
                        donateBinding.uploadImage4.setImageURI(it)
                    }
                }
                startUpload(it)
            }
        }
    }

    private fun startUpload(filePath: Uri) {
        productName = donateBinding.productName.text.toString()
        progressDialog = createProgressDialog()
        progressDialog.show()
        val ref =
            storage.reference.child("donate/${auth.uid.toString()}/" + "$productCategory$productName$flag")
        val uploadTask = ref.putFile(filePath)
        uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            return@Continuation ref.downloadUrl
        }).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                when (flag) {
                    "1" -> {
                        productUrl1 = task.result.toString()
                    }
                    "2" -> {
                        productUrl2 = task.result.toString()
                    }
                    "3" -> {
                        productUrl3 = task.result.toString()
                    }
                    "4" -> {
                        productUrl4 = task.result.toString()
                    }
                }
                progressDialog.dismiss()
            } else {
                // Handle failures
                Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun createProgressDialog(): ProgressDialog {
        return ProgressDialog(this).apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setMessage("Uploading Image...")
        }
    }
}
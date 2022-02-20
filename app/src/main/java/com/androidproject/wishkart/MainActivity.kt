package com.androidproject.wishkart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.androidproject.wishkart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Initializing Variables
    private lateinit var mainActivity: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainActivity.root)
    }
}
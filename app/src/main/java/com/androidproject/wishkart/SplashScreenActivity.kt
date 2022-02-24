package com.androidproject.wishkart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {
    // Initializing Variables
    private var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (auth.currentUser == null) {
            startActivity(Intent(this, IntroActivity::class.java))
        } else {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        }*/
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }
}
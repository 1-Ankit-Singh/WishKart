package com.androidproject.wishkart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidproject.wishkart.auth.LoginActivity
import com.androidproject.wishkart.auth.UserTypeActivity
import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.github.appintro.model.SliderPagerBuilder

class IntroActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showIntroSlides()
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        goToLogin()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToLogin()
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        Log.d("Intro", "Changed")
    }

    private fun goToLogin(){
        startActivity(Intent(this, UserTypeActivity::class.java))
        finish()
    }

    private fun showIntroSlides(){
        val introOne = SliderPagerBuilder()
            .title("Welcome")
            .description("Welcome to WishKart\n\nSell, purchase, donate and exchange items\nwithout any hassle")
            .imageDrawable(R.drawable.logo)
            .backgroundColorRes(R.color.pink_light)
            .build()
        val introTwo = SliderPagerBuilder()
            .title("Sell")
            .description("Sell any item")
            .imageDrawable(R.drawable.sell)
            .backgroundColorRes(R.color.purple_light)
            .build()
        val introThree = SliderPagerBuilder()
            .title("Buy")
            .description("Buy available items")
            .imageDrawable(R.drawable.buy)
            .backgroundColorRes(R.color.pink_light)
            .build()
        val introFour = SliderPagerBuilder()
            .title("Donate")
            .description("Donate to NGOs")
            .imageDrawable(R.drawable.donate)
            .backgroundColorRes(R.color.purple_light)
            .build()
        val introFive = SliderPagerBuilder()
            .title("Barter")
            .description("Exchange Items")
            .imageDrawable(R.drawable.barter)
            .backgroundColorRes(R.color.pink_light)
            .build()
        addSlide(AppIntroFragment.createInstance(introOne))
        addSlide(AppIntroFragment.createInstance(introTwo))
        addSlide(AppIntroFragment.createInstance(introThree))
        addSlide(AppIntroFragment.createInstance(introFour))
        addSlide(AppIntroFragment.createInstance(introFive))
        showStatusBar(true)
        setTransformer(AppIntroPageTransformerType.Fade)
        isColorTransitionsEnabled = true
    }
}
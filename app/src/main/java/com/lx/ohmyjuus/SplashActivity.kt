package com.lx.ohmyjuus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation)
        val bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        val topTextView = findViewById(R.id.topTextView)as TextView
        val splashLogo = findViewById(R.id.splashLogo)as ImageView
        val bottomTextView = findViewById(R.id.bottomTextView)as TextView

        topTextView.startAnimation(topAnimation)
        splashLogo.startAnimation(middleAnimation)
        bottomTextView.startAnimation(bottomAnimation)

        val splashScreenTimeOut = 3000
        val homeIntent = Intent(this@SplashActivity, LoginActivity::class.java)

        Handler().postDelayed({
            startActivity(homeIntent)
            finish()
        }, splashScreenTimeOut.toLong())
    }
}
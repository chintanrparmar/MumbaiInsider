package com.crp.mumbaiinsider.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.crp.mumbaiinsider.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }, 1000)
    }
}
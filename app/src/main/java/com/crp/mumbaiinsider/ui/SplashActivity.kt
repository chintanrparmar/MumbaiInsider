package com.crp.mumbaiinsider.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.edit
import com.crp.mumbaiinsider.R

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences =
            getSharedPreferences(getString(R.string.theme_mode), Context.MODE_PRIVATE)


        val currentTheme = sharedPreferences.getInt(getString(R.string.theme_mode), 0)

        //checking if theme value already set.
        //If not will update current theme by checking current device theme.
        if (currentTheme == 0) {
            changeTheme()
        } else {
            AppCompatDelegate.setDefaultNightMode(currentTheme)
        }

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 1000)
    }

    private fun changeTheme() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                sharedPreferences.edit { putInt(getString(R.string.theme_mode), MODE_NIGHT_NO) }

            } // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                sharedPreferences.edit { putInt(getString(R.string.theme_mode), MODE_NIGHT_YES) }

            } // Night mode is active, we're using dark theme
            else -> {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                sharedPreferences.edit { putInt(getString(R.string.theme_mode), MODE_NIGHT_NO) }

            } //Default Night Mode
        }
    }
}